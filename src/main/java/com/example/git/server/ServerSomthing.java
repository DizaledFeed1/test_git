package com.example.git.server;

import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;


class ServerSomthing extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом,
    // кроме него - клиент и сервер никак не связаны
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток завписи в сокет

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        // сооюбщений новому поключению
        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word;
        try {
            while (true) {
                word = in.readLine();
                if (word.equals("stop")) {
                    downService();
                }else if (word.equals("connect")) {
                    try {
                        System.out.println("Echoing: " + word);
                        for (ServerSomthing vr : TCPServer.serverList) {
                            vr.send(String.valueOf(TCPServer.serverList)); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                        }
                    } catch (NullPointerException ignored) {}
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }

    /**
     * закрытие сервера
     * прерывание себя как нити и удаление из списка нитей
     */
    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : TCPServer.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    TCPServer.serverList.remove(this);
                }
                for (ServerSomthing vr : TCPServer.serverList) {
                    vr.send(String.valueOf(TCPServer.serverList)); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                }
            }
        } catch (IOException ignored) {}
    }
}
