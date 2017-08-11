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
import java.util.Arrays;
import java.util.List;

class ChannelsGHZ2 extends Channels {
    private static final Pair<Integer, Integer> RANGE = new Pair<>(2400, 2499);
    private static final List<Pair<ScannerChannel, ScannerChannel>> SETS = Arrays.asList(
        new Pair<>(new ScannerChannel(1, 2412), new ScannerChannel(13, 2472)),
        new Pair<>(new ScannerChannel(14, 2484), new ScannerChannel(14, 2484)));
    private static final Pair<ScannerChannel, ScannerChannel> SET = new Pair<>(SETS.get(0).first, SETS.get(SETS.size() - 1).second);

    ChannelsGHZ2() {
        super(RANGE, SETS);
    }

    @Override
    public List<Pair<ScannerChannel, ScannerChannel>> getWiFiChannelPairs() {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList(SET);
    }

    @Override
    public Pair<ScannerChannel, ScannerChannel> getWiFiChannelPairFirst(String countryCode) {
        return SET;
    }

    @Override
    public List<ScannerChannel> getAvailableChannels(String countryCode) {
        List<ScannerChannel> scannerChannels = new ArrayList<>();
        for (int channel : ChannelCountry.get(countryCode).getChannelsGHZ2()) {
            scannerChannels.add(getWiFiChannelByChannel(channel));
        }
        return scannerChannels;
    }

    @Override
    public boolean isChannelAvailable(String countryCode, int channel) {
        return ChannelCountry.get(countryCode).isChannelAvailableGHZ2(channel);
    }

    @Override
    public ScannerChannel getWiFiChannelByFrequency(int frequency, @NonNull Pair<ScannerChannel, ScannerChannel> wiFiChannelPair) {
        return isInRange(frequency) ? getWiFiChannel(frequency, SET) : ScannerChannel.UNKNOWN;
    }

}

