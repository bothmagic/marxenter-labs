#ifndef TODOGRAPHICSVIEW_H
#define TODOGRAPHICSVIEW_H

#include <QGraphicsView>
#include <QGraphicsLinearLayout>
#include <QModelIndex>

class QUndoStack;
class KGVisualItem;
class QAbstractItemModel;
class KGVisualContextList;
class KGVisualItemGroup;
class QAbstractItemModel;

class KGVisualView: public QGraphicsView
{
    Q_OBJECT

public:
    KGVisualView(QWidget *parent);
    ~KGVisualView();
    void clear();
    //void acceptFilter();
    void filterContext(QStringList contextList);

    KGVisualItem* visualItem(int group, int item);

    int createGroup(QString const &groupName);

    KGVisualItemGroup *removeGroup(int group);
    QList<KGVisualItemGroup *> allGroups();

    void moveVisualItem(int fromGroup, int item, int toGroup);
    void scrollToGroup(int group);
    void setModel(QAbstractItemModel *model);
    QAbstractItemModel* model() const;


signals:
    void newItem(KGVisualItem *item);
    void visualItemAdded(int group, int item);
    void visualItemRemoved(int group, int visualItem);
    void visualItemUpdated(int group, int item, QString objectName);


public slots:
    void reload();
    void fieldChanged(int row, const char* name, QVariant value);
    void insertItem(KGVisualItem *item, int group = 0, int index = -1);
    void removeItem(KGVisualItem *item);

protected:

    void mouseReleaseEvent(QMouseEvent *event);
    void mouseMoveEvent(QMouseEvent *event);
    void mousePressEvent(QMouseEvent *event);
    void keyPressEvent(QKeyEvent *event);
    void resizeEvent(QResizeEvent *event);

private:

    bool p_createNewItemOnMouseRelease;
    QGraphicsItem *p_lastSelectedSingleItem;
    QGraphicsWidget *p_globalItemGroup;
    QList <KGVisualItemGroup*> m_groupList;
    QAbstractItemModel *m_model;

    void p_updateSceneRect();
    void p_updateGroupLayout();

private slots:

    void visualItemAdded(KGVisualItemGroup *group, KGVisualItem *visualItem);
    void itemUpdated(KGVisualItem *item, QString objectName);
    void visualItemStartDrag(KGVisualItem *visualItem, QGraphicsSceneMouseEvent *event);

    void modelChanged(QModelIndex parent, int start,int end);
};

#endif // TODOGRAPHICSVIEW_H
