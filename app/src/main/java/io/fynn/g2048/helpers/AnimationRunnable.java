package io.fynn.g2048.helpers;

import android.animation.Animator;
import android.graphics.Point;

import java.util.ArrayList;

import io.fynn.g2048.Tile;

public class AnimationRunnable implements Runnable {

    public int i = 0;
    public int j = 0;
    public Tile tile;

    public AnimationRunnable(Tile tile){
        this.tile = tile;
    }

    @Override
    public void run() {

    }
}
