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
#include "declarativeprogressbar.h"

DeclarativeProgressBar::DeclarativeProgressBar(QDeclarativeItem *parent)
	: DeclarativeWidget(new QStyleOptionProgressBarV2, parent)
{
	STYLE_OPTION(QStyleOptionProgressBarV2);
	option->minimum = 0;
	option->maximum = 100;
	option->progress = 0;
	option->state = QStyle::State_Enabled;
	option->text = "0%";
	option->textVisible = true;
}


int DeclarativeProgressBar::minimum() const
{
	STYLE_OPTION(const QStyleOptionProgressBarV2);
	return option->minimum;
}


void DeclarativeProgressBar::setMinimum(int minimum)
{
	STYLE_OPTION(QStyleOptionProgressBarV2);
	option->minimum = minimum;
	emit minimumChanged(minimum);
	update();
}


int DeclarativeProgressBar::maximum() const
{
	STYLE_OPTION(const QStyleOptionProgressBarV2);
	return option->maximum;
}


void DeclarativeProgressBar::setMaximum(int maximum)
{
	STYLE_OPTION(QStyleOptionProgressBarV2);
	option->maximum = maximum;
	emit maximumChanged(maximum);
	update();
}


bool DeclarativeProgressBar::textVisible() const
{
	STYLE_OPTION(const QStyleOptionProgressBarV2);
	return option->textVisible;
}


void DeclarativeProgressBar::setTextVisible(bool visible)
{
	STYLE_OPTION(QStyleOptionProgressBarV2);
	option->textVisible = visible;
	emit textVisibleChanged(visible);
	update();
}


int DeclarativeProgressBar::value() const
{
	STYLE_OPTION(const QStyleOptionProgressBarV2);
	return option->progress;
}


void DeclarativeProgressBar::setValue(int value)
{
	STYLE_OPTION(QStyleOptionProgressBarV2);
	option->progress = value;
	if(value >= minimum() && value <= maximum() && maximum() > minimum())	// ensuring maximum>minimum prevents division by 0
		option->text = QString("%1%").arg((100 * (value-minimum())) / (maximum()-minimum()));
	else
		option->text.clear();
	emit valueChanged(value);
	update();
}


void DeclarativeProgressBar::paint(QPainter *painter, const QStyleOptionGraphicsItem *itemOption, QWidget *widget)
{
	STYLE_OPTION(QStyleOptionProgressBarV2);
	QStyleOptionProgressBarV2 opt(*option);
	opt.rect = itemOption->rect;
	qApp->style()->drawControl(QStyle::CE_ProgressBar, &opt, painter, widget);
}
