package Chat.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("请输入端口号：");
        Scanner sc = new Scanner(System.in);
        int port = sc.nextInt();
        Server server = new Server(port);
        server.listen();
    }
}