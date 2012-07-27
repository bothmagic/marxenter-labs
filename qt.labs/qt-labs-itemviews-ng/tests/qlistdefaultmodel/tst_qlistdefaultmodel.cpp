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
#include <qlistdefaultmodel.h>

class tst_QtListDefaultModel : public QObject
{
    Q_OBJECT

public:
    tst_QtListDefaultModel();
    virtual ~tst_QtListDefaultModel();

public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void appendItems_data();
    void appendItems();
    void insertItems_data();
    void insertItems();
    void removeItems_data();
    void removeItems();
    void moveItems_data();
    void moveItems();
    void setData_data();
    void setData();

protected:
    QtListDefaultModel *model;
};

Q_DECLARE_METATYPE(QList<int>)

tst_QtListDefaultModel::tst_QtListDefaultModel()
{
}

tst_QtListDefaultModel::~tst_QtListDefaultModel()
{
}

void tst_QtListDefaultModel::initTestCase()
{
}

void tst_QtListDefaultModel::cleanupTestCase()
{
}

void tst_QtListDefaultModel::init()
{
    model = new QtListDefaultModel();
}

void tst_QtListDefaultModel::cleanup()
{
    delete model;
}

void tst_QtListDefaultModel::getSetCheck()
{
}

void tst_QtListDefaultModel::appendItems_data()
{
    QTest::addColumn<QStringList>("appendItems");
    QTest::addColumn<QStringList>("expectedItems");
    QTest::addColumn<int>("signalCount");
    QTest::addColumn<QList<int> >("signalArgs");

    QTest::newRow("no items")
        << QStringList()
        << QStringList()
        << 0
        << QList<int>();

    QTest::newRow("one item")
        << (QStringList() << "one")
        << (QStringList() << "one")
        << 1
        << (QList<int>() << 0 << 1); // index, count

    QTest::newRow("three items")
        << (QStringList() << "one" << "two" << "three")
        << (QStringList() << "one" << "two" << "three")
        << 3
        << (QList<int>()
            << 0 << 1 // index, count
            << 1 << 1
            << 2 << 1);
}

void tst_QtListDefaultModel::appendItems()
{
    QFETCH(QStringList, appendItems);
    QFETCH(QStringList, expectedItems);
    QFETCH(int, signalCount);
    QFETCH(QList<int>, signalArgs);

    QSignalSpy itemsInserted(model, SIGNAL(itemsInserted(int,int)));

    for (int i = 0; i < appendItems.count(); ++i)
        model->appendItem(new QtListDefaultItem(appendItems.at(i)));

    QCOMPARE(model->count(), expectedItems.count());
    for (int j = 0; j < expectedItems.count(); ++j) {
        QtListDefaultItem *item = model->item(j);
        QVERIFY(!!item);
        QCOMPARE(item->text(), expectedItems.at(j));
    }

    QCOMPARE(itemsInserted.count(), signalCount);
    for (int k = 0; k < signalCount; ++k) {
        QList<QVariant> args = itemsInserted.at(k);
        for (int l = 0; l < args.count(); ++l) {
            int signalArgCount = signalArgs.count()/signalCount;
            int m = (k * signalArgCount) + l;
            QCOMPARE(args.at(l).toInt(), signalArgs.at(m));
        }
    }
}

void tst_QtListDefaultModel::insertItems_data()
{
    QTest::addColumn<QStringList>("initialItems");
    QTest::addColumn<int>("insertAt");
    QTest::addColumn<QStringList>("insertItems");
    QTest::addColumn<QStringList>("expectedItems");
    QTest::addColumn<int>("signalCount");
    QTest::addColumn<QList<int> >("signalArgs");

    QTest::newRow("no items")
        << QStringList()
        << 0
        << QStringList()
        << QStringList()
        << 0
        << QList<int>();

    QTest::newRow("no items, insert one")
        << QStringList()
        << 0
        << (QStringList() << "one")
        << (QStringList() << "one")
        << 1
        << (QList<int>() << 0 << 1); // index, count

    QTest::newRow("no items, insert one at -1")
        << QStringList()
        << -1
        << (QStringList() << "one")
        << (QStringList() << "one")
        << 1
        << (QList<int>() << 0 << 1);

    QTest::newRow("no items, insert one at INT_MAX")
        << QStringList()
        << INT_MAX
        << (QStringList() << "one")
        << (QStringList() << "one")
        << 1
        << (QList<int>() << 0 << 1);

    QTest::newRow("two items, insert one before")
        << (QStringList() << "two" << "three")
        << 0
        << (QStringList() << "one")
        << (QStringList() << "one" << "two" << "three")
        << 3
        << (QList<int>()
            << 0 << 1  // index, count
            << 1 << 1
            << 0 << 1);

    QTest::newRow("two items, insert one in the middle")
        << (QStringList() << "one" << "three")
        << 1
        << (QStringList() << "two")
        << (QStringList() << "one" << "two" << "three")
        << 3
        << (QList<int>()
            << 0 << 1
            << 1 << 1
            << 1 << 1);

    QTest::newRow("two items, insert one after")
        << (QStringList() << "one" << "two")
        << 2
        << (QStringList() << "three")
        << (QStringList() << "one" << "two" << "three")
        << 3
        << (QList<int>()
            << 0 << 1
            << 1 << 1
            << 2 << 1);
}

void tst_QtListDefaultModel::insertItems()
{
    QFETCH(QStringList, initialItems);
    QFETCH(int, insertAt);
    QFETCH(QStringList, insertItems);
    QFETCH(QStringList, expectedItems);
    QFETCH(int, signalCount);
    QFETCH(QList<int>, signalArgs);

    QSignalSpy itemsInserted(model, SIGNAL(itemsInserted(int,int)));

    for (int i = 0; i < initialItems.count(); ++i)
        model->appendItem(new QtListDefaultItem(initialItems.at(i)));
    QCOMPARE(model->count(), initialItems.count());

    for (int j = 0; j < insertItems.count(); ++j)
        model->insertItem(insertAt + j, new QtListDefaultItem(insertItems.at(j)));

    QCOMPARE(model->count(), expectedItems.count());
    for (int k = 0; k < expectedItems.count(); ++k) {
        QtListDefaultItem *item = model->item(k);
        QVERIFY(!!item);
        QCOMPARE(item->text(), expectedItems.at(k));
    }

    QCOMPARE(itemsInserted.count(), signalCount);
    for (int k = 0; k < signalCount; ++k) {
        QList<QVariant> args = itemsInserted.at(k);
        for (int l = 0; l < args.count(); ++l) {
            int signalArgCount = signalArgs.count()/signalCount;
            int m = (k * signalArgCount) + l;
            QCOMPARE(args.at(l).toInt(), signalArgs.at(m));
        }
    }
}

void tst_QtListDefaultModel::removeItems_data()
{
    QTest::addColumn<QStringList>("initialItems");
    QTest::addColumn<int>("removeAt");
    QTest::addColumn<int>("removeCount");
    QTest::addColumn<QStringList>("expectedItems");
    QTest::addColumn<int>("signalCount");
    QTest::addColumn<QList<int> >("signalArgs");

    QTest::newRow("no items")
        << QStringList()
        << 0
        << 0
        << QStringList()
        << 0
        << QList<int>();

    QTest::newRow("one item, remove one")
        << (QStringList() << "one")
        << 0
        << 1
        << QStringList()
        << 1
        << (QList<int>() << 0 << 1);

    QTest::newRow("two items, remove first")
        << (QStringList() << "one" << "two")
        << 0
        << 1
        << (QStringList() << "two")
        << 1
        << (QList<int>() << 0 << 1);

    QTest::newRow("two items, remove second")
        << (QStringList() << "one" << "two")
        << 1
        << 1
        << (QStringList() << "one")
        << 1
        << (QList<int>() << 1 << 1);

    QTest::newRow("three items, remove middle")
        << (QStringList() << "one" << "two" << "three")
        << 1
        << 1
        << (QStringList() << "one" << "three")
        << 1
        << (QList<int>() << 1 << 1);

    QTest::newRow("three items, remove first two")
        << (QStringList() << "one" << "two" << "three")
        << 0
        << 2
        << (QStringList() << "three")
        << 2
        << (QList<int>()
            << 0 << 1
            << 0 << 1);

    QTest::newRow("three items, remove last two")
        << (QStringList() << "one" << "two" << "three")
        << 1
        << 2
        << (QStringList() << "one")
        << 2
        << (QList<int>()
            << 1 << 1
            << 1 << 1);
}

void tst_QtListDefaultModel::removeItems()
{
    QFETCH(QStringList, initialItems);
    QFETCH(int, removeAt);
    QFETCH(int, removeCount);
    QFETCH(QStringList, expectedItems);
    QFETCH(int, signalCount);
    QFETCH(QList<int>, signalArgs);

    QSignalSpy itemsRemoved(model, SIGNAL(itemsRemoved(int,int)));

    for (int i = 0; i < initialItems.count(); ++i)
        model->appendItem(new QtListDefaultItem(initialItems.at(i)));
    QCOMPARE(model->count(), initialItems.count());

    for (int j = 0; j < removeCount; ++j)
        model->removeItem(removeAt);

    QCOMPARE(model->count(), expectedItems.count());
    for (int k = 0; k < expectedItems.count(); ++k) {
        QtListDefaultItem *item = model->item(k);
        QVERIFY(!!item);
        QCOMPARE(item->text(), expectedItems.at(k));
    }

    QCOMPARE(itemsRemoved.count(), signalCount);
     for (int k = 0; k < signalCount; ++k) {
        QList<QVariant> args = itemsRemoved.at(k);
        for (int l = 0; l < args.count(); ++l) {
            int signalArgCount = signalArgs.count()/signalCount;
            int m = (k * signalArgCount) + l;
            QCOMPARE(args.at(l).toInt(), signalArgs.at(m));
        }
    }
}

void tst_QtListDefaultModel::moveItems_data()
{
    QTest::addColumn<QStringList>("initialItems");
    QTest::addColumn<int>("moveFrom");
    QTest::addColumn<int>("moveTo");
    QTest::addColumn<int>("moveCount");
    QTest::addColumn<QStringList>("expectedItems");
    QTest::addColumn<int>("signalCount");
    QTest::addColumn<QList<int> >("signalArgs");

    QTest::newRow("no items")
        << QStringList()
        << 0
        << 0
        << 0
        << QStringList()
        << 0
        << QList<int>();

    QTest::newRow("three items, move one")
        << (QStringList() << "one" << "two" << "three")
        << 0
        << 1
        << 1
        << (QStringList() << "two" << "one" << "three")
        << 1
        << (QList<int>() << 0 << 1 << 1);

    QTest::newRow("three items, move two")
        << (QStringList() << "one" << "two" << "three")
        << 0
        << 1
        << 2
        << (QStringList() << "one" << "two" << "three")
        << 2
        << (QList<int>()
            << 0 << 1 << 1
            << 0 << 1 << 1);

    QTest::newRow("three items, move three back")
        << (QStringList() << "one" << "two" << "three")
        << 0
        << 2
        << 3
        << (QStringList() << "one" << "two" << "three")
        << 3
        << (QList<int>()
            << 0 << 2 << 1
            << 0 << 2 << 1
            << 0 << 2 << 1);

    QTest::newRow("three items, move three front")
        << (QStringList() << "one" << "two" << "three")
        << 2
        << 0
        << 3
        << (QStringList() << "one" << "two" << "three")
        << 3
        << (QList<int>()
            << 2 << 0 << 1
            << 2 << 0 << 1
            << 2 << 0 << 1);
}

void tst_QtListDefaultModel::moveItems()
{
    QFETCH(QStringList, initialItems);
    QFETCH(int, moveFrom);
    QFETCH(int, moveTo);
    QFETCH(int, moveCount);
    QFETCH(QStringList, expectedItems);
    QFETCH(int, signalCount);
    QFETCH(QList<int>, signalArgs);

    QSignalSpy itemsRemoved(model, SIGNAL(itemsMoved(int,int,int)));

    for (int i = 0; i < initialItems.count(); ++i)
        model->appendItem(new QtListDefaultItem(initialItems.at(i)));
    QCOMPARE(model->count(), initialItems.count());

    for (int j = 0; j < moveCount; ++j)
        model->moveItem(moveFrom, moveTo);

    QCOMPARE(model->count(), expectedItems.count());
    for (int k = 0; k < expectedItems.count(); ++k) {
        QtListDefaultItem *item = model->item(k);
        QVERIFY(!!item);
        QCOMPARE(item->text(), expectedItems.at(k));
    }

    QCOMPARE(itemsRemoved.count(), signalCount);
     for (int k = 0; k < signalCount; ++k) {
        QList<QVariant> args = itemsRemoved.at(k);
        for (int l = 0; l < args.count(); ++l) {
            int signalArgCount = signalArgs.count()/signalCount;
            int m = (k * signalArgCount) + l;
            QCOMPARE(args.at(l).toInt(), signalArgs.at(m));
        }
    }
}

void tst_QtListDefaultModel::setData_data()
{
    // ### FIXME : add data
}

void tst_QtListDefaultModel::setData()
{
    // ### FIXME: add test
}

QTEST_MAIN(tst_QtListDefaultModel)
#include "tst_qlistdefaultmodel.moc"
