package io.fynn.g2048;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.fynn.g2048.enums.RotationDirection;
import io.fynn.g2048.enums.SwipeDirection;
import io.fynn.g2048.helpers.AnimationRunnable;
import io.fynn.g2048.helpers.SwipeManager;

public class Game2048 extends AppCompatActivity {
    //UI Stuff
    public static ConstraintLayout layout;
    ImageView grid, filter;
    TextView scoretext, highscoretext, gamestatetext, continuetext;
    View view;

    //Arrays
    public PointF[][] coordinates = new PointF[4][4];

    //Gameboard reflecting the game graphically(T)
    Tile[][] gameboard = new Tile[4][4];

    //variables
    public static boolean moved = false;
    public static boolean fingerliftoff = true;
    public static int score = 0;
    public static int highscore = 0;
    public static int animationcount = 0;
    public static boolean animfinished = true;
    public static boolean updated = false;
    public static int updatecount = 0;
    Point optionbefore;

    //Other important classes
    GameFunctions gameFunctions = new GameFunctions();

    //Native classes
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    Timer timer = new Timer();
    Handler handler = new Handler();
    Object lock = new Object();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);

        overridePendingTransition(0, 0);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ed = sp.edit();

        layout = findViewById(R.id.cl2048);
        scoretext = findViewById(R.id.score2048);
        highscoretext = findViewById(R.id.highscore2048);
        gamestatetext = findViewById(R.id.gamestate2048);
        continuetext = findViewById(R.id.continue2048);
        filter = findViewById(R.id.filter2048);
        view = findViewById(R.id.view19);

        int var = sp.getInt("highscore", 0);
        highscore = (var > 0) ? var : 0;
        highscoretext.setText(highscore + "");

        grid = findViewById(R.id.grid_frame);
        grid.post(new Runnable() {
            @Override
            public void run() {
                coordinates = makeCoords(grid);
                createTile(grid, gameboard);
                createTile(grid, gameboard);

            }
        });

        layout.setOnTouchListener(new SwipeManager() {
            public void onTouched() {
                if (fingerliftoff) {
                    if (filter.getVisibility() == View.VISIBLE) {
                        filter.setVisibility(View.INVISIBLE);
                        gamestatetext.setText(null);
                        continuetext.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.VISIBLE);

                        RestartGame(gameboard);

                    }
                }
            }

            public synchronized void onSwipeRight() {
                if (fingerliftoff) {
                    fingerliftoff = false;
                    if (updatecount < 2) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (lock) {
                                    if (!updated) {
                                        try {
                                            lock.wait();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updated = false;
                                            for (int i = 0; i < 4; i++) {
                                                gameFunctions.slide(gameboard[i], SwipeDirection.SWIPE_RIGHT, coordinates);
                                            }

                                            update();
                                        }
                                    });

                                }
                            }
                        }).start();
                    }else{
                        System.out.println("denied");
                    }
                }
            }

            public synchronized void onSwipeLeft() {
                if (fingerliftoff) {
                    fingerliftoff = false;
                    if (updatecount <= 2) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (lock) {
                                    if (!updated) {
                                        try {
                                            lock.wait();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updated = false;
                                            for (int i = 0; i < 4; i++) {
                                                gameFunctions.slide(gameboard[i], SwipeDirection.SWIPE_LEFT, coordinates);
                                            }

                                            update();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }else{
                        System.out.println("denied");
                    }
                }
            }

            //For swiping up/down first rotate clockwise, slide tiles over and rotate counterclockwise

            public synchronized void onSwipeUp() {
                if (fingerliftoff) {
                    fingerliftoff = false;
                    if (updatecount <= 2) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (lock) {
                                    if (!updated) {
                                        try {
                                            lock.wait();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updated = false;
                                            //Rotate matrices
                                            gameboard = gameFunctions.rotateArray(gameboard, RotationDirection.CLOCKWISE);

                                            for (int i = 0; i < 4; i++) {
                                                gameFunctions.slide(gameboard[i], SwipeDirection.SWIPE_UP, coordinates);
                                            }

                                            //Rotate them back
                                            gameboard = gameFunctions.rotateArray(gameboard, RotationDirection.COUNTERCLOCKWISE);

                                            update();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }else{
                        System.out.println("denied");
                    }
                }
            }

            public synchronized void onSwipeDown() {
                if (fingerliftoff) {
                    fingerliftoff = false;
                    if (updatecount <= 2) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (lock) {
                                    if (!updated) {
                                        try {
                                            lock.wait();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updated = false;
                                            //Rotate matrices
                                            gameboard = gameFunctions.rotateArray(gameboard, RotationDirection.CLOCKWISE);

                                            for (int i = 0; i < 4; i++) {
                                                gameFunctions.slide(gameboard[i], SwipeDirection.SWIPE_DOWN, coordinates);
                                            }

                                            //Rotate them back
                                            gameboard = gameFunctions.rotateArray(gameboard, RotationDirection.COUNTERCLOCKWISE);

                                            update();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }else{
                        System.out.println("denied");
                    }
                }
            }

            public void onFingerLiftOff() {
                fingerliftoff = true;
            }

        });

    }


    public synchronized void update() {

        boolean won = false;
        animfinished = false;
        updated = false;
        updatecount += 1;
        System.out.println("updatecount :" + updatecount);

        //Set UI components to match Array reflections
        for (int i = 0; i < gameboard[0].length; i++) {
            for (int j = 0; j < gameboard[1].length; j++) {
                if (gameboard[i][j] != null) {
                    Point spot = new Point(i, j);
                    if (!gameboard[i][j].getSpot().equals(spot) || moved) {
                        animationcount += 1;
                        gameboard[i][j].animate().
                                translationX(coordinates[i][j].x).
                                translationY(coordinates[i][j].y).
                                setDuration(spot != gameboard[i][j].getSpot() ? 75 : 0).
                                withEndAction(new AnimationRunnable(gameboard[i][j]) {
                                    @Override
                                    public void run() {
                                        if (tile.merge) {
                                            tile.merge = false;
                                            tile.animate().
                                                    scaleX(1.05f).
                                                    scaleY(1.05f).
                                                    setDuration(35).
                                                    withEndAction(new AnimationRunnable(tile) {
                                                        @Override
                                                        public void run() {
                                                            tile.setColor(tile.getNumber());
                                                            tile.animate().
                                                                    scaleX(1).
                                                                    scaleY(1).
                                                                    setDuration(35).
                                                                    withEndAction(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            animationcount -= 1;
                                                                            if (animationcount == 0) {
                                                                                animfinished = true;
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    });
                                        } else {
                                            animationcount -= 1;
                                            if (animationcount == 0) {
                                                animfinished = true;
                                            }
                                        }
                                    }
                                });

                        moved = true;
                    }

                    if (gameboard[i][j].getNumber() == 2048) {
                        won = true;
                    }

                    gameboard[i][j].setSpot(new Point(i, j));
                }
            }
        }
        if (animationcount == 0) {
            animfinished = true;
        }


        scoretext.setText(score + "");
        highscoretext.setText(highscore + "");

        ed.putInt("highscore", highscore);
        ed.commit();


        if (won) {
            filter.setVisibility(View.VISIBLE);
            continuetext.setVisibility(View.VISIBLE);
            gamestatetext.setText("You won!");
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (lock) {
                            if (animfinished && moved) {
                                createTile(grid, gameboard);
                                animfinished = false;
                                updatecount -= 1;
                            } else if (animfinished && !updated) {
                                updated = true;
                                updatecount -= 1;
                                animfinished = false;
                                lock.notify();
                            }
                        }
                    }
                });
            }
        }, 0, 20);

    }


    public synchronized void createTile(ImageView grid, Tile[][] gameboard) {

        ArrayList<Point> options = getOptions();

        if (options.size() > 0) {

            Point spot = options.get(randomNumber(0, options.size() - 1));
            PointF spotCoords = coordinates[spot.x][spot.y];
            optionbefore = spot;

            float percentagetileX = 144f / 656.6f;
            float percentagetileY = percentagetileX;

            Tile t = new Tile(this, spotCoords.x, spotCoords.y, ((int) (grid.getWidth() * percentagetileX)), ((int) (grid.getHeight() * percentagetileY)));
            t.setSpot(spot);
            layout.addView(t);

            t.animate().scaleX(1f).scaleY(1f).setDuration(50).withEndAction(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        moved = false;
                        updated = true;
                        lock.notify();
                    }
                }
            });
            gameboard[spot.x][spot.y] = t;

            if (isGameOver()) {
                filter.setVisibility(View.VISIBLE);
                continuetext.setVisibility(View.VISIBLE);
                gamestatetext.setText("Game Over!");
            }
        }
    }

    public static void removeTile(Tile tile) {
        layout.removeView(tile);
    }

    public void RestartGame(Tile[][] gameboard) {
        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard[0].length; j++) {
                layout.removeView(gameboard[i][j]);
                gameboard[i][j] = null;
            }
        }

        score = 0;
        scoretext.setText(score + "");

        fingerliftoff = true;
        moved = false;
        animationcount = 0;
        animfinished = false;
        updated = false;

        optionbefore = null;

        view.setVisibility(View.INVISIBLE);
        createTile(grid, gameboard);

    }

    public boolean isGameOver() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameboard[i][j] == null || (i != 3 && gameboard[i + 1][j] == null) || (j != 3 && gameboard[i][j + 1] == null)) {
                    return false;
                }
                if (i != 3 && gameboard[i][j].getNumber() == gameboard[i + 1][j].getNumber()) {
                    return false;
                }

                if (j != 3 && gameboard[i][j].getNumber() == gameboard[i][j + 1].getNumber()) {
                    return false;
                }
            }
        }
        return true;
    }

    public PointF[][] makeCoords(ImageView grid) {

        PointF[][] pointf = new PointF[4][4];

        float percentageblockX = 144f / 656.6f;
        float percentageblockY = percentageblockX;
        float percantegespace = 16.12f / 656.6f;

        float blocksizeX = grid.getWidth() * percentageblockX;
        float blocksizeY = grid.getHeight() * percentageblockY;

        float space = grid.getWidth() * percantegespace;

        for (int i = 0; i < pointf[0].length; i++) {
            for (int j = 0; j < pointf[1].length; j++) {
                float x = grid.getLeft() + (space * (j + 1) + (blocksizeX * j));
                float y = grid.getTop() + (space * (i + 1) + (blocksizeY * i));
                pointf[i][j] = new PointF(x, y);
            }
        }

        return pointf;
    }

    public ArrayList<Point> getOptions() {
        ArrayList<Point> options = new ArrayList<>();
        options.clear();

        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard[0].length; j++) {
                if (gameboard[i][j] == null) {
                    options.add(new Point(i, j));
                }
            }
        }

        if (options.size() > 1) {
            options.remove(optionbefore);
        }

        return options;
    }

    public int randomNumber(int min, int max) {
        int range = (max - min) + 1;
        return ((int) (Math.random() * range) + min);
    }

    @Override
    public void onStop() {
        super.onStop();
        fingerliftoff = true;
        moved = false;
        score = 0;
        highscore = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
