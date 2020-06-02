package com.example.appointmentsystem;

import java.util.Date;

class Appointment {
    String patientName;
    String patientPhone;
    Date date;
    Date startTime;
    Date endTime;
    int tokenNo;
    String reason;

    public Appointment(String patientName, String patientPhone, Date date, Date startTime, Date endTime, int tokenNo, String reason) {
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tokenNo = tokenNo;
        this.reason = reason;
    }
}
