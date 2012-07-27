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
#include <qtableselectionmanager.h>
#include <qtabledefaultmodel.h>

class TableSelectionManager : public QtTableSelectionManager
{
public:
    inline void setModel(QtTableModelInterface *model)
    {
        QtTableSelectionManager::setModel(model);
    }
};

class tst_QtTableSelectionManager : public QObject
{
    Q_OBJECT

public:
    enum SelectionMode {
        Select = QtTableSelectionManager::Select,
        Deselect = QtTableSelectionManager::Deselect,
        Toggle = QtTableSelectionManager::Toggle
    };

    tst_QtTableSelectionManager();
    virtual ~tst_QtTableSelectionManager();
    
public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void currentCell_data();
    void currentCell();
    void anchorCell_data();
    void anchorCell();
    void selections_data();
    void selections();
    void anchoredSelections_data();
    void anchoredSelections();

private:
    TableSelectionManager *manager;
    QtTableDefaultModel *model;
};

Q_DECLARE_METATYPE(QList<int>)
Q_DECLARE_METATYPE(QList<bool>)
Q_DECLARE_METATYPE(QtTableSelectionRange)
Q_DECLARE_METATYPE(QList<QtTableSelectionRange>)

tst_QtTableSelectionManager::tst_QtTableSelectionManager()
    : manager(0), model(0)
{
    qRegisterMetaType<QtTableSelectionRange>("QtTableSelectionRange");
}

tst_QtTableSelectionManager::~tst_QtTableSelectionManager()
{
}

void tst_QtTableSelectionManager::initTestCase()
{
}

void tst_QtTableSelectionManager::cleanupTestCase()
{
}

void tst_QtTableSelectionManager::init()
{
    manager = new TableSelectionManager();
    model = new QtTableDefaultModel();
    manager->setModel(model);
}

void tst_QtTableSelectionManager::cleanup()
{
    delete manager;
    delete model;
}

void tst_QtTableSelectionManager::getSetCheck()
{
}

/*
  Note that current cell and anchor cell should behave the same.
  So if you modify one test, make sure you also modify the other.
 */
void tst_QtTableSelectionManager::currentCell_data()
{
    QTest::addColumn<bool>("useModel");
    QTest::addColumn<int>("rowCount");
    QTest::addColumn<int>("columnCount");
    QTest::addColumn<int>("initialCurrentRow");
    QTest::addColumn<int>("initialCurrentColumn");
    QTest::addColumn<int>("newCurrentRow");
    QTest::addColumn<int>("newCurrentColumn");
    QTest::addColumn<int>("expectedCurrentRow");
    QTest::addColumn<int>("expectedCurrentColumn");
    QTest::addColumn<int>("insertRowsAt");
    QTest::addColumn<int>("insertRowCount");
    QTest::addColumn<int>("insertColumnsAt");
    QTest::addColumn<int>("insertColumnCount");
    QTest::addColumn<int>("removeRowsAt");
    QTest::addColumn<int>("removeRowCount");
    QTest::addColumn<int>("removeColumnsAt");
    QTest::addColumn<int>("removeColumnCount");
    QTest::addColumn<int>("moveRowsFrom");
    QTest::addColumn<int>("moveRowsTo");
    QTest::addColumn<int>("moveRowCount");
    QTest::addColumn<int>("moveColumnsFrom");
    QTest::addColumn<int>("moveColumnsTo");
    QTest::addColumn<int>("moveColumnCount");
    QTest::addColumn<int>("changedCurrentRow");
    QTest::addColumn<int>("changedCurrentColumn");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no model")
        << false         // use model
        << 0 << 0       // cell count
        << -1 << -1     // current cell
        << 0 << 0       // new current
        << -1 << -1     // expected current
        << 0 << 0       // insert rows
        << 0 << 0       // insert columns
        << 0 << 0       // remove rows
        << 0 << 0       // remove columns
        << 0 << 0 << 0  // move rows
        << 0 << 0 << 0  // move columns
        << -1 << -1     // changed current
        << 0;            // signals

    QTest::newRow("no data")
        << true
        << 0 << 0
        << -1 << -1
        << 0 << 0
        << -1 << -1
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << -1 << -1
        << 0;

    QTest::newRow("before first row")
        << true
        << 10 << 10
        << 0 << 0
        << -1 << -1
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << 0 << 0
        << 0;

    QTest::newRow("valid")
        << true
        << 10 << 10
        << 0 << 0
        << 4 << 4
        << 4 << 4
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << 4 << 4
        << 1;

    QTest::newRow("after last row")
        << true
        << 10 << 10
        << 0 << 0
        << 10 << 10
        << 9 << 9
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << 9 << 9
        << 1;
}

void tst_QtTableSelectionManager::currentCell()
{
    QFETCH(bool, useModel);
    QFETCH(int, rowCount);
    QFETCH(int, columnCount);
    QFETCH(int, initialCurrentRow);
    QFETCH(int, initialCurrentColumn);
    QFETCH(int, newCurrentRow);
    QFETCH(int, newCurrentColumn);
    QFETCH(int, expectedCurrentRow);
    QFETCH(int, expectedCurrentColumn);
    QFETCH(int, insertRowsAt);
    QFETCH(int, insertRowCount);
    QFETCH(int, insertColumnsAt);
    QFETCH(int, insertColumnCount);
    QFETCH(int, removeRowsAt);
    QFETCH(int, removeRowCount);
    QFETCH(int, removeColumnsAt);
    QFETCH(int, removeColumnCount);
    QFETCH(int, moveRowsFrom);
    QFETCH(int, moveRowsTo);
    QFETCH(int, moveRowCount);
    QFETCH(int, moveColumnsFrom);
    QFETCH(int, moveColumnsTo);
    QFETCH(int, moveColumnCount);
    QFETCH(int, changedCurrentRow);
    QFETCH(int, changedCurrentColumn);
    QFETCH(int, signalCount);

    QSignalSpy currentChanged(manager, SIGNAL(currentChanged(int,int,int,int)));

    // start without model
    manager->setModel(0);

    // setup the model row and column count
    model->setRowCount(rowCount);
    model->setColumnCount(columnCount);

    // with or without data model
    manager->setModel(useModel ? model : 0);
    QCOMPARE(manager->model(), (useModel ? model : 0));

    // check initial current item and selection
    QCOMPARE(manager->currentRow(), initialCurrentRow);
    QCOMPARE(manager->currentColumn(), initialCurrentColumn);
    //QCOMPARE(manager->selectionRanges().count(), selectionRangesCount);

    // set current cell
    manager->setCurrentCell(newCurrentRow, newCurrentColumn);
    QCOMPARE(manager->currentRow(), expectedCurrentRow);
    QCOMPARE(manager->currentColumn(), expectedCurrentColumn);
    //QCOMPARE(manager->selectionRanges().count(), selectionRangesCount);

    // insert rows
    for (int i = insertRowsAt; i < insertRowCount; ++i)
        model->insertRow(i);

    // insert columns
    for (int j = insertColumnsAt; j < insertColumnCount; ++j)
        model->insertColumn(j);

    // remove rows
    for (int k = removeRowsAt; k < removeRowCount; ++k)
        model->removeRow(k);

    // remove columns
    for (int l = removeColumnsAt; l < removeColumnCount; ++l)
        model->removeColumn(l);

    // move rows
    for (int m = 0; m < moveRowCount; ++m)
        model->moveRow(moveRowsFrom, moveRowsTo + m);

    // move columns
    for (int n = 0; n < moveColumnCount; ++n)
        model->moveColumn(moveColumnsFrom, moveColumnsTo + n);

    // check again
    QCOMPARE(manager->currentRow(), changedCurrentRow);
    QCOMPARE(manager->currentColumn(), changedCurrentColumn);

    // check signals
    QCOMPARE(currentChanged.count(), signalCount);
}

/*
  Note that current cell and anchor cell should behave the same.
  So if you modify one test, make sure you also modify the other.
 */
void tst_QtTableSelectionManager::anchorCell_data()
{
    QTest::addColumn<bool>("useModel");
    QTest::addColumn<int>("rowCount");
    QTest::addColumn<int>("columnCount");
    QTest::addColumn<int>("initialAnchorRow");
    QTest::addColumn<int>("initialAnchorColumn");
    QTest::addColumn<int>("newAnchorRow");
    QTest::addColumn<int>("newAnchorColumn");
    QTest::addColumn<int>("expectedAnchorRow");
    QTest::addColumn<int>("expectedAnchorColumn");
    QTest::addColumn<int>("insertRowsAt");
    QTest::addColumn<int>("insertRowCount");
    QTest::addColumn<int>("insertColumnsAt");
    QTest::addColumn<int>("insertColumnCount");
    QTest::addColumn<int>("removeRowsAt");
    QTest::addColumn<int>("removeRowCount");
    QTest::addColumn<int>("removeColumnsAt");
    QTest::addColumn<int>("removeColumnCount");
    QTest::addColumn<int>("moveRowsFrom");
    QTest::addColumn<int>("moveRowsTo");
    QTest::addColumn<int>("moveRowCount");
    QTest::addColumn<int>("moveColumnsFrom");
    QTest::addColumn<int>("moveColumnsTo");
    QTest::addColumn<int>("moveColumnCount");
    QTest::addColumn<int>("changedAnchorRow");
    QTest::addColumn<int>("changedAnchorColumn");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no model")
        << false
        << 0 << 0       // cell count
        << -1 << -1     // anchor cell
        << 0 << 0       // new anchor
        << -1 << -1     // expected anchor
        << 0 << 0       // insert rows
        << 0 << 0       // insert columns
        << 0 << 0       // remove rows
        << 0 << 0       // remove columns
        << 0 << 0 << 0  // move rows
        << 0 << 0 << 0  // move columns
        << -1 << -1     // changed anchor
        << 0;            // signals

    QTest::newRow("no data")
        << true
        << 0 << 0
        << -1 << -1
        << 0 << 0
        << -1 << -1
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << -1 << -1
        << 0;

    QTest::newRow("before first row")
        << true
        << 10 << 10
        << 0 << 0
        << -1 << -1
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << 0 << 0
        << 0;

    QTest::newRow("valid")
        << true
        << 10 << 10
        << 0 << 0
        << 4 << 4
        << 4 << 4
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << 4 << 4
        << 1;

    QTest::newRow("after last row")
        << true
        << 10 << 10
        << 0 << 0
        << 10 << 10
        << 9 << 9
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << 9 << 9
        << 1;
}

void tst_QtTableSelectionManager::anchorCell()
{
    QFETCH(bool, useModel);
    QFETCH(int, rowCount);
    QFETCH(int, columnCount);
    QFETCH(int, initialAnchorRow);
    QFETCH(int, initialAnchorColumn);
    QFETCH(int, newAnchorRow);
    QFETCH(int, newAnchorColumn);
    QFETCH(int, expectedAnchorRow);
    QFETCH(int, expectedAnchorColumn);
    QFETCH(int, insertRowsAt);
    QFETCH(int, insertRowCount);
    QFETCH(int, insertColumnsAt);
    QFETCH(int, insertColumnCount);
    QFETCH(int, removeRowsAt);
    QFETCH(int, removeRowCount);
    QFETCH(int, removeColumnsAt);
    QFETCH(int, removeColumnCount);
    QFETCH(int, moveRowsFrom);
    QFETCH(int, moveRowsTo);
    QFETCH(int, moveRowCount);
    QFETCH(int, moveColumnsFrom);
    QFETCH(int, moveColumnsTo);
    QFETCH(int, moveColumnCount);
    QFETCH(int, changedAnchorRow);
    QFETCH(int, changedAnchorColumn);
    QFETCH(int, signalCount);

    QSignalSpy anchorChanged(manager, SIGNAL(anchorChanged(int,int,int,int)));

    // start without model
    manager->setModel(0);

    // setup the model row and column count
    model->setRowCount(rowCount);
    model->setColumnCount(columnCount);

    // with or without data model
    manager->setModel(useModel ? model : 0);
    QCOMPARE(manager->model(), (useModel ? model : 0));

    // check initial anchor item and selection
    QCOMPARE(manager->anchorRow(), initialAnchorRow);
    QCOMPARE(manager->anchorColumn(), initialAnchorColumn);
    //QCOMPARE(manager->selectionRanges().count(), selectionRangesCount);

    // set anchor cell
    manager->setAnchorCell(newAnchorRow, newAnchorColumn);
    QCOMPARE(manager->anchorRow(), expectedAnchorRow);
    QCOMPARE(manager->anchorColumn(), expectedAnchorColumn);
    //QCOMPARE(manager->selectionRanges().count(), selectionRangesCount);

    // insert rows
    for (int i = insertRowsAt; i < insertRowCount; ++i)
        model->insertRow(i);

    // insert columns
    for (int j = insertColumnsAt; j < insertColumnCount; ++j)
        model->insertColumn(j);

    // remove rows
    for (int k = removeRowsAt; k < removeRowCount; ++k)
        model->removeRow(k);

    // remove columns
    for (int l = removeColumnsAt; l < removeColumnCount; ++l)
        model->removeColumn(l);

    // move rows
    for (int m = 0; m < moveRowCount; ++m)
        model->moveRow(moveRowsFrom, moveRowsTo + m);

    // move columns
    for (int n = 0; n < moveColumnCount; ++n)
        model->moveColumn(moveColumnsFrom, moveColumnsTo + n);

    // check again
    QCOMPARE(manager->anchorRow(), changedAnchorRow);
    QCOMPARE(manager->anchorColumn(), changedAnchorColumn);

    // check signals
    QCOMPARE(anchorChanged.count(), signalCount);
}

void tst_QtTableSelectionManager::selections_data()
{
    QTest::addColumn<bool>("useModel");
    QTest::addColumn<int>("rowCount");
    QTest::addColumn<int>("columnCount");
    QTest::addColumn<QList<QtTableSelectionRange> >("newSelections");
    QTest::addColumn<QList<int> >("newSelectionModes");
    QTest::addColumn<QList<QtTableSelectionRange> >("expectedSelections");
    QTest::addColumn<int>("insertRowsAt");
    QTest::addColumn<int>("insertRowCount");
    QTest::addColumn<int>("insertColumnsAt");
    QTest::addColumn<int>("insertColumnCount");
    QTest::addColumn<int>("removeRowsAt");
    QTest::addColumn<int>("removeRowCount");
    QTest::addColumn<int>("removeColumnsAt");
    QTest::addColumn<int>("removeColumnCount");
    QTest::addColumn<int>("moveRowsFrom");
    QTest::addColumn<int>("moveRowsTo");
    QTest::addColumn<int>("moveRowCount");
    QTest::addColumn<int>("moveColumnsFrom");
    QTest::addColumn<int>("moveColumnsTo");
    QTest::addColumn<int>("moveColumnCount");
    QTest::addColumn<QList<QtTableSelectionRange> >("changedSelections");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no model")
        << false        // use model
        << 0 << 0       // cell count
        << QList<QtTableSelectionRange>()
        << QList<int>() // modes
        << QList<QtTableSelectionRange>()
        << 0 << 0       // insert rows
        << 0 << 0       // insert columns
        << 0 << 0       // remove rows
        << 0 << 0       // remove columns
        << 0 << 0 << 0  // move rows
        << 0 << 0 << 0  // move columns
        << QList<QtTableSelectionRange>()
        << 0;            // signals

    QTest::newRow("no model, one selection")
        << false
        << 0 << 0
        << (QList<QtTableSelectionRange>() << QtTableSelectionRange(0, 0, 1, 1))
        << (QList<int>() << int(Select))
        << QList<QtTableSelectionRange>()
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << QList<QtTableSelectionRange>()
        << 0;

    QTest::newRow("no data")
        << true
        << 0 << 0
        << QList<QtTableSelectionRange>()
        << QList<int>()
        << QList<QtTableSelectionRange>()
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << QList<QtTableSelectionRange>()
        << 0;

    QTest::newRow("no data, one selection")
        << true
        << 0 << 0
        << (QList<QtTableSelectionRange>() << QtTableSelectionRange(0, 0, 1, 1))
        << (QList<int>() << int(Select))
        << QList<QtTableSelectionRange>()
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << QList<QtTableSelectionRange>()
        << 0;

    QTest::newRow("no selections")
        << true
        << 10 << 10
        << QList<QtTableSelectionRange>()
        << QList<int>()
        << QList<QtTableSelectionRange>()
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << QList<QtTableSelectionRange>()
        << 0;

    QTest::newRow("one selection")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>() << QtTableSelectionRange(0, 0, 1, 1))
        << (QList<int>() << int(Select))
        << (QList<QtTableSelectionRange>() << QtTableSelectionRange(0, 0, 1, 1))
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>() << QtTableSelectionRange(0, 0, 1, 1))
        << 1;

    QTest::newRow("two selections")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 0, 0, 9)
            << QtTableSelectionRange(2, 0, 2, 9))
        << (QList<int>()
            << int(Select)
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 0, 0, 9)
            << QtTableSelectionRange(2, 0, 2, 9))
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 0, 0, 9)
            << QtTableSelectionRange(2, 0, 2, 9))
        << 2;

    QTest::newRow("two selections, one deselection")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 0, 0, 9)
            << QtTableSelectionRange(2, 0, 2, 9)
            << QtTableSelectionRange(0, 3, 0, 6))
        << (QList<int>()
            << int(Select)
            << int(Select)
            << int(Deselect))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(2, 0, 2, 9)
            << QtTableSelectionRange(0, 0, 0, 2)
            << QtTableSelectionRange(0, 7, 0, 9))
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(2, 0, 2, 9)
            << QtTableSelectionRange(0, 0, 0, 2)
            << QtTableSelectionRange(0, 7, 0, 9))
        << 3;

    QTest::newRow("one invalid and one valid selection")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(-1, -1, -1, -1)
            << QtTableSelectionRange(0, 0, INT_MAX, INT_MAX))
        << (QList<int>()
            << int(Select)
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 0, INT_MAX, INT_MAX))
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 0, INT_MAX, INT_MAX))
        << 1;

    QTest::newRow("no selections, one row insertion")
        << true
        << 10 << 10
        << QList<QtTableSelectionRange>()
        << QList<int>()
        << QList<QtTableSelectionRange>()
        << 4 << 1
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << QList<QtTableSelectionRange>()
        << 0;

    QTest::newRow("no selections, one column insertion")
        << true
        << 10 << 10
        << QList<QtTableSelectionRange>()
        << QList<int>()
        << QList<QtTableSelectionRange>()
        << 0 << 0
        << 4 << 1
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << QList<QtTableSelectionRange>()
        << 0;

    QTest::newRow("no selections, one row removal")
        << true
        << 10 << 10
        << QList<QtTableSelectionRange>()
        << QList<int>()
        << QList<QtTableSelectionRange>()
        << 0 << 0
        << 0 << 0
        << 4 << 1
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << QList<QtTableSelectionRange>()
        << 0;

    QTest::newRow("no selections, one column removal")
        << true
        << 10 << 10
        << QList<QtTableSelectionRange>()
        << QList<int>()
        << QList<QtTableSelectionRange>()
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 4 << 1
        << 0 << 0 << 0
        << 0 << 0 << 0
        << QList<QtTableSelectionRange>()
        << 0;

    QTest::newRow("one selections, one row insertion before")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << 2 << 1
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(5, 0, 5, 9))
        << 2;

    QTest::newRow("one selections, one row insertion in the middle")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(3, 0, 5, 9))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(3, 0, 5, 9))
        << 4 << 1
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(3, 0, 6, 9))
        << 2;

    QTest::newRow("one selections, one row insertion after")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << 6 << 1
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << 1;

    QTest::newRow("one selections, one column insertion before")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << 0 << 0
        << 2 << 1
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 5, 9, 5))
        << 2;

    QTest::newRow("one selections, one column insertion in the middle")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 3, 9, 5))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 3, 9, 5))
        << 0 << 0
        << 4 << 1
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 3, 9, 6))
        << 2;

    QTest::newRow("one selections, one column insertion after")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << 0 << 0
        << 6 << 1
        << 0 << 0
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << 1;

    QTest::newRow("one selections, one row removal before")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << 0 << 0
        << 0 << 0
        << 2 << 1
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(3, 0, 3, 9))
        << 2;

    QTest::newRow("one selections, one row removal in the middle")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(3, 0, 5, 9))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(3, 0, 5, 9))
        << 0 << 0
        << 0 << 0
        << 4 << 1
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(3, 0, 4, 9))
        << 2;

    QTest::newRow("one selections, one row removal after")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << 0 << 0
        << 0 << 0
        << 6 << 1
        << 0 << 0
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(4, 0, 4, 9))
        << 1;

    QTest::newRow("one selections, one column removal before")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 2 << 1
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 3, 9, 3))
        << 2;

    QTest::newRow("one selections, one column removal in the middle")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 3, 9, 5))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 3, 9, 5))
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 4 << 1
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 3, 9, 4))
        << 2;

    QTest::newRow("one selections, one column removal after")
        << true
        << 10 << 10
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << (QList<int>()
            << int(Select))
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << 0 << 0
        << 0 << 0
        << 0 << 0
        << 6 << 1
        << 0 << 0 << 0
        << 0 << 0 << 0
        << (QList<QtTableSelectionRange>()
            << QtTableSelectionRange(0, 4, 9, 4))
        << 1;
}

void tst_QtTableSelectionManager::selections()
{
    QFETCH(bool, useModel);
    QFETCH(int, rowCount);
    QFETCH(int, columnCount);
    QFETCH(QList<QtTableSelectionRange>, newSelections);
    QFETCH(QList<int>, newSelectionModes);
    QFETCH(QList<QtTableSelectionRange>, expectedSelections);
    QFETCH(int, insertRowsAt);
    QFETCH(int, insertRowCount);
    QFETCH(int, insertColumnsAt);
    QFETCH(int, insertColumnCount);
    QFETCH(int, removeRowsAt);
    QFETCH(int, removeRowCount);
    QFETCH(int, removeColumnsAt);
    QFETCH(int, removeColumnCount);
    QFETCH(int, moveRowsFrom);
    QFETCH(int, moveRowsTo);
    QFETCH(int, moveRowCount);
    QFETCH(int, moveColumnsFrom);
    QFETCH(int, moveColumnsTo);
    QFETCH(int, moveColumnCount);
    QFETCH(QList<QtTableSelectionRange>, changedSelections);
    QFETCH(int, signalCount);

    QSignalSpy selectionsChanged(manager, SIGNAL(selectionsChanged(const QList<QtTableSelectionRange>&)));

    // start without model
    manager->setModel(0);

    // setup the model item count
    model->setRowCount(rowCount);
    model->setColumnCount(columnCount);

    // with or without data model
    manager->setModel(useModel ? model : 0);
    QCOMPARE(manager->model(), (useModel ? model : 0));

    // set and check selection
    for (int a = 0; a < newSelections.count(); ++a)
        manager->setSelected(newSelections.at(a), QtTableSelectionManager::SelectionMode(newSelectionModes.at(a)));
    QList<QtTableSelectionRange> selectionRanges = manager->selectionRanges();
    QCOMPARE(selectionRanges.count(), expectedSelections.count());
    for (int b = 0; b < expectedSelections.count(); ++b)
        QCOMPARE(selectionRanges.at(b), expectedSelections.at(b));

    // insert rows
    for (int j = insertRowsAt; j < insertRowsAt + insertRowCount; ++j)
        model->insertRow(j);

    // insert columns
    for (int k = insertColumnsAt; k < insertColumnsAt + insertColumnCount; ++k)
        model->insertColumn(k);

    // remove rows
    for (int l = removeRowsAt; l < removeRowsAt + removeRowCount; ++l)
        model->removeRow(l);

    // remove columns
    for (int m = removeColumnsAt; m < removeColumnsAt + removeColumnCount; ++m)
        model->removeColumn(m);

    // move rows
    for (int n = 0; n < moveRowCount; ++n)
        model->moveRow(moveRowsFrom, moveRowsTo + n);

    // move columns
    for (int o = 0; o < moveColumnCount; ++o)
        model->moveColumn(moveColumnsFrom, moveColumnsTo + o);

    // check selection again
    selectionRanges = manager->selectionRanges();
    QCOMPARE(selectionRanges.count(), changedSelections.count());
    for (int p = 0; p < changedSelections.count(); ++p)
        QCOMPARE(selectionRanges.at(p), changedSelections.at(p));

   // check signals
    QCOMPARE(selectionsChanged.count(), signalCount);
}

void tst_QtTableSelectionManager::anchoredSelections_data()
{
}

void tst_QtTableSelectionManager::anchoredSelections()
{
}

QTEST_MAIN(tst_QtTableSelectionManager)
#include "tst_qtableselectionmanager.moc"
