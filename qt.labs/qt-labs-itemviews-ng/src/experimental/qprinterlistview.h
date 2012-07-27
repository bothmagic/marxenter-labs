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

#ifndef QTPRINTERLISTVIEW_H
#define QTPRINTERLISTVIEW_H

#include <QtGui/qprinter.h>

QT_BEGIN_HEADER

QT_BEGIN_NAMESPACE

//QT_MODULE(Gui)

class QtPrinterListViewPrivate;
class QtListModelInterface;
class QtGraphicsListViewItemCreatorBase;
class QTextDocument;

class Q_ITEMVIEWSNG_EXPORT QtPrinterListView
{
public:
    QtPrinterListView(QPrinter *printer, QtListModelInterface *model);
    QtPrinterListView(QtListModelInterface *model);
    ~QtPrinterListView();

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

    QtGraphicsListViewItemCreatorBase *viewItemCreator() const;
    void setViewItemCreator(QtGraphicsListViewItemCreatorBase *creator);

    static void printModel(QPrinter *printer, QtListModelInterface *model, const QString &header = QString(), const QString &footer = QString());

protected:
    QtPrinterListViewPrivate *d_ptr;

private:
    Q_DECLARE_PRIVATE(QtPrinterListView)
    Q_DISABLE_COPY(QtPrinterListView)
};

QT_END_NAMESPACE

QT_END_HEADER

#endif //QTPRINTERLISTVIEW_H
