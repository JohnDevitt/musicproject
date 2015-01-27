package com.project.music.sequencer;

import android.view.View;

/*
    This class implements the main logic of the step sequencer. It steps through the matrix of buttons
    and spawns threads to play any buttons that are selected. It also makes slight UI changes to show
    the user at which step the sequencer is currently at, and highlights buttons that are playing a sound.
 */
public class StepSequencer implements Runnable{

      // Class variables
    private int rowCount;
    private int colCount;
    public boolean isPlaying = true;
    private static View[][] buttonMatrix;
    private static int speed;
    private int defaultButton = R.drawable.sequencergrey_btn_default_normal_holo_light;
    private int pressedButton = R.drawable.sequencerred_btn_default_normal_holo_light;
    private int highlightedButton = R.drawable.sequencergreen_btn_default_normal_holo_light;
    private int rowButton = R.drawable.sequencerlgrey_btn_default_normal_holo_light;

    public PlayService playService;

    public StepSequencer(int rowCount, int colCount, View[][] buttonMatrix, int speed) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.buttonMatrix = buttonMatrix;
        this.speed = speed;
    }


    public void run() {

        boolean play = this.isPlaying;
        PlayService.changeScale("CMajor");


        // This is the main loop, it will return the sequencer back to the first step after it has
        // played the last.
        while(true) {
                // This is the loop that actually steps through all the notes
                for (int i = 0; i < colCount; i++) {
                    // This piece of code allows the app to check the play variable before it plays a note,
                    // it only plays if the play variable is set, the play variable is set in the main
                    // activity(UI). Therefore the user can stop the sequencer at any step
                    if(play){
                        // This preforms a slight change to the UI so the user can get a feel for what step
                        // they are on
                        highlightCurrentColumn(i);
                        // At every step, we loop through all the notes in that step
                        for(int j = 0; j < rowCount; j++) {
                            // If any note is selected(a.k.a. if any button is pressed)
                            if(buttonMatrix[j][i].isActivated() == true) {
                                // Then we spawn a thread to play that note
                                playService = new PlayService(j, speed);
                                Thread myThread = new Thread(playService);
                                myThread.start();
                                // We also make a slight tweak to the UI to show that note is being played
                                buttonMatrix[j][i].post(new EditButton(buttonMatrix, j, i, this.highlightedButton));
                            }
                        }

                        // All of the notes that are played through spawning separate threads that run parallel to this one.
                        // Therefore, in order to stop notes from overlapping, we need this thread to sleep for the same
                        // duration that the notes are being played for before we continue to the next step
                        try {
                            Thread.sleep(speed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        // Once we are finished with this step, we undo any of the UI tweaks that we have made
                        removeHighlightCurrentColumn(i);
                    }
                    // This else statement corresponds to the if(play) statement above, it means that we will always
                    // return to the first step after we stop playing. Because it is inside the for loop we are constantly
                    // checking if this changes.
                    else
                    {
                        i = colCount;
                    }

                    // Check for changes to the play variable(i.e. If play/stop has been pressed in the UI)
                    play = getPlayStatus();
                }
            // Check for changes to the play variable(i.e. If play/stop has been pressed in the UI)
            play = getPlayStatus();
        }
    }


    // This method is used to highlight the current step in the sequencer
    public void highlightCurrentColumn(int column) {
        for(int row = 0; row < rowCount; row++) {
            buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, this.rowButton));
        }
    }

    // This method is used to undo any UI tweaks made in a step
    public void removeHighlightCurrentColumn(int column) {
        for(int row = 0; row < rowCount; row++) {
            if(buttonMatrix[row][column].isActivated()) {
                buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, this.pressedButton));
            } else {
                buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, this.defaultButton));
            }
        }
    }

    // Returns status of isPlaying
    public boolean getPlayStatus()
    {
        return this.isPlaying;
    }

    // Sets isPlaying to true
    public void startPlay()
    {
        this.isPlaying = true;
    }

    // Sets isPlaying to false
    public void stopPlay()
    {
        this.isPlaying = false;
    }

    // This method changes the static scale variable in the PlayService class
    public void changeScale(String scale)
    {
        PlayService.changeScale(scale);
        /*
        if(playService != null)
        {
            System.out.println("playService is not null");
            playService.setScale(scale);
        }
        */
    }

    // This updates the static tempo variable in the PlayService class
    public static void updateTempo(int tempo) {
        speed = tempo;
    }
}
