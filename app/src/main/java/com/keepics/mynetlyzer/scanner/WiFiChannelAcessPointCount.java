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
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WiFiChannelAcessPointCount implements Comparable<WiFiChannelAcessPointCount> {
    private final ScannerChannel scannerChannel;
    private final int count;

    public WiFiChannelAcessPointCount(@NonNull ScannerChannel scannerChannel, int count) {
        this.scannerChannel = scannerChannel;
        this.count = count;
    }

    public ScannerChannel getScannerChannel() {
        return scannerChannel;
    }

    int getCount() {
        return count;
    }

    @Override
    public int compareTo(@NonNull WiFiChannelAcessPointCount another) {
        return new CompareToBuilder()
            .append(getCount(), another.getCount())
            .append(getScannerChannel(), another.getScannerChannel())
            .toComparison();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
