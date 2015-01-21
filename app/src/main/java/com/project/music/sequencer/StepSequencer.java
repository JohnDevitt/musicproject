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
    private static View[][] buttonMatrix;

    public StepSequencer(int rowCount, int colCount, View[][] buttonMatrix) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.buttonMatrix = buttonMatrix;
    }

    private static class TurnOn implements Runnable {
        public final int j;
        public final int k;

        TurnOn(final int j, final int k) {
            this.j = j;
            this.k = k;
        }

        @Override
        public void run() {

        }
    }

    private static class TurnOff implements Runnable {
        public final int j;
        public final int k;

        TurnOff(final int j, final int k) {
            this.j = j;
            this.k = k;
        }

        @Override
        public void run() {

        }
    }

    public void run() {
        boolean play = this.isPlaying;

        boolean hasActivated;
        PlayService playService = new PlayService();

        while(true) {

                for (int i = 0; i < colCount; i++) {

                    if(play)
                    {

                        boolean[] notes = initializeNoteArray(rowCount, i);
                        hasActivated = hasActiveNote(notes);
                        int note = getNote(notes);

                        if(hasActivated) {

                            buttonMatrix[note][i].post(new TurnOn(note, i) {
                                @Override
                                public void run() {
                                    buttonMatrix[j][k].setBackgroundResource(R.drawable.sequencergreen_btn_default_focused_holo_light);
                                }
                            });


                            playService.play_notes(notes);

                            buttonMatrix[note][i].post(new TurnOff(note, i) {
                                @Override
                                public void run() {
                                    buttonMatrix[j][k].setBackgroundResource(R.drawable.sequencer_theme_btn_default_pressed_holo_light);
                                }
                            });


                        } else {
                            for (long stop=System.nanoTime()+ TimeUnit.MILLISECONDS.toNanos(250);stop>System.nanoTime();){}
                        }
                    play = getPlayStatus();
                }

            }
            play = getPlayStatus();

        }
    }



    private boolean[] initializeNoteArray(int rowCount, int colNum) {
        boolean[] notes = new boolean[rowCount];
        for(int n = 0; n < notes.length; n++) {
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

    private int getNote(boolean[] notes) {
        for(int i = 0; i < notes.length; i++) {
            if(notes[i] == true) {
                return i;
            }
        }

        return 0;
    }

    public boolean getPlayStatus()
    {
        return this.isPlaying;
    }

    public void togglePlaying()
    {
        if(this.isPlaying)
        {
            this.isPlaying = false;
        }
        else
        {
            this.isPlaying = true;
        }
    }
}
