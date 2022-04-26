package Classes;

import Controllers.StartController;

public class Solid extends Point{

    public SolidClass solidClass;

    public enum SolidClass {Food, Poison, Wall}

    public Solid(SolidClass solidClass) {
        this.solidClass = solidClass;
    }

    @Override
    public int getNodeNumber(){
        return this.solidClass.ordinal() + 5;
    }

    static Point[][] foodEvo(Point[][] field){

        int spawnedFood = 0;

        double foodProbability = (double) Field.foodCount / (StartController.x * StartController.y) ;

        while (spawnedFood < Field.foodCount){
            for(int i = 0; i < StartController.x; i++){
                for(int j = 0; j < StartController.y; j++) {

                    if(foodProbability >= Math.random() && spawnedFood < Field.foodCount){

                        if(field[i][j].getNodeNumber() == 0){
                            field[i][j] = new Solid(Solid.SolidClass.Food);
                            spawnedFood += 1;
                        }
                    }

                }
            }
        }

        return field;

    }


    static Point[][] poisonEvo(Point[][] field){

        int spawnedPoison = 0;

        double poisonProbability = (double) Field.poisonCount / (StartController.x * StartController.y) ;

        while (spawnedPoison < Field.poisonCount){
            for(int i = 0; i < StartController.x; i++){
                for(int j = 0; j < StartController.y; j++) {

                    if(poisonProbability >= Math.random() && spawnedPoison != Field.poisonCount){

                        if(field[i][j].getNodeNumber() == 0){
                            field[i][j] = new Solid(Solid.SolidClass.Poison);
                            spawnedPoison += 1;
                        }
                    }

                }
            }
        }

        return field;

    }
}
