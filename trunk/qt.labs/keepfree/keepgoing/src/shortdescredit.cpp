#include "shortdescredit.h"

ShortDescrEdit::ShortDescrEdit(QWidget *parent) :
    QLineEdit(parent)
{
    setContextMenuPolicy(Qt::NoContextMenu);
}

void ShortDescrEdit::mouseReleaseEvent(QMouseEvent *e)
{
    emit clicked();
}
