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
#include <QtGui/QStyleOptionViewItemV4>
#include <QList>

#include <qgraphicslistview.h>
#include <qlistdefaultmodel.h>
#include <qlistcontroller.h>

class GraphicsListView : public QtGraphicsListView
{
public:
    GraphicsListView() { }
    void setController(QtListController *c) { QtGraphicsListView::setController(c); }
    void setModel(QtListModelInterface *m) { QtGraphicsListView::setModel(m); }
};

class GraphicsListViewItem : public QtGraphicsListViewItem
{
public:
    GraphicsListViewItem(int index, QtGraphicsListView *view) : QtGraphicsListViewItem(index, view) { }
    QSizeF sizeHint(int, const QStyleOptionViewItemV4 *, Qt::SizeHint, const QSizeF&) const { return QSize(20, 20); }
};

class tst_QtGraphicsListView : public QObject
{
    Q_OBJECT

public:
    tst_QtGraphicsListView();
    virtual ~tst_QtGraphicsListView();

public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void maximumOffset_data();
    void maximumOffset();
    void maximumFirstIndex_data();
    void maximumFirstIndex();
    void itemGeometry_data();
    void itemGeometry();
    void itemAt_data();
    void itemAt();
    void layout_data();
    void layout();

protected:
    GraphicsListView *view;
};

Q_DECLARE_METATYPE(QList<int>)
Q_DECLARE_METATYPE(QList<QRectF>)

tst_QtGraphicsListView::tst_QtGraphicsListView()
{
}

tst_QtGraphicsListView::~tst_QtGraphicsListView()
{
}

void tst_QtGraphicsListView::initTestCase()
{
}

void tst_QtGraphicsListView::cleanupTestCase()
{
}

void tst_QtGraphicsListView::init()
{
    view = new GraphicsListView();
}

void tst_QtGraphicsListView::cleanup()
{
    delete view;
}

void tst_QtGraphicsListView::getSetCheck()
{
    QCOMPARE(view->offset(), 0.); // default
    QCOMPARE(view->firstIndex(), 0); // default
    QCOMPARE(view->orientation(), Qt::Vertical); // default

    view->setFirstIndex(9);
    QCOMPARE(view->firstIndex(), 9);
    view->setFirstIndex(0);
    QCOMPARE(view->firstIndex(), 0);

    QCOMPARE(view->offset(), 0.);
    view->setOffset(0);
    QCOMPARE(view->offset(), 0.);
    view->setOffset(0.123);
    QCOMPARE(view->offset(), 0.123);
    view->setOffset(123.4);
    QCOMPARE(view->offset(), 123.4);

    QCOMPARE(view->orientation(), Qt::Vertical);
    view->setOrientation(Qt::Horizontal);
    QCOMPARE(view->orientation(), Qt::Horizontal);

    QVERIFY(view->model() == 0);
    QtListDefaultModel *model = new QtListDefaultModel(view);
    view->setModel(model);
    QCOMPARE(view->model(), model);

    // check that the model is not deleted
    QSignalSpy modelDeleted(model, SIGNAL(destroyed(QObject*)));
    view->setModel(0);
    QVERIFY(modelDeleted.isEmpty());
    view->setModel(model);

    QVERIFY(view->controller() == 0);
    QtListController *controller = new QtListController();
    view->setController(controller);
    QCOMPARE(view->controller(), controller);
    QCOMPARE(view->model(), model);

    // check that the controller is not deleted
    QSignalSpy controllerDeleted(controller, SIGNAL(destroyed(QObject*)));
    view->setController(0);
    QVERIFY(view->controller() == 0);
    QVERIFY(modelDeleted.isEmpty());
    QVERIFY(controllerDeleted.isEmpty());

    delete controller;
    delete model;
}

void tst_QtGraphicsListView::maximumOffset_data()
{
    QTest::addColumn<int>("count");
    QTest::addColumn<int>("orientation");
    QTest::addColumn<QSizeF>("size");
    QTest::addColumn<qreal>("maximumOffset");

    QTest::newRow("no items, vertical, all visible")
        << 0
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0.;

    QTest::newRow("one item, vertical, all visible")
        << 5
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0.;

    QTest::newRow("five items, vertical, all visible")
        << 5
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0.;

    QTest::newRow("six items, vertical")
        << 6
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 20.;

    QTest::newRow("ten items, vertical")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 100.;

    QTest::newRow("no items, horizontal, all visible")
        << 0
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0.;

    QTest::newRow("one item, horizontal, all visible")
        << 5
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0.;

    QTest::newRow("five items, horizontal, all visible")
        << 5
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0.;

    QTest::newRow("six items, horizontal")
        << 6
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 20.;

    QTest::newRow("ten items, horizontal")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 100.;
}

void tst_QtGraphicsListView::maximumOffset()
{
    QFETCH(int, count);
    QFETCH(int, orientation);
    QFETCH(QSizeF, size);
    QFETCH(qreal, maximumOffset);

    QtListDefaultModel model;
    for (int i = 0; i < count; ++i)
        model.insertItem(i, new QtListDefaultItem());

    view->setOrientation(static_cast<Qt::Orientation>(orientation));
    view->setGeometry(QRectF(QPointF(0, 0), size));
    view->setItemCreator(new QtGraphicsListViewItemCreator<GraphicsListViewItem>());
    view->setModel(&model);
    view->doLayout();

    QCOMPARE(view->maximumOffset(), maximumOffset);
}

void tst_QtGraphicsListView::maximumFirstIndex_data()
{
    QTest::addColumn<int>("count");
    QTest::addColumn<int>("orientation");
    QTest::addColumn<QSizeF>("size");
    QTest::addColumn<int>("maximumFirstIndex");

    QTest::newRow("no items, vertical, all visible")
        << 0
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0;

    QTest::newRow("one item, vertical, all visible")
        << 5
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0;

    QTest::newRow("five items, vertical, all visible")
        << 5
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0;

    QTest::newRow("six items, vertical")
        << 6
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 1;

    QTest::newRow("ten items, vertical")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 5;

    QTest::newRow("no items, horizontal, all visible")
        << 0
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0;

    QTest::newRow("one item, horizontal, all visible")
        << 5
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0;

    QTest::newRow("five items, horizontal, all visible")
        << 5
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0;

    QTest::newRow("six items, horizontal")
        << 6
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 1;

    QTest::newRow("ten items, horizontal")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 5;
}

void tst_QtGraphicsListView::maximumFirstIndex()
{
    QFETCH(int, count);
    QFETCH(int, orientation);
    QFETCH(QSizeF, size);
    QFETCH(int, maximumFirstIndex);

    QtListDefaultModel model;
    for (int i = 0; i < count; ++i)
        model.insertItem(i, new QtListDefaultItem());

    view->setOrientation(static_cast<Qt::Orientation>(orientation));
    view->setGeometry(QRectF(QPointF(0, 0), size));
    view->setItemCreator(new QtGraphicsListViewItemCreator<GraphicsListViewItem>());
    view->setModel(&model);
    view->doLayout();

    QCOMPARE(view->maximumFirstIndex(), maximumFirstIndex);
}

void tst_QtGraphicsListView::itemGeometry_data()
{
    QTest::addColumn<int>("count");
    QTest::addColumn<int>("orientation");
    QTest::addColumn<QSizeF>("viewSize");
    QTest::addColumn<int>("firstIndex");
    QTest::addColumn<qreal>("offset");
    QTest::addColumn<QList<QRectF> >("geometries");

    QTest::newRow("no items, vertical")
        << 0
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QList<QRectF>();

    QTest::newRow("one item, vertical, all visible")
        << 1
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20));

    QTest::newRow("five items, vertical, all visible")
        << 5
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("ten items, vertical, first index five")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 5
        << 0.
        << (QList<QRectF>()
            << QRectF()
            << QRectF()
            << QRectF()
            << QRectF()
            << QRectF()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("ten items, vertical, offset")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 100.
        << (QList<QRectF>()
            << QRectF(0, -100, 20, 20)
            << QRectF(0, -80, 20, 20)
            << QRectF(0, -60, 20, 20)
            << QRectF(0, -40, 20, 20)
            << QRectF(0, -20, 20, 20)
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("no items, horizontal")
        << 0
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<QRectF>());

    QTest::newRow("one item, horizontal, all visible")
        << 1
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20));

    QTest::newRow("five items, horizontal, all visible")
        << 5
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(20, 0, 20, 20)
            << QRectF(40, 0, 20, 20)
            << QRectF(60, 0, 20, 20)
            << QRectF(80, 0, 20, 20));

    QTest::newRow("ten items, horizontal, first index five")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 5
        << 0.
        << (QList<QRectF>()
            << QRectF()
            << QRectF()
            << QRectF()
            << QRectF()
            << QRectF()
            << QRectF(0, 0, 20, 20)
            << QRectF(20, 0, 20, 20)
            << QRectF(40, 0, 20, 20)
            << QRectF(60, 0, 20, 20)
            << QRectF(80, 0, 20, 20));

    QTest::newRow("ten items, horizontal, offset")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 100.
        << (QList<QRectF>()
            << QRectF(-100, 0, 20, 20)
            << QRectF(-80, 0, 20, 20)
            << QRectF(-60, 0, 20, 20)
            << QRectF(-40, 0, 20, 20)
            << QRectF(-20, 0, 20, 20)
            << QRectF(0, 0, 20, 20)
            << QRectF(20, 0, 20, 20)
            << QRectF(40, 0, 20, 20)
            << QRectF(60, 0, 20, 20)
            << QRectF(80, 0, 20, 20));

}

void tst_QtGraphicsListView::itemGeometry()
{
    QFETCH(int, count);
    QFETCH(int, orientation);
    QFETCH(QSizeF, viewSize);
    QFETCH(int, firstIndex);
    QFETCH(qreal, offset);
    QFETCH(QList<QRectF>, geometries);

    QtListDefaultModel model;
    for (int i = 0; i < count; ++i)
        model.insertItem(i, new QtListDefaultItem());

    view->setOrientation(static_cast<Qt::Orientation>(orientation));
    view->setGeometry(QRectF(QPointF(0, 0), viewSize));
    view->setItemCreator(new QtGraphicsListViewItemCreator<GraphicsListViewItem>());
    view->setModel(&model);
    view->setFirstIndex(firstIndex);
    view->setOffset(offset);
    view->doLayout();

    for (int index = 0; index < geometries.count(); ++index)
        QCOMPARE(geometries.at(index), view->itemGeometry(index));
}

void tst_QtGraphicsListView::itemAt_data()
{
    QTest::addColumn<int>("count");
    QTest::addColumn<int>("orientation");
    QTest::addColumn<QSizeF>("size");
    QTest::addColumn<int>("firstIndex");
    QTest::addColumn<qreal>("offset");
    QTest::addColumn<QPointF>("position");
    QTest::addColumn<int>("expectedIndex");

    QTest::newRow("no items, vertical")
        << 0
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QPointF()
        << -1;

    QTest::newRow("one item, vertical, all visible")
        << 1
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QPointF(50, 10)
        << 0;

    QTest::newRow("five items, vertical, all visible")
        << 5
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QPointF(50, 90)
        << 4;

    QTest::newRow("ten items, vertical, first index five")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 5
        << 0.
        << QPointF(50, 90)
        << 9;

    QTest::newRow("ten items, vertical, offset")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 100.
        << QPointF(50, 90)
        << 9;

    QTest::newRow("no items, horizontal")
        << 0
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QPointF()
        << -1;

    QTest::newRow("one item, horizontal, all visible")
        << 1
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QPointF(10, 50)
        << 0;

    QTest::newRow("five items, horizontal, all visible")
        << 5
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QPointF(90, 50)
        << 4;

    QTest::newRow("ten items, horizontal, first index five")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 5
        << 0.
        << QPointF(90, 50)
        << 9;

    QTest::newRow("ten items, horizontal, offset")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 100.
        << QPointF(90, 50)
        << 9;

}

void tst_QtGraphicsListView::itemAt()
{
    QFETCH(int, count);
    QFETCH(int, orientation);
    QFETCH(QSizeF, size);
    QFETCH(int, firstIndex);
    QFETCH(qreal, offset);
    QFETCH(QPointF, position);
    QFETCH(int, expectedIndex);

    QtListDefaultModel model;
    for (int i = 0; i < count; ++i)
        model.insertItem(i, new QtListDefaultItem());

    view->setOrientation(static_cast<Qt::Orientation>(orientation));
    view->setGeometry(QRectF(QPointF(0, 0), size));
    view->setItemCreator(new QtGraphicsListViewItemCreator<GraphicsListViewItem>());
    view->setModel(&model);
    view->setFirstIndex(firstIndex);
    view->setOffset(offset);
    view->doLayout();

    QCOMPARE(view->itemAt(position), expectedIndex);
}

void tst_QtGraphicsListView::layout_data()
{
    QTest::addColumn<int>("count");
    QTest::addColumn<int>("orientation");
    QTest::addColumn<QSizeF>("viewSize");
    QTest::addColumn<int>("firstIndex");
    QTest::addColumn<qreal>("offset");
    QTest::addColumn<QList<int> >("expectedIndexes");
    QTest::addColumn<QList<QRectF> >("expectedGeometries");

    QTest::newRow("no items, vertical")
        << 0
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QList<int>()
        << QList<QRectF>();

    QTest::newRow("one item, vertical, all visible")
        << 1
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<int>() << 0)
        << (QList<QRectF>() << QRectF(0, 0, 20, 20));

    QTest::newRow("five items, vertical, all visible")
        << 5
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<int>()
            << 0 << 1 << 2 << 3 << 4)
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("ten items, vertical, five visible")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<int>()
            << 0 << 1 << 2 << 3 << 4)
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("ten items, vertical, five visible, first item five")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 5
        << 0.
        << (QList<int>()
            << 5 << 6 << 7 << 8 << 9)
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("ten items, vertical, five visible, offset")
        << 10
        << int(Qt::Vertical)
        << QSizeF(100, 100)
        << 0
        << 10.
        << (QList<int>()
            << 0 << 1 << 2 << 3 << 4 << 5)
        << (QList<QRectF>()
            << QRectF(0, -10, 20, 20)
            << QRectF(0, 10, 20, 20)
            << QRectF(0, 30, 20, 20)
            << QRectF(0, 50, 20, 20)
            << QRectF(0, 70, 20, 20)
            << QRectF(0, 90, 20, 20));

     QTest::newRow("no items, horizontal")
        << 0
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << QList<int>()
        << QList<QRectF>();

    QTest::newRow("one item, horizontal, all visible")
        << 1
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<int>() << 0)
        << (QList<QRectF>() << QRectF(0, 0, 20, 20));

    QTest::newRow("five items, horizontal, all visible")
        << 5
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<int>()
            << 0 << 1 << 2 << 3 << 4)
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(20, 0, 20, 20)
            << QRectF(40, 0, 20, 20)
            << QRectF(60, 0, 20, 20)
            << QRectF(80, 0, 20, 20));

    QTest::newRow("ten items, horizontal, five visible")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 0.
        << (QList<int>()
            << 0 << 1 << 2 << 3 << 4)
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(20, 0, 20, 20)
            << QRectF(40, 0, 20, 20)
            << QRectF(60, 0, 20, 20)
            << QRectF(80, 0, 20, 20));

    QTest::newRow("ten items, horizontal, five visible, first item five")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 5
        << 0.
        << (QList<int>()
            << 5 << 6 << 7 << 8 << 9)
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(20, 0, 20, 20)
            << QRectF(40, 0, 20, 20)
            << QRectF(60, 0, 20, 20)
            << QRectF(80, 0, 20, 20));

    QTest::newRow("ten items, horizontal, five visible, offset")
        << 10
        << int(Qt::Horizontal)
        << QSizeF(100, 100)
        << 0
        << 10.
        << (QList<int>()
            << 0 << 1 << 2 << 3 << 4 << 5)
        << (QList<QRectF>()
            << QRectF(-10, 0, 20, 20)
            << QRectF(10, 0, 20, 20)
            << QRectF(30, 0, 20, 20)
            << QRectF(50, 0, 20, 20)
            << QRectF(70, 0, 20, 20)
            << QRectF(90, 0, 20, 20));
}

void tst_QtGraphicsListView::layout()
{
    QFETCH(int, count);
    QFETCH(int, orientation);
    QFETCH(QSizeF, viewSize);
    QFETCH(int, firstIndex);
    QFETCH(qreal, offset);
    QFETCH(QList<int>, expectedIndexes);
    QFETCH(QList<QRectF>, expectedGeometries);

    QtListDefaultModel model;
    for (int i = 0; i < count; ++i)
        model.insertItem(i, new QtListDefaultItem());

    view->setOrientation(static_cast<Qt::Orientation>(orientation));
    view->setGeometry(QRectF(QPointF(0, 0), viewSize));
    view->setItemCreator(new QtGraphicsListViewItemCreator<GraphicsListViewItem>());
    view->setModel(&model);
    view->setFirstIndex(firstIndex);
    view->setOffset(offset);
    view->doLayout();

    for (int j = 0; j < expectedIndexes.count(); ++j) {
        QGraphicsItem *item = view->itemForIndex(expectedIndexes.at(j));
        QVERIFY(item);
        QVERIFY(item->isWidget());
        QCOMPARE(static_cast<QtGraphicsListViewItem*>(item)->index(), expectedIndexes.at(j));
        QCOMPARE(static_cast<QtGraphicsListViewItem*>(item)->geometry(), expectedGeometries.at(j));
    }
}

QTEST_MAIN(tst_QtGraphicsListView)
#include "tst_qgraphicslistview.moc"
