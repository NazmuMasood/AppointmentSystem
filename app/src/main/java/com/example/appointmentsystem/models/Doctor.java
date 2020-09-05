package com.example.appointmentsystem.models;

public class Doctor {
    public String name, serviceType, designation, agencyName, address,
            //gpsAddress,
            appointmentPhone, uId;

    public Doctor(String name, String serviceType, String designation, String agencyName,
                  String address,
                  //String gpsAddress,
                  String appointmentPhone,
                  String uId) {
        this.name = name;
        this.serviceType = serviceType;
        this.designation = designation;
        this.agencyName = agencyName;
        this.address = address;
        //this.gpsAddress = gpsAddress;
        this.appointmentPhone = appointmentPhone;
        this.uId = uId;
    }
}
