package com.example.researchrepo.model;

public enum Department {
    AIML(0),
    CSE(1),
    ISE(2),
    EC(3),
    MECH(4);

    private final int id;

    Department(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Department fromId(int id) {
        for (Department dept : Department.values()) {
            if (dept.id == id) {
                return dept;
            }
        }
        throw new IllegalArgumentException("Invalid department ID: " + id);
    }

    public static Department fromName(String name) {
        try {
            return Department.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid department name: " + name);
        }
    }
}