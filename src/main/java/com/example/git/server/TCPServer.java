package com.example.git.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer {
    private static TCPServer instance; // Статическая переменная для хранения единственного экземпляра сервера
    private static CopyOnWriteArrayList<ClientInfo> value = new CopyOnWriteArrayList<>(); // Список для хранения информации о подключенных клиентах
    private ServerSocket serverSocket;

    // Приватный конструктор, чтобы предотвратить создание экземпляров извне
    private TCPServer() throws IOException {
        boolean success = false;
        int port = 8080;
        while (!success) {
            try {
                serverSocket = new ServerSocket(port);
                success = true;
            } catch (BindException e) {
                System.out.println("Port " + port + " is already in use. Trying another port.");
                port++; // Пробуем следующий порт
            }
        }
    }
    // Метод для получения единственного экземпляра сервера
    public static synchronized TCPServer getInstance() throws IOException {
        if (instance == null) {
            instance = new TCPServer();
        }
        return instance;
    }
    // Метод для запуска сервера
    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientInfo clientInfo = new ClientInfo(clientSocket); // Создаем объект ClientInfo для каждого подключенного клиента
                value.add(clientInfo); // Добавляем информацию о клиенте в список
                new Thread(new ClientHandler(clientSocket, clientInfo, this)).start(); // Создаем новый поток для обработки запросов клиента
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Закрытие сервера
    public void close() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    // Метод для получения списка информации о подключенных клиентах
    public CopyOnWriteArrayList<ClientInfo> getValue() {
        return value;
    }

    public String getValueAsString() {
        StringBuilder sb = new StringBuilder();
        for (ClientInfo info : value) {
            sb.append(info.getClientAddress()).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            TCPServer server = TCPServer.getInstance();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ClientInfo clientInfo;
    private final TCPServer server;

    public ClientHandler(Socket socket, ClientInfo clientInfo, TCPServer server) {
        this.clientSocket = socket;
        this.clientInfo = clientInfo;
        this.server = server;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("GET_VALUE".equals(inputLine)) {
                    // Отправляем значение value клиенту
                    out.println(server.getValueAsString());
                } else {
                    System.out.println("Received message from " + clientInfo.getClientAddress() + ": " + inputLine);
                    out.println("Response from server");
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

 class ClientInfo {
    private final Socket clientSocket;

    public ClientInfo(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getClientAddress() {
        return clientSocket.getInetAddress().toString();
    }

    // Другие методы и данные о клиенте, если необходимо
}
