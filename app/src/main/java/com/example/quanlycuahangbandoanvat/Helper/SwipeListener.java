package com.example.quanlycuahangbandoanvat.Helper;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class SwipeListener extends GestureDetector.SimpleOnGestureListener {

    private ViewFlipper viewFlipper;

    public SwipeListener(ViewFlipper viewFlipper) {
        this.viewFlipper = viewFlipper;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float deltaX = e2.getX() - e1.getX();
        if (deltaX > 100) { // Threshold for left swipe (adjust as needed)
            viewFlipper.showNext();
        } else if (deltaX < -100) { // Threshold for right swipe (adjust as needed)
            viewFlipper.showPrevious();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
