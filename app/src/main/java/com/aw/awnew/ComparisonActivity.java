package com.aw.awnew;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.jme3.app.SimpleApplication;

public class ComparisonActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_jme);

        JMEFragment jmeFragment = (JMEFragment) getSupportFragmentManager().findFragmentById(R.id.jMEFragment);
        TestSizeComparison app = (TestSizeComparison) ((SimpleApplication) jmeFragment.getJmeApplication());

        Button back_button = (Button) findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.playBackwards();
            }
        });

        Button play_button = (Button) findViewById(R.id.play_button);

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.togglePlayPause();
            }
        });


        Button forward_button = (Button) findViewById(R.id.forward_button);

        forward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.playForwards();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

}
