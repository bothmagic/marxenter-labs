package org.jdesktop.jdnc.incubator.vprise.forms;

import java.beans.*;

public class AddressBeanInfo extends SimpleBeanInfo {
    private PropertyDescriptor[] descriptors;
    
    /** Creates a new instance of AddressBeanInfo */
    public AddressBeanInfo() {
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            if(descriptors == null) {
                Class address = Address.class;
                descriptors = new PropertyDescriptor[]{
                    new PropertyDescriptor("Id", address, "getId", null),
                    new PropertyDescriptor("First Name", address, "getFirstName", "setFirstName"),
                    new PropertyDescriptor("Surname", address, "getSurname", "setSurname"),
                    new PropertyDescriptor("Address Info", address, "getAddressLine", "setAddressLine"),
                    new PropertyDescriptor("Street", address, "getStreet", "setStreet"),
                    new PropertyDescriptor("Building", address, "getBuildingNumber", "setBuildingNumber"),
                    new PropertyDescriptor("City", address, "getCity", "setCity"),
                    new PropertyDescriptor("Zip Code", address, "getZipcode", "setZipcode"),
                    new PropertyDescriptor("State", address, "getState", "setState"),
                    new PropertyDescriptor("Country", address, "getCountry", "setCountry")
                };
            }
        } catch(Exception err) {
            err.printStackTrace();
        }
        return descriptors;
    }
}
