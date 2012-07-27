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
#include <qgraphicstreeview.h>
#include <qtreedefaultmodel.h>

class GraphicsTreeView : public QtGraphicsTreeView
{
public:
    GraphicsTreeView() { }
    void setController(QtTreeController *c) { QtGraphicsTreeView::setController(c); }
    void setModel(QtTreeModelBase *m) { QtGraphicsTreeView::setModel(m); }
};

class GraphicsTreeViewItem : public QtGraphicsTreeViewItem
{
public:
    GraphicsTreeViewItem(const QtTreeModelIterator &it, QtGraphicsTreeView *view) : QtGraphicsTreeViewItem(it, view) { }
    QSizeF sizeHint(const QtTreeModelIterator&, const QStyleOptionViewItemV4 *, Qt::SizeHint, const QSizeF&) const { return QSize(20, 20); }
};

class tst_QtGraphicsTreeView : public QObject
{
    Q_OBJECT
public:
    tst_QtGraphicsTreeView();
    virtual ~tst_QtGraphicsTreeView();

public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void maximumFirstIndex_data();
    void maximumFirstIndex();
    void itemAt_data();
    void itemAt();
    void layout_data();
    void layout();

protected:
    GraphicsTreeView *view;
};

Q_DECLARE_METATYPE(QList<int>)
Q_DECLARE_METATYPE(QList<QRectF>)

tst_QtGraphicsTreeView::tst_QtGraphicsTreeView()
{
}

tst_QtGraphicsTreeView::~tst_QtGraphicsTreeView()
{
}

void tst_QtGraphicsTreeView::initTestCase()
{
}

void tst_QtGraphicsTreeView::cleanupTestCase()
{
}

void tst_QtGraphicsTreeView::init()
{
    view = new GraphicsTreeView();
}

void tst_QtGraphicsTreeView::cleanup()
{
    delete view;
}

void tst_QtGraphicsTreeView::getSetCheck()
{
}

void tst_QtGraphicsTreeView::maximumFirstIndex_data()
{
}

void tst_QtGraphicsTreeView::maximumFirstIndex()
{
}

void tst_QtGraphicsTreeView::itemAt_data()
{
}

void tst_QtGraphicsTreeView::itemAt()
{
}

void tst_QtGraphicsTreeView::layout_data()
{
    QTest::addColumn<int>("branchCount");
    QTest::addColumn<int>("branchDepth");
    QTest::addColumn<QSizeF>("viewSize");
    QTest::addColumn<int>("firstIndex");
    QTest::addColumn<qreal>("horizontalOffset");
    QTest::addColumn<qreal>("verticalOffset");
    QTest::addColumn<bool>("expandBranches");
    QTest::addColumn<QList<QRectF> >("expectedGeometries");

    QTest::newRow("no items")
        << 0 << 0
        << QSizeF(100, 100)
        << 0
        << 0. << 0.
        << false
        << QList<QRectF>();

    QTest::newRow("five branches, no depth, collapsed")
        << 5 << 1
        << QSizeF(100, 100)
        << 0
        << 0. << 0.
        << false
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("five branches, depth of 2, collapsed")
        << 5 << 2
        << QSizeF(100, 100)
        << 0
        << 0. << 0.
        << false
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("ten branches, no depth, collapsed")
        << 10 << 1
        << QSizeF(100, 100)
        << 0
        << 0. << 0.
        << false
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("ten branches, depth of 2, collapsed")
        << 10 << 2
        << QSizeF(100, 100)
        << 0
        << 0. << 0.
        << false
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("five branches, depth of 2, expanded")
        << 5 << 2
        << QSizeF(100, 100)
        << 0
        << 0. << 0.
        << true
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));

    QTest::newRow("ten branches, depth of 2, expanded")
        << 10 << 2
        << QSizeF(100, 100)
        << 0
        << 0. << 0.
        << true
        << (QList<QRectF>()
            << QRectF(0, 0, 20, 20)
            << QRectF(0, 20, 20, 20)
            << QRectF(0, 40, 20, 20)
            << QRectF(0, 60, 20, 20)
            << QRectF(0, 80, 20, 20));
}

void tst_QtGraphicsTreeView::layout()
{
    QFETCH(int, branchCount);
    QFETCH(int, branchDepth);
    QFETCH(QSizeF, viewSize);
    QFETCH(int, firstIndex);
    QFETCH(qreal, horizontalOffset);
    QFETCH(qreal, verticalOffset);
    QFETCH(bool, expandBranches);
    QFETCH(QList<QRectF>, expectedGeometries);

    QtTreeDefaultModel model;
    QStack<QtTreeDefaultItem*> parents;
    parents.push(model.rootItem());
    for (int i = 0; i < branchCount; ++i) {
        // add a new branch of items
        for (int j = 0; j < branchDepth; ++j)
            parents.push(new QtTreeDefaultItem("foo", parents.top()));
        // go back to the root
        for (int k = 0; k < branchDepth; ++k)
            parents.pop();
    }

    view->setGeometry(QRectF(QPointF(0, 0), viewSize));
    view->setItemCreator(new QtGraphicsTreeViewItemCreator<GraphicsTreeViewItem>());
    view->setModel(&model);
    view->setFirstIndex(firstIndex);
    view->setHorizontalOffset(horizontalOffset);
    view->setVerticalOffset(verticalOffset);

    if (expandBranches) {
        QtTreeModelIterator it = model.begin();
        while (it.isValid()) {
            view->setItemExpanded(it);
            view->nextItem(it);
        }
    }
    view->doLayout();

    // get top item iterator and check geometries
    QtTreeModelIterator it = view->itemAt(QPointF(10, 10));
    for (int j = 0; j < expectedGeometries.count(); ++j) {
        QVERIFY(it.isValid());
        QGraphicsObject *item = view->itemForIterator(it);
        QVERIFY(item);
        QVERIFY(item->isWidget());
        QCOMPARE(static_cast<QtGraphicsTreeViewItem*>(item)->geometry(), expectedGeometries.at(j));
        view->nextItem(it);
    }
}

QTEST_MAIN(tst_QtGraphicsTreeView)
#include "tst_qgraphicstreeview.moc"
