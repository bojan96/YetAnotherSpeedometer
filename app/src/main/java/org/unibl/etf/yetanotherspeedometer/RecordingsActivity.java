package org.unibl.etf.yetanotherspeedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Bundle;
import android.util.Log;

import org.unibl.etf.yetanotherspeedometer.databinding.ActivityRecordingsBinding;
import org.unibl.etf.yetanotherspeedometer.db.AppDatabase;
import org.unibl.etf.yetanotherspeedometer.db.dao.RecordingDao;
import org.unibl.etf.yetanotherspeedometer.util.ListAdapter;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecordingsActivity extends AppCompatActivity {

    @Inject
    public AppDatabase appDatabase;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivityRecordingsBinding.inflate(getLayoutInflater());
        var viewModel = new ViewModelProvider(this).get(RecordingsViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        getLifecycle().addObserver(viewModel);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        viewModel.getRecordings().observe(this, recordings ->
        {
            binding.recyclerView.setAdapter(new ListAdapter(recordings, appDatabase.getRecordingDao()));
        });
        setContentView(binding.getRoot());
    }
}