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
            System.out.println(RESOURCE_BUNDLE.getValue("mainMenu"));

            messageInChat(messageConsoleReceiver, messageSender);

//            while ((messageFromConsole = messageConsoleReceiver.readMessage()) != null) {
//                System.out.println("MENU");
//
//                if (messageFromConsole.startsWith("меню")) {
//                    int num = Integer.parseInt(messageFromConsole.substring(4).trim());
//                    menu(num, messageSender);
//                }
//
//                messageSender.sendMessage(messageFromConsole);
//            }
        }
    }

    public void messageInChat(MessageReceiver messageConsoleReceiver, MessageSender messageSender) {
        String messageFromConsole;

        while ((messageFromConsole = messageConsoleReceiver.readMessage()) != null) {
            System.out.println("--------");
            if ((messageFromConsole.startsWith("м1")) || (messageFromConsole.startsWith("m1"))) {
                System.out.println("|     m0    |" + "\n"
                        + "| Exit chat |");
                System.out.println("Chat:");
//                MessageReceiver message = new MessageReceiver(System.in);
//                String mess;
                while (!(messageFromConsole = messageConsoleReceiver.readMessage()).contains("m0")) {
                    messageSender.sendMessage(messageFromConsole);
                    createMessage(messageConsoleReceiver, messageSender);
                }
                System.out.println("exit chat");
//                System.exit(0);

            } else if ((messageFromConsole.startsWith("м2")) || (messageFromConsole.startsWith("m2"))) {
                setLogin(messageConsoleReceiver, messageSender);
            } else if ((messageFromConsole.startsWith("м3")) || (messageFromConsole.startsWith("m3"))) {
                setPassword(messageConsoleReceiver, messageSender);
            } else if ((messageFromConsole.startsWith("м4")) || (messageFromConsole.startsWith("m4"))) {
                delete(messageConsoleReceiver, messageSender);
            } else if ((messageFromConsole.startsWith("м5")) || (messageFromConsole.startsWith("m5"))) {
                System.exit(0);
            }
            System.out.println(RESOURCE_BUNDLE.getValue("mainMenu"));


//            if ((messageFromConsole.startsWith("меню")) || (messageFromConsole.startsWith("menu"))) {
////                int num = Integer.parseInt(messageFromConsole.substring(4).trim());
//                System.out.println("менюююю");
////                menu(num, messageSender);
//            }

        }

    }


    public static void printMenu() {
        System.out.println(RESOURCE_BUNDLE.getValue("mainMenu"));
    }

//    public void menu(int numMenu, MessageSender messageSender) {
//        MessageReceiver messageConsoleReceiver = new MessageReceiver(System.in);
//        System.out.print("\nВведите номер меню:");
//
//        printMenu();
//        if (numMenu == 1) {
////            createMessage(messageConsoleReceiver, messageSender);
//
//
//        } else if (numMenu == 2) {
//
//        } else if (numMenu == 3) {
//            setLogin(messageConsoleReceiver, messageSender);
//        } else if (numMenu == 4) {
//            setPassword(messageConsoleReceiver, messageSender);
//        } else if (numMenu == 5) {
//            delete(messageConsoleReceiver, messageSender);
//        } else if (numMenu == 0) {
//            System.exit(0);
//        }
//
//
//    }

    public void createMessage(MessageReceiver messageReceiver, MessageSender messageSender) {
//        System.out.println(RESOURCE_BUNDLE.getValue("createMessage"));
//        String text = getString(messageReceiver, RESOURCE_BUNDLE.getValue("enterText")).toUpperCase();
        String text = getText(messageReceiver).toUpperCase();
//        messageSender.sendMessage(RESOURCE_BUNDLE.getValue("createMessage") + text);
        messageSender.sendMessage("->" + text);

    }


//    public static void createMessage(String message, MessageSender messageSender) {
////        System.out.println(RESOURCE_BUNDLE.getValue("createMessage"));
////        String text = getText(messageReceiver).toUpperCase();
//        messageSender.sendMessage("->" + message);

//    }


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

    private String getText(MessageReceiver messageReceiver) {
//        System.out.println(userHint);
        return messageReceiver.readMessage().trim();
    }


}
