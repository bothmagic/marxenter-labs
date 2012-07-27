/****************************************************************************
** Meta object code from reading C++ file 'qlistcontroller.h'
**
** Created: Thu 7. Jul 08:17:46 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qlistcontroller.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qlistcontroller.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtListController[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      15,   14, // methods
       5,   89, // properties
       1,  104, // enums/sets
       0,    0, // constructors
       0,       // flags
       6,       // signalCount

 // signals: signature, parameters, type, tag, flags
      24,   18,   17,   17, 0x05,
      56,   43,   17,   17, 0x05,
     106,   89,   17,   17, 0x05,
     164,   89,   17,   17, 0x05,
     237,   89,   17,   17, 0x05,
     296,  290,   17,   17, 0x05,

 // slots: signature, parameters, type, tag, flags
     322,  290,   17,   17, 0x0a,
     344,  290,   17,   17, 0x0a,
     364,   17,   17,   17, 0x08,
     384,   17,   17,   17, 0x08,
     403,   17,   17,   17, 0x08,
     428,   17,   17,   17, 0x08,
     454,   17,   17,   17, 0x08,
     480,  478,   17,   17, 0x08,
     507,   17,   17,   17, 0x08,

 // properties: name, type, flags
     552,  530, 0x0009510b,
     582,  558, 0x0009510b,
     619,  599, 0x0009510b,
     642,  624, 0x0009510b,
     665,  660, 0x01095003,

 // enums: name, flags, count, data
     624, 0x0,    3,  108,

 // enum data: key, value
     680, uint(QtListController::NoSelection),
     692, uint(QtListController::SingleSelection),
     708, uint(QtListController::MultiSelection),

       0        // eod
};

static const char qt_meta_stringdata_QtListController[] = {
    "QtListController\0\0index\0itemActivated(int)\0"
    "index,button\0itemClicked(int,Qt::MouseButton)\0"
    "current,previous\0"
    "modelChanged(QtListModelInterface*,QtListModelInterface*)\0"
    "selectionManagerChanged(QtListSelectionManager*,QtListSelectionManager"
    "*)\0"
    "viewChanged(QtGraphicsListView*,QtGraphicsListView*)\0"
    "value\0scrollValueChanged(qreal)\0"
    "setScrollValue(qreal)\0setScrollValue(int)\0"
    "_q_modelDestroyed()\0_q_viewDestroyed()\0"
    "_q_selectionsDestroyed()\0"
    "_q_firstIndexChanged(int)\0"
    "_q_offsetChanged(qreal)\0,\0"
    "_q_currentChanged(int,int)\0"
    "_q_animationFinished()\0QtListModelInterface*\0"
    "model\0QtListSelectionManager*\0"
    "selectionManager\0QtGraphicsListView*\0"
    "view\0SelectionBehavior\0selectionBehavior\0"
    "bool\0isWheelEnabled\0NoSelection\0"
    "SingleSelection\0MultiSelection\0"
};

const QMetaObject QtListController::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_QtListController,
      qt_meta_data_QtListController, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtListController::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtListController::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtListController::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtListController))
        return static_cast<void*>(const_cast< QtListController*>(this));
    return QObject::qt_metacast(_clname);
}

int QtListController::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: itemActivated((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 1: itemClicked((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::MouseButton(*)>(_a[2]))); break;
        case 2: modelChanged((*reinterpret_cast< QtListModelInterface*(*)>(_a[1])),(*reinterpret_cast< QtListModelInterface*(*)>(_a[2]))); break;
        case 3: selectionManagerChanged((*reinterpret_cast< QtListSelectionManager*(*)>(_a[1])),(*reinterpret_cast< QtListSelectionManager*(*)>(_a[2]))); break;
        case 4: viewChanged((*reinterpret_cast< QtGraphicsListView*(*)>(_a[1])),(*reinterpret_cast< QtGraphicsListView*(*)>(_a[2]))); break;
        case 5: scrollValueChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 6: setScrollValue((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 7: setScrollValue((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 8: d_func()->_q_modelDestroyed(); break;
        case 9: d_func()->_q_viewDestroyed(); break;
        case 10: d_func()->_q_selectionsDestroyed(); break;
        case 11: d_func()->_q_firstIndexChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 12: d_func()->_q_offsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 13: d_func()->_q_currentChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 14: d_func()->_q_animationFinished(); break;
        default: ;
        }
        _id -= 15;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< QtListModelInterface**>(_v) = model(); break;
        case 1: *reinterpret_cast< QtListSelectionManager**>(_v) = selectionManager(); break;
        case 2: *reinterpret_cast< QtGraphicsListView**>(_v) = view(); break;
        case 3: *reinterpret_cast< SelectionBehavior*>(_v) = selectionBehavior(); break;
        case 4: *reinterpret_cast< bool*>(_v) = isWheelEnabled(); break;
        }
        _id -= 5;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setModel(*reinterpret_cast< QtListModelInterface**>(_v)); break;
        case 1: setSelectionManager(*reinterpret_cast< QtListSelectionManager**>(_v)); break;
        case 2: setView(*reinterpret_cast< QtGraphicsListView**>(_v)); break;
        case 3: setSelectionBehavior(*reinterpret_cast< SelectionBehavior*>(_v)); break;
        case 4: setWheelEnabled(*reinterpret_cast< bool*>(_v)); break;
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
void QtListController::itemActivated(int _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtListController::itemClicked(int _t1, Qt::MouseButton _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtListController::modelChanged(QtListModelInterface * _t1, QtListModelInterface * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}

// SIGNAL 3
void QtListController::selectionManagerChanged(QtListSelectionManager * _t1, QtListSelectionManager * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 3, _a);
}

// SIGNAL 4
void QtListController::viewChanged(QtGraphicsListView * _t1, QtGraphicsListView * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 4, _a);
}

// SIGNAL 5
void QtListController::scrollValueChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 5, _a);
}
QT_END_MOC_NAMESPACE
