package com.project.music.sequencer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.EventListener;
import java.util.HashMap;


/**
 *  PlayService
 *
 *  Class is in control of making the actual noises of the app.
 **/
public class PlayService implements EventListener, Runnable{

    private double duration;
    private int sampleRate;
    private int numSamples;
    private double sample[];
    private byte generatedSnd[];

    private int note;
    private int speed;

    private AudioTrack audioTrack;

    private static HashMap<Integer, Long> indexToFrequency = new HashMap<Integer, Long>();

    /**
     *  PlayService
     *
     *  @param note     int of note (1-8) of which note is being played
     *  @param speed    speed, in milliseconds
     **/
    public PlayService(int note, int speed) {

        this.note = note;
        this.speed = speed;

        duration = (double)speed/(double)1000; // convert milliseconds into seconds
        sampleRate = 8000;
        numSamples = (new Double(duration * sampleRate)).intValue();
        this.sample = new double[numSamples];
        generatedSnd = new byte[2 * numSamples];

        changeScale("CMajor"); // Default scales
    }

    public void run()
    {
        playSound(this.note);

        // Sleep thread for length of note
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        audioTrack.release(); // Release audioTrack

        return;
    }

    /**
     *  genTone
     *
     *  Generates the tone to be played by pitch (note played)
     *
     *  @param pitch    which note is being sent in
     **/
    void genTone(int pitch)
    {
        // Get frequency from hash table
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

    /**
     *  playSound
     *
     *  Plays the generated tone.
     *
     *  @param pitch    which note is being sent in
     **/
    void playSound(int pitch)
    {
        // Generate the tone first
        genTone(pitch);

        // Plays the tone
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }


    /**
     *  changeScale
     *
     *  Function updates/initializes hash table of frequencies for different scales.
     *
     *  @param scale    input string to pick scale
     **/
    public static void changeScale(String scale)
    {
        switch (scale) {
            case "AMinor":
                initializeAMinorScale();
                break;
            case "BMajor":
                initializeBMajorScale();
                break;
            case "CMajor":
                initializeCMajorScale();
                break;
            case "DMajor":
                initializeDMajorScale();
                break;
            case "EMajor":
                initializeEMajorScale();
                break;
            case "FMajor":
                initializeFMajorScale();
                break;
            case "GMajor":
                initializeGMajorScale();
                break;
            default:
                initializeCMajorScale();
                break;
        }
    }


    public static void initializeCMajorScale() {
        indexToFrequency.put(new Integer(0), new Long(262)); // C4
        indexToFrequency.put(new Integer(1), new Long(294)); // D4
        indexToFrequency.put(new Integer(2), new Long(330)); // E4
        indexToFrequency.put(new Integer(3), new Long(349)); // F4
        indexToFrequency.put(new Integer(4), new Long(392)); // G4
        indexToFrequency.put(new Integer(5), new Long(440)); // A4
        indexToFrequency.put(new Integer(6), new Long(494)); // B4
        indexToFrequency.put(new Integer(7), new Long(523)); // C5
    }

    public static void initializeAMinorScale() {
        indexToFrequency.put(new Integer(0), new Long(220)); // A3
        indexToFrequency.put(new Integer(1), new Long(247)); // B3
        indexToFrequency.put(new Integer(2), new Long(262)); // C4
        indexToFrequency.put(new Integer(3), new Long(294)); // D4
        indexToFrequency.put(new Integer(4), new Long(330)); // E4
        indexToFrequency.put(new Integer(5), new Long(349)); // F4
        indexToFrequency.put(new Integer(6), new Long(392)); // G4
        indexToFrequency.put(new Integer(7), new Long(440)); // A4
    }

    public static void initializeGMajorScale() {
        indexToFrequency.put(new Integer(0), new Long(392)); // G4
        indexToFrequency.put(new Integer(1), new Long(440)); // A4
        indexToFrequency.put(new Integer(2), new Long(494)); // B4
        indexToFrequency.put(new Integer(3), new Long(523)); // C5
        indexToFrequency.put(new Integer(4), new Long(587)); // D5
        indexToFrequency.put(new Integer(5), new Long(659)); // E5
        indexToFrequency.put(new Integer(6), new Long(740)); // F#5
        indexToFrequency.put(new Integer(7), new Long(784)); // G5
    }

    public static void initializeDMajorScale() {
        indexToFrequency.put(new Integer(0), new Long(294)); // D4
        indexToFrequency.put(new Integer(1), new Long(330)); // E4
        indexToFrequency.put(new Integer(2), new Long(370)); // F#
        indexToFrequency.put(new Integer(3), new Long(392)); // G4
        indexToFrequency.put(new Integer(4), new Long(440)); // A4
        indexToFrequency.put(new Integer(5), new Long(493)); // B4
        indexToFrequency.put(new Integer(6), new Long(554)); // C#
        indexToFrequency.put(new Integer(7), new Long(587)); // D5
    }

    public static void initializeEMajorScale() {
        indexToFrequency.put(new Integer(1), new Long(330)); // E4
        indexToFrequency.put(new Integer(2), new Long(370)); // F#
        indexToFrequency.put(new Integer(2), new Long(415)); // G#
        indexToFrequency.put(new Integer(3), new Long(440)); // A4
        indexToFrequency.put(new Integer(4), new Long(493)); // B4
        indexToFrequency.put(new Integer(5), new Long(554)); // C#
        indexToFrequency.put(new Integer(6), new Long(622)); // D#
        indexToFrequency.put(new Integer(7), new Long(659)); // E5
    }

    public static void initializeFMajorScale() {
        indexToFrequency.put(new Integer(1), new Long(349)); // F4
        indexToFrequency.put(new Integer(2), new Long(392)); // G4
        indexToFrequency.put(new Integer(2), new Long(440)); // A4
        indexToFrequency.put(new Integer(3), new Long(466)); // A#
        indexToFrequency.put(new Integer(4), new Long(523)); // C5
        indexToFrequency.put(new Integer(5), new Long(587)); // D5
        indexToFrequency.put(new Integer(6), new Long(659)); // E5
        indexToFrequency.put(new Integer(7), new Long(698)); // F5
    }

    public static void initializeBMajorScale() {
        indexToFrequency.put(new Integer(1), new Long(494)); // B4
        indexToFrequency.put(new Integer(2), new Long(554)); // C#
        indexToFrequency.put(new Integer(2), new Long(622)); // D#
        indexToFrequency.put(new Integer(3), new Long(659)); // E5
        indexToFrequency.put(new Integer(4), new Long(740)); // F#
        indexToFrequency.put(new Integer(5), new Long(831)); // G#
        indexToFrequency.put(new Integer(6), new Long(932)); // A#
        indexToFrequency.put(new Integer(7), new Long(988)); // B5
    }

}
