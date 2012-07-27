/****************************************************************************
** Meta object code from reading C++ file 'qgraphicslistview.h'
**
** Created: Thu 7. Jul 08:17:44 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qgraphicslistview.h"
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qgraphicslistview.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtGraphicsListViewItem[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       0,    0, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

       0        // eod
};

static const char qt_meta_stringdata_QtGraphicsListViewItem[] = {
    "QtGraphicsListViewItem\0"
};

const QMetaObject QtGraphicsListViewItem::staticMetaObject = {
    { &QGraphicsWidget::staticMetaObject, qt_meta_stringdata_QtGraphicsListViewItem,
      qt_meta_data_QtGraphicsListViewItem, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtGraphicsListViewItem::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtGraphicsListViewItem::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtGraphicsListViewItem::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtGraphicsListViewItem))
        return static_cast<void*>(const_cast< QtGraphicsListViewItem*>(this));
    return QGraphicsWidget::qt_metacast(_clname);
}

int QtGraphicsListViewItem::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QGraphicsWidget::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    return _id;
}
static const uint qt_meta_data_QtGraphicsListView[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      14,   14, // methods
       5,   84, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       3,       // signalCount

 // signals: signature, parameters, type, tag, flags
      32,   20,   19,   19, 0x05,
      74,   68,   19,   19, 0x05,
     104,   97,   19,   19, 0x05,

 // slots: signature, parameters, type, tag, flags
     125,   68,   19,   19, 0x0a,
     144,   97,   19,   19, 0x0a,
     161,   19,   19,   19, 0x0a,
     176,   19,   19,   19, 0x08,
     201,   19,   19,   19, 0x08,
     221,   19,   19,   19, 0x08,
     264,  246,   19,   19, 0x08,
     319,  307,   19,   19, 0x08,
     345,  307,   19,   19, 0x08,
     377,  370,   19,   19, 0x08,
     438,  421,   19,   19, 0x08,

 // properties: name, type, flags
      20,  465, 0x0049510b,
     499,  481, 0x0009510b,
     517,  513, 0x02495103,
      97,  528, (QMetaType::QReal << 24) | 0x00495103,
     551,  534, 0x0009510b,

 // properties: notify_signal_id
       0,
       0,
       1,
       2,
       0,

       0        // eod
};

static const char qt_meta_stringdata_QtGraphicsListView[] = {
    "QtGraphicsListView\0\0orientation\0"
    "orientationChanged(Qt::Orientation)\0"
    "index\0firstIndexChanged(int)\0offset\0"
    "offsetChanged(qreal)\0setFirstIndex(int)\0"
    "setOffset(qreal)\0updateLayout()\0"
    "_q_controllerDestroyed()\0_q_modelDestroyed()\0"
    "_q_selectionsDestroyed()\0index,count,roles\0"
    "_q_itemsChanged(int,int,QList<QByteArray>)\0"
    "index,count\0_q_itemsInserted(int,int)\0"
    "_q_itemsRemoved(int,int)\0change\0"
    "_q_selectionsChanged(QtListSelectionChange)\0"
    "current,previous\0_q_currentChanged(int,int)\0"
    "Qt::Orientation\0Qt::TextElideMode\0"
    "textElideMode\0int\0firstIndex\0qreal\0"
    "QGraphicsObject*\0highlight\0"
};

const QMetaObject QtGraphicsListView::staticMetaObject = {
    { &QGraphicsWidget::staticMetaObject, qt_meta_stringdata_QtGraphicsListView,
      qt_meta_data_QtGraphicsListView, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtGraphicsListView::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtGraphicsListView::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtGraphicsListView::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtGraphicsListView))
        return static_cast<void*>(const_cast< QtGraphicsListView*>(this));
    return QGraphicsWidget::qt_metacast(_clname);
}

int QtGraphicsListView::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QGraphicsWidget::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: orientationChanged((*reinterpret_cast< Qt::Orientation(*)>(_a[1]))); break;
        case 1: firstIndexChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 2: offsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 3: setFirstIndex((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 4: setOffset((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 5: updateLayout(); break;
        case 6: d_func()->_q_controllerDestroyed(); break;
        case 7: d_func()->_q_modelDestroyed(); break;
        case 8: d_func()->_q_selectionsDestroyed(); break;
        case 9: d_func()->_q_itemsChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< const QList<QByteArray>(*)>(_a[3]))); break;
        case 10: d_func()->_q_itemsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 11: d_func()->_q_itemsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 12: d_func()->_q_selectionsChanged((*reinterpret_cast< const QtListSelectionChange(*)>(_a[1]))); break;
        case 13: d_func()->_q_currentChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        default: ;
        }
        _id -= 14;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< Qt::Orientation*>(_v) = orientation(); break;
        case 1: *reinterpret_cast< Qt::TextElideMode*>(_v) = textElideMode(); break;
        case 2: *reinterpret_cast< int*>(_v) = firstIndex(); break;
        case 3: *reinterpret_cast< qreal*>(_v) = offset(); break;
        case 4: *reinterpret_cast< QGraphicsObject**>(_v) = highlight(); break;
        }
        _id -= 5;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setOrientation(*reinterpret_cast< Qt::Orientation*>(_v)); break;
        case 1: setTextElideMode(*reinterpret_cast< Qt::TextElideMode*>(_v)); break;
        case 2: setFirstIndex(*reinterpret_cast< int*>(_v)); break;
        case 3: setOffset(*reinterpret_cast< qreal*>(_v)); break;
        case 4: setHighlight(*reinterpret_cast< QGraphicsObject**>(_v)); break;
        }
        _id -= 5;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 5;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 5;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 5;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 5;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 5;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 5;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}

// SIGNAL 0
void QtGraphicsListView::orientationChanged(Qt::Orientation _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtGraphicsListView::firstIndexChanged(int _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtGraphicsListView::offsetChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}
QT_END_MOC_NAMESPACE
