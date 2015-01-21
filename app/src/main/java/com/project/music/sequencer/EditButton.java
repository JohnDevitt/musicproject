package com.project.music.sequencer;

import android.view.View;

/**
 * Created by john on 21/01/15.
 */
public class EditButton implements Runnable {
    public final View[][] buttonMatrix;
    public final int row;
    public final int column;
    public final int button;

    EditButton(final View[][] buttonMatrix, final int row, final int column, final int button) {
        this.buttonMatrix = buttonMatrix;
        this.row = row;
        this.column = column;
        this.button = button;
    }

    @Override
    public void run() {
        buttonMatrix[row][column].setBackgroundResource(button);
    }
}
