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
#include "declarativescrollbar.h"

QString subControlToString(QStyle::SubControl subcontrol)
{
	switch(subcontrol) {
		case QStyle::SC_ScrollBarAddLine:
			return "AddLine";
		case QStyle::SC_ScrollBarSubLine:
			return "SubLine";
		case QStyle::SC_ScrollBarAddPage:
			return "AddPage";
		case QStyle::SC_ScrollBarSubPage:
			return "SubPage";
		case QStyle::SC_ScrollBarFirst:
			return "First";
		case QStyle::SC_ScrollBarLast:
			return "Last";
		case QStyle::SC_ScrollBarSlider:
			return "Slider";
		case QStyle::SC_ScrollBarGroove:
			return "Groove";
		default:
			return QString();
	}
}


QStyle::SubControl stringToSubControl(const QString& str)
{
	if(str.compare("AddLine", Qt::CaseInsensitive) == 0)
		return QStyle::SC_ScrollBarAddLine;
	else if(str.compare("SubLine", Qt::CaseInsensitive) == 0)
		return QStyle::SC_ScrollBarSubLine;
	else if(str.compare("AddPage", Qt::CaseInsensitive) == 0)
		return QStyle::SC_ScrollBarAddPage;
	else if(str.compare("SubPage", Qt::CaseInsensitive) == 0)
		return QStyle::SC_ScrollBarSubPage;
	else if(str.compare("First", Qt::CaseInsensitive) == 0)
		return QStyle::SC_ScrollBarFirst;
	else if(str.compare("Last", Qt::CaseInsensitive) == 0)
		return QStyle::SC_ScrollBarLast;
	else if(str.compare("Slider", Qt::CaseInsensitive) == 0)
		return QStyle::SC_ScrollBarSlider;
	else if(str.compare("Groove", Qt::CaseInsensitive) == 0)
		return QStyle::SC_ScrollBarGroove;
	else
		return QStyle::SC_None;
}




DeclarativeScrollBar::DeclarativeScrollBar(QDeclarativeItem *parent)
	: DeclarativeWidget(new QStyleOptionSlider, parent)
{
	STYLE_OPTION(QStyleOptionSlider);
	option->minimum = 0;
	option->maximum = 0;
	option->orientation = Qt::Horizontal;
	option->pageStep = 10;		// Default value found in QAbstractSliderPrivate::QAbstractSliderPrivate()
	option->singleStep = 1;		// Default value found in QAbstractSliderPrivate::QAbstractSliderPrivate()
	option->sliderValue = 0;
	option->activeSubControls = QStyle::SC_None;
	option->subControls = QStyle::SC_All;
	option->state = QStyle::State_Enabled;
}


QString DeclarativeScrollBar::orientation() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	if(option->orientation == Qt::Vertical)
		return "vertical";
	else
		return "horizontal";
}


void DeclarativeScrollBar::setOrientation(const QString& orientation)
{
	STYLE_OPTION(QStyleOptionSlider);
	if(orientation.compare("vertical", Qt::CaseInsensitive) == 0) {
		option->orientation = Qt::Vertical;
		option->state &= ~QStyle::State_Horizontal;
	} else if(orientation.compare("horizontal", Qt::CaseInsensitive) == 0) {
		option->orientation = Qt::Horizontal;
		option->state |= QStyle::State_Horizontal;
	} else
		return;
	emit orientationChanged(orientation);
	update();
}


int DeclarativeScrollBar::minimum() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	return option->minimum;
}


void DeclarativeScrollBar::setMinimum(int min)
{
	STYLE_OPTION(QStyleOptionSlider);
	option->minimum = min;
	emit minimumChanged(min);
	update();
}


int DeclarativeScrollBar::maximum() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	return option->maximum;
}

void DeclarativeScrollBar::setMaximum(int max)
{
	STYLE_OPTION(QStyleOptionSlider);
	option->maximum = max;
	emit maximumChanged(max);
	update();
}


int DeclarativeScrollBar::pageStep() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	return option->pageStep;
}


void DeclarativeScrollBar::setPageStep(int step)
{
	STYLE_OPTION(QStyleOptionSlider);
	option->pageStep = step;
	emit pageStepChanged(step);
	update();
}


int DeclarativeScrollBar::singleStep() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	return option->singleStep;
}


void DeclarativeScrollBar::setSingleStep(int step)
{
	STYLE_OPTION(QStyleOptionSlider);
	option->singleStep = step;
	emit singleStepChanged(step);
	update();
}


int DeclarativeScrollBar::value() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	return option->sliderValue;
}


void DeclarativeScrollBar::setValue(int val)
{
	STYLE_OPTION(QStyleOptionSlider);
	if(val < option->minimum)
		val = option->minimum;
	if(val > option->maximum)
		val = option->maximum;
	option->sliderPosition = val;
	option->sliderValue = val;
	emit valueChanged(val);
	update();
}


bool DeclarativeScrollBar::hovered() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	return option->state & QStyle::State_MouseOver;
}


void DeclarativeScrollBar::setHovered(bool on)
{
	STYLE_OPTION(QStyleOptionSlider);
	if(on)
		option->state |= QStyle::State_MouseOver;
	else
		option->state &= ~QStyle::State_MouseOver;
	emit hoveredChanged(on);
	update();
}


bool DeclarativeScrollBar::pressed() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	return option->state & QStyle::State_Sunken;
}


void DeclarativeScrollBar::setPressed(bool on)
{
	STYLE_OPTION(QStyleOptionSlider);
	if(on)
		option->state |= QStyle::State_Sunken;
	else
		option->state &= ~QStyle::State_Sunken;
	emit pressedChanged(on);
	update();
}


int DeclarativeScrollBar::grooveMinimum() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	if(option->orientation == Qt::Vertical)
		return grooveRect().top();
	else
		return grooveRect().left();
}


int DeclarativeScrollBar::grooveMaximum() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	if(option->orientation == Qt::Vertical)
		return grooveRect().bottom();
	else
		return grooveRect().right();
}


int DeclarativeScrollBar::sliderMinimum() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	if(option->orientation == Qt::Vertical)
		return sliderRect().top();
	else
		return sliderRect().left();
}


int DeclarativeScrollBar::sliderMaximum() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	if(option->orientation == Qt::Vertical)
		return sliderRect().bottom();
	else
		return sliderRect().right();
}


void DeclarativeScrollBar::activateSubControl(const QString& subControl)
{
	STYLE_OPTION(QStyleOptionSlider);
	option->activeSubControls = stringToSubControl(subControl);
	update();
}


QString DeclarativeScrollBar::hitTest(int mouseX, int mouseY)
{
	STYLE_OPTION(const QStyleOptionSlider);
	QStyleOptionSlider opt(*option);
	opt.rect = m_rect;
	return subControlToString( qApp->style()->hitTestComplexControl(QStyle::CC_ScrollBar, &opt, QPoint(mouseX, mouseY)) );
}


void DeclarativeScrollBar::paint(QPainter *painter, const QStyleOptionGraphicsItem *itemOption, QWidget *widget)
{
	STYLE_OPTION(const QStyleOptionSlider);
	QStyleOptionSlider opt(*option);
	opt.rect = itemOption->rect;
	m_rect = itemOption->rect;
	qApp->style()->drawComplexControl(QStyle::CC_ScrollBar, &opt, painter, widget);
}


QRect DeclarativeScrollBar::grooveRect() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	QStyleOptionSlider opt(*option);
	opt.rect = m_rect;
	return qApp->style()->subControlRect(QStyle::CC_ScrollBar, &opt, QStyle::SC_ScrollBarGroove);
}


QRect DeclarativeScrollBar::sliderRect() const
{
	STYLE_OPTION(const QStyleOptionSlider);
	QStyleOptionSlider opt(*option);
	opt.rect = m_rect;
	return qApp->style()->subControlRect(QStyle::CC_ScrollBar, &opt, QStyle::SC_ScrollBarSlider);
}
