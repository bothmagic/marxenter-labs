/****************************************************************************
**
** Copyright (C) 2008-2009 Nokia Corporation and/or its subsidiary(-ies).
** Contact: Qt Software Information (qt-info@nokia.com)
**
** This file is part of the Itemviews NG project on Trolltech Labs.
**
** This file may be used under the terms of the GNU General Public
** License version 2.0 or 3.0 as published by the Free Software Foundation
** and appearing in the file LICENSE.GPL included in the packaging of
** this file.  Please review the following information to ensure GNU
** General Public Licensing requirements will be met:
** http://www.fsf.org/licensing/licenses/info/GPLv2.html and
** http://www.gnu.org/copyleft/gpl.html.
**
** If you are unsure which license is appropriate for your use, please
** contact the sales department at qt-sales@nokia.com.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
****************************************************************************/

#include <QtTest/QtTest>
#include <qlistselectionmanager.h>
#include <qlistselectionmanager_p.h>
#include <qlistdefaultmodel.h>

class ListSelectionManager : public QtListSelectionManager
{
public:
    inline void setModel(QtListModelInterface *model)
    {
        QtListSelectionManager::setModel(model);
    }
};

class tst_QtListSelectionManager : public QObject
{
    Q_OBJECT

public:
    enum SelectionMode {
        Select = QtListSelectionManager::Select,
        Deselect = QtListSelectionManager::Deselect,
        Toggle = QtListSelectionManager::Toggle
    };

    tst_QtListSelectionManager();
    virtual ~tst_QtListSelectionManager();

public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void currentItem_data();
    void currentItem();
    void anchorItem_data();
    void anchorItem();
    void selections_data();
    void selections();
    void anchoredSelections_data();
    void anchoredSelections();

private:
    ListSelectionManager *manager;
    QtListDefaultModel *model;
};

Q_DECLARE_METATYPE(QList<int>)
Q_DECLARE_METATYPE(QList<bool>)

tst_QtListSelectionManager::tst_QtListSelectionManager()
    : manager(0), model(0)
{
    qRegisterMetaType<QtListSelectionChange>("QtListSelectionChange");
}

tst_QtListSelectionManager::~tst_QtListSelectionManager()
{
}

void tst_QtListSelectionManager::initTestCase()
{
}

void tst_QtListSelectionManager::cleanupTestCase()
{
}

void tst_QtListSelectionManager::init()
{
    manager = new ListSelectionManager();
    model = new QtListDefaultModel();
    manager->setModel(model);
}

void tst_QtListSelectionManager::cleanup()
{
    delete manager;
    delete model;
}

void tst_QtListSelectionManager::getSetCheck()
{
}

/*
  Note that current item and anchor item should behave the same.
  So if you modify one test, make sure you also modify the other.
 */
void tst_QtListSelectionManager::currentItem_data()
{
    QTest::addColumn<bool>("useModel");
    QTest::addColumn<int>("itemCount");
    QTest::addColumn<int>("initialCurrent");
    QTest::addColumn<int>("selectedCount");
    QTest::addColumn<int>("newCurrent");
    QTest::addColumn<int>("expectedCurrent");
    QTest::addColumn<int>("insertAt");
    QTest::addColumn<int>("insertCount");
    QTest::addColumn<int>("removeAt");
    QTest::addColumn<int>("removeCount");
    QTest::addColumn<int>("moveFrom");
    QTest::addColumn<int>("moveTo");
    QTest::addColumn<int>("moveCount");
    QTest::addColumn<int>("changedCurrent");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no model")
            << false << 0  // model
            << -1 << 0     // initial
            << 0 << -1     // set/get
            << 0 << 0      // remove
            << 0 << 0      // insert
            << 0 << 0 << 0 // move
            << -1 << 0;    // changed/signals

    QTest::newRow("no data")
            << true << 0
            << -1 << 0
            << 0 << -1
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << -1 << 0;

    QTest::newRow("before first")
            << true << 10
            << 0 << 0
            << -1 << 0
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << 0 << 0;

    QTest::newRow("valid")
            << true << 10
            << 0 << 0
            << 5 << 5
            << 0 << 0
            << 0 << 0 << 0
            << 0 << 0
            << 5 << 1;

    QTest::newRow("after last")
            << true << 10
            << 0 << 0
            << INT_MAX << 9
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << 9 << 1;
}

void tst_QtListSelectionManager::currentItem()
{
    QFETCH(bool, useModel);
    QFETCH(int, itemCount);
    QFETCH(int, initialCurrent);
    QFETCH(int, selectedCount);
    QFETCH(int, newCurrent);
    QFETCH(int, expectedCurrent);
    QFETCH(int, insertAt);
    QFETCH(int, insertCount);
    QFETCH(int, removeAt);
    QFETCH(int, removeCount);
    QFETCH(int, moveFrom);
    QFETCH(int, moveTo);
    QFETCH(int, moveCount);
    QFETCH(int, changedCurrent);
    QFETCH(int, signalCount);

    QSignalSpy currentChanged(manager, SIGNAL(currentChanged(int,int)));

    // start without model
    manager->setModel(0);

    // setup the model item count
    for (int i = 0 ; i < itemCount; ++i)
        model->appendItem(new QtListDefaultItem(QString("Item %1").arg(i)));

    // with or without data model
    manager->setModel(useModel ? model : 0);
    QCOMPARE(manager->model(), (useModel ? model : 0));

    // check initial current item and selection
    QCOMPARE(manager->currentItem(), initialCurrent);
    QCOMPARE(manager->selectedItems().count(), selectedCount);

    // set current item
    manager->setCurrentItem(newCurrent);
    QCOMPARE(manager->currentItem(), expectedCurrent);
    QCOMPARE(manager->selectedItems().count(), selectedCount);

    // insert items
    for (int j = insertAt; j < insertCount; ++j)
        model->insertItem(j, new QtListDefaultItem(QString("Item %1").arg(j)));

    // remove items
    for (int k = removeAt; k < removeCount; ++k)
        model->removeItem(k);

    // move items
    for (int l = 0; l < moveCount; ++l)
        model->moveItem(moveFrom, moveTo + l);

    // check again
    QCOMPARE(manager->currentItem(), changedCurrent);

    // check signals
    QCOMPARE(currentChanged.count(), signalCount);
}

/*
  Note that current item and anchor item should behave the same.
  So if you modify one test, make sure you also modify the other.
 */
void tst_QtListSelectionManager::anchorItem_data()
{
    QTest::addColumn<bool>("useModel");
    QTest::addColumn<int>("itemCount");
    QTest::addColumn<int>("initialAnchor");
    QTest::addColumn<int>("selectedCount");
    QTest::addColumn<int>("newAnchor");
    QTest::addColumn<int>("expectedAnchor");
    QTest::addColumn<int>("insertAt");
    QTest::addColumn<int>("insertCount");
    QTest::addColumn<int>("removeAt");
    QTest::addColumn<int>("removeCount");
    QTest::addColumn<int>("moveFrom");
    QTest::addColumn<int>("moveTo");
    QTest::addColumn<int>("moveCount");
    QTest::addColumn<int>("changedAnchor");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no model")
            << false << 0  // model
            << -1 << 0     // initial
            << 0 << -1     // set/get
            << 0 << 0      // remove
            << 0 << 0      // insert
            << 0 << 0 << 0 // move
            << -1 << 0;    // changed/signals

    QTest::newRow("no data")
            << true << 0
            << -1 << 0
            << 0 << -1
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << -1 << 0;

    QTest::newRow("before first")
            << true << 10
            << 0 << 0
            << -1 << 0
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << 0 << 0;

    QTest::newRow("valid")
            << true << 10
            << 0 << 0
            << 5 << 5
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << 5 << 1;

    QTest::newRow("after last")
            << true << 10
            << 0 << 0
            << INT_MAX << 9
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << 9 << 1;
}

void tst_QtListSelectionManager::anchorItem()
{
    QFETCH(bool, useModel);
    QFETCH(int, itemCount);
    QFETCH(int, initialAnchor);
    QFETCH(int, selectedCount);
    QFETCH(int, newAnchor);
    QFETCH(int, expectedAnchor);
    QFETCH(int, insertAt);
    QFETCH(int, insertCount);
    QFETCH(int, removeAt);
    QFETCH(int, removeCount);
    QFETCH(int, moveFrom);
    QFETCH(int, moveTo);
    QFETCH(int, moveCount);
    QFETCH(int, changedAnchor);
    QFETCH(int, signalCount);

    QSignalSpy anchorChanged(manager, SIGNAL(anchorChanged(int,int)));

    // start without model
    manager->setModel(0);

    // setup the model item count
    for (int i = 0 ; i < itemCount; ++i)
        model->appendItem(new QtListDefaultItem(QString("Item %1").arg(i)));

    // with or without data model
    manager->setModel(useModel ? model : 0);
    QCOMPARE(manager->model(), (useModel ? model : 0));

    // check initial anchor item and selection
    QCOMPARE(manager->anchorItem(), initialAnchor);
    QCOMPARE(manager->selectedItems().count(), selectedCount);

    // set anchor item
    manager->setAnchorItem(newAnchor);
    QCOMPARE(manager->anchorItem(), expectedAnchor);
    QCOMPARE(manager->selectedItems().count(), selectedCount);

    // insert items
    for (int j = insertAt; j < insertCount; ++j)
        model->insertItem(j, new QtListDefaultItem(QString("Item %1").arg(j)));

    // remove items
    for (int k = removeAt; k < removeCount; ++k)
        model->removeItem(k);

    // move items
    for (int l = 0; l < moveCount; ++l)
        model->moveItem(moveFrom, moveTo + l);

    // check again
    QCOMPARE(manager->anchorItem(), changedAnchor);

    // check signals
    QCOMPARE(anchorChanged.count(), signalCount);
}

/*
  Test the selections independendly of the anchor selection.
 */
void tst_QtListSelectionManager::selections_data()
{
    QTest::addColumn<bool>("useModel");
    QTest::addColumn<int>("itemCount");
    QTest::addColumn<QList<int> >("newSelectionIndexes");
    QTest::addColumn<QList<int> >("newSelectionModes");
    QTest::addColumn<QList<int> >("expectedSelectionIndexes");
    QTest::addColumn<int>("insertAt");
    QTest::addColumn<int>("insertCount");
    QTest::addColumn<int>("removeAt");
    QTest::addColumn<int>("removeCount");
    QTest::addColumn<int>("moveFrom");
    QTest::addColumn<int>("moveTo");
    QTest::addColumn<int>("moveCount");
    QTest::addColumn<QList<int> >("changedSelectionIndexes");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no model")
            << false << 0                                     // model
            << QList<int>() << QList<int>() << QList<int>() // initial
            << 0 << 0                                          // insert
            << 0 << 0                                          // remove
            << 0 << 0 << 0                                     // move
            << QList<int>()                                     // changed
            << 0;                                               // signal

    QTest::newRow("no model, one selection")
            << false << 0
            << (QList<int>() << 3) << (QList<int>() << int(Select)) << QList<int>()
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << QList<int>()
            << 0;

        QTest::newRow("no data")
            << true << 0
            << QList<int>() << QList<int>() << QList<int>()
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << QList<int>()
            << 0;

    QTest::newRow("no data, one selection")
            << true << 0
            << (QList<int>() << 3) << (QList<int>() << int(Select)) << QList<int>()
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << QList<int>()
            << 0;

    QTest::newRow("no selections")
            << true << 10
            << QList<int>() << QList<int>() << QList<int>()
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << QList<int>()
            << 0;

    QTest::newRow("one selection")
            << true << 10
            << (QList<int>() << 3) << (QList<int>() << int(Select)) << (QList<int>() << 3)
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 3)
            << 1;

     QTest::newRow("two selections")
            << true << 10
            << (QList<int>() << 3 << 5) << (QList<int>() << int(Select) << int(Select)) << (QList<int>() << 3 << 5)
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 3 << 5)
            << 2;

      QTest::newRow("one selection, one de-selection")
            << true << 10
            << (QList<int>() << 3 << 3) << (QList<int>() << int(Select) << int(Deselect)) << QList<int>()
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << QList<int>()
            << 2;

      QTest::newRow("two selections, one de-selection")
            << true << 10
            << (QList<int>() << 3 << 5 << 3) << (QList<int>() << int(Select) << int(Select) << int(Deselect)) << (QList<int>() << 5)
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 5)
            << 3;

      QTest::newRow("two out of range selections")
            << true << 10
            << (QList<int>() << -1 << INT_MAX) << (QList<int>() << int(Select) << int(Select)) << QList<int>()
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << QList<int>()
            << 0;

      QTest::newRow("no selections, one insertion")
            << true << 10
            << QList<int>() << QList<int>() << QList<int>()
            << 4 << 1
            << 0 << 0
            << 0 << 0 << 0
            << QList<int>()
            << 0;

      QTest::newRow("one selection, one insertion before")
            << true << 10
            << (QList<int>() << 3) << (QList<int>() << int(Select)) << (QList<int>() << 3)
            << 2 << 1
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 4)
            << 2; // selection and insertion

      QTest::newRow("one selection, one insertion after")
            << true << 10
            << (QList<int>() << 3) << (QList<int>() << int(Select)) << (QList<int>() << 3)
            << 4 << 1
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 3)
            << 1; // selection

      QTest::newRow("one selection, one removal before")
            << true << 10
            << (QList<int>() << 3) << (QList<int>() << int(Select)) << (QList<int>() << 3)
            << 0 << 0
            << 2 << 1
            << 0 << 0 << 0
            << (QList<int>() << 2)
            << 2; // selection and removal

      QTest::newRow("one selection, one removal after")
            << true << 10
            << (QList<int>() << 3) << (QList<int>() << int(Select)) << (QList<int>() << 3)
            << 0 << 0
            << 4 << 1
            << 0 << 0 << 0
            << (QList<int>() << 3)
            << 1; // selection
}

void tst_QtListSelectionManager::selections()
{
    QFETCH(bool, useModel);
    QFETCH(int, itemCount);
    QFETCH(QList<int>, newSelectionIndexes);
    QFETCH(QList<int>, newSelectionModes);
    QFETCH(QList<int>, expectedSelectionIndexes);
    QFETCH(int, insertAt);
    QFETCH(int, insertCount);
    QFETCH(int, removeAt);
    QFETCH(int, removeCount);
    QFETCH(int, moveFrom);
    QFETCH(int, moveTo);
    QFETCH(int, moveCount);
    QFETCH(QList<int>, changedSelectionIndexes);
    QFETCH(int, signalCount);

    QSignalSpy selectionsChanged(manager, SIGNAL(selectionsChanged(const QtListSelectionChange&)));

    // start without model
    manager->setModel(0);

    // setup the model item count
    for (int i = 0 ; i < itemCount; ++i)
        model->appendItem(new QtListDefaultItem(QString("Item %1").arg(i)));

    // with or without data model
    manager->setModel(useModel ? model : 0);
    QCOMPARE(manager->model(), (useModel ? model : 0));

    // set and check selection
    for (int a = 0; a < newSelectionIndexes.count(); ++a)
        manager->setSelected(newSelectionIndexes.at(a), 1, QtListSelectionManager::SelectionMode(newSelectionModes.at(a)));
    QList<int> selectedItems = manager->selectedItems();
    QCOMPARE(selectedItems.count(), expectedSelectionIndexes.count());
    for (int b = 0; b < expectedSelectionIndexes.count(); ++b)
        QCOMPARE(selectedItems.at(b), expectedSelectionIndexes.at(b));

    // insert items
    for (int j = insertAt; j < insertAt + insertCount; ++j)
        model->insertItem(j, new QtListDefaultItem(QString("Item %1").arg(j)));

    // remove items
    for (int k = 0; k < removeCount; ++k)
        model->removeItem(removeAt);

    // move items
    for (int l = 0; l < moveCount; ++l)
        model->moveItem(moveFrom, moveTo + l);

    // check selection again
    selectedItems = manager->selectedItems();
    QCOMPARE(selectedItems.count(), changedSelectionIndexes.count());
    for (int c = 0; c < changedSelectionIndexes.count(); ++c)
        QCOMPARE(selectedItems.at(c), changedSelectionIndexes.at(c));

   // check signals
    QCOMPARE(selectionsChanged.count(), signalCount);
}

void tst_QtListSelectionManager::anchoredSelections_data()
{
    QTest::addColumn<bool>("useModel");
    QTest::addColumn<int>("itemCount");
    QTest::addColumn<QList<int> >("initialSelectionIndexes");
    QTest::addColumn<int>("mode");
    QTest::addColumn<int>("anchorIndex");
    QTest::addColumn<int>("currentIndex");
    QTest::addColumn<QList<int> >("expectedSelectionIndexes");
    QTest::addColumn<int>("insertAt");
    QTest::addColumn<int>("insertCount");
    QTest::addColumn<int>("removeAt");
    QTest::addColumn<int>("removeCount");
    QTest::addColumn<int>("moveFrom");
    QTest::addColumn<int>("moveTo");
    QTest::addColumn<int>("moveCount");
    QTest::addColumn<QList<int> >("changedSelectionIndexes");
    QTest::addColumn<QList<int> >("commitedSelectionIndexes");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no model")
            << false << 0               // model
            << QList<int>()             // initial
            << int(Select) << 0 << 0   // range
            << QList<int>()             // expected
            << 0 << 0                   // insert
            << 0 << 0                   // remove
            << 0 << 0 << 0              // move
            << QList<int>()             // changed
            << QList<int>()             // commited
            << 0;                       // signals

     QTest::newRow("valid range")
            << true << 10
            << QList<int>()
            << int(Select) << 4 << 7
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 4 << 5 << 6 << 7)
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 2; // begin and when setting current

     QTest::newRow("reversed valid range")
            << true << 10
            << QList<int>()
            << int(Select) << 7 << 4
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 4 << 5 << 6 << 7)
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 2;

    QTest::newRow("invalid anchor")
            << true << 10
            << QList<int>()
            << int(Select) << -1 << 3
            << (QList<int>() << 0 << 1 << 2 << 3)
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 0 << 1 << 2 << 3)
            << (QList<int>() << 0 << 1 << 2 << 3)
            << 1; // anchor doesn't change

    QTest::newRow("invalid current")
            << true << 10
            << QList<int>()
            << int(Select) << 6 << INT_MAX
            << (QList<int>() << 6 << 7 << 8 << 9)
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 6 << 7 << 8 << 9)
            << (QList<int>() << 6 << 7 << 8 << 9)
            << 2;

     QTest::newRow("same anchor and current")
            << true << 10
            << QList<int>()
            << int(Select) << 4 << 4
            << (QList<int>() << 4)
            << 0 << 0
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 4)
            << (QList<int>() << 4)
            << 2;

      QTest::newRow("valid range, one insert above")
            << true << 10
            << QList<int>()
            << int(Select) << 4 << 7
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 1 << 1
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 5 << 6 << 7 << 8)
            << (QList<int>() << 5 << 6 << 7 << 8)
            << 3;

        QTest::newRow("valid range, one insert below")
            << true << 10
            << QList<int>()
            << int(Select) << 4 << 7
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 8 << 1
            << 0 << 0
            << 0 << 0 << 0
            << (QList<int>() << 4 << 5 << 6 << 7)
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 2;

      QTest::newRow("valid range, one removal above")
            << true << 10
            << QList<int>()
            << int(Select) << 4 << 7
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 0 << 0
            << 1 << 1
            << 0 << 0 << 0
            << (QList<int>() << 3 << 4 << 5 << 6)
            << (QList<int>() << 3 << 4 << 5 << 6)
            << 3;

        QTest::newRow("valid range, one removal below")
            << true << 10
            << QList<int>()
            << int(Select) << 4 << 7
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 0 << 0
            << 8 << 1
            << 0 << 0 << 0
            << (QList<int>() << 4 << 5 << 6 << 7)
            << (QList<int>() << 4 << 5 << 6 << 7)
            << 2;
}

void tst_QtListSelectionManager::anchoredSelections()
{
    QFETCH(bool, useModel);
    QFETCH(int, itemCount);
    QFETCH(QList<int>, initialSelectionIndexes);
    QFETCH(int, mode);
    QFETCH(int, anchorIndex);
    QFETCH(int, currentIndex);
    QFETCH(QList<int>, expectedSelectionIndexes);
    QFETCH(int, insertAt);
    QFETCH(int, insertCount);
    QFETCH(int, removeAt);
    QFETCH(int, removeCount);
    QFETCH(int, moveFrom);
    QFETCH(int, moveTo);
    QFETCH(int, moveCount);
    QFETCH(QList<int>, changedSelectionIndexes);
    QFETCH(QList<int>, commitedSelectionIndexes);
    QFETCH(int, signalCount);

    QSignalSpy selectionsChanged(manager, SIGNAL(selectionsChanged(const QtListSelectionChange&)));

    // start without model
    manager->setModel(0);

    // setup the model item count
    for (int i = 0 ; i < itemCount; ++i)
        model->appendItem(new QtListDefaultItem(QString("Item %1").arg(i)));

    // with or without data model
    manager->setModel(useModel ? model : 0);
    QCOMPARE(manager->model(), (useModel ? model : 0));

    // set initial selection
    for (int a = 0; a < initialSelectionIndexes.count(); ++a)
        manager->setSelected(initialSelectionIndexes.at(a), 1, QtListSelectionManager::Select);

    // set anchor and mode
    manager->beginAnchoredSelection(anchorIndex, QtListSelectionManager::SelectionMode(mode));

    // set current to make a selected range
    manager->setCurrentItem(currentIndex);

    // check the range
    QList<int> selectedItems = manager->selectedItems();
    QCOMPARE(selectedItems.count(), expectedSelectionIndexes.count());
    for (int b = 0; b < expectedSelectionIndexes.count(); ++b)
        QCOMPARE(selectedItems.at(b), expectedSelectionIndexes.at(b));

    // insert items
    for (int j = insertAt; j < insertAt + insertCount; ++j)
        model->insertItem(j, new QtListDefaultItem(QString("Item %1").arg(j)));

    // remove items
    for (int k = 0; k < removeCount; ++k)
        model->removeItem(removeAt);

    // move items
    for (int l = 0; l < moveCount; ++l)
        model->moveItem(moveFrom, moveTo + l);

    // check selection again
    selectedItems = manager->selectedItems();
    QCOMPARE(selectedItems.count(), changedSelectionIndexes.count());
    for (int c = 0; c < changedSelectionIndexes.count(); ++c)
        QCOMPARE(selectedItems.at(c), changedSelectionIndexes.at(c));

    // commit the anchor selection
    manager->endAnchoredSelection();

    // check the comitted selection
    selectedItems = manager->selectedItems();
    QCOMPARE(selectedItems.count(), commitedSelectionIndexes.count());
    for (int d = 0; d < commitedSelectionIndexes.count(); ++d)
        QCOMPARE(selectedItems.at(d), commitedSelectionIndexes.at(d));

    // check signals
    QCOMPARE(selectionsChanged.count(), signalCount);
}

QTEST_MAIN(tst_QtListSelectionManager)
#include "tst_qlistselectionmanager.moc"
