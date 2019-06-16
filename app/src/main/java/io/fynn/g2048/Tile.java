package io.fynn.g2048;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import java.util.Random;

public class Tile extends android.support.v7.widget.AppCompatTextView {

    Random r = new Random();

    int number;
    Point spot = null;
    boolean remove = false;
    public boolean merge = false;

    Context context;

    public Tile(Context context, float posX, float posY, int width, int height) {
        super(context);

        this.context = context;

        number = r.nextBoolean() ? 2 : 4;

        setColor(number);

        this.setX(posX);
        this.setY(posY);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, height);
        this.setLayoutParams(params);

        this.setScaleX(0.8f);
        this.setScaleY(0.8f);
    }




    //Sets the color (and number) of the tile
    public void setColor(int number) {
        int resourceId = this.getResources().getIdentifier("tile_" + number, "drawable", getContext().getPackageName());
        this.setBackground(context.getDrawable(resourceId));
    }


    public void setPostion(float x, float y) {
        setPosition(new PointF(x, y));
    }

    public void setPosition(PointF pos) {
        this.setX(pos.x);
        this.setY(pos.y);
    }

    public void setSpot(Point p) {
        spot = p;
    }

    public Point getSpot() {
        return spot;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public int randomNumber(int min, int max) {
        int range = (max - min) + 1;
        return ((int) (Math.random() * range) + min);
    }
}
