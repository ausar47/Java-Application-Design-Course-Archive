package Chat.Client;

import Chat.Message.Message;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Client client;
        while (true) {
            System.out.print("请输入IP地址：");
            String serverIP = sc.nextLine();
            System.out.print("请输入端口号：");
            int port = sc.nextInt();
            sc.nextLine();
            System.out.print("请输入用户名：");
            String userName = sc.nextLine();
            client = new Client(serverIP, port, userName);
            if (client.connectServer())
                break;
            else
                System.out.println("请重新输入正确的IP地址和端口号！");
        }

//        while (true) {
//            String msg = sc.nextLine();
//            if (msg.isEmpty()) {
//                System.out.println("不能发送空消息！");
//                continue;
//            }
//            if (msg.equals("quit")) {
//                client.sendMessage(new Message(msg));
//                break;
//            }
//            client.sendMessage(new Message(msg));
//        }

        try {
            client.close();
        }
        catch (IOException e) {
            System.err.println("Socket关闭失败！");
        }
    }
}