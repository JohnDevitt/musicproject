package com.project.music.sequencer;

import android.view.View;

/*
    This class is used to edit the UI of a button, it takes in the buttonMatrix, the i and j coordinates
    of the button and the id of the drawable resource of the button, and implements the change
 */
public class EditButton implements Runnable {
    public final View[][] buttonMatrix;
    public final int row;
    public final int column;
    // Drawable resource id
    public final int button;

    EditButton(final View[][] buttonMatrix, final int row, final int column, final int button) {
        this.buttonMatrix = buttonMatrix;
        this.row = row;
        this.column = column;
        this.button = button;
    }

    // Updates the button UI to the given id
    @Override
    public void run() {
        buttonMatrix[row][column].setBackgroundResource(button);
    }
}
