/****************************************************************************
**
** Copyright (C) 2008-2009 Nokia Corporation and/or its subsidiary(-ies).
** Contact: Qt Software Information (qt-info@nokia.com)
**
** This file is part of the Itemviews NG project on Trolltech Labs.
**
** This file may be used under the terms of the GNU General Public
** License version 2.0 or 3.0 as published by the Free Software Foundation
** and appearing in the file LICENSE.GPL included in the packaging of
** this file.  Please review the following information to ensure GNU
** General Public Licensing requirements will be met:
** http://www.fsf.org/licensing/licenses/info/GPLv2.html and
** http://www.gnu.org/copyleft/gpl.html.
**
** If you are unsure which license is appropriate for your use, please
** contact the sales department at qt-sales@nokia.com.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
****************************************************************************/

#include "borderpixmap.h"

static inline void qVerticalRepeat(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source,
                                   void (*drawPixmap)(QPainter*, const QRectF&, const QPixmap&, const QRectF&))
{
    const qreal x = target.x();
    const qreal width = target.width();
    const qreal height = source.height();
    const qreal bottom = target.bottom() - height;
    qreal y = target.y();
    for (; y < bottom; y += height)
        (*drawPixmap)(painter, QRectF(x, y, width, height), pixmap, source);
    const QRectF remaining(source.x(), source.y(), source.width(), target.bottom() - y);
    (*drawPixmap)(painter, QRectF(x, y, width, remaining.height()), pixmap, remaining);
}

static inline void qHorizontalRepeat(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source,
                                     void (*drawPixmap)(QPainter*, const QRectF&, const QPixmap&, const QRectF&))
{
    const qreal y = target.y();
    const qreal width = source.width();
    const qreal height = target.height();
    const qreal right = target.right() - width;
    qreal x = target.x();
    for (; x < right; x += width)
        (*drawPixmap)(painter, QRectF(x, y, width, height), pixmap, source);
    const QRectF remaining(source.x(), source.y(), target.right() - x, source.height());
    (*drawPixmap)(painter, QRectF(x, y, remaining.width(), height), pixmap, remaining);
}

static inline void qVerticalRound(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source,
                                  void (*drawPixmap)(QPainter*, const QRectF&, const QPixmap&, const QRectF&))
{
    const qreal x = target.x();
    const qreal width = target.width();
    const qreal verticalFactor = target.height() / source.height();
    const qreal verticalIncrement = target.height() / static_cast<int>(verticalFactor + 0.5);
    const qreal bottom = target.bottom() - 0.5; // ###
    for (qreal y = target.y(); y < bottom; y += verticalIncrement)
         (*drawPixmap)(painter, QRectF(x, y, width, verticalIncrement), pixmap, source);
}

static inline void qHorizontalRound(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source,
                                    void (*drawPixmap)(QPainter*, const QRectF&, const QPixmap&, const QRectF&))
{
    const qreal y = target.y();
    const qreal height = target.height();
    const qreal horizontalFactor = target.width() / source.width();
    const qreal horizontalIncrement = target.width() / static_cast<int>(horizontalFactor + 0.5);
    const qreal right = target.right() - 0.5; // ###
    for (qreal x = target.x(); x < right; x += horizontalIncrement)
        (*drawPixmap)(painter, QRectF(x, y, horizontalIncrement, height), pixmap, source);
}

static inline void qDrawPixmap(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source)
{
    painter->drawPixmap(target, pixmap, source);
    //painter->drawRect(target); // for debugging
}

static inline void qDrawVerticallyRepeatedPixmap(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source)
{
    qVerticalRepeat(painter, target, pixmap, source, qDrawPixmap);
}

static inline void qDrawHorizontallyRepeatedPixmap(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source)
{
    qHorizontalRepeat(painter, target, pixmap, source, qDrawPixmap);
}

static inline void qDrawVerticallyRoundedPixmap(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source)
{
    qVerticalRound(painter, target, pixmap, source, qDrawPixmap);
}

static inline void qDrawHorizontallyRoundedPixmap(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source)
{
    qHorizontalRound(painter, target, pixmap, source, qDrawPixmap);
}

/*!
  Draws the indicated \a source rectangle from the given \a pixmap into the given \a target rectangle,
  using the given \a painter.
  The pixmap will be splitt into nine segments and drawn according to the given \a borders structure.

  This function is used to draw a scaled pixmap, similar to CSS3 border-images.
 */
void qtDrawBorderPixmap(QPainter *painter, const QRectF &target, const QPixmap &pixmap, const QRectF &source, const QtPixmapBorders &borders)
{
    // source center
    const qreal sourceTop = source.top();
    const qreal sourceLeft = source.left();
    const qreal sourceCenterTop = sourceTop + borders.sourceTopBorder;
    const qreal sourceCenterLeft = sourceLeft + borders.sourceLeftBorder;
    const qreal sourceCenterRight = source.right() - borders.sourceRightBorder;
    const qreal sourceCenterBottom = source.bottom() - borders.sourceBottomBorder;
    const qreal sourceCenterWidth = sourceCenterRight - borders.sourceLeftBorder;
    const qreal sourceCenterHeight = sourceCenterBottom - borders.sourceTopBorder;
    // target borders
    const qreal targetTopBorder = (borders.targetTopBorder == -1 ? borders.sourceTopBorder : borders.targetTopBorder);
    const qreal targetLeftBorder = (borders.targetLeftBorder == -1 ? borders.sourceLeftBorder : borders.targetLeftBorder);
    const qreal targetBottomBorder = (borders.targetBottomBorder == -1 ? borders.sourceBottomBorder : borders.targetBottomBorder);
    const qreal targetRightBorder = (borders.targetRightBorder == -1 ? borders.sourceRightBorder : borders.targetRightBorder);
    // target center
    const qreal targetTop = target.top();
    const qreal targetLeft = target.left();
    const qreal targetCenterTop = targetTop + targetTopBorder;
    const qreal targetCenterLeft = targetLeft + targetLeftBorder;
    const qreal targetCenterRight = target.right() - targetRightBorder;
    const qreal targetCenterBottom = target.bottom() - targetBottomBorder;
    const qreal targetCenterWidth = targetCenterRight - targetCenterLeft;
    const qreal targetCenterHeight = targetCenterBottom - targetCenterTop;

    // corners
    if (targetTopBorder > 0 && targetLeftBorder > 0 && borders.sourceTopBorder > 0 && borders.sourceLeftBorder > 0) { // top left
        const QRectF targetTopLeftRect(targetLeft, targetTop, targetLeftBorder, targetTopBorder);
        const QRectF sourceTopLeftRect(sourceLeft, sourceTop, borders.sourceLeftBorder, borders.sourceTopBorder);
        qDrawPixmap(painter, targetTopLeftRect, pixmap, sourceTopLeftRect);
    }
    if (targetTopBorder > 0 && targetRightBorder > 0 && borders.sourceTopBorder > 0 && borders.sourceRightBorder > 0) { // top right
        const QRectF targetTopRightRect(targetCenterRight, targetTop, targetRightBorder, targetTopBorder);
        const QRectF sourceTopRightRect(sourceCenterRight, 0, borders.sourceRightBorder, borders.sourceTopBorder);
        qDrawPixmap(painter, targetTopRightRect, pixmap, sourceTopRightRect);
    }
    if (targetBottomBorder > 0 && targetLeftBorder > 0 && borders.sourceBottomBorder > 0 && borders.sourceLeftBorder > 0) { // bottom left
        const QRectF targetBottomLeftRect(targetLeft, targetCenterBottom, targetLeftBorder, targetBottomBorder);
        const QRectF sourceBottomLeftRect(sourceLeft, sourceCenterBottom, borders.sourceLeftBorder, borders.sourceBottomBorder);
        qDrawPixmap(painter, targetBottomLeftRect, pixmap, sourceBottomLeftRect);
    }
    if (targetBottomBorder > 0 && targetRightBorder > 0 && borders.sourceBottomBorder > 0 && borders.sourceRightBorder > 0) { // bottom right
        const QRectF targetBottomRightRect(targetCenterRight, targetCenterBottom, targetRightBorder, targetBottomBorder);
        const QRectF sourceBottomRightRect(sourceCenterRight, sourceCenterBottom, borders.sourceRightBorder, borders.sourceBottomBorder);
        qDrawPixmap(painter, targetBottomRightRect, pixmap, sourceBottomRightRect);
    }

    // horizontal edges
    switch (borders.horizontalRule) {
        case QtPixmapBorders::Stretch:
        if (targetTopBorder > 0 && borders.sourceTopBorder > 0) { // top
            const QRectF targetTopRect(targetCenterLeft, targetTop, targetCenterWidth, targetTopBorder);
            const QRectF sourceTopRect(sourceCenterLeft, sourceTop, sourceCenterWidth, borders.sourceTopBorder);
            qDrawPixmap(painter, targetTopRect, pixmap, sourceTopRect);
        }
        if (targetBottomBorder > 0 && borders.sourceBottomBorder > 0) { // bottom
            const QRectF targetBottomRect(targetCenterLeft, targetCenterBottom, targetCenterWidth, targetBottomBorder);
            const QRectF sourceBottomRect(sourceCenterLeft, sourceCenterBottom, sourceCenterWidth, borders.sourceBottomBorder);
            qDrawPixmap(painter, targetBottomRect, pixmap, sourceBottomRect);
        }
        break;
        case QtPixmapBorders::Repeat:
        if (targetTopBorder > 0 && borders.sourceTopBorder > 0) { // top
            const QRectF targetTopRect(targetCenterLeft, targetTop, targetCenterWidth, targetTopBorder);
            const QRectF sourceTopRect(sourceCenterLeft, sourceTop, sourceCenterWidth, borders.sourceTopBorder);
            qDrawHorizontallyRepeatedPixmap(painter, targetTopRect, pixmap, sourceTopRect);
        }
        if (targetBottomBorder > 0 && borders.sourceBottomBorder > 0) { // bottom
            const QRectF targetBottomRect(targetCenterLeft, targetCenterBottom, targetCenterWidth, targetBottomBorder);
            const QRectF sourceBottomRect(sourceCenterLeft, sourceCenterBottom, sourceCenterWidth, borders.sourceBottomBorder);
            qDrawHorizontallyRepeatedPixmap(painter, targetBottomRect, pixmap, sourceBottomRect);
        }
        break;
        case QtPixmapBorders::Round:
        if (targetTopBorder > 0 && borders.sourceTopBorder > 0) { // top
            const QRectF targetTopRect(targetCenterLeft, targetTop, targetCenterWidth, targetTopBorder);
            const QRectF sourceTopRect(sourceCenterLeft, sourceTop, sourceCenterWidth, borders.sourceTopBorder);
            qDrawHorizontallyRoundedPixmap(painter, targetTopRect, pixmap, sourceTopRect);
        }
        if (targetBottomBorder > 0 && borders.sourceBottomBorder > 0) { // bottom
            const QRectF targetBottomRect(targetCenterLeft, targetCenterBottom, targetCenterWidth, targetBottomBorder);
            const QRectF sourceBottomRect(sourceCenterLeft, sourceCenterBottom, sourceCenterWidth, borders.sourceBottomBorder);
            qDrawHorizontallyRoundedPixmap(painter, targetBottomRect, pixmap, sourceBottomRect);
        }
        break;
    }

    // vertical edges
    switch (borders.verticalRule) {
        case QtPixmapBorders::Stretch:
        if (targetLeftBorder > 0 && borders.sourceLeftBorder > 0) { // left
            const QRectF targetLeftRect(targetLeft, targetCenterTop, targetLeftBorder, targetCenterHeight);
            const QRectF sourceLeftRect(sourceLeft, sourceCenterTop, borders.sourceLeftBorder, sourceCenterHeight);
            qDrawPixmap(painter, targetLeftRect, pixmap, sourceLeftRect);
        }
        if (targetRightBorder > 0 && borders.sourceRightBorder > 0) { // right
            const QRectF targetRightRect(targetCenterRight, targetCenterTop, targetRightBorder, targetCenterHeight);
            const QRectF sourceRightRect(sourceCenterRight, sourceCenterTop, borders.sourceRightBorder, sourceCenterHeight);
            qDrawPixmap(painter, targetRightRect, pixmap, sourceRightRect);
        }
        break;
        case QtPixmapBorders::Repeat:
        if (targetLeftBorder > 0 && borders.sourceLeftBorder > 0) { // left
            const QRectF targetLeftRect(targetLeft, targetCenterTop, targetLeftBorder, targetCenterHeight);
            const QRectF sourceLeftRect(sourceLeft, sourceCenterTop, borders.sourceLeftBorder, sourceCenterHeight);
            qDrawVerticallyRepeatedPixmap(painter, targetLeftRect, pixmap, sourceLeftRect);
        }
        if (targetRightBorder > 0 && borders.sourceRightBorder > 0) { // right
            const QRectF targetRightRect(targetCenterRight, targetCenterTop, targetRightBorder, targetCenterHeight);
            const QRectF sourceRightRect(sourceCenterRight, sourceCenterTop, borders.sourceRightBorder, sourceCenterHeight);
            qDrawVerticallyRepeatedPixmap(painter, targetRightRect, pixmap, sourceRightRect);
        }
        break;
        case QtPixmapBorders::Round:
        if (targetLeftBorder > 0 && borders.sourceLeftBorder > 0) { // left
            const QRectF targetLeftRect(targetLeft, targetCenterTop, targetLeftBorder, targetCenterHeight);
            const QRectF sourceLeftRect(sourceLeft, sourceCenterTop, borders.sourceLeftBorder, sourceCenterHeight);
            qDrawVerticallyRoundedPixmap(painter, targetLeftRect, pixmap, sourceLeftRect);
        }
        if (targetRightBorder > 0 && borders.sourceRightBorder > 0) { // right
            const QRectF targetRightRect(targetCenterRight, targetCenterTop, targetRightBorder, targetCenterHeight);
            const QRectF sourceRightRect(sourceCenterRight, sourceCenterTop, borders.sourceRightBorder, sourceCenterHeight);
            qDrawVerticallyRoundedPixmap(painter, targetRightRect, pixmap, sourceRightRect);
        }
        break;
    }

    // center
    if (targetCenterWidth > 0 && targetCenterHeight > 0 && sourceCenterWidth > 0 && sourceCenterHeight > 0) {
        const QRectF targetCenterRect(targetCenterLeft, targetCenterTop, targetCenterWidth, targetCenterHeight);
        const QRectF sourceCenterRect(sourceCenterLeft, sourceCenterTop, sourceCenterWidth, sourceCenterHeight);
        switch (borders.horizontalRule) {
        case QtPixmapBorders::Stretch:
            switch (borders.verticalRule) {
                case QtPixmapBorders::Stretch: // stretch stretch
                    qDrawPixmap(painter, targetCenterRect, pixmap, sourceCenterRect);
                break;
                case QtPixmapBorders::Repeat: // stretch repeat
                    qVerticalRepeat(painter, targetCenterRect, pixmap, sourceCenterRect, qDrawPixmap);
                break;
                case QtPixmapBorders::Round: // stretch round
                    qVerticalRound(painter, targetCenterRect, pixmap, sourceCenterRect, qDrawPixmap);
                break;
            }
            break;
        case QtPixmapBorders::Repeat:
            switch (borders.verticalRule) {
                case QtPixmapBorders::Stretch: // repeat stretch
                    qHorizontalRepeat(painter, targetCenterRect, pixmap, sourceCenterRect, qDrawPixmap);
                break;
                case QtPixmapBorders::Repeat: // repeat repeat
                    qVerticalRepeat(painter, targetCenterRect, pixmap, sourceCenterRect, qDrawHorizontallyRepeatedPixmap);
                break;
                case QtPixmapBorders::Round: // repeat round
                    qVerticalRound(painter, targetCenterRect, pixmap, sourceCenterRect, qDrawHorizontallyRepeatedPixmap);
                break;
            }
            break;
        case QtPixmapBorders::Round:
            switch (borders.verticalRule) {
                case QtPixmapBorders::Stretch: // round stretch
                    qHorizontalRound(painter, targetCenterRect, pixmap, sourceCenterRect, qDrawPixmap);
                break;
                case QtPixmapBorders::Repeat: // round repeat
                    qHorizontalRound(painter, targetCenterRect, pixmap, sourceCenterRect, qDrawVerticallyRepeatedPixmap);
                break;
                case QtPixmapBorders::Round: // round round
                    qHorizontalRound(painter, targetCenterRect, pixmap, sourceCenterRect, qDrawVerticallyRoundedPixmap);
                break;
            }
            break;
        }
    }
}
