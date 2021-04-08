import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {
    public TextField login;
    public TextField password;

    public void enter(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Message answer;

        String[] command = new String[3];
        command[0] = "REG";
        command[1] = login.getText();
        command[2] = password.getText();

        ChatApplication.outputStream.writeObject(Message.of("", "", command));
        answer = (Message)ChatApplication.inputStream.readObject();
        switch (answer.getCommand()[0]) {
            case "EXISTS":
                login.setText("Пользователь существует!");
                return;
            case "REG":
                break;
            case "DBFAULT":
                login.setText("Нет подключения к базе пользователей");
                return;
        }

        Parent chat = FXMLLoader.load(getClass().getResource("chat.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Сетевой чат");
        stage.setScene(new Scene(chat));
        stage.setResizable(false);
        stage.show();
        login.getScene().getWindow().hide();
    }
}