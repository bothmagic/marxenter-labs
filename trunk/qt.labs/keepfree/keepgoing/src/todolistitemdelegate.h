#ifndef TODOLISTITEMDELEGATE_H
#define TODOLISTITEMDELEGATE_H

#include <QStyledItemDelegate>

class TodoListItemDelegate : public QStyledItemDelegate
{
    Q_OBJECT

public:
    TodoListItemDelegate(QObject *parent=0);

    QWidget *createEditor(QWidget *parent, const QStyleOptionViewItem &option,
                          const QModelIndex &index) const;

    void setEditorData(QWidget *editor, const QModelIndex &index) const;
    void setModelData(QWidget *editor, QAbstractItemModel *model,
                      const QModelIndex &index) const;

    void updateEditorGeometry(QWidget *editor,
        const QStyleOptionViewItem &option, const QModelIndex &index) const;
    void paint( QPainter* painter, const QStyleOptionViewItem& option,
        const QModelIndex& index) const;

    QSize sizeHint(const QStyleOptionViewItem &option, const QModelIndex &index) const;


signals:
    void startEditing();
    void endEditing();

private:
    mutable int inEditMode;

    bool eventFilter(QObject *object, QEvent *event);
    bool editorEvent(QEvent *event,
                                    QAbstractItemModel *model,
                                    const QStyleOptionViewItem &option,
                                    const QModelIndex &index);



};

#endif // TODOLISTITEMDELEGATE_H
