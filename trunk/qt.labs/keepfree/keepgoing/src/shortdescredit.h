#ifndef SHORTDESCREDIT_H
#define SHORTDESCREDIT_H

#include <QLineEdit>

class ShortDescrEdit : public QLineEdit
{
    Q_OBJECT
public:
    explicit ShortDescrEdit(QWidget *parent = 0);

signals:
    void clicked();

public slots:

protected:
    void mouseReleaseEvent(QMouseEvent *e);

};

#endif // SHORTDESCREDIT_H
