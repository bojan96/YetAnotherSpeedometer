package org.unibl.etf.yetanotherspeedometer.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.unibl.etf.yetanotherspeedometer.R;
import org.unibl.etf.yetanotherspeedometer.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment_container, new SettingsFragment())
                .commit();
        setContentView(ActivitySettingsBinding.inflate(getLayoutInflater()).getRoot());
    }
}