
package com.keepics.mynetlyzer;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.keepics.mynetlyzer.util.AppSettings;
import com.keepics.mynetlyzer.util.Database;
import com.keepics.mynetlyzer.util.ManufactorManager;
import com.keepics.mynetlyzer.scanner.WiFiDataCollector;

public enum AppContext {
    INSTANCE;

    public AppSettings appSettings;
    public MainActivity mainActivity;
    public WiFiDataCollector wiFiDataCollector;
    public ManufactorManager manufactorManager;
    public Database database;
    public AppConfiguration appConfiguration;

    public AppSettings getAppSettings() {
        return appSettings;
    }

    public void setAppSettings(AppSettings appSettings) {
        this.appSettings = appSettings;
    }

    public ManufactorManager getManufactorManager() {
        return manufactorManager;
    }

    public void setManufactorManager(ManufactorManager manufactorManager) {
        this.manufactorManager = manufactorManager;
    }

    public WiFiDataCollector getWiFiDataCollector() {
        return wiFiDataCollector;
    }

    public void setWiFiDataCollector(WiFiDataCollector wiFiDataCollector) {
        this.wiFiDataCollector = wiFiDataCollector;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public AppConfiguration getAppConfiguration() {
        return appConfiguration;
    }

    public void setAppConfiguration(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    void initialize(@NonNull MainActivity mainActivity, boolean largeScreen) {
        //WifiManager wifiManager = (WifiManager) mainActivity.getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiManager = (WifiManager) mainActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Handler handler = new Handler();
        AppSettings appSettings = new AppSettings(mainActivity);
        AppConfiguration appConfiguration = new AppConfiguration(largeScreen);

        setMainActivity(mainActivity);
        setAppConfiguration(appConfiguration);
        setDatabase(new Database(mainActivity));
        setAppSettings(appSettings);
        setManufactorManager(new ManufactorManager());
        setWiFiDataCollector(new WiFiDataCollector(wifiManager, handler, appSettings));
    }
}
