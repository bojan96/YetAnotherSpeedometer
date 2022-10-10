package org.unibl.etf.yetanotherspeedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import org.unibl.etf.yetanotherspeedometer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivityMainBinding.inflate(getLayoutInflater());
        var viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        setContentView(binding.getRoot());
    }
}