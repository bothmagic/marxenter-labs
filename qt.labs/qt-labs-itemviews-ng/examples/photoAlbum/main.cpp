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

#include <QtCore/QtCore>
#include <QtGui/QtGui>

#include <qgraphicslistview.h>
#include <qgraphicsgridview.h>
#include <qlistdefaultmodel.h>
#include <qlistselectionmanager.h>
#include <qkineticlistcontroller.h>

class Photo : public QtGraphicsListViewItem
{
public:
    Photo(int index, QtGraphicsListView *view);
    QSizeF sizeHint(Qt::SizeHint which, const QSizeF &constraint = QSizeF()) const;
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget = 0);
};

Photo::Photo(int index, QtGraphicsListView *view)
    : QtGraphicsListViewItem(index, view)
{
}

QSizeF Photo::sizeHint(Qt::SizeHint which, const QSizeF &constraint) const
{
    Q_UNUSED(constraint);
    switch (which) {
        case Qt::MinimumSize:
            return QSizeF(64, 48);
        case Qt::PreferredSize:
            return QSizeF(640, 480);//view()->size().boundedTo(constraint);
        case Qt::MaximumSize:
            return QSizeF(640, 480);//qvariant_cast<QPixmap>(data().value(Qt::DecorationRole)).size();
        default:
            break;
    }
    return QSize();
}

void Photo::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{
    Q_UNUSED(widget);
    const QPixmap pixmap = qvariant_cast<QPixmap>(data().value("DecorationRole"));
    const QSizeF bounded = pixmap.size().boundedTo(option->rect.size());
    QSizeF scaled = pixmap.size();
    scaled.scale(bounded, Qt::KeepAspectRatio);
    const QRectF aligned = QStyle::alignedRect(option->direction, Qt::AlignCenter, scaled.toSize(), option->rect);
    painter->drawPixmap(aligned, pixmap, pixmap.rect());
}

// State

class ViewState : public QState
{
public:
    ViewState(QtGraphicsListView *view, QState *parent = 0);

    void onEntry(QEvent *event);
    void onExit(QEvent *event);

private:
    QtGraphicsListView *m_view;
};

ViewState::ViewState(QtGraphicsListView *view, QState *parent)
    : QState(parent), m_view(view)
{
}

void ViewState::onEntry(QEvent *event)
{
    Q_UNUSED(event);
    m_view->show();
}

void ViewState::onExit(QEvent *event)
{
    Q_UNUSED(event);
    m_view->hide();
}

class ViewTransition : public QSignalTransition
{
public:
    ViewTransition(QtKineticListController *sender,
                   const char *signalName,
                   QState *targetState,
                   QState *sourceState = 0);
    ~ViewTransition();

    void setFromView(QtGraphicsListView *from);
    void setToView(QtGraphicsListView *to);

protected:
    void onTransition(QEvent *event);

private:
    QtGraphicsListView *m_from;
    QtGraphicsListView *m_to;
    QPropertyAnimation *m_animation;
};

ViewTransition::ViewTransition(QtKineticListController *sender,
                               const char *signalName,
                               QState *targetState,
                               QState *sourceState)
    : QSignalTransition(sender, signalName, sourceState),
    m_from(0), m_to(0), m_animation(new QPropertyAnimation(0, "geometry", this))
{
    setTargetState(targetState);
    m_animation->setEasingCurve(QEasingCurve::OutElastic);
    m_animation->setDuration(1000);
    addAnimation(m_animation);
}

ViewTransition::~ViewTransition()
{
}

void ViewTransition::setFromView(QtGraphicsListView *from)
{
    m_from = from;
}

void ViewTransition::setToView(QtGraphicsListView *to)
{
    m_to = to;
}

void ViewTransition::onTransition(QEvent *event)
{
    // set up the animation with the current item
    if (event->type() == QEvent::StateMachineSignal) {
        QStateMachine::SignalEvent *se = static_cast<QStateMachine::SignalEvent*>(event);
        QList<QVariant> args = se->arguments();
        if (!args.isEmpty() && m_from && m_to) {
            const int index = args.at(0).toInt();
            const qreal offset = m_to->offsetToEnsureIndexIsVisible(index);
            m_to->setOffset(offset); // ### FIXME center on item
            m_to->doLayout(); // ensure the item is layed out
            qobject_cast<QtKineticListController*>(m_to->controller())->stop();
            m_animation->setTargetObject(reinterpret_cast<QObject*>(m_to->itemForIndex(index)));
            m_animation->setStartValue(m_from->itemGeometry(index));
            m_animation->setEndValue(m_to->itemGeometry(index));
        }
    }
}

Q_DECLARE_METATYPE(Qt::MouseButton) // ### FIXME

// main

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    QGraphicsView widget;
    widget.setScene(new QGraphicsScene(&widget));
    widget.resize(QSize(640, 480));
    widget.scene()->setSceneRect(0, 0, 640, 480);

    // data

    QtListDefaultModel *model = new QtListDefaultModel(&widget);

    QtListDefaultItem *item = new QtListDefaultItem;
    item->setData(QPixmap(":images/go.jpg"), "DecorationRole");
    model->appendItem(item);

    item = new QtListDefaultItem;
    item->setData(QPixmap(":images/contrast.jpg"), "DecorationRole");
    model->appendItem(item);

    item = new QtListDefaultItem;
    item->setData(QPixmap(":images/nightfall.jpg"), "DecorationRole");
    model->appendItem(item);

    item = new QtListDefaultItem;
    item->setData(QPixmap(":images/flower.jpg"), "DecorationRole");
    model->appendItem(item);

    item = new QtListDefaultItem;
    item->setData(QPixmap(":images/beach.jpg"), "DecorationRole");
    model->appendItem(item);

    // selections

    QtListSelectionManager *selections = new QtListSelectionManager(&widget);

    // views and controllers

    QtGraphicsListView *listView = new QtGraphicsListView(Qt::Horizontal);
    QtKineticListController *listController = new QtKineticListController(&widget);

    listView->setItemCreator(new QtGraphicsListViewItemCreator<Photo>());
    listView->setGeometry(0, 0, 640, 480);
    listView->setFlag(QGraphicsItem::ItemClipsChildrenToShape, true);
    listView->hide();

    listController->setView(listView);
    listController->setModel(model);
    listController->setSelectionManager(selections);
    listController->setOvershootEnabled(true);
    listController->setCenterOnItemEnabled(true);

    //QtGraphicsGridView *gridView = new QtGraphicsGridView(2, Qt::Vertical);
    QtGraphicsListView *gridView = new QtGraphicsListView();
    QtKineticListController *gridController = new QtKineticListController(&widget);

    gridView->setItemCreator(new QtGraphicsListViewItemCreator<Photo>());
    gridView->setGeometry(0, 0, 640, 480);
    //gridView->setFlag(QGraphicsItem::ItemClipsChildrenToShape, true); // ### FIXME
    gridView->hide();

    gridController->setView(gridView);
    gridController->setModel(model);
    gridController->setSelectionManager(selections);
    gridController->setOvershootEnabled(true);

    // state machine

    QStateMachine stateMachine;
    ViewState *listState = new ViewState(listView);
    ViewState *gridState = new ViewState(gridView);

    ViewTransition *listToGridTransition = new ViewTransition(listController, SIGNAL(itemClicked(int,Qt::MouseButton)), gridState);
    ViewTransition *gridToListTransition = new ViewTransition(gridController, SIGNAL(itemClicked(int,Qt::MouseButton)), listState);

    listToGridTransition->setFromView(listView);
    listToGridTransition->setToView(gridView);

    gridToListTransition->setFromView(gridView);
    gridToListTransition->setToView(listView);

    listState->addTransition(listToGridTransition);
    gridState->addTransition(gridToListTransition);

    stateMachine.addState(listState);
    stateMachine.addState(gridState);
    stateMachine.setInitialState(gridState);
    stateMachine.start();

    // run

    widget.setBackgroundBrush(Qt::black);
    widget.scene()->addItem(listView);
    widget.scene()->addItem(gridView);
    widget.show();

    return app.exec();
}
