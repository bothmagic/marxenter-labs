TEMPLATE = subdirs
SUBDIRS = \
          listWidget \
          listModel \
          tableWidget \
          tableModel \
          treeWidget \
          treeModel \
          treeAdaptor \
          gridView \
          pathView \
          nonUniformList \
          # iconList \
          # spreadsheet \

# !contains(DEFINES, QT_NO_PRINTER) SUBDIRS += printing

exists($$QMAKE_INCDIR_QT/QtCore/qeasingcurve.h) {
    SUBDIRS += \
            chat
}

exists($$QMAKE_INCDIR_QT/QtCore/qpropertyanimation.h) {
    SUBDIRS += \
            photoAlbum
}
