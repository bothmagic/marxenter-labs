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

#ifndef FILESYSTEM_H
#define FILESYSTEM_H

#include "qtreemodelinterface.h"
#include <QString>

struct Node
{
    Node(const QString &p)
        :parent(0), child(0), next(0), previous(0),
         isNotEmptyDir(false),path(p){}
    ~Node() {
        Node *c = child;
        child = 0;
        while (c) {
            Node *tbd = c;
            c = c->next;
            delete tbd;
        }
    }
    Node *parent;
    Node *child;
    Node *next;
    Node *previous;
    bool isNotEmptyDir;

    QString path;
};

class Filesystem : public QtTreeModelInterface<Node*>
{
public:
    Filesystem(QObject *parent = 0);
    ~Filesystem();

    Node *firstChild(Node *node) const;
    Node *nextSibling(Node *node) const;
    Node *previousSibling(Node *node) const;
    Node *parentItem(Node *node) const;
    QHash<int,QVariant> data(Node *node, int column, const QList<int> &roles) const;
    bool setData(const QVariant &data, Node *node, int column, int role);
    bool isValid(Node *node) const;
    bool hasChildren(Node *node) const;

private:
    void init();
    void populate(Node *node) const;
    Node *hiddenRoot;
};

#endif//FILESYSTEM_H
