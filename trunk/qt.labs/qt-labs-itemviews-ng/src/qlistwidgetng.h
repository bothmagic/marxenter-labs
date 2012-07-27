/****************************************************************************
**
** Copyright (C) 2010 Nokia Corporation and/or its subsidiary(-ies).
** All rights reserved.
** Contact: Nokia Corporation (qt-info@nokia.com)
**
** This file is part of the Itemviews NG project on Trolltech Labs.
**
** GNU Lesser General Public License Usage
** This file may be used under the terms of the GNU Lesser
** General Public License version 2.1 as published by the Free Software
** Foundation and appearing in the file LICENSE.LGPL included in the
** packaging of this file.  Please review the following information to
** ensure the GNU Lesser General Public License version 2.1 requirements
** will be met: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html.
**
** If you have questions regarding the use of this file, please contact
** Nokia at qt-info@nokia.com.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
****************************************************************************/

#ifndef QTLISTWIDGETNG_H
#define QTLISTWIDGETNG_H

#include "qitemviewsglobal.h"

#include <QtGui/qgraphicsview.h>

QT_BEGIN_HEADER

QT_BEGIN_NAMESPACE

//QT_MODULE(Gui)

class QtListController;
class QtListDefaultModel;
class QtListModelInterface;
class QtGraphicsListView;
class QtListWidgetNGPrivate;

class Q_ITEMVIEWSNG_EXPORT QtListWidgetNG : public QAbstractScrollArea
{
    Q_OBJECT
public:
    QtListWidgetNG(QWidget *parent = 0);
    explicit QtListWidgetNG(QtListController *controller, QWidget *parent = 0);
    virtual ~QtListWidgetNG();

    QtListController *controller() const;
    QtListDefaultModel *defaultModel() const;

protected:
    bool viewportEvent(QEvent *event);
    void scrollContentsBy(int dx, int dy);

protected:
    QtListWidgetNGPrivate *d_ptr;

private:
    Q_DECLARE_PRIVATE(QtListWidgetNG)
    Q_DISABLE_COPY(QtListWidgetNG)
    Q_PRIVATE_SLOT(d_func(), void _q_itemsInserted(int index, int count))
    Q_PRIVATE_SLOT(d_func(), void _q_itemsRemoved(int index, int count))
    Q_PRIVATE_SLOT(d_func(), void _q_itemsMoved(int from, int to, int count))
    Q_PRIVATE_SLOT(d_func(), void _q_itemsChanged(int from, int count, const QList<int> &roles))
    Q_PRIVATE_SLOT(d_func(), void _q_showView())
    Q_PRIVATE_SLOT(d_func(), void _q_hideView())
    Q_PRIVATE_SLOT(d_func(), void _q_updateGeometries())
    Q_PRIVATE_SLOT(d_func(), void _q_modelChanged(QtListModelInterface *current, QtListModelInterface *previous))
    Q_PRIVATE_SLOT(d_func(), void _q_selectionManagerChanged(QtListSelectionManager *current, QtListSelectionManager *previous))
    Q_PRIVATE_SLOT(d_func(), void _q_viewChanged(QtGraphicsListView *current, QtGraphicsListView *previous))
    Q_PRIVATE_SLOT(d_func(), void _q_firstIndexChanged(int index))
};

QT_END_NAMESPACE

QT_END_HEADER

#endif//QTLISTWIDGETNG_H
