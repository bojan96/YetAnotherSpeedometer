package org.unibl.etf.yetanotherspeedometer.util;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.yetanotherspeedometer.databinding.RecordingItemBinding;
import org.unibl.etf.yetanotherspeedometer.db.entity.Recording;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<Recording> recordings;

    public ListAdapter(List<Recording> recordings)
    {
        this.recordings = recordings;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final RecordingItemBinding recordingItemBinding;

        public ViewHolder(RecordingItemBinding recordingItemBinding) {
            super(recordingItemBinding.getRoot());
            this.recordingItemBinding = recordingItemBinding;
        }

        private void setAverageSpeed(double averageSpeed)
        {
            recordingItemBinding.recordingItemAverageSpeed.setText(String.format("%.2f km/h", averageSpeed));
        }

        private void setMaxSpeed(double maxSpeed)
        {
            recordingItemBinding.recordingItemMaxSpeed.setText(String.format("%.2f km/h", maxSpeed));
        }

        private void setTotalDistance(double distance)
        {
            recordingItemBinding.recordingItemDistance.setText(String.format("%f m", distance));
        }

        private void setElapsedTime(long elapsedTime)
        {
            recordingItemBinding.recordingItemElapsedTime.setText(String.format("%d s", elapsedTime));
        }

        public void setRecording(Recording recording)
        {
            setMaxSpeed(recording.maxSpeed);
            setAverageSpeed(recording.avgSpeed);
            setElapsedTime(recording.elapsedTime);
            setTotalDistance(recording.totalDistance);
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
}
