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

#ifndef QTGRAPHICSFLOWVIEW_H
#define QTGRAPHICSFLOWVIEW_H

#include "../qgraphicslistview.h"

class QtGraphicsFlowViewPrivate;

class Q_ITEMVIEWSNG_EXPORT QtGraphicsFlowView : public QtGraphicsListView
{
public:
    QtGraphicsFlowView(Qt::Orientation orientation = Qt::Vertical, QGraphicsWidget *parent = 0);
    virtual ~QtGraphicsFlowView();

    virtual QRectF itemGeometry(int index) const;
    virtual int itemAt(const QPointF &position) const;
    virtual int maximumFirstIndex() const;
    virtual void doLayout();
    virtual QSizeF itemSize(const QStyleOptionViewItemV4 *option, int index) const;

private:
    Q_DECLARE_PRIVATE(QtGraphicsFlowView)
};

#endif
