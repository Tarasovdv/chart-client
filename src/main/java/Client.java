import lombok.SneakyThrows;
import utils.MyResourceBundle;
import utils.Props;

import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;

public class Client {
    private final static String IP = "localhost";
    private final static int PORT = 8080;
    private final static MyResourceBundle RESOURCE_BUNDLE = new MyResourceBundle(
            new Locale(Props.getValue("language"), Props.getValue("country")));


    @SneakyThrows
    public void start() {
        Socket socket = new Socket(IP, PORT);

        if (socket.isConnected()) {
            MessageReceiver messageConsoleReceiver = new MessageReceiver(System.in);
            MessageSender messageSender = new MessageSender(socket.getOutputStream());

            registrationOrAuthorization(messageConsoleReceiver, messageSender);

            new Thread(new SockedRunnable(socket)).start();
            String messageFromConsole;

//            menu(messageConsoleReceiver, messageSender);

            while ((messageFromConsole = messageConsoleReceiver.readMessage()) != null) {

                if (messageFromConsole.startsWith("меню")) {
                    int num = Integer.parseInt(messageFromConsole.substring(4).trim());
                    menu(num, messageSender);
                }

                messageSender.sendMessage(messageFromConsole);
            }
        }
    }

    public static void printMenu() {
        System.out.println(RESOURCE_BUNDLE.getValue("mainMenu"));
    }

    public void menu(int numMenu, MessageSender messageSender) {
        MessageReceiver messageConsoleReceiver = new MessageReceiver(System.in);
        System.out.print("\nВведите номер меню:");

        printMenu();
        if (numMenu == 1) {
            createMessage(messageConsoleReceiver, messageSender);


        } else if (numMenu == 2) {

        } else if (numMenu == 3) {
            setLogin(messageConsoleReceiver, messageSender);
        } else if (numMenu == 4) {
            setPassword(messageConsoleReceiver, messageSender);
        } else if (numMenu == 5) {
            delete(messageConsoleReceiver, messageSender);
        } else if (numMenu == 0) {
            System.exit(0);
        }



    }

    public void createMessage(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println(RESOURCE_BUNDLE.getValue("createMessage"));
        String text = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterText")).toUpperCase();
        messageSender.sendMessage(RESOURCE_BUNDLE.getValue("createMessage") + text);

    }


    public void registrationOrAuthorization(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println("-----------------------");
        System.out.println(RESOURCE_BUNDLE.getValue("welcome"));
        System.out.println("-----------------------");
        System.out.println(RESOURCE_BUNDLE.getValue("menuRegAuth"));

        int numFromConsole = Integer.parseInt(messageReceiver.readMessage().trim());

        if (numFromConsole == 1) {
            System.out.println(RESOURCE_BUNDLE.getValue("authorization"));
            String name = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterName")).toUpperCase();
            String password = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterPassword"));
            messageSender.sendMessage(RESOURCE_BUNDLE.getValue("authorization") + name + " " + password);
        } else if (numFromConsole == 2) {
            System.out.println(RESOURCE_BUNDLE.getValue("registration"));
            String name = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterName")).toUpperCase();
            String password = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterPassword"));
            messageSender.sendMessage(RESOURCE_BUNDLE.getValue("registration") + name + " " + password);
        } else {
            System.err.println(RESOURCE_BUNDLE.getValue("enter") + "1 "
                    + RESOURCE_BUNDLE.getValue("or") + " 2");
            registrationOrAuthorization(messageReceiver, messageSender);
        }
    }

    public void delete(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println(RESOURCE_BUNDLE.getValue("delete"));
        String name = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterName")).toUpperCase();
        String password = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterPassword"));
        messageSender.sendMessage(RESOURCE_BUNDLE.getValue("delete") + name + " " + password);

    }

    public void setLogin(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println(RESOURCE_BUNDLE.getValue("changeName"));
        String oldName = getString(messageReceiver, RESOURCE_BUNDLE.getValue("oldName")).toUpperCase();
        String newName = getString(messageReceiver, RESOURCE_BUNDLE.getValue("newName")).toUpperCase();
        messageSender.sendMessage(RESOURCE_BUNDLE.getValue("changeName") + oldName + " " + newName);

    }

    public void setPassword(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println(RESOURCE_BUNDLE.getValue("changePassword"));
        String name = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterName")).toUpperCase();
        String oldPassword = getString(messageReceiver, RESOURCE_BUNDLE.getValue("oldPassword")).toUpperCase();
        String newPassword = getString(messageReceiver, RESOURCE_BUNDLE.getValue("newPassword")).toUpperCase();
        messageSender.sendMessage(RESOURCE_BUNDLE.getValue("changePassword") + name + " " + oldPassword + " " + newPassword);

    }


    private String getString(MessageReceiver messageReceiver, String userHint) {
        System.out.println(userHint);
        return messageReceiver.readMessage().trim();
    }


}
