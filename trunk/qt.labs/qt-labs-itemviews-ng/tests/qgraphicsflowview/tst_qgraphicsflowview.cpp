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

#include <qgraphicsflowview.h>
#include <qlistdefaultmodel.h>
#include <qgraphicslistview.h>

class MockFlowView : public QtGraphicsFlowView
{
public:
    MockFlowView() { }

    void setController(QtListController *c) { QtGraphicsListView::setController(c); }
    void setModel(QtListModelInterface *m) { QtGraphicsListView::setModel(m); }
};

class tst_QListFlowLayout : public QObject
{
    Q_OBJECT

public:
    tst_QListFlowLayout();
    virtual ~tst_QListFlowLayout();

public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void itemAtVertical();
    void layoutPositioningVertical();

protected:
    MockFlowView *view;
};

tst_QListFlowLayout::tst_QListFlowLayout()
{
}

tst_QListFlowLayout::~tst_QListFlowLayout()
{
}

void tst_QListFlowLayout::initTestCase()
{
}

void tst_QListFlowLayout::cleanupTestCase()
{
}

void tst_QListFlowLayout::init()
{
    view = new MockFlowView();
}

void tst_QListFlowLayout::cleanup()
{
    delete view;
}

void tst_QListFlowLayout::itemAtVertical()
{
    QtListDefaultModel model;
    for (int i = 0; i < 10; ++i) {
        QtListDefaultItem *item = new QtListDefaultItem(QString("Item %1").arg(i));
        model.insertItem(i, item);
    }
    view->setModel(&model);
    view->setGeometry(0, 0, 200, 2000);
    // default icon size is 80 x 60
    // there is a default spacing of 6

    QCOMPARE(view->itemAt(QPointF(0, 0)), 0);
    QCOMPARE(view->itemAt(QPointF(79, 59)), 0);
    QCOMPARE(view->itemAt(QPointF(80, 59)), -1);
    QCOMPARE(view->itemAt(QPointF(85, 59)), -1);
    QCOMPARE(view->itemAt(QPointF(86, 59)), 1);
    QCOMPARE(view->itemAt(QPointF(165, 59)), 1);
    QCOMPARE(view->itemAt(QPointF(166, 59)), -1);

    QCOMPARE(view->itemAt(QPointF(0, 66)), 2);
    QCOMPARE(view->itemAt(QPointF(79, 125)), 2);
    QCOMPARE(view->itemAt(QPointF(80, 125)), -1);
    QCOMPARE(view->itemAt(QPointF(85, 125)), -1);
    QCOMPARE(view->itemAt(QPointF(90, 125)), 3);
    QCOMPARE(view->itemAt(QPointF(165, 125)), 3);
    QCOMPARE(view->itemAt(QPointF(166, 125)), -1);
    QCOMPARE(view->itemAt(QPointF(20, 126)), -1);
    QCOMPARE(view->itemAt(QPointF(166, 126)), -1);

    QCOMPARE(view->itemAt(QPointF(10, 323)), 8);
    QCOMPARE(view->itemAt(QPointF(10, 324)), -1);
    QCOMPARE(view->itemAt(QPointF(160, 323)), 9);
    QCOMPARE(view->itemAt(QPointF(10, 340)), -1);

    // setting the firstIndex will make the itemAt be repositioned to the new baseline.
    // meaning all y positions are offset by -66
    view->setFirstIndex(2);
    QCOMPARE(view->itemAt(QPointF(0, 0)), 2);
    QCOMPARE(view->itemAt(QPointF(0, 66)), 4);
    QCOMPARE(view->itemAt(QPointF(80, 125)), -1);
    QCOMPARE(view->itemAt(QPointF(10, 257)), 8);
    QCOMPARE(view->itemAt(QPointF(10, 258)), -1);
    QCOMPARE(view->itemAt(QPointF(10, 340)), -1);
    QCOMPARE(view->itemAt(QPointF(165, 59)), 3);
    QCOMPARE(view->itemAt(QPointF(166, 59)), -1);

    view->setOffset(-10); // moves the items to the right
    QCOMPARE(view->itemAt(QPointF(0, 0)), -1);
    QCOMPARE(view->itemAt(QPointF(10, 0)), 2);
    QCOMPARE(view->itemAt(QPointF(10, 66)), 4);
    QCOMPARE(view->itemAt(QPointF(90, 125)), -1);
    QCOMPARE(view->itemAt(QPointF(20, 257)), 8);
    QCOMPARE(view->itemAt(QPointF(20, 258)), -1);
    QCOMPARE(view->itemAt(QPointF(20, 340)), -1);
    QCOMPARE(view->itemAt(QPointF(175, 59)), 3);
    QCOMPARE(view->itemAt(QPointF(176, 59)), -1);

    // vertical offset has an effect too
    view->setOffset(21);
    int expectedYOffset = 21;
    QCOMPARE(view->itemAt(QPointF(0, 0)), -1);
    QCOMPARE(view->itemAt(QPointF(0, 20)), -1);
    QCOMPARE(view->itemAt(QPointF(10, 21)), 2);
    QCOMPARE(view->itemAt(QPointF(10, 66-expectedYOffset)), 4);
    QCOMPARE(view->itemAt(QPointF(90, 125-expectedYOffset)), -1);
    QCOMPARE(view->itemAt(QPointF(20, 257-expectedYOffset)), 8);
    QCOMPARE(view->itemAt(QPointF(20, 258-expectedYOffset)), -1);
    QCOMPARE(view->itemAt(QPointF(20, 340-expectedYOffset)), -1);
    QCOMPARE(view->itemAt(QPointF(175, 59-expectedYOffset)), 3);
    QCOMPARE(view->itemAt(QPointF(176, 59-expectedYOffset)), -1);

    view->setOffset(-21);
    expectedYOffset -= 21 * 2;
    // a negative offset means we move the items down in the view and thus some white space
    // appears above.  The view does not show any items that come before the firstIndex!
    QCOMPARE(view->itemAt(QPointF(0, 0)), -1);
    QCOMPARE(view->itemAt(QPointF(10, 20)), -1);
    QCOMPARE(view->itemAt(QPointF(9, 21)), -1);
    QCOMPARE(view->itemAt(QPointF(10, 21)), 2);
    QCOMPARE(view->itemAt(QPointF(10, 66-expectedYOffset)), 4);
    QCOMPARE(view->itemAt(QPointF(90, 125-expectedYOffset)), -1);
    QCOMPARE(view->itemAt(QPointF(20, 257-expectedYOffset)), 8);
    QCOMPARE(view->itemAt(QPointF(20, 258-expectedYOffset)), -1);
    QCOMPARE(view->itemAt(QPointF(20, 340-expectedYOffset)), -1);
    QCOMPARE(view->itemAt(QPointF(175, 59-expectedYOffset)), 3);
    QCOMPARE(view->itemAt(QPointF(176, 59-expectedYOffset)), -1);
}

void tst_QListFlowLayout::layoutPositioningVertical() // vertical
{
    QtListDefaultModel model;
    for (int i = 0; i < 10; ++i) {
        QtListDefaultItem *item = new QtListDefaultItem(QString("Item %1").arg(i));
        model.insertItem(i, item);
    }
    view->setModel(&model);
    view->setGeometry(0, 0, 200, 135);
    QVERIFY(view->children().isEmpty());
    view->updateLayout();

    QCOMPARE(view->childItems().count(), 6); // 2.1 rows
    QList<int> visibleItems;
    visibleItems << 0 << 1 << 2 << 3 << 4 << 5;
    foreach (QGraphicsItem *child, view->childItems()) {
        QtGraphicsListViewItem *item = dynamic_cast<QtGraphicsListViewItem*>(child);
        QVERIFY(item);
        //qDebug() << item->index();
        QVERIFY(visibleItems.contains(item->index()));
    }
    view->setGeometry(0, 0, 200, 195);
    view->setFirstIndex(2); // causes updateLayout
    QCOMPARE(view->childItems().count(), 6);
    visibleItems.clear();
    visibleItems << 2 << 3 << 4 << 5 << 6 << 7;
    foreach (QGraphicsItem *child, view->childItems()) {
        QtGraphicsListViewItem *item = dynamic_cast<QtGraphicsListViewItem*>(child);
        QVERIFY(item);
        //qDebug() << item->index();
        QVERIFY(visibleItems.contains(item->index()));
    }

    view->updateLayout(); // should have no effect
    QCOMPARE(view->childItems().count(), 6);
    foreach (QGraphicsItem *child, view->childItems()) {
        QtGraphicsListViewItem *item = dynamic_cast<QtGraphicsListViewItem*>(child);
        QVERIFY(item);
        //qDebug() << item->index();
        QVERIFY(visibleItems.contains(item->index()));
    }

    view->setFirstIndex(8);
    view->setGeometry(0, 0, 200, 200);
    view->updateLayout();
    QCOMPARE(view->childItems().count(), 2);
    visibleItems.clear();
    visibleItems << 8 << 9;
    foreach (QGraphicsItem *child, view->childItems()) {
        QtGraphicsListViewItem *item = dynamic_cast<QtGraphicsListViewItem*>(child);
        QVERIFY(item);
        //qDebug() << item->index();
        QVERIFY(visibleItems.contains(item->index()));
    }
}

QTEST_MAIN(tst_QListFlowLayout)
#include "tst_qgraphicsflowview.moc"
