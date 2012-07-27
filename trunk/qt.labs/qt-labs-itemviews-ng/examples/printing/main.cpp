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
#include <QtGui>

#include <qlistdefaultmodel.h>
#include <qprinterlistview.h>

int main(int argc, char **argv)
{
    QApplication app(argc, argv);
    QtListDefaultModel model;
    for (int i = 1; i <= 100; ++i)
        model.appendItem(new QtListDefaultItem(QString("This is item %1 with some default text").arg(i)));

    QtPrinterListView printerView(&model);
    //printerView.printer()->setOutputFileName("testje.pdf");
    printerView.printer()->setResolution(600);
    //printerView.setOrientation(Qt::Horizontal);

    printerView.setHeaderText("Test text for header");
    printerView.setFooterText("Test text for footer");

    QPrintDialog dialog(printerView.printer());
    if (dialog.exec() == QPrintDialog::Accepted) {
        printerView.print();
    }
    return 0;
}
