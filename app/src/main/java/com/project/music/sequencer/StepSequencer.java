package com.project.music.sequencer;

import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * Created by john on 17/12/14.
 */
public class StepSequencer implements Runnable{

    private int rowCount;
    private int colCount;
    private static View[][] buttonMatrix;

    public StepSequencer(int rowCount, int colCount, View[][] buttonMatrix) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.buttonMatrix = buttonMatrix;
    }

    public void run() {

        PlayService playService = new PlayService();

        while(true) {
            for (int i = 0; i < colCount; i++) {

                highlightCurrentColumn(i);

                boolean[] notes = initializeNoteArray(i);
                boolean hasActivated = hasActiveNote(notes);
                int note = getActiveNote(notes);

                if(hasActivated) {
                    buttonMatrix[note][i].post(new EditButton(buttonMatrix, note, i, R.drawable.sequencergreen_btn_default_focused_holo_light));
                    playService.play_notes(notes);
                    buttonMatrix[note][i].post(new EditButton(buttonMatrix, note, i, R.drawable.sequencer_theme_btn_default_pressed_holo_light));
                } else {
                    for (long stop=System.nanoTime()+ TimeUnit.MILLISECONDS.toNanos(250);stop>System.nanoTime();){}
                }

                removeHighlightCurrentColumn(i);

            }
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

    private int getActiveNote(boolean[] notes) {
        for(int i = 0; i < notes.length; i++) {
            if(notes[i] == true) {
                return i;
            }
        }

        return 0;
    }

    public void highlightCurrentColumn(int column) {
        for(int row = 0; row < rowCount; row++) {
            buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, R.drawable.sequencer_theme_btn_default_pressed_holo_light));
        }
    }

    public void removeHighlightCurrentColumn(int column) {
        for(int row = 0; row < rowCount; row++) {
            buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, R.drawable.sequencer_theme_btn_default_holo_light));
        }

        boolean[] notes = initializeNoteArray(column);
        boolean hasActivated = hasActiveNote(notes);
        int note = getActiveNote(notes);

        if(hasActivated) {
            buttonMatrix[note][column].post(new EditButton(buttonMatrix, note, column, R.drawable.sequencer_theme_btn_default_focused_holo_light));
        }
    }
}
