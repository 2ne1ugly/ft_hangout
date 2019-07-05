package com.example.hangout;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private static SettingsActivity _self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        _self = this;
//        ActionBar actionBar = getSupportActionBar();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int val = pref.getInt("barColorIndex", 0);
        Resources res = getResources();
        String string = res.getStringArray(R.array.color_values)[val];
        Toolbar toolbar = SettingsActivity._self.findViewById(R.id.toolbarSettings);
        toolbar.setBackgroundColor(Color.parseColor(string));
        toolbar.setTitle(R.string.title_activity_settings);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SettingsActivity _activity;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            final Preference languagePref = findPreference("language");
            final Preference barColorPref = findPreference("barColorIndex");
            languagePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object object) {
                    Resources res = getResources();
                    Configuration config = res.getConfiguration();
                    String str = object.toString();
                    Locale loc = new Locale(str);
                    config.setLocale(loc);
                    res.updateConfiguration(config, res.getDisplayMetrics());
                    getActivity().finishAffinity();
                    return true;
                }
            });
            barColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                    int val = pref.getInt("barColorIndex", 0);
                    val++;
                    if (val >= 9)
                        val = 0;
                    pref.edit().putInt("barColorIndex", val).apply();
                    Resources res = getResources();
                    String string = res.getStringArray(R.array.color_values)[val];
                    Toolbar toolbar = SettingsActivity._self.findViewById(R.id.toolbarSettings);
                    toolbar.setBackgroundColor(Color.parseColor(string));
                    return true;
                }
            });
        }
    }
}