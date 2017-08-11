
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

package com.keepics.mynetlyzer.scanner;

import android.support.v4.util.Pair;

public class BandChannelSet {

    public Pair<ScannerChannel, ScannerChannel> wiFiChannelPair;
    public ScannerBand scannerBand;
    public String name;
    public int startChannel;
    public int lastChannel;

    public  BandChannelSet(ScannerBand band, Pair<ScannerChannel, ScannerChannel> pair){
        this.wiFiChannelPair = pair;
        this.scannerBand = band;
        this.startChannel = wiFiChannelPair.first.getChannel();
        this.lastChannel = wiFiChannelPair.second.getChannel();
        String firstChannelStr = Integer.toString(startChannel);
        String lastChannelStr = Integer.toString(lastChannel);
        this.name = firstChannelStr + "-" + lastChannelStr;
    }

    public boolean is2GHZ(){
        return (!this.scannerBand.isGHZ5());
    }
}
