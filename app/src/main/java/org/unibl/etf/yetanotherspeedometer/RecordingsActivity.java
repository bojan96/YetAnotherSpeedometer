package org.unibl.etf.yetanotherspeedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Bundle;

import org.unibl.etf.yetanotherspeedometer.databinding.ActivityRecordingsBinding;
import org.unibl.etf.yetanotherspeedometer.util.ListAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecordingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivityRecordingsBinding.inflate(getLayoutInflater());
        var viewModel = new ViewModelProvider(this).get(RecordingsViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        getLifecycle().addObserver(viewModel);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        viewModel.getRecordings().observe(this, recordings -> binding.recyclerView.setAdapter(new ListAdapter(recordings)));
        setContentView(binding.getRoot());
    }
}