package com.bloodbank.models;

import javafx.beans.property.*;

public class Donor {


    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty bloodGroup;
    private final StringProperty location;
    private final StringProperty phone;

    public Donor(int id, String name, String bloodGroup, String location, String phone) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
        this.location = new SimpleStringProperty(location);
        this.phone = new SimpleStringProperty(phone);
    }





    public int getId() {return id.get();}

    public String getName() {
        return name.get();
    }

    public String getBloodGroup() {
        return bloodGroup.get();
    }

    public String getLocation() {
        return location.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    @Override
    public String toString() {
        return String.format("Donor{id=%d, name='%s', bloodGroup='%s', location='%s', phone='%s'}",
                getId(), getName(), getBloodGroup(), getLocation(), getPhone());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Donor donor = (Donor) obj;
        return getId() == donor.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }
}