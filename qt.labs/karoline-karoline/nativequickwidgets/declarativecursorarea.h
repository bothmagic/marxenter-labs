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
#ifndef DECLARATIVECURSORAREA_H
#define DECLARATIVECURSORAREA_H

#include <QDeclarativeItem>

class DeclarativeCursorArea : public QDeclarativeItem
{
    Q_OBJECT
	Q_PROPERTY(QString cursorShape READ cursorShape WRITE setCursorShape NOTIFY cursorShapeChanged)

public:
    explicit DeclarativeCursorArea(QDeclarativeItem *parent = 0);
	QString cursorShape() const;
	Q_INVOKABLE void setCursorShape(const QString& shape);

signals:
	void cursorShapeChanged(const QString& shape);
};

#endif // DECLARATIVECURSORAREA_H
