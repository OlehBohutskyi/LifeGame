package Controllers;


import Windows.MainWindow;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class StartController implements Initializable {

    final static int MIN_FIELD_SIZE = 10;
    final static int MAX_FIELD_SIZE = 50;

    @FXML
    Button startButton;
    @FXML
    TextField xField;
    @FXML
    TextField yField;
    @FXML
    Label labelCheck;
    public static int x;
    public static int y;

    // Перевірка числа
    private boolean textCheck(String fieldText){

        if (fieldText == null) return false;

        try {
            int buf = Integer.parseInt(fieldText);
            if (buf > MAX_FIELD_SIZE || buf < MIN_FIELD_SIZE) return false;
        } catch (NumberFormatException ex){
            return false;
        }

        return true;

    }

    private void buttonTrans(){

        // Збільшення кнопки при наведенні на неї
        EventHandler <MouseEvent> buttonEnter = mouseEvent -> {

            ScaleTransition buttonTran = new ScaleTransition(Duration.seconds(0.1), startButton);
            buttonTran.setToX(1.1);
            buttonTran.setToY(1.1);

            buttonTran.play();

        };
        startButton.setOnMouseEntered(buttonEnter);

        // Зменшення кнопки про наведенні на неї
        EventHandler <MouseEvent> buttonExit = mouseEvent -> {

            ScaleTransition buttonTran = new ScaleTransition(Duration.seconds(0.1), startButton);
            buttonTran.setToX(1);
            buttonTran.setToY(1);

            buttonTran.play();

        };
        startButton.setOnMouseExited(buttonExit);

        // Натискання на кнопку
        EventHandler<ActionEvent> buttonPress = actionEvent -> {

            if (!textCheck(xField.getText()) || !textCheck(yField.getText())){
                labelCheck.setText("Введіть число від " + MIN_FIELD_SIZE +" до " + MAX_FIELD_SIZE + ":");
            } else {
                MainWindow window = new MainWindow();

                x = Integer.parseInt(xField.getText());
                y = Integer.parseInt(yField.getText());

                try {
                    window.mainStart();
                    startButton.getScene().getWindow().hide();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        };

        startButton.setOnAction(buttonPress);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonTrans();
    }
}
