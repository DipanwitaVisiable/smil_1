package com.example.crypedu.Helper;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fonts {
    private AssetManager mngr;

    public Fonts(Context context) {
        mngr = context.getAssets();
    }
    private enum AssetTypefaces {
        RobotoLight,
    }

    private Typeface getTypeface() {
        Typeface tf;
        switch (AssetTypefaces.RobotoLight) {
            case RobotoLight:
                tf = Typeface.createFromAsset(mngr, "fonts/OpenSans-Regular.ttf");
                break;
            default:
                tf = Typeface.DEFAULT;
                break;
        }
        return tf;
    }
    private void setupLayoutTypefaces(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setupLayoutTypefaces(child);
                }
            } else if (v instanceof TextView) {
                if (v.getTag().toString().equals(AssetTypefaces.RobotoLight.toString())){
                    ((TextView)v).setTypeface(getTypeface());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }
}
