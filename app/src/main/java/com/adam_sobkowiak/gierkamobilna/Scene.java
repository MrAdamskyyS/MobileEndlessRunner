package com.adam_sobkowiak.gierkamobilna;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    public void Update();
    public void draw(Canvas canvas);
    public void terminate();
    public void receiveTouch(MotionEvent event);
}
