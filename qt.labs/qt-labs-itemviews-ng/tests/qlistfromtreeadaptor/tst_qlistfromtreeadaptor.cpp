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

#include <qlistfromtreeadaptor.h>
#include <qtreedefaultmodel.h>
#include <qdataroles_p.h>

class tst_QtListFromTreeAdaptor : public QObject
{
    Q_OBJECT

public:
    tst_QtListFromTreeAdaptor();
    virtual ~tst_QtListFromTreeAdaptor();

public slots:
    void initTestCase();
    void cleanupTestCase();

private slots:
    void count_data();
    void count();
    void parentCount_data();
    void parentCount();
    void dataCheck_data();
    void dataCheck();
    void rootCheck_data();
    void rootCheck();

protected:
    QtTreeDefaultModel *source;
    QtListFromTreeAdaptor *adaptor;
};

Q_DECLARE_METATYPE(QList<bool>)
Q_DECLARE_METATYPE(QList<QByteArray>)
Q_DECLARE_METATYPE(QtTreeModelBase::iterator_base)

tst_QtListFromTreeAdaptor::tst_QtListFromTreeAdaptor()
{
    qRegisterMetaType<QList<QByteArray> >("QList<QByteArray>");
    qRegisterMetaType<QtTreeModelBase::iterator_base>("const QtTreeModelBase::iterator_base&");
}

tst_QtListFromTreeAdaptor::~tst_QtListFromTreeAdaptor()
{
}

void tst_QtListFromTreeAdaptor::initTestCase()
{
    source = new QtTreeDefaultModel;
    adaptor = new QtListFromTreeAdaptor(source, source->begin());
}

void tst_QtListFromTreeAdaptor::cleanupTestCase()
{
    delete adaptor;
    delete source;
}

void tst_QtListFromTreeAdaptor::count_data()
{
    QTest::addColumn<int>("itemCount");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no items") << 0 << 0;
    QTest::newRow("one item") << 1 << 1;
    QTest::newRow("two items") << 2 << 2;
}

void tst_QtListFromTreeAdaptor::count()
{
    QFETCH(int, itemCount);
    QFETCH(int, signalCount);

    QSignalSpy sourceItemsInserted(source,
            SIGNAL(itemsInserted(const QtTreeModelBase::iterator_base&,int)));
    QSignalSpy sourceItemsRemoved(source,
            SIGNAL(itemsRemoved(const QtTreeModelBase::iterator_base&,int)));

    QSignalSpy adaptorItemsInserted(adaptor, SIGNAL(itemsInserted(int,int)));
    QSignalSpy adaptorItemsRemoved(adaptor, SIGNAL(itemsRemoved(int,int)));

    for (int x = 0; x < itemCount; ++x)
        new QtTreeDefaultItem(QString::number(x), source->rootItem());

    QCOMPARE(source->rootItem()->children().count(), itemCount);
    QCOMPARE(source->rootItem()->children().count(), adaptor->count());
    QCOMPARE(sourceItemsInserted.count(), signalCount);
    QCOMPARE(adaptorItemsInserted.count(), signalCount);

    foreach (QtTreeDefaultItem *item, source->rootItem()->children())
        source->rootItem()->removeChild(item);

    QCOMPARE(sourceItemsRemoved.count(), signalCount);
    QCOMPARE(adaptorItemsRemoved.count(), signalCount);
}

void tst_QtListFromTreeAdaptor::parentCount_data()
{
    QTest::addColumn<int>("parentItemCount");
    QTest::addColumn<int>("childrenItemCount");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("one parent item, one children item") << 1 << 1 << 1;
    QTest::newRow("two parent items, two children items") << 2 << 2 << 2;
}

void tst_QtListFromTreeAdaptor::parentCount()
{
    QFETCH(int, parentItemCount);
    QFETCH(int, childrenItemCount);
    QFETCH(int, signalCount);

    QSignalSpy sourceItemsInserted(source, SIGNAL(itemsInserted(const QtTreeModelBase::iterator_base&,int)));
    QSignalSpy adaptorItemsInserted(adaptor, SIGNAL(itemsInserted(int,int)));

    for (int x = 0; x < parentItemCount; ++x) {
        QtTreeDefaultItem *item = new QtTreeDefaultItem(QString::number(x), source->rootItem());
        for (int y = 0; y < childrenItemCount; ++y) {
            new QtTreeDefaultItem(QString::number(y), item);
        }
    }

    QCOMPARE(sourceItemsInserted.count(), (parentItemCount * childrenItemCount) + parentItemCount);
    QCOMPARE(adaptorItemsInserted.count(), signalCount);

    foreach (QtTreeDefaultItem *item, source->rootItem()->children())
        source->rootItem()->removeChild(item);
}

void tst_QtListFromTreeAdaptor::dataCheck_data()
{
    QTest::addColumn<int>("itemCount");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("one item") << 1 << 1;
    QTest::newRow("two items") << 2 << 2;
}

void tst_QtListFromTreeAdaptor::dataCheck()
{
    QFETCH(int, itemCount);
    QFETCH(int, signalCount);
    int x;

    QSignalSpy sourceItemsChanged(source, SIGNAL(itemsChanged(const QtTreeModelBase::iterator_base&, int, QList<QByteArray>)));
    QSignalSpy adaptorItemsChanged(adaptor, SIGNAL(itemsChanged(int, int, QList<QByteArray>)));

    for (x = 0; x < itemCount; x++) {
        new QtTreeDefaultItem(QString::number(x), source->rootItem());

        QCOMPARE(source->rootItem()->children().at(x)->data(0, QList<QByteArray>() << QByteArray("DisplayRole")),
                 adaptor->data(x, QList<QByteArray>() << QByteArray("DisplayRole")));

        source->rootItem()->children().at(x)->setData(QString("changed text"), 0, QByteArray("DisplayRole"));

        QCOMPARE(source->rootItem()->children().at(x)->data(0, QList<QByteArray>() << QByteArray("DisplayRole")),
                 adaptor->data(x, QList<QByteArray>() << QByteArray("DisplayRole")));
    }

    QCOMPARE(sourceItemsChanged.count(), signalCount);
    QCOMPARE(adaptorItemsChanged.count(), signalCount);

    foreach (QtTreeDefaultItem *item, source->rootItem()->children())
        source->rootItem()->removeChild(item);
}

void tst_QtListFromTreeAdaptor::rootCheck_data()
{
    QTest::addColumn<int>("itemCount");

    QTest::newRow("two items") << 2;
}

void tst_QtListFromTreeAdaptor::rootCheck()
{
    QFETCH(int, itemCount);

    QtTreeDefaultModel *source = new QtTreeDefaultModel;

    QSignalSpy sourceItemsInserted(source, SIGNAL(itemsInserted(const QtTreeModelBase::iterator_base&,int)));

    for (int x = 0; x < itemCount; ++x)
        new QtTreeDefaultItem(QString::number(x), source->rootItem());

    QtListFromTreeAdaptor *adaptor = new QtListFromTreeAdaptor(source, source->itemIterator(source->rootItem()->firstChild()));

    QSignalSpy adaptorItemsInserted(adaptor, SIGNAL(itemsInserted(int,int)));

    for (int y = 0; y < itemCount; ++y)
        new QtTreeDefaultItem(QString::number(y), source->rootItem()->firstChild());

    QCOMPARE(sourceItemsInserted.count(), itemCount * itemCount);
    QCOMPARE(adaptorItemsInserted.count(), itemCount);
}

QTEST_MAIN(tst_QtListFromTreeAdaptor)
#include <tst_qlistfromtreeadaptor.moc>
