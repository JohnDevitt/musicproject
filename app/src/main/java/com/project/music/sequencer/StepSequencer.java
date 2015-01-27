package com.project.music.sequencer;

import android.view.View;

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
    private int defaultButton = R.drawable.sequencergrey_btn_default_normal_holo_light;
    private int pressedButton = R.drawable.sequencerred_btn_default_normal_holo_light;
    private int highlightedButton = R.drawable.sequencergreen_btn_default_normal_holo_light;
    private int rowButton = R.drawable.sequencergrey_btn_default_pressed_holo_light;

    public StepSequencer(int rowCount, int colCount, View[][] buttonMatrix, int speed) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.buttonMatrix = buttonMatrix;
        this.speed = speed;
    }

    public void run() {

        boolean play = this.isPlaying;


        while(true) {

                for (int i = 0; i < colCount; i++) {

                    if(play){
                        highlightCurrentColumn(i);

                        for(int j = 0; j < rowCount; j++) {
                            if(buttonMatrix[j][i].isActivated() == true) {
                                buttonMatrix[j][i].post(new EditButton(buttonMatrix, j, i, this.highlightedButton));
                                PlayService playService = new PlayService(j, speed);
                                Thread myThread = new Thread(playService);
                                myThread.start();
                            }
                        }

                        try {
                            Thread.sleep(speed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        removeHighlightCurrentColumn(i);
                    }
                    else
                    {
                        i = colCount;
                    }



                    play = getPlayStatus();
                }

            play = getPlayStatus();
        }
    }


    public void highlightCurrentColumn(int column) {
        for(int row = 0; row < rowCount; row++) {
            buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, this.rowButton));
        }
    }

    public void removeHighlightCurrentColumn(int column) {
        for(int row = 0; row < rowCount; row++) {
            if(buttonMatrix[row][column].isActivated()) {
                buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, this.pressedButton));
            } else {
                buttonMatrix[row][column].post(new EditButton(buttonMatrix, row, column, this.defaultButton));
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
        PlayService.changeScale(scale);
    }

    public static void updateTempo(int tempo) {
        speed = tempo;
    }
}
