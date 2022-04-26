package Classes;


import Controllers.StartController;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


public class Field {

    Point [][] statPoint = new Point[StartController.x][StartController.y];

    Image cellUpImage = new Image("/res/up.jpg");
    Image cellLeftImage = new Image("/res/left.jpg");
    Image cellDownImage = new Image("/res/down.jpg");
    Image cellRightImage = new Image("/res/right.jpg");
    Image foodImage = new Image("/res/food.png");
    Image poisonImage = new Image("/res/poison.png");
    Image wallImage = new Image("/res/wall.png");

    public enum CellNode {Null, CellUp, CellRight, CellDown, CellLeft, Food, Poison, Wall}

    private static Field instance = null;

    private static boolean stopThread = true;

    public static int cellCount = 0;
    public static int foodCount = 0;
    public static int poisonCount = 0;
    public static int cycleMax = 0;
    public static int cycleMaxExternal;
    public static int cycleNumber = 0;


    private Field(){}

    public static Field getInstance(){
        if (instance == null) instance = new Field();

        return instance;
    }

    public void process(SimpleBooleanProperty working, DoubleProperty speed, GridPane gridPane, DoubleProperty cellSize, Label stat){
            Thread thread1 = new Thread(() -> {
                while(working.get() && stopThread){
                    statPoint = Cell.fieldProcess(statPoint);
                    Platform.runLater(() -> {
                        fieldVisualization(gridPane, cellSize);
                        if(Field.cycleMax < Field.cycleMaxExternal){
                            Field.cycleMax = Field.cycleMaxExternal;
                            stat.setText(stat.getText() + '\n' + cycleNumber + ". " + (cycleMax - 1));
                        }
                    });

                    try {
                        Thread.sleep(1000 - (long)speed.get());
                    } catch (InterruptedException ignored) {
                    }
                }
            });
            try{
                thread1.join();
            }
            catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }
            thread1.start();

    }

    public void setStopThread(){
        stopThread = false;
    }


    public void wallAutoInitialize(GridPane gridPane, DoubleProperty wallSize, double probability){
        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++){

                if(statPoint[i][j].getNodeNumber() == 7) statPoint[i][j] = new Point();

                if(statPoint[i][j].getNodeNumber() == 0){
                    if (probability >= Math.random()){
                        statPoint[i][j] = new Solid(Solid.SolidClass.Wall);
                    }
                }
            }
        }

        fieldVisualization(gridPane, wallSize);

    }

    public void foodAutoInitialize(GridPane gridPane, DoubleProperty foodSize, double probability){
        foodCount = 0;

        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++){

                if(statPoint[i][j].getNodeNumber() == 5) statPoint[i][j] = new Point();

                if(statPoint[i][j].getNodeNumber() == 0){
                    if (probability >= Math.random()){
                        statPoint[i][j] = new Solid(Solid.SolidClass.Food);
                        foodCount += 1;
                    }
                }
            }
        }

        fieldVisualization(gridPane, foodSize);

    }

    public void poisonAutoInitialize(GridPane gridPane, DoubleProperty poisonSize, double probability){
        poisonCount = 0;

        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++){

                if(statPoint[i][j].getNodeNumber() == 6) statPoint[i][j] = new Point();

                if(statPoint[i][j].getNodeNumber() == 0){
                    if (probability >= Math.random()){
                        statPoint[i][j] = new Solid(Solid.SolidClass.Poison);
                        poisonCount += 1;
                    }
                }
            }
        }

        fieldVisualization(gridPane, poisonSize);

    }

    public void cellAutoInitialize(GridPane gridPane, DoubleProperty cellSize, double probability){
        cellCount = 0;

        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++){

                if(statPoint[i][j] instanceof Cell) statPoint[i][j] = new Point();

                if(statPoint[i][j].getNodeNumber() == 0){
                    if (probability >= Math.random()){
                        statPoint[i][j] = new Cell(0, 100);
                        cellCount += 1;
                    }
                }
            }
        }

        fieldVisualization(gridPane, cellSize);
        System.out.println(cellCount);
    }

    public void preInitialize(){
        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++){

                statPoint[i][j] = new Point();

            }
        }
    }

    public void manualInitialize(GridPane gridPane, CellNode n, DoubleProperty cellSize, int x, int y){

        switch(n.ordinal()){
            case 1 -> {
                statPoint[x][y] = new Cell(0,100);
                cellCount += 1;
            }
            case 2 -> {
                statPoint[x][y] = new Cell(90,100);
                cellCount += 1;
            }
            case 3 -> {
                statPoint[x][y] = new Cell(180,100);
                cellCount += 1;
            }
            case 4 -> {
                statPoint[x][y] = new Cell(270,100);
                cellCount += 1;
            }
            case 5 -> {
                statPoint[x][y] = new Solid(Solid.SolidClass.Food);
                foodCount += 1;
            }
            case 6 -> {
                statPoint[x][y] = new Solid(Solid.SolidClass.Poison);
                poisonCount += 1;
            }
            case 7 -> statPoint[x][y] = new Solid(Solid.SolidClass.Wall);
        }

        fieldVisualization(gridPane, cellSize);
    }

    public void fieldVisualization(GridPane gridPane, DoubleProperty cellSize){

        gridPane.getChildren().remove(1,gridPane.getChildren().size());

        for (int i = 0; i < StartController.x; i++){
            for (int j = 0; j < StartController.y; j++){

                if (statPoint[i][j].getNodeNumber() == 0){
                    gridPane.add(new Pane(),i,j);
                }

                else{
                    gridPane.add(getNewFieldNode(CellNode.values()[statPoint[i][j].getNodeNumber()],cellSize), i, j);
                }
            }
        }

    }



    public ImageView getNewFieldNode(CellNode n, DoubleProperty cellSize){
        switch (n.ordinal()) {
            case 1 -> {
                ImageView cellUp = new ImageView(cellUpImage);
                cellUp.setCache(true);
                cellUp.setCacheHint(CacheHint.SPEED);
                cellUp.fitWidthProperty().bind(cellSize);
                cellUp.fitHeightProperty().bind(cellSize);
                return cellUp;
            }
            case 2 -> {
                ImageView cellRight = new ImageView(cellRightImage);
                cellRight.setCache(true);
                cellRight.setCacheHint(CacheHint.SPEED);
                cellRight.fitWidthProperty().bind(cellSize);
                cellRight.fitHeightProperty().bind(cellSize);
                return cellRight;
            }
            case 3 -> {
                ImageView cellDown = new ImageView(cellDownImage);
                cellDown.setCache(true);
                cellDown.setCacheHint(CacheHint.SPEED);
                cellDown.fitWidthProperty().bind(cellSize);
                cellDown.fitHeightProperty().bind(cellSize);
                return cellDown;
            }
            case 4 -> {
                ImageView cellLeft = new ImageView(cellLeftImage);
                cellLeft.setCache(true);
                cellLeft.setCacheHint(CacheHint.SPEED);
                cellLeft.fitWidthProperty().bind(cellSize);
                cellLeft.fitHeightProperty().bind(cellSize);
                return cellLeft;
            }
            case 5 -> {
                ImageView food = new ImageView(foodImage);
                food.setCache(true);
                food.setCacheHint(CacheHint.SPEED);
                food.fitWidthProperty().bind(cellSize);
                food.fitHeightProperty().bind(cellSize);
                return food;
            }
            case 6 -> {
                ImageView poison = new ImageView(poisonImage);
                poison.setCache(true);
                poison.setCacheHint(CacheHint.SPEED);
                poison.fitWidthProperty().bind(cellSize);
                poison.fitHeightProperty().bind(cellSize);
                return poison;
            }
            case 7 -> {
                ImageView wall = new ImageView(wallImage);
                wall.setCache(true);
                wall.setCacheHint(CacheHint.SPEED);
                wall.fitWidthProperty().bind(cellSize);
                wall.fitHeightProperty().bind(cellSize);
                return wall;
            }
            default -> {
                return new ImageView();
            }
        }
    }



}
