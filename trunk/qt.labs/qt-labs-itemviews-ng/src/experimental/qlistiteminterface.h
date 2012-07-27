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

#ifndef QLISTITEMINTERFACE_H
#define QLISTITEMINTERFACE_H

#include <QtCore/qobject.h>
#include <QtCore/qstring.h>
#include <QtGui/qicon.h>

QT_BEGIN_HEADER

QT_BEGIN_NAMESPACE

//QT_MODULE(Gui)

class QtListModelInterface;
class QtListItemInterfacePrivate;

class Q_ITEMVIEWSNG_EXPORT QtListItemInterface : public QObject
{
    Q_OBJECT
    Q_PROPERTY(int index READ index WRITE setIndex)
    Q_PROPERTY(QString text READ text WRITE setText NOTIFY textChanged)
    Q_PROPERTY(QIcon icon READ icon WRITE setIcon NOTIFY iconChanged)

public:
    QtListItemInterface(QtListModelInterface *model);
    explicit QtListItemInterface(int index, QtListModelInterface *model);
    virtual ~QtListItemInterface();

    void setIndex(int index);
    int index() const;

    void setText(const QString &text);
    QString text() const;

    void setIcon(const QIcon &icon);
    QIcon icon() const;

Q_SIGNALS:
    void textChanged(const QString &text);
    void iconChanged(const QIcon &icon);

protected:
    QtListItemInterfacePrivate *d_ptr;

private:
    Q_DECLARE_PRIVATE(QtListItemInterface);
    Q_DISABLE_COPY(QtListItemInterface);
    Q_PRIVATE_SLOT(d_func(), void _q_modelDestroyed())
    Q_PRIVATE_SLOT(d_func(), void _q_itemsInserted(int index, int count))
    Q_PRIVATE_SLOT(d_func(), void _q_itemsRemoved(int index, int count))
    Q_PRIVATE_SLOT(d_func(), void _q_itemsMoved(int from, int to, int count))
    Q_PRIVATE_SLOT(d_func(), void _q_itemsChanged(int index, int count, const QList<int> &roles))
};

QT_END_NAMESPACE

QT_END_HEADER

#endif // QLISTITEMLINTERFACE_H
