package io.fynn.g2048;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;

import java.util.Random;

public class Tile extends android.support.v7.widget.AppCompatImageView {

    Random r = new Random();

    int number;
    Point spot = null;

    public Tile(Context context, float posX, float posY, int width, int height) {
        super(context);

        number = r.nextBoolean() ? 2 : 4;

        setColor(number);

        this.setX(posX);
        this.setY(posY);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, height);
        this.setLayoutParams(params);
    }


    //Sets the color (and number) of the tile
    public void setColor(int number){
        if(number < 2048){
            int i = 0;
            while(number / Math.pow(2,i) != 2){
                i += 1;
            }

            int resourceId = this.getResources().getIdentifier("block" + (i + 1),"drawable",getContext().getPackageName());
            this.setImageResource(resourceId);
        }else{
            //Sets tile color to 2048
            this.setImageResource(R.drawable.block11);
        }
    }


    public void setPostion(float x,float y){
        setPosition(new PointF(x,y));
    }

    public void setPosition(PointF pos){
        this.setX(pos.x);
        this.setY(pos.y);
    }

    public void setSpot(Point p){
        spot = p;
    }

    public Point getSpot(){
       return spot;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int number){
        this.number = number;
    }


    public int randomNumber(int min, int max){
        int range = (max - min) + 1;
        return ((int) (Math.random() * range) + min);
    }
}
