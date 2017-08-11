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


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.keepics.mynetlyzer.scanner.ChannelCountry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CountrySetting extends CustomSetting {
    public CountrySetting(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs, getData(), getDefault(context));
    }

    @NonNull
    private static List<SettingData> getData() {
        List<SettingData> result = new ArrayList<>();
        for (ChannelCountry channelCountry : ChannelCountry.getAll()) {
            result.add(new SettingData(channelCountry.getCountryCode(), channelCountry.getCountryName()));
        }
        Collections.sort(result);
        return result;
    }

    @NonNull
    public static String getDefault(@NonNull Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        return getLocale(configuration).getCountry();
    }

    @NonNull
    @SuppressWarnings("deprecation")
    private static Locale getLocale(@NonNull Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        }
        return config.locale;
    }

}
