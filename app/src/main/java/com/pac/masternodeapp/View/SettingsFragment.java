package com.pac.masternodeapp.View;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.pac.masternodeapp.R;

public class SettingsFragment extends PreferenceFragment {
    SharedPreferences preferences;
    SwitchPreference switchPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);
        final PreferenceManager preferenceManager = getPreferenceManager();
        preferences = getContext().getSharedPreferences("active_passcode", 0);
        final Editor editor = preferences.edit();
        switchPreference = (SwitchPreference) findPreference("passcode_preference");

        switchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!switchPreference.isChecked()) {
                    editor.putBoolean("active_passcode", false);
                    editor.apply();
                }
                return false;
            }
        });

        Preference setPasscodePref = findPreference(getString(R.string.pref_set_pin));
        setPasscodePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setPasscode();
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        HomeActivity.actionButton.hide();
        checkPreference();
    }

    @Override
    public void onStop() {
        super.onStop();
        checkPreference();
    }

    public void checkPreference() {
        Boolean isActive = preferences.getBoolean("active_passcode", false);
        if (!isActive) {
            switchPreference.setChecked(false);
        } else {
            switchPreference.setChecked(true);
        }
    }

    private void setPasscode() {

        Fragment settingsFragment = new MasternodePasscodeFragment();
        HomeActivity.passcodeSetup = settingsFragment.getClass().getName();
        HomeActivity.changeFragment(settingsFragment, null);
    }
}
