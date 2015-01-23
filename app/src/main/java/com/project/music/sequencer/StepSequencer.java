package com.project.music.sequencer;

import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * Created by john on 17/12/14.
 */
public class StepSequencer implements Runnable{

    private int rowCount;
    private int colCount;
    public boolean isPlaying = true;
    public PlayService playService;
    private static View[][] buttonMatrix;
    private static int speed;

    public StepSequencer(int rowCount, int colCount, View[][] buttonMatrix, int speed) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.buttonMatrix = buttonMatrix;
        this.speed = speed;
    }

    public void run() {

        boolean play = this.isPlaying;

        playService = new PlayService();


        while(true) {
            for (int i = 0; i < colCount; i++) {

                if(play){
                    highlightCurrentColumn(i);

                    //boolean[] notes = initializeNoteArray(i);
                    //boolean hasActivated = hasActiveNote(notes);

                    //if(hasActivated) {
                        for(int j = 0; j < rowCount; j++) {
                            if(buttonMatrix[j][i].isActivated() == true) {
                                PlayService playServiceOne = new PlayService(j, speed);
                                Thread myThreadOne = new Thread(playServiceOne);
                                myThreadOne.start();
                            }
                        }
                    //} else {
                        for (long stop=System.nanoTime()+ TimeUnit.MILLISECONDS.toNanos(speed);stop>System.nanoTime();){}
                    //}

                    removeHighlightCurrentColumn(i);
                }
                play = getPlayStatus();
            }
            play = getPlayStatus();
        }
    }



    private boolean[] initializeNoteArray(int colNum) {
        boolean[] notes = new boolean[rowCount];
        for(int n = 0; n < rowCount; n++) {
            if(buttonMatrix[n][colNum].isActivated()) {
                notes[n] = true;
            } else {
                notes[n] = false;
            }
        }

        return notes;
    }

    private boolean hasActiveNote(boolean[] notes) {
        for(int i = 0; i < notes.length; i++) {
            if(notes[i] == true) {
                return true;
            }
        }

        return false;
    }


    public void highlightCurrentColumn(int column) {
        for(int row = 0; row < rowCount; row++) {
            buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, R.drawable.sequencer_theme_btn_default_pressed_holo_light));
        }
    }

    public void removeHighlightCurrentColumn(int column) {
        for(int row = 0; row < rowCount; row++) {
            if(buttonMatrix[row][column].isActivated()) {
                buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, R.drawable.sequencer_theme_btn_default_focused_holo_light));
            } else {
                buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, R.drawable.sequencer_theme_btn_default_holo_light));
            }
        }
    }

    public boolean getPlayStatus()
    {
        return this.isPlaying;
    }

    public void startPlay()
    {
        this.isPlaying = true;
    }

    public void stopPlay()
    {
        this.isPlaying = false;
    }

    public void changeScale(String scale)
    {
        playService.changeScale(scale);
    }

    public static void updateTempo(int tempo) {
        speed = tempo;
    }
}
