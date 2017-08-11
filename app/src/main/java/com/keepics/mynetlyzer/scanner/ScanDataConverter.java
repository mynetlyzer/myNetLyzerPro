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

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ScanDataConverter {

    WiFiConnectionInfo transformWifiInfo(WifiInfo wifiInfo) {
        if (wifiInfo == null || wifiInfo.getNetworkId() == -1) {
            return WiFiConnectionInfo.EMPTY;
        }
        return new WiFiConnectionInfo(
            WiFiUtils.convertSSID(wifiInfo.getSSID()),
            wifiInfo.getBSSID(),
            WiFiUtils.convertIpAddress(wifiInfo.getIpAddress()),
            wifiInfo.getLinkSpeed());
    }

    List<String> transformWifiConfigurations(List<WifiConfiguration> configuredNetworks) {
        List<String> results = new ArrayList<>();
        if (configuredNetworks != null) {
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                results.add(WiFiUtils.convertSSID(wifiConfiguration.SSID));
            }
        }
        return Collections.unmodifiableList(results);
    }

    List<ScannerDataDetail> transformCacheResults(List<ScannerLocalMemoryDataItem> scannerLocalMemoryDataItems) {
        List<ScannerDataDetail> results = new ArrayList<>();
        if (scannerLocalMemoryDataItems != null) {
            for (ScannerLocalMemoryDataItem scannerLocalMemoryDataItem : scannerLocalMemoryDataItems) {
                ScanResult scanResult = scannerLocalMemoryDataItem.getScanResult();
                ChannelWidth channelWidth = getWiFiWidth(scanResult);
                int centerFrequency = getCenterFrequency(scanResult, channelWidth);
                ScannerSignalData scannerSignalData = new ScannerSignalData(scanResult.frequency, centerFrequency, channelWidth, scannerLocalMemoryDataItem.getLevelAverage());
                ScannerDataDetail scannerDataDetail = new ScannerDataDetail(scanResult.SSID, scanResult.BSSID, scanResult.capabilities, scannerSignalData);
                results.add(scannerDataDetail);
            }
        }
        return Collections.unmodifiableList(results);
    }

    ChannelWidth getWiFiWidth(@NonNull ScanResult scanResult) {
        try {
            return ChannelWidth.find(getFieldValue(scanResult, Fields.channelWidth));
        } catch (Exception e) {
            return ChannelWidth.MHZ_20;
        }
    }

    int getCenterFrequency(@NonNull ScanResult scanResult, @NonNull ChannelWidth channelWidth) {
        try {
            int centerFrequency = getFieldValue(scanResult, Fields.centerFreq0);
            if (centerFrequency == 0) {
                centerFrequency = scanResult.frequency;
            } else if (isExtensionFrequency(scanResult, channelWidth, centerFrequency)) {
                centerFrequency = (centerFrequency + scanResult.frequency) / 2;
            }
            return centerFrequency;
        } catch (Exception e) {
            return scanResult.frequency;
        }
    }

    boolean isExtensionFrequency(@NonNull ScanResult scanResult, @NonNull ChannelWidth channelWidth, int centerFrequency) {
        return ChannelWidth.MHZ_40.equals(channelWidth) && Math.abs(scanResult.frequency - centerFrequency) >= ChannelWidth.MHZ_40.getFrequencyWidthHalf();
    }

    int getFieldValue(@NonNull ScanResult scanResult, @NonNull Fields field) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = scanResult.getClass().getDeclaredField(field.name());
        return (int) declaredField.get(scanResult);
    }

    ScannerData transformToWiFiData(List<ScannerLocalMemoryDataItem> scannerLocalMemoryDataItems, WifiInfo wifiInfo, List<WifiConfiguration> configuredNetworks) {
        List<ScannerDataDetail> scannerDataDetails = transformCacheResults(scannerLocalMemoryDataItems);
        WiFiConnectionInfo wiFiConnectionInfo = transformWifiInfo(wifiInfo);
        List<String> wifiConfigurations = transformWifiConfigurations(configuredNetworks);
        return new ScannerData(scannerDataDetails, wiFiConnectionInfo, wifiConfigurations);
    }

    enum Fields {
        centerFreq0,
        //        centerFreq1,
        channelWidth
    }

}
