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

#ifndef QTKINETICLISTCONTROLLER_H
#define QTKINETICLISTCONTROLLER_H

#include "../qlistcontroller.h"
#include <QtCore/qmetatype.h>

QT_BEGIN_HEADER

QT_BEGIN_NAMESPACE

//QT_MODULE(Gui)

class QtKineticListControllerPrivate;
class Q_ITEMVIEWSNG_EXPORT QtKineticListController : public QtListController
{
    Q_OBJECT
    Q_PROPERTY(qreal scrollVelocity READ scrollVelocity WRITE setScrollVelocity)
    Q_PROPERTY(qreal maximumScrollVelocity READ maximumScrollVelocity WRITE setMaximumScrollVelocity)
    Q_PROPERTY(bool overshootEnabled READ isOvershootEnabled WRITE setOvershootEnabled)
    Q_PROPERTY(bool centerOnItemEnabled READ isCenterOnItemEnabled WRITE setCenterOnItemEnabled)

public:
    QtKineticListController(QObject *parent = 0);
    virtual ~QtKineticListController();

    void setScrollVelocity(qreal velocity);
    qreal scrollVelocity() const;

    void setMaximumScrollVelocity(qreal velocity);
    qreal maximumScrollVelocity() const;

    void setOvershootEnabled(bool enable);
    bool isOvershootEnabled() const;

    void setCenterOnItemEnabled(bool enable);
    bool isCenterOnItemEnabled() const;

public Q_SLOTS:
    void stop();

protected:
    bool hideEvent(QHideEvent *event);
    bool mouseMoveEvent(QGraphicsSceneMouseEvent *event, const QTransform &transform);
    bool mousePressEvent(QGraphicsSceneMouseEvent *event, const QTransform &transform);
    bool mouseReleaseEvent(QGraphicsSceneMouseEvent *event, const QTransform &transform);
    bool wheelEvent(QGraphicsSceneWheelEvent *event, const QTransform &transform);
    void timerEvent(QTimerEvent *event);

private:
    Q_DISABLE_COPY(QtKineticListController)
    Q_DECLARE_PRIVATE(QtKineticListController)
};

QT_END_HEADER

QT_END_NAMESPACE

#endif // QTKINETICLISTCONTROLLER_H
