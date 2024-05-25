package com.example.git.server;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

public class ClientSomthing {
    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток чтения в сокет
    private String addr; // ip адрес клиента
    private int port; // порт соединения


    public ClientSomthing(String addr, int port) {
        this.addr = addr;
        this.port = port;
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
                    javafx.application.Platform.runLater(() -> {

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
