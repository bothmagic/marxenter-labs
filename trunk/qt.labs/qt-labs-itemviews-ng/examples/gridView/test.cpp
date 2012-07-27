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

#include "test.h"

Test::Test(QObject *parent)
  : QtListModelInterface(parent)
{
    init();
}

Test::~Test()
{
}

int Test::count() const
{
    return icons.count();
}

QHash<QByteArray,QVariant> Test::data(int index, const QList<QByteArray> &roles) const
{
    QHash<QByteArray,QVariant> hash;
    if (index >= 0 && index < icons.count())
        for (int i = 0; i < roles.count(); ++i)
            if (roles.at(i) == "DecorationRole")
                hash.insert("DecorationRole", icons.at(index));
    return hash;
}

bool Test::moveItems(int from, int to, int count)
{
    QList<QIcon> moved;
    for (int i = 0; i < count; ++i)
        moved.append(icons.takeAt((from)));
    for (int j = 0; j < count; ++j)
        moved.insert(to, moved.takeFirst());
    emit itemsMoved(from, to, count);
    return true;
}

void Test::init()
{
    icons << QIcon(":images/user_32.png")
          << QIcon(":images/user_32.png")
          << QIcon(":images/user_32.png")
          << QIcon(":images/user_32.png")
          << QIcon(":images/user_32.png")
          << QIcon(":images/user_32.png")
          << QIcon(":images/user_32.png")
          << QIcon(":images/user_32.png")
          << QIcon(":images/user_32.png");
}
