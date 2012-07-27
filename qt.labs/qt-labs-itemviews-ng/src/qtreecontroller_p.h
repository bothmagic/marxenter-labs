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

#ifndef QTTREECONTROLLER_P_H
#define QTTREECONTROLLER_P_H

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

#include "qtreeselectionmanager.h"

QT_BEGIN_NAMESPACE

class QEvent;
class QKeyEvent;
class QInputMethodEvent;
class QMouseEvent;
class QDragEnterEvent;
class QDragLeaveEvent;
class QDragMoveEvent;
class QDropEvent;
class QHoverEvent;
class QWheelEvent;
class QResizeEvent;

class QtTreeController;
class QtGraphicsHeader;
class QtGraphicsTreeView;
class QTreeHeaderDataProvider;

class QtTreeControllerPrivate
{
    Q_DECLARE_PUBLIC(QtTreeController)
public:
    QtTreeControllerPrivate();
    ~QtTreeControllerPrivate();

    void _q_viewDestroyed();
    void _q_modelDestroyed();
    void _q_selectionsDestroyed();
    void _q_headerDestroyed();

    void _q_firstIndexChanged(int index);
    void _q_firstSectionChanged(int section);
    void _q_verticalOffsetChanged(qreal offset);
    void _q_horizontalOffsetChanged(qreal offset);

    QtTreeSelection anchoredSelection() const; // ### FIXME
    
    QtTreeController *q_ptr;
    QtGraphicsTreeView *view;
    QtTreeModelBase *model;
    QtTreeSelectionManager *selectionManager;
    QtGraphicsHeader *header;
    QTreeHeaderDataProvider *headerDataProvider;
    bool scrollPerItem;
    bool scrollPerSection;
    int currentIndex;
    int anchorIndex;
};

QT_END_NAMESPACE

#endif//QTTREECONTROLLER_P_H
