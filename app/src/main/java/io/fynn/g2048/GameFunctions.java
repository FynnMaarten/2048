package io.fynn.g2048;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Collections;

import io.fynn.g2048.enums.RotationDirection;
import io.fynn.g2048.enums.SwipeDirection;
import io.fynn.g2048.helpers.AnimationListener;
import io.fynn.g2048.helpers.AnimationRunnable;

public class GameFunctions {
    //Basic functions of 2048

    public static ArrayList<Tile> listremove = new ArrayList<>();

    public Tile[] slide(Tile[] arr, SwipeDirection direction, PointF[][] coordinates) {
        ArrayList<Tile> arrlist = new ArrayList<>(arr.length);
        for (Tile t : arr) {
            arrlist.add(t);
        }

        arrlist.removeAll(Collections.singleton(null));

        //Check if they can merge and merge them
        if (direction.equals(SwipeDirection.SWIPE_LEFT) || direction.equals(SwipeDirection.SWIPE_DOWN)) {
            for (int i = 0; i < arrlist.size() - 1; i++) {
                if (arrlist.get(i).getNumber() == arrlist.get(i + 1).getNumber()) {

                    int number = arrlist.get(i).getNumber() * 2 + 1;

                    arrlist.get(i).setNumber(number);
                    arrlist.get(i).merge = true;

                    Point p = direction.equals(SwipeDirection.SWIPE_LEFT) ? new Point(arrlist.get(i + 1).getSpot().x, i) : new Point(3 - i, arrlist.get(i + 1).getSpot().y);

                    arrlist.get(i + 1).remove = true;
                    listremove.add(arrlist.get(i + 1));
                    arrlist.get(i + 1).animate().
                            translationX(coordinates[p.x][p.y].x).
                            translationY(coordinates[p.x][p.y].y).
                            setDuration(!arrlist.get(i + 1).getSpot().equals(p) ? 250 : 0).
                            withEndAction(new AnimationRunnable(arrlist.get(i + 1)) {
                                @Override
                                public void run() {
                                    Game2048.removeTile(tile);
                                }
                            });

                    arrlist.remove(i + 1);

                    Game2048.moved = true;
                    Game2048.score += number;
                    Game2048.highscore = (Game2048.score > Game2048.highscore) ? Game2048.score : Game2048.highscore;
                }
            }
        } else if (direction.equals(SwipeDirection.SWIPE_RIGHT) || direction.equals(SwipeDirection.SWIPE_UP)) {
            for (int i = arrlist.size() - 1; i >= 1; i--) {
                if (arrlist.get(i).getNumber() == arrlist.get(i - 1).getNumber()) {

                    int number = arrlist.get(i).getNumber() * 2 + 1;

                    arrlist.get(i).setNumber(number);
                    arrlist.get(i).merge = true;

                    Point p = direction.equals(SwipeDirection.SWIPE_RIGHT) ? new Point(arrlist.get(i - 1).getSpot().x, 3 - (arrlist.size() - i - 1)) : new Point(arrlist.size() - i - 1, arrlist.get(i - 1).getSpot().y);

                    arrlist.get(i - 1).remove = true;
                    listremove.add(arrlist.get(i - 1));
                    arrlist.get(i - 1).animate().
                            translationX(coordinates[p.x][p.y].x).
                            translationY(coordinates[p.x][p.y].y).
                            setDuration(!arrlist.get(i - 1).getSpot().equals(p) ? 250 : 0).
                            withEndAction(new AnimationRunnable(arrlist.get(i - 1)) {
                                @Override
                                public void run() {
                                    Game2048.removeTile(tile);
                                }
                            });

                    arrlist.remove(i - 1);

                    Game2048.moved = true;
                    Game2048.score += number;
                    Game2048.highscore = (Game2048.score > Game2048.highscore) ? Game2048.score : Game2048.highscore;
                }
            }
        }

        for (int i = 0; i < arrlist.size(); i++) {
            if (arrlist.get(i).getNumber() / 2f != Math.floor(arrlist.get(i).getNumber() / 2f)) {
                arrlist.get(i).setNumber(arrlist.get(i).getNumber() - 1);
            }
        }

        int missing = 4 - arrlist.size();

        //makes an list with the size of missing,filled with zeros
        ArrayList<Tile> zeros = new ArrayList<>(Collections.nCopies(missing, (Tile) null));

        if (zeros.size() > 0) {
            if (direction.equals(SwipeDirection.SWIPE_LEFT) || direction.equals(SwipeDirection.SWIPE_DOWN)) {
                //Add zeros at the end of the list
                arrlist.addAll(zeros);
            } else if (direction.equals(SwipeDirection.SWIPE_RIGHT) || direction.equals(SwipeDirection.SWIPE_UP)) {
                //Add zeros at the beginning of the list
                arrlist.addAll(0, zeros);
            }
        }

        for (int i = 0; i < arrlist.size(); i++) {
            arr[i] = arrlist.get(i);
        }

        return arr;
    }

    public Tile[][] rotateArray(Tile[][] arr, RotationDirection rotateDirection) {
        Tile[][] arr1 = new Tile[4][4];
        if (rotateDirection.equals(RotationDirection.COUNTERCLOCKWISE)) {
            for (int i = 0; i < arr[0].length; i++) {
                for (int j = 0; j < arr.length; j++) {
                    arr1[arr[0].length - (j + 1)][i] = arr[i][j];
                }
            }
        } else if (rotateDirection.equals(RotationDirection.CLOCKWISE)) {
            for (int i = 0; i < arr[0].length; i++) {
                for (int j = 0; j < arr.length; j++) {
                    arr1[j][arr[0].length - (i + 1)] = arr[i][j];
                }
            }
        }

        return arr1;
    }
}
