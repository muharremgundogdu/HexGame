// muharremgundogdu

package edu.erciyes.FX;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainApp extends Application {
    private AnchorPane tileMap = new AnchorPane();
    private HexGame hexGame;
    private Label resultLabel;
    private ImageView resultImageView;
    private Label turnLabel;
    private Alert winner = new Alert(Alert.AlertType.INFORMATION);
    Button btnStart;
    Button btnHowToPlay;

    @Override
    public void start(Stage stage) {
        hexGame = new HexGame();
        BorderPane root = new BorderPane();
        HBox hbox = new HBox();
        hbox.getChildren().clear();
        tileMap.getChildren().clear();

        startPage(stage, root, hbox);
    }

    public void startPage(Stage stage, BorderPane root, HBox hbox) {
        root.getChildren().clear();

        double windowWidth = 900.0;
        double windowHeight = 650.0;

        HBox hboxRadio = new HBox();  // radiobuttonları içerecek
        HBox hboxLblAndImage = new HBox();  // gameover resmini ve labelı içerecek

        RadioButton fiveRadio = new RadioButton("5x5");
        RadioButton elevenRadio = new RadioButton("11x11");
        RadioButton seventeenRadio = new RadioButton("17x17");

        btnStart = new Button("Start");
        btnStart.setDisable(false);

        btnHowToPlay = new Button("How To Play");
        btnHowToPlay.setPrefSize(75, 30);
        btnHowToPlay.setStyle("-fx-background-color: #FFA500; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-family: Arial;");


        fiveRadio.setStyle("-fx-font-size: 16px;");
        elevenRadio.setStyle("-fx-font-size: 16px;");
        seventeenRadio.setStyle("-fx-font-size: 16px;");

        btnStart.setPrefSize(150, 40);
        btnStart.setStyle("-fx-background-color: #0003a4; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: Arial;");

        HBox hboxStart = new HBox();       // start butonunu içerecek
        hboxStart.setAlignment(Pos.CENTER);
        hboxStart.getChildren().addAll(btnStart , btnHowToPlay);
        hboxStart.setSpacing(15);

        hboxRadio.getChildren().addAll(fiveRadio, elevenRadio, seventeenRadio);

        resultLabel = new Label();
        resultLabel.setAlignment(Pos.CENTER);
        resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: Arial");

        Image image = new Image("file:image/game-over.png");
        resultImageView = new ImageView(image);
        resultImageView.setFitWidth(70);
        resultImageView.setFitHeight(40);
        resultImageView.setVisible(false);

        turnLabel = new Label();
        FadeTransition ft = new FadeTransition(Duration.millis(1000) , turnLabel);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        hboxLblAndImage.getChildren().addAll(turnLabel , resultLabel, resultImageView);

        hboxRadio.setPadding(new Insets(7.5, 0, 0, 10));
        hboxRadio.setSpacing(20);

        hboxLblAndImage.setSpacing(20);
        hboxLblAndImage.setPadding(new Insets(0 , 15 , 0 , 0));

        hbox.setSpacing(40);
        hbox.setPadding(new Insets(0, 0, 20, 0));
        hbox.getChildren().addAll(hboxRadio, hboxStart, hboxLblAndImage);
        hbox.setAlignment(Pos.CENTER);


        ToggleGroup group = new ToggleGroup();
        fiveRadio.setToggleGroup(group);
        elevenRadio.setToggleGroup(group);
        seventeenRadio.setToggleGroup(group);


        root.setTop(tileMap);
        root.setBottom(hbox);

        fiveRadio.setSelected(true);
        drawTileMap(tileMap, 5, 60.0,  120.0, 50.0 , stage);

        Scene scene = new Scene(root, windowWidth, windowHeight);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Hex Game");
        stage.show();

        startPlay(fiveRadio, elevenRadio, seventeenRadio, btnStart , stage);
        howToPlayButton();
    }
    private void startPlay(RadioButton fiveRadio, RadioButton elevenRadio, RadioButton seventeenRadio, Button btnStart , Stage stage) {
        fiveRadio.setOnAction(event -> {
            tileMap.getChildren().clear();
            drawTileMap(tileMap, 5, 60.0, 120, 50 , stage);
        });
        elevenRadio.setOnAction(event -> {
            tileMap.getChildren().clear();
            drawTileMap(tileMap, 11, 25.0, 80, 60 , stage);
        });
        seventeenRadio.setOnAction(event -> {
            tileMap.getChildren().clear();
            drawTileMap(tileMap, 17, 15, 70, 50 , stage);
        });
        btnStart.setOnAction(event -> {
            int choosen = 0;
            if (fiveRadio.isSelected())
                choosen = 5;
            else if (elevenRadio.isSelected())
                choosen = 11;
            else if (seventeenRadio.isSelected())
                choosen = 17;

            fiveRadio.setDisable(true);
            elevenRadio.setDisable(true);
            seventeenRadio.setDisable(true);
            btnStart.setDisable(true);

            hexGame.startGame(choosen);

            if(hexGame.isBlueTurn()){
                turnLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: Blue;");
                turnLabel.setText("Blue Turn");
            }
            else{
                turnLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: Red;");
                turnLabel.setText("Red Turn");
            }
        });
    }
    public void drawTileMap(AnchorPane tileMap, int rowCount, double r, double xStartOffset, double yStartOffset , Stage stage) {
        final double n = Math.sqrt(r * r * 0.75);
        final double TILE_HEIGHT = 2 * r + 6;
        final double TILE_WIDTH = 2 * n + 6;
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < rowCount; x++) {
                double xCoord = x * TILE_WIDTH + (y % rowCount) * (n + 2.75) + xStartOffset;
                double yCoord = y * TILE_HEIGHT * 0.75 + yStartOffset;

                Hexagon hex = new Hexagon(xCoord, yCoord, r, x, y, hexGame);
                hex.setOnMouseClicked(event -> {
                    if(hexGame.isGameStart()) {
                        hexGame.handleHexClick(hex);
                        if(hexGame.isGameStart()) {
                            if(hexGame.isBlueTurn()){
                                turnLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: Blue;");
                                turnLabel.setText("Blue Turn");
                            }
                            else
                            {
                                turnLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: Red;");
                                turnLabel.setText("Red Turn");
                            }
                        }
                        else
                        {
                            turnLabel.setVisible(false);
                            resultImageView.setVisible(true);
                            resultLabel.setVisible(true);
                            if (hexGame.isBlueTurn()) {
                                resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: Red;");
                                resultLabel.setText("Red Won"+"\n"+"Turn : "+hexGame.counterRed);
                                winner.setTitle("Red Won");
                                winner.setHeaderText("Congratulations! Red Won!");
                                winner.showAndWait();
                                restartGame(btnStart , stage);
                            } else {
                                resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-family: Arial; -fx-text-fill: Blue;");
                                resultLabel.setText("Blue Won"+"\n"+"Turn : "+hexGame.counterBlue);
                                winner.setTitle("Blue Won");
                                winner.setHeaderText("Congratulations! Blue Won!");
                                winner.showAndWait();
                                restartGame(btnStart , stage);
                            }
                        }
                    }
                });
                tileMap.getChildren().add(hex);
            }
        }
    }
    private void restartGame(Button btnStart , Stage stage){
        btnStart.setText("Play Again");
        btnStart.setPrefSize(150, 40);
        btnStart.setStyle("-fx-background-color: #00a408; -fx-background-radius: 10; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: Arial;");
        btnStart.setDisable(false);
        btnStart.setOnAction(event -> {
            start(stage);
        });
    }
    private void howToPlayButton() {
        btnHowToPlay.setOnAction(event -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL); //  bu pencere açıkken diğer pencerelerin tıklanmasını engeller.
            dialog.initOwner(null);  // Pencerenin sahibi olarak ana uygulamayı belirtir.
            dialog.initStyle(StageStyle.DECORATED);
            dialog.setResizable(false);

            Image image = new Image("file:image/hex-board.png");
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);

            Label instructions = new Label("\t\t\t\t\t\t\tHex Game\n" +"Hex is a strategy game involving two players. Hex is a simple game played on a rhombus grid, typically composed of hexagons. The game is played by two players and each player is assigned a specific color, traditionally red or blue. Each player is also assigned two opposite sides of the board. Players take turns placing a piece of their color in a single cell on the board. Once placed, the pieces cannot be moved but can be exchanged (only for the first moves, the second player either moves normally or exchanges his piece for the piece placed by the first player). The aim of each player is to create a connected path of their own pieces connects two board edges together. The player who completes such a connection wins the game.");
            instructions.setStyle("-fx-font-weight: bold; -fx-font-family: Arial; ");
            instructions.setWrapText(true);

            VBox content = new VBox(10, imageView, instructions);
            content.setPadding(new Insets(10));
            content.setAlignment(Pos.TOP_CENTER);


            Scene dialogScene = new Scene(content,500, 400);
            dialog.setScene(dialogScene);
            dialog.setTitle("How to Play");
            dialog.show();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
