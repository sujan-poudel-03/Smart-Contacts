package com.example.smartcontacts.model;

public class Contact {
    private int id;
    private String firstName;
    private String lastName;
    private String company;
    private String phone;
    private String email;

    // Constructor for creating new contacts
    public Contact(String firstName, String lastName, String company, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.phone = phone;
        this.email = email;
    }

    // Constructor for retrieving contacts from the database
    public Contact(int id, String firstName, String lastName, String company, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompany() {
        return company;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        String first = (firstName == null || firstName.isEmpty()) ? "" : firstName;
        String last = (lastName == null || lastName.isEmpty()) ? "" : lastName;
        String fullName = (first + " " + last).trim();
        return fullName.isEmpty() ? "No Name" : fullName;
    }
}
