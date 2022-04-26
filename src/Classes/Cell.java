package Classes;

import Controllers.StartController;

import java.util.ArrayList;
import java.util.Arrays;

public class Cell extends Point{

    private final int GENOME_SIZE = 16;
    private final static double MUTATION_PROBABILITY = 0.05;

    private int health;
    private int head;
    private int[] genome = new int[GENOME_SIZE];
    private int state;

    public Cell(int head, int health) {
        this.health = health;
        this.head = head;
        this.state = 0;
        for (int i = 0; i < GENOME_SIZE; i++)
            this.genome[i] = (int)Math.floor(Math.random() * GENOME_SIZE);
        System.out.println(Arrays.toString(this.genome));
    }

    public Cell (int head, int[] genome){
        this.health = 100;
        this.state = 0;
        this.genome = genome;
        this.head = head;

    }

    @Override
    public int getNodeNumber() {
        return this.head / 90 + 1;
    }

    static ArrayList<Cell> deadCells = new ArrayList<>();
    public static int cycleCounter = 0;


    static public Point[][] fieldProcess(Point[][] newField){

        cycleCounter += 1;

        Point[][] oldField = new Point[StartController.x][StartController.y];



        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++){
                oldField[i][j] = newField[i][j];
            }
        }


        stateCheck(newField);


        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++){
                if(oldField[i][j] instanceof Cell){
                    switch(((Cell) oldField[i][j]).genome[((Cell) oldField[i][j]).state]){
                        case 0, 1, 2, 3 -> newField = cellMove(oldField, newField, i, j);
                        case 4 -> ((Cell) newField[i][j]).head += 0;
                        case 5 -> ((Cell) newField[i][j]).head = ((((Cell) newField[i][j]).head + 90) / 90 % 4) * 90;
                        case 6 -> ((Cell) newField[i][j]).head = ((((Cell) newField[i][j]).head + 180) / 90 % 4) * 90;
                        case 7 -> ((Cell) newField[i][j]).head = ((((Cell) newField[i][j]).head + 270) / 90 % 4) * 90;
                        default -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + ((Cell) newField[i][j]).
                                genome[((Cell) newField[i][j]).state]) % 16;
                    }

                }

            }
        }

        int cellCounter = 0;
        int foodCounter = 0;
        int poisonCounter = 0;

        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++) {

                if(newField[i][j] instanceof Solid){
                    if(newField[i][j].getNodeNumber() == 5) foodCounter += 1;
                    else if(newField[i][j].getNodeNumber() == 6) poisonCounter += 1;
                }

                if(newField[i][j] instanceof Cell){
                    ((Cell) newField[i][j]).health -= 1;
                    if (((Cell) newField[i][j]).health <= 0) {
                        deadCells.add((Cell)newField[i][j]);
                        newField[i][j] = new Point();
                    }
                    cellCounter += 1;
                }
            }
        }

        if(cellCounter == 0){
            newField = cellEvo(newField);
            if(Field.cycleMaxExternal < cycleCounter){
                Field.cycleMaxExternal = cycleCounter;
            }
            Field.cycleNumber += 1;
            cycleCounter = 0;
        }

        if(foodCounter < 2) {
            newField = Solid.foodEvo(newField);
        }

        if(poisonCounter < 2) {
            newField = Solid.poisonEvo(newField);
        }



        return newField;
    }





    static private Point[][] cellEvo(Point[][] field){

        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++) {

                if(field[i][j] instanceof Cell){
                    deadCells.add((Cell) field[i][j]);
                    field[i][j] = new Point();
                }
            }
        }

        double probability = (double) deadCells.size() / (StartController.x * StartController.y) ;

        System.out.println("--------------------------------------------------------------");

        System.out.println(deadCells.size());

        int counter = deadCells.size();
        int spawnedCells = 0;

        while (spawnedCells < deadCells.size()){

            for(int i = 0; i < StartController.x; i++){
                for(int j = 0; j < StartController.y; j++) {

                    if(probability >= Math.random() && spawnedCells < deadCells.size()){

                        if(field[i][j].getNodeNumber() == 0){

                            for(int k = 0; k < deadCells.get(counter - 1).GENOME_SIZE; k++){
                                if (Cell.MUTATION_PROBABILITY >= Math.random()){
                                    deadCells.get(counter - 1).genome[k] = Math.abs((int)(deadCells.get(counter - 1).genome[k] + Math.round(Math.random() * 4 - 2))) % 16;
                                }
                            }

                            field[i][j] = new Cell(deadCells.get(counter - 1).head, deadCells.get(counter - 1).genome);
                            System.out.println(Arrays.toString(deadCells.get(counter - 1).genome));
                            counter -= 1;
                            spawnedCells += 1;

                            if(deadCells.size() - counter > (double)Field.cellCount / 5) counter = deadCells.size();
                        }
                    }


                }
            }
        }

        deadCells.clear();

        return field;
    }

    static private Point[][] stateCheck(Point[][] newField){

        for(int i = 0; i < StartController.x; i++){
            for(int j = 0; j < StartController.y; j++) {

                if(newField[i][j] instanceof Cell){

                    switch (((Cell) newField[i][j]).head / 90){
                        case 0 -> {
                            if (j - 1 >= 0){
                                    switch (newField[i][j - 1].getNodeNumber()){
                                        case 0 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 4) % 16;
                                        case 6 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 2) % 16;
                                        case 7 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 3) % 16;
                                        default -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 1) % 16;
                                    }
                            }
                        }

                        case 1 -> {
                            if (i + 1 < StartController.x){
                                    switch (newField[i + 1][j].getNodeNumber()){
                                        case 0 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 4) % 16;
                                        case 6 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 2) % 16;
                                        case 7 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 3) % 16;
                                        default -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 1) % 16;
                                    }
                            }
                        }

                        case 2 -> {
                            if (j + 1 < StartController.y){
                                    switch (newField[i][j + 1].getNodeNumber()){
                                        case 0 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 4) % 16;
                                        case 6 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 2) % 16;
                                        case 7 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 3) % 16;
                                        default -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 1) % 16;
                                    }
                            }
                        }

                        case 3 -> {
                            if (i - 1 >= 0){
                                    switch (newField[i - 1][j].getNodeNumber()){
                                        case 0 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 4) % 16;
                                        case 6 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 2) % 16;
                                        case 7 -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 3) % 16;
                                        default -> ((Cell) newField[i][j]).state = (((Cell) newField[i][j]).state + 1) % 16;
                                    }
                            }
                        }

                    }

                }

            }
        }

        return newField;
    }

    static private Point[][] cellMove(Point[][] oldField, Point[][] newField, int i, int j){

        final int HEALTH_PLUS = 10;
        final int HEALTH_MINUS = 10;

        switch((((Cell)oldField[i][j]).genome[((Cell) oldField[i][j]).state] + ((Cell)oldField[i][j]).head/90) % 4){
            case 0 -> {
                if (j - 1 >= 0) {
                    if (oldField[i][j - 1].getNodeNumber() == 0 && newField[i][j - 1].getNodeNumber() == 0) {
                        newField[i][j - 1] = newField[i][j];
                        newField[i][j] = new Point();
                    } else if (newField[i][j - 1].getNodeNumber() == 5) {
                        ((Cell) newField[i][j]).health += HEALTH_PLUS;
                        newField[i][j - 1] = newField[i][j];
                        newField[i][j] = new Point();
                    } else if (newField[i][j - 1].getNodeNumber() == 6) {
                        ((Cell) newField[i][j]).health -= HEALTH_MINUS;
                        newField[i][j - 1] = newField[i][j];
                        newField[i][j] = new Point();
                    }
                }
            }

            case 1 -> {
                if (i + 1 < StartController.x) {
                    if (oldField[i + 1][j].getNodeNumber() == 0 && newField[i + 1][j].getNodeNumber() == 0) {
                        newField[i + 1][j] = newField[i][j];
                        newField[i][j] = new Point();
                    } else if (newField[i + 1][j].getNodeNumber() == 5) {
                        ((Cell) newField[i][j]).health += HEALTH_PLUS;
                        newField[i + 1][j] = newField[i][j];
                        newField[i][j] = new Point();
                    } else if (newField[i + 1][j].getNodeNumber() == 6) {
                        ((Cell) newField[i][j]).health -= HEALTH_MINUS;
                        newField[i + 1][j] = newField[i][j];
                        newField[i][j] = new Point();
                    }
                }
            }

            case 2 -> {
                if (j + 1 < StartController.y){
                    if (oldField[i][j + 1].getNodeNumber() == 0 && newField[i][j + 1].getNodeNumber() == 0){
                        newField[i][j + 1] = newField[i][j];
                        newField[i][j] = new Point();
                    }else
                    if (newField[i][j + 1].getNodeNumber() == 5){
                        ((Cell) newField[i][j]).health += HEALTH_PLUS;
                        newField[i][j + 1] = newField[i][j];
                        newField[i][j] = new Point();
                    }else
                    if (newField[i][j + 1].getNodeNumber() == 6){
                        ((Cell) newField[i][j]).health -= HEALTH_MINUS;
                        newField[i][j + 1] = newField[i][j];
                        newField[i][j] = new Point();
                    }
                }
            }

            case 3 -> {
                if (i - 1 >= 0){
                    if (oldField[i - 1][j].getNodeNumber() == 0 && newField[i - 1][j].getNodeNumber() == 0){
                        newField[i - 1][j] = newField[i][j];
                        newField[i][j] = new Point();
                    }else
                    if (newField[i - 1][j].getNodeNumber() == 5){
                        ((Cell) newField[i][j]).health += HEALTH_PLUS;
                        newField[i - 1][j] = newField[i][j];
                        newField[i][j] = new Point();
                    }else
                    if (newField[i - 1][j].getNodeNumber() == 6){
                        ((Cell) newField[i][j]).health -= HEALTH_MINUS;
                        newField[i - 1][j] = newField[i][j];
                        newField[i][j] = new Point();
                    }
                }
            }

        }
        return newField;
    }

}
