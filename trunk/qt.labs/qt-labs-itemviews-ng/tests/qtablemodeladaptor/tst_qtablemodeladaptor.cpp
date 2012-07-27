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
#include <QtTest/QtTest>
#include <qtablemodeladaptor.h>

class tst_QtTableModelAdaptor : public QObject
{
    Q_OBJECT

public:
    tst_QtTableModelAdaptor();
    virtual ~tst_QtTableModelAdaptor();

public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void checkGetData();
};

tst_QtTableModelAdaptor::tst_QtTableModelAdaptor()
{
}

tst_QtTableModelAdaptor::~tst_QtTableModelAdaptor()
{
}

void tst_QtTableModelAdaptor::initTestCase()
{
}

void tst_QtTableModelAdaptor::cleanupTestCase()
{
}

void tst_QtTableModelAdaptor::init()
{
}

void tst_QtTableModelAdaptor::cleanup()
{
}

void tst_QtTableModelAdaptor::checkGetData()
{
}

QTEST_MAIN(tst_QtTableModelAdaptor)

#include <tst_qtablemodeladaptor.moc>

