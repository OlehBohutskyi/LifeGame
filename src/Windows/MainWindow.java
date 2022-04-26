package Windows;

import Classes.Field;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainWindow{



    public void mainStart() throws Exception{

        Stage mainStage = new Stage();
        AnchorPane root = FXMLLoader.load(getClass().getResource("/FXMLs/main.fxml"));
        root.setBackground(new Background(new BackgroundFill(Color.rgb(233, 233, 233), CornerRadii.EMPTY, Insets.EMPTY)));
        mainStage.setScene(new Scene(root, 1300, 800));
        mainStage.setTitle("Life Game");
        mainStage.getIcons().add(new Image(MainWindow.class.getResourceAsStream("/res/icon.png")));
        //mainStage.setMaximized(true);
        mainStage.show();

        Field field = Field.getInstance();

        mainStage.setOnCloseRequest(windowEvent -> {
            field.setStopThread();
        });

    }

}
