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

#ifndef QTTREEWIDGETNG_P_H
#define QTTREEWIDGETNG_P_H

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

#include <qbasictimer.h>

QT_BEGIN_NAMESPACE

class QtTreeDefaultModel;
class QtTreeSelectionManager;
class QtTreeController;
class QtGraphicsTreeView;
class QtGraphicsHeader;
class QtGraphicsScrollBar;

class QtTreeWidgetNGPrivate
{
    Q_DECLARE_PUBLIC(QtTreeWidgetNG)
public:
    QtTreeWidgetNGPrivate();

    void _q_itemExpanded(QtTreeModelBase::iterator_base &it, int count);
    void _q_itemCollapsed(QtTreeModelBase::iterator_base &it, int count);
    void _q_itemsInserted(QtTreeModelBase::iterator_base &it, int count);
    void _q_itemsRemoved(QtTreeModelBase::iterator_base &, int count);
    void _q_itemsMoved(QtTreeModelBase::iterator_base &from, QtTreeModelBase::iterator_base &to, int count);
    void _q_showView();
    void _q_hideView();
    void _q_updateGeometries();

    void _q_firstIndexChanged(int index);
    void _q_firstSectionChanged(int section);
    void _q_offsetChanged(qreal offset);

    void _q_modelChanged(QtTreeModelBase *current, QtTreeModelBase *previous);
    void _q_selectionManagerChanged(QtTreeSelectionManager *current, QtTreeSelectionManager *previous);

    void _q_viewChanged(QtGraphicsTreeView *current, QtGraphicsTreeView *previous);
    void _q_headerChanged(QtGraphicsHeader *current, QtGraphicsHeader *previous);
    void _q_controllerChanged(QtTreeController *current, QtTreeController *previous);

    void initialize();
    
    QtTreeController *controller;
    QtGraphicsHeader *header;

    QBasicTimer timer;

    QtTreeWidgetNG *q_ptr;
};

QT_END_NAMESPACE

#endif//QTTREEWIDGETNG_P_H
