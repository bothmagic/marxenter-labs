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
#include "spreadsheet.h"

#include <qtabledefaultmodel.h>
#include <qtablecontroller.h>
#include <qgraphicstableview.h>
#include <qprintertableview.h>
#include <qtableselectionmanager.h>

#include <QDebug>
#include <QStack>
#include <QLabel>
#include <QGraphicsWidget>
#include <QPrintDialog>
#include <QPrinter>
#include <QFileDialog>
#include <QFile>
#include <QXmlStreamReader>
#include <QXmlStreamAttributes>

SpreadSheet::SpreadSheet(QWidget *parent)
    : QMainWindow(parent)
{
    mainwindow.setupUi(this);

    QtTableDefaultModel *table = mainwindow.table->defaultModel();
    table->setRowCount(100);
    table->setColumnCount(100);

    cellLabel = new QLabel(mainwindow.toolBar);
    cellLabel->setMinimumSize(80, 0);
    mainwindow.toolBar->addWidget(cellLabel);

    connect(mainwindow.actionE_xit, SIGNAL(triggered()), QApplication::instance(), SLOT(quit()));
    connect(mainwindow.action_Print, SIGNAL(triggered()), this, SLOT(print()));
    connect(mainwindow.action_Open, SIGNAL(triggered()), this, SLOT(open()));
    mainwindow.actionShow_Grid->setChecked(true);
    connect(mainwindow.actionShow_Grid, SIGNAL(toggled(bool)), mainwindow.table->controller()->view(), SLOT(setGridShown(bool)));
    connect(mainwindow.table->controller()->selectionManager(), SIGNAL(currentChanged(int,int,int,int)),
            this, SLOT(updateStatus(int,int)));

    mainwindow.table->controller()->view()->updateLayout(); // ### FIXME
}

SpreadSheet::~SpreadSheet()
{
}

QString encode_pos(int row, int col)
{
    return QString(col + 'A') + QString::number(row + 1);
}

extern int qt_defaultDpiY();
extern int qt_defaultDpiX();

void SpreadSheet::print()
{
    QtPrinterTableView printerView(mainwindow.table->defaultModel(),
        mainwindow.table->controller()->verticalHeader(),
        mainwindow.table->controller()->horizontalHeader());

    QPrintDialog dialog(printerView.printer());
    if (dialog.exec() == QPrintDialog::Accepted)
        printerView.print();
}

void SpreadSheet::open()
{
    QString path = QFileDialog::getOpenFileName();
    if (!path.isNull())
        loadFile(path);
}

void SpreadSheet::updateStatus(int row, int column)
{
    QtTableDefaultItem *item = mainwindow.table->defaultModel()->item(row, column);
    if (item) {
        statusBar()->showMessage(item->statusTip(), 1000);
        cellLabel->setText(tr("Cell: (%1)").arg(encode_pos(row, column)));
    }
}

void SpreadSheet::loadFile(const QString &path)
{
    QtTableDefaultModel *table = mainwindow.table->defaultModel();

    //QColor titleBackground(Qt::lightGray);
    //QFont titleFont = mainwindow.table->controller()->view()->font();
    //titleFont.setBold(true);

    QFile file(path);
    file.open(QIODevice::ReadOnly);

    QXmlStreamReader reader(&file);
    while (!reader.atEnd()) {
        switch (reader.readNext()) {
        case QXmlStreamReader::StartElement:
            if (reader.name() == "table") {
                QXmlStreamAttributes attributes = reader.attributes();
                int rowCount = attributes.value("rowCount").toString().toInt();
                int columnCount = attributes.value("columnCount").toString().toInt();
                table->clear();
                table->setRowCount(rowCount); // ### FIXME: bad results happen if we swap these two
                table->setColumnCount(columnCount);
            }
            if (reader.name() == "cell") {
                QXmlStreamAttributes attributes = reader.attributes();
                QString text = attributes.value("text").toString();
                QtTableDefaultItem *item = new QtTableDefaultItem(text);
                QString background = attributes.value("background").toString();
                if (!background.isEmpty())
                    item->setBackground(QColor(background));
                int row = attributes.value("row").toString().toInt();
                int column = attributes.value("column").toString().toInt();
                table->setItem(row, column, item);
            }
            break;
        default:
            break;
        }
    }

    mainwindow.table->controller()->view()->updateLayout(); // ### FIXME
}
