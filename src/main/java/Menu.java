import lombok.SneakyThrows;

import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Menu {
    private final static String IP = "localhost";
    private final static int PORT = 8080;

    @SneakyThrows
    public static void menu(){
        Socket socket = new Socket(IP, PORT);
        MessageReceiver messageConsoleReceiver = new MessageReceiver(System.in);
        MessageSender messageSender = new MessageSender(socket.getOutputStream());
        Client client = new Client();

        Scanner consoleShop = new Scanner(System.in);
        int menuNum;
        printGreeting();
        printMenu();
        while ((menuNum = consoleShop.nextInt()) != 0) {
            if (menuNum == 1) {
                client.autorization(messageConsoleReceiver, messageSender);
            } else if (menuNum == 2) {

            }
            System.out.print("\nВведите номер меню:");
            printMenu();
        }
    }

    public static void printGreeting() {
        System.out.println("\n____________________________________ Добро пожаловать в ЧАТ! ____________________________________");
    }


    public static void printMenu() {
        System.out.println("\nМеню:\n" +
                "|       1       .   0   |\n" +
                "|  Авторизация  | Выход |  ");
    }
}
