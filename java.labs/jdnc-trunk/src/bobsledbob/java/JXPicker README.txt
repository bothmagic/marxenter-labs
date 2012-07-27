
JXPicker Demo
=============

Adam Taft bobsledbob@dev.java.net


1)  What is JXPicker?

JXPicker is a generic combobox like Swing component.  JXPicker is meant to be a generic
alternative to JComboBox.

A picker or combobox widget can be defined as a component with a trigger or action button
that upon being pressed, a popup opens to allow the user a choice from the popup.  Once
the choice has been made, the widget will then typically display the choice to the user in
a defined way and make the popup disappear.

JXPicker defers the rendering of its selected component to a PickerRenderer class allowing
easy handling of mixed data types.  The default renderer (DefaultPickerRenderer) uses a
JLabel to display the selected item.


2)  What is JXPickerDemo?

The JXPickerDemo is a simple form to show some potential use cases for the JXPicker component.
The demo displays various uses of the JXPicker component.

The first component in the demo is a standard JComboBox.  This is show to give a reference a
comparison to how the JXPicker looks and behaves.

The next component is a JXPicker with a JList as the popup.  This is a functionally equivalent
version of JComboBox (though not quite as perty).  A choice is given to the user based on
the contents of an already defined JList.

The next component is a JXPicker with an embedded JTree as the popup.  This shows functionality
which can not be easily achieved with the standard JComboBox.

The next component is a JXDatePicker, a standard component from the Swing Labs' SwingX project.

The next component shows a functional equivalenet using JXPicker.

Finally, the last component shows how a JXPicker can be used as a TableCellEditor inside a
standard JTable component.  This is an important use case for the component as well.


3)  How do I run the demo?

Included in this zip file are the required libraries to run the demo.  Launch the demo by placing
all the jars in the classpath and running the demo.JXPickerDemo class.  Something like:

java -cp jxpicker-demo.jar;lib/forms-1.0.6.jar;lib/jgoodies-uif-lite.jar;lib/looks-2.0.1.jar;lib/swingx.jar demo.JXPickerDemo


4)  How do I use JXPicker in my code?

Simply create a new JXPicker instance.  You'll probably want to provide the popup component in the constructor.
So, for example, say you have a JTree in a JScrollPane which you want to use as the JXPicker popup component.
The code would look something like this:

JTree tree = new JTree();
JScrollPane scrollPane = new JScrollPane(tree);
JXPicker picker = new JXPicker(scrollPane);

This will allow the picker to display the scrollPane (with the tree inside) when the trigger button is pressed.

Note, you will need to provide some sort of listener to the tree when the user has selected the correct node.
The listener should call the JXPicker's setSelectedItem method and then call the togglePopup() method on the
JXPicker.


5)  What are the terms or licensing of JXPicker?

As of this writing (2006-07-13), the code is being housed in the Swing Labs incubator, located at
https://jdnc-incubator.dev.java.net/  The code is under joint copyright between the author and
Sun Microsystems.  The code is licensed freely under the terms of the LGPL.  A copy of this license
agreement can be found at http://www.gnu.org/licenses/lgpl.txt


6)  What is the long term vision of JXPicker?  Will JXPicker replace JComboBox?

Ultimately, the author believes that the generic nature of the JXPicker component will lend itself to
being used and extended in many other various picker components.  The JXPicker should be the base class
which all other picker classes use underneath.

Of course, JComboBox will never go away.  It is the hope though that other components can build from a
more common platform when creating their picker classes.  JComboBox isn't necessarily the best candidate
for many types of picker / combobox components.

It's really the hope of the author that a JComponent guru will contribute to this work and really make the
JXPicker component "all that" since it's likely beyond the author's current coding ability to do so himself.  :)  


