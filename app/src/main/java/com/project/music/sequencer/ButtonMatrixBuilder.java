package com.project.music.sequencer;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

/*
    This class is used to separated out the logic of building our main matrix of buttons from the
    Main Activity. The Main Activity can then call this class as a service. The object that this
    class creates will be the object that the StepSequencer class will preform operations on
 */
public class ButtonMatrixBuilder {

    // The different styles of buttons to be used
    private static int defaultButton = R.drawable.sequencergrey_btn_default_normal_holo_light;
    private static int pressedButton = R.drawable.sequencerred_btn_default_normal_holo_light;

    // This method is actually used to build the matrix
    public static View[][] buildButtonMatrix(Context applicationContext, GridLayout layout, int rowCount, int columnCount) {

        // This View matrix will hold our buttons in memory
        View[][] buttonMatrix;
        buttonMatrix = new Button[rowCount][columnCount];

        // Simple loop that loops the the sizes passed into the method. It build the buttons
        // and adds them to the layout as well as the matrix
        for(int row = 0; row < rowCount; row++) {
            for(int column = 0; column < columnCount; column++) {
                View button = generateButton(applicationContext); // Generate button
                buttonMatrix[row][column] = button; // Put button into button array
                layout.addView(button); // Add button
            }
        }

        // Return the matrix
        return buttonMatrix;
    }

    // Basic method to build the buttons with listeners
    private static View generateButton(Context applicationContext) {
        Button button;
        button = new Button(applicationContext);

        // This listener will change the UI when a button is pressed, along with changing
        // it's activated state in the button matrix, the step sequencer then uses this info to
        // actually play a note.
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.isActivated()) {
                            v.setActivated(false);
                            v.setBackgroundResource(defaultButton);
                        } else {
                            v.setActivated(true);
                            v.setBackgroundResource(pressedButton);
                        }
                    }
                });

        button.setBackgroundResource(defaultButton);
        return button;
    }

}
