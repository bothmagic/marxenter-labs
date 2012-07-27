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
#include <qtreemodelinterface.h>
#include <qtreedefaultmodel.h>
#include <qdataroles_p.h>

class tst_QtTreeDefaultModel : public QObject
{
    Q_OBJECT

public:
    tst_QtTreeDefaultModel();
    virtual ~tst_QtTreeDefaultModel();
    
public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void insertChild_data();
    void insertChild();
    void appendChild_data();
    void appendChild();
    void takeChild_data();
    void takeChild();
    void removeChild_data();
    void removeChild();
    void setData_data();
    void setData();

protected:
    QtTreeDefaultModel *model;
};

Q_DECLARE_METATYPE(QList<int>)
Q_DECLARE_METATYPE(QList<bool>)
Q_DECLARE_METATYPE(QList<QByteArray>)
Q_DECLARE_METATYPE(QtTreeModelBase::iterator_base)

tst_QtTreeDefaultModel::tst_QtTreeDefaultModel()
{
    qRegisterMetaType<QtTreeModelBase::iterator_base>("QtTreeModelBase::iterator_base");
}

tst_QtTreeDefaultModel::~tst_QtTreeDefaultModel()
{
}

void tst_QtTreeDefaultModel::initTestCase()
{
}

void tst_QtTreeDefaultModel::cleanupTestCase()
{
}

void tst_QtTreeDefaultModel::init()
{
    model = new QtTreeDefaultModel;
}

void tst_QtTreeDefaultModel::cleanup()
{
    delete model;
}

void tst_QtTreeDefaultModel::getSetCheck()
{
}

void tst_QtTreeDefaultModel::insertChild_data()
{
    QTest::addColumn<QStringList>("initialChildren");
    QTest::addColumn<QStringList>("insertChildren");
    QTest::addColumn<int>("insertChildrenAt");
    QTest::addColumn<QStringList>("expectedChildren");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no children")
            << QStringList()
            << QStringList()
            << 0
            << QStringList()
            << 0;

    QTest::newRow("one initial child")
            << (QStringList() << "one")
            << QStringList()
            << 0
            << (QStringList() << "one")
            << 1;

    QTest::newRow("two initial children")
            << (QStringList() << "one" << "two")
            << QStringList()
            << 0
            << (QStringList() << "one" << "two")
            << 2;

     QTest::newRow("no initial children, one insert")
            << QStringList()
            << (QStringList() << "one")
            << 0
            << (QStringList() << "one")
            << 1;

     QTest::newRow("no initial children, one insert at illegal index")
            << QStringList()
            << (QStringList() << "one")
            << -1
            << (QStringList() << "one")
            << 1;

     QTest::newRow("two initial children, two inserts before")
            << (QStringList() << "three" << "four")
            << (QStringList() << "one" << "two")
            << 0
            << (QStringList() << "one" << "two" << "three" << "four")
            << 4;

     QTest::newRow("two initial children, two inserts after")
            << (QStringList() << "one" << "two")
            << (QStringList() << "three" << "four")
            << 2
            << (QStringList() << "one" << "two" << "three" << "four")
            << 4;

      QTest::newRow("two initial children, two inserts in the middle")
            << (QStringList() << "one" << "four")
            << (QStringList() << "two" << "three")
            << 1
            << (QStringList() << "one" << "two" << "three" << "four")
            << 4;
}

void tst_QtTreeDefaultModel::insertChild()
{
    QFETCH(QStringList, initialChildren);
    QFETCH(QStringList, insertChildren);
    QFETCH(int, insertChildrenAt);
    QFETCH(QStringList, expectedChildren);
    QFETCH(int, signalCount);

    Q_UNUSED(signalCount);
    //QSignalSpy itemsInserted(model, SIGNAL(itemsInserted(QtTreeModelBase::iterator_base&,int)));

    QVERIFY(model->rootItem());

    // add initial children
    for (int i = 0; i < initialChildren.count(); ++i)
        new QtTreeDefaultItem(initialChildren.at(i), model->rootItem());
    QCOMPARE(model->rootItem()->children().count(), initialChildren.count());

    // check children
    QtTreeDefaultItem *previous = 0;
    for (int j = 0; j < initialChildren.count(); ++j) {
        QtTreeDefaultItem *item = model->rootItem()->children().at(j);
        QCOMPARE(item->text(), initialChildren.at(j));
        QCOMPARE(item->previous(), previous);
        if (previous)
            QCOMPARE(previous->next(), item);
        previous = item;
    }

    // insert children
    for (int k = 0;  k < insertChildren.count(); ++k)
        model->rootItem()->insertChild(insertChildrenAt + k, new QtTreeDefaultItem(insertChildren.at(k)));

    // check children
    previous = 0;
    QCOMPARE(model->rootItem()->children().count(), expectedChildren.count());
    for (int l = 0; l < expectedChildren.count(); ++l) {
        QtTreeDefaultItem *item = model->rootItem()->children().at(l);
        QCOMPARE(item->text(), expectedChildren.at(l));
        QCOMPARE(item->previous(), previous);
        if (previous)
            QCOMPARE(previous->next(), item);
        previous = item;
    }

    // check signals
    //QCOMPARE(itemsInserted.count(), signalCount);
}

void tst_QtTreeDefaultModel::appendChild_data()
{
    QTest::addColumn<QStringList>("initialChildren");
    QTest::addColumn<QStringList>("appendChildren");
    QTest::addColumn<QStringList>("expectedChildren");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no children")
            << QStringList()
            << QStringList()
            << QStringList()
            << 0;

    QTest::newRow("one initial child")
            << (QStringList() << "one")
            << QStringList()
            << (QStringList() << "one")
            << 1;

    QTest::newRow("two initial children")
            << (QStringList() << "one" << "two")
            << QStringList()
            << (QStringList() << "one" << "two")
            << 2;

    QTest::newRow("no initial children, one append")
            << QStringList()
            << (QStringList() << "one")
            << (QStringList() << "one")
            << 1;

    QTest::newRow("one initial child, one append")
            << (QStringList() << "one")
            << (QStringList() << "two")
            << (QStringList() << "one" << "two")
            << 2;

    QTest::newRow("two initial children, one append")
            << (QStringList() << "one" << "two")
            << (QStringList() << "three")
            << (QStringList() << "one" << "two" << "three")
            << 3;

     QTest::newRow("one initial child, two appends")
            << (QStringList() << "one")
            << (QStringList() << "two" << "three")
            << (QStringList() << "one" << "two" << "three")
            << 2;
}

void tst_QtTreeDefaultModel::appendChild()
{
    QFETCH(QStringList, initialChildren);
    QFETCH(QStringList, appendChildren);
    QFETCH(QStringList, expectedChildren);
    QFETCH(int, signalCount);

    Q_UNUSED(signalCount);
    //QSignalSpy itemsInserted(model, SIGNAL(itemsInserted(QtTreeModelBase::iterator_base&,int)));

    QVERIFY(model->rootItem());

    // add initial children
    for (int i = 0; i < initialChildren.count(); ++i)
        new QtTreeDefaultItem(initialChildren.at(i), model->rootItem());
    QCOMPARE(model->rootItem()->children().count(), initialChildren.count());

    // check children
    QtTreeDefaultItem *previous = 0;
    for (int j = 0; j < initialChildren.count(); ++j) {
        QtTreeDefaultItem *item = model->rootItem()->children().at(j);
        QCOMPARE(item->text(), initialChildren.at(j));
        QCOMPARE(item->previous(), previous);
        if (previous)
            QCOMPARE(previous->next(), item);
        previous = item;
    }

    // append children
    for (int k = 0;  k < appendChildren.count(); ++k)
        model->rootItem()->appendChild(new QtTreeDefaultItem(appendChildren.at(k)));

    // check children
    previous = 0;
    QCOMPARE(model->rootItem()->children().count(), expectedChildren.count());
    for (int l = 0; l < expectedChildren.count(); ++l) {
        QtTreeDefaultItem *item = model->rootItem()->children().at(l);
        QCOMPARE(model->rootItem()->children().at(l)->text(), expectedChildren.at(l));
        QCOMPARE(item->previous(), previous);
        if (previous)
            QCOMPARE(previous->next(), item);
        previous = item;
    }

    // check signals
    //QCOMPARE(itemsInserted.count(), signalCount);
}

void tst_QtTreeDefaultModel::takeChild_data()
{
    QTest::addColumn<QStringList>("initialChildren");
    QTest::addColumn<int>("takeChildrenAt");
    QTest::addColumn<int>("takeChildrenCount");
    QTest::addColumn<QList<bool> >("takeChildrenExist");
    QTest::addColumn<QStringList>("expectedChildren");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no children")
        << QStringList()
        << 0
        << 0
        << QList<bool>()
        << QStringList()
        << 0;

    QTest::newRow("no children, take one")
        << QStringList()
        << 0
        << 1
        << (QList<bool>() << false)
        << QStringList()
        << 0;

    QTest::newRow("one child, take one")
        << (QStringList() << "one")
        << 0
        << 1
        << (QList<bool>() << true)
        << QStringList()
        << 1;

    QTest::newRow("one child, take two")
        << (QStringList() << "one")
        << 0
        << 1
        << (QList<bool>() << true << false)
        << QStringList()
        << 1;

    QTest::newRow("three children, take first")
        << (QStringList() << "one" << "two" << "three")
        << 0
        << 1
        << (QList<bool>() << true)
        << (QStringList() << "two" << "three")
        << 1;

   QTest::newRow("three children, take middle")
        << (QStringList() << "one" << "two" << "three")
        << 1
        << 1
        << (QList<bool>() << true)
        << (QStringList() << "one" << "three")
        << 1;

    QTest::newRow("three children, take last")
        << (QStringList() << "one" << "two" << "three")
        << 2
        << 1
        << (QList<bool>() << true)
        << (QStringList() << "one" << "two")
        << 1;

    QTest::newRow("two children, take -1")
        << (QStringList() << "one" << "two")
        << -1
        << 1
        << (QList<bool>() << false)
        << (QStringList() << "one" << "two")
        << 0;

    QTest::newRow("two children, take INT_MAX")
        << (QStringList() << "one" << "two")
        << INT_MAX
        << 1
        << (QList<bool>() << false)
        << (QStringList() << "one" << "two")
        << 0;
}

void tst_QtTreeDefaultModel::takeChild()
{
    QFETCH(QStringList, initialChildren);
    QFETCH(int, takeChildrenAt);
    QFETCH(int, takeChildrenCount);
    QFETCH(QList<bool>, takeChildrenExist);
    QFETCH(QStringList, expectedChildren);
    QFETCH(int, signalCount);

    Q_UNUSED(signalCount);
    //QSignalSpy itemsRemoved(model, SIGNAL(itemsInserted(QtTreeModelBase::iterator_base&,int)));

    QVERIFY(model->rootItem());

    // add initial children
    for (int i = 0; i < initialChildren.count(); ++i)
        new QtTreeDefaultItem(initialChildren.at(i), model->rootItem());
    QCOMPARE(model->rootItem()->children().count(), initialChildren.count());

    // check children
    QtTreeDefaultItem *previous = 0;
    for (int j = 0; j < initialChildren.count(); ++j) {
        QtTreeDefaultItem *item = model->rootItem()->children().at(j);
        QCOMPARE(item->text(), initialChildren.at(j));
        QCOMPARE(item->previous(), previous);
        if (previous)
            QCOMPARE(previous->next(), item);
        previous = item;
    }

    // take children
    for (int k = 0;  k < takeChildrenCount; ++k) {
        QtTreeDefaultItem *item = model->rootItem()->takeChild(takeChildrenAt);
        QCOMPARE(!!item, takeChildrenExist.at(k));
        delete item;
    }

    // check children
    previous = 0;
    QCOMPARE(model->rootItem()->children().count(), expectedChildren.count());
    for (int l = 0; l < expectedChildren.count(); ++l) {
        QtTreeDefaultItem *item = model->rootItem()->children().at(l);
        QCOMPARE(model->rootItem()->children().at(l)->text(), expectedChildren.at(l));
        QCOMPARE(item->previous(), previous);
        if (previous)
            QCOMPARE(previous->next(), item);
        previous = item;
    }

    // check signals
    //QCOMPARE(itemsRemoved.count(), signalCount);
}

void tst_QtTreeDefaultModel::removeChild_data()
{
    QTest::addColumn<QStringList>("initialChildren");
    QTest::addColumn<int>("removeChildrenAt");
    QTest::addColumn<int>("removeChildrenCount");
    QTest::addColumn<QStringList>("expectedChildren");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no children")
        << QStringList()
        << 0
        << 0
        << QStringList()
        << 0;

    QTest::newRow("no children, remove one")
        << QStringList()
        << 0
        << 1
        << QStringList()
        << 0;

    QTest::newRow("one child, remove one")
        << (QStringList() << "one")
        << 0
        << 1
        << QStringList()
        << 1;

    QTest::newRow("one child, remove two")
        << (QStringList() << "one")
        << 0
        << 1
        << QStringList()
        << 1;

    QTest::newRow("three children, remove first")
        << (QStringList() << "one" << "two" << "three")
        << 0
        << 1
        << (QStringList() << "two" << "three")
        << 1;

   QTest::newRow("three children, remove middle")
        << (QStringList() << "one" << "two" << "three")
        << 1
        << 1
        << (QStringList() << "one" << "three")
        << 1;

    QTest::newRow("three children, remove last")
        << (QStringList() << "one" << "two" << "three")
        << 2
        << 1
        << (QStringList() << "one" << "two")
        << 1;

    QTest::newRow("two children, remove -1")
        << (QStringList() << "one" << "two")
        << -1
        << 1
        << (QStringList() << "one" << "two")
        << 0;

    QTest::newRow("two children, remove INT_MAX")
        << (QStringList() << "one" << "two")
        << INT_MAX
        << 1
        << (QStringList() << "one" << "two")
        << 0;
}

void tst_QtTreeDefaultModel::removeChild()
{
    QFETCH(QStringList, initialChildren);
    QFETCH(int, removeChildrenAt);
    QFETCH(int, removeChildrenCount);
    QFETCH(QStringList, expectedChildren);
    QFETCH(int, signalCount);

    Q_UNUSED(signalCount);
    //QSignalSpy itemsRemoved(model, SIGNAL(itemsInserted(QtTreeModelBase::iterator_base&,int)));

    QVERIFY(model->rootItem());

    // add initial children
    for (int i = 0; i < initialChildren.count(); ++i)
        new QtTreeDefaultItem(initialChildren.at(i), model->rootItem());
    QCOMPARE(model->rootItem()->children().count(), initialChildren.count());

    // check children
    QtTreeDefaultItem *previous = 0;
    for (int j = 0; j < initialChildren.count(); ++j) {
        QtTreeDefaultItem *item = model->rootItem()->children().at(j);
        QCOMPARE(item->text(), initialChildren.at(j));
        QCOMPARE(item->previous(), previous);
        if (previous)
            QCOMPARE(previous->next(), item);
        previous = item;
    }

    // remove children
    for (int k = 0;  k < removeChildrenCount; ++k) {
        QtTreeDefaultItem *item = model->rootItem()->children().value(removeChildrenAt);
        model->rootItem()->removeChild(item);
        delete item;
    }

    // check children
    previous = 0;
    QCOMPARE(model->rootItem()->children().count(), expectedChildren.count());
    for (int l = 0; l < expectedChildren.count(); ++l) {
        QtTreeDefaultItem *item = model->rootItem()->children().at(l);
        QCOMPARE(model->rootItem()->children().at(l)->text(), expectedChildren.at(l));
        QCOMPARE(item->previous(), previous);
        if (previous)
            QCOMPARE(previous->next(), item);
        previous = item;
    }

    // check signals
    //QCOMPARE(itemsRemoved.count(), signalCount);
}

void tst_QtTreeDefaultModel::setData_data()
{
    QTest::addColumn<QStringList>("initialChildren");
    QTest::addColumn<QList<int> >("columns");
    QTest::addColumn<QList<QByteArray> >("roles");
    QTest::addColumn<QList<QVariant> >("data");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no children")
        << QStringList()
        << QList<int>()
        << QList<QByteArray>()
        << QList<QVariant>()
        << 0;

    QTest::newRow("one child, no data")
        << (QStringList() << "one")
        << QList<int>()
        << QList<QByteArray>()
        << QList<QVariant>()
        << 0;

    QTest::newRow("one child, one column, one text")
        << (QStringList() << "one")
        << (QList<int>() << 0)
        << (QList<QByteArray>() << "DisplayRole")
        << (QList<QVariant>() << QVariant("text"))
        << 1;

    QTest::newRow("one child, two columns, one role")
        << (QStringList() << "one")
        << (QList<int>() << 0 << 1)
        << (QList<QByteArray>() << "DisplayRole")
        << (QList<QVariant>() << QVariant("text") << QVariant("text"))
        << 2;

    QTest::newRow("one child, one column, two roles")
        << (QStringList() << "one")
        << (QList<int>() << 0)
        << (QList<QByteArray>() << "DisplayRole" << "EditRole")
        << (QList<QVariant>() << QVariant("text") << QVariant("edit"))
        << 2;

    QTest::newRow("one child, two columns, two roles")
        << (QStringList() << "one")
        << (QList<int>() << 0 << 1)
        << (QList<QByteArray>() << "DisplayRole" << "EditRole")
        << (QList<QVariant>() // for each column and for each role
            << QVariant("text") << QVariant("edit")
            << QVariant("text") << QVariant("edit"))
        << 2;
}

void tst_QtTreeDefaultModel::setData()
{
    QFETCH(QStringList, initialChildren);
    QFETCH(QList<int>, columns);
    QFETCH(QList<QByteArray>, roles);
    QFETCH(QList<QVariant>, data);
    QFETCH(int, signalCount);

    Q_UNUSED(signalCount);
    //QSignalSpy itemsChanged(model, SIGNAL(itemsInserted(QtTreeModelBase::iterator_base&,int,const QList<int>&)));

    QVERIFY(model->rootItem());

    // add initial children
    for (int i = 0; i < initialChildren.count(); ++i)
        new QtTreeDefaultItem(initialChildren.at(i), model->rootItem());
    QCOMPARE(model->rootItem()->children().count(), initialChildren.count());

    // change data and check
    for (int j = 0;  j < model->rootItem()->children().count(); ++j) {
        QtTreeDefaultItem *item = model->rootItem()->children().value(j);
        item->setColumnCount(columns.count());
        QCOMPARE(item->columnCount(), columns.count());
        for (int k = 0; k < columns.count(); ++k) {
            for (int l = 0; l < roles.count(); ++l) {
                int m = k * roles.count() + l;
                item->setData(data.at(m), columns.at(k), roles.at(l));
                QCOMPARE(item->data(columns.at(k), roles.at(l)), data.at(m));
            }
        }
    }

    // check signals
    //QCOMPARE(itemsChanged.count(), signalCount);
}

QTEST_MAIN(tst_QtTreeDefaultModel)
#include "tst_qtreedefaultmodel.moc"
