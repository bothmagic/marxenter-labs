#ifndef WIN_IMPL_H
#define WIN_IMPL_H

#include <QWidget>
#include <QList>
#include <QSqlRecord>
#include "ui_win.h"

class QueryThread;

class Win : public QWidget,
            protected Ui::win
{
    Q_OBJECT
  public:
    Win( QWidget* parent = 0 );
    ~Win();

  signals:
    void exec( const QString& );

  private slots:
    void slotGo();
    void slotResults( const QList<QSqlRecord>& records );

  private:
    QueryThread* m_querythread;

    void dispatch( const QString& query );
};


#endif
