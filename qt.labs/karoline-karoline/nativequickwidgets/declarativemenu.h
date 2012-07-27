/***************************************************************************
 *   Copyright (C) 2011 by Víctor Fernández Martínez                       *
 *   deejayworld@gmail.com                                                 *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 3 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
#ifndef DECLARATIVEMENU_H
#define DECLARATIVEMENU_H

#include <QDeclarativeItem>
#include <QDeclarativeListProperty>
#include <QMenu>
#include <QObject>

class DeclarativeMenuAction : public QObject
{
	Q_OBJECT
	Q_PROPERTY(bool enabled READ enabled WRITE setEnabled NOTIFY enabledChanged)
	Q_PROPERTY(bool separator READ isSeparator WRITE setSeparator NOTIFY separatorChanged)
	Q_PROPERTY(QString text READ text WRITE setText NOTIFY textChanged)
	Q_PROPERTY(QString icon READ icon WRITE setIcon NOTIFY iconChanged)

public:
	DeclarativeMenuAction(QObject *parent = 0);
	bool enabled() const;
	void setEnabled(bool on);
	bool isSeparator() const;
	void setSeparator(bool on);
	QString text() const;
	void setText(const QString& text);
	QString icon() const;
	void setIcon(const QString& icon);

signals:
	void enabledChanged(bool on);
	void separatorChanged(bool on);
	void textChanged(const QString& text);
	void iconChanged(const QString& icon);
	void triggered();

private:
	bool m_enabled;
	bool m_separator;
	QString m_text;
	QString m_iconPath;
};

class DeclarativeMenu : public DeclarativeMenuAction
{
    Q_OBJECT
	Q_PROPERTY(QDeclarativeListProperty<DeclarativeMenuAction> actions READ actions)
	Q_PROPERTY(bool visible READ isVisible NOTIFY visibilityChanged)
	Q_CLASSINFO("DefaultProperty", "actions")

public:
	explicit DeclarativeMenu(QObject *parent = 0);
	~DeclarativeMenu();
	QDeclarativeListProperty<DeclarativeMenuAction> actions();
	Q_INVOKABLE void exec();
	Q_INVOKABLE void execAt(int x, int y, QDeclarativeItem *item);
	bool isVisible() const;

signals:
	void visibilityChanged(bool on);

private:
	void createSubMenu(QMenu *parentMenu, const QList<DeclarativeMenuAction*>& actions);
	static QString translateIconPathResource(const QString& iconPath);

	QList<DeclarativeMenuAction*> m_actions;
	QMenu *m_menu;
};

#endif // DECLARATIVEMENU_H
