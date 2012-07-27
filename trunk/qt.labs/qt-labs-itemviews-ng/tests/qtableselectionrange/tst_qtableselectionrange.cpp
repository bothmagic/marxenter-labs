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

class tst_QtTableSelectionRange : public QObject
{
    Q_OBJECT

public:
    tst_QtTableSelectionRange();
    virtual ~tst_QtTableSelectionRange();
    
public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void intersects_data();
    void intersects();
    void subtracted_data();
    void subtracted();
};

Q_DECLARE_METATYPE(QList<int>)

tst_QtTableSelectionRange::tst_QtTableSelectionRange()
{
}

tst_QtTableSelectionRange::~tst_QtTableSelectionRange()
{
}

void tst_QtTableSelectionRange::initTestCase()
{
}

void tst_QtTableSelectionRange::cleanupTestCase()
{
}

void tst_QtTableSelectionRange::init()
{
}

void tst_QtTableSelectionRange::cleanup()
{
}

void tst_QtTableSelectionRange::getSetCheck()
{
    QtTableSelectionRange invalid;

    QVERIFY(!invalid.isValid());

    QCOMPARE(invalid.topRow(), -1);
    QCOMPARE(invalid.bottomRow(), -1);
    QCOMPARE(invalid.leftColumn(), -1);
    QCOMPARE(invalid.rightColumn(), -1);

    QCOMPARE(invalid.rowCount(), 0);
    QCOMPARE(invalid.columnCount(), 0);

    QVERIFY(!invalid.intersectsRow(INT_MIN));
    QVERIFY(!invalid.intersectsRow(0));
    QVERIFY(!invalid.intersectsRow(INT_MAX));

    QVERIFY(!invalid.intersectsColumn(INT_MIN));
    QVERIFY(!invalid.intersectsColumn(0));
    QVERIFY(!invalid.intersectsColumn(INT_MAX));

    QVERIFY(!invalid.containsCell(INT_MIN, INT_MIN));
    QVERIFY(!invalid.containsCell(0, INT_MIN));
    QVERIFY(!invalid.containsCell(INT_MAX, INT_MIN));

    QVERIFY(!invalid.containsCell(INT_MIN, 0));
    QVERIFY(!invalid.containsCell(0, 0));
    QVERIFY(!invalid.containsCell(INT_MAX, 0));

    QVERIFY(!invalid.containsCell(INT_MIN, INT_MAX));
    QVERIFY(!invalid.containsCell(0, INT_MAX));
    QVERIFY(!invalid.containsCell(INT_MAX, INT_MAX));
    
    QtTableSelectionRange valid(0, 0, 1, 1);

    QVERIFY(valid.isValid());

    QCOMPARE(valid.topRow(), 0);
    QCOMPARE(valid.bottomRow(), 1);
    QCOMPARE(valid.leftColumn(), 0);
    QCOMPARE(valid.rightColumn(), 1);

    QCOMPARE(valid.rowCount(), 2);
    QCOMPARE(valid.columnCount(), 2);

    QVERIFY(!valid.intersectsRow(INT_MIN));
    QVERIFY(valid.intersectsRow(0));
    QVERIFY(!valid.intersectsRow(INT_MAX));

    QVERIFY(!valid.intersectsColumn(INT_MIN));
    QVERIFY(valid.intersectsColumn(0));
    QVERIFY(!valid.intersectsColumn(INT_MAX));

    QVERIFY(!valid.containsCell(INT_MIN, INT_MIN));
    QVERIFY(!valid.containsCell(0, INT_MIN));
    QVERIFY(!valid.containsCell(INT_MAX, INT_MIN));

    QVERIFY(!valid.containsCell(INT_MIN, 0));
    QVERIFY(valid.containsCell(0, 0));
    QVERIFY(!valid.containsCell(INT_MAX, 0));

    QVERIFY(!valid.containsCell(INT_MIN, INT_MAX));
    QVERIFY(!valid.containsCell(0, INT_MAX));
    QVERIFY(!valid.containsCell(INT_MAX, INT_MAX));
}

void tst_QtTableSelectionRange::intersects_data()
{
}

void tst_QtTableSelectionRange::intersects()
{
}

void tst_QtTableSelectionRange::subtracted_data()
{
    QTest::addColumn<int>("rangeTop");
    QTest::addColumn<int>("rangeLeft");
    QTest::addColumn<int>("rangeBottom");
    QTest::addColumn<int>("rangeRight");

    QTest::addColumn<int>("subtractTop");
    QTest::addColumn<int>("subtractLeft");
    QTest::addColumn<int>("subtractBottom");
    QTest::addColumn<int>("subtractRight");

    QTest::addColumn<int>("resultsCount");

    QTest::addColumn<QList<int> >("resultsTop");
    QTest::addColumn<QList<int> >("resultsLeft");
    QTest::addColumn<QList<int> >("resultsBottom");
    QTest::addColumn<QList<int> >("resultsRight");

    QTest::newRow("0. contained")
        << 1 << 1 << 1 << 1 // range
        << 0 << 0 << 2 << 2 // subtract
        << 0                // results count
        << (QList<int>())   // results top
        << (QList<int>())   // results left
        << (QList<int>())   // results bottom
        << (QList<int>());  // results right

    QTest::newRow("1. contains")
        << 0 << 0 << 2 << 2                    // range
        << 1 << 1 << 1 << 1                    // subtract
        << 4                                   // results count
        << (QList<int>() << 0 << 1 << 2 << 1)  // results top
        << (QList<int>() << 0 << 0 << 0 << 2)  // results left
        << (QList<int>() << 0 << 1 << 2 << 1)  // results bottom
        << (QList<int>() << 2 << 0 << 2 << 2); // results right

    QTest::newRow("2. top side")
        << 0 << 0 << 2 << 2      // range
        << 0 << 0 << 0 << 2      // subtract
        << 1                     // results count
        << (QList<int>() << 1)   // results top
        << (QList<int>() << 0)   // results left
        << (QList<int>() << 2)   // results bottom
        << (QList<int>() << 2);  // results right

    QTest::newRow("3. left side")
        << 0 << 0 << 2 << 2      // range
        << 0 << 0 << 2 << 0      // subtract
        << 1                     // results count
        << (QList<int>() << 0)   // results top
        << (QList<int>() << 1)   // results left
        << (QList<int>() << 2)   // results bottom
        << (QList<int>() << 2);  // results right
    
    QTest::newRow("4. bottom side")
        << 0 << 0 << 2 << 2      // range
        << 2 << 0 << 2 << 2      // subtract
        << 1                     // results count
        << (QList<int>() << 0)   // results top
        << (QList<int>() << 0)   // results left
        << (QList<int>() << 1)   // results bottom
        << (QList<int>() << 2);  // results right

    QTest::newRow("5. right side")
        << 0 << 0 << 2 << 2      // range
        << 0 << 2 << 2 << 2      // subtract
        << 1                     // results count
        << (QList<int>() << 0)   // results top
        << (QList<int>() << 0)   // results left
        << (QList<int>() << 2)   // results bottom
        << (QList<int>() << 1);  // results right
    
    QTest::newRow("6. top left corner")
        << 0 << 0 << 2 << 2          // range
        << 0 << 0 << 0 << 0          // subtract
        << 2                         // results count
        << (QList<int>() << 0 << 1)  // results top
        << (QList<int>() << 1 << 0)  // results left
        << (QList<int>() << 0 << 2)  // results bottom
        << (QList<int>() << 2 << 2); // results right
    
    QTest::newRow("7. bottom left corner")
        << 0 << 0 << 2 << 2          // range
        << 2 << 0 << 2 << 0          // subtract
        << 2                         // results count
        << (QList<int>() << 0 << 2)  // results top
        << (QList<int>() << 0 << 1)  // results left
        << (QList<int>() << 1 << 2)  // results bottom
        << (QList<int>() << 2 << 2); // results right

    QTest::newRow("8. top right corner")
        << 0 << 0 << 2 << 2          // range
        << 0 << 2 << 0 << 2          // subtract
        << 2                         // results count
        << (QList<int>() << 0 << 1)  // results top
        << (QList<int>() << 0 << 0)  // results left
        << (QList<int>() << 0 << 2)  // results bottom
        << (QList<int>() << 1 << 2); // results right

    QTest::newRow("9. bottom right corner")
        << 0 << 0 << 2 << 2          // range
        << 2 << 2 << 2 << 2          // subtract
        << 2                         // results count
        << (QList<int>() << 0 << 2)  // results top
        << (QList<int>() << 0 << 0)  // results left
        << (QList<int>() << 1 << 2)  // results bottom
        << (QList<int>() << 2 << 1); // results right

    QTest::newRow("10. into top side")
        << 0 << 0 << 2 << 2               // range
        << 0 << 1 << 0 << 1               // subtract
        << 3                              // results count
        << (QList<int>() << 0 << 0 << 1)  // results top
        << (QList<int>() << 0 << 2 << 0)  // results left
        << (QList<int>() << 0 << 0 << 2)  // results bottom
        << (QList<int>() << 0 << 2 << 2); // results right

    QTest::newRow("11. into left side")
        << 0 << 0 << 2 << 2               // range
        << 1 << 0 << 1 << 0               // subtract
        << 3                              // results count
        << (QList<int>() << 0 << 2 << 0)  // results top
        << (QList<int>() << 0 << 0 << 1)  // results left
        << (QList<int>() << 0 << 2 << 2)  // results bottom
        << (QList<int>() << 0 << 0 << 2); // results right

    QTest::newRow("12. into bottom side")
        << 0 << 0 << 2 << 2               // range
        << 2 << 1 << 2 << 1               // subtract
        << 3                              // results count
        << (QList<int>() << 0 << 2 << 2)  // results top
        << (QList<int>() << 0 << 0 << 2)  // results left
        << (QList<int>() << 1 << 2 << 2)  // results bottom
        << (QList<int>() << 2 << 0 << 2); // results right


    QTest::newRow("13. into right side")
        << 0 << 0 << 2 << 2               // range
        << 1 << 2 << 1 << 2               // subtract
        << 3                              // results count
        << (QList<int>() << 0 << 0 << 2)  // results top
        << (QList<int>() << 0 << 2 << 2)  // results left
        << (QList<int>() << 2 << 0 << 2)  // results bottom
        << (QList<int>() << 1 << 2 << 2); // results right

    QTest::newRow("14. intersect horizontally")
        << 0 << 0 << 2 << 2          // range
        << 1 << 0 << 1 << 2          // subtract
        << 2                         // results count
        << (QList<int>() << 0 << 2)  // results top
        << (QList<int>() << 0 << 0)  // results left
        << (QList<int>() << 0 << 2)  // results bottom
        << (QList<int>() << 2 << 2); // results right
    
    QTest::newRow("15. intersect vertically")
        << 0 << 0 << 2 << 2          // range
        << 0 << 1 << 2 << 1          // subtract
        << 2                         // results count
        << (QList<int>() << 0 << 0)  // results top
        << (QList<int>() << 0 << 2)  // results left
        << (QList<int>() << 2 << 2)  // results bottom
        << (QList<int>() << 0 << 2); // results right
}

void tst_QtTableSelectionRange::subtracted()
{
    QFETCH(int, rangeTop);
    QFETCH(int, rangeLeft);
    QFETCH(int, rangeBottom);
    QFETCH(int, rangeRight);

    QFETCH(int, subtractTop);
    QFETCH(int, subtractLeft);
    QFETCH(int, subtractBottom);
    QFETCH(int, subtractRight);

    QFETCH(int, resultsCount);

    QFETCH(QList<int>, resultsTop);
    QFETCH(QList<int>, resultsLeft);
    QFETCH(QList<int>, resultsBottom);
    QFETCH(QList<int>, resultsRight);
    
    QtTableSelectionRange range(rangeTop, rangeLeft, rangeBottom, rangeRight);
    QtTableSelectionRange subtract(subtractTop, subtractLeft, subtractBottom, subtractRight);
    QList<QtTableSelectionRange> results = range.subtracted(subtract);

    QCOMPARE(results.count(), resultsCount);
    for (int i = 0; i < resultsCount; ++i) {
        QCOMPARE(results.at(i).topRow(), resultsTop.at(i));
        QCOMPARE(results.at(i).leftColumn(), resultsLeft.at(i));
        QCOMPARE(results.at(i).bottomRow(), resultsBottom.at(i));
        QCOMPARE(results.at(i).rightColumn(), resultsRight.at(i));
    }
}

QTEST_MAIN(tst_QtTableSelectionRange)
#include "tst_qtableselectionrange.moc"
