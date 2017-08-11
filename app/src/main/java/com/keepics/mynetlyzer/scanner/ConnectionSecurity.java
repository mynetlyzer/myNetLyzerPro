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

import com.keepics.mynetlyzer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public enum ConnectionSecurity {
    // weak getSecurity first - keep this order
    NONE(R.drawable.lock2),
    WPS(R.drawable.lock1),
    WEP(R.drawable.lock1),
    WPA(R.drawable.lock3),
    WPA2(R.drawable.lock3);

    private final int imageResource;

    ConnectionSecurity(int imageResource) {
        this.imageResource = imageResource;
    }

    public static List<ConnectionSecurity> findAll(String capabilities) {
        Set<ConnectionSecurity> results = new TreeSet<>();
        if (capabilities != null) {
            String[] values = capabilities.toUpperCase()
                .replace("][", "-").replace("]", "").replace("[", "").split("-");
            for (String value : values) {
                try {
                    results.add(ConnectionSecurity.valueOf(value));
                } catch (Exception e) {
                    // skip getCapabilities that are not getSecurity
                }
            }
        }
        return new ArrayList<>(results);
    }

    public static ConnectionSecurity findOne(String capabilities) {
        List<ConnectionSecurity> securities = findAll(capabilities);
        for (ConnectionSecurity connectionSecurity : ConnectionSecurity.values()) {
            if (securities.contains(connectionSecurity)) {
                return connectionSecurity;
            }
        }
        return ConnectionSecurity.NONE;
    }

    public int imageResource() {
        return imageResource;
    }

}
