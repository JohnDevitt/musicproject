package com.project.music.sequencer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.EventListener;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by john on 17/12/14.
 */
public class PlayService implements EventListener, Runnable{

    private static final int sr = 44100;
    private static final int buffsize = AudioTrack.getMinBufferSize(sr,
            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
            sr, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT, buffsize,
            AudioTrack.MODE_STREAM);

    private int note;
    private int speed;

    private static HashMap<Integer, Long> indexToFrequency = new HashMap<Integer, Long>();

    public PlayService(int note, int speed) {
        this.note = note;
        this.speed = speed;
        changeScale("CMajor");
    }

    public void run() {
        playSound(this.note, this.speed);
        return;
    }

    public void play_music(int pitch, int speed) {

        short samples[] = new short[buffsize];
        int amp = 10000;
        double twopi = 8.*Math.atan(1.);
        double ph = 0.0;

        audioTrack.play();
        for (long stop = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(speed); stop > System.nanoTime(); ) {

            double fr = indexToFrequency.get(new Integer(pitch));

            for (int j = 0; j < buffsize; j++) {
                samples[j] = (short) (amp * Math.sin(ph));
                ph += twopi * fr / sr;
            }

            audioTrack.write(samples, 0, buffsize);
        }
        audioTrack.stop();
    }

    public static void changeScale(String scale)
    {
        switch (scale) {
            case "CMajor":
                initializeCMajorScale();
                break;
            case "AMinor":
                initializeAMinorScale();
                break;
            default:
                initializeCMajorScale();
                break;
        }
    }

    private final int duration = 1; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    //private final double freqOfTone = 440; // hz

    private final byte generatedSnd[] = new byte[2 * numSamples];

    void genTone(int pitch, int speed){
        double freqOfTone = indexToFrequency.get(new Integer(pitch));
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            // sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
            sample[i] = Math.sin((2 * Math.PI - .001) * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        int ramp = numSamples / 20;

        for (int i = 0; i < ramp; i++) {
            // scale to maximum amplitude
            final short val = (short) ((sample[i] * 32767) * i / ramp);
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        for (int i = ramp; i < numSamples - ramp; i++) {
            // scale to maximum amplitude
            final short val = (short) ((sample[i] * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        for (int i = numSamples - ramp; i < numSamples; i++) {
            // scale to maximum amplitude
            final short val = (short) ((sample[i] * 32767) * (numSamples - i) / ramp);
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    void playSound(int pitch, int speed)
    {
        genTone(pitch, speed);

        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }

    public static void initializeCMajorScale() {
        indexToFrequency.put(new Integer(0), new Long(261));
        indexToFrequency.put(new Integer(1), new Long(293));
        indexToFrequency.put(new Integer(2), new Long(329));
        indexToFrequency.put(new Integer(3), new Long(349));
        indexToFrequency.put(new Integer(4), new Long(392));
        indexToFrequency.put(new Integer(5), new Long(440));
        indexToFrequency.put(new Integer(6), new Long(493));
        indexToFrequency.put(new Integer(7), new Long(523));
    }

    public static void initializeAMinorScale() {
        indexToFrequency.put(new Integer(0), new Long(220));
        indexToFrequency.put(new Integer(1), new Long(233));
        indexToFrequency.put(new Integer(2), new Long(247));
        indexToFrequency.put(new Integer(3), new Long(262));
        indexToFrequency.put(new Integer(4), new Long(277));
        indexToFrequency.put(new Integer(5), new Long(294));
        indexToFrequency.put(new Integer(6), new Long(311));
        indexToFrequency.put(new Integer(7), new Long(330));
    }
}
