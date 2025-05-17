package com.eduardowanderley.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.eduardowanderley.model.Message;
import com.eduardowanderley.util.LocalDateTimeAdapter;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ChatServer extends WebSocketServer {
    private final Set<WebSocket> connections;
    private final Gson gson;
    private final List<Message> messageHistory;
    private static final String HISTORY_FILE = "chat_history.json";

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
        this.connections = new HashSet<>();
        this.gson = new GsonBuilder()
            .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
        this.messageHistory = loadHistory();
    }

    private List<Message> loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Message[] messages = gson.fromJson(reader, Message[].class);
            if (messages != null) return new ArrayList<>(List.of(messages));
        } catch (Exception e) {
            System.err.println("Failed to load chat history: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private void saveHistory() {
        try (Writer writer = new FileWriter(HISTORY_FILE)) {
            gson.toJson(messageHistory, writer);
        } catch (Exception e) {
            System.err.println("Failed to save chat history: " + e.getMessage());
        }
    }

    private void sendHistory(WebSocket conn) {
        try {
            for (Message msg : messageHistory) {
                conn.send(gson.toJson(msg));
            }
        } catch (Exception e) {
            System.err.println("Failed to send chat history: " + e.getMessage());
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        System.out.println("Nova conexão: " + conn.getRemoteSocketAddress());
        sendHistory(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        System.out.println("Conexão fechada: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Message chatMessage = gson.fromJson(message, Message.class);
        messageHistory.add(chatMessage);
        saveHistory();
        broadcast(gson.toJson(chatMessage));
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Erro na conexão: " + ex.getMessage());
        if (conn != null) {
            connections.remove(conn);
        }
    }

    @Override
    public void onStart() {
        System.out.println("Servidor iniciado na porta: " + getPort());
    }

    public static void main(String[] args) {
        int port = 8887;
        ChatServer server = new ChatServer(port);
        server.start();
    }
} 