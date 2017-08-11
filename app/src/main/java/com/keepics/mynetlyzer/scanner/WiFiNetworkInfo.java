
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
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WiFiNetworkInfo {
    public static final WiFiNetworkInfo EMPTY = new WiFiNetworkInfo(StringUtils.EMPTY, false);

    private final String vendorName;
    private final boolean configuredNetwork;
    private final WiFiConnectionInfo wiFiConnectionInfo;

    private WiFiNetworkInfo(@NonNull String vendorName, @NonNull WiFiConnectionInfo wiFiConnectionInfo, boolean configuredNetwork) {
        this.vendorName = vendorName;
        this.wiFiConnectionInfo = wiFiConnectionInfo;
        this.configuredNetwork = configuredNetwork;
    }

    public WiFiNetworkInfo(@NonNull String vendorName, boolean configuredNetwork) {
        this(vendorName, WiFiConnectionInfo.EMPTY, configuredNetwork);
    }

    public WiFiNetworkInfo(@NonNull String vendorName, @NonNull WiFiConnectionInfo wiFiConnectionInfo) {
        this(vendorName, wiFiConnectionInfo, true);
    }

    public String getVendorName() {
        return vendorName;
    }

    public WiFiConnectionInfo getWiFiConnectionInfo() {
        return wiFiConnectionInfo;
    }

    public boolean isConfiguredNetwork() {
        return configuredNetwork;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}