#ifndef KGMAIN_H
#define KGMAIN_H

#include <QMainWindow>
class TaskModel;
class KGItemModel;

namespace Ui {
    class KGMain;
}

class KGMain : public QMainWindow
{
    Q_OBJECT

public:
    explicit KGMain(QWidget *parent = 0);
    ~KGMain();

private:
    Ui::KGMain *ui;
    TaskModel *taskModel;

    KGItemModel *m_model;

    void initTodoModel();
    void initDockWindows();
    void initToolbar();

    void initContextModel();

private slots:
    void createProject();
};

#endif // KGMAIN_H
