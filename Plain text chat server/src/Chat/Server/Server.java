package Chat.Server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import Chat.Message.Message;

public class Server {
    private int port;
    private ArrayList<Communication> clients;
    private int clientId;
    private SimpleDateFormat time;

    public Server() {}

    public Server(int port) {
        this.port = port;
        clients = new ArrayList<Communication>();
        this.clientId = 0;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    // 广播消息
    private synchronized void broadcastClients(String message) {
        Message msg = new Message(time.format(new Date()) + "\n" + message + "\n\n");
        for (Communication client : clients) {
            client.sendMessage(msg);
        }
    }

    // 删除断开的Client
    private synchronized void removeClient(int id) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).id == id) {
                clients.remove(i);
                return;
            }
        }
    }

    // 子线程
    private class Communication extends Thread {
        int id;								// 线程id
        Socket socket;
        ObjectInputStream socketInputStream;
        ObjectOutputStream socketOutputStream;
        String userName;					// 客户端名
        Message clientMessage;				// 客户端发送的消息

        public Communication() {}

        public Communication(Socket socket) {
            id = ++clientId;
            this.socket = socket;
            try {
                socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
                socketInputStream = new ObjectInputStream(socket.getInputStream());
                userName = (String) socketInputStream.readObject();
            }
            catch (IOException e) {
                System.err.println(time.format(new Date()) + "创建Socket的I/O流时出错！ " + e);
            }
            catch (ClassNotFoundException ignored) {
            }
        }

        // 子线程运行这个
        @Override
        public void run() {
            while (true) {
                try {
                    clientMessage = (Message)socketInputStream.readObject();
                    if (clientMessage.getContent().equals("quit"))
                        break;
                }
                catch (IOException | ClassNotFoundException e) {
                    break;
                }
                broadcastClients(userName + " 说: \n" + clientMessage.getContent());
            }
            // 循环退出，代表该子线程对应的客户端关闭了连接
            removeClient(id);
            broadcastClients(userName + " 已下线");
            System.out.println(userName + " 断开了连接，当前在线人数为：" + clients.size());
        }

        private void sendMessage(Message msg) {
            if (!socket.isConnected()) {
                return;
            }

            try {
                socketOutputStream.writeObject(msg);
            }
            catch(IOException e) {
                System.err.println("发送消息给" + userName + "时发生异常" + e);
            }
        }
    }

    // 主线程
    public void listen() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("正在监听端口" + port + " ...");
            // accept循环
            while (true) {
                Socket socket = serverSocket.accept();
                Communication subThread = new Communication(socket);    // 接收后创建子线程，子线程掌管该客户后续操作
                clients.add(subThread);
                subThread.start();
                broadcastClients(subThread.userName + " 已连接");
                System.out.println(time.format(new Date()) + " " + subThread.userName + " 连接到服务器，当前在线人数为：" + clients.size());
            }
        }
        catch (IOException e) {
            System.err.println(time.format(new Date()) + " 创建ServerSocket出错！: " + e);
        }
    }
}