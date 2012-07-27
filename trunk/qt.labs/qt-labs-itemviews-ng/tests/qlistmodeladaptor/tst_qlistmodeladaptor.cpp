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
#include <QtGui/qstandarditemmodel.h>

#include <qlistmodeladaptor.h>
#include <qdataroles_p.h>

class tst_QtListModelAdaptor : public QObject
{
    Q_OBJECT

public:
    tst_QtListModelAdaptor();
    virtual ~tst_QtListModelAdaptor();

public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void count_data();
    void count();
    void getData_data();
    void getData();
    void setData_data();
    void setData();

protected:
    QStandardItemModel *source;
    QtListModelAdaptor *adaptor;
};

Q_DECLARE_METATYPE(QList<int>)

tst_QtListModelAdaptor::tst_QtListModelAdaptor()
{
    qRegisterMetaType<QList<int> >();
}

tst_QtListModelAdaptor::~tst_QtListModelAdaptor()
{
}

void tst_QtListModelAdaptor::initTestCase()
{
    source = new QStandardItemModel();
    adaptor = new QtListModelAdaptor(source);
}

void tst_QtListModelAdaptor::cleanupTestCase()
{
    delete source;
    delete adaptor;
}

void tst_QtListModelAdaptor::init()
{
}

void tst_QtListModelAdaptor::cleanup()
{
    source->clear();
}

void tst_QtListModelAdaptor::count_data()
{
    QTest::addColumn<int>("itemCount");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no items") << 0 << 0;
    QTest::newRow("one items") << 1 << 1;
    QTest::newRow("two items") << 2 << 1;
    QTest::newRow("many items") << 10000 << 1;
}

void tst_QtListModelAdaptor::count()
{
    QFETCH(int, itemCount);
    QFETCH(int, signalCount);

    QSignalSpy itemsInserted(adaptor, SIGNAL(itemsInserted(int,int)));
    source->setRowCount(itemCount);
    QCOMPARE(adaptor->count(), itemCount);
    QCOMPARE(itemsInserted.count(), signalCount);
}

void tst_QtListModelAdaptor::getData_data()
{
    QTest::addColumn<QStringList>("items");
    QTest::addColumn<QByteArray>("role");
    QTest::addColumn<QStringList>("data");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no items")
            << QStringList()
            << QByteArray("DisplayRole")
            << QStringList()
            << 0;

    QTest::newRow("three items")
            << (QStringList() << "one" << "two" << "three")
            << QByteArray("DisplayRole")
            << (QStringList() << "four" << "five" << "six")
            << 3;
}

void tst_QtListModelAdaptor::getData()
{
    QFETCH(QStringList, items);
    QFETCH(QByteArray, role);
    QFETCH(QStringList, data);
    QFETCH(int, signalCount);

    QSignalSpy itemsChanged(adaptor, SIGNAL(itemsChanged(int,int,const QList<QByteArray>&)));

    for (int i = 0; i < items.count(); ++i)
        source->appendRow(new QStandardItem(items.at(i)));

    QCOMPARE(adaptor->count(), items.count());
    for (int j = 0; j < items.count(); ++j)
        QCOMPARE(adaptor->data(j, QList<QByteArray>() << role).value(role).toString(), items.at(j));

    for (int k = 0; k < data.count(); ++k)
        source->setData(source->index(k, 0), data.at(k), QtDataRoles::value(role));

    for (int l = 0; l < data.count(); ++l)
        QCOMPARE(adaptor->data(l, QList<QByteArray>() << role).value(role).toString(), data.at(l));

    QCOMPARE(itemsChanged.count(), signalCount);
}

void tst_QtListModelAdaptor::setData_data()
{
    QTest::addColumn<QStringList>("items");
    QTest::addColumn<QByteArray>("role");
    QTest::addColumn<QStringList>("data");
    QTest::addColumn<int>("signalCount");

    QTest::newRow("no items")
            << QStringList()
            << QByteArray("DisplayRole")
            << QStringList()
            << 0;

    QTest::newRow("three items")
            << (QStringList() << "one" << "two" << "three")
            << QByteArray("DisplayRole")
            << (QStringList() << "four" << "five" << "six")
            << 3;
}

void tst_QtListModelAdaptor::setData()
{
    QFETCH(QStringList, items);
    QFETCH(QByteArray, role);
    QFETCH(QStringList, data);
    QFETCH(int, signalCount);

    QSignalSpy itemsChanged(adaptor, SIGNAL(itemsChanged(int,int,const QList<QByteArray>&)));

    for (int i = 0; i < items.count(); ++i)
        source->appendRow(new QStandardItem(items.at(i)));

    QCOMPARE(adaptor->count(), items.count());
    for (int j = 0; j < items.count(); ++j)
        QCOMPARE(adaptor->data(j, QList<QByteArray>() << role).value(role).toString(), items.at(j));

    for (int k = 0; k < data.count(); ++k) {
        QHash<QByteArray, QVariant> hash;
        hash.insert(role, data.at(k));
        adaptor->setData(k, hash);
    }

    for (int l = 0; l < data.count(); ++l)
        QCOMPARE(source->data(source->index(l, 0), QtDataRoles::value(role)).toString(), data.at(l));

    QCOMPARE(itemsChanged.count(), signalCount);
}

QTEST_MAIN(tst_QtListModelAdaptor)

#include "tst_qlistmodeladaptor.moc"

