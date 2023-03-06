package Chat.Client;

import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.Scanner;

import Chat.Message.Message;

public class Client {
    private ObjectInputStream socketInputStream;
    private ObjectOutputStream socketOutputStream;
    private String serverIP;
    private int port;
    private String userName;
    private Socket cliSocket;

    private class Communication extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println(((Message)socketInputStream.readObject()).getContent());
                }
                catch (Exception e) {
                    System.err.println("与服务器的连接已断开！");
                    break;
                }
            }
        }
    }

    private synchronized void sendMessage(Message msg) {
        try {
            socketOutputStream.writeObject(msg);
        }
        catch (Exception e) {
            System.err.println("消息发送过程出错！");
        }
    }

    private class SendMessage extends Thread {
        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String msg = sc.nextLine();
                if (msg.isEmpty()) {
                    System.out.println("不能发送空消息！");
                    continue;
                }
                if (msg.equals("quit")) {
                    sendMessage(new Message(msg));
                    break;
                }
                sendMessage(new Message(msg));
            }
        }
    }

    public Client() {}

    public Client(String serverIP, int port, String userName) {
        this.serverIP = serverIP;
        this.port = port;
        this.userName = userName;
    }

    public boolean connectServer() {
        try {
            cliSocket = new Socket(serverIP, port);
        }
        catch (Exception e) {
            System.err.println("连接服务器失败！");
            return false;
        }
        System.out.println("连接服务器成功！");

        try {
            socketInputStream = new ObjectInputStream(cliSocket.getInputStream());
            socketOutputStream = new ObjectOutputStream(cliSocket.getOutputStream());
        }
        catch (Exception e) {
            System.err.println("创建套接字的I/O流时出错！ " + e);
            return false;
        }

        try {
            socketOutputStream.writeObject(userName);
        }
        catch (IOException e) {
            System.err.println("发送消息时出错！ " + e);
            return false;
        }

        new Communication().start();
        Thread t = new SendMessage();
        t.start();
        try {
            t.join();
        }
        catch (InterruptedException e) {
            System.err.println("线程join失败！" + e);
            return false;
        }
        return true;
    }

    public void close() throws IOException {
        this.cliSocket.close();
    }
}