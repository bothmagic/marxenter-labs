/****************************************************************************
**
** Copyright (C) 2010 Nokia Corporation and/or its subsidiary(-ies).
** All rights reserved.
** Contact: Nokia Corporation (qt-info@nokia.com)
**
** This file is part of the Itemviews NG project on Trolltech Labs.
**
** GNU Lesser General Public License Usage
** This file may be used under the terms of the GNU Lesser
** General Public License version 2.1 as published by the Free Software
** Foundation and appearing in the file LICENSE.LGPL included in the
** packaging of this file.  Please review the following information to
** ensure the GNU Lesser General Public License version 2.1 requirements
** will be met: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html.
**
** If you have questions regarding the use of this file, please contact
** Nokia at qt-info@nokia.com.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
****************************************************************************/

#ifndef QTTREESELECTIONMANAGER_H
#define QTTREESELECTIONMANAGER_H

#include "qtreemodelinterface.h"

#include <QtCore/qlist.h>
#include <QtCore/qobject.h>

QT_BEGIN_HEADER

QT_BEGIN_NAMESPACE

//QT_MODULE(Gui)

class QtTreeSelectionManagerPrivate;

typedef QSet<QtTreeModelBase::iterator_base> QtTreeSelection;

class Q_ITEMVIEWSNG_EXPORT QtTreeSelectionManager : public QObject
{
    Q_OBJECT
public:
    enum SelectionMode {
        Select,
        Deselect,
        Toggle
    };

    QtTreeSelectionManager(QObject *parent = 0);
    ~QtTreeSelectionManager();

    QtTreeModelIterator currentItem() const;
    QtTreeModelIterator anchorItem() const;
    bool isSelected(const QtTreeModelBase::iterator_base &it) const;

    SelectionMode anchorSelectionMode() const;

public Q_SLOTS:
    void setCurrentItem(const QtTreeModelIterator &it);
    void setAnchorItem(const QtTreeModelIterator &it);
    void setAnchorSelectionMode(SelectionMode mode);
    void setSelected(const QtTreeSelection &selection, SelectionMode mode = Select);
    void setAnchorSelection(const QtTreeSelection &selection);
    void clearSelections();
    
Q_SIGNALS:
    void currentChanged(const QtTreeModelBase::iterator_base &current, const QtTreeModelBase::iterator_base &previous);
    void anchorChanged(const QtTreeModelBase::iterator_base &current, const QtTreeModelBase::iterator_base &previous);
    void selectionsChanged(const QtTreeSelection &selection);

protected:
    QtTreeSelectionManager(QtTreeSelectionManagerPrivate &, QObject *parent = 0);
    QtTreeSelectionManagerPrivate *d_ptr;

private:
    Q_DECLARE_PRIVATE(QtTreeSelectionManager)
    Q_DISABLE_COPY(QtTreeSelectionManager)
    Q_PRIVATE_SLOT(d_func(), void _q_modelDestroyed())
    Q_PRIVATE_SLOT(d_func(), void _q_itemsRemoved(const QtTreeModelBase::iterator_base &it, int count))
};

QT_END_NAMESPACE

QT_END_HEADER

#endif//QTTREESELECTIONMANAGER_H
