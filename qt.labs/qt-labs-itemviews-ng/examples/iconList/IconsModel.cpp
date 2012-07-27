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

#include "IconsModel.h"

IconsModel::IconsModel(QObject *parent)
    : QtListDefaultModel(parent)
{
    insertIcon(":/icons/chronometer.svgz");
    insertIcon(":/icons/configure.svgz");
    insertIcon(":/icons/document-edit.svgz");
    insertIcon(":/icons/document-encrypt.svgz");
    insertIcon(":/icons/document-preview-archive.svgz");
    insertIcon(":/icons/draw-brush.svgz");
    insertIcon(":/icons/draw-eraser.svgz");
    insertIcon(":/icons/draw-freehand.svgz");
}

void IconsModel::insertIcon(const char *iconName)
{
    QtListDefaultItem *item = new QtListDefaultItem(QString()); // why 'new' and why not an empty constructor?
    item->setIcon(QIcon(QString(iconName)));
    item->setSizeHint(QSizeF(80, 60));
    appendItem(item);
}
