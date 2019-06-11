package io.fynn.g2048.helpers;

import android.view.MotionEvent;
import android.view.View;

public class SwipeManager implements View.OnTouchListener {

    float prevX, prevY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        onTouched();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    prevX = event.getX();
                    prevY = event.getY();

                    return true;
                case MotionEvent.ACTION_MOVE:
                    float newX = event.getX();
                    float newY = event.getY();


                    //Calculates swipe direction

                    if (Math.abs(newX - prevX) > Math.abs(newY - prevY) && Math.abs(newX - prevX) > 20) {

                        if (newX > prevX) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    else if (Math.abs(newX - prevX) < Math.abs(newY - prevY) && Math.abs(newY - prevY) > 20) {

                        if (newY > prevY) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    onFingerLiftOff();
                    break;
            }
        return false;
    }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeUp() {
        }

        public void onSwipeDown() {
        }

        public void onTouched() {

        }

        public void onFingerLiftOff(){
        }

}
