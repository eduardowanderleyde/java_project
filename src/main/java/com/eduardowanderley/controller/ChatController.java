package com.eduardowanderley.controller;

import com.eduardowanderley.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import com.eduardowanderley.util.LocalDateTimeAdapter;

public class ChatController {
    @FXML private TextField usernameField;
    @FXML private TextField roomField;
    @FXML private TextField messageField;
    @FXML private Button connectButton;
    @FXML private Button joinRoomButton;
    @FXML private Button sendButton;
    @FXML private Button fileButton;
    @FXML private ListView<Message> messageList;

    private WebSocketClient client;
    private String currentUsername;
    private String currentRoom;
    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
        .create();
    private final List<Message> messageHistory = new ArrayList<>();

    @FXML
    public void initialize() {
        messageList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setStyle("");
                } else {
                    String time = message.getTimestamp() != null ? message.getTimestamp().toLocalTime().withNano(0).toString() : "";
                    String display = String.format("%s\n%s  %s",
                            message.getContent(),
                            message.getSender(),
                            time
                    );
                    setText(display);
                    // Balão à direita se for do usuário atual, à esquerda se for de outro
                    if (currentUsername != null && message.getSender().equals(currentUsername)) {
                        setStyle("-fx-background-color: #a7f3d0; -fx-background-radius: 12; -fx-alignment: CENTER_RIGHT; -fx-padding: 8 16 8 32;");
                    } else {
                        setStyle("-fx-background-color: #e0e7ef; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 8 32 8 16;");
                    }
                }
            }
        });
    }

    @FXML
    private void handleConnect() {
        currentUsername = usernameField.getText().trim();
        if (currentUsername.isEmpty()) {
            showAlert("Erro", "Por favor, insira seu nome.");
            return;
        }

        try {
            client = new WebSocketClient(new URI("ws://localhost:8887")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Platform.runLater(() -> {
                        connectButton.setDisable(true);
                        usernameField.setDisable(true);
                        showAlert("Sucesso", "Conectado ao servidor!");
                    });
                }

                @Override
                public void onMessage(String message) {
                    Message chatMessage = gson.fromJson(message, Message.class);
                    Platform.runLater(() -> {
                        messageHistory.add(chatMessage);
                        updateMessageList();
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Platform.runLater(() -> {
                        connectButton.setDisable(false);
                        usernameField.setDisable(false);
                        showAlert("Desconectado", "Conexão com o servidor foi encerrada.");
                    });
                }

                @Override
                public void onError(Exception ex) {
                    Platform.runLater(() -> {
                        showAlert("Erro", "Erro na conexão: " + ex.getMessage());
                    });
                }
            };
            client.connect();
        } catch (Exception e) {
            showAlert("Erro", "Não foi possível conectar ao servidor: " + e.getMessage());
        }
    }

    @FXML
    private void handleJoinRoom() {
        currentRoom = roomField.getText().trim();
        if (currentRoom.isEmpty()) {
            showAlert("Erro", "Por favor, insira o nome da sala.");
            return;
        }
        showAlert("Sucesso", "Entrou na sala: " + currentRoom);
    }

    @FXML
    private void handleSendMessage() {
        if (client == null || !client.isOpen()) {
            showAlert("Erro", "Não conectado ao servidor.");
            return;
        }

        String content = messageField.getText().trim();
        if (content.isEmpty()) {
            return;
        }

        Message message = new Message(currentUsername, content, currentRoom, Message.MessageType.TEXT);
        client.send(gson.toJson(message));
        messageField.clear();
    }

    @FXML
    private void handleFileUpload() {
        if (client == null || !client.isOpen()) {
            showAlert("Erro", "Não conectado ao servidor.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um arquivo");
        File file = fileChooser.showOpenDialog(messageField.getScene().getWindow());

        if (file != null) {
            try {
                // Criar diretório para arquivos se não existir
                Path uploadDir = Path.of("uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectory(uploadDir);
                }

                // Copiar arquivo para o diretório de uploads
                Path targetPath = uploadDir.resolve(file.getName());
                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                Message message = new Message(currentUsername, file.getName(), currentRoom, Message.MessageType.FILE);
                message.setFilePath(targetPath.toString());
                client.send(gson.toJson(message));
            } catch (Exception e) {
                showAlert("Erro", "Erro ao enviar arquivo: " + e.getMessage());
            }
        }
    }

    private void updateMessageList() {
        messageList.getItems().clear();
        messageList.getItems().addAll(messageHistory);
        messageList.scrollTo(messageList.getItems().size() - 1);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 