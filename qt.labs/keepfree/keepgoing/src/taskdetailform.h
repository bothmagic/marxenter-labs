#ifndef TASKDETAILFORM_H
#define TASKDETAILFORM_H

#include <QWidget>
#include <QModelIndex>
#include <QListWidgetItem>
#include <QPushButton>
#include <QGraphicsProxyWidget>

namespace Ui {
    class TaskDetailForm;
}

class TaskDetailForm : public QWidget
{
    Q_OBJECT

public:
    explicit TaskDetailForm(QGraphicsProxyWidget *proxy, QWidget *parent = 0);
    ~TaskDetailForm();
    bool eventFilter(QObject *obj, QEvent *event);
    void setDescription(QString description);
    void setContexts(QStringList contexts);

signals:
    void appendContext(const QString newContext);
    void removeContext(const QString context);
    void changeDescription(const QString description);

public slots:
    void dataChanged(QModelIndex index1, QModelIndex index2);
    void connectContext(QListWidgetItem *item);

protected slots:
    void appendLeNewContext();
    void storeContextAndAppendNew();
    void storeContext();
    void checkContextCompleter();

private:
    Ui::TaskDetailForm *ui;
    QPushButton *btNewContext;
    QLineEdit *leNewContext;
    QGraphicsProxyWidget *grProxy;

    void removeLeContext();


};

#endif // TASKDETAILFORM_H
