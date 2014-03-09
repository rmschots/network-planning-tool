package com.ugent.networkplanningtool.data.enums;

import android.util.SparseArray;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

public enum Frequency {
    FREQ_1(MainActivity.getInstance().getResources().getString(R.string.frequency1), 2412),
    FREQ_2(MainActivity.getInstance().getResources().getString(R.string.frequency2), 2417),
    FREQ_3(MainActivity.getInstance().getResources().getString(R.string.frequency3), 2422),
    FREQ_4(MainActivity.getInstance().getResources().getString(R.string.frequency4), 2427),
    FREQ_5(MainActivity.getInstance().getResources().getString(R.string.frequency5), 2432),
    FREQ_6(MainActivity.getInstance().getResources().getString(R.string.frequency6), 2437),
    FREQ_7(MainActivity.getInstance().getResources().getString(R.string.frequency7), 2442),
    FREQ_8(MainActivity.getInstance().getResources().getString(R.string.frequency8), 2447),
    FREQ_9(MainActivity.getInstance().getResources().getString(R.string.frequency9), 2452),
    FREQ_10(MainActivity.getInstance().getResources().getString(R.string.frequency10), 2457),
    FREQ_11(MainActivity.getInstance().getResources().getString(R.string.frequency11), 2462);

    private String text;
    private int number;

    private static Map<String, Frequency> textToFreqMapping;
    private static SparseArray<Frequency> numberToFreqMapping;

    private Frequency(String text, int number) {
        this.text = text;
        this.number = number;
    }

    private static void initMapping() {
        textToFreqMapping = new HashMap<String, Frequency>();
        for (Frequency s : values()) {
            textToFreqMapping.put(s.getText(), s);
        }
    }

    public static Frequency getFrequencyByText(String text) {
        if (textToFreqMapping == null) {
            initMapping();
        }
        return textToFreqMapping.get(text);
    }

    private static void initMapping2() {
        numberToFreqMapping = new SparseArray<Frequency>();
        for (Frequency s : values()) {
            numberToFreqMapping.put(s.getNumber(), s);
        }
    }

    public static Frequency getFreqByNumber(int number) {
        if (numberToFreqMapping == null) {
            initMapping2();
        }
        return numberToFreqMapping.get(number);
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return text+" ("+number+")";
    }
}
