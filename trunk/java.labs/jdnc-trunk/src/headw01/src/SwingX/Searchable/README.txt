This source directory contains an experimental "SearchAssistant" enhancement
to allow a "generic" way of finding a String representation of a value
in a JXList, JXTable, JXTree, or any object that has the Searchable capability.

directory descriptions:
src/java: The SwingX classes that are changed/added to support this.
          The classes in this directory should be copied over a
          checkout of the SwingX classes from the repository.

src/test/org: The SwingX test classes that needed to be changed because
              of the changes to the src/java classes.

src/test/search: My own test classes... Didn't make JUnit tests yet, so I
                 didn't put them in src/test/org.


file descriptions:
.classpath: Eclipse project file
.project  : Eclipse project file
