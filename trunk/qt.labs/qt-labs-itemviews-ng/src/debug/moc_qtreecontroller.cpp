/****************************************************************************
** Meta object code from reading C++ file 'qtreecontroller.h'
**
** Created: Thu 7. Jul 08:18:05 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtreecontroller.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtreecontroller.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTreeController[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      18,   14, // methods
       1,  104, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       6,       // signalCount

 // signals: signature, parameters, type, tag, flags
      28,   18,   17,   17, 0x05,
      95,   78,   17,   17, 0x05,
     148,   78,   17,   17, 0x05,
     196,   78,   17,   17, 0x05,
     275,  269,   17,   17, 0x05,
     309,  269,   17,   17, 0x05,

 // slots: signature, parameters, type, tag, flags
     345,  269,   17,   17, 0x0a,
     375,  269,   17,   17, 0x0a,
     407,  269,   17,   17, 0x0a,
     435,  269,   17,   17, 0x0a,
     465,   17,   17,   17, 0x08,
     484,   17,   17,   17, 0x08,
     504,   17,   17,   17, 0x08,
     529,   17,   17,   17, 0x08,
     556,  550,   17,   17, 0x08,
     590,  582,   17,   17, 0x08,
     625,  618,   17,   17, 0x08,
     657,  618,   17,   17, 0x08,

 // properties: name, type, flags
     708,  691, 0x0009510b,

       0        // eod
};

static const char qt_meta_stringdata_QtTreeController[] = {
    "QtTreeController\0\0it,button\0"
    "itemClicked(QtTreeModelIterator&,Qt::MouseButton)\0"
    "current,previous\0"
    "viewChanged(QtGraphicsTreeView*,QtGraphicsTreeView*)\0"
    "modelChanged(QtTreeModelBase*,QtTreeModelBase*)\0"
    "selectionManagerChanged(QtTreeSelectionManager*,QtTreeSelectionManager"
    "*)\0"
    "value\0verticalScrollValueChanged(qreal)\0"
    "horizontalScrollValueChanged(qreal)\0"
    "setVerticalScrollValue(qreal)\0"
    "setHorizontalScrollValue(qreal)\0"
    "setVerticalScrollValue(int)\0"
    "setHorizontalScrollValue(int)\0"
    "_q_viewDestroyed()\0_q_modelDestroyed()\0"
    "_q_selectionsDestroyed()\0_q_headerDestroyed()\0"
    "index\0_q_firstIndexChanged(int)\0section\0"
    "_q_firstSectionChanged(int)\0offset\0"
    "_q_verticalOffsetChanged(qreal)\0"
    "_q_horizontalOffsetChanged(qreal)\0"
    "QtTreeModelBase*\0model\0"
};

const QMetaObject QtTreeController::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_QtTreeController,
      qt_meta_data_QtTreeController, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTreeController::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTreeController::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTreeController::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTreeController))
        return static_cast<void*>(const_cast< QtTreeController*>(this));
    return QObject::qt_metacast(_clname);
}

int QtTreeController::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: itemClicked((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< Qt::MouseButton(*)>(_a[2]))); break;
        case 1: viewChanged((*reinterpret_cast< QtGraphicsTreeView*(*)>(_a[1])),(*reinterpret_cast< QtGraphicsTreeView*(*)>(_a[2]))); break;
        case 2: modelChanged((*reinterpret_cast< QtTreeModelBase*(*)>(_a[1])),(*reinterpret_cast< QtTreeModelBase*(*)>(_a[2]))); break;
        case 3: selectionManagerChanged((*reinterpret_cast< QtTreeSelectionManager*(*)>(_a[1])),(*reinterpret_cast< QtTreeSelectionManager*(*)>(_a[2]))); break;
        case 4: verticalScrollValueChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 5: horizontalScrollValueChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 6: setVerticalScrollValue((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 7: setHorizontalScrollValue((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 8: setVerticalScrollValue((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 9: setHorizontalScrollValue((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 10: d_func()->_q_viewDestroyed(); break;
        case 11: d_func()->_q_modelDestroyed(); break;
        case 12: d_func()->_q_selectionsDestroyed(); break;
        case 13: d_func()->_q_headerDestroyed(); break;
        case 14: d_func()->_q_firstIndexChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 15: d_func()->_q_firstSectionChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 16: d_func()->_q_verticalOffsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 17: d_func()->_q_horizontalOffsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        default: ;
        }
        _id -= 18;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< QtTreeModelBase**>(_v) = model(); break;
        }
        _id -= 1;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setModel(*reinterpret_cast< QtTreeModelBase**>(_v)); break;
        }
        _id -= 1;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 1;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 1;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}

// SIGNAL 0
void QtTreeController::itemClicked(QtTreeModelIterator & _t1, Qt::MouseButton _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtTreeController::viewChanged(QtGraphicsTreeView * _t1, QtGraphicsTreeView * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtTreeController::modelChanged(QtTreeModelBase * _t1, QtTreeModelBase * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}

// SIGNAL 3
void QtTreeController::selectionManagerChanged(QtTreeSelectionManager * _t1, QtTreeSelectionManager * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 3, _a);
}

// SIGNAL 4
void QtTreeController::verticalScrollValueChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 4, _a);
}

// SIGNAL 5
void QtTreeController::horizontalScrollValueChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 5, _a);
}
QT_END_MOC_NAMESPACE
