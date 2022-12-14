package org.unibl.etf.yetanotherspeedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Bundle;
import android.util.Log;

import org.unibl.etf.yetanotherspeedometer.databinding.ActivityRecordingsBinding;
import org.unibl.etf.yetanotherspeedometer.db.AppDatabase;
import org.unibl.etf.yetanotherspeedometer.db.dao.RecordingDao;
import org.unibl.etf.yetanotherspeedometer.settings.SettingsStore;
import org.unibl.etf.yetanotherspeedometer.util.ListAdapter;
import org.unibl.etf.yetanotherspeedometer.util.UnitFormatters;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecordingsActivity extends AppCompatActivity {

    @Inject
    public AppDatabase appDatabase;
    @Inject
    public UnitFormatters unitFormatters;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivityRecordingsBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar.getRoot());
        var viewModel = new ViewModelProvider(this).get(RecordingsViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        getLifecycle().addObserver(viewModel);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ListAdapter(appDatabase, unitFormatters);
        viewModel.getRecordings().observe(this, recordings ->
        {
            if(binding.recyclerView.getAdapter() != null)
                return;
            Log.d(RecordingsActivity.class.getName(), "Observing recordings");
            adapter.setRecordings(recordings);
            binding.recyclerView.setAdapter(adapter);
        });
        setContentView(binding.getRoot());
    }
}