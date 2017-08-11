
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ChannelsGHZ5 extends Channels {
    private static final Pair<Integer, Integer> RANGE = new Pair<>(4900, 5899);
    private static final List<Pair<ScannerChannel, ScannerChannel>> SETS = Arrays.asList(
        new Pair<>(new ScannerChannel(36, 5180), new ScannerChannel(64, 5320)),
        new Pair<>(new ScannerChannel(100, 5500), new ScannerChannel(144, 5720)),
        new Pair<>(new ScannerChannel(149, 5745), new ScannerChannel(165, 5825)));

    private static final int DEFAULT_PAIR = 0;

    ChannelsGHZ5() {
        super(RANGE, SETS);
    }

    @Override
    public List<Pair<ScannerChannel, ScannerChannel>> getWiFiChannelPairs() {
        return Collections.unmodifiableList(SETS);
    }

    @Override
    public Pair<ScannerChannel, ScannerChannel> getWiFiChannelPairFirst(String countryCode) {
        List<Pair<ScannerChannel, ScannerChannel>> wiFiChannelPairs = getWiFiChannelPairs();
        if (!StringUtils.isBlank(countryCode)) {
            for (Pair<ScannerChannel, ScannerChannel> wiFiChannelPair : wiFiChannelPairs) {
                if (isChannelAvailable(countryCode, wiFiChannelPair.first.getChannel())) {
                    return wiFiChannelPair;
                }
            }
        }
        return wiFiChannelPairs.get(DEFAULT_PAIR);
    }

    @Override
    public List<ScannerChannel> getAvailableChannels(String countryCode) {
        List<ScannerChannel> scannerChannels = new ArrayList<>();
        for (int channel : ChannelCountry.get(countryCode).getChannelsGHZ5()) {
            scannerChannels.add(getWiFiChannelByChannel(channel));
        }
        return scannerChannels;
    }

    @Override
    public boolean isChannelAvailable(String countryCode, int channel) {
        return ChannelCountry.get(countryCode).isChannelAvailableGHZ5(channel);
    }

    @Override
    public ScannerChannel getWiFiChannelByFrequency(int frequency, @NonNull Pair<ScannerChannel, ScannerChannel> wiFiChannelPair) {
        return isInRange(frequency) ? getWiFiChannel(frequency, wiFiChannelPair) : ScannerChannel.UNKNOWN;
    }
}
