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

#ifndef QTLISTMODELINTERFACE_H
#define QTLISTMODELINTERFACE_H

#include "qitemviewsglobal.h"

#include <QtCore/qhash.h>
#include <QtCore/qvariant.h>
#include <QtCore/qobject.h>

QT_BEGIN_HEADER

QT_BEGIN_NAMESPACE

//QT_MODULE(Gui)

class Q_ITEMVIEWSNG_EXPORT QtListModelInterface : public QObject
{
    Q_OBJECT
public:
    QtListModelInterface(QObject *parent = 0);
    virtual ~QtListModelInterface();

    virtual int count() const = 0;
    virtual QHash<QByteArray,QVariant> data(int index, const QList<QByteArray> &roles = (QList<QByteArray>())) const = 0;
    virtual bool setData(int index, const QHash<QByteArray,QVariant> &values);

Q_SIGNALS:
    void itemsInserted(int index, int count);
    void itemsRemoved(int index, int count);
    void itemsMoved(int from, int to, int count);
    void itemsChanged(int index, int count, const QList<QByteArray> &roles);

protected:
    QtListModelInterface(QObjectPrivate &dd, QObject *parent);
};

QT_END_NAMESPACE

QT_END_HEADER

#endif // QTLISTMODELINTERFACE_H
