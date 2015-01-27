package com.project.music.sequencer;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Random;


public class MainActivity extends Activity {

    public StepSequencer sequencer;
    GridLayout layout;
    View[][] buttonMatrix;
    static Thread myThread;
    int speed;
    private int defaultButton = R.drawable.sequencergrey_btn_default_normal_holo_light;
    private int pressedButton = R.drawable.sequencerred_btn_default_normal_holo_light;
    private int highlightedButton = R.drawable.sequencergreen_btn_default_normal_holo_light;
    private int rowButton = R.drawable.sequencergrey_btn_default_pressed_holo_light;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (GridLayout)findViewById(R.id.mainLayout);
        buttonMatrix = new Button[layout.getRowCount()][layout.getColumnCount()];

        for(int row = 0; row < layout.getRowCount(); row++) {
            for(int column = 0; column < layout.getColumnCount(); column++) {
                View button = generateButton();
                buttonMatrix[row][column] = button;
                layout.addView(button);
            }
        }

        speed = 550;


        // FILL SCALES SPINNER
        Spinner scales_spinner = (Spinner) findViewById(R.id.scales_array);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> scales_adapter = ArrayAdapter.createFromResource(this,
                R.array.scales_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        scales_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        scales_spinner.setAdapter(scales_adapter);

        // add button listener to play button and stop button
        ImageButton playButton=(ImageButton) findViewById(R.id.playButton);

        // add button listener to play button and stop button
        ImageButton stopButton=(ImageButton) findViewById(R.id.stopButton);

        ImageButton clearButton=(ImageButton) findViewById(R.id.clearButton);

        ImageButton randomButton=(ImageButton) findViewById(R.id.randomButton);

        SeekBar seekBar=(SeekBar) findViewById(R.id.tempoSeekBar);

        this.sequencer = new StepSequencer(layout.getRowCount(), layout.getColumnCount(), buttonMatrix, speed);
        myThread = new Thread(this.sequencer);
        myThread.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                StepSequencer.updateTempo(progressValue + 250);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });



        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.this.getSequencer().startPlay();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { MainActivity.this.getSequencer().stopPlay(); }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.getSequencer().stopPlay();

                for(int i = 0; i < layout.getRowCount(); i++) {
                    for(int j = 0; j < layout.getColumnCount(); j++) {
                        buttonMatrix[i][j].setActivated(false);
                        buttonMatrix[i][j].setBackgroundResource(MainActivity.this.defaultButton);
                    }
                }

                MainActivity.this.getSequencer().startPlay();
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.getSequencer().stopPlay();
                Random rand = new Random();

                for(int i = 0; i < layout.getRowCount(); i++) {
                    for(int j = 0; j < layout.getColumnCount(); j++) {
                        buttonMatrix[i][j].setActivated(false);
                        buttonMatrix[i][j].setBackgroundResource(MainActivity.this.defaultButton);
                    }
                }

                for(int i = 0; i < layout.getColumnCount(); i++) {
                    int note = (rand.nextInt(8));
                    if(note != 0) {
                        buttonMatrix[note][i].setActivated(true);
                        buttonMatrix[note][i].setBackgroundResource(MainActivity.this.pressedButton);
                    }
                }

                MainActivity.this.getSequencer().startPlay();
            }
        });

        scales_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selection = parentView.getItemAtPosition(position).toString();

                MainActivity.this.getSequencer().changeScale(selection);

                Context context = getApplicationContext();
                CharSequence text = selection;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;//MainActivity.this.getSequencer().changeScale("CMajor");
            }
        });


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
                            v.setBackgroundResource(MainActivity.this.defaultButton);
                        } else {
                            v.setActivated(true);
                            v.setBackgroundResource(MainActivity.this.pressedButton);
                        }
                    }
                });

        button.setBackgroundResource(this.defaultButton);

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

    public StepSequencer getSequencer()
    {
        return this.sequencer;
    }
}
