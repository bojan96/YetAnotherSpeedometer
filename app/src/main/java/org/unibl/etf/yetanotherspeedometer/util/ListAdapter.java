package org.unibl.etf.yetanotherspeedometer.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.yetanotherspeedometer.databinding.RecordingItemBinding;
import org.unibl.etf.yetanotherspeedometer.db.AppDatabase;
import org.unibl.etf.yetanotherspeedometer.db.dao.RecordingDao;
import org.unibl.etf.yetanotherspeedometer.db.dao.RecordingPointDao;
import org.unibl.etf.yetanotherspeedometer.db.entity.Recording;
import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingPoint;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Unit;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final UnitFormatters unitFormatters;
    private List<Recording> recordings;
    private final RecordingDao recordingDao;
    private final RecordingPointDao recordingPointDao;

    public ListAdapter(AppDatabase appDatabase, UnitFormatters unitFormatters)
    {
        this.recordingDao = appDatabase.getRecordingDao();
        this.recordingPointDao = appDatabase.getRecordingPointsDao();
        this.unitFormatters = unitFormatters;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private final RecordingItemBinding recordingItemBinding;
        private Recording recording;

        public ViewHolder(RecordingItemBinding recordingItemBinding) {
            super(recordingItemBinding.getRoot());
            this.recordingItemBinding = recordingItemBinding;
            recordingItemBinding.recordingItemDeleteButton.setOnClickListener(view -> deleteRecording());
        }

        private void setAverageSpeed(double averageSpeed)
        {
            recordingItemBinding.recordingItemAverageSpeed.setText(unitFormatters.formatSpeed(averageSpeed));
        }

        private void setMaxSpeed(double maxSpeed)
        {
            recordingItemBinding.recordingItemMaxSpeed.setText(unitFormatters.formatSpeed(maxSpeed));
        }

        private void setTotalDistance(double distance)
        {
            recordingItemBinding.recordingItemDistance.setText(unitFormatters.formatDistance(distance));
        }

        private void setElapsedTime(long elapsedTime)
        {
            recordingItemBinding.recordingItemElapsedTime.setText(UnitFormatters.formatElapsedTime(elapsedTime));
        }

        public void setRecording(Recording recording)
        {
            setMaxSpeed(recording.maxSpeed);
            setAverageSpeed(recording.avgSpeed);
            setElapsedTime(recording.elapsedTime);
            setTotalDistance(recording.totalDistance);
            this.recording = recording;
        }

        public void deleteRecording()
        {
            recordingPointDao.deleteRecordingPoints(recording.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() ->
                    {
                        recordingDao
                                .deleteRecording(recording)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() ->
                                {
                                    var position =  recordings.indexOf(recording);
                                    recordings.remove(position);
                                    notifyItemRemoved(position);
                                });
                    });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = RecordingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setRecording(recordings.get(position));
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    public void setRecordings(List<Recording> recordings)
    {
        this.recordings = recordings;
        notifyDataSetChanged();
    }
}
