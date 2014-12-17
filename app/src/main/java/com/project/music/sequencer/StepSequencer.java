package com.project.music.sequencer;

import java.util.concurrent.TimeUnit;
import android.view.View;

/**
 * Created by john on 17/12/14.
 */
public class StepSequencer implements Runnable {

    private int rowCount;
    private int colCount;
    private static View[][] buttonMatrix;

    public StepSequencer(int rowCount, int colCount, View[][] buttonMatrix) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.buttonMatrix = buttonMatrix;
    }

    @Override
    public void run() {

        boolean hasActivated;
        PlayService playService = new PlayService();

        while(true) {
            for (int i = 0; i < colCount; i++) {

                boolean[] notes = initializeNoteArray(rowCount, i);
                hasActivated = hasActiveNote(notes);


                if(hasActivated) {
                    playService.play_notes(notes);
                } else {
                    for (long stop=System.nanoTime()+ TimeUnit.MILLISECONDS.toNanos(250);stop>System.nanoTime();){}
                }

            }
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
}
