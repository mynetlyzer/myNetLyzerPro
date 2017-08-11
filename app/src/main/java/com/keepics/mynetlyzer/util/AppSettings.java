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
import android.support.annotation.NonNull;

import com.keepics.mynetlyzer.R;
import com.keepics.mynetlyzer.scanner.ScannerBand;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class AppSettings {

    private final Context context;
    private SettingsRepository settingsRepository;

    public AppSettings(@NonNull Context context) {
        this.context = context;
        setSettingsRepository(new SettingsRepository());
    }

    public void setSettingsRepository(@NonNull SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public void initializeDefaultValues() {
        settingsRepository.initializeDefaultValues();
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        settingsRepository.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public int getScanInterval() {
        return settingsRepository.getInteger(R.string.scan_interval_key, settingsRepository.getResourceInteger(R.integer.scan_interval_default));
    }


    public ScannerBand getWiFiBand() {
        return ScannerBand.find(settingsRepository.getStringAsInteger(R.string.wifi_band_key, ScannerBand.GHZ2.ordinal()));
    }

    public ThemeStyle getThemeStyle() {
        return ThemeStyle.find(settingsRepository.getStringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal()));
    }


    public String getCountryCode() {
        String countryCode = CountrySetting.getDefault(context);
        return settingsRepository.getString(R.string.country_code_key, countryCode);
    }

}
