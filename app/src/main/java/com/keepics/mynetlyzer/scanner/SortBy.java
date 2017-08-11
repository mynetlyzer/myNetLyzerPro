
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

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

public enum SortBy {
    STRENGTH(new StrengthComparator()),
    SSID(new SSIDComparator()),
    CHANNEL(new ChannelComparator());

    private final Comparator<ScannerDataDetail> comparator;

    SortBy(@NonNull Comparator<ScannerDataDetail> comparator) {
        this.comparator = comparator;
    }

    public static SortBy find(int index) {
        if (index < 0 || index >= values().length) {
            return STRENGTH;
        }
        return values()[index];
    }

    Comparator<ScannerDataDetail> comparator() {
        return comparator;
    }


    static class StrengthComparator implements Comparator<ScannerDataDetail> {
        @Override
        public int compare(ScannerDataDetail lhs, ScannerDataDetail rhs) {
            return new CompareToBuilder()
                .append(rhs.getScannerSignalData().getLevel(), lhs.getScannerSignalData().getLevel())
                .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                .toComparison();
        }
    }


    static class SSIDComparator implements Comparator<ScannerDataDetail> {
        @Override
        public int compare(ScannerDataDetail lhs, ScannerDataDetail rhs) {
            return new CompareToBuilder()
                .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                .append(rhs.getScannerSignalData().getLevel(), lhs.getScannerSignalData().getLevel())
                .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                .toComparison();
        }
    }

    static class ChannelComparator implements Comparator<ScannerDataDetail> {
        @Override
        public int compare(ScannerDataDetail lhs, ScannerDataDetail rhs) {
            return new CompareToBuilder()
                .append(lhs.getScannerSignalData().getPrimaryWiFiChannel().getChannel(), rhs.getScannerSignalData().getPrimaryWiFiChannel().getChannel())
                .append(rhs.getScannerSignalData().getLevel(), lhs.getScannerSignalData().getLevel())
                .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                .toComparison();
        }
    }

}
