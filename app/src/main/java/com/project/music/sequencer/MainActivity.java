package com.project.music.sequencer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout layout = (GridLayout)findViewById(R.id.mainLayout);
        View[][] buttonMatrix = new Button[layout.getRowCount()][layout.getColumnCount()];

        for(int row = 0; row < layout.getRowCount(); row++) {
            for(int column = 0; column < layout.getColumnCount(); column++) {
                View button = generateButton();
                buttonMatrix[row][column] = button;
                layout.addView(button);
            }
        }

        StepSequencer sequencer = new StepSequencer(layout.getRowCount(), layout.getColumnCount(), buttonMatrix);
        Thread myThread = new Thread(sequencer);
        myThread.start();

    }


    private View generateButton() {
        Button button;
        button = new Button(getApplicationContext());

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.isActivated()) {
                            v.setActivated(false);
                            v.setBackgroundResource(R.drawable.sequencer_theme_btn_default_holo_light);
                        } else {
                            v.setActivated(true);
                            v.setBackgroundResource(R.drawable.sequencer_theme_btn_default_focused_holo_light);
                        }
                    }
                });

        button.setBackgroundResource(R.drawable.sequencer_theme_btn_default_holo_light);

        return button;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
