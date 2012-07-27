#include "QtGui/QtGui"
#include "drilldownview.h"


void DrillDownView::paintEvent(QPaintEvent *event) {
    if (animation.state() == QTimeLine::Running) {
        QPainter painter(viewport());
        if (animation.direction() == QTimeLine::Backward) {
            painter.drawPixmap(-animation.currentFrame(), 0, newView);
            painter.drawPixmap(-animation.currentFrame() + animation.endFrame(), 0, oldView);
        } else {
            painter.drawPixmap(-animation.currentFrame(), 0, oldView);
            painter.drawPixmap(-animation.currentFrame() + animation.endFrame(), 0, newView);
        }
    } else {
        QListView::paintEvent(event);
    }
}

void DrillDownView::slide(int x){
    viewport()->scroll(lastPosition - x, 0);
    lastPosition = x;
}

QModelIndex DrillDownView::moveCursor(CursorAction cursorAction, Qt::KeyboardModifiers modifiers)
{
    if (animation.state() == QTimeLine::Running)
        return QListView::moveCursor(cursorAction, modifiers);

    QModelIndex current = currentIndex();
    if (cursorAction == MoveLeft && current.parent().isValid()) {
        oldView = QPixmap::grabWidget(viewport());
        return current.parent();
    }

    if (cursorAction == MoveRight && model() && model()->hasChildren(current)) {
        oldView = QPixmap::grabWidget(viewport());
        return model()->index(0, 0, current);
    }

    return QListView::moveCursor(cursorAction, modifiers);
}

void DrillDownView::currentChanged(const QModelIndex &current, const QModelIndex &previous) {
    if ((current.isValid() && previous.isValid())
        && (current.parent() == previous || previous.parent() == current)) {
        setUpdatesEnabled(false);
        setRootIndex(currentIndex().parent());
        setCurrentIndex(currentIndex());
        executeDelayedItemsLayout();
        // Force the hiding/showing of scrollbars
        setVerticalScrollBarPolicy(verticalScrollBarPolicy());
        newView = QPixmap::grabWidget(viewport());
        setUpdatesEnabled(true);

        int length = qMax(oldView.width(), newView.width());
        lastPosition = (previous.parent() == current) ? length : 0;
        animation.setFrameRange(0, length);
        animation.stop();
        animation.setDirection(previous.parent() == current ? QTimeLine::Backward : QTimeLine::Forward);
        animation.start();
    } else {
        QListView::currentChanged(current, previous);
    }
}

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    DrillDownView view;
    view.setWindowTitle("Use arrow keys");
    QDirModel dirmodel;
    view.setModel(&dirmodel);
    view.show();
    return app.exec();
}
