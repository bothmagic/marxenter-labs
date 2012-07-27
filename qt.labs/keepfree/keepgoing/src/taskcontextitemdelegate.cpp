#include "taskcontextitemdelegate.h"
#include <QLineEdit>
#include <QCompleter>
#include <QSqlTableModel>
#include <QDebug>
#include <QComboBox>
#include <QSqlRelationalDelegate>

TaskContextItemDelegate::TaskContextItemDelegate(QSqlTableModel *contextModel, QObject *parent) :
    QItemDelegate(parent), vcontextModel(contextModel)
{
}

QWidget* TaskContextItemDelegate::createEditor(QWidget *parent, const QStyleOptionViewItem &option, const QModelIndex &index) const
{

    QLineEdit *editor = new QLineEdit(parent);//qobject_cast<QLineEdit*>(QItemDelegate::createEditor(parent, option, index));
    editor->setFrame(true);
    QCompleter *completer = new QCompleter(vcontextModel);
    completer->setModel(vcontextModel);
    completer->setCaseSensitivity(Qt::CaseInsensitive);
    completer->setCompletionMode(QCompleter::InlineCompletion);
    completer->setCompletionColumn(1);
    completer->setWrapAround(false);

    editor->setCompleter(completer);
    return editor;
}

void TaskContextItemDelegate::updateEditorGeometry(QWidget *editor, const QStyleOptionViewItem &option, const QModelIndex &index) const
{
    QItemDelegate::updateEditorGeometry(editor, option, index);

    /*QLineEdit *lineedit = qobject_cast<QLineEdit*>(editor);
    lineedit->setGeometry(option.rect);
    lineedit->completer()->popup()->setWindowFlags();
*/

}

void TaskContextItemDelegate::setEditorData(QWidget *editor, const QModelIndex &index) const
{
    QItemDelegate::setEditorData(editor, index);
}
