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

#include "test2.h"
#include <QDebug>

Test2::Test2(QObject *parent)
    : QtTreeModelInterface<Path>(parent), hiddenRoot(0)
{
    init();
}

Test2::~Test2()
{
}

Path Test2::firstChild(Path path) const
{
    Node2 *node = lookup(path);
    if (node && !node->children.isEmpty()) {
        if (cachedPath == path && !cachedFirstChild.isEmpty())
            return cachedFirstChild;
        cachedPath = path; // ### cached
        path.append(0);
        cachedFirstChild = path; // ### cached
        cachedNextSibling = Path();
        cachedPreviousSibling = Path();
        return path;
    }
    return Path();
}

Path Test2::nextSibling(Path path) const
{
    if (!path.isEmpty()) {
        if (cachedPath == path && !cachedNextSibling.isEmpty())
            return cachedNextSibling;
        cachedPath = path; // ### cached

        int last = path.count() - 1;

        Node2 *parent = lookup(path, last);
        if (!parent || path.at(last) >= parent->children.count())
            return Path();
 
       ++path[last];

        cachedFirstChild = Path();
        cachedNextSibling = path; // ### cached
        cachedPreviousSibling = Path();
    }
    return path;
}

Path Test2::parentItem(Path path) const
{
    if (!path.isEmpty())
        path.removeLast();
    return path;
}

Path Test2::previousSibling(Path path) const
{
    if (!path.isEmpty()) {
        if (cachedPath == path && !cachedPreviousSibling.isEmpty())
            return cachedPreviousSibling;
        cachedPath = path; // ### cached
        int last = path.count() - 1;
        if (path.at(last) <= 0)
            return Path();
        --path[last];
        cachedFirstChild = Path();
        cachedNextSibling = Path();
        cachedPreviousSibling = path; // cached
    }
    return path;
}

QHash<int,QVariant> Test2::data(Path path, int column, const QList<int> &roles) const
{
    QHash<int, QVariant> values;
    if (column == 0)
        for (int i = 0; i < roles.count(); ++i)
            if (roles.at(i) == Qt::DisplayRole) 
                if (Node2 *node = lookup(path))
                    values.insert(Qt::DisplayRole, node->text);
    return values;
}

bool Test2::isValid(Path path) const
{
    if (path.isEmpty())
        return false;
    return !!lookup(path);
}

// Internal mapping

Node2 *Test2::lookup(Path path, int depth) const
{
    Node2 *node = hiddenRoot;
    int c = depth >= 0 ? qMin(depth, path.count()) : path.count();
    for (int i = 0; i < c; ++i) {
        int j = path.at(i);
        if (j >= 0 && j < node->children.count())
            node = node->children.at(j);
        else
            return 0; // ### something went wrong!
    }
    return node;
}

// Setup tree

void Test2::init()
{
    // It seems that most itunes collections are less that 20000 songs, and
    // only in very extreme cases does it go over 50000.
    // Over 50k songs, is still only 5000 albums. So organized in a tree, it's
    // still not that much.
    
    hiddenRoot = new Node2;
    for (int i = 0; i < 5000; ++i) {
        Node2 *node = new Node2;
        node->parent = hiddenRoot;
        node->text = QString("text ") + QString::number(i);
        hiddenRoot->children.append(node);
        for (int j = 0; j < 10; ++j) {
            Node2 *child = new Node2;
            child->parent = node;
            child->text = QString("child ") + QString::number(j);
            node->children.append(child);
            for (int k = 0; k < 10; ++k) {
                Node2 *grandchild = new Node2;
                grandchild->parent = child;
                grandchild->text = QString("grand ") + QString::number(k);
                child->children.append(grandchild);
            }
        }
    }
}
