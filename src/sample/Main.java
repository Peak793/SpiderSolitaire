package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main extends Application {

    public Scene gameScene;
    Menu menu = new Menu();
    Game game = new Game();
    ScoreBoard sb = new ScoreBoard();

    TextField textArea = new TextField();

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
        textArea.setFont(new Font("",30));
        textArea.setPrefSize(200,50);
        textArea.setAlignment(Pos.CENTER);
        textArea.setPromptText("Enter Your Name....");
        textArea.setLayoutX(527.5-100);
        textArea.setLayoutY(450);
        textArea.setVisible(false);

        Pane root = new Pane();
        //only green bg
        root.setStyle("-fx-background-image: url(res/a.png);"+
        "-fx-background-repeat: stretch;"+
        "-fx-background-size: 1055 800;"+
        "-fx-background-position: center center;");

        gameScene = new Scene(root);

        Canvas canvas = new Canvas(1055,800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().addAll(canvas,textArea);

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

                    if(game.isGameWon() && currentScene==1)
                    {
                        gc.setFill(Color.WHITE);
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.fillText("You Win!",527.5,400);
                        textArea.setVisible(true);
                    }else
                    {
                        textArea.setVisible(false);
                    }



            }

        }.start();

        textArea.setOnKeyPressed(t ->{
            enterText(t,primaryStage);
        });

        canvas.setOnMouseClicked(t->{

            int n = game.handleMouseClicked(t);
            if(n==1) {
                currentScene = 0;
                isGameStart = false;
                primaryStage.setScene(menu.scene);
                primaryStage.centerOnScreen();
            }else if(n==2)
            {
                game = new Game(gc);
            }

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
            if (menu.isScoreBtnPressed)
            {
                File file = new File("Score.txt");
                Score[] scoreL = new Score[6];
                scoreL[0] = new Score(textArea.getText(),game.score);
                for(int i=1;i< scoreL.length;i++)
                {
                    scoreL[i] = new Score();
                }
                try {
                    Scanner reader = new Scanner(file);
                    int n=1;
                    while(reader.hasNext())
                    {
                        scoreL[n] = new Score(reader.next(),reader.nextInt());
                        n++;
                    }
                    reader.close();
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                }
                sb = new ScoreBoard(scoreL);
                primaryStage.setScene(sb.scoreboard);
                primaryStage.centerOnScreen();
                menu.isScoreBtnPressed = false;
                currentScene =2;
            }

        }else
        {
            if (sb.isMMBTPressed)
            {
                primaryStage.setScene(menu.scene);
                primaryStage.centerOnScreen();
                sb.isMMBTPressed =false;
                currentScene = 0;
            }
        }
    }

    void enterText(KeyEvent KE,Stage stage) throws NullPointerException{
        if(KE.getCode() == KeyCode.ENTER && KE.getText() != ""){
            File file = new File("Score.txt");
            Score[] scoreL = new Score[6];
            scoreL[0] = new Score(textArea.getText(),game.score);
            for(int i=1;i< scoreL.length;i++)
            {
                scoreL[i] = new Score();
            }
            try {
                Scanner reader = new Scanner(file);
                int n=1;
                while(reader.hasNext())
                {
                    scoreL[n] = new Score(reader.next(),reader.nextInt());
                    n++;
                }
                reader.close();
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }

            bubleSort(scoreL);

            try {
                FileWriter fw = new FileWriter(file);
                for(int i =0;i< scoreL.length-1;i++)
                {
                    if(!scoreL[i].getName().equals(""))
                    {
                        fw.write(scoreL[i].getName() + " ");
                        fw.write(scoreL[i].getScore() + "\n");
                    }
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            textArea.setText("");
            stage.setScene(menu.scene);
            stage.centerOnScreen();
            currentScene = 0;
        }
    }

    void bubleSort(Score[] scores)
    {
        for(int i=0;i<scores.length-1;i++)
        {
            for(int j =0;j< scores.length-1-i;j++)
            {
                if(scores[j].getScore()<scores[j+1].getScore())
                {
                    Score temp = scores[j];
                    scores[j] = scores[j+1];
                    scores[j+1]  = temp;
                }
            }
        }
    }
}
