
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

public enum ScannerBand {
    GHZ2("2.4 GHz", new ChannelsGHZ2()),
    GHZ5("5 GHz", new ChannelsGHZ5());

    private final String band;
    private final Channels channels;

    ScannerBand(@NonNull String band, @NonNull Channels channels) {
        this.band = band;
        this.channels = channels;
    }

    public static ScannerBand findByFrequency(int frequency) {
        for (ScannerBand scannerBand : ScannerBand.values()) {
            if (scannerBand.getChannels().isInRange(frequency)) {
                return scannerBand;
            }
        }
        return ScannerBand.GHZ2;
    }

    public static ScannerBand find(int index) {
        if (index < 0 || index >= values().length) {
            return GHZ2;
        }
        return values()[index];
    }

    public String getBand() {
        return band;
    }

    public ScannerBand toggle() {
        return isGHZ5() ? ScannerBand.GHZ2 : ScannerBand.GHZ5;
    }

    public boolean isGHZ5() {
        return ScannerBand.GHZ5.equals(this);
    }

    public Channels getChannels() {
        return channels;
    }
}
