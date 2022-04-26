package Controllers;


import Classes.Field;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    GridPane gridPane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Slider cellSizeSlider;
    @FXML
    Slider cellDifficulty;
    @FXML
    Slider foodDifficulty;
    @FXML
    Slider poisonDifficulty;
    @FXML
    Slider wallDifficulty;
    @FXML
    Slider speedSlider;
    @FXML
    RadioButton placerModeCellAuto;
    @FXML
    RadioButton placerModeCellManual;
    @FXML
    RadioButton placerModeFoodAuto;
    @FXML
    RadioButton placerModeFoodManual;
    @FXML
    RadioButton placerModePoisonAuto;
    @FXML
    RadioButton placerModePoisonManual;
    @FXML
    RadioButton placerModeWallAuto;
    @FXML
    RadioButton placerModeWallManual;
    @FXML
    RadioButton placerChoiceCell;
    @FXML
    RadioButton placerChoiceFood;
    @FXML
    RadioButton placerChoicePoison;
    @FXML
    RadioButton placerChoiceWall;
    @FXML
    Button cellAutoGenerate;
    @FXML
    Button wallAutoGenerate;
    @FXML
    Button foodAutoGenerate;
    @FXML
    Button poisonAutoGenerate;
    @FXML
    Button startProcess;
    @FXML
    Button stopProcess;
    @FXML
    Label statLabel;


    Field field = Field.getInstance();

    private void gridInitialize(){

        gridPane.setGridLinesVisible(true);
        gridPane.setLayoutX(20);
        gridPane.setLayoutY(20);

        for(int i = 0; i < StartController.x; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0/StartController.x);
            columnConstraints.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for(int i = 0; i < StartController.y; i++){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0/StartController.y);
            rowConstraints.setValignment(VPos.CENTER);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for(int i = 0; i < StartController.x; i++)
            for (int j = 0; j < StartController.y; j++){
                Pane click = new Pane();
                gridPane.add(click, i, j);
            }

        gridPane.prefHeightProperty().bind(cellSizeSlider.valueProperty().multiply(StartController.y));
        gridPane.prefWidthProperty().bind(cellSizeSlider.valueProperty().multiply(StartController.x));
        scrollPane.setMaxHeight(Screen.getScreens().get(0).getBounds().getHeight()-300);
        scrollPane.setMaxWidth(900);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void autoManualControl(){

        EventHandler <ActionEvent> cellModeToAuto = actionEvent -> {
            cellDifficulty.setDisable(false);
            cellAutoGenerate.setDisable(false);
        };
        placerModeCellAuto.setOnAction(cellModeToAuto);

        EventHandler <ActionEvent> cellModeToManual = actionEvent -> {
            cellDifficulty.setDisable(true);
            cellAutoGenerate.setDisable(true);
        };
        placerModeCellManual.setOnAction(cellModeToManual);

        EventHandler <ActionEvent> foodModeToAuto = actionEvent -> {
            foodDifficulty.setDisable(false);
            foodAutoGenerate.setDisable(false);
        };
        placerModeFoodAuto.setOnAction(foodModeToAuto);

        EventHandler <ActionEvent> foodModeToManual = actionEvent -> {
            foodDifficulty.setDisable(true);
            foodAutoGenerate.setDisable(true);
        };
        placerModeFoodManual.setOnAction(foodModeToManual);

        EventHandler <ActionEvent> poisonModeToAuto = actionEvent -> {
            poisonDifficulty.setDisable(false);
            poisonAutoGenerate.setDisable(false);
        };
        placerModePoisonAuto.setOnAction(poisonModeToAuto);

        EventHandler <ActionEvent> poisonModeToManual = actionEvent -> {
            poisonDifficulty.setDisable(true);
            poisonAutoGenerate.setDisable(true);
        };
        placerModePoisonManual.setOnAction(poisonModeToManual);

        EventHandler <ActionEvent> wallModeToAuto = actionEvent -> {
            wallDifficulty.setDisable(false);
            wallAutoGenerate.setDisable(false);
        };
        placerModeWallAuto.setOnAction(wallModeToAuto);

        EventHandler <ActionEvent> wallModeToManual = actionEvent -> {
            wallDifficulty.setDisable(true);
            wallAutoGenerate.setDisable(true);
        };
        placerModeWallManual.setOnAction(wallModeToManual);
    }

    private void autoInitialize(){

        EventHandler<ActionEvent> cellInit = actionEvent -> {
            field.cellAutoInitialize(gridPane, cellSizeSlider.valueProperty(), cellDifficulty.getValue());
        };
        cellAutoGenerate.setOnAction(cellInit);

        EventHandler<ActionEvent> wallInit = actionEvent -> {
            field.wallAutoInitialize(gridPane, cellSizeSlider.valueProperty(), wallDifficulty.getValue());
        };
        wallAutoGenerate.setOnAction(wallInit);

        EventHandler<ActionEvent> foodInit = actionEvent -> {
            field.foodAutoInitialize(gridPane, cellSizeSlider.valueProperty(), foodDifficulty.getValue());
        };
        foodAutoGenerate.setOnAction(foodInit);

        EventHandler<ActionEvent> poisionInit = actionEvent -> {
            field.poisonAutoInitialize(gridPane, cellSizeSlider.valueProperty(), poisonDifficulty.getValue());
        };
        poisonAutoGenerate.setOnAction(poisionInit);

    }


    private void imageManualPlacer(){

        gridPane.setOnMouseClicked(e -> {
            Node source = e.getPickResult().getIntersectedNode() ;
            Integer colIndex = GridPane.getColumnIndex(source);
            Integer rowIndex = GridPane.getRowIndex(source);
            if(placerChoiceCell.isSelected() && placerModeCellManual.isSelected()) field.manualInitialize(gridPane, Field.CellNode.CellUp, cellSizeSlider.valueProperty(),colIndex,rowIndex);
            else
                if (placerChoiceFood.isSelected() && placerModeFoodManual.isSelected()) field.manualInitialize(gridPane, Field.CellNode.Food, cellSizeSlider.valueProperty(),colIndex,rowIndex);
                else
                    if(placerChoicePoison.isSelected() && placerModePoisonManual.isSelected()) field.manualInitialize(gridPane, Field.CellNode.Poison, cellSizeSlider.valueProperty(),colIndex,rowIndex);
                    else
                        if(placerChoiceWall.isSelected() && placerModeWallManual.isSelected()) field.manualInitialize(gridPane, Field.CellNode.Wall, cellSizeSlider.valueProperty(),colIndex,rowIndex);
        });

    }

    private SimpleBooleanProperty startStopProcess(){
        SimpleBooleanProperty startStopProperty = new SimpleBooleanProperty();
        startProcess.setOnAction(actionEvent -> {
            startStopProperty.setValue(true);
            field.process(startStopProperty, speedSlider.valueProperty(), gridPane, cellSizeSlider.valueProperty(), statLabel);
        });

        stopProcess.setOnAction(actionEvent -> startStopProperty.setValue(false));

        return startStopProperty;
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        field.preInitialize();
        gridInitialize();
        autoManualControl();
        imageManualPlacer();
        autoInitialize();
        startStopProcess();
    }
}
