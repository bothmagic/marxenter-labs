package org.jdesktop.jdnc.incubator.vprise.forms;

/**
 * This is a simple java bean that is copied from my server side.
 * In the server it is anotated as a J2EE 5 entity bean
 *
 * @author Shai Almog
 */
public class Address implements java.io.Serializable {
    private Long id;
    private String firstName;
    private String surname;
    private String addressLine;
    private String street;
    private String buildingNumber;
    private String city;
    private String zipcode;
    private String state;
    private String country;    
    
    public Address() {
    }    

    public Address(long id, String firstName, String surname, String addressLine,  
            String street, String buildingNumber, String city, String zipcode, String state,
            String country) {
        this.id = new Long(id);
        this.firstName = firstName;
        this.surname = surname;
        this.addressLine = addressLine;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.country = country;
    }    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
