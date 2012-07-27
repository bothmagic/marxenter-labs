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

#ifndef QTPRINTERLISTVIEW_P_H
#define QTPRINTERLISTVIEW_P_H

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

#ifndef QT_NO_PRINTER
class QTextDocument;

class QtPrinterListViewPrivate
{
    Q_DECLARE_PUBLIC(QtPrinterListView)
public:
    QtPrinterListViewPrivate(QPrinter *printer);
    ~QtPrinterListViewPrivate();

    QtPrinterListView *q_ptr;

    bool ownsPrinter;
    QPrinter * const printer;
    QtListModelInterface *model;

    Qt::Orientation orientation;
    mutable QTextDocument *header;
    mutable QTextDocument *footer;

    qreal headerFooterSpacing; // in mm

    QtGraphicsListViewItemCreatorBase *creator;

    /// print a header and return the height we used
    qreal printHeader(QPainter &painter);
    qreal printFooter(QPainter &painter);
    QTextDocument *getOrCreateHeader() const;
    QTextDocument *getOrCreateFooter() const;
    inline qreal headerFooterSpacingInPixels() {
        return headerFooterSpacing * (printer->resolution() / 25.4);
    }
};

#endif
QT_END_NAMESPACE

#endif//QTPRINTERLISTVIEW_P_H
