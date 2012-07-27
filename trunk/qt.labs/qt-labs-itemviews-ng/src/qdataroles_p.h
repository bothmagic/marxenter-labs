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

#ifndef QTDATAROLES_P_H
#define QTDATAROLES_P_H

//
//  W A R N I N G
//  -------------
//
// This file is not part of the Qt API.  It exists purely as an
// implementation detail.  This header file may change from version to
// version without notice, or even be removed.
//
// We mean it.
//

#include <qmetaobject.h>
#include <qdebug.h>

QT_BEGIN_NAMESPACE

class QtDataRoles : private QObject
{
    Q_OBJECT
    Q_ENUMS(ItemDataRole)
public:
    // ### copy/paste from Qt namespace
    enum ItemDataRole {
        DisplayRole = 0,
        DecorationRole = 1,
        EditRole = 2,
        ToolTipRole = 3,
        StatusTipRole = 4,
        WhatsThisRole = 5,
        // Metadata
        FontRole = 6,
        TextAlignmentRole = 7,
        BackgroundColorRole = 8,
        BackgroundRole = 8,
        TextColorRole = 9,
        ForegroundRole = 9,
        CheckStateRole = 10,
        // Accessibility
        AccessibleTextRole = 11,
        AccessibleDescriptionRole = 12,
        // More general purpose
        SizeHintRole = 13,
        // Internal UiLib roles. Start worrying when public roles go that high.
        DisplayPropertyRole = 27,
        DecorationPropertyRole = 28,
        ToolTipPropertyRole = 29,
        StatusTipPropertyRole = 30,
        WhatsThisPropertyRole = 31,
        // Reserved
        UserRole = 32
    };

    static QByteArray name(int value) {
        const QMetaObject &metaObject = QtDataRoles().staticMetaObject;
        const int index = metaObject.indexOfEnumerator("ItemDataRole");
        if (index == -1)
            qWarning() << "QtDataRoles: could not find ItemDataRoles enumerator";
        const QMetaEnum metaEnum = metaObject.enumerator(index);
        return metaEnum.key(value);
    }

    static int value(const QByteArray &name) {
        const QMetaObject &metaObject = QtDataRoles().staticMetaObject;
        const int index = metaObject.indexOfEnumerator("ItemDataRole");
        if (index == -1)
            qWarning() << "QtDataRoles: could not find ItemDataRoles enumerator";
        const QMetaEnum metaEnum = metaObject.enumerator(index);
        return metaEnum.keyToValue(name);
    }
};


QT_END_NAMESPACE

#endif//QTDATAROLES_P_H
