#include "twitterparser.h"
#include <QtDebug>

const QString XmlParserHandler::TAG_STATUS = "status";
const QString XmlParserHandler::TAG_STATUS_TEXT = "text";
const QString XmlParserHandler::TAG_USER_NAME = "name";
const QString XmlParserHandler::TAG_USER_SCREENNAME = "screen_name";
const QString XmlParserHandler::TAG_CREATED_AT = "created_at";
const QString XmlParserHandler::TAG_USER = "user";

const QSet<QString> XmlParserHandler::tags = QSet<QString>() << TAG_STATUS << TAG_STATUS_TEXT << TAG_USER_NAME << TAG_USER_SCREENNAME << TAG_CREATED_AT << TAG_USER;

XmlParserHandler::XmlParserHandler(QObject *parent) :
    QObject(parent),
    QXmlDefaultHandler(),
    currentTag(QString()),
    indentationLevel(0)
{
}

bool XmlParserHandler::startDocument()
{
//    qDebug(Q_FUNC_INFO);
//    timelineList.clear();

    return true;
}

bool XmlParserHandler::endDocument()
{
    emit this->parsed(timelineList);
//    foreach (QString status, this->dataList) {
//        qDebug() << ">> " << status;
//    }
//    qDebug() << fooList.count();`

    return true;
}

bool XmlParserHandler::startElement(const QString &namespaceURI, const QString &localName, const QString &qName, const QXmlAttributes &atts)
{
    Q_UNUSED(namespaceURI)
    Q_UNUSED(localName)
    Q_UNUSED(atts)

    ++indentationLevel;
    if (qName == TAG_STATUS)
    {
        statusItem = new TimelineItem();
        //qDebug() << "status:";
    }
    // Proc ta podminka s levelem?
    if (qName == TAG_USER && indentationLevel <= 3)
    {
        parsingUser = true;
    }

    // pokud je tag v nasem seznamu, zajima nas, nastavim important na 1 a currentTag na aktualni tag, jeho obsah se zpracuje v characters metode
    important = tags.contains(qName);
    if (important)
    {
        currentTag = qName;
    }

    return true;
}

bool XmlParserHandler::endElement(const QString &namespaceURI, const QString &localName, const QString &qName)
{
    Q_UNUSED(namespaceURI)
    Q_UNUSED(localName)
    Q_UNUSED(qName)

    --indentationLevel;
    if (qName == TAG_STATUS)
    {
        timelineList << statusItem;
    }
    if (qName == TAG_USER)
    {
        parsingUser = false;
    }

    // to abych nezpracovaval znovu obsah elementu, kdyz dolezu na ukoncovaci znacku
    important = false;

    return true;
}

bool XmlParserHandler::characters(const QString &ch)
{
//    qDebug() << "ch: " << ch;
    // na obsah tagu kouknem jenom pokud nas opravdu zajima
    if (important)
    {
        if (parsingUser)
        {
            parseUser(ch);
        }
        else
        {
            if (currentTag == TAG_STATUS_TEXT)
            {
    //            qDebug() << "Text statusu: " << ch;
                statusItem->setStatusText(ch);
    //            fooItem.footext = ch;
    //            statusItem->setStatusText(ch);
            }
            else if (currentTag == TAG_USER_NAME)
            {
    //            statusItem->setAuthor(ch);
            }
            else if (currentTag == TAG_CREATED_AT)
            {
    //            statusItem->setCreatedAt(this->toDateTime(ch));
            }
        }
    }

    return true;
}

QDateTime XmlParserHandler::toDateTime(const QString &timestamp)
{
    QRegExp rx( "(\\w+) (\\w+) (\\d{2}) (\\d{1,2}):(\\d{2}):(\\d{2}) .+ (\\d{4})" );
    // dve lomitka kvuli escapovani, kompiler je transformuje ci co...
    // \d - digit
    // \w - word character
    // E{n,m} - aspon n, ale maximalne m
    // E{n} - presne n
    // . - jakykoli znak
    // E+ - jeden nebo vice

    // cap(1) - den jmenem
    // cap(2) - mesic
    // cap(3) - den, cislo v mesici
    // cap(4) - hodina
    // cap(5) - minuta
    // cap(6) - sekunda
    // cap(7) - rok


    rx.indexIn( timestamp );
    //qDebug() << QString::number(this->getMonth(rx.cap(2)));
//    return QDateTime( QDate( 1, 2, 3 ),
//                      QTime( 1, 2, 3));
    return QDateTime(QDate(rx.cap(7).toInt(), this->getMonth(rx.cap(2)), rx.cap(3).toInt()),
                     QTime(rx.cap(4).toInt(), rx.cap(5).toInt(), rx.cap(6).toInt()));
}

int XmlParserHandler::getMonth(const QString &month)
{
    // FIXME: nepremava, opravit
//    QDate dt(QDate::fromString("1 "+month_+" 2005", Qt::TextDate));
//    return dt.month();
    if ( month == "Jan" )
        return 1;
    if ( month == "Feb" )
        return 2;
    if ( month == "Mar" )
        return 3;
    if ( month == "Apr" )
        return 4;
    if ( month == "May" )
        return 5;
    if ( month == "Jun" )
        return 6;
    if ( month == "Jul" )
        return 7;
    if ( month == "Aug" )
        return 8;
    if ( month == "Sep" )
        return 9;
    if ( month == "Oct" )
        return 10;
    if ( month == "Nov" )
        return 11;
    if ( month == "Dec" )
        return 12;
    else
        return -1;
}

void XmlParserHandler::parseUser(const QString &ch)
{
    if (currentTag == TAG_USER_NAME)
    {
        statusItem->setAuthorName(ch);
    }
    else if (currentTag == TAG_USER_SCREENNAME)
    {
        statusItem->setAuthorScreenName(ch);
    }
}
