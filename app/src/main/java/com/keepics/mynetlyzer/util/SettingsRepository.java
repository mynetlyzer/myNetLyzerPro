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
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

import com.keepics.mynetlyzer.AppContext;
import com.keepics.mynetlyzer.R;

class SettingsRepository {
    void initializeDefaultValues() {
        Context ctx = AppContext.INSTANCE.getMainActivity();
        PreferenceManager.setDefaultValues(ctx, R.xml.preferences, false);
    }

    void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    void save(int key, int value) {
        Context ctx = AppContext.INSTANCE.getMainActivity();
        save(ctx.getString(key), "" + value);
    }

    private void save(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    int getStringAsInteger(int key, int defaultValue) {
        try {
            return Integer.parseInt(getString(key, "" + defaultValue));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    String getString(int key, String defaultValue) {
        Context ctx = AppContext.INSTANCE.getMainActivity();
        String keyValue = ctx.getString(key);
        try {
            return getSharedPreferences().getString(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, defaultValue);
            return defaultValue;
        }
    }

    int getResourceInteger(int key) {
        Context ctx = AppContext.INSTANCE.getMainActivity();

        return ctx.getResources().getInteger(key);
    }

    int getInteger(int key, int defaultValue) {
        Context ctx = AppContext.INSTANCE.getMainActivity();
        String keyValue = ctx.getString(key);
        try {
            return getSharedPreferences().getInt(keyValue, defaultValue);
        } catch (Exception e) {
            save(keyValue, "" + defaultValue);
            return defaultValue;
        }
    }

    private SharedPreferences getSharedPreferences() {
        Context ctx = AppContext.INSTANCE.getMainActivity();
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
}
