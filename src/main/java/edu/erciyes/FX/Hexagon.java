package edu.erciyes.FX;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {
    int xIndex;
    int yIndex;
    private HexGame game;
    public Hexagon(double x, double y, double r, int xIndex, int yIndex, HexGame game) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.game = game;
        double n = Math.sqrt(r * r * 0.75);
        getPoints().addAll(
                x, y,
                x - n, y + r * 0.5,
                x - n, y + r * 1.5,
                x, y + 2 * r,
                x + n, y + r * 1.5,
                x + n, y + r * 0.5
        );
        setFill(Color.WHITE);
        setStroke(Color.BLACK);

        setOnMouseEntered(event -> {
            if (game.isGameStart() && getFill().equals(Color.WHITE)) {
                setScaleX(1.07);
                setScaleY(1.07);
                if(getFill().equals(Color.WHITE))
                {
                    if(game.isBlueTurn())
                        setFill(Color.LIGHTBLUE);
                    else
                        setFill(Color.PALEVIOLETRED);
                }
            }
        });

        setOnMouseExited(event -> {
            if (game.isGameStart()) {
                if(getFill().equals(Color.LIGHTBLUE) || getFill().equals(Color.PALEVIOLETRED)){
                    setFill(Color.WHITE);}
                setScaleX(1.0);
                setScaleY(1.0);
            }
        });
    }
}
