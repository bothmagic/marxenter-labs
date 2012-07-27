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
#include <qtreeselectionmanager.h>
#include <qtreedefaultmodel.h>

class tst_QtTreeSelectionManager : public QObject
{
    Q_OBJECT

public:
    enum SelectionMode {
        Select = QtTreeSelectionManager::Select,
        Deselect = QtTreeSelectionManager::Deselect,
        Toggle = QtTreeSelectionManager::Toggle
    };

    tst_QtTreeSelectionManager();
    virtual ~tst_QtTreeSelectionManager();
    
public slots:
    void initTestCase();
    void cleanupTestCase();
    void init();
    void cleanup();

private slots:
    void getSetCheck();
    void currentItem_data();
    void currentItem();
    void anchorItem_data();
    void anchorItem();
    void selections_data();
    void selections();
    void anchoredSelections_data();
    void anchoredSelections();

private:
    QtTreeSelectionManager *manager;
    QtTreeDefaultModel *model;
};

Q_DECLARE_METATYPE(QList<int>)
Q_DECLARE_METATYPE(QList<bool>)
Q_DECLARE_METATYPE(QtTreeModelBase::iterator_base)

tst_QtTreeSelectionManager::tst_QtTreeSelectionManager()
{
    qRegisterMetaType<QtTreeModelBase::iterator_base>("QtTreeModelBase::iterator_base");
}

tst_QtTreeSelectionManager::~tst_QtTreeSelectionManager()
{
}

void tst_QtTreeSelectionManager::initTestCase()
{
}

void tst_QtTreeSelectionManager::cleanupTestCase()
{
}

void tst_QtTreeSelectionManager::init()
{
    manager = new QtTreeSelectionManager();
    model = new QtTreeDefaultModel();
}

void tst_QtTreeSelectionManager::cleanup()
{
    delete manager;
    delete model;
}

void tst_QtTreeSelectionManager::getSetCheck()
{
}

void tst_QtTreeSelectionManager::currentItem_data()
{
    QTest::addColumn<QStringList>("items");
    QTest::addColumn<QString>("initialCurrent");
    QTest::addColumn<QString>("expectedCurrent");
    QTest::addColumn<QString>("newCurrent");
    QTest::addColumn<int>("insertAt");
    QTest::addColumn<int>("insertCount");
    QTest::addColumn<int>("removeAt");
    QTest::addColumn<int>("removeCount");
    // ### move
    QTest::addColumn<int>("signalCount");

    QTest::newRow("empty")
            << QStringList()
            << QString()
            << QString()
            << QString()
            << 0 << 0
            << 0 << 0
            << 0;
}

void tst_QtTreeSelectionManager::currentItem()
{
    QFETCH(QStringList, items);
    QFETCH(QString, initialCurrent);
    QFETCH(QString, expectedCurrent);
    QFETCH(QString, newCurrent);
    QFETCH(int, insertAt);
    QFETCH(int, insertCount);
    QFETCH(int, removeAt);
    QFETCH(int, removeCount);
    QFETCH(int, signalCount);

    QSignalSpy currentChanged(manager, SIGNAL(currentChanged(const QtTreeModelBase::iterator_base&, const QtTreeModelBase::iterator_base&)));
}

void tst_QtTreeSelectionManager::anchorItem_data()
{
}

void tst_QtTreeSelectionManager::anchorItem()
{
}

void tst_QtTreeSelectionManager::selections_data()
{
}

void tst_QtTreeSelectionManager::selections()
{
}

void tst_QtTreeSelectionManager::anchoredSelections_data()
{
}

void tst_QtTreeSelectionManager::anchoredSelections()
{
}

QTEST_MAIN(tst_QtTreeSelectionManager)
#include "tst_qtreeselectionmanager.moc"
