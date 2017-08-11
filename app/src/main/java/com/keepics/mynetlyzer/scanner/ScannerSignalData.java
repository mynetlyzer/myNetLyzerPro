
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ScannerSignalData {
    public static final ScannerSignalData EMPTY = new ScannerSignalData(0, 0, ChannelWidth.MHZ_20, 0);
    public static final String FREQUENCY_UNITS = "MHz";

    private final int primaryFrequency;
    private final int centerFrequency;
    private final ChannelWidth channelWidth;
    private final ScannerBand scannerBand;
    private final int level;

    public ScannerSignalData(int primaryFrequency, int centerFrequency, @NonNull ChannelWidth channelWidth, int level) {
        this.primaryFrequency = primaryFrequency;
        this.centerFrequency = centerFrequency;
        this.channelWidth = channelWidth;
        this.level = level;
        this.scannerBand = ScannerBand.findByFrequency(primaryFrequency);
    }

    public int getPrimaryFrequency() {
        return primaryFrequency;
    }

    public int getCenterFrequency() {
        return centerFrequency;
    }

    public int getFrequencyStart() {
        return getCenterFrequency() - getChannelWidth().getFrequencyWidthHalf();
    }

    public int getFrequencyEnd() {
        return getCenterFrequency() + getChannelWidth().getFrequencyWidthHalf();
    }

    public ScannerBand getScannerBand() {
        return scannerBand;
    }

    public ChannelWidth getChannelWidth() {
        return channelWidth;
    }

    public ScannerChannel getPrimaryWiFiChannel() {
        return getScannerBand().getChannels().getWiFiChannelByFrequency(getPrimaryFrequency());
    }

    public ScannerChannel getCenterWiFiChannel() {
        return getScannerBand().getChannels().getWiFiChannelByFrequency(getCenterFrequency());
    }

    public int getLevel() {
        return level;
    }

    public SignalStrength getStrength() {
        return SignalStrength.calculate(level);
    }

    public double getDistance() {
        return WiFiUtils.calculateDistance(getPrimaryFrequency(), getLevel());
    }

    public boolean isInRange(int frequency) {
        return frequency >= getFrequencyStart() && frequency <= getFrequencyEnd();
    }

    @NonNull
    public String getChannelDisplay() {
        int primaryChannel = getPrimaryWiFiChannel().getChannel();
        int centerChannel = getCenterWiFiChannel().getChannel();
        String channel = "" + primaryChannel;
        if (primaryChannel != centerChannel) {
            channel += "(" + centerChannel + ")";
        }
        return channel;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return new EqualsBuilder()
            .append(getPrimaryFrequency(), ((ScannerSignalData) other).getPrimaryFrequency())
            .append(getChannelWidth(), ((ScannerSignalData) other).getChannelWidth())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getPrimaryFrequency())
            .append(getChannelWidth())
            .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
