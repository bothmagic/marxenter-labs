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

#ifndef TEST2_H
#define TEST2_H

#include "qtreemodelinterface.h"
#include <QString>
#include <QList>
#include <QDebug>

struct Node2
{
    Node2 *parent;
    QList<Node2*> children;
    QString text;
};

typedef QList<int> Path;

// ### hacky
inline uint qHash(QList<int> t) { return qHash(t.begin().i->v); }

class Test2 : public QtTreeModelInterface<Path>
{
public:
    Test2(QObject *parent = 0);
    ~Test2();

    Path firstChild(Path path) const;
    Path nextSibling(Path path) const;
    Path parentItem(Path path) const;
    Path previousSibling(Path path) const;
    QHash<int,QVariant> data(Path path, int column, const QList<int> &roles) const;
    bool isValid(Path path) const;
//    uint hash(Path path) const { qDebug() << "####### hash ######"; return qHash(path.begin().i->v); }

protected:
    Node2 *lookup(Path path, int depth = -1) const;

private:
    void init();
    Node2 *hiddenRoot;

    // optimizations
    mutable Path cachedPath;
    mutable Path cachedFirstChild;
    mutable Path cachedNextSibling;
    mutable Path cachedPreviousSibling;
};

#endif//TEST2_H
