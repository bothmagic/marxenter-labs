/****************************************************************************
** Meta object code from reading C++ file 'qgraphicstreeview.h'
**
** Created: Thu 7. Jul 08:18:07 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qgraphicstreeview.h"
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qgraphicstreeview.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtGraphicsTreeView[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      22,   14, // methods
       5,  124, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       6,       // signalCount

 // signals: signature, parameters, type, tag, flags
      26,   20,   19,   19, 0x05,
      56,   49,   19,   19, 0x05,
      87,   49,   19,   19, 0x05,
     125,  116,   19,   19, 0x05,
     164,  116,   19,   19, 0x05,
     204,   19,   19,   19, 0x05,

 // slots: signature, parameters, type, tag, flags
     220,   20,   19,   19, 0x0a,
     239,   49,   19,   19, 0x0a,
     266,   49,   19,   19, 0x0a,
     303,  291,   19,   19, 0x0a,
     349,  346,   19,   19, 0x2a,
     387,   19,   19,   19, 0x0a,
     402,   19,   19,   19, 0x08,
     427,   19,   19,   19, 0x08,
     447,   19,   19,   19, 0x08,
     472,   19,   19,   19, 0x08,
     508,  493,   19,   19, 0x08,
     578,  116,   19,   19, 0x08,
     631,  116,   19,   19, 0x08,
     693,  683,   19,   19, 0x08,
     748,  731,   19,   19, 0x08,
     829,   19,   19,   19, 0x08,

 // properties: name, type, flags
     857,  840, 0x0009510b,
     867,  863, 0x02095103,
     884,  878, (QMetaType::QReal << 24) | 0x00095103,
     901,  878, (QMetaType::QReal << 24) | 0x00095103,
     934,  916, 0x0009510b,

       0        // eod
};

static const char qt_meta_stringdata_QtGraphicsTreeView[] = {
    "QtGraphicsTreeView\0\0index\0"
    "firstIndexChanged(int)\0offset\0"
    "horizontalOffsetChanged(qreal)\0"
    "verticalOffsetChanged(qreal)\0it,count\0"
    "itemExpanded(QtTreeModelIterator&,int)\0"
    "itemCollapsed(QtTreeModelIterator&,int)\0"
    "layoutChanged()\0setFirstIndex(int)\0"
    "setHorizontalOffset(qreal)\0"
    "setVerticalOffset(qreal)\0it,expanded\0"
    "setItemExpanded(QtTreeModelIterator&,bool)\0"
    "it\0setItemExpanded(QtTreeModelIterator&)\0"
    "updateLayout()\0_q_controllerDestroyed()\0"
    "_q_modelDestroyed()\0_q_selectionsDestroyed()\0"
    "_q_headerDestroyed()\0it,count,roles\0"
    "_q_itemsChanged(QtTreeModelBase::iterator_base,int,QList<QByteArray>)\0"
    "_q_itemsInserted(QtTreeModelBase::iterator_base,int)\0"
    "_q_itemsRemoved(QtTreeModelBase::iterator_base,int)\0"
    "selection\0_q_selectionsChanged(QtTreeSelection)\0"
    "current,previous\0"
    "_q_currentChanged(QtTreeModelBase::iterator_base,QtTreeModelBase::iter"
    "ator_base)\0"
    "_q_reset()\0QtTreeModelBase*\0model\0int\0"
    "firstIndex\0qreal\0horizontalOffset\0"
    "verticalOffset\0Qt::TextElideMode\0"
    "textElideMode\0"
};

const QMetaObject QtGraphicsTreeView::staticMetaObject = {
    { &QGraphicsWidget::staticMetaObject, qt_meta_stringdata_QtGraphicsTreeView,
      qt_meta_data_QtGraphicsTreeView, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtGraphicsTreeView::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtGraphicsTreeView::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtGraphicsTreeView::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtGraphicsTreeView))
        return static_cast<void*>(const_cast< QtGraphicsTreeView*>(this));
    return QGraphicsWidget::qt_metacast(_clname);
}

int QtGraphicsTreeView::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QGraphicsWidget::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: firstIndexChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 1: horizontalOffsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 2: verticalOffsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 3: itemExpanded((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 4: itemCollapsed((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 5: layoutChanged(); break;
        case 6: setFirstIndex((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 7: setHorizontalOffset((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 8: setVerticalOffset((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 9: setItemExpanded((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 10: setItemExpanded((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1]))); break;
        case 11: updateLayout(); break;
        case 12: d_func()->_q_controllerDestroyed(); break;
        case 13: d_func()->_q_modelDestroyed(); break;
        case 14: d_func()->_q_selectionsDestroyed(); break;
        case 15: d_func()->_q_headerDestroyed(); break;
        case 16: d_func()->_q_itemsChanged((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< const QList<QByteArray>(*)>(_a[3]))); break;
        case 17: d_func()->_q_itemsInserted((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 18: d_func()->_q_itemsRemoved((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 19: d_func()->_q_selectionsChanged((*reinterpret_cast< const QtTreeSelection(*)>(_a[1]))); break;
        case 20: d_func()->_q_currentChanged((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[2]))); break;
        case 21: d_func()->_q_reset(); break;
        default: ;
        }
        _id -= 22;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< QtTreeModelBase**>(_v) = model(); break;
        case 1: *reinterpret_cast< int*>(_v) = firstIndex(); break;
        case 2: *reinterpret_cast< qreal*>(_v) = horizontalOffset(); break;
        case 3: *reinterpret_cast< qreal*>(_v) = verticalOffset(); break;
        case 4: *reinterpret_cast< Qt::TextElideMode*>(_v) = textElideMode(); break;
        }
        _id -= 5;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setModel(*reinterpret_cast< QtTreeModelBase**>(_v)); break;
        case 1: setFirstIndex(*reinterpret_cast< int*>(_v)); break;
        case 2: setHorizontalOffset(*reinterpret_cast< qreal*>(_v)); break;
        case 3: setVerticalOffset(*reinterpret_cast< qreal*>(_v)); break;
        case 4: setTextElideMode(*reinterpret_cast< Qt::TextElideMode*>(_v)); break;
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
void QtGraphicsTreeView::firstIndexChanged(int _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtGraphicsTreeView::horizontalOffsetChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtGraphicsTreeView::verticalOffsetChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}

// SIGNAL 3
void QtGraphicsTreeView::itemExpanded(QtTreeModelIterator & _t1, int _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 3, _a);
}

// SIGNAL 4
void QtGraphicsTreeView::itemCollapsed(QtTreeModelIterator & _t1, int _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 4, _a);
}

// SIGNAL 5
void QtGraphicsTreeView::layoutChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 5, 0);
}
QT_END_MOC_NAMESPACE
