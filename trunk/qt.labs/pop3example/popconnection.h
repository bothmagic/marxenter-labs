#ifndef POPCONNECTION_H
#define POPCONNECTION_H

#include <QObject>
#include <QSslSocket>

class QTextStream;
class PopConnection : public QObject
{
    Q_OBJECT

    enum State {
        Connected=0, Auth1=1, Auth2=2, Disconnected=3

    };
public:
    explicit PopConnection(QObject *parent = 0);
    
signals:
    
public slots:

private:
    QSslSocket *m_socket;
    QTextStream* t;
    State m_state;
private slots:
    void s_connected();
    void s_readyRead();
    void stateChanged(QAbstractSocket::SocketState socketState);
void errorReceived(QAbstractSocket::SocketError socketError);
void disconnected();
    
};

#endif // POPCONNECTION_H
