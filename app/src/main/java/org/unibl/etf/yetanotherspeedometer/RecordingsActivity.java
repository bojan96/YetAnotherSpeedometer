package org.unibl.etf.yetanotherspeedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import org.unibl.etf.yetanotherspeedometer.databinding.ActivityRecordingsBinding;

public class RecordingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivityRecordingsBinding.inflate(getLayoutInflater());
        var viewModel = new ViewModelProvider(this).get(RecordingsViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        getLifecycle().addObserver(viewModel);
        setContentView(binding.getRoot());
    }
}