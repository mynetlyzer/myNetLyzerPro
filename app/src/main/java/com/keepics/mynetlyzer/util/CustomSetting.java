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
import android.preference.ListPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

class CustomSetting extends ListPreference {
    CustomSetting(@NonNull Context context, AttributeSet attrs, @NonNull List<SettingData> settingDatas, @NonNull String defaultValue) {
        super(context, attrs);
        setEntries(getNames(settingDatas));
        setEntryValues(getCodes(settingDatas));
        setDefaultValue(defaultValue);
    }

    @NonNull
    private CharSequence[] getCodes(@NonNull List<SettingData> settingDatas) {
        List<String> entryValues = new ArrayList<>();
        for (SettingData settingData : settingDatas) {
            entryValues.add(settingData.getCode());
        }
        return entryValues.toArray(new CharSequence[]{});
    }

    @NonNull
    private CharSequence[] getNames(@NonNull List<SettingData> settingDatas) {
        List<String> entries = new ArrayList<>();
        for (SettingData settingData : settingDatas) {
            entries.add(settingData.getName());
        }
        return entries.toArray(new CharSequence[]{});
    }

}
