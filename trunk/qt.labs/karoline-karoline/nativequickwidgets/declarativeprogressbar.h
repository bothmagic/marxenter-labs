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
#ifndef DECLARATIVEPROGRESSBAR_H
#define DECLARATIVEPROGRESSBAR_H

#include <QStyleOptionProgressBarV2>
#include "declarativewidget.h"

class DeclarativeProgressBar : public DeclarativeWidget
{
    Q_OBJECT
	Q_PROPERTY(int minimum READ minimum WRITE setMinimum NOTIFY minimumChanged)
	Q_PROPERTY(int maximum READ maximum WRITE setMaximum NOTIFY maximumChanged)
	Q_PROPERTY(bool textVisible READ textVisible WRITE setTextVisible NOTIFY textVisibleChanged)
	Q_PROPERTY(int value READ value WRITE setValue NOTIFY valueChanged)

public:
	explicit DeclarativeProgressBar(QDeclarativeItem *parent = 0);
	int minimum() const;
	void setMinimum(int minimum);
	int maximum() const;
	void setMaximum(int maximum);
	bool textVisible() const;
	void setTextVisible(bool visible);
	int value() const;
	void setValue(int value);

	void paint(QPainter *painter, const QStyleOptionGraphicsItem *itemOption, QWidget *widget = 0);

signals:
	void minimumChanged(int minimum);
	void maximumChanged(int maximum);
	void textVisibleChanged(bool visible);
	void valueChanged(int value);
};

#endif // DECLARATIVEPROGRESSBAR_H
