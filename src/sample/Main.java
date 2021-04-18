package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public Scene gameScene;
    Menu menu = new Menu();
    Game game = new Game();

    boolean isGameStart = false;

    /**
     * 0 = menu scene
     * 1 = game scene
     * 2 = score scene
     */
    public int currentScene = 0;


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Spider Solitaire");

        Pane root = new Pane();
        //only green bg
        root.setStyle("-fx-background-color: darkGreen;"+
        "-fx-background-repeat: stretch;"+
        "-fx-background-size: 1055 800;"+
        "-fx-background-position: center center;");
        //back bg
//        root.setStyle("-fx-background-image : url(/res/background.jpg);"+
//                "-fx-background-repeat: stretch;"+
//                "-fx-background-size: 1055 800;"+
//                "-fx-background-position: center center;");
        gameScene = new Scene(root);

        Canvas canvas = new Canvas(1055,800);
        root.getChildren().add(canvas);

        menu =new Menu();

        final long startNanoTime = System.nanoTime();

        /**
         * Game loop update 60 times per second (60fps)
         */
        new AnimationTimer()
        {

            @Override
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                //update
                sceneChanger(primaryStage);
                    //game start
                    if(!isGameStart) {
                        game = new Game(canvas.getGraphicsContext2D());
                        isGameStart = true;
                    }

                //draw
            }
        }.start();


        primaryStage.setScene(menu.scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /*
        Change scene from main menu to other scenes of other scenes to main menu
     */
    void sceneChanger(Stage primaryStage)
    {
        if(currentScene == 0) {
            if (menu.isStartBtnPressed) {
                primaryStage.setScene(gameScene);
                primaryStage.centerOnScreen();
                currentScene = 1;
                menu.isStartBtnPressed = false;
            }
        }
    }
}
