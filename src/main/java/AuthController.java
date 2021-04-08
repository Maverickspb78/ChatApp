import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthController {
    public TextField login;
    public TextField password;

    public void enter(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

        Message answer;

        String[] command = new String[3];
        command[0] = "AUTH";
        command[1] = login.getText();
        command[2] = password.getText();

        ChatApplication.outputStream.writeObject(Message.of("", "", command));
        answer = (Message)ChatApplication.inputStream.readObject();

        if (answer.getCommand()[0].equals("OK")) {
            Parent chat = FXMLLoader.load(getClass().getResource("chat.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Сетевой чат");
            stage.setScene(new Scene(chat));
            stage.setResizable(false);
            stage.show();
            login.getScene().getWindow().hide();
        } else {
            login.setText("WRONG LOGIN OR PASSWORD");
            password.clear();
        }
    }

    public void reg(ActionEvent actionEvent) throws IOException {
        Parent chat = FXMLLoader.load(getClass().getResource("registration.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Регистрация");
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        stage.show();
        login.getScene().getWindow().hide();
    }

}