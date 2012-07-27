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
#include <QtGui>
#include "declarativemenu.h"

DeclarativeMenuAction::DeclarativeMenuAction(QObject *parent)
	: QObject(parent),
	  m_enabled(true),
	  m_separator(false)
{
}


bool DeclarativeMenuAction::enabled() const
{
	return m_enabled;
}


void DeclarativeMenuAction::setEnabled(bool on)
{
	m_enabled = on;
	emit enabledChanged(on);
}


bool DeclarativeMenuAction::isSeparator() const
{
	return m_separator;
}


void DeclarativeMenuAction::setSeparator(bool on)
{
	m_separator = on;
	emit separatorChanged(on);
}


QString DeclarativeMenuAction::text() const
{
	return m_text;
}


void DeclarativeMenuAction::setText(const QString& text)
{
	m_text = text;
	emit textChanged(text);
}


QString DeclarativeMenuAction::icon() const
{
	return m_iconPath;
}


void DeclarativeMenuAction::setIcon(const QString& icon)
{
	m_iconPath = icon;
	emit iconChanged(icon);
}




DeclarativeMenu::DeclarativeMenu(QObject *parent)
	: DeclarativeMenuAction(parent),
	  m_menu(0)
{
}


DeclarativeMenu::~DeclarativeMenu()
{
	if(m_menu)
		m_menu->deleteLater();
}


QDeclarativeListProperty<DeclarativeMenuAction> DeclarativeMenu::actions()
{
	return QDeclarativeListProperty<DeclarativeMenuAction>(this, m_actions);
}


/*!
  Displays the menu at the position where the mouse cursor is.
*/
void DeclarativeMenu::exec()
{
	if(m_menu)
		return;
	m_menu = new QMenu;
	createSubMenu(m_menu, m_actions);
	emit visibilityChanged(true);
	m_menu->exec(QCursor::pos());
	emit visibilityChanged(false);
	m_menu->deleteLater();
	m_menu = 0;
}


/*!
  Displays the menu at the position \a x and \a y relative to \a item.
*/
void DeclarativeMenu::execAt(int x, int y, QDeclarativeItem *item)
{
	if(m_menu)
		return;
	m_menu = new QMenu;
	createSubMenu(m_menu, m_actions);

	QPoint globalPoint(x, y);
	if(item) {
		QPointF scenePoint = item->mapToScene(x, y);
		if(!item->scene()->views().isEmpty()) {
			QGraphicsView *view = item->scene()->views().at(0);
			QPoint viewPortPoint = view->mapFromScene(scenePoint);
			globalPoint = view->mapToGlobal(viewPortPoint);
		}
	}

	emit visibilityChanged(true);
	m_menu->exec(globalPoint);
	emit visibilityChanged(false);
	m_menu->deleteLater();
	m_menu = 0;
}


bool DeclarativeMenu::isVisible() const
{
	if(!m_menu)
		return false;
	return m_menu->isVisible();
}


void DeclarativeMenu::createSubMenu(QMenu *parentMenu, const QList<DeclarativeMenuAction*>& contextActions)
{
	foreach(DeclarativeMenuAction *contextAction, contextActions) {
		DeclarativeMenu *contextMenu = qobject_cast<DeclarativeMenu*>(contextAction);
		if(contextMenu) {
			QMenu *menu = parentMenu->addMenu(QIcon(translateIconPathResource(contextAction->icon())), contextMenu->text());
			createSubMenu(menu, contextMenu->m_actions);

		} else {
			if(contextAction->isSeparator())
				parentMenu->addSeparator();
			else {
				QAction *action = new QAction(parentMenu);
				action->setEnabled(contextAction->enabled());
				action->setSeparator(contextAction->isSeparator());
				action->setIcon(QIcon(translateIconPathResource(contextAction->icon())));
				action->setText(contextAction->text());
				connect(action, SIGNAL(triggered()),
						contextAction, SIGNAL(triggered()));
				parentMenu->addAction(action);
			}
		}
	}
}


QString DeclarativeMenu::translateIconPathResource(const QString& iconPath)
{
	if(iconPath.startsWith("qrc:/"))
		return iconPath.mid(3);
	else
		return iconPath;
}
