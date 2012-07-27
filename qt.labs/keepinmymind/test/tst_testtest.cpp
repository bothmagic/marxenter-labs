#include <QtCore/QString>
#include <QtTest/QtTest>
#include "gui/mainwindow.h"

class TestTest : public QObject
{
    Q_OBJECT
    
public:
    TestTest();
    
private Q_SLOTS:
    void testCase1();
    void testCase1_data();
};

TestTest::TestTest()
{
}

void TestTest::testCase1()
{
    QFETCH(QString, data);
    QVERIFY2(true, "Failure");

}

void TestTest::testCase1_data()
{
    QTest::addColumn<QString>("data");
    QTest::newRow("0") << QString();
}


class TestTest2 : public QObject
{
    Q_OBJECT

public:
    TestTest2();

private Q_SLOTS:
    void testCase2();
    void testCase2_data();
};

TestTest2::TestTest2()
{
}

void TestTest2::testCase2()
{
    QFETCH(QString, data);
    QVERIFY2(true, "Failure");

}

void TestTest2::testCase2_data()
{
    QTest::addColumn<QString>("data");
    QTest::newRow("0") << QString();
}

int main(int argc, char** argv)
{
    QCoreApplication app(argc, argv);

    QTest::qExec(new TestTest, argc, argv);
    QTest::qExec(new TestTest2, argc, argv);
}


#include "tst_testtest.moc"
