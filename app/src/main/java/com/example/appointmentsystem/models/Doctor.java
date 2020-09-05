package com.example.appointmentsystem.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({"name", "serviceType", "designation", "agencyName", "address", "appointmentPhone","uId"})
@IgnoreExtraProperties
public class Doctor {
    public String name, serviceType, designation, agencyName, address,
            appointmentPhone, uId;

    public Doctor(){
        // Default constructor required for calls to DataSnapshot.getValue(Doctor.class)
    }

    public Doctor(String name, String serviceType, String designation, String agencyName,
                  String address,
                  String appointmentPhone,
                  String uId) {
        this.name = name;
        this.serviceType = serviceType;
        this.designation = designation;
        this.agencyName = agencyName;
        this.address = address;
        this.appointmentPhone = appointmentPhone;
        this.uId = uId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("serviceType", serviceType);
        result.put("designation", designation);
        result.put("agencyName", agencyName);
        result.put("address", address);
        result.put("appointmentPhone", appointmentPhone);
        result.put("uId", uId);

        return result;
    }
}
