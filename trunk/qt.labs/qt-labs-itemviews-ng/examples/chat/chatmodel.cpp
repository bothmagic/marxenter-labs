/****************************************************************************
**
** Copyright (C) 2008-2009 Nokia Corporation and/or its subsidiary(-ies).
** Contact: Qt Software Information (qt-info@nokia.com)
**
** This file is part of the Itemviews NG project on Trolltech Labs.
**
** This file may be used under the terms of the GNU General Public
** License version 2.0 or 3.0 as published by the Free Software Foundation
** and appearing in the file LICENSE.GPL included in the packaging of
** this file.  Please review the following information to ensure GNU
** General Public Licensing requirements will be met:
** http://www.fsf.org/licensing/licenses/info/GPLv2.html and
** http://www.gnu.org/copyleft/gpl.html.
**
** If you are unsure which license is appropriate for your use, please
** contact the sales department at qt-sales@nokia.com.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
****************************************************************************/

#include "chatmodel.h"
#include <qevent.h>
#include <qdebug.h>

QString nicks[] = {
    "gammamute",
    "tseng",
    "Viper550",
    "mdke",
    "jsgotangco",
    "dholbach",
    "seb128",
    "ajmitch",
    "pitti"
};

QPair<int,QString> conversation[] = {
qMakePair<int, QString>(0, "hey how do I make a website with ubuntu-desktop?"),
qMakePair<int, QString>(1, "you can try bluefish or nvu"),
qMakePair<int, QString>(0, "what are those?"),
qMakePair<int, QString>(1, "html editor"),
qMakePair<int, QString>(1, "s"),
qMakePair<int, QString>(1, "I use vi"),
qMakePair<int, QString>(0, "oh. how do I do it with php in ubuntu-desktop?"),
qMakePair<int, QString>(1, "that probably isnt your speed."),
qMakePair<int, QString>(1, "I am not sure what you are asking for"),
qMakePair<int, QString>(1, "a php ide?"),
qMakePair<int, QString>(0, "??"),
qMakePair<int, QString>(1, "or a php-able webserver"),
qMakePair<int, QString>(0, "no, just how to make a website with php in ubuntu-desktop"),
qMakePair<int, QString>(1, "sigh."),
qMakePair<int, QString>(1, "do you want to write php code?"),
qMakePair<int, QString>(0, "no, I want to make a website."),
qMakePair<int, QString>(1, "or run the webserver, or both"),
qMakePair<int, QString>(1, "\"I want to make a website\" involves alot of things"),
qMakePair<int, QString>(1, "I think you are better off finding a tutorial on php"),
qMakePair<int, QString>(1, "it really isnt in the scope of ubuntu-desktop at all"),
qMakePair<int, QString>(0, "it doesnt help with web development?"),
qMakePair<int, QString>(1, "I edit php on my ubuntu desktop, I host it on my ubuntu server"),
qMakePair<int, QString>(1, "I would like to help you, but you keep speaking in generalities"),
qMakePair<int, QString>(1, "have you ever built a php site before?"),
qMakePair<int, QString>(0, "oh. you should be in #untuntu-server."),
qMakePair<int, QString>(1, "I should?"),
qMakePair<int, QString>(0, "i dont know much about php"),
qMakePair<int, QString>(1, "I think your first step then should be to study up on PHP"),
qMakePair<int, QString>(0, "what about ubuntu?"),
qMakePair<int, QString>(1, "ubuntu-desktop doesnt have a php IDE"),
qMakePair<int, QString>(1, "what about ubuntu, exactly?"),
qMakePair<int, QString>(0, "i dont know what that is"),
qMakePair<int, QString>(0, "IDE"),
qMakePair<int, QString>(1, "you are extremely unclear on what you want from us"),
qMakePair<int, QString>(1, "Integrated Development Environment"),
qMakePair<int, QString>(0, "I want to make a website."),
qMakePair<int, QString>(1, "to make a website you write code"),
qMakePair<int, QString>(1, "it runs on a webserver"),
qMakePair<int, QString>(1, "there is no magic point of entry"),
qMakePair<int, QString>(0, "I don't understand."),
qMakePair<int, QString>(1, "there are 100 steps in making a website"),
qMakePair<int, QString>(0, "What does any of that have to do with making a website?"),
qMakePair<int, QString>(1, "you ask me \"how can I make a website with ubuntu\""),
qMakePair<int, QString>(1, "oh god"),
qMakePair<int, QString>(2, "Just get nvu"),
qMakePair<int, QString>(0, "I dont know what that is."),
qMakePair<int, QString>(2, "Or, ask your question in #ubuntu, we deal with the actual desktop, not what you do with it!"),
qMakePair<int, QString>(0, "or what i would do with that"),
qMakePair<int, QString>(0, "if you deal with the actual desktop, and not what you do with it....what exactly do you do?"),
qMakePair<int, QString>(0, "do you polish it?"),
qMakePair<int, QString>(3, "gammamute: you need to find a basic tutorial about websites. This channel is for talking about how to make the ubuntu desktop better"),
qMakePair<int, QString>(2, "Yes, making it usable"),
qMakePair<int, QString>(0, "oh. well. I'm finding it very difficult to make a website with it."),
qMakePair<int, QString>(2, "Nice, clear, concise, simple! If you don't like the default layout, ask us, if you like making websites, go to #ubuntu"),
qMakePair<int, QString>(3, "gammamute: that is because making a website is very difficult"),
qMakePair<int, QString>(3, "you need to read a tutorial and maybe do some classes and so on"),
qMakePair<int, QString>(0, "so ubuntu desktop, an OS, doesn't make websites? how long has it been in development?"),
qMakePair<int, QString>(4, "gammamute: do you know HTML"),
qMakePair<int, QString>(0, "jsgotangco: he's a fine fellow."),
qMakePair<int, QString>(2, "Ubuntu Desktop ISN'T THE OS!!!"),
qMakePair<int, QString>(3, "gammamute: no operating system makes websites. There are some programs to do that, one of which is nvu"),
qMakePair<int, QString>(4, "Viper550: chill"),
qMakePair<int, QString>(4, "gammamute: i meant HTML, the markup language"),
qMakePair<int, QString>(0, "why isnt it part of the os, though?"),
qMakePair<int, QString>(0, "to make it easy for people like me?"),
qMakePair<int, QString>(5, "thi is not a support channel"),
qMakePair<int, QString>(3, "gammamute: because making websites is not thought to be a common enough activity. You can easily install a program to do so"),
qMakePair<int, QString>(6, "dholbach is right"),
qMakePair<int, QString>(6, "that's not the chan to speak about website"),
qMakePair<int, QString>(5, "you can search for \"html editor\" in synaptic or apt-cache"),
qMakePair<int, QString>(0, "mdke: if that was true, then why is myspace HUGE?"),
//"* Viper550 saves the log of this (which is what you are now reading)"
qMakePair<int, QString>(0, "it gets the most hits"),
qMakePair<int, QString>(5, "this channel is about developing the ubuntu desktop"),
qMakePair<int, QString>(0, "of any site in america"),
qMakePair<int, QString>(5, "now please give it a rest"),
qMakePair<int, QString>(0, "all i want is to make a website with ubuntu-desktop."),
qMakePair<int, QString>(0, "i love ubuntu-desktop"),
qMakePair<int, QString>(0, "but"),
qMakePair<int, QString>(5, "gammamute: please read what i said"),
qMakePair<int, QString>(6, "gammamute: you can use any text editor to do a website"),
qMakePair<int, QString>(0, "its support channel is very mean."),
qMakePair<int, QString>(2, "ubuntu-desktop is just the desktop"),
qMakePair<int, QString>(0, "what do desktops do?"),
qMakePair<int, QString>(6, "that's a matter or you learning how to make the site"),
qMakePair<int, QString>(6, "it provides softwares for you to use"),
qMakePair<int, QString>(2, "gives you the interface"),
qMakePair<int, QString>(5, "gammamute: you can search for \"html editor\" in synaptic or apt-cache"),
qMakePair<int, QString>(6, "like a text editor"),
qMakePair<int, QString>(6, "it doesn't teach you to type on a keyboard"),
qMakePair<int, QString>(6, "or write a website"),
qMakePair<int, QString>(6, "that's something you have to do by your own"),
qMakePair<int, QString>(0, "oh, I dont want to learn any code."),
qMakePair<int, QString>(6, "so you can't do a website"),
qMakePair<int, QString>(4, "great"),
qMakePair<int, QString>(6, "that's like wanting to write a book without learning to write or type"),
qMakePair<int, QString>(2, "http://www.nvu.com/index.php "),
qMakePair<int, QString>(0, "hey, can ubuntu desktop be used for a binary proxy server with secured access?"),
qMakePair<int, QString>(2, "ubuntu-desktop is just the core desktop, you need apache to do that"),
qMakePair<int, QString>(0, "really? apache does that??"),
qMakePair<int, QString>(2, "Yes."),
qMakePair<int, QString>(0, "cool. i didn't know that. I always thought it was restricted to port 80."),
qMakePair<int, QString>(6, "gammamute: desktop = apps you use on a desktop, that's not the chan to speak about proxy, server, website"),
qMakePair<int, QString>(0, "I dont understand."),
qMakePair<int, QString>(5, "seb128: did you start on any of gnome-session, gdm, control-center?"),
qMakePair<int, QString>(0, "why dont you have a wysiwyg visual editor built in to the desktop for developers?"),
qMakePair<int, QString>(6, "dholbach: yeah, all of them, go and triage bugs now "),
//"* seb128 runs"
qMakePair<int, QString>(5, "gammamute: please try #ubuntu or #ubuntu+1"),
qMakePair<int, QString>(6, "dholbach: no, joke aside you can do all of them "),
qMakePair<int, QString>(5, "gammamute: this is a channel about development of ubuntu desktop"),
//"* Viper550 thinks this would make a good Saturday Night Live skit"
qMakePair<int, QString>(0, "but I want to talk about the desktop"),
qMakePair<int, QString>(7, "hi pitti"),
qMakePair<int, QString>(6, "gammamute: do you have any good one to suggest?"),
qMakePair<int, QString>(5, "gammamute: then talk about DEVELOPMENT of the ubuntu desktop"),
qMakePair<int, QString>(0, "yeah i have a good one to suggest"),
qMakePair<int, QString>(0, "why dont you have a wysiwyg visual editor built in to the desktop for developers?"),
qMakePair<int, QString>(6, "gammamute: why one?"),
qMakePair<int, QString>(6, "which"),
qMakePair<int, QString>(6, "gammamute: which one?"),
qMakePair<int, QString>(1, "you mean nvu?"),
qMakePair<int, QString>(2, "He wants something like nvu included"),
qMakePair<int, QString>(0, "no"),
qMakePair<int, QString>(0, "yea"),
qMakePair<int, QString>(1, "we cant include everything."),
qMakePair<int, QString>(0, "VISUAL editor, not code"),
qMakePair<int, QString>(8, "hey"),
qMakePair<int, QString>(0, "PHP visual editor"),
qMakePair<int, QString>(5, "hey pitti"),
qMakePair<int, QString>(1, "you cant have a php visual editor"),
qMakePair<int, QString>(6, "gammamute: apt-get install nvu and you are done"),
qMakePair<int, QString>(6, "gammamute: we don't ship devel tools on the default desktop because there is not enough space on a CD for that"),
qMakePair<int, QString>(6, "wb pitti"),
qMakePair<int, QString>(2, "Go to #ubuntu, you might get more help there"),
qMakePair<int, QString>(0, "well, you dont need EVERYTHINg included to be hot. a kick ***, simple FTP client, a visual php editor (like dreamweaver is for html) and a google bar that searches your hd all right there on the desktop."),
qMakePair<int, QString>(1, "you could write an open source visual php editor"),
qMakePair<int, QString>(0, "see?"),
qMakePair<int, QString>(1, "and make you own ubuntu install cds with all that stuff"),
qMakePair<int, QString>(1, "ubuntu is a general purpose os"),
qMakePair<int, QString>(1, "so is windows or mac.. it doesnt come with dreamweaver"),
qMakePair<int, QString>(0, "yeah, I can see that's helped its publicity so much, too."),
qMakePair<int, QString>(0, "tseng: go A step above"),
qMakePair<int, QString>(0, "Aspire."),
qMakePair<int, QString>(6, "gammamute: the CD is already oversized without those"),
qMakePair<int, QString>(2, "Dreamweaver is COMMERCIAL software, you can't just include anything in an operating system"),
qMakePair<int, QString>(0, "yeah, because its loaded with crap no one ever uses."),
qMakePair<int, QString>(5, "Ok"),
qMakePair<int, QString>(6, "like a music player?"),
qMakePair<int, QString>(6, "or a web browser?"),
qMakePair<int, QString>(4, "dholbach: go for it"),
qMakePair<int, QString>(6, "or a mail agent?"),
qMakePair<int, QString>(5, "That's enough. There's a process for getting things included on the CD. It's very simple."),
qMakePair<int, QString>(0, "viper: I meant an 'ubuntu' ve"),
qMakePair<int, QString>(5, "Take the discussion to the Mailing list, then look on the wiki for Inclusion."),
qMakePair<int, QString>(0, "nah, I dont really care that much. you already have the ideas.")
}; //153

ChatModel::ChatModel(QObject *parent)
  : QtListModelInterface(parent), timerId(0)
{
  timerId = startTimer(2000);
  //for (int i = 0; i < 153; ++i)
  //    appendMessage(conversation[i].second, conversation[i].first);
}

ChatModel::~ChatModel()
{
}

int ChatModel::count() const
{
    return messages.count();
}

QHash<QByteArray,QVariant> ChatModel::data(int index, const QList<QByteArray> &roles) const
{
    QHash<QByteArray,QVariant> hash;
    if (index >= 0 && index < messages.count()) {
        for (int i = 0; i < roles.count(); ++i) {
            if (roles.at(i) == "DisplayRole")
                hash.insert("DisplayRole", messages.at(index));
            if (roles.at(i) == "BackgroundRole")
                hash.insert("BackgroundRole", userIds.at(index));
            if (roles.at(i) == "ToolTipRole")
                hash.insert("ToolTipRole", nicks[userIds.at(index)]);
        }
    }
    return hash;
}

void ChatModel::appendMessage(const QString &message, int userId)
{
    if (!userIds.isEmpty() && userIds.last() == userId) {
        messages[messages.count() - 1] += ("\n" + message);
        emit itemsChanged(messages.count() - 1, 1, QList<QByteArray>() << "DisplayRole");
    } else {
        messages.append(message);
        userIds.append(userId);
        emit itemsInserted(messages.count() - 1, 1);
    }
}

void ChatModel::timerEvent(QTimerEvent *event)
{
    static int i = 0;
    if (event->timerId() == timerId) {
        int index = i++;
        if (index < 153) {
            appendMessage(conversation[index].second, conversation[index].first);
        } else {
            killTimer(timerId);
            timerId = 0;
        }
    }
}
