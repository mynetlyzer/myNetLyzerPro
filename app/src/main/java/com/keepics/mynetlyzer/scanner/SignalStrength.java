
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

public enum SignalStrength {
    ZERO(R.drawable.wifi_strength_0, R.color.error_color),
    ONE(R.drawable.wifi_strength_1, R.color.warning_color),
    TWO(R.drawable.wifi_strength_2, R.color.warning_color),
    THREE(R.drawable.wifi_strength_3, R.color.success_color),
    FOUR(R.drawable.wifi_strength_4, R.color.success_color),
    FIVE(R.drawable.wifi_strength_5, R.color.success_color);

    private final int imageResource;
    private final int colorResource;

    SignalStrength(int imageResource, int colorResource) {
        this.imageResource = imageResource;
        this.colorResource = colorResource;
    }

    public static SignalStrength calculate(int level) {
        int index = WiFiUtils.calculateSignalLevel(level, values().length);
        return SignalStrength.values()[index];
    }

    public static SignalStrength reverse(SignalStrength signalStrength) {
        int index = SignalStrength.values().length - signalStrength.ordinal() - 1;
        return SignalStrength.values()[index];
    }

    public int colorResource() {
        return colorResource;
    }

    public int imageResource() {
        return imageResource;
    }

    public boolean weak() {
        return ZERO.equals(this);
    }

}
