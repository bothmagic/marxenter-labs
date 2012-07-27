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

#ifndef BORDERPIXMAP_H
#define BORDERPIXMAP_H

#include <qpainter.h>

// ### this has been deprecated by the implementation in Qt mainline

struct QtPixmapBorders
{
    enum Rule { Stretch, Repeat, Round };
    QtPixmapBorders()
        : horizontalRule(Stretch),
          verticalRule(Stretch),
           sourceTopBorder(0),
           sourceLeftBorder(0),
           sourceBottomBorder(0),
           sourceRightBorder(0),
           targetTopBorder(-1),
           targetLeftBorder(-1),
           targetBottomBorder(-1),
           targetRightBorder(-1) {}
    Rule horizontalRule;
    Rule verticalRule;
    qreal sourceTopBorder;
    qreal sourceLeftBorder;
    qreal sourceBottomBorder;
    qreal sourceRightBorder;
    qreal targetTopBorder;
    qreal targetLeftBorder;
    qreal targetBottomBorder;
    qreal targetRightBorder;
};

void qtDrawBorderPixmap(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source, const QtPixmapBorders &borders);

inline void qtDrawBorderPixmap(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QtPixmapBorders &borders)
{ qtDrawBorderPixmap(painter, target, pixmap, pixmap.rect(), borders); }
    
#endif//BORDERPIXMAP_H
