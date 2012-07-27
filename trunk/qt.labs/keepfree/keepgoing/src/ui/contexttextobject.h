#ifndef CONTEXTTEXTOBJECT_H
#define CONTEXTTEXTOBJECT_H

#include <QTextObjectInterface>

class QTextDocument;
class QTextFormat;
class QPainter;
class QRectF;
class QSizeF;

class ContextTextObject : public QObject, public QTextObjectInterface
{
    Q_OBJECT
    Q_INTERFACES(QTextObjectInterface)


public:
   QSizeF intrinsicSize(QTextDocument *doc, int posInDocument,
                        const QTextFormat &format);
   void drawObject(QPainter *painter, const QRectF &rect, QTextDocument *doc,
                   int posInDocument, const QTextFormat &format);


};

#endif // CONTEXTTEXTOBJECT_H
