
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

public enum GroupBy {
    NONE(new None(), new None()),
    SSID(new SSIDSortOrder(), new SSIDGroupBy()),
    CHANNEL(new ChannelSortOrder(), new ChannelGroupBy());

    private final Comparator<ScannerDataDetail> sortOrder;
    private final Comparator<ScannerDataDetail> groupBy;

    GroupBy(@NonNull Comparator<ScannerDataDetail> sortOrder, @NonNull Comparator<ScannerDataDetail> groupBy) {
        this.sortOrder = sortOrder;
        this.groupBy = groupBy;
    }

    public static GroupBy find(int index) {
        if (index < 0 || index >= values().length) {
            return NONE;
        }
        return values()[index];
    }

    Comparator<ScannerDataDetail> sortOrder() {
        return sortOrder;
    }

    Comparator<ScannerDataDetail> groupBy() {
        return groupBy;
    }

    static class None implements Comparator<ScannerDataDetail> {
        @Override
        public int compare(ScannerDataDetail lhs, ScannerDataDetail rhs) {
            return lhs.equals(rhs) ? 0 : 1;
        }
    }

    static class SSIDSortOrder implements Comparator<ScannerDataDetail> {
        @Override
        public int compare(ScannerDataDetail lhs, ScannerDataDetail rhs) {
            return new CompareToBuilder()
                .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                .append(rhs.getScannerSignalData().getLevel(), lhs.getScannerSignalData().getLevel())
                .append(lhs.getBSSID().toUpperCase(), rhs.getBSSID().toUpperCase())
                .toComparison();
        }
    }

    static class SSIDGroupBy implements Comparator<ScannerDataDetail> {
        @Override
        public int compare(ScannerDataDetail lhs, ScannerDataDetail rhs) {
            return new CompareToBuilder()
                .append(lhs.getSSID().toUpperCase(), rhs.getSSID().toUpperCase())
                .toComparison();
        }
    }

    static class ChannelSortOrder implements Comparator<ScannerDataDetail> {
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

    static class ChannelGroupBy implements Comparator<ScannerDataDetail> {
        @Override
        public int compare(ScannerDataDetail lhs, ScannerDataDetail rhs) {
            return new CompareToBuilder()
                .append(lhs.getScannerSignalData().getPrimaryWiFiChannel().getChannel(), rhs.getScannerSignalData().getPrimaryWiFiChannel().getChannel())
                .toComparison();
        }
    }

}
