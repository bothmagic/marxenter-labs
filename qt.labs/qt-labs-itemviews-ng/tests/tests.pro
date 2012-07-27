TEMPLATE = subdirs

SUBDIRS += \
    qgraphicsheader \
    qgraphicslistview \
    qgraphicstableview \
    qgraphicstreeview \
#    qgraphicsgridview \
#    qgraphicsflowview \
#    qgraphicspathview \
    qsectionspans \
    qlistcontroller \
    qlistdefaultmodel \
    qlistmodeladaptor \
    qlistfromtreeadaptor \
    qlistselectionmanager \
    qtablecontroller \
    qtabledefaultmodel \
    qtablemodeladaptor \
    qtableselectionmanager \
    qtableselectionrange \
    qtreecontroller \
    qtreedefaultmodel \
    qtreemodeladaptor \
    qtreeselectionmanager

test.CONFIG = recursive
test.recurse = $$SUBDIRS
test.depends = all
test.recurse_target = test

QMAKE_EXTRA_TARGETS += test
