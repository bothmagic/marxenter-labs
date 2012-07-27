package org.jdesktop.swingx.tree;


/**
 * The userObject of the node. 
 * Top level class.
 */
public class Person implements Comparable<Person> {
    private String firstName = "";

    private String middleName = "";

    private String lastName = "";

    private String phoneNbr = "";

    public Person() {
    }

    public Person(String firstName, String middleName, String lastName,
            String phoneNbr) {
        super();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNbr = phoneNbr;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    @Override
    public String toString() {
        return firstName + " " + middleName + ". " + lastName + "\t"
                + phoneNbr;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + ((firstName == null) ? 0 : firstName.hashCode());
        result = PRIME * result
                + ((middleName == null) ? 0 : middleName.hashCode());
        result = PRIME * result
                + ((lastName == null) ? 0 : lastName.hashCode());
        result = PRIME * result
                + ((phoneNbr == null) ? 0 : phoneNbr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Person other = (Person) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (middleName == null) {
            if (other.middleName != null)
                return false;
        } else if (!middleName.equals(other.middleName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (phoneNbr == null) {
            if (other.phoneNbr != null)
                return false;
        } else if (!phoneNbr.equals(other.phoneNbr))
            return false;
        return true;
    }

    public int compareTo(Person otherObject) {
        int res = 0;
        res = otherObject.getLastName().compareTo(getLastName());
        if (0 == res) {
            res = otherObject.getFirstName().compareTo(getFirstName());
        }
        if (0 == res) {
            res = otherObject.getMiddleName().compareTo(getMiddleName());
        }
        return res;
    }
}