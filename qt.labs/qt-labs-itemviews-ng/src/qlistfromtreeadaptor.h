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

#ifndef QTLISTFROMTREEADAPTOR_H
#define QTLISTFROMTREEADAPTOR_H

#include "qlistmodelinterface.h"
#include "qtreemodelbase.h"

QT_BEGIN_HEADER

QT_BEGIN_NAMESPACE

class QtListFromTreeAdaptorPrivate;

class Q_ITEMVIEWSNG_EXPORT QtListFromTreeAdaptor : public QtListModelInterface
{
    Q_OBJECT
public:
    QtListFromTreeAdaptor(QtTreeModelBase *model, const QtTreeModelBase::iterator_base &it, int column = 0, QObject *parent = 0);
    virtual ~QtListFromTreeAdaptor();

    void setModel(QtTreeModelBase *model);
    QtTreeModelBase *model() const;

    void setParentItem(const QtTreeModelBase::iterator_base &it);
    QtTreeModelIterator parentItem() const;

    void setColumn(int column);
    int column() const;

    int count() const;
    QHash<QByteArray,QVariant> data(int index, const QList<QByteArray> &roles) const;
    bool setData(int index, const QHash<QByteArray,QVariant> &values);

protected:
    QtListFromTreeAdaptor(QtListFromTreeAdaptorPrivate &, QObject *parent = 0);
    QtListFromTreeAdaptorPrivate *d_ptr;

private:
    Q_DECLARE_PRIVATE(QtListFromTreeAdaptor)
    Q_DISABLE_COPY(QtListFromTreeAdaptor)
    Q_PRIVATE_SLOT(d_func(), void _q_modelDestroyed())
    Q_PRIVATE_SLOT(d_func(), void _q_itemsInserted(const QtTreeModelBase::iterator_base &,int))
    Q_PRIVATE_SLOT(d_func(), void _q_itemsRemoved(const QtTreeModelBase::iterator_base &,int))
    Q_PRIVATE_SLOT(d_func(), void _q_itemsChanged(const QtTreeModelBase::iterator_base &,int,const QList<QByteArray> &))
};

QT_END_NAMESPACE

QT_END_HEADER

#endif//QTLISTFROMTREEADAPTOR_H
