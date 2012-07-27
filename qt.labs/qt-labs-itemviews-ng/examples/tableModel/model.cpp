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
#include "model.h"

MyModel::MyModel(QObject *parent)
    : QtTableModelInterface(parent)
{
}

QHash<QByteArray, QVariant> MyModel::data(int row, int column, const QList<QByteArray> &roles) const
{
    QHash<QByteArray, QVariant> answer;
    if (roles.contains("DisplayRole")) {
        switch (column) {
        case 0:
            answer.insert("DisplayRole", QString::number(row+1));
            break;
        case 1:
            answer.insert("DisplayRole", QString::number((row+1) % 10));
            break;
        case 2:
            answer.insert("DisplayRole", QString::number((row+1) % 100));
            break;
        case 3: {
            QString x;
            switch (row%4) {
                case 0: x = "Milk"; break;
                case 1: x = "Remember"; break;
                case 2: x = "The"; break;
            }
            answer.insert("DisplayRole", x);
            break;
        }
        default:
            if (row == 0)
                answer.insert("DisplayRole", QString("Filler space here"));
        }
    }
    return answer;
}

int MyModel::rowCount() const
{
    return 1E6;
}

int MyModel::columnCount() const
{
    return 5;
}
