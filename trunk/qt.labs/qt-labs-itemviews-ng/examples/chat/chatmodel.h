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

#ifndef CHATMODEL_H
#define CHATMODEL_H

#include "qlistmodelinterface.h"
#include <QStringList>

class ChatModel : public QtListModelInterface
{
public:
    ChatModel(QObject *parent = 0);
    ~ChatModel();

    int count() const;
    QHash<QByteArray,QVariant> data(int index, const QList<QByteArray> &roles) const;

protected:
    void appendMessage(const QString &message, int userId);
    void timerEvent(QTimerEvent *event);

private:
    QStringList messages;
    QList<int> userIds;
    int timerId;
};

#endif //CHATMODEL_H
