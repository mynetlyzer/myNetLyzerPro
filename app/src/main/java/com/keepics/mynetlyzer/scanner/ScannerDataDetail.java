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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class ScannerDataDetail implements Comparable<ScannerDataDetail> {
    public static final ScannerDataDetail EMPTY = new ScannerDataDetail(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, ScannerSignalData.EMPTY);

    private final List<ScannerDataDetail> children;
    private final String SSID;
    private final String BSSID;
    private final String capabilities;
    private final ScannerSignalData scannerSignalData;
    private final WiFiNetworkInfo wiFiNetworkInfo;

    public ScannerDataDetail(@NonNull String SSID, @NonNull String BSSID, @NonNull String capabilities,
                             @NonNull ScannerSignalData scannerSignalData, @NonNull WiFiNetworkInfo wiFiNetworkInfo) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.capabilities = capabilities;
        this.scannerSignalData = scannerSignalData;
        this.wiFiNetworkInfo = wiFiNetworkInfo;
        this.children = new ArrayList<>();
    }

    public ScannerDataDetail(@NonNull String SSID, @NonNull String BSSID, @NonNull String capabilities, @NonNull ScannerSignalData scannerSignalData) {
        this(SSID, BSSID, capabilities, scannerSignalData, WiFiNetworkInfo.EMPTY);
    }

    public ScannerDataDetail(@NonNull ScannerDataDetail scannerDataDetail, @NonNull WiFiNetworkInfo wiFiNetworkInfo) {
        this(scannerDataDetail.SSID, scannerDataDetail.BSSID, scannerDataDetail.getCapabilities(), scannerDataDetail.getScannerSignalData(), wiFiNetworkInfo);
    }

    public ConnectionSecurity getSecurity() {
        return ConnectionSecurity.findOne(capabilities);
    }

    public String getSSID() {
        return isHidden() ? "***" : SSID;
    }

    public boolean isHidden() {
        return StringUtils.isBlank(SSID);
    }

    public String getBSSID() {
        return BSSID;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public ScannerSignalData getScannerSignalData() {
        return scannerSignalData;
    }

    public WiFiNetworkInfo getWiFiNetworkInfo() {
        return wiFiNetworkInfo;
    }

    public List<ScannerDataDetail> getChildren() {
        return children;
    }

    public String getTitle() {
        return String.format("%s (%s)", getSSID(), getBSSID());
    }

    public void addChild(@NonNull ScannerDataDetail scannerDataDetail) {
        children.add(scannerDataDetail);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        ScannerDataDetail otherDetail = (ScannerDataDetail) other;
        return new EqualsBuilder()
            .append(getSSID(), otherDetail.getSSID())
            .append(getBSSID(), otherDetail.getBSSID())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getSSID())
            .append(getBSSID())
            .toHashCode();
    }

    @Override
    public int compareTo(@NonNull ScannerDataDetail another) {
        return new CompareToBuilder()
            .append(getSSID(), another.getSSID())
            .append(getBSSID(), another.getBSSID())
            .toComparison();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}