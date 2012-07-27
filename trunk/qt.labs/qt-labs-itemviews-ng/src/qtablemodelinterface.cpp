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

#include "qtablemodelinterface.h"
#include "qdataroles_p.h"

QT_BEGIN_NAMESPACE

/*!
    \class QtTableModelInterface
    \brief api docs go here
*/

QtTableModelInterface::QtTableModelInterface(QObject *parent)
    : QObject(parent)
{
}

QtTableModelInterface::QtTableModelInterface(QObjectPrivate &dd, QObject *parent)
    : QObject(dd, parent)
{
}

QtTableModelInterface::~QtTableModelInterface()
{
}

/*!
    Sets the data for the cell at \a row and \a column to the given \a values.
    Returns true if the data was set on the cell; returns false otherwise.

    The default implementation does not set the data, and will always return
    false.
*/
bool QtTableModelInterface::setData(int row, int column, const QHash<QByteArray,QVariant> &values)
{
    Q_UNUSED(row);
    Q_UNUSED(column);
    Q_UNUSED(values);
    return false;
}

QT_END_NAMESPACE
