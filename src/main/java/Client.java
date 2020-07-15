import lombok.SneakyThrows;

import java.net.Socket;

public class Client {
    private final static String IP = "localhost";
    private final static int PORT = 8080;
//    Menu menuChat = new Menu();

    @SneakyThrows
    public void start() {
        Socket socket = new Socket(IP, PORT);

        if (socket.isConnected()) {
            MessageReceiver messageConsoleReceiver = new MessageReceiver(System.in);
            MessageSender messageSender = new MessageSender(socket.getOutputStream());

//            menuChat.menu();

            autorization(messageConsoleReceiver, messageSender);
////            registration(messageConsoleReceiver, messageSender);
//            delete(messageConsoleReceiver,messageSender);

            new Thread(new SockedRunnable(socket)).start();


            String messageFromConsole;
            while ((messageFromConsole = messageConsoleReceiver.readMessage()) != null) {
                messageSender.sendMessage(messageFromConsole);
            }


        }
    }

    public void registration(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println("Добро пожаловать!\n");

        System.out.println("Введите имя: ");
        String name = messageReceiver.readMessage();
        System.out.println("Введите пароль: ");
        String password = messageReceiver.readMessage();

        messageSender.sendMessage("Registration " + name + " " + password);
//        messageSender.sendMessage(password);

    }

    public void autorization(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println("Добро пожаловать!\nАвторизация:\n");

        System.out.println("Введите имя: ");
        String name = messageReceiver.readMessage();
        System.out.println("Введите пароль: ");
        String password = messageReceiver.readMessage();

        messageSender.sendMessage("Autorization " + name + " " + password);
//        messageSender.sendMessage(password);

    }
    public void delete(MessageReceiver messageReceiver, MessageSender messageSender) {
        System.out.println("удаление\n");

        System.out.println("Введите имя: ");
        String name = messageReceiver.readMessage();
//        System.out.println("Введите пароль: ");
//        String password = messageReceiver.readMessage();

        messageSender.sendMessage("Delete " + name);
//        messageSender.sendMessage(password);

    }


}
