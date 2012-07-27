/********************************************************************************
** Form generated from reading UI file 'tasklistform.ui'
**
** Created: Tue 3. May 10:16:47 2011
**      by: Qt User Interface Compiler version 4.7.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_TASKLISTFORM_H
#define UI_TASKLISTFORM_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QGridLayout>
#include <QtGui/QHeaderView>
#include <QtGui/QWidget>
#include "src/ui/kgvisualview.h"

QT_BEGIN_NAMESPACE

class Ui_TaskListForm
{
public:
    QGridLayout *gridLayout;
    KGVisualView *kgVisualView;

    void setupUi(QWidget *TaskListForm)
    {
        if (TaskListForm->objectName().isEmpty())
            TaskListForm->setObjectName(QString::fromUtf8("TaskListForm"));
        TaskListForm->resize(574, 456);
        gridLayout = new QGridLayout(TaskListForm);
        gridLayout->setContentsMargins(0, 0, 0, 0);
        gridLayout->setObjectName(QString::fromUtf8("gridLayout"));
        gridLayout->setHorizontalSpacing(0);
        gridLayout->setVerticalSpacing(5);
        kgVisualView = new KGVisualView(TaskListForm);
        kgVisualView->setObjectName(QString::fromUtf8("kgVisualView"));
        QSizePolicy sizePolicy(QSizePolicy::Expanding, QSizePolicy::MinimumExpanding);
        sizePolicy.setHorizontalStretch(0);
        sizePolicy.setVerticalStretch(0);
        sizePolicy.setHeightForWidth(kgVisualView->sizePolicy().hasHeightForWidth());
        kgVisualView->setSizePolicy(sizePolicy);
        kgVisualView->setAcceptDrops(true);
        kgVisualView->setAlignment(Qt::AlignLeading|Qt::AlignLeft|Qt::AlignTop);
        kgVisualView->setRenderHints(QPainter::Antialiasing|QPainter::HighQualityAntialiasing|QPainter::SmoothPixmapTransform|QPainter::TextAntialiasing);
        kgVisualView->setDragMode(QGraphicsView::RubberBandDrag);
        kgVisualView->setTransformationAnchor(QGraphicsView::AnchorViewCenter);
        kgVisualView->setResizeAnchor(QGraphicsView::AnchorViewCenter);
        kgVisualView->setViewportUpdateMode(QGraphicsView::SmartViewportUpdate);

        gridLayout->addWidget(kgVisualView, 0, 0, 1, 1);


        retranslateUi(TaskListForm);

        QMetaObject::connectSlotsByName(TaskListForm);
    } // setupUi

    void retranslateUi(QWidget *TaskListForm)
    {
        TaskListForm->setWindowTitle(QApplication::translate("TaskListForm", "Form", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class TaskListForm: public Ui_TaskListForm {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_TASKLISTFORM_H
