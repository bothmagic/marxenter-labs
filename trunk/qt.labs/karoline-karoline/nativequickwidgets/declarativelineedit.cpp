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
#include "declarativelineedit.h"

DeclarativeLineEdit::DeclarativeLineEdit(QDeclarativeItem *parent)
	: DeclarativeWidget(new QStyleOptionFrameV2, parent)
{
	STYLE_OPTION(QStyleOptionFrameV2);
	option->features = QStyleOptionFrameV2::None;
	option->lineWidth = qApp->style()->pixelMetric(QStyle::PM_DefaultFrameWidth, option);
	option->midLineWidth = 0;
	option->state = QStyle::State_Enabled | QStyle::State_Sunken;
}


bool DeclarativeLineEdit::hovered() const
{
	STYLE_OPTION(const QStyleOptionFrameV2);
	return option->state & QStyle::State_MouseOver;
}


void DeclarativeLineEdit::setHovered(bool on)
{
	STYLE_OPTION(QStyleOptionFrameV2);
	if(on)
		option->state |= QStyle::State_MouseOver;
	else
		option->state &= ~QStyle::State_MouseOver;
	emit hoveredChanged(on);
	update();
}


bool DeclarativeLineEdit::hasEditFocus() const
{
	STYLE_OPTION(const QStyleOptionFrameV2);
	return option->state & QStyle::State_HasFocus;
}


void DeclarativeLineEdit::setEditFocus(bool on)
{
	STYLE_OPTION(QStyleOptionFrameV2);
	if(on)
		option->state |= QStyle::State_HasFocus;
	else
		option->state &= ~QStyle::State_HasFocus;
	emit editFocusChanged(on);
	update();
}


bool DeclarativeLineEdit::readOnly() const
{
	STYLE_OPTION(const QStyleOptionFrameV2);
	 return option->state & QStyle::State_ReadOnly;
}


void DeclarativeLineEdit::setReadOnly(bool on)
{
	STYLE_OPTION(QStyleOptionFrameV2);
	 if(on)
		option->state |= QStyle::State_ReadOnly;
	 else
		option->state &= ~QStyle::State_ReadOnly;
	 emit readOnlyChanged(on);
	 update();
}


void DeclarativeLineEdit::paint(QPainter *painter, const QStyleOptionGraphicsItem *itemOption, QWidget *widget)
{
	STYLE_OPTION(QStyleOptionFrameV2);
	QStyleOptionFrameV2 opt(*option);
	opt.rect = itemOption->rect;
	qApp->style()->drawPrimitive(QStyle::PE_PanelLineEdit, &opt, painter, widget);
}
