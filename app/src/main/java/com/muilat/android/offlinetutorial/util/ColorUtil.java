package com.muilat.android.offlinetutorial.util;

import android.graphics.Color;

import java.util.Random;

public class ColorUtil {
    public static Random RANDOM = new Random();

    public static int generateColor(){
        int red = RANDOM.nextInt(255);
        int green = RANDOM.nextInt(255);
        int blue = RANDOM.nextInt(255);

        return Color.rgb(red, green, blue);

    }
}
