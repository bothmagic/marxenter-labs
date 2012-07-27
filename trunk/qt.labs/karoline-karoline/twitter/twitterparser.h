#ifndef TWITTERPARSER_H
#define TWITTERPARSER_H

#include <QObject>
#include <QRunnable>
#include <QXmlDefaultHandler>
#include <QDebug>
#include <QSet>

#include "twitterApi.h"

class XmlParserHandler : public QObject, public QXmlDefaultHandler
{
    Q_OBJECT

public:
    XmlParserHandler(QObject *parent = 0);

    virtual bool startDocument();
    virtual bool endDocument();
    virtual bool startElement(const QString &namespaceURI, const QString &localName, const QString &qName, const QXmlAttributes &atts);
    virtual bool endElement(const QString &namespaceURI, const QString &localName, const QString &qName);
    virtual bool characters(const QString &ch);

protected:
    int indentationLevel;
    bool important;
    bool parsingUser;
    QString currentTag;


    // tady sbiram seznam objektu pro pridani do timeline
    TimelineList timelineList;
    TimelineItem *statusItem;

    static const QSet<QString> tags;
    static const QString TAG_STATUS;
    static const QString TAG_STATUS_TEXT;
    static const QString TAG_USER_NAME;
    static const QString TAG_USER_SCREENNAME;
    static const QString TAG_CREATED_AT;
    static const QString TAG_USER;

    QDateTime toDateTime(const QString &timestamp);
    int getMonth(const QString &month);
    void parseUser(const QString &ch);

signals:
    // TODO: v signalu predat EntryItems objecty na pridani do timeline
    void parsed(TimelineList);
};

class TwitterParser : public QRunnable
{
    // Tohleto uz budu volat z twitter api
    // pak to bude chtit pridat do konstruktoru pointer na twitterapi, abych mohl poslat zparsovana data
public:
    TwitterParser(TwitterAPI *twitterapi, QByteArray data) :
//    TwitterParser() :
        QRunnable(),
        data(data)
    {
        qRegisterMetaType<TimelineList>("TimelineList");
        parser = new XmlParserHandler();
        if (parser)
        {
            QObject::connect(parser, SIGNAL(parsed(TimelineList)), twitterapi, SIGNAL(dataParsed(TimelineList)), Qt::QueuedConnection);
        }
    }

    virtual void run()
    {
//        qDebug(Q_FUNC_INFO);
//        qDebug() << data;
        source.setData(data);
        reader.setContentHandler(parser);
        reader.parse(source);
        // proc? uklid?
//        delete parser;
    }

private:
    QByteArray data;
    XmlParserHandler *parser;
    QXmlSimpleReader reader;
    QXmlInputSource source;
};

#endif // TWITTERPARSER_H
