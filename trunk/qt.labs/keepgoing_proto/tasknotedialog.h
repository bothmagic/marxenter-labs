#ifndef TASKNOTEDIALOG_H
#define TASKNOTEDIALOG_H

#include <QDialog>

namespace Ui {
    class TaskNoteDialog;
}

class TaskNoteDialog : public QDialog
{
    Q_OBJECT

public:
    explicit TaskNoteDialog(QWidget *parent = 0);
    ~TaskNoteDialog();
    QString text();

private:
    Ui::TaskNoteDialog *ui;
};

#endif // TASKNOTEDIALOG_H
