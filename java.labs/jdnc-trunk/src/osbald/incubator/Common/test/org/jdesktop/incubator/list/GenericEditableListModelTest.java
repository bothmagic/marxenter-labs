package org.jdesktop.incubator.list;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 29-Jul-2008
 * Time: 10:59:39
 */

import junit.framework.TestCase;

import java.util.Collection;
import java.util.Locale;
import java.util.Arrays;

public class GenericEditableListModelTest extends GenericListModelTest {

    GenericListModel<Locale> createLocaleModel() {
        return new GenericEditableListModel<Locale>(Locale.getAvailableLocales());
    }

    GenericListModel<String> createStringModel() {
        return new GenericEditableListModel<String>("rod", "jane", "freddy");
    }

    GenericListModel<String> createStringModel(Collection<String> strings) {
        return new GenericEditableListModel<String>(strings);
    }

    public void testRemovals() {
        GenericEditableListModel<String> listModel =
                new GenericEditableListModel<String>("one", "two", "three", "four", "five");
        assertEquals(5, listModel.getSize());
        assertTrue(listModel.contains("four"));
        listModel.remove("four");
        assertEquals(4, listModel.getSize());
        assertFalse(listModel.contains("four"));

        listModel.removeAll(new String[] {"seven","eight","nine"});
        assertEquals(4, listModel.getSize());

        assertTrue(listModel.contains("one"));
        assertTrue(listModel.contains("two"));
        listModel.removeAll(Arrays.asList("one", "two"));
        assertEquals(2, listModel.getSize());
        assertFalse(listModel.contains("one"));
        assertFalse(listModel.contains("two"));        
    }

    public void testRemovalIntervals() {
        GenericEditableListModel<String> listModel =
                new GenericEditableListModel<String>("one", "two", "three", "four", "five");
        assertEquals(5, listModel.getSize());
        listModel.removeAll("four", "three", "one");
        assertEquals(2, listModel.getSize());

        listModel = new GenericEditableListModel<String>("one", "two", "three", "four", "five");
        listModel.removeAll("five", "one");
        assertEquals(3, listModel.getSize());

        listModel = new GenericEditableListModel<String>("one", "two", "three", "four", "five");
        listModel.removeAll("one", "two", "three");
        assertEquals(2, listModel.getSize());

        listModel = new GenericEditableListModel<String>("one", "two", "three", "four", "five");
        listModel.removeAll("four", "two");
        assertEquals(3, listModel.getSize());
    }
}