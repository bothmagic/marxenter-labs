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

#include "filesystem.h"
#include <QtGui>

Filesystem::Filesystem(QObject *parent)
 : QtTreeModelInterface<Node*>(parent), hiddenRoot(0)
{
    init();
}

Filesystem::~Filesystem()
{
    delete hiddenRoot;
}

Node *Filesystem::firstChild(Node *node) const
{
    if (!hiddenRoot)
        return 0;
    if (!node)
        return hiddenRoot->child;
    if (node->child)
        return node->child;

    if (node->isNotEmptyDir)
        populate(node);

    return node->child;
}

Node *Filesystem::nextSibling(Node *node) const
{
    return (node ? node->next : 0);
}

Node *Filesystem::parentItem(Node *node) const
{
    return (node ? node->parent : 0);
}

Node *Filesystem::previousSibling(Node *node) const
{
    return (node ? node->previous : 0);
}

QHash<int,QVariant> Filesystem::data(Node *node, int column, const QList<int> &roles) const
{
    QHash<int,QVariant> map;
    if (node && column == 0)
        for (int i = 0; i < roles.count(); ++i)
            if (roles.at(i) == Qt::DisplayRole)
                map.insert(Qt::DisplayRole, QFileInfo(node->path).fileName());
    return map;
}

bool Filesystem::setData(const QVariant &/*data*/, Node */*node*/, int /*column*/, int /*role*/)
{
    return false;
}

bool Filesystem::isValid(Node *node) const
{
    return !!node;
}

bool Filesystem::hasChildren(Node *node) const
{
    return node->child || node->isNotEmptyDir;
}

// Setup tree

void Filesystem::init()
{
    hiddenRoot = new Node(QLatin1String("/"));
    populate(hiddenRoot);
}

void Filesystem::populate(Node *node) const
{
    QDir dir(node->path);
    QFileInfoList entryInfoList = dir.entryInfoList(QDir::AllEntries | QDir::NoDotAndDotDot);
    Node *n = 0;
    foreach(QFileInfo fileInfo, entryInfoList) {
        Node *nn = new Node(fileInfo.filePath());
        nn->parent = node;
        if (fileInfo.isDir()) {
            QDirIterator it(nn->path, QDir::AllEntries | QDir::NoDotAndDotDot);
            nn->isNotEmptyDir = it.hasNext();
        }
        if (!node->child)
            node->child = nn;
        nn->previous = n;
        if (n)
            n->next = nn;
        n = nn;
    }
}
