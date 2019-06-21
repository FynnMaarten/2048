package io.fynn.g2048.helpers;

import android.animation.Animator;
import android.content.Context;
import android.graphics.PointF;

import io.fynn.g2048.Game2048;
import io.fynn.g2048.Tile;

public class AnimationListener implements Animator.AnimatorListener {

    Tile tile;
    Context context;
    static int animationcount;
    public static boolean animrunning = false;

    public AnimationListener(Context context, Tile tile) {
        this.context = context;
        this.tile = tile;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        animationcount += 1;
        animrunning = true;
    }

    @Override
    public void onAnimationEnd(final Animator animation) {
        animationcount -= 1;

        tile.setColor(tile.getNumber());
        tile.setScaleX(1f);
        tile.setScaleY(1f);

        if(animationcount == 0 && Game2048.moved){
            animrunning = false;
            Game2048.createTile(context);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
