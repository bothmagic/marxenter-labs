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
#include <qlistdefaultmodel.h>
#include <qgraphicslistview.h>
#include <qlistcontroller.h>

class Item : public QtGraphicsListViewItem
{
public:
    Item(int index, QtGraphicsListView *view)
        : QtGraphicsListViewItem(index, view) {}
    QSizeF sizeHint(Qt::SizeHint which = Qt::PreferredSize, const QSizeF &contraints = QSizeF()) const
    {
        QSizeF hint = QtGraphicsListViewItem::sizeHint(which, contraints);
        if (index() & 9)
            hint.setHeight(44);
        else if (index() & 5)
            hint.setHeight(33);
        else if (index() & 3)
            hint.setHeight(66);
        else
            hint.setHeight(qBound(20., hint.height(), 100.)); // #### ???
        qDebug() << "item height" << hint.height() << "index" << index();
        return hint;
    }
    void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget = 0)
    {
        QtGraphicsListViewItem::paint(painter, option, widget);
        painter->drawRect(boundingRect().adjusted(0, 0, -1, -1));
    }
};

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    QtListWidgetNG widget;
    for (int i = 0; i < 100; ++i)
        widget.defaultModel()->appendItem(new QtListDefaultItem(QString("list item %1").arg(i)));
    widget.controller()->view()->setItemCreator(new QtGraphicsListViewItemCreator<Item>());
    widget.resize(QSize(240, 320));
    widget.show();
    return app.exec();
}
