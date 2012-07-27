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

#ifndef TEST_H
#define TEST_H

#include "qtreemodelinterface.h"
#include <QString>

struct Node
{
    Node() : parent(0) {}
    Node *parent;
    QList<Node*> children;
    QString text;
};

class Test : public QtTreeModelInterface<Node*>
{
public:
    Test(QObject *parent = 0);
    ~Test();
    
    Node *firstChild(Node *node) const;
    Node *nextSibling(Node *node) const;
    Node *previousSibling(Node *node) const;
    Node *parentItem(Node *node) const;
    QHash<QByteArray,QVariant> data(Node *node, int column, const QList<QByteArray> &roles) const;
    bool setData(const QVariant &data, Node *node, int column, const QByteArray &role);
    bool isValid(Node *node) const;

    // Model specific API
    void insertNode(Node *parent, int index, Node *node);
    Node *node(Node *parent, int index);
    void removeNode(Node *node);
    
private:
    void init();
    Node *hiddenRoot;
};

#endif//TEST_H
