package com.pac.masternodeapp.View;

import android.Manifest;
import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.pac.masternodeapp.R;

/**
 * Created by PACcoin Team on 3/14/2018.
 */

public class SettingsFragment extends PreferenceFragment {
    SharedPreferences preferences;
    SwitchPreference switchPreference;
    //fingerprint_preference
    SwitchPreference fingerprintPreference;

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

        fingerprintPreference = (SwitchPreference) findPreference("fingerprint_preference");
        fingerprintPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!fingerprintPreference.isChecked()) {
                    editor.putBoolean("active_fingerprint", false);
                    editor.apply();
                }else{
                    editor.putBoolean("active_fingerprint", true);
                    editor.apply();
                }
                return false;
            }
        });

        FingerprintManager fingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);

        if (!fingerprintManager.isHardwareDetected()) {
            // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
            Toast.makeText(getContext(), "Your device doesn't support fingerprint authentication", Toast.LENGTH_SHORT).show();
        }
        //Check whether the user has granted your app the USE_FINGERPRINT permission//
        else if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            // If your app doesn't have this permission, then display the following text//
            Toast.makeText(getContext(), "Please enable the fingerprint permission", Toast.LENGTH_SHORT).show();
        }
        //Check that the user has registered at least one fingerprint//
        else if (!fingerprintManager.hasEnrolledFingerprints()) {
            // If the user hasn’t configured any fingerprints, then display the following message//
            Toast.makeText(getContext(), "No fingerprint configured. Please register at least one fingerprint in your device's Settings", Toast.LENGTH_SHORT).show();
        }
        //Check that the lockscreen is secured//
        else if (!keyguardManager.isKeyguardSecure()) {
            // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
            Toast.makeText(getContext(), "Please enable lockscreen security in your device's Settings", Toast.LENGTH_SHORT).show();
        }else{
            fingerprintPreference.setEnabled(true);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        HomeActivity.main_menu.findItem(R.id.action_search).setVisible(false);
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
