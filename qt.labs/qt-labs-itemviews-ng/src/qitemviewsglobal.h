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

#ifndef QITEMVIEWSNGGLOBAL_H
#define QITEMVIEWSNGGLOBAL_H

#include <QtCore/qglobal.h>

#if defined(Q_OS_WIN)
#   if defined(QT_BUILD_ITEMVIEWS_LIB)
#       ifndef Q_ITEMVIEWSNG_EXPORT
#           define Q_ITEMVIEWSNG_EXPORT Q_DECL_EXPORT
#       endif
#   else
#       ifndef Q_ITEMVIEWSNG_EXPORT
#           define Q_ITEMVIEWSNG_EXPORT Q_DECL_IMPORT
#       endif
#   endif
#else
#       ifndef Q_ITEMVIEWSNG_EXPORT
#           define Q_ITEMVIEWSNG_EXPORT
#		endif
#endif

#endif //QITEMVIEWSNGGLOBAL_H

