package org.unibl.etf.yetanotherspeedometer.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.unibl.etf.yetanotherspeedometer.R;
import org.unibl.etf.yetanotherspeedometer.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar.getRoot());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment_container, new SettingsFragment())
                .commit();
        setContentView(binding.getRoot());
    }
}