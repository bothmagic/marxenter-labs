
Swing Labs Incubator 
====================

Document History
------------
Original by Mark Davidson (~ Sept/2004)
Updated 2006-07-13 by Adam Taft (bobsledbob) ** Updating directory structure information and overall project contribution guidelines


Welcome to the Swing Labs Incubator!

(note: for historical reasons, this project is also referred to as the JDNC Incubator project)

More information on joining the incubator can be found at:  https://jdnc-incubator.dev.java.net/


Contributing
------------
In order to contribute to the Swing Labs Incubator, you must have a signed JCA (Joint Copyright Agreement) on file
with Sun Microsystems.  Please visit the above link to download the JCA in pdf format.

Once you have a signed JCA on file with Sun, you will be given developer access to the incubator project.  You should
access and contribute code using CVS.


CVS Connection Info
------------
Using your CVS client, connect to the java.net CVS server with the following CVS connection information:

CVS connection type: pserver
Username: {your dev.java.net username}
Password: {your dev.java.net passsword}
Host: cvs.dev.java.net
Repository Path: /cvs
Module: jdnc-incubator
Tag: HEAD

Using a command line CVS client, the connection procedure might look something like:

cvs -d :pserver:{your dev.java.net username}@cvs.dev.java.net:/cvs login

cvs -d :pserver:{your dev.java.net username}@cvs.dev.java.net:/cvs checkout jdnc-incubator



Directory Structure
------------
The incubator high level directory structure uses the following conventions:

/src - Source files organized by java.net username
/lib - Common shared libraries and developer specific libraries
/www - The document root for the incubator website

Source code should be put into the /src/ directory under your java.net username, followed by
appropriate subdirectories: java, test, demo, or others as needed.

As an example, for the dev.java.net user "rbair":

/src/rbair - Base contribution directory
/src/rbair/java - Java source code
/src/rbair/test - Test code, to test your contributed code
/src/rbair/demo - Demo code, to show off your code contributions

You will likely want to create the package of your source code to match/mimick that of the main
Swing Labs projects.  For example, if you are developing a contribution for the SwingX project, your
directory structure might look something like:

/src/rbair/java/org/jdesktop/swingx/SomeNewSwingXWidget.java

Of course, SomeNewSwingXWidget would be placed in the org.jdesktop.swingx package.

This will ease the migration of accepted incubator contributions into the mainline tree of the
appropriate Swing Labs project.


Similar to the /src directory structure, the /lib directory is also organized by dev.java.net username.
Please add any external or required libraries for your contribution in the /lib directory.

For example, if SomeNewSwingXWidget.java relies on another jar library, the following would be
appropriate:

/lib/rbair/some-dependency-lib.jar

Note that common jar files are being kept in /lib/share.  While this directory should contain
a recent build of the shared libraries, there is no guarantee the jars in /lib/share will be the most
up-to-date.  If you contribution relies on a specific library, it's likely best to include the library
in your own /lib/{java.net username} directory.



Building the Incubator
------------
[note:  The information below is likely outdated.  It's unknown by me as to whether building
the incubator is even needed or required, or if building should be left to the individual contributors.
The information below is included because it was in this document originally. -AT ]


Requirements
------------
- Ant 1.6.0 or greater http://ant.apache.org
- JUnit 3.8.1 or greater http://www.junit.org

Note: The junit jar should be installed in the $ANT_HOME/lib directory.


Edit the build.properties file
-----------
Edit the values in the jdnc/make/build.properties to reflect your
environment. 

The property values are documented in build.properties.


Any resources like text files, test data, graphics or icons should be placed
in a sub-directory or the developers' package called "resources".
All files in the resources directory will be copied to thier respective
classpaths as part of the build process.

Build Targets
-----------
Execute "ant -p" to get a listing of the public build targets. 

The "debug" target will only work in the NetBeans 4.0 IDE. Please read the comment
about setting up NB 4.0 in the build-nb-impl.xml file.
