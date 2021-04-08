import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SerialHandler implements Closeable, Runnable {

    private static int cnt = 0;
    private String userName;
    private String currentRoom = "Common";
    private final ObjectInputStream is;
    private final ObjectOutputStream os;
    private boolean running;
    private final byte [] buffer;
    private final EchoServer server;


    public SerialHandler(Socket socket, EchoServer server) throws IOException {
        System.out.println("1");
        os = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("2");
        is = new ObjectInputStream(socket.getInputStream());
        System.out.println("3");
        cnt++;
        userName = "user#" + cnt;
        running = true;
        buffer = new byte[256];
        this.server = server;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        DBAuthServiceImpl.getInstance().init();
        while (running) {
            try {
                Message message = (Message) is.readObject();
                System.out.println(message);
                System.out.println(Arrays.toString(message.getCommand()));

                //message parsing

                String[] answer = new String[1];
                switch (message.getCommand()[0]) {
                    case "REG":
                        switch (DBAuthServiceImpl.getInstance().addUser(message.getCommand()[1],
                                message.getCommand()[2])) {
                            case 0:
                                answer[0] = "REG";
                                userName = message.getCommand()[1];
                                sendMessage(Message.of("", "", answer));
                                answer[0] = "MESSAGE";
                                server.roomCast(Message.of("server", "Пользователь " +
                                        userName + " вошёл в чат.\n", answer), currentRoom);
                                break;
                            case 1:
                                answer[0] = "EXISTS";
                                sendMessage(Message.of("", "", answer));
                                break;
                            case -1:
                                answer[0] = "DBFAULT";
                                sendMessage(Message.of("", "", answer));
                                break;
                        }
                        break;
                    case "AUTH":
                        if (DBAuthServiceImpl.getInstance().auth(message.getCommand()[1],
                                message.getCommand()[2])) {
                            answer[0] = "OK";
                            sendMessage(Message.of("", "", answer));
                            userName = message.getCommand()[1];
                            answer[0] = "MESSAGE";
                            server.roomCast(Message.of("server", "Пользователь " +
                                    userName + " вошёл в чат.\n", answer), currentRoom);
                        } else {
                            answer[0] = "DENIED";
                            sendMessage(Message.of("", "", answer));
                        }
                        break;
                    case "CHNAME":
                        String oldUserName = userName;
                        userName = message.getCommand()[1];
                        answer[0] = "CHNAME";
                        sendMessage(Message.of("", "", answer));
                        answer[0] = "MESSAGE";
                        server.roomCast(Message.of("server", "Пользователь " +
                                oldUserName + " сменил имя на " + userName + "\n", answer), currentRoom);
                        break;
                    case "ENTER":
                        currentRoom = message.getCommand()[1];
                        answer[0] = "ENTER";
                        sendMessage(Message.of("", "", answer));
                        answer[0] = "MESSAGE";
                        server.roomCast(Message.of("server", "Пользователь " +
                                userName + " вошёл в комнату " + currentRoom + "\n", answer), currentRoom);
                        break;
                    case "LEAVE":
                        answer[0] = "LEAVE";
                        String oldRoom = currentRoom;
                        currentRoom = "Common";
                        sendMessage(Message.of("", "", answer));
                        answer[0] = "MESSAGE";
                        server.roomCast(Message.of("server", "Пользователь " +
                                userName + " покинул комнату.\n", answer), oldRoom);
                    case "QUIT":
                        answer[0] = "QUIT";
                        running = false;
                        sendMessage(Message.of("", "", answer));
                        server.kickMe(this);
                        TimeUnit.MILLISECONDS.sleep(1000);
                        answer[0] = "MESSAGE";
                        server.roomCast(Message.of("server", "Пользователь " +
                                userName + " покинул чат.\n", answer), currentRoom);
                        break;
                    default:
                        answer[0] = "MESSAGE";
                        //server.broadCast(Message.of(userName, message.getMessage(), answer));
                        server.roomCast(Message.of(userName, message.getMessage(), answer), currentRoom);
                        break;
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                System.err.println("Exception while read");
                break;
            }
        }

        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) throws IOException {
        os.writeObject(message);
        os.flush();
    }

    @Override
    public void close() throws IOException {
        os.close();
        is.close();
    }

    public String getCurrentRoom() {
        return currentRoom;
    }
}