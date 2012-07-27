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
#include "declarativecursorarea.h"

DeclarativeCursorArea::DeclarativeCursorArea(QDeclarativeItem *parent) :
    QDeclarativeItem(parent)
{
}


QString DeclarativeCursorArea::cursorShape() const
{
	switch(cursor().shape()) {
		case Qt::UpArrowCursor:
			return "up_arrow";
		case Qt::CrossCursor:
			return "cross";
		case Qt::IBeamCursor:
			return "ibeam";
		case Qt::WaitCursor:
			return "wait";
		case Qt::BusyCursor:
			return "busy";
		case Qt::ForbiddenCursor:
			return "forbidden";
		case Qt::PointingHandCursor:
			return "hand";
		case Qt::WhatsThisCursor:
			return "whats_this";
		case Qt::SizeVerCursor:
			return "size_ver";
		case Qt::SizeHorCursor:
			return "size_hor";
		case Qt::SizeBDiagCursor:
			return "size_bdiag";
		case Qt::SizeFDiagCursor:
			return "size_fdiag";
		case Qt::SizeAllCursor:
			return "size_all";
		case Qt::SplitVCursor:
			return "split_v";
		case Qt::SplitHCursor:
			return "split_h";
		case Qt::OpenHandCursor:
			return "openhand";
		case Qt::ClosedHandCursor:
			return "closedhand";
		case Qt::DragMoveCursor:
			return "dnd-move";
		case Qt::DragCopyCursor:
			return "dnd-copy";
		case Qt::DragLinkCursor:
			return "dnd-link";
		default:
			return "arrow";
	}
}


void DeclarativeCursorArea::setCursorShape(const QString& shape)
{
	if(shape == "up_arrow")
		setCursor(Qt::UpArrowCursor);
	else if(shape == "cross")
		setCursor(Qt::CrossCursor);
	else if(shape == "ibeam" || shape == "i-beam")
		setCursor(Qt::IBeamCursor);
	else if(shape == "wait" || shape == "hourglass")
		setCursor(Qt::WaitCursor);
	else if(shape == "busy")
		setCursor(Qt::BusyCursor);
	else if(shape == "forbidden")
		setCursor(Qt::ForbiddenCursor);
	else if(shape == "hand" || shape == "pointing_hand")
		setCursor(Qt::PointingHandCursor);
	else if(shape == "whats_this")
		setCursor(Qt::WhatsThisCursor);
	else if(shape == "size_ver")
		setCursor(Qt::SizeVerCursor);
	else if(shape == "size_hor")
		setCursor(Qt::SizeHorCursor);
	else if(shape == "size_bdiag")
		setCursor(Qt::SizeBDiagCursor);
	else if(shape == "size_fdiag")
		setCursor(Qt::SizeFDiagCursor);
	else if(shape == "size_all")
		setCursor(Qt::SizeAllCursor);
	else if(shape == "split_v")
		setCursor(Qt::SplitVCursor);
	else if(shape == "split_h")
		setCursor(Qt::SplitHCursor);
	else if(shape == "openhand")
		setCursor(Qt::OpenHandCursor);
	else if(shape == "closedhand")
		setCursor(Qt::ClosedHandCursor);
	else if(shape == "dnd-move" || shape == "move")
		setCursor(Qt::DragMoveCursor);
	else if(shape == "dnd-copy" || shape == "copy")
		setCursor(Qt::DragCopyCursor);
	else if(shape == "dnd-link" || shape == "link")
		setCursor(Qt::DragLinkCursor);
	else
		setCursor(Qt::ArrowCursor);
}
