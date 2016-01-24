package com.example.didrik.compulsory1v3;

/**
 * The person class represents the name of the person and an URI
 * string to an image located on the device.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class Person {

    private String name;
    private String uriString;

    /**
     * Constructor assigning values to the field variables
     * @param name Name of person
     * @param uriString URI string of where the image is located on the device
     */
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

    /**
     * A default ArrayAdapter implementation will retrieve the toString
     * representation of an object. In our case, we want toString to get the name,
     * saving us some lines of code by avoiding a custom implementation of the adapter
     * when rendering the ListView in NamesActivity.
     * @return Name of the person.
     */
    @Override
    public String toString() {
        return name;
    }
}
