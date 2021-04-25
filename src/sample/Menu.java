package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.stream.events.StartElement;
import java.io.InputStream;


public class Menu {
    //Constants
    final double WIDTH =900,HEIGHT = 600;

    //MenuScene
    public Scene scene;

    Boolean isStartBtnPressed = false;
    Boolean isScoreBtnPressed = false;
    Boolean isExitBtnPressed = false;

    Menu()
    {
        VBox menuBtn = new VBox();

        //1
//        menuBtn.setStyle("-fx-alignment : center;" +
//                "-fx-background-color : transparent;" +
//                "-fx-spacing : 15;" +
//                "-fx-pref-width : 200;" +
//                "-fx-pref-height : 200;" );
//        menuBtn.setLayoutX(160);
//        menuBtn.setLayoutY(350);

        //2
//        menuBtn.setStyle("-fx-alignment : center;" +
//                "-fx-background-color : transparent;" +
//                "-fx-spacing : 20;" +
//                "-fx-pref-width : 100;" +
//                "-fx-pref-height : 200;" );
//        menuBtn.setLayoutX(670);
//        menuBtn.setLayoutY(270);

//        Image image = new Image("res/logo2.png");
//        ImageView view = new ImageView(image);
//        Label label = new Label();
//        view.setFitWidth(500);
//        view.setFitHeight(250);
//        view.setX(20);
//        view.setY(40);
        Pane pane = new Pane();

        //1
//        pane.setStyle("-fx-background-image : url(/res/menubg.png);" +
//                "-fx-background-repeat: stretch;"+
//                "-fx-background-size: 900 600;"+
//                "-fx-background-position: center center;");

        //2
//        pane.setStyle("-fx-background-image : url(/res/menubg2.png);" +
//                "-fx-background-repeat: stretch;"+
//                "-fx-background-size: 900 600;"+
//                "-fx-background-position: center center;");

        //3
        pane.setStyle("-fx-background-image : url(/res/ClassyMenu2.png);" +
                "-fx-background-repeat: stretch;"+
                "-fx-background-size: 900 600;"+
                "-fx-background-position: center center;");

        scene = new Scene(pane,WIDTH,HEIGHT);
        /*
            Create buttons for menu items.
             */
        ImageView StartBtView = new ImageView(new Image("/res/Startbt.png"));
        ImageView ScoreBtView = new ImageView(new Image("/res/Scorebt.png"));
        ImageView ExitBtView = new ImageView(new Image("/res/Exitbt.png"));

        StartBtView.setFitHeight(70);
        StartBtView.setFitWidth(198);
        ScoreBtView.setFitHeight(70);
        ScoreBtView.setFitWidth(198);
        ExitBtView.setFitHeight(70);
        ExitBtView.setFitWidth(198);

        Button startBtn = new Button("");
                startBtn.setStyle("-fx-alignment : center;" +
                "-fx-font-size : 22;" +
                "-fx-pref-width : 198;" +
                "-fx-pref-height : 70;" +
                "-fx-background-color: transparent");

                startBtn.setLayoutX(600);
                startBtn.setLayoutY(259);
                startBtn.setGraphic(StartBtView);

        Button scoreBtn = new Button("");
                scoreBtn.setStyle("-fx-alignment : center;" +
                "-fx-font-size : 22;" +
                "-fx-pref-width : 198;" +
                "-fx-pref-height : 70;" +
                "-fx-background-color : grey;"+
                "-fx-background-color: transparent");
                scoreBtn.setLayoutX(600);
                scoreBtn.setLayoutY(349);
                scoreBtn.setGraphic(ScoreBtView);

        Button exitBtn = new Button("");
                exitBtn.setStyle("-fx-font-size : 22;" +
                "-fx-pref-width : 198;" +
                "-fx-pref-height : 70;" +
                "-fx-alignment : center;" +
                "-fx-background-color : transparent;");
                exitBtn.setLayoutX(600);
                exitBtn.setLayoutY(439);
                exitBtn.setGraphic(ExitBtView);
        //Add all the button to Vbox
//        menuBtn.getChildren().addAll(startBtn,scoreBtn,exitBtn);
        pane.getChildren().addAll(startBtn,scoreBtn,exitBtn);

        /**
         * handle when start button is clicked
         */
        startBtn.setOnAction(t->{
            isStartBtnPressed = !isStartBtnPressed;
        });

        scoreBtn.setOnAction(t->{
            isScoreBtnPressed = !isScoreBtnPressed;
        });

        exitBtn.setOnAction(t->{
            isExitBtnPressed = !isExitBtnPressed;
        });

    }

}
