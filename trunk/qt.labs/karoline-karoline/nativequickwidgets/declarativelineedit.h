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
#ifndef DECLARATIVELINEEDIT_H
#define DECLARATIVELINEEDIT_H

#include <QStyleOptionFrameV2>
#include "declarativewidget.h"

class DeclarativeLineEdit : public DeclarativeWidget
{
    Q_OBJECT
	Q_PROPERTY(bool hovered READ hovered WRITE setHovered NOTIFY hoveredChanged)
	Q_PROPERTY(bool editFocus READ hasEditFocus WRITE setEditFocus NOTIFY editFocusChanged)
	Q_PROPERTY(bool readOnly READ readOnly WRITE setReadOnly NOTIFY readOnlyChanged)

public:
	explicit DeclarativeLineEdit(QDeclarativeItem *parent = 0);
	bool hovered() const;
	void setHovered(bool on);
	bool hasEditFocus() const;
	void setEditFocus(bool on);
	bool readOnly() const;
	void setReadOnly(bool on);

	void paint(QPainter *painter, const QStyleOptionGraphicsItem *itemOption, QWidget *widget = 0);

signals:
	void hoveredChanged(bool on);
	void editFocusChanged(bool on);
	void readOnlyChanged(bool on);
};

#endif // DECLARATIVELINEEDIT_H
