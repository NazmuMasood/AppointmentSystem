package com.example.appointmentsystem.models;

import java.util.ArrayList;

public class TimeSlot {

    private String startTime;
    private String endTime;

    public TimeSlot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public static ArrayList<TimeSlot> createTimeSlotList(int numTimeSlots) {
        ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();

        for (int i = 1; i <= numTimeSlots; i++) {
            if (i==1) {
                timeSlots.add(new TimeSlot("09.00 AM", "02.00 PM"));
            }
            else if (i==2) { timeSlots.add(new TimeSlot("07.00 PM", "10.00 PM")); }
            else { timeSlots.add(new TimeSlot("10.30 PM", "11.00 PM")); }
        }

        return timeSlots;
    }
}
