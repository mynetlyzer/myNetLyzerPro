
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

import android.os.Handler;
import android.support.annotation.NonNull;

import com.keepics.mynetlyzer.util.AppSettings;

class ScheduledScanTask implements Runnable {
    static final int DELAY_INITIAL = 1;
    static final int DELAY_INTERVAL = 1000;

    private final WiFiDataCollector wiFiDataCollector;
    private final Handler handler;
    private final AppSettings appSettings;
    private boolean running;

    ScheduledScanTask(@NonNull WiFiDataCollector wiFiDataCollector, @NonNull Handler handler, @NonNull AppSettings appSettings) {
        this.wiFiDataCollector = wiFiDataCollector;
        this.handler = handler;
        this.appSettings = appSettings;
        start();
    }

    void stop() {
        handler.removeCallbacks(this);
        running = false;
    }

    void start() {
        nextRun(DELAY_INITIAL);
    }

    private void nextRun(int delayInitial) {
        stop();
        handler.postDelayed(this, delayInitial);
        running = true;
    }

    @Override
    public void run() {
        wiFiDataCollector.update();
        nextRun(appSettings.getScanInterval() * DELAY_INTERVAL);
    }

    boolean isRunning() {
        return running;
    }
}
