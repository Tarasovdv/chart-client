import lombok.SneakyThrows;
import utils.MyResourceBundle;
import utils.Props;

import java.io.BufferedReader;
import java.net.Socket;
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

            registrationOrAutorization(messageConsoleReceiver, messageSender);


//            delete(messageConsoleReceiver, messageSender);
//            setLogin(messageConsoleReceiver, messageSender);
//            setPassword(messageConsoleReceiver, messageSender);

            new Thread(new SockedRunnable(socket)).start();
            String messageFromConsole;

            menu(messageConsoleReceiver, messageSender);

            while ((messageFromConsole = messageConsoleReceiver.readMessage()) != null) {
                messageSender.sendMessage(messageFromConsole);
            }
        }
    }

    public void menu(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println("-----------------------");
        System.out.println(RESOURCE_BUNDLE.getValue("welcomeChat"));
        System.out.println("-----------------------");
        System.out.println(RESOURCE_BUNDLE.getValue("menuChat"));

        int numFromConsole = Integer.parseInt(messageReceiver.readMessage().trim());

            if (numFromConsole == 1) {
                System.out.println("-----------------------");
                System.out.println(RESOURCE_BUNDLE.getValue("welcomeMessage"));
                System.out.println("-----------------------");
                System.out.println(RESOURCE_BUNDLE.getValue("menuMessage"));

                int numFromConsoleMessage = Integer.parseInt(messageReceiver.readMessage().trim());
                if (numFromConsoleMessage == 1) {
                    System.out.println(RESOURCE_BUNDLE.getValue("createMessage"));
                }

            } else if (numFromConsole == 2) {
                System.out.println("-----------------------");
                System.out.println(RESOURCE_BUNDLE.getValue("welcomeSettings"));
                System.out.println("-----------------------");
                System.out.println(RESOURCE_BUNDLE.getValue("menuSettings"));

                int numFromConsoleSettings = Integer.parseInt(messageReceiver.readMessage().trim());
                if (numFromConsoleSettings == 1) {
                    System.out.println(RESOURCE_BUNDLE.getValue("changeName"));
                } else if (numFromConsoleSettings == 2) {
                    System.out.println(RESOURCE_BUNDLE.getValue("changePassword"));
                } else if (numFromConsoleSettings == 3) {
                    MessageReceiver messageConsoleReceiver = new MessageReceiver(System.in);
//                System.out.println(RESOURCE_BUNDLE.getValue("delete"));
                    delete(messageConsoleReceiver, messageSender);

                }
            }


    }

    public void registrationOrAutorization(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println("-----------------------");
        System.out.println(RESOURCE_BUNDLE.getValue("welcome"));
        System.out.println("-----------------------");
        System.out.println(RESOURCE_BUNDLE.getValue("menu"));

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
            registrationOrAutorization(messageReceiver, messageSender);
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
        String oldPassword = getString(messageReceiver, RESOURCE_BUNDLE.getValue("oldPassword")).toUpperCase();
        String newPassword = getString(messageReceiver, RESOURCE_BUNDLE.getValue("newPassword")).toUpperCase();
        messageSender.sendMessage(RESOURCE_BUNDLE.getValue("changeName") + oldPassword + " " + newPassword);

    }


    private String getString(MessageReceiver messageReceiver, String userHint) {
        System.out.println(userHint);
        return messageReceiver.readMessage().trim();
    }


}
