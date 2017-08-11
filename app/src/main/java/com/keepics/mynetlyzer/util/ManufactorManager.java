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

import com.keepics.mynetlyzer.AppContext;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class ManufactorManager {
    private final Set<String> remoteCalls = new TreeSet<>();
    private final Map<String, String> cache = new HashMap<>();

    private ManufactorWebservice manufactorWebservice;

    public String findVendorName(String macAddress) {
        String key = MacAddress.clean(macAddress);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        Database database = AppContext.INSTANCE.getDatabase();
        String result = database.find(macAddress);
        if (result != null) {
            result = WiFiUtil.cleanVendorName(result);
            cache.put(key, result);
            return result;
        }
        if (!remoteCalls.contains(key)) {
            remoteCalls.add(key);
            getManufactorWebservice().execute(macAddress);
        }
        return StringUtils.EMPTY;
    }

    void clear() {
        cache.clear();
        remoteCalls.clear();
    }

    public SortedMap<String, List<String>> findAll() {
        SortedMap<String, List<String>> results = new TreeMap<>();
        Database database = AppContext.INSTANCE.getDatabase();
        List<ManufactorItem> manufactorItems = database.findAll();
        for (ManufactorItem manufactorItem : manufactorItems) {
            String key = WiFiUtil.cleanVendorName(manufactorItem.getName());
            List<String> macs = results.get(key);
            if (macs == null) {
                macs = new ArrayList<>();
                results.put(key, macs);
            }
            macs.add(manufactorItem.getMac());
            Collections.sort(macs);
        }
        return results;
    }

    // injectors start
    private ManufactorWebservice getManufactorWebservice() {
        return manufactorWebservice == null ? new ManufactorWebservice() : manufactorWebservice;
    }

    void setManufactorWebservice(@NonNull ManufactorWebservice manufactorWebservice) {
        this.manufactorWebservice = manufactorWebservice;
    }
    // injectors end
}
