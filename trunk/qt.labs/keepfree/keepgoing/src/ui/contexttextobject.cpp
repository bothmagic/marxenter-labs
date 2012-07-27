#include "contexttextobject.h"
#include "qgraphicslisttext.h"
#include <QFont>
#include <QTextFormat>
#include <QPainter>

QSizeF ContextTextObject::intrinsicSize(QTextDocument * doc, int /*posInDocument*/,
                                     const QTextFormat &format)
 {
    QSizeF contextSize = qVariantValue<QSizeF>(format.property(QGraphicsListText::ContextRect));
    contextSize -= QSizeF(-5,9);
    return contextSize;
 }

 void ContextTextObject::drawObject(QPainter *painter, const QRectF &rect,
                                QTextDocument * /*doc*/, int /*posInDocument*/,
                                const QTextFormat &format)
 {
    painter->setFont(format.toCharFormat().font());
    QString contextName = qVariantValue<QString>(format.property(QGraphicsListText::ContextName));
    painter->setBrush(QBrush(QColor(225,225,225)));
    painter->setPen(Qt::NoPen);
    QRectF border = rect;
    border.adjust(1,-1,-1,2);
    painter->drawRoundedRect(border, 2, 2);
    border = rect;
    border.adjust(0,2,0,0);
    painter->setPen(Qt::black);
    painter->drawText(border, Qt::AlignCenter,contextName);

 }

