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
#include <qlistwidgetng.h>
#include <qlistcontroller.h>
#include <qgraphicslistview.h>
#include "model.h"

class Highlight : public QGraphicsWidget
{
public:
    Highlight(QGraphicsWidget *parent = 0) : QGraphicsWidget(parent) {}
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option,QWidget *widget)
    {
        Q_UNUSED(option);
        Q_UNUSED(widget);
        painter->fillRect(rect(), Qt::green);
    }
};

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    QtListWidgetNG widget;
    widget.controller()->setModel(new Model(&widget));
    widget.controller()->view()->setHighlight(new Highlight);
    widget.resize(QSize(240, 320));
    widget.show();
    return app.exec();
}
