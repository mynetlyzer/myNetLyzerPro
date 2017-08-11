/* original copy right */
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
package com.keepics.mynetlyzer.util;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.keepics.mynetlyzer.scanner.BandChannelSet;
import com.keepics.mynetlyzer.scanner.ScannerBand;
import com.keepics.mynetlyzer.scanner.ScannerChannel;
import com.keepics.mynetlyzer.scanner.ScannerDataDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class WiFiDataCollector {
    static int frequencyAdjustment(int frequency) {
        return frequency - (frequency % 5);
    }
    static ArrayList<BandChannelSet> bandChannelSets = new ArrayList<>();

    public static ArrayList<BandChannelSet> getBandChannelSets(){
        if(bandChannelSets.isEmpty()){
            for (ScannerBand scannerBand : ScannerBand.values()) {
                for (Pair<ScannerChannel, ScannerChannel> wiFiChannelPair : scannerBand.getChannels().getWiFiChannelPairs()) {
                    bandChannelSets.add(new BandChannelSet(scannerBand, wiFiChannelPair));
                }
            }
        }
        return bandChannelSets;
    }

    public Set<ScannerDataDetail> getNewSeries(@NonNull List<ScannerDataDetail> scannerDataDetails, @NonNull Pair<ScannerChannel, ScannerChannel> wiFiChannelPair) {
        Set<ScannerDataDetail> results = new TreeSet<>();
        for (ScannerDataDetail scannerDataDetail : scannerDataDetails) {
            if (isInRange(scannerDataDetail.getScannerSignalData().getCenterFrequency(), wiFiChannelPair)) {
                results.add(scannerDataDetail);
            }
        }
        return results;
    }


    private boolean isInRange(int frequency, Pair<ScannerChannel, ScannerChannel> wiFiChannelPair) {
        return frequency >= wiFiChannelPair.first.getFrequency() && frequency <= wiFiChannelPair.second.getFrequency();
    }

}
