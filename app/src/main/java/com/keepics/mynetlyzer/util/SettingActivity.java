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

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.keepics.mynetlyzer.R;

public class SettingActivity extends AppCompatActivity {

    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        backButton = (Button) this.findViewById(R.id.settings_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        backButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Drawable drawableRes = getResources().getDrawable(R.drawable.home);
                    backButton.setBackground(drawableRes);
                }else{
                    Drawable drawableRes = getResources().getDrawable(R.drawable.home_focus);
                    backButton.setBackground(drawableRes);
                }
            }
        });
        backButton.requestFocus();

        getFragmentManager().beginTransaction().replace(R.id.settings_main_fragment, new SettingPreferenceFragment()).commit();
    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    public static class SettingPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                    SharedPreferences.OnSharedPreferenceChangeListener() {
                        @Override
                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                              String key) {
                            String value = sharedPreferences.getString(key,"");
                            Log.d("wifianaylzer",value);
                        }
                    };
            PreferenceManager.getDefaultSharedPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(spChanged);
        }
    }

}
