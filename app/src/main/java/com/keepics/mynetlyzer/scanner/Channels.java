
/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/*
 * Modified by Keepics Inc, 2017
 */

package com.keepics.mynetlyzer.scanner;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class Channels {
    public static final Pair<ScannerChannel, ScannerChannel> UNKNOWN = new Pair<>(ScannerChannel.UNKNOWN, ScannerChannel.UNKNOWN);
    public static final int FREQUENCY_SPREAD = 5;
    public static final int CHANNEL_OFFSET = 2;
    public static final int FREQUENCY_OFFSET = FREQUENCY_SPREAD * CHANNEL_OFFSET;

    private final Pair<Integer, Integer> wiFiRange;
    private final List<Pair<ScannerChannel, ScannerChannel>> wiFiChannelPairs;

    Channels(@NonNull Pair<Integer, Integer> wiFiRange, @NonNull List<Pair<ScannerChannel, ScannerChannel>> wiFiChannelPairs) {
        this.wiFiRange = wiFiRange;
        this.wiFiChannelPairs = wiFiChannelPairs;
    }

    public boolean isInRange(int frequency) {
        return frequency >= wiFiRange.first && frequency <= wiFiRange.second;
    }

    public ScannerChannel getWiFiChannelByFrequency(int frequency) {
        if (isInRange(frequency)) {
            for (Pair<ScannerChannel, ScannerChannel> wiFiChannelPair : wiFiChannelPairs) {
                ScannerChannel scannerChannel = getWiFiChannel(frequency, wiFiChannelPair);
                if (!ScannerChannel.UNKNOWN.equals(scannerChannel)) {
                    return scannerChannel;
                }
            }
        }
        return ScannerChannel.UNKNOWN;
    }

    ScannerChannel getWiFiChannelByChannel(int channel) {
        for (Pair<ScannerChannel, ScannerChannel> wiFiChannelPair : wiFiChannelPairs) {
            ScannerChannel first = wiFiChannelPair.first;
            ScannerChannel last = wiFiChannelPair.second;
            if (channel >= first.getChannel() && channel <= last.getChannel()) {
                int frequency = first.getFrequency() + ((channel - first.getChannel()) * FREQUENCY_SPREAD);
                return new ScannerChannel(channel, frequency);
            }
        }
        return ScannerChannel.UNKNOWN;
    }

    public ScannerChannel getWiFiChannelFirst() {
        return wiFiChannelPairs.get(0).first;
    }

    public ScannerChannel getWiFiChannelLast() {
        return wiFiChannelPairs.get(wiFiChannelPairs.size() - 1).second;
    }

    public List<ScannerChannel> getWiFiChannels() {
        List<ScannerChannel> results = new ArrayList<>();
        for (Pair<ScannerChannel, ScannerChannel> wiFiChannelPair : wiFiChannelPairs) {
            for (int channel = wiFiChannelPair.first.getChannel(); channel <= wiFiChannelPair.second.getChannel(); channel++) {
                results.add(getWiFiChannelByChannel(channel));
            }
        }
        return results;
    }

    ScannerChannel getWiFiChannel(int frequency, @NonNull Pair<ScannerChannel, ScannerChannel> wiFiChannelPair) {
        ScannerChannel first = wiFiChannelPair.first;
        ScannerChannel last = wiFiChannelPair.second;
        int channel = (int) (((double) (frequency - first.getFrequency()) / FREQUENCY_SPREAD) + first.getChannel() + 0.5);
        if (channel >= first.getChannel() && channel <= last.getChannel()) {
            return new ScannerChannel(channel, frequency);
        }
        return ScannerChannel.UNKNOWN;
    }

    public abstract List<ScannerChannel> getAvailableChannels(String countryCode);

    public abstract boolean isChannelAvailable(String countryCode, int channel);

    public abstract List<Pair<ScannerChannel, ScannerChannel>> getWiFiChannelPairs();

    public abstract Pair<ScannerChannel, ScannerChannel> getWiFiChannelPairFirst(String countryCode);

    public abstract ScannerChannel getWiFiChannelByFrequency(int frequency, @NonNull Pair<ScannerChannel, ScannerChannel> wiFiChannelPair);
}