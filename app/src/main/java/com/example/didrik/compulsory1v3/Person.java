package com.example.didrik.compulsory1v3;

/**
 * Created by didrik on 24.01.16.
 */

public class Person {

    private String name;
    private String uriString;

    public Person(String name, String uriString) {
        this.name = name;
        this.uriString = uriString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUriString() { return uriString; }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    @Override
    public String toString() {
        return name;
    }
}
