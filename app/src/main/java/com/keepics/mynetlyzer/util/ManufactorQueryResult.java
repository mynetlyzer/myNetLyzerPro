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
package com.keepics.mynetlyzer.util;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

class ManufactorQueryResult {
    public static final ManufactorQueryResult EMPTY = new ManufactorQueryResult(StringUtils.EMPTY, StringUtils.EMPTY);

    private final String macAddress;
    private final String vendorName;

    ManufactorQueryResult(@NonNull String macAddress, @NonNull String vendorName) {
        this.macAddress = macAddress;
        this.vendorName = vendorName;
    }

    String getMacAddress() {
        return macAddress;
    }

    String getVendorName() {
        return vendorName;
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
            .append(getMacAddress(), ((ManufactorQueryResult) other).getMacAddress())
            .append(getVendorName(), ((ManufactorQueryResult) other).getVendorName())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getMacAddress())
            .append(getVendorName())
            .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
