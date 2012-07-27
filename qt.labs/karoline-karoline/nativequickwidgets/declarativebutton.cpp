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
#include "declarativebutton.h"

DeclarativeButton::DeclarativeButton(QDeclarativeItem *parent)
	: DeclarativeWidget(new QStyleOptionButton, parent)
{
	STYLE_OPTION(QStyleOptionButton);
	option->features = QStyleOptionButton::None;
	option->state = QStyle::State_Enabled | QStyle::State_Raised;
}


bool DeclarativeButton::hasMenu() const
{
	STYLE_OPTION(const QStyleOptionButton);
	return option->features & QStyleOptionButton::HasMenu;
}


void DeclarativeButton::setMenu(bool on)
{
	STYLE_OPTION(QStyleOptionButton);
	if(on)
		option->features |= QStyleOptionButton::HasMenu;
	else
		option->features &= ~QStyleOptionButton::HasMenu;
	emit menuChanged(on);
	update();
}


bool DeclarativeButton::hovered() const
{
	STYLE_OPTION(const QStyleOptionButton);
	return option->state & QStyle::State_MouseOver;
}


void DeclarativeButton::setHovered(bool on)
{
	STYLE_OPTION(QStyleOptionButton);
	if(on)
		option->state |= QStyle::State_MouseOver;
	else
		option->state &= ~QStyle::State_MouseOver;
	emit hoveredChanged(on);
	update();
}


bool DeclarativeButton::pressed() const
{
	STYLE_OPTION(const QStyleOptionButton);
	return option->state & QStyle::State_Sunken;
}


void DeclarativeButton::setPressed(bool on)
{
	STYLE_OPTION(QStyleOptionButton);
	if(on)
		option->state |= QStyle::State_Sunken;
	else {
		option->state |= QStyle::State_Raised;
		option->state &= ~QStyle::State_Sunken;
	}
	emit pressedChanged(on);
	update();
}


void DeclarativeButton::paint(QPainter *painter, const QStyleOptionGraphicsItem *itemOption, QWidget *widget)
{
	STYLE_OPTION(QStyleOptionButton);
	QStyleOptionButton opt(*option);
	opt.rect = itemOption->rect;
	qApp->style()->drawControl(QStyle::CE_PushButton, &opt, painter, widget);
}
