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

#ifndef QTPRINTERTABLEVIEW_H
#define QTPRINTERLTABLEIEW_H

#include <QtGui/qprinter.h>

QT_BEGIN_HEADER

QT_BEGIN_NAMESPACE

//QT_MODULE(Gui)

class QtPrinterTableViewPrivate;
class QtTableModelInterface;
class QtGraphicsHeader;
class QTextDocument;

class Q_ITEMVIEWSNG_EXPORT QtPrinterTableView
{
public:
    explicit QtPrinterTableView(QPrinter *printer, QtTableModelInterface *model, QtGraphicsHeader *verticalHeader = 0, QtGraphicsHeader *horizontalHeader = 0);
    explicit QtPrinterTableView(QtTableModelInterface *model, QtGraphicsHeader *verticalHeader = 0, QtGraphicsHeader *horizontalHeader = 0);
    ~QtPrinterTableView();

    QPrinter *printer();
    bool print();

    void setOrientation(Qt::Orientation orientation);
    Qt::Orientation orientation() const;

    QTextDocument *headerDocument();
    QTextDocument *footerDocument();

    void setHeaderText(const QString &header);
    void setFooterText(const QString &footer);
    QString headerText() const;
    QString footerText() const;

    // TODO add the setter of the creator object, add static print object

protected:
    QtPrinterTableViewPrivate *d_ptr;

private:
    Q_DECLARE_PRIVATE(QtPrinterTableView)
    Q_DISABLE_COPY(QtPrinterTableView)
};

QT_END_NAMESPACE

QT_END_HEADER

#endif //QTPRINTERTABLEVIEW_H
