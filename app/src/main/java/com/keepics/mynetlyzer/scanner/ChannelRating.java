
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
import android.util.Log;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChannelRating {
    static final int LEVEL_RANGE_MIN = -5;
    private static final int LEVEL_RANGE_MAX = 5;
    private static final int BSSID_LENGTH = 17;

    private List<ScannerDataDetail> scannerDataDetails = new ArrayList<>();

    public int getCount(@NonNull ScannerChannel scannerChannel) {
        return collectOverlapping(scannerChannel).size();
    }

    public SignalStrength getStrength(@NonNull ScannerChannel scannerChannel) {
        SignalStrength signalStrength = SignalStrength.ZERO;
        for (ScannerDataDetail scannerDataDetail : collectOverlapping(scannerChannel)) {
            if (!scannerDataDetail.getWiFiNetworkInfo().getWiFiConnectionInfo().isConnected()) {
                signalStrength = SignalStrength.values()[Math.max(signalStrength.ordinal(), scannerDataDetail.getScannerSignalData().getStrength().ordinal())];
            }
        }
        return signalStrength;
    }

    public int getStrengthV2(@NonNull ScannerChannel scannerChannel) {
        SignalStrength signalStrength = SignalStrength.ZERO;
        int level = -100;
        List<ScannerDataDetail> list = collectOverlapping(scannerChannel);
        for (ScannerDataDetail scannerDataDetail : list) {
            if (!scannerDataDetail.getWiFiNetworkInfo().getWiFiConnectionInfo().isConnected()) {
                int eachLevel = scannerDataDetail.getScannerSignalData().getLevel();
                if(eachLevel > level){
                    level = eachLevel;
                }
            }
        }
        Log.d("wifianlyzer", level + "db");
        return level;
    }

    private List<ScannerDataDetail> removeGuest(@NonNull List<ScannerDataDetail> scannerDataDetails) {
        List<ScannerDataDetail> results = new ArrayList<>();
        ScannerDataDetail scannerDataDetail = ScannerDataDetail.EMPTY;
        Collections.sort(scannerDataDetails, new GuestSort());
        for (ScannerDataDetail current : scannerDataDetails) {
            if (isGuest(current, scannerDataDetail)) {
                continue;
            }
            results.add(current);
            scannerDataDetail = current;
        }
        Collections.sort(results, SortBy.STRENGTH.comparator());
        return results;
    }

    List<ScannerDataDetail> getScannerDataDetails() {
        return scannerDataDetails;
    }

    public void setScannerDataDetails(@NonNull List<ScannerDataDetail> scannerDataDetails) {
        this.scannerDataDetails = removeGuest(new ArrayList<>(scannerDataDetails));
    }

    private boolean isGuest(@NonNull ScannerDataDetail lhs, @NonNull ScannerDataDetail rhs) {
        if (!isGuestBSSID(lhs.getBSSID(), rhs.getBSSID())) {
            return false;
        }
        int result = lhs.getScannerSignalData().getPrimaryFrequency() - rhs.getScannerSignalData().getPrimaryFrequency();
        if (result == 0) {
            result = rhs.getScannerSignalData().getLevel() - lhs.getScannerSignalData().getLevel();
            if (result > LEVEL_RANGE_MIN || result < LEVEL_RANGE_MAX) {
                result = 0;
            }
        }
        return result == 0;
    }

    private boolean isGuestBSSID(@NonNull String lhs, @NonNull String rhs) {
        return lhs.length() == BSSID_LENGTH &&
            lhs.length() == rhs.length() &&
            lhs.substring(0, 0).equalsIgnoreCase(rhs.substring(0, 0)) &&
            lhs.substring(2, BSSID_LENGTH - 1).equalsIgnoreCase(rhs.substring(2, BSSID_LENGTH - 1));
    }

    private List<ScannerDataDetail> collectOverlapping(@NonNull ScannerChannel scannerChannel) {
        List<ScannerDataDetail> result = new ArrayList<>();
        for (ScannerDataDetail scannerDataDetail : scannerDataDetails) {
            if (scannerDataDetail.getScannerSignalData().isInRange(scannerChannel.getFrequency())) {
                result.add(scannerDataDetail);
            }
        }
        return result;
    }

    public List<WiFiChannelAcessPointCount> getBestChannels(@NonNull final List<ScannerChannel> scannerChannels) {
        List<WiFiChannelAcessPointCount> results = new ArrayList<>();
        for (ScannerChannel scannerChannel : scannerChannels) {
            SignalStrength signalStrength = getStrength(scannerChannel);
            if (SignalStrength.ZERO.equals(signalStrength)
                    || SignalStrength.ONE.equals(signalStrength)
                    || SignalStrength.TWO.equals(signalStrength)
                    /*|| SignalStrength.THREE.equals(signalStrength)*/) {
                results.add(new WiFiChannelAcessPointCount(scannerChannel, getCount(scannerChannel)));
            }
        }
        Collections.sort(results);
        return results;
    }

    public int getBestChannelsV2(@NonNull final List<ScannerChannel> scannerChannels) {
        int bestSignal = 0;
        ScannerChannel bestChannel = null;
        for (ScannerChannel scannerChannel : scannerChannels) {
            int eachLevel = getStrengthV2(scannerChannel);
            if(eachLevel < bestSignal){
                bestSignal = eachLevel;
                bestChannel = scannerChannel;
            }
        }
        if(bestChannel != null){
            return bestChannel.getChannel();
        }else{
            return 0;
        }
    }

    private static class GuestSort implements Comparator<ScannerDataDetail> {
        @Override
        public int compare(@NonNull ScannerDataDetail lhs, @NonNull ScannerDataDetail rhs) {
            return new CompareToBuilder()
                .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                .append(lhs.getScannerSignalData().getPrimaryFrequency(), rhs.getScannerSignalData().getPrimaryFrequency())
                .append(rhs.getScannerSignalData().getLevel(), lhs.getScannerSignalData().getLevel())
                .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                .toComparison();
        }
    }
}
