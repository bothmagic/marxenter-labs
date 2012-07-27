#ifndef KGPROJECTLISTWIDGET_H
#define KGPROJECTLISTWIDGET_H

#include <QListWidget>

class KGProjectListWidget : public QListWidget
{
    Q_OBJECT
public:
    explicit KGProjectListWidget(QWidget *parent = 0);

signals:
    void moveItem(int group, int item, int destGroup);
    void removeRow(int group);
public slots:

protected:
    void dropEvent(QDropEvent *event);
    void dragEnterEvent(QDragEnterEvent *event);
    void dragMoveEvent(QDragMoveEvent *event);
    void keyReleaseEvent(QKeyEvent *event);

};

#endif // KGPROJECTLISTWIDGET_H
