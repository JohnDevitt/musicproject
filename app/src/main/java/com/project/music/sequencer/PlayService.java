package com.project.music.sequencer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by john on 17/12/14.
 */
public class PlayService {

    private static final int sr = 44100;
    private static final int buffsize = AudioTrack.getMinBufferSize(sr,
            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
            sr, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT, buffsize,
            AudioTrack.MODE_STREAM);

    private static HashMap<Integer, Long> indexToFrequency = new HashMap<Integer, Long>();

    public PlayService() {
        initializeCMajorScale();
    }


    public void play_notes(boolean[] notes) {
        boolean play = false;

        for (int i = 0; i < notes.length; i++) {
            if (notes[i] == true) {

                // create thread


                play_music(i);
                /*
                Runnable myRunnable = new Runnable(i) {

                    @Override
                    public void run(i) {

                    }
                };*/
                return;
            }
        }
    }

    private void play_music(int pitch) {

        short samples[] = new short[buffsize];
        int amp = 10000;
        double twopi = 8.*Math.atan(1.);
        double fr;
        double ph = 0.0;

        audioTrack.play();
        for (long stop=System.nanoTime()+ TimeUnit.MILLISECONDS.toNanos(250);stop>System.nanoTime();){
            // gets a weighting value from the slider on the GUI
            fr = indexToFrequency.get(new Integer(pitch));
            for(int i=0; i < buffsize; i++){
                samples[i] = (short) (amp*Math.sin(ph));
                ph += twopi*fr/sr;
            }
            audioTrack.write(samples, 0, buffsize);
        }
        audioTrack.stop();
    }

    public void initializeCMajorScale() {
        indexToFrequency.put(new Integer(0), new Long(261));
        indexToFrequency.put(new Integer(1), new Long(293));
        indexToFrequency.put(new Integer(2), new Long(329));
        indexToFrequency.put(new Integer(3), new Long(349));
        indexToFrequency.put(new Integer(4), new Long(392));
        indexToFrequency.put(new Integer(5), new Long(440));
        indexToFrequency.put(new Integer(6), new Long(493));
        indexToFrequency.put(new Integer(7), new Long(523));
    }
}
