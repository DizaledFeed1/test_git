package com.example.git.server;

import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ClientSomthing {
    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток чтения в сокет
    private String addr; // ip адрес клиента
    private int port; // порт соединения
    private ListView<String> serverList;


    public ClientSomthing(String addr, int port, ListView<String> serverList) {
        this.addr = addr;
        this.port = port;
        this.serverList = serverList;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
        } catch (IOException e) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            ClientSomthing.this.downService();
        }
        // В противном случае сокет будет закрыт
        // в методе run() нити.
    }
    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                out.write("stop\n");
                out.flush();
                downService();
            }
        } catch (IOException ignored) {}
    }
    public void downService() {
        try {
            if (!socket.isClosed()) {
                out.write("stop\n");
                serverList.getItems().clear();
                out.flush();
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }


    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера
                    if (str.equals("stop")) {
                        ClientSomthing.this.downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    }
                    String[] clients = str.split(" "); // Разделить строку по пробелам
                    Platform.runLater(() -> {
                        serverList.getItems().clear(); // Очистить список перед добавлением новых клиентов
                        for (String client : clients) {
                            serverList.getItems().add(client); // Добавить каждого клиента в список отдельно
                        }
                    });
                }
            } catch (IOException e) {
                ClientSomthing.this.downService();
            }
        }
    }

    // нить отправляющая сообщения приходящие с консоли на сервер
    public class WriteMsg extends Thread {
        @Override
        public void run() {
            String userWord;
            try {
                userWord = "connect"; // сообщения с консоли
                out.write(userWord + "\n"); // отправляем на сервер
                out.flush();
            } catch (IOException e) {
                ClientSomthing.this.downService(); // в случае исключения тоже харакири
            }
        }
    }
}
