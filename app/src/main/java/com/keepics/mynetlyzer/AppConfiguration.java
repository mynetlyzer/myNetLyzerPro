
package com.keepics.mynetlyzer;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.keepics.mynetlyzer.scanner.ScannerChannel;
import com.keepics.mynetlyzer.scanner.Channels;

public class AppConfiguration {
    public static final int SIZE_MIN = 1024;
    public static final int SIZE_MAX = 4096;

    private final boolean largeScreen;
    private int size;
    private Pair<ScannerChannel, ScannerChannel> wiFiChannelPair;

    public AppConfiguration(boolean largeScreen) {
        this.largeScreen = largeScreen;
        setSize(SIZE_MAX);
        setWiFiChannelPair(Channels.UNKNOWN);
    }

    public boolean isLargeScreen() {
        return largeScreen;
    }

    public Pair<ScannerChannel, ScannerChannel> getWiFiChannelPair() {
        return wiFiChannelPair;
    }

    public void setWiFiChannelPair(@NonNull Pair<ScannerChannel, ScannerChannel> wiFiChannelPair) {
        this.wiFiChannelPair = wiFiChannelPair;
    }

    public boolean isSizeAvailable() {
        return size == SIZE_MAX;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
