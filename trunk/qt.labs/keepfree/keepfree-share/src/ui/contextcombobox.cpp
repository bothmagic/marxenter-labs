#include "contextcombobox.h"
#include <QLineEdit>

ContextComboBox::ContextComboBox(QWidget *parent) :
    QComboBox(parent)
{
    setEditable(true);




}

void ContextComboBox::focusInEvent(QFocusEvent *e)
{
    qDebug("focusin");
    QComboBox::focusInEvent(e);

}
