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

import com.keepics.mynetlyzer.AppContext;
import com.keepics.mynetlyzer.util.ManufactorManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScannerData {
    public static final ScannerData EMPTY = new ScannerData(new ArrayList<ScannerDataDetail>(), WiFiConnectionInfo.EMPTY, new ArrayList<String>());

    private final List<ScannerDataDetail> scannerDataDetails;
    private final WiFiConnectionInfo wiFiConnectionInfo;
    private final List<String> wiFiConfigurations;

    public ScannerData(@NonNull List<ScannerDataDetail> scannerDataDetails, @NonNull WiFiConnectionInfo wiFiConnectionInfo, @NonNull List<String> wiFiConfigurations) {
        this.scannerDataDetails = scannerDataDetails;
        this.wiFiConnectionInfo = wiFiConnectionInfo;
        this.wiFiConfigurations = wiFiConfigurations;
    }

    @NonNull
    public ScannerDataDetail getConnection() {
        ManufactorManager manufactorManager = AppContext.INSTANCE.getManufactorManager();
        for (ScannerDataDetail scannerDataDetail : scannerDataDetails) {
            WiFiConnectionInfo connection = new WiFiConnectionInfo(scannerDataDetail.getSSID(), scannerDataDetail.getBSSID());
            if (wiFiConnectionInfo.equals(connection)) {
                String vendorName = manufactorManager.findVendorName(scannerDataDetail.getBSSID());
                WiFiNetworkInfo wiFiNetworkInfo = new WiFiNetworkInfo(vendorName, wiFiConnectionInfo);
                return new ScannerDataDetail(scannerDataDetail, wiFiNetworkInfo);
            }
        }
        return ScannerDataDetail.EMPTY;
    }

    @NonNull
    public List<ScannerDataDetail> getWiFiDetails(@NonNull ScannerBand scannerBand, @NonNull SortBy sortBy) {
        return getWiFiDetails(scannerBand, sortBy, GroupBy.NONE);
    }

    @NonNull
    public List<ScannerDataDetail> getWiFiDetails(@NonNull ScannerBand scannerBand, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
        List<ScannerDataDetail> results = getWiFiDetails(scannerBand);
        if (!results.isEmpty() && !GroupBy.NONE.equals(groupBy)) {
            results = getWiFiDetails(results, sortBy, groupBy);
        }
        Collections.sort(results, sortBy.comparator());
        return Collections.unmodifiableList(results);
    }

    @NonNull
    List<ScannerDataDetail> getWiFiDetails(@NonNull List<ScannerDataDetail> scannerDataDetails, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
        List<ScannerDataDetail> results = new ArrayList<>();
        Collections.sort(scannerDataDetails, groupBy.sortOrder());
        ScannerDataDetail parent = null;
        for (ScannerDataDetail scannerDataDetail : scannerDataDetails) {
            if (parent == null || groupBy.groupBy().compare(parent, scannerDataDetail) != 0) {
                if (parent != null) {
                    Collections.sort(parent.getChildren(), sortBy.comparator());
                }
                parent = scannerDataDetail;
                results.add(parent);
            } else {
                parent.addChild(scannerDataDetail);
            }
        }
        if (parent != null) {
            Collections.sort(parent.getChildren(), sortBy.comparator());
        }
        Collections.sort(results, sortBy.comparator());
        return results;
    }

    @NonNull
    private List<ScannerDataDetail> getWiFiDetails(@NonNull ScannerBand scannerBand) {
        List<ScannerDataDetail> results = new ArrayList<>();
        ScannerDataDetail connection = getConnection();
        ManufactorManager manufactorManager = AppContext.INSTANCE.getManufactorManager();
        for (ScannerDataDetail scannerDataDetail : scannerDataDetails) {
            if (scannerDataDetail.getScannerSignalData().getScannerBand().equals(scannerBand)) {
                if (scannerDataDetail.equals(connection)) {
                    results.add(connection);
                } else {
                    String vendorName = manufactorManager.findVendorName(scannerDataDetail.getBSSID());
                    boolean contains = wiFiConfigurations.contains(scannerDataDetail.getSSID());
                    WiFiNetworkInfo wiFiNetworkInfo = new WiFiNetworkInfo(vendorName, contains);
                    results.add(new ScannerDataDetail(scannerDataDetail, wiFiNetworkInfo));
                }
            }
        }
        return results;
    }

    @NonNull
    public List<ScannerDataDetail> getScannerDataDetails() {
        return Collections.unmodifiableList(scannerDataDetails);
    }

    @NonNull
    public List<String> getWiFiConfigurations() {
        return Collections.unmodifiableList(wiFiConfigurations);
    }

    @NonNull
    public WiFiConnectionInfo getWiFiConnectionInfo() {
        return wiFiConnectionInfo;
    }

}
