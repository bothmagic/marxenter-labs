package org.jdesktop.swingx.table;

import javax.swing.table.DefaultTableModel;

/**
 * @author Bryan Young
*/
public class BigTableModel extends DefaultTableModel {

    private static String[] FIRST_NAMES = new String[] {"Bob", "Steve", "John", "Bryan", "Chad", "Fred", "Jerry", "Jason"};
    private static String[] LAST_NAMES = new String[] {"Young", "Smith", "Jones", "McNail", "Henderson"};
    private static String[] STREET_NAMES = new String[] {"Big Bend", "Mesa Verde", "Manchester", "Sulfur Springs", "Clayton"};
    private static String[] CITY_NAMES = new String[] {"St Louis", "Manchester", "Ballwin", "Ellisville"};

    public BigTableModel() {
        super(new String[] {"First", "Last", "Address", "City", "State"}, 0);
        for (int i = 0; i < 50000; i++) {
            String firstName = FIRST_NAMES[getRandom(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[getRandom(LAST_NAMES.length)];
            String streetName = getRandom(1000) + " " + STREET_NAMES[getRandom(STREET_NAMES.length)];
            String cityName = CITY_NAMES[getRandom(CITY_NAMES.length)];
            addRow(new String[] {firstName, lastName, streetName, cityName, "MO"});
        }
    }

    private static int getRandom(int max) {
        return (int)(Math.random() * max);
    }
}
