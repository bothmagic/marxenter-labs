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
#include <qgraphicstableview.h>
#include <qtabledefaultmodel.h>
#include <qtablecontroller.h>

class GraphicsTableView : public QtGraphicsTableView
{
public:
    GraphicsTableView() { }
    void setController(QtTableController *c) { QtGraphicsTableView::setController(c); }
    void setModel(QtTableModelInterface *m) { QtGraphicsTableView::setModel(m); }
};

class tst_QtGraphicsTableView : public QObject
{
    Q_OBJECT

public:
    tst_QtGraphicsTableView();
    virtual ~tst_QtGraphicsTableView();

public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void maximumFirstRow_data();
    void maximumFirstRow();
    void maximumFirstColumn_data();
    void maximumFirstColumn();
    void cellGeometry_data();
    void cellGeometry();
    void cellAt_data();
    void cellAt();
    void layout_data();
    void layout();

protected:
    GraphicsTableView *view;
};

struct QtCell
{
    QtCell(int row = 0, int column = 0) : row(row), column(column) {}
    int row;
    int column;
};

Q_DECLARE_METATYPE(QtCell)
Q_DECLARE_METATYPE(QList<QtCell>)
Q_DECLARE_METATYPE(QList<int>)
Q_DECLARE_METATYPE(QList<QRectF>)

tst_QtGraphicsTableView::tst_QtGraphicsTableView()
{
}

tst_QtGraphicsTableView::~tst_QtGraphicsTableView()
{
}

void tst_QtGraphicsTableView::initTestCase()
{
}

void tst_QtGraphicsTableView::cleanupTestCase()
{
}

void tst_QtGraphicsTableView::init()
{
    view = new GraphicsTableView();
}

void tst_QtGraphicsTableView::cleanup()
{
    delete view;
}

void tst_QtGraphicsTableView::getSetCheck()
{
}

void tst_QtGraphicsTableView::maximumFirstRow_data()
{
}

void tst_QtGraphicsTableView::maximumFirstRow()
{
}

void tst_QtGraphicsTableView::maximumFirstColumn_data()
{
}

void tst_QtGraphicsTableView::maximumFirstColumn()
{
}

void tst_QtGraphicsTableView::cellGeometry_data()
{
    QTest::addColumn<int>("rowCount");
    QTest::addColumn<int>("columnCount");
    QTest::addColumn<QSizeF>("viewSize");
    QTest::addColumn<int>("firstRow");
    QTest::addColumn<int>("firstColumn");
    QTest::addColumn<qreal>("horizontalOffset");
    QTest::addColumn<qreal>("verticalOffset");
    QTest::addColumn<QList<QRectF> >("geometries");

    QTest::newRow("no cells")
        << 0 << 0               // table dimensions
        << QSizeF(100, 100)     // view size
        << 0 << 0               // first view row and column
        << 0. << 0.             // offsets
        << QList<QRectF>();     // expected geometries

    QTest::newRow("1 row, 1 column")
        << 1 << 1
        << QSizeF(100, 100)
        << 0 << 0
        << 0. << 0.
        << (QList<QRectF>()
            << QRectF(0, 0, 100, 30));

    QTest::newRow("3 rows, 3 columns")
        << 3 << 3
        << QSizeF(100, 100)
        << 0 << 0
        << 0. << 0.
        << (QList<QRectF>()
            << QRectF(0, 0, 100, 30) << QRectF(100, 0, 100, 30) << QRectF(200, 0, 100, 30)
            << QRectF(0, 30, 100, 30) << QRectF(100, 30, 100, 30) << QRectF(200, 30, 100, 30)
            << QRectF(0, 60, 100, 30) << QRectF(100, 60, 100, 30) << QRectF(200, 60, 100, 30));
}

void tst_QtGraphicsTableView::cellGeometry()
{
    QFETCH(int, rowCount);
    QFETCH(int, columnCount);
    QFETCH(QSizeF, viewSize);
    QFETCH(int, firstRow);
    QFETCH(int, firstColumn);
    QFETCH(qreal, verticalOffset);
    QFETCH(qreal, horizontalOffset);
    QFETCH(QList<QRectF>, geometries);

    QtTableDefaultModel model(rowCount, columnCount);

    view->setGeometry(QRectF(QPointF(0, 0), viewSize));
    view->setModel(&model);
    view->setFirstRow(firstRow);
    view->setFirstColumn(firstColumn);
    view->setVerticalOffset(verticalOffset);
    view->setHorizontalOffset(horizontalOffset);
    view->doLayout();

    for (int row = 0; row < rowCount; ++row)
        for (int column = 0; column < columnCount; ++column)
            QCOMPARE(geometries.at(row * columnCount + column), view->cellGeometry(row, column));
}

void tst_QtGraphicsTableView::cellAt_data()
{
}

void tst_QtGraphicsTableView::cellAt()
{
}

void tst_QtGraphicsTableView::layout_data()
{
    QTest::addColumn<int>("rowCount");
    QTest::addColumn<int>("columnCount");
    QTest::addColumn<QSizeF>("viewSize");
    QTest::addColumn<int>("firstRow");
    QTest::addColumn<int>("firstColumn");
    QTest::addColumn<qreal>("horizontalOffset");
    QTest::addColumn<qreal>("verticalOffset");
    QTest::addColumn<QList<QtCell> >("cells");
    QTest::addColumn<QList<QRectF> >("geometries");

    QTest::newRow("no cells")
        << 0 << 0               // table dimensions
        << QSizeF(100, 100)     // view size
        << 0 << 0               // first view row and column
        << 0. << 0.             // offsets
        << QList<QtCell>()      // expected cells
        << QList<QRectF>();     // expected geometries

    QTest::newRow("1 row, 1 column, all visible")
        << 1 << 1
        << QSizeF(100, 100)
        << 0 << 0
        << 0. << 0.
        << (QList<QtCell>()
            << QtCell(0, 0))
        << (QList<QRectF>()
            << QRectF(0, 0, 100, 30));

    QTest::newRow("5 rows, 5 columns, 3 visible")
        << 5 << 5
        << QSizeF(100, 100)
        << 0 << 0
        << 0. << 0.
        << (QList<QtCell>()
            << QtCell(0, 0) << QtCell(1, 0) << QtCell(2, 0))
        << (QList<QRectF>()
            << QRectF(0, 0, 100, 30) << QRectF(0, 30, 100, 30) << QRectF(0, 60, 100, 30));

    // ### FIXME: add more data-sets
}

void tst_QtGraphicsTableView::layout()
{
    QFETCH(int, rowCount);
    QFETCH(int, columnCount);
    QFETCH(QSizeF, viewSize);
    QFETCH(int, firstRow);
    QFETCH(int, firstColumn);
    QFETCH(qreal, horizontalOffset);
    QFETCH(qreal, verticalOffset);
    QFETCH(QList<QtCell>, cells);
    QFETCH(QList<QRectF>, geometries);

    QtTableDefaultModel model(rowCount, columnCount);

    view->setGeometry(QRectF(QPointF(0, 0), viewSize));
    view->setModel(&model);
    view->setFirstRow(firstRow);
    view->setFirstColumn(firstColumn);
    view->setHorizontalOffset(horizontalOffset);
    view->setVerticalOffset(verticalOffset);
    view->doLayout();

    for (int j = 0; j < cells.count(); ++j) {
        QtCell cell = cells.at(j);
        QGraphicsObject *item = view->itemForCell(cell.row, cell.column);
        QVERIFY(item);
        QVERIFY(item->isWidget());
        QCOMPARE(static_cast<QtGraphicsTableViewItem*>(item)->row(), cell.row);
        QCOMPARE(static_cast<QtGraphicsTableViewItem*>(item)->column(), cell.column);
        QCOMPARE(static_cast<QtGraphicsTableViewItem*>(item)->geometry(), geometries.at(j));
    }
}

QTEST_MAIN(tst_QtGraphicsTableView)
#include <tst_qgraphicstableview.moc>
