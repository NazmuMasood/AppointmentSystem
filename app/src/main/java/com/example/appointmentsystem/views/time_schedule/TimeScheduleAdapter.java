package com.example.appointmentsystem.views.time_schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentsystem.R;
import com.example.appointmentsystem.models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeScheduleAdapter extends
        RecyclerView.Adapter<TimeScheduleAdapter.ViewHolder> {

    private Context context;
    private String day;
    private ArrayList<TimeSlot> timeSlots;
    private OnDeleteItemClickListener onDeleteItemClickListener;

    public TimeScheduleAdapter(Context context, String day, ArrayList<TimeSlot> timeSlots, OnDeleteItemClickListener onDeleteItemClickListener){
        this.context = context;
        this.day = day;
        this.timeSlots = timeSlots;
        this.onDeleteItemClickListener = onDeleteItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View timeSlotView = inflater.inflate(R.layout.single_time_slot, parent, false);

        // Return a new holder instance
        return new ViewHolder(timeSlotView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // Get the data model based on position
        final TimeSlot timeSlot = timeSlots.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.timeSlotTV;
        textView.setText(timeSlot.getStartTime()+" - "+timeSlot.getEndTime());
        ImageButton button = holder.timeSlotRemoveButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Clicked on "+day+" : "+timeSlot.getStartTime(),
//                        Toast.LENGTH_LONG).show();
                onDeleteItemClickListener.onDeleteItemClick(day, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeSlotTV;
        public ImageButton timeSlotRemoveButton;

        public ViewHolder(View itemView) {
            super(itemView);

            timeSlotTV = itemView.findViewById(R.id.time_slot_tv);
            timeSlotRemoveButton = itemView.findViewById(R.id.time_slot_remove_button);
        }
    }
}
