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
#ifndef DECLARATIVESCROLLBAR_H
#define DECLARATIVESCROLLBAR_H

#include <QStyleOptionSlider>
#include "declarativewidget.h"

class DeclarativeScrollBar : public DeclarativeWidget
{
	Q_OBJECT
	Q_PROPERTY(QString orientation READ orientation WRITE setOrientation NOTIFY orientationChanged)
	Q_PROPERTY(int minimum READ minimum WRITE setMinimum NOTIFY minimumChanged)
	Q_PROPERTY(int maximum READ maximum WRITE setMaximum NOTIFY maximumChanged)
	Q_PROPERTY(int pageStep READ pageStep WRITE setPageStep NOTIFY pageStepChanged)
	Q_PROPERTY(int singleStep READ singleStep WRITE setSingleStep NOTIFY singleStepChanged)
	Q_PROPERTY(int value READ value WRITE setValue NOTIFY valueChanged)
	Q_PROPERTY(bool hovered READ hovered WRITE setHovered NOTIFY hoveredChanged)
	Q_PROPERTY(bool pressed READ pressed WRITE setPressed NOTIFY pressedChanged)
	Q_PROPERTY(int grooveMinimum READ grooveMinimum)
	Q_PROPERTY(int grooveMaximum READ grooveMaximum)
	Q_PROPERTY(int sliderMinimum READ sliderMinimum)
	Q_PROPERTY(int sliderMaximum READ sliderMaximum)

public:
	explicit DeclarativeScrollBar(QDeclarativeItem *parent = 0);
	QString orientation() const;
	void setOrientation(const QString& orientation);
	int minimum() const;
	void setMinimum(int min);
	int maximum() const;
	void setMaximum(int max);
	int pageStep() const;
	void setPageStep(int step);
	int singleStep() const;
	void setSingleStep(int step);
	int value() const;
	void setValue(int val);
	bool hovered() const;
	void setHovered(bool on);
	bool pressed() const;
	void setPressed(bool on);
	int grooveMinimum() const;
	int grooveMaximum() const;
	int sliderMinimum() const;
	int sliderMaximum() const;

	Q_INVOKABLE void activateSubControl(const QString& subControl);
	Q_INVOKABLE QString hitTest(int mouseX, int mouseY);

	void paint(QPainter *painter, const QStyleOptionGraphicsItem *itemOption, QWidget *widget = 0);

signals:
	void orientationChanged(const QString& orientation);
	void minimumChanged(int min);
	void maximumChanged(int max);
	void pageStepChanged(int step);
	void singleStepChanged(int step);
	void valueChanged(int val);
	void hoveredChanged(bool on);
	void pressedChanged(bool on);

private:
	QRect grooveRect() const;
	QRect sliderRect() const;

	QRect m_rect;
};

#endif // DECLARATIVESCROLLBAR_H
