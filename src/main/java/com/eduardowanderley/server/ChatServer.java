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

public class ChatServer extends WebSocketServer {
    private final Set<WebSocket> connections;
    private final Gson gson;

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
        this.connections = new HashSet<>();
        this.gson = new GsonBuilder()
            .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        System.out.println("Nova conexão: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        System.out.println("Conexão fechada: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Message chatMessage = gson.fromJson(message, Message.class);
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