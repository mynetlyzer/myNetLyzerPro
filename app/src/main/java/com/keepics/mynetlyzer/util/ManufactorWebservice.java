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

import android.os.AsyncTask;

import com.keepics.mynetlyzer.AppContext;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class ManufactorWebservice extends AsyncTask<String, Void, ManufactorQueryResult> {
    static final String MAC_VENDOR_LOOKUP = "http://api.macvendors.com/%s";

    @Override
    protected ManufactorQueryResult doInBackground(String... params) {
        if (params == null || params.length < 1 || StringUtils.isBlank(params[0])) {
            return ManufactorQueryResult.EMPTY;
        }
        String macAddress = params[0];
        String request = String.format(MAC_VENDOR_LOOKUP, macAddress.substring(0, macAddress.length() / 2));
        BufferedReader bufferedReader = null;
        try {
            URLConnection urlConnection = new URL(request).openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            String vendorName = WiFiUtil.cleanVendorName(response.toString().trim());
            if (StringUtils.isNotBlank(vendorName)) {
                return new ManufactorQueryResult(macAddress, vendorName);
            }
            return ManufactorQueryResult.EMPTY;
        } catch (Exception e) {
            return ManufactorQueryResult.EMPTY;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }


    @Override
    protected void onPostExecute(ManufactorQueryResult result) {
        String macAddress = result.getMacAddress();
        String vendorName = result.getVendorName();
        if (StringUtils.isNotBlank(vendorName) && StringUtils.isNotBlank(macAddress)) {
            Database database = AppContext.INSTANCE.getDatabase();
            database.insert(macAddress, vendorName);
        }
    }
}
