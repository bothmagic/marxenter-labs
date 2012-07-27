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
#include "qsectionspans_p.h"

class tst_QSectionSpans : public QObject
{
    Q_OBJECT

public:
    tst_QSectionSpans();
    virtual ~tst_QSectionSpans();
    
public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void setValue_data();
    void setValue();
    void addSections_data();
    void addSections();
    void removeSections_data();
    void removeSections();
    void insertSectionsAt_data();
    void insertSectionsAt();
    void removeSectionsAt_data();
    void removeSectionsAt();
    void replaceAll_data();
    void replaceAll();
};

Q_DECLARE_METATYPE(QList<int>)

tst_QSectionSpans::tst_QSectionSpans()
{
}

tst_QSectionSpans::~tst_QSectionSpans()
{
}

void tst_QSectionSpans::initTestCase()
{
}

void tst_QSectionSpans::cleanupTestCase()
{
}

void tst_QSectionSpans::init()
{
}

void tst_QSectionSpans::cleanup()
{
}

void tst_QSectionSpans::getSetCheck()
{
}

void tst_QSectionSpans::setValue_data()
{
    QTest::addColumn<int>("empty");
    QTest::addColumn<QList<int> >("sectionIndexes");
    QTest::addColumn<QList<int> >("sectionValues");
    QTest::addColumn<QList<int> >("spanValues");
    QTest::addColumn<QList<int> >("spanCounts");
    QTest::addColumn<QList<int> >("expectedValues");

    QTest::newRow("-")
        << 0
        << (QList<int>() << 2 << 5 << 7 << 8 << 9)
        << (QList<int>() << 10 << 100 << 1000 << 1000 << 1000)
        << (QList<int>() << 0 << 10 << 0 << 100 << 0 << 1000)
        << (QList<int>() << 2 << 1 << 2 << 1 << 1 << 3)
        << (QList<int>() << 0 << 0 << 10 << 0 << 0 << 100 << 0 << 1000 << 1000 << 1000);
}

void tst_QSectionSpans::setValue()
{
    QFETCH(int, empty);
    QFETCH(QList<int>, sectionIndexes);
    QFETCH(QList<int>, sectionValues);
    QFETCH(QList<int>, spanValues);
    QFETCH(QList<int>, spanCounts);
    QFETCH(QList<int>, expectedValues);

    QtSectionSpans<int> spans;
    QCOMPARE(spans.count(), 0);

    spans.addSections(empty, expectedValues.count());

    QCOMPARE(spans.sectionCount(), expectedValues.count());
    for (int i = 0; i < sectionIndexes.count(); ++i)
        spans.setValue(sectionIndexes.at(i), sectionValues.at(i));
    
    QCOMPARE(spans.count(), spanValues.count());
    for (int j = 0; j < spanValues.count(); ++j) {
        QCOMPARE(spans.at(j).value, spanValues.at(j));
        QCOMPARE(spans.at(j).count, uint(spanCounts.at(j)));
    }

    QCOMPARE(spans.sectionCount(), expectedValues.count());
    for (int k = 0; k < expectedValues.count(); ++k)
        QCOMPARE(spans.sectionValue(k, -1), expectedValues.at(k));
}

void tst_QSectionSpans::addSections_data()
{
    QTest::addColumn<int>("empty");
    QTest::addColumn<int >("sectionCount");
    QTest::addColumn<int >("spanCount");

    QTest::newRow("-") << 0 << 10 << 1;
}

void tst_QSectionSpans::addSections()
{
    QFETCH(int, empty);
    QFETCH(int, sectionCount);
    QFETCH(int, spanCount);
        
    QtSectionSpans<int> spans;
    QCOMPARE(spans.count(), 0);

    spans.addSections(empty, sectionCount);
    QCOMPARE(spans.sectionCount(), sectionCount);
    QCOMPARE(spans.count(), spanCount);
}

void tst_QSectionSpans::removeSections_data()
{
}

void tst_QSectionSpans::removeSections()
{
}

void tst_QSectionSpans::insertSectionsAt_data()
{
}

void tst_QSectionSpans::insertSectionsAt()
{
}

void tst_QSectionSpans::removeSectionsAt_data()
{
    QTest::addColumn<int>("empty");
    QTest::addColumn<QList<int> >("sectionIndexes");
    QTest::addColumn<QList<int> >("sectionValues");
    QTest::addColumn<QList<int> >("sectionsToRemove");
    QTest::addColumn<QList<int> >("spanValues");
    QTest::addColumn<QList<int> >("spanCounts");
    QTest::addColumn<QList<int> >("expectedValues");

    QTest::newRow("one section, remove none")
        << 0
        << (QList<int>() << 0)
        << (QList<int>() << 1)
        << (QList<int>())
        << (QList<int>() << 1)
        << (QList<int>() << 1)
        << (QList<int>() << 1);

    QTest::newRow("one section, remove one")
        << 0
        << (QList<int>() << 0)
        << (QList<int>() << 1)
        << (QList<int>() << 0)
        << (QList<int>())
        << (QList<int>())
        << (QList<int>());

    QTest::newRow("ten sections, remove three")
        << 0
        << (QList<int>() << 2 << 5 << 7 << 8 << 9)
        << (QList<int>() << 10 << 100 << 1000 << 1000 << 1000)
        << (QList<int>() << 2 << 5 << 8)
        << (QList<int>() << 0 << 1000)
        << (QList<int>() << 5 << 2)
        << (QList<int>() << 0 << 0 << 0 << 0 << 0 << 1000 << 1000);

    QTest::newRow("ten sections, remove ten")
        << 0
        << (QList<int>() << 2 << 5 << 7 << 8 << 9)
        << (QList<int>() << 10 << 100 << 1000 << 1000 << 1000)
        << (QList<int>() << 0 << 1 << 2 << 3 << 4 << 5 << 6 << 7 << 8 << 9)
        << (QList<int>())
        << (QList<int>())
        << (QList<int>());
}

void tst_QSectionSpans::removeSectionsAt()
{
    QFETCH(int, empty);
    QFETCH(QList<int>, sectionIndexes);
    QFETCH(QList<int>, sectionValues);
    QFETCH(QList<int>, sectionsToRemove);
    QFETCH(QList<int>, spanValues);
    QFETCH(QList<int>, spanCounts);
    QFETCH(QList<int>, expectedValues);

    QtSectionSpans<int> spans;
    QCOMPARE(spans.count(), 0);

    int initialSectionCount = expectedValues.count() + sectionsToRemove.count();
    spans.addSections(empty, initialSectionCount);

    QCOMPARE(spans.sectionCount(), initialSectionCount);
    for (int i = 0; i < sectionIndexes.count(); ++i)
        spans.setValue(sectionIndexes.at(i), sectionValues.at(i));

    QCOMPARE(spans.sectionCount(), initialSectionCount);
    for (int j = sectionsToRemove.count() - 1; j >= 0; --j)
        spans.removeSectionsAt(sectionsToRemove.at(j), 1);

    QCOMPARE(spans.sectionCount(), expectedValues.count());
    for (int k = 0; k < spans.count(); ++k) {
        QCOMPARE(spans.at(k).value, spanValues.at(k));
        QCOMPARE(spans.at(k).count, uint(spanCounts.at(k)));
    }

    for (int l = 0; l < expectedValues.count(); ++l)
        QCOMPARE(spans.sectionValue(l, -1), expectedValues.at(l));
}

void tst_QSectionSpans::replaceAll_data()
{
}

void tst_QSectionSpans::replaceAll()
{
}

QTEST_MAIN(tst_QSectionSpans)
#include "tst_qsectionspans.moc"
