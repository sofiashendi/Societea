package com.example.android.societea;

import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selectTea(View v) {
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.timerView);
        RelativeLayout childLayout = createTimerBlock();
        Button newButton = createNewButton();
        Button clickButton = (Button)v;
        final TextView newChrono = createNewTextview();
        final String teaSelector = clickButton.getText().toString();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//      Start the timer for the tea selected
        new CountDownTimer(getTeaTimerLength(teaSelector), 1000) {

            public void onTick(long millisUntilFinished) {
                newChrono.setText(teaSelector + " " + getResources().getString(R.string.tea) + "\n" + String.format("%d min %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {

//              Update the text & look
                newChrono.setText(getResources().getString(R.string.tea_ready_1) + " " + teaSelector + " " + getResources().getString(R.string.tea_ready_2));
                newChrono.setTextColor(Color.parseColor("#60de92"));
                newChrono.setAllCaps(false);

//              Make the text blink a few times
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(500);
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(4);
                newChrono.startAnimation(anim);

//              Play a sound to alert the user
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }

        }.start();

//      Finalize layout parameters and create new block
        layoutParams.setMargins(10, 20, 10, 0);
        parentLayout.addView(childLayout, layoutParams);

        childLayout.addView(newButton);
        childLayout.addView(newChrono);
    }

//  Get the value for the timer based on the tea type
    private int getTeaTimerLength(String teaType) {
        int seconds = 0;
        int testTotal = 3000;
        int greenTotal = 90000;
        int blackTotal = 180000;
        int oolongTotal = 240000;
        int whiteTotal = 360000;

        switch (teaType) {
            case "green":
                seconds = greenTotal;
                break;
            case "black":
                seconds = blackTotal;
                break;
            case "oolong":
                seconds = oolongTotal;
                break;
            case "white":
                seconds = whiteTotal;
                break;
        }

        return seconds;
    }

//  Create RelativeLayout for Timer Block
    private RelativeLayout createTimerBlock() {
        RelativeLayout layout = new RelativeLayout(this);

        layout.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
        layout.setPadding(10, 20, 10, 20);

        return layout;
    }

//  Create TextView for Timer Block
    private TextView createNewTextview() {
        TextView textview = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        textview.setGravity(Gravity.CENTER);
        textview.setPadding(0, 30, 0, 0);
        textview.setTextAppearance(getApplicationContext(), R.style.TimerTextView);
        textview.setLayoutParams(layoutParams);

        return textview;
    }

//  Create Button for Timer Block
    private Button createNewButton() {
        Button button = new Button(this);
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(80, 80);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        button.setBackgroundResource(R.drawable.close);
        button.setPadding(0, 0, 20, 0);

        button.setLayoutParams(buttonParams);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout parentLayout = (LinearLayout) findViewById(R.id.timerView);

                parentLayout.removeView((View) v.getParent());

            }
        });

        return button;
    }

//  Let the user email his friends to talk about the app
    public void shareApp(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.email_message));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

//  Lead the user to our Facebook page once it's live
    public void followLink(View v) {
        Toast.makeText(MainActivity.this, R.string.coming_soon, Toast.LENGTH_SHORT).show();
    }

}
