TEMPLATE = subdirs
CONFIG += ordered
SUBDIRS = src \
          #tests \
          examples \

# installation paths
libraries.path = $$[QT_INSTALL_LIBS]
libraries.files = $$OUT_PWD/lib/*
# tests.path =
# tests.files = $$OUT_PWD/tests/*
# examples.path =
# examples.files = $$OUT_PWD/examples/*

INSTALLS = libraries \
#           tests \
#           examples \

