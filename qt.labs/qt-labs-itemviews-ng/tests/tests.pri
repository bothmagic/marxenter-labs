INCLUDEPATH += $$PWD/../src $$PWD/../src/experimental
DEPENDPATH  += $$INCLUDEPATH .
LIBS += -L../../lib -litemviews-ng
QMAKE_RPATHDIR = $$OUT_PWD/../../lib
CONFIG += qtestlib
CONFIG -= app_bundle

test.files =
test.path = .
test.depends = all
!isEmpty(DESTDIR): test.commands = cd ./$(DESTDIR) &&
macx:      test.commands += ./$(QMAKE_TARGET).app/Contents/MacOS/$(QMAKE_TARGET)
else:unix: test.commands += ./$(QMAKE_TARGET)
else:      test.commands += $(QMAKE_TARGET)
embedded:  test.commands += -qws
INSTALLS += test

QMAKE_EXTRA_TARGETS += test
