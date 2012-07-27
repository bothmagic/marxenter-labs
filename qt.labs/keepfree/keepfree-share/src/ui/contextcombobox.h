#ifndef CONTEXTCOMBOBOX_H
#define CONTEXTCOMBOBOX_H

#include <QComboBox>
#include <QCursor>

class ContextComboBox : public QComboBox
{
    Q_OBJECT
public:
    explicit ContextComboBox(QWidget *parent = 0);
    QCursor cursor();

signals:

public slots:

private:
    void focusInEvent(QFocusEvent *e);

};

#endif // CONTEXTCOMBOBOX_H
