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
#include "declarativewidget.h"

DeclarativeWidget::DeclarativeWidget(QStyleOption *option, QDeclarativeItem *parent) :
	QDeclarativeItem(parent),
	m_styleOption(option)
{
	setFlag(QGraphicsItem::ItemHasNoContents, false);

	option->direction = qApp->layoutDirection();
	option->fontMetrics = qApp->fontMetrics();
	option->palette = qApp->palette();
}


DeclarativeWidget::~DeclarativeWidget()
{
	if(m_styleOption)
		delete m_styleOption;
}


bool DeclarativeWidget::widgetEnabled() const
{
	return m_styleOption->state & QStyle::State_Enabled;
}


void DeclarativeWidget::setWidgetEnabled(bool enabled)
{
	if(enabled)
		m_styleOption->state |= QStyle::State_Enabled;
	else
		m_styleOption->state &= ~QStyle::State_Enabled;
	emit widgetEnabledChanged(enabled);
	update();
}
