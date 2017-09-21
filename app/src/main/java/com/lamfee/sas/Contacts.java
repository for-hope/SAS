package com.lamfee.sas;


class Contacts {
private String contactName;
private String contactNumber;
private int id;

    Contacts(){}

    Contacts(String name, String number) {
        super();
        this.contactName = name;
        this.contactNumber = number;
    }


    String getContactNumber() {
        return contactNumber;
    }

    void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getContactName() {

        return contactName;
    }

    void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", id=" + id +
                '}';
    }
}
