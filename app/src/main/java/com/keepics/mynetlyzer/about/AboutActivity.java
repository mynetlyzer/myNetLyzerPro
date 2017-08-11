
package com.keepics.mynetlyzer.about;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.keepics.mynetlyzer.BuildConfig;
import com.keepics.mynetlyzer.AppContext;
import com.keepics.mynetlyzer.MainActivity;
import com.keepics.mynetlyzer.R;
import com.keepics.mynetlyzer.util.AppSettings;
import com.keepics.mynetlyzer.util.ThemeStyle;

public class AboutActivity extends AppCompatActivity {

    private Button backButton;
    Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setExtraInformation();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        backButton = (Button) this.findViewById(R.id.about_back);
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
    }

    private void setCustomTheme() {
        AppSettings appSettings = AppContext.INSTANCE.getAppSettings();
        if (appSettings != null) {
            ThemeStyle themeStyle = appSettings.getThemeStyle();
            setTheme(themeStyle.themeAppCompatStyle());
        }
    }

    private void setExtraInformation() {
        String text = BuildConfig.VERSION_NAME;
        setText(R.id.about_version_info, text);
        setText(R.id.about_package_name, BuildConfig.APPLICATION_ID);
    }

    private void setText(int id, String text) {
        TextView version = (TextView) findViewById(id);
        if (version != null) {
            version.setText(text);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        backButton.requestFocus();
        super.onResume();
    }

}
