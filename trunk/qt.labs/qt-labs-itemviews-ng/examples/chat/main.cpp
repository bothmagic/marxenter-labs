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

#include <QtCore>
#include <QtGui>

#include <experimental/qkineticlistcontroller.h>

#include "chatmodel.h"
#include "chatview.h"

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    QGraphicsView widget;
    widget.setScene(new QGraphicsScene(&widget));
    widget.resize(QSize(320, 480));
    widget.scene()->setSceneRect(0, 0, 320, 480);

    QtKineticListController *controller = new QtKineticListController(&widget);
    controller->setView(new ChatView);
    controller->setModel(new ChatModel(controller));
    controller->setOvershootEnabled(true);
    controller->view()->setGeometry(0, 0, 320, 480);
    controller->view()->setFlag(QGraphicsItem::ItemClipsChildrenToShape, true);
    widget.scene()->addItem(controller->view());
    widget.show();
    return app.exec();
}
