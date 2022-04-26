package Windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class StartWindow extends Application {

    @Override
    public void start(Stage StartStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/FXMLs/start.fxml"));
        StartStage.setTitle("Life Game");
        StartStage.setScene(new Scene(root, 400, 200));
        StartStage.getIcons().add(new Image(MainWindow.class.getResourceAsStream("/res/icon.png")));

        StartStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
