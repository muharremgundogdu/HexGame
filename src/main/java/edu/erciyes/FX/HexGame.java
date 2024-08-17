package edu.erciyes.FX;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import java.util.Random;

public class HexGame {
    private String[][] location;
    private boolean isBlueTurn;
    private boolean isGameStart;
    private boolean isFirstMove;
    private boolean isSecondPlayerFirstMove;
    private boolean secondPlayerSpecialMoveUsed;
    private Disjoint dsRed, dsBlue;
    private String topNodeRed, bottomNodeRed, leftNodeBlue, rightNodeBlue;
    public int counterBlue = 0;
    public int counterRed = 0;

    public HexGame() {
        isBlueTurn = new Random().nextBoolean(); // rastgele başlayacak oyuncuyu belirleme
        isGameStart = false;
        isFirstMove = true;
        isSecondPlayerFirstMove = false;
        secondPlayerSpecialMoveUsed = false;
    }

    public void startGame(int size) {
        location = new String[size][size];
        isGameStart = true;
        isFirstMove = true;
        isSecondPlayerFirstMove = false;
        secondPlayerSpecialMoveUsed = false;
        initializeDisjointSets(size);
    }

    private void initializeDisjointSets(int size) {
        dsRed = new Disjoint();
        dsBlue = new Disjoint();
        topNodeRed = "topRed";
        bottomNodeRed = "botRed";
        leftNodeBlue = "leftBlue";
        rightNodeBlue = "rightBlue";

        dsRed.makeSet(topNodeRed);
        dsRed.makeSet(bottomNodeRed);
        dsBlue.makeSet(leftNodeBlue);
        dsBlue.makeSet(rightNodeBlue);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                dsRed.makeSet(i + "," + j);
                dsBlue.makeSet(i + "," + j);
            }
        }

        for (int i = 0; i < size; i++) {
            dsRed.union(topNodeRed, i + ",0");
            dsRed.union(bottomNodeRed, i + "," + (size - 1));
            dsBlue.union(leftNodeBlue, "0," + i);
            dsBlue.union(rightNodeBlue, (size - 1) + "," + i);
        }
    }

    public void handleHexClick(Hexagon hex) {
        if (!isGameStart) {
            return;
        }

        if (!hex.getFill().equals(Color.BLUE) && !hex.getFill().equals(Color.RED)) {
            if (isBlueTurn) {
                counterBlue++;
                hex.setFill(Color.BLUE);
                location[hex.xIndex][hex.yIndex] = "Blue";
                connectNeighbors(hex.xIndex, hex.yIndex, "Blue");
                if (dsBlue.find(leftNodeBlue).equals(dsBlue.find(rightNodeBlue))) {
                    isGameStart = false;
                    System.out.println("Blue Win!");
                }
            }
            else{
                counterRed++;
                hex.setFill(Color.RED);
                location[hex.xIndex][hex.yIndex] = "Red";
                connectNeighbors(hex.xIndex, hex.yIndex, "Red");
                if (dsRed.find(topNodeRed).equals(dsRed.find(bottomNodeRed))) {
                    isGameStart = false;
                    System.out.println("Red Win!");
                }
            }
            isBlueTurn = !isBlueTurn;
            if (isFirstMove) {
                isFirstMove = false;
                isSecondPlayerFirstMove = true;
            }
            else if (isSecondPlayerFirstMove) {
                isSecondPlayerFirstMove = false;
            }
        }
        else if (isSecondPlayerFirstMove && !secondPlayerSpecialMoveUsed) {
            // İkinci oyuncunun ilk hamlesinde rakibin taşını çevirme
            if (hex.getFill().equals(Color.BLUE) && !isBlueTurn) {
                counterRed++;
                hex.setFill(Color.RED);
                location[hex.xIndex][hex.yIndex] = "Red";
                connectNeighbors(hex.xIndex, hex.yIndex, "Red");
                if (dsRed.find(topNodeRed).equals(dsRed.find(bottomNodeRed))) {
                    isGameStart = false;
                    System.out.println("Red Win!");
                }
                isSecondPlayerFirstMove = false;
                secondPlayerSpecialMoveUsed = true;
                isBlueTurn = true;
            }
            else if (hex.getFill().equals(Color.RED) && isBlueTurn) {
                counterBlue++;
                hex.setFill(Color.BLUE);
                location[hex.xIndex][hex.yIndex] = "Blue";
                connectNeighbors(hex.xIndex, hex.yIndex, "Blue");
                if (dsBlue.find(leftNodeBlue).equals(dsBlue.find(rightNodeBlue))) {
                    isGameStart = false;
                    System.out.println("Blue Win!");

                }
                isSecondPlayerFirstMove = false;
                secondPlayerSpecialMoveUsed = true;
                isBlueTurn = false;
            }
        }
    }

    private void connectNeighbors(int x, int y, String color) {
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, -1}, {-1, 1}
        };
        String current = x + "," + y;
        Disjoint ds = color.equals("Red") ? dsRed : dsBlue;

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < location.length && ny >= 0 && ny < location.length && color.equals(location[nx][ny])) {
                ds.union(current, nx + "," + ny);
            }
        }
    }

    public boolean isGameStart() {
        return isGameStart;
    }

    public boolean isBlueTurn() {
        return isBlueTurn;
    }
}
