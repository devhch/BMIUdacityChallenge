package com.devhch.bmiudacitychallenge;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SeekBar seek_bar;

    private TextView textheight, textWeight, calBmi;

    private Button add, remove;

    private int MAX = 250; // Highest value for text size
    private int MIN = 20; // Lowest value for text size
    private int PLUS = 1; //Provide the text size for each step

    private double initialHeight = 176, initialWeight = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        textWeight = findViewById(R.id.textWeight);
        textheight = findViewById(R.id.textheight);
        seek_bar = findViewById(R.id.seek_bar);
        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);
        calBmi = findViewById(R.id.cal_bmi);

        seek_bar.setProgress((int) initialHeight);
        seek_bar.setMax((MAX - MIN) / PLUS);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                initialHeight = (MIN + (progress * PLUS));
                textheight.setText(String.valueOf(initialHeight));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        add.setOnClickListener(view -> {

            add.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            });

            initialWeight = add(initialWeight);
            textWeight.setText(String.valueOf(initialWeight));
        });

        remove.setOnClickListener(view -> {

            remove.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(
                                0xe0f47521,
                                PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            });

            initialWeight = remove(initialWeight);
            textWeight.setText(String.valueOf(initialWeight));
        });

        calBmi.setOnClickListener(view -> {

            BigDecimal bd = new BigDecimal(
                    calculateBmi((initialHeight / 100), initialWeight)
            ).setScale(2, RoundingMode.HALF_UP);

            double bmi = bd.doubleValue();

            String textNormal = "";
            String bmiMsg;
            int color = Color.GREEN;

            if (bmi <= 18.5) {
                textNormal = "UnderWeight";
                color = Color.RED;
            } else if (bmi <= 25 && bmi > 18.5) {
                textNormal = "Normal";
                color = Color.GREEN;
            } else if (bmi < 30 && bmi > 25) {
                textNormal = "Overweight";
                color = Color.GRAY;
            } else if (bmi >= 30) {
                textNormal = "Obese ";
                color = Color.RED;
            }

            bmiMsg = "You have a " + textNormal + " body weight";

            showResultDialog(
                    textNormal,
                    bmi + "",
                    bmiMsg,
                    color,
                    initialHeight,
                    initialWeight);
        });
    }

    private void showResultDialog(String normalText, String bmiText,
                                  String bmiMsgText, int textNormalColor,
                                  double height, double weight) {

        TextView normal, bmi, bmi_msg, re_calculate_bu, resultHeight;
        Dialog dialog;

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.result_layout);
        Window dialogWindow = dialog.getWindow();

        assert dialogWindow != null;
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAimation;


        normal = dialog.findViewById(R.id.normal);
        bmi = dialog.findViewById(R.id.bmi);
        bmi_msg = dialog.findViewById(R.id.bmi_msg);
        re_calculate_bu = dialog.findViewById(R.id.re_calculate_bu);
        resultHeight = dialog.findViewById(R.id.resultHeight);

        resultHeight.setText( height
                + " cm, " + weight + " kg."
        );

        normal.setText(normalText);
        normal.setTextColor(textNormalColor);
        bmi.setText(bmiText);
        bmi_msg.setText(bmiMsgText);

        re_calculate_bu.setOnClickListener(view -> {

            //buTwo
            dialog.dismiss();

        });

        dialog.setCancelable(true);
        window.setLayout(android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private double calculateBmi(double height, double weight) {
        return weight / (height * height); //BMI = weight kg/(height(m) * height(m))
    }

    private double add(double add_weight) {
        if (add_weight >= 200)
            return 200;
        else
            return add_weight + 1;
    }

    private double remove(double add_remove) {
        if (add_remove <= 20)
            return 20;
        else
            return add_remove - 1;
    }

    // private static DecimalFormat df2 = new DecimalFormat("#.##");
//    public static double round(double A, int B){
//        return (double) ( (int) (A * Math.pow(10,B) + .5)) / Math.pow(10, B);
//    }
}
