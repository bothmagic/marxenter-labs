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

#ifndef QTLISTWIDGETNG_P_H
#define QTLISTWIDGETNG_P_H

//
//  W A R N I N G
//  -------------
//
// This file is not part of the Qt API.  It exists purely as an
// implementation detail.  This header file may change from version to
// version without notice, or even be removed.
//
// We mean it.
//

QT_BEGIN_NAMESPACE

class QtListDefaultModel;
class QtListSelectionManager;
class QtListController;
class QtGraphicsScrollBar;

class QtListWidgetNGPrivate
{
    Q_DECLARE_PUBLIC(QtListWidgetNG)
public:
    QtListWidgetNGPrivate();

    void _q_update(const QRegion &region);
    void _q_itemsInserted(int index, int count);
    void _q_itemsRemoved(int index, int count);
    void _q_itemsMoved(int from, int to, int count);
    void _q_itemsChanged(int from, int count, const QList<int> &roles);
    void _q_showView();
    void _q_hideView();
    void _q_controllerChanged(QtListController *current, QtListController *previous);
    void _q_modelChanged(QtListModelInterface *current, QtListModelInterface *previous);
    void _q_selectionManagerChanged(QtListSelectionManager *current, QtListSelectionManager *previous);
    void _q_viewChanged(QtGraphicsListView *current, QtGraphicsListView *previous);
    void _q_firstIndexChanged(int index);
    void _q_updateGeometries();

    void initialize();
    
    QGraphicsScene *scene;
    QtListController *controller;

    QtListWidgetNG *q_ptr;
};

QT_END_NAMESPACE

#endif//QTLISTWIDGETNG_P_H
