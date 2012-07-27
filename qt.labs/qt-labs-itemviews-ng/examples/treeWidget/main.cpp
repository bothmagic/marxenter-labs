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

#include <QtGui>

#include <qtreecontroller.h>
#include <qtreewidgetng.h>
#include <qtreedefaultmodel.h>
#include <qgraphicstreeview.h>

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    
    //QTime timer;
    //timer.start();
    
    QtTreeWidgetNG widget;
    widget.show();

    QtTreeDefaultItem *item;
    int k = 0;
    for (int i = 0; i < 2000; ++i) {
        item = new QtTreeDefaultItem(QString::number(k++), widget.defaultModel()->rootItem());
        QtTreeDefaultItem *child;
        for (int j = 0; j < 5; ++j)
            child = new QtTreeDefaultItem(QString::number(k++), item);
        if (i % 5 || true) {
            QtTreeModelIterator it = widget.defaultModel()->itemIterator(item);
            widget.controller()->view()->setItemExpanded(it);
        }
    }

    //qDebug() << timer.elapsed();

    return app.exec();
}
