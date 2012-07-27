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
#include <QDebug>

Test::Test(QObject *parent)
 : QtTreeModelInterface<Node*>(parent), hiddenRoot(0)
{
    init();
}

Test::~Test()
{
}

Node *Test::firstChild(Node *node) const
{
    if (!hiddenRoot)
        return 0;
    if (node)
        return node->children.isEmpty() ? 0 : node->children.first();
    return hiddenRoot->children.isEmpty() ? 0 : hiddenRoot->children.first();
}

Node *Test::nextSibling(Node *node) const
{
    Node *next = 0;
    if (node && node->parent) { // find a faster way
        int i = node->parent->children.indexOf(node);
        if (i != -1 && i < node->parent->children.count() - 1)
            next = node->parent->children.at(i + 1);
    }
    return next;
}

Node *Test::parentItem(Node *node) const
{
    return (node && node->parent != hiddenRoot? node->parent : 0);
}

Node *Test::previousSibling(Node *node) const
{
    Node *prev = 0;
    if (node && node->parent) { // find a faster way
        int i = node->parent->children.indexOf(node);
        if (i != -1 && i > 1)
            prev = node->parent->children.at(i - 1);
    }
    return prev;
}

QHash<QByteArray,QVariant> Test::data(Node *node, int column, const QList<QByteArray> &roles) const
{
    QHash<QByteArray,QVariant> values;
    if (node && column == 0)
        for (int i = 0; i < roles.count(); ++i)
            if (roles.at(i) == "DisplayRole")
                values.insert("DisplayRole", node->text);
    return values;
}

bool Test::setData(const QVariant &data, Node *node, int column, const QByteArray &role)
{
    if (node && column == 0 && role == "DisplayRole") {
        node->text = data.toString();
        Test::iterator it = iterator(node, this);
        emit itemsChanged(it, 1, QList<QByteArray>() << "DisplayRole");
    }
    return !!node;
}

bool Test::isValid(Node *node) const
{
    return !!node;
}

// Model specific API

void Test::insertNode(Node *parent, int index, Node *node)
{
    if (parent)
        parent->children.insert(index, node);
    else
        hiddenRoot->children.insert(index, node);
    node->parent = parent;
    
    Test::iterator it = iterator(node, this);
    emit itemsInserted(it, 1);
}

Node *Test::node(Node *parent, int index)
{
    if (parent)
        return parent->children.at(index);
    return hiddenRoot->children.at(index);
}

void Test::removeNode(Node *node)
{
    if (node->parent)
        node->parent->children.removeAll(node);
    else
        hiddenRoot->children.removeAll(node);
    node->parent = 0;
    
    // ### doesn't make sense to send out a signal with an interator _after_ removal
    //change(node, 0); // invalidate

    Test::iterator it = iterator(node, this);
    emit itemsRemoved(it, 1);
}

// Setup tree

void Test::init()
{
    hiddenRoot = new Node;
    for (int i = 0; i < 1000; ++i) {
        Node *node = new Node;
        node->parent = hiddenRoot;
        node->text = QString("node ") + QString::number(i);
        hiddenRoot->children.append(node);
        for (int j = 0; j < 10; ++j) {
            Node *child = new Node;
            child->parent = node;
            child->text = QString("child ") + QString::number(j);
            node->children.append(child);
        }
    }
}
