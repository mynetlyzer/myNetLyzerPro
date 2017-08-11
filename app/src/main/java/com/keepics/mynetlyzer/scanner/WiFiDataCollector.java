
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
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.keepics.mynetlyzer.AppContext;
import com.keepics.mynetlyzer.util.AppSettings;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

public class WiFiDataCollector {
    private final List<ScannerNotifier> scannerNotifiers;
    private final WifiManager wifiManager;
    private ScanDataConverter scanDataConverter;
    private ScannerData scannerData;
    private ScannerLocalMemory scannerLocalMemory;
    private ScheduledScanTask scheduledScanTask;

    public WiFiDataCollector(@NonNull WifiManager wifiManager, @NonNull Handler handler, @NonNull AppSettings appSettings) {
        this.scannerNotifiers = new ArrayList<>();
        this.wifiManager = wifiManager;
        this.scannerData = ScannerData.EMPTY;
        this.setScanDataConverter(new ScanDataConverter());
        this.setScannerLocalMemory(new ScannerLocalMemory());
        this.scheduledScanTask = new ScheduledScanTask(this, handler, appSettings);
    }

    public void update() {
        performWiFiScan();
        for (ScannerNotifier scannerNotifier : scannerNotifiers) {
            scannerNotifier.update(scannerData);
        }
    }

    private void performWiFiScan() {
        List<ScanResult> scanResults = new ArrayList<>();
        WifiInfo wifiInfo = null;
        List<WifiConfiguration> configuredNetworks = null;
        try {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            if (wifiManager.startScan()) {
                scanResults = wifiManager.getScanResults();
            }
            wifiInfo = wifiManager.getConnectionInfo();
            configuredNetworks = wifiManager.getConfiguredNetworks();
        } catch (Exception e) {

        }
        scannerLocalMemory.add(scanResults);
        scannerData = scanDataConverter.transformToWiFiData(scannerLocalMemory.getScanResults(), wifiInfo, configuredNetworks);

        getBestChannels(scannerData, ReportDataManager.band);
    }

    public ScannerData getScannerData() {
        return scannerData;
    }

    public void getBestChannels(@NonNull ScannerData scannerData, int band){

        ScannerDataDetail connection = scannerData.getConnection();
        ScannerSignalData scannerSignalData = connection.getScannerSignalData();
        SignalStrength signalStrength = scannerSignalData.getStrength();
        WiFiNetworkInfo wiFiNetworkInfo = connection.getWiFiNetworkInfo();
        WiFiConnectionInfo wiFiConnectionInfo = wiFiNetworkInfo.getWiFiConnectionInfo();

        AppSettings appSettings = AppContext.INSTANCE.getAppSettings();
        String channelText = scannerSignalData.getChannelDisplay();

        ScannerBand scannerBand;
        if(band == 0){
            scannerBand = ScannerBand.GHZ2;
        }else{
            scannerBand = ScannerBand.GHZ5;
        }

        String countryCode = appSettings.getCountryCode();
        List<ScannerChannel> scannerChannels = scannerBand.getChannels().getAvailableChannels(countryCode);
        addAll(scannerChannels);
        ChannelRating channelRate = new ChannelRating();
        channelRate.setScannerDataDetails(scannerData.getWiFiDetails(scannerBand, SortBy.STRENGTH));
        int best = channelRate.getBestChannelsV2(scannerChannels);

        if(best == 0 && ScannerBand.GHZ2.equals(scannerBand)){
            ReportDataManager.getInstance().alternativeBand = "5";
        }else{
            ReportDataManager.getInstance().alternativeBand = "-1";
        }

        ReportDataManager.getInstance().alternativeChannelString = Integer.toString(best);
    }


    public void register(@NonNull ScannerNotifier scannerNotifier) {
        scannerNotifiers.add(scannerNotifier);
    }

    public void unregister(@NonNull ScannerNotifier scannerNotifier) {
        scannerNotifiers.remove(scannerNotifier);
    }

    public void pause() {
        scheduledScanTask.stop();
    }

    public boolean isRunning() {
        return scheduledScanTask.isRunning();
    }

    public void resume() {
        scheduledScanTask.start();
    }

    ScheduledScanTask getScheduledScanTask() {
        return scheduledScanTask;
    }

    void setScheduledScanTask(@NonNull ScheduledScanTask scheduledScanTask) {
        this.scheduledScanTask = scheduledScanTask;
    }

    void setScannerLocalMemory(@NonNull ScannerLocalMemory scannerLocalMemory) {
        this.scannerLocalMemory = scannerLocalMemory;
    }

    void setScanDataConverter(@NonNull ScanDataConverter scanDataConverter) {
        this.scanDataConverter = scanDataConverter;
    }

    List<ScannerNotifier> getScannerNotifiers() {
        return scannerNotifiers;
    }
}
