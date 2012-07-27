package org.jdesktop.incubator.list;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 29-Jul-2008
 * Time: 18:13:18
 */

import junit.framework.TestCase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

public class GenericListModelTest extends TestCase {

    GenericListModel<Locale> createLocaleModel() {
        return new GenericListModel<Locale>(Locale.getAvailableLocales());
    }

    GenericListModel<String> createStringModel() {
        return new GenericListModel<String>("rod", "jane", "freddy");
    }

    GenericListModel<String> createStringModel(Collection<String> strings) {
        return new GenericListModel<String>(strings);
    }

    public void testCollections() {
        GenericListModel<String> stringListModel = createStringModel();
        assertEquals(3, stringListModel.getSize());

        Collection<String> strings = stringListModel.getValues();
        assertEquals(3, strings.size());

        GenericListModel<String> stringListModel2 = createStringModel(strings);
        assertEquals(3, strings.size());
        assertEquals(strings, stringListModel2.getValues());
        assertNotSame(strings, stringListModel2.getValues());
        Collection<String> strings2 = new LinkedList<String>(stringListModel2.getValues());
        stringListModel2.setValues(strings2);
        assertEquals(strings, stringListModel2.getValues());
        assertNotSame(strings2, stringListModel2.getValues());
        strings2.add("bungle");
        assertEquals(3, stringListModel.getSize());
    }

    public void testArrays() {
        GenericListModel<Locale> localesListModel = createLocaleModel();
        assertEquals(Locale.getAvailableLocales().length, localesListModel.getSize());
        assertEquals(Locale.getAvailableLocales()[5], localesListModel.getElementAt(5));
        assertTrue(localesListModel.contains(Locale.FRENCH));
        assertTrue(localesListModel.indexOf(Locale.FRENCH) > -1);
    }
}