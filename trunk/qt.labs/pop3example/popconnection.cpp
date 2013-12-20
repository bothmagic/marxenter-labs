#include "popconnection.h"
#include <QDebug>
#include <QDataStream>
PopConnection::PopConnection(QObject *parent) :
    QObject(parent)
{

    m_socket= new QSslSocket(this);
    connect(m_socket, SIGNAL(connected()), this, SLOT(s_connected()));
    connect(m_socket, SIGNAL(readyRead()), this, SLOT(s_readyRead()));
    connect(m_socket, SIGNAL(error(QAbstractSocket::SocketError)), this,
            SLOT(errorReceived(QAbstractSocket::SocketError)));
    connect(m_socket, SIGNAL(stateChanged( QAbstractSocket::SocketState)), this,
            SLOT(stateChanged(QAbstractSocket::SocketState)));
    connect(m_socket, SIGNAL(disconnected()), this,
            SLOT(disconnected()));;

    m_socket->connectToHostEncrypted("pop.googlemail.com", 995);

    t = new QTextStream( m_socket );

}

void PopConnection::stateChanged(QAbstractSocket::SocketState socketState)
{

    qDebug() <<"stateChanged " << socketState;
}

void PopConnection::errorReceived(QAbstractSocket::SocketError socketError)
{
    qDebug() << "error " <<socketError;
}

void PopConnection::disconnected()
{

    qDebug() <<"disconneted";
    qDebug() << "error " << m_socket->errorString();
}

void PopConnection::s_connected() {
    qDebug()<<"connected";
    m_state = Connected;

}

void PopConnection::s_readyRead() {

    qDebug() <<"readyRead";
    QString responseLine;
    do
    {
        responseLine = m_socket->readLine();

    }
    while ( m_socket->canReadLine());

    qDebug() << responseLine;

    if (m_state == Connected) {

        *t << "USER " << "markus.taubert@gmail.com\r\n";
        t->flush();
        m_state = Auth1;
    } else if (m_state == Auth1) {

        *t << "PASS " << "edgar010\r\n";
        t->flush();
        m_state = Auth2;
    } else if (m_state == Auth2) {

        *t << "STAT\r\n";
        t->flush();
        m_state = Disconnected;
    }

}
