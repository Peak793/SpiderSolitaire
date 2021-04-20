package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.MouseListener;


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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Spider Solitaire");

        Pane root = new Pane();
        //only green bg
        root.setStyle("-fx-background-color: darkGreen;"+
        "-fx-background-repeat: stretch;"+
        "-fx-background-size: 1055 800;"+
        "-fx-background-position: center center;");

        gameScene = new Scene(root);

        Canvas canvas = new Canvas(1055,800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
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
                    if(!isGameStart && currentScene == 1) {
                        game = new Game(gc);
                        System.out.println("1");
                        isGameStart = true;
                    }

                //draw
            }

        }.start();
        canvas.setOnMouseClicked(t->{
            game.handleMouseClicked(t);
        });

        primaryStage.setScene(menu.scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
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
            if (menu.isExitBtnPressed)
            {
                primaryStage.close();
            }
        }
    }
}
