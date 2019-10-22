package com.yyj.bestbase.base;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yyj.bestbase.R;


public class BaseToolbar {

    public Toolbar build(AppCompatActivity activity, String title, boolean isCenter) {
      return   build(activity, title, isCenter, null, null, null);
    }

    public Toolbar build(AppCompatActivity activity, String title, boolean isCenter, Class cla) {
        return build(activity, title, isCenter, cla, null, null);
    }

    public Toolbar build(AppCompatActivity activity, String title, boolean isCenter, Class cla, String rightText, View.OnClickListener clickListener) {
        return  build(activity, title, isCenter, cla, rightText, clickListener, null);
    }

    public Toolbar build(AppCompatActivity activity, String title, boolean isCenter, Class cla, String rightText, View.OnClickListener clickListener, Integer rightColor) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> {
            if (cla != null) {
                activity.startActivity(new Intent(activity, cla));
            }
            activity.finish();
        });
        if (rightText != null) {
            TextView toolbarRight = activity.findViewById(R.id.toolbar_right);
            toolbarRight.setText(rightText);
            toolbarRight.setOnClickListener(clickListener);
            if (rightColor != null) {
                toolbarRight.setTextColor(rightColor);
            }
        }
        toolbar.setTitle(title);
        if (isCenter) {
            setTitleCenter(toolbar,title);

        }
        return toolbar;
    }


    private static void setTitleCenter(Toolbar toolbar, String title) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.equals(textView.getText().toString())) {
                    textView.setGravity(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
        }
    }

}
