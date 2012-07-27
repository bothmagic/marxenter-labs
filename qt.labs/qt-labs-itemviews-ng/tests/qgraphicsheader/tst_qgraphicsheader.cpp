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
#include "qgraphicsheader.h"

class tst_QtGraphicsHeader : public QObject
{
    Q_OBJECT

public:
    tst_QtGraphicsHeader();
    virtual ~tst_QtGraphicsHeader();
    
public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();

protected:
    QtGraphicsHeader *control;
};

tst_QtGraphicsHeader::tst_QtGraphicsHeader()
{
    qRegisterMetaType<int>("Qt::SortOrder");
}

tst_QtGraphicsHeader::~tst_QtGraphicsHeader()
{
}

void tst_QtGraphicsHeader::initTestCase()
{
}

void tst_QtGraphicsHeader::cleanupTestCase()
{
}

void tst_QtGraphicsHeader::init()
{
    control = new QtGraphicsHeader();
    control->setOrientation(Qt::Vertical);

    QCOMPARE(control->orientation(), Qt::Vertical);
    QCOMPARE(control->size(), QSizeF(0, 0));
    QCOMPARE(control->offset(), 0.);
    QCOMPARE(control->maximumOffset(), 0.);
    QCOMPARE(control->firstSection(), 0);
    QCOMPARE(control->maximumFirstSection(), 0);
    QCOMPARE(control->sectionCount(), 0);
    QCOMPARE(control->contentSize(), 0.);
}

void tst_QtGraphicsHeader::cleanup()
{
    delete control;
}

void tst_QtGraphicsHeader::getSetCheck()
{
    // orientation

    control->setOrientation(Qt::Horizontal);
    QCOMPARE(control->orientation(), Qt::Horizontal);

    control->setOrientation(Qt::Vertical);
    QCOMPARE(control->orientation(), Qt::Vertical);

    // size
    
    control->setGeometry(0, 0, 30, 100);
    QCOMPARE(control->size(), QSizeF(30, 100));

    // sectionCount
    
    control->setSectionCount(100);
    QCOMPARE(control->sectionCount(), 100);

    control->setSectionCount(INT_MIN);
    QCOMPARE(control->sectionCount(), INT_MIN);

    control->setSectionCount(INT_MAX);
    QCOMPARE(control->sectionCount(), INT_MAX);

    // offset
    
    control->setOffset(10);
    QCOMPARE(control->offset(), 10.);

    control->setOffset(INT_MIN);
    QCOMPARE((int) control->offset(), INT_MIN);

    control->setOffset(INT_MAX);
    QCOMPARE((int) control->offset(), INT_MAX);

    // first section
    
    control->setFirstSection(5);
    QCOMPARE(control->firstSection(), 5);

    control->setFirstSection(INT_MIN);
    QCOMPARE(control->firstSection(), INT_MIN);

    control->setFirstSection(INT_MAX);
    QCOMPARE(control->firstSection(), INT_MAX);
}

QTEST_MAIN(tst_QtGraphicsHeader)
#include "tst_qgraphicsheader.moc"
