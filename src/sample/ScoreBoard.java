package sample;

import javafx.geometry.BoundingBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class ScoreBoard {
    double HGAP = 600/8,VGAP= 900/6;
    Pane root = new Pane();
    BoundingBox backButton;
    Scene scoreboard;
    boolean isMMBTPressed= false;

    ScoreBoard () { }

    ScoreBoard (Score[] scores){
        root.setStyle("-fx-background-image: url(res/scoreBoard.jpg);"+
                "-fx-background-repeat: stretch;"+
                "-fx-background-size: 900 600;"+
                "-fx-background-position: center center;");

        ImageView MainMenuBtView = new ImageView(new Image("/res/MainMenu.png"));
        MainMenuBtView.setFitHeight(50);
        MainMenuBtView.setFitWidth(198);

        Button MainMenuBtn = new Button("");
        MainMenuBtn.setStyle("-fx-alignment : center;" +
                "-fx-font-size : 22;" +
                "-fx-pref-width : 100;" +
                "-fx-pref-height : 50;" +
                "-fx-background-color: transparent");

        MainMenuBtn.setLayoutX(900-450-96);
        MainMenuBtn.setLayoutY(600-82);
        MainMenuBtn.setGraphic(MainMenuBtView);

        Label[] names = new Label[6];
        Label[] scoresText = new Label[6];
        for(int i =0;i<6;i++)
        {
            names[i] = new Label();
            scoresText[i] = new Label();
        }

        names[0] = new Label("NAME");
        scoresText[0] = new Label("SCORE");
        names[0].setFont(Font.font("Brush Script MT", FontWeight.BOLD,40));
        scoresText[0].setFont(Font.font("Brush Script MT", FontWeight.BOLD,40));
        names[0].setLayoutX(VGAP*2-20);
        names[0].setLayoutY(HGAP*(1));
        scoresText[0].setLayoutX(VGAP*4-30);
        scoresText[0].setLayoutY(HGAP*(1));
        for(int i =1 ;i< scores.length;i++)
        {
            if(!scores[i].getName().equals("")) {
                names[i] = new Label(scores[i].getName());
                scoresText[i] = new Label(Integer.toString(scores[i].getScore()));
                names[i].setFont(Font.font("Brush Script MT", FontWeight.BOLD,30));
                scoresText[i].setFont(Font.font("Brush Script MT", FontWeight.BOLD,30));
                names[i].setLayoutX(VGAP*2);
                names[i].setLayoutY(HGAP*(1+i));
                scoresText[i].setLayoutX(VGAP*4);
                scoresText[i].setLayoutY(HGAP*(1+i));
            }
        }

        for(int i =0 ;i< scores.length;i++) {
            names[i].setTextAlignment(TextAlignment.CENTER);
            scoresText[i].setTextAlignment(TextAlignment.CENTER);
            names[i].setTextFill(Color.WHITE);
            scoresText[i].setTextFill(Color.WHITE);
            root.getChildren().addAll(names[i],scoresText[i]);
        }
        root.getChildren().add(MainMenuBtn);

        scoreboard = new Scene(root,900,600);

        MainMenuBtn.setOnAction(t->{
            isMMBTPressed = true;
        });
    }
}
