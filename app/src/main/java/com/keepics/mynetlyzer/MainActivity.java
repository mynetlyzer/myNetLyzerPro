
package com.keepics.mynetlyzer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amazon.device.iap.PurchasingService;
import com.keepics.mynetlyzer.about.AboutActivity;
import com.keepics.mynetlyzer.graphscreen.WiFiGraphViewFragment;
import com.keepics.mynetlyzer.scanner.WiFiDataCollector;
import com.keepics.mynetlyzer.support.SupportFragment;
import com.keepics.mynetlyzer.util.SettingActivity;
import com.keepics.mynetlyzer.util.AppSettings;
import com.keepics.mynetlyzer.home.HomeFragment;
import com.keepics.mynetlyzer.scanner.ScannerBand;
import com.keepics.mynetlyzer.scanner.ScannerChannel;


public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {
    private String currentCountryCode;
    private static MainActivity instance;
    static public boolean isInitialLoad = false;

    WiFiGraphViewFragment wiFiGraphViewFragment = new WiFiGraphViewFragment();
    HomeFragment homeFragment = new HomeFragment();
    SupportFragment supportFragment = new SupportFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        AppContext appContext = AppContext.INSTANCE;
        appContext.initialize(this, isLargeScreen());

        AppSettings appSettings = appContext.getAppSettings();
        appSettings.initializeDefaultValues();

        setTheme(appSettings.getThemeStyle().themeAppCompatStyle());
        setWiFiChannelPairs(appContext);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        appSettings.registerOnSharedPreferenceChangeListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gotoHomeScreen("Home");
    }


    public static MainActivity getInstance(){
        return instance;
    }

    private void setWiFiChannelPairs(AppContext appContext) {
        AppSettings appSettings = appContext.getAppSettings();
        String countryCode = appSettings.getCountryCode();
        if (!countryCode.equals(currentCountryCode)) {
            Pair<ScannerChannel, ScannerChannel> pair = ScannerBand.GHZ5.getChannels().getWiFiChannelPairFirst(countryCode);
            AppConfiguration appConfiguration = appContext.getAppConfiguration();
            appConfiguration.setWiFiChannelPair(pair);
            currentCountryCode = countryCode;
        }
    }

    private boolean isLargeScreen() {
        Resources resources = getResources();
        android.content.res.Configuration configuration = resources.getConfiguration();
        int screenLayoutSize = configuration.screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenLayoutSize == android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE ||
            screenLayoutSize == android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        AppContext appContext = AppContext.INSTANCE;
    }

    private void reloadActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |
            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = MainActivity.getInstance().getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount()> 0){
            fragmentManager.popBackStack();
        }else{
            finish();
            WiFiDataCollector wiFiDataCollector = AppContext.INSTANCE.getWiFiDataCollector();
            wiFiDataCollector.pause();
        }
    }


    public void gotoWifiGraphScreen(String tag){
        FragmentManager fragmentManager = MainActivity.getInstance().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, wiFiGraphViewFragment,tag ).addToBackStack("home").commit();
        MainActivity.getInstance().setTitle("Current WiFi");
    }

    public void gotoSupportScreen(String tag){
        FragmentManager fragmentManager = MainActivity.getInstance().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, supportFragment,tag ).addToBackStack("home").commit();
        MainActivity.getInstance().setTitle("Current WiFi");
    }

    public void gotoHomeScreen(String tag){
        FragmentManager fragmentManager = MainActivity.getInstance().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, homeFragment,tag ).commit();

        MainActivity.getInstance().setTitle("Home");
    }

    public void gotoSettingsScreen(){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void gotoAboutScreen(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
