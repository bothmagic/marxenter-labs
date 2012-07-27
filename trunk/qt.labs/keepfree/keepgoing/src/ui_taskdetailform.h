/********************************************************************************
** Form generated from reading UI file 'taskdetailform.ui'
**
** Created: Sat 18. Dec 21:35:01 2010
**      by: Qt User Interface Compiler version 4.7.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_TASKDETAILFORM_H
#define UI_TASKDETAILFORM_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QDateEdit>
#include <QtGui/QGridLayout>
#include <QtGui/QHeaderView>
#include <QtGui/QLabel>
#include <QtGui/QSpacerItem>
#include <QtGui/QTextEdit>
#include <QtGui/QWidget>

QT_BEGIN_NAMESPACE

class Ui_TaskDetailForm
{
public:
    QGridLayout *gridLayout;
    QDateEdit *dateEdit;
    QSpacerItem *horizontalSpacer;
    QLabel *label;
    QWidget *contextLayout;
    QTextEdit *textEdit;

    void setupUi(QWidget *TaskDetailForm)
    {
        if (TaskDetailForm->objectName().isEmpty())
            TaskDetailForm->setObjectName(QString::fromUtf8("TaskDetailForm"));
        TaskDetailForm->setWindowModality(Qt::WindowModal);
        TaskDetailForm->resize(709, 200);
        QSizePolicy sizePolicy(QSizePolicy::Preferred, QSizePolicy::Fixed);
        sizePolicy.setHorizontalStretch(0);
        sizePolicy.setVerticalStretch(0);
        sizePolicy.setHeightForWidth(TaskDetailForm->sizePolicy().hasHeightForWidth());
        TaskDetailForm->setSizePolicy(sizePolicy);
        TaskDetailForm->setMinimumSize(QSize(0, 200));
        TaskDetailForm->setMaximumSize(QSize(16777215, 200));
        TaskDetailForm->setFocusPolicy(Qt::StrongFocus);
        TaskDetailForm->setContextMenuPolicy(Qt::NoContextMenu);
        TaskDetailForm->setAutoFillBackground(false);
        gridLayout = new QGridLayout(TaskDetailForm);
        gridLayout->setContentsMargins(0, 0, 0, 0);
        gridLayout->setObjectName(QString::fromUtf8("gridLayout"));
        gridLayout->setHorizontalSpacing(7);
        gridLayout->setVerticalSpacing(4);
        dateEdit = new QDateEdit(TaskDetailForm);
        dateEdit->setObjectName(QString::fromUtf8("dateEdit"));
        dateEdit->setFrame(true);
        dateEdit->setButtonSymbols(QAbstractSpinBox::UpDownArrows);
        dateEdit->setCorrectionMode(QAbstractSpinBox::CorrectToNearestValue);
        dateEdit->setCalendarPopup(true);

        gridLayout->addWidget(dateEdit, 3, 1, 1, 1);

        horizontalSpacer = new QSpacerItem(40, 20, QSizePolicy::Expanding, QSizePolicy::Minimum);

        gridLayout->addItem(horizontalSpacer, 3, 2, 1, 1);

        label = new QLabel(TaskDetailForm);
        label->setObjectName(QString::fromUtf8("label"));
        label->setFrameShape(QFrame::NoFrame);
        label->setFrameShadow(QFrame::Plain);
        label->setLineWidth(0);
        label->setPixmap(QPixmap(QString::fromUtf8(":/img/mark_icon.png")));
        label->setScaledContents(false);
        label->setAlignment(Qt::AlignLeading|Qt::AlignLeft|Qt::AlignTop);
        label->setMargin(0);
        label->setIndent(0);

        gridLayout->addWidget(label, 0, 0, 1, 1);

        contextLayout = new QWidget(TaskDetailForm);
        contextLayout->setObjectName(QString::fromUtf8("contextLayout"));
        QSizePolicy sizePolicy1(QSizePolicy::Preferred, QSizePolicy::MinimumExpanding);
        sizePolicy1.setHorizontalStretch(0);
        sizePolicy1.setVerticalStretch(0);
        sizePolicy1.setHeightForWidth(contextLayout->sizePolicy().hasHeightForWidth());
        contextLayout->setSizePolicy(sizePolicy1);
        contextLayout->setMinimumSize(QSize(0, 40));
        contextLayout->setMaximumSize(QSize(16777215, 16777215));
        contextLayout->setFocusPolicy(Qt::StrongFocus);

        gridLayout->addWidget(contextLayout, 0, 1, 2, 2);

        textEdit = new QTextEdit(TaskDetailForm);
        textEdit->setObjectName(QString::fromUtf8("textEdit"));
        textEdit->setMouseTracking(false);
        textEdit->setFocusPolicy(Qt::WheelFocus);
        textEdit->setContextMenuPolicy(Qt::NoContextMenu);
        textEdit->setFrameShape(QFrame::NoFrame);
        textEdit->setAutoFormatting(QTextEdit::AutoAll);
        textEdit->setTextInteractionFlags(Qt::LinksAccessibleByKeyboard|Qt::LinksAccessibleByMouse|Qt::TextBrowserInteraction|Qt::TextEditable|Qt::TextEditorInteraction|Qt::TextSelectableByKeyboard|Qt::TextSelectableByMouse);

        gridLayout->addWidget(textEdit, 2, 1, 1, 2);


        retranslateUi(TaskDetailForm);

        QMetaObject::connectSlotsByName(TaskDetailForm);
    } // setupUi

    void retranslateUi(QWidget *TaskDetailForm)
    {
        TaskDetailForm->setWindowTitle(QApplication::translate("TaskDetailForm", "Form", 0, QApplication::UnicodeUTF8));
        dateEdit->setSpecialValueText(QApplication::translate("TaskDetailForm", "test", 0, QApplication::UnicodeUTF8));
        label->setText(QString());
#ifndef QT_NO_TOOLTIP
        textEdit->setToolTip(QString());
#endif // QT_NO_TOOLTIP
    } // retranslateUi

};

namespace Ui {
    class TaskDetailForm: public Ui_TaskDetailForm {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_TASKDETAILFORM_H
