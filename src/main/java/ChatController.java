import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    public TextArea output;
    public TextField input;

    private ServerListener serverListener;
    private Thread thread;

    public void send(ActionEvent actionEvent) throws IOException {

        String text = input.getText();

        //parse input string

        String[] textArgs;
        textArgs = text.split("\\s");
        String[] command = new String[3];
        command[0] = "MESSAGE";

        switch (textArgs[0]) {
            case "/quit":
                command[0] = "QUIT";
                break;
            case "/enter":
                command[0] = "ENTER";
                command[1] = textArgs[1];
                break;
            case "/leave":
                command[0] = "LEAVE";
                break;
            case "/chname":
                command[0] = "CHNAME";
                command[1] = textArgs[1];
                break;
        }

        ChatApplication.outputStream.writeObject(Message.of("", text + "\n", command));
        input.clear();
    }

    public void quit(ActionEvent actionEvent) throws IOException {
        quitApp();
    }

    public void quitApp () throws IOException {
        String[] command = new String[1];
        command[0] = "QUIT";
        ChatApplication.outputStream.writeObject(Message.of("", "", command));
        while (serverListener.isRunning()) {
        }
        ChatApplication.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverListener = new ServerListener(ChatApplication.inputStream, output);
        List<String> messageList = new LocalHistoryService().getHistory(100);
        if (messageList != null) {
            for (int i = messageList.size(); i > 0; i--) {
                output.appendText(messageList.get(i - 1));
            }
        }
        thread = new Thread(serverListener);
        thread.start();
    }
}