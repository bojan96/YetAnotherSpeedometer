package org.unibl.etf.yetanotherspeedometer.recordings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.yetanotherspeedometer.R;
import org.unibl.etf.yetanotherspeedometer.databinding.RecordingItemBinding;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public ListAdapter()
    {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final RecordingItemBinding recordingItemBinding;

        public ViewHolder(RecordingItemBinding recordingItemBinding) {
            super(recordingItemBinding.getRoot());
            this.recordingItemBinding = recordingItemBinding;
        }

        public void setAverageSpeed(double averageSpeed)
        {
            recordingItemBinding.recordingItemAverageSpeed.setText(String.format("%.2f km/h", averageSpeed));
        }

        public void setMaxSpeed(double maxSpeed)
        {
            recordingItemBinding.recordingItemMaxSpeed.setText(String.format("%.2f km/h", maxSpeed));
        }

        public void setTotalDistance(long distance)
        {
            recordingItemBinding.recordingItemDistance.setText(String.format("%d m", distance));
        }

        public void setElapsedTime(long elapsedTime)
        {
            recordingItemBinding.recordingItemElapsedTime.setText(String.format("%d s", elapsedTime));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = RecordingItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
