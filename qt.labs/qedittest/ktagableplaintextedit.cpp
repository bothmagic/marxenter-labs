#include "ktagableplaintextedit.h"
#include <QCheckBox>
#include <QStyle>
#include <QStyleOptionButton>
#include <QTextBlock>



KTagablePlainTextEdit::KTagablePlainTextEdit(QWidget *parent) :
    QTextEdit(parent)
{

    QWidget *panel = new QWidget(viewport());

    QCheckBox *check = new QCheckBox(panel);
    check->setFocusProxy(this);
    check->setCursor(QCursor(Qt::ArrowCursor));
    QRect indiRect = check->style()->subElementRect(
                QStyle::SE_CheckBoxIndicator, new QStyleOptionButton);

    QRect rect = indiRect;
    rect.moveTop(5);
    check->setGeometry(rect);
    QTextBlockFormat format;
    format.setTextIndent(indiRect.width());
    QTextCursor cursor(textCursor());
    cursor.setBlockFormat(format);
    cursor.insertText("sdafasf");

}
