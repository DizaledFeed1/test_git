package com.example.git.server;

import com.example.git.transports.Transport;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.stream.Collectors;

class ServerSomthing extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет
    private int clientId; // уникальный идентификатор клиента

    public ServerSomthing(Socket socket, int clientId) throws IOException {
        this.socket = socket;
        this.clientId = clientId;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write("ID:" + clientId + "\n"); // отправка уникального идентификатора клиенту
        out.flush();
        start(); // вызываем run()
    }

    @Override
    public void run() {
        String word;
        try {
            while (true) {
                word = in.readLine();
                if (word == null || word.equals("stop")) {
                    downService();
                    break;
                } else if (word.equals("connect")) {
                    sendAllClients();
                } else {
                    sendCars();
                }
            }
        } catch (IOException e) {
            downService(); // Обработка исключений
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    private void sendAllClients() {
        String clients = TCPServer.serverList.stream()
                .map(ServerSomthing::getClientInfo)
                .collect(Collectors.joining(" "));
        for (ServerSomthing vr : TCPServer.serverList) {
            vr.send(clients);
        }
    }

    private String getClientInfo() {
        return clientId + ":" + socket.getInetAddress().toString();
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
        synchronized (TCPServer.serverList) {
            TCPServer.serverList.remove(this);
            sendAllClients(); // Отправляем обновленный список клиентов
        }
    }
    private void sendCars() {
        try {
            // Создаем поток для чтения сериализованных данных из сокета
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Десериализуем список машин
            ArrayList<Transport> carsList = (ArrayList<Transport>) objectInputStream.readObject();

            // Теперь у вас есть список объектов carsList, который вы можете использовать на сервере
            // Продолжите обработку этого списка по вашему желанию

            // Закрываем потоки
            objectInputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
