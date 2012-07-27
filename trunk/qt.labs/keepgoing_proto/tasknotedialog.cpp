#include "tasknotedialog.h"
#include "ui_tasknotedialog.h"

TaskNoteDialog::TaskNoteDialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::TaskNoteDialog)
{
    ui->setupUi(this);
}

TaskNoteDialog::~TaskNoteDialog()
{
    delete ui;
}

QString TaskNoteDialog::text() {
    return ui->text->document()->toPlainText();
}
