/****************************************************************************
** Meta object code from reading C++ file 'qtablecontroller.h'
**
** Created: Thu 7. Jul 08:17:55 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtablecontroller.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtablecontroller.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTableController[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      27,   14, // methods
       5,  149, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       8,       // signalCount

 // signals: signature, parameters, type, tag, flags
      37,   19,   18,   18, 0x05,
      91,   74,   18,   18, 0x05,
     146,   74,   18,   18, 0x05,
     206,   74,   18,   18, 0x05,
     281,   74,   18,   18, 0x05,
     340,   74,   18,   18, 0x05,
     407,  401,   18,   18, 0x05,
     441,  401,   18,   18, 0x05,

 // slots: signature, parameters, type, tag, flags
     477,  401,   18,   18, 0x0a,
     507,  401,   18,   18, 0x0a,
     539,   18,   18,   18, 0x08,
     558,   18,   18,   18, 0x08,
     578,   18,   18,   18, 0x08,
     603,   18,   18,   18, 0x08,
     632,   18,   18,   18, 0x08,
     677,  663,   18,   18, 0x08,
     718,  663,   18,   18, 0x08,
     760,  663,   18,   18, 0x08,
     823,  806,   18,   18, 0x08,
     867,  806,   18,   18, 0x08,
     912,  806,   18,   18, 0x08,
     965,  961,   18,   18, 0x08,
     996,  989,   18,   18, 0x08,
    1030, 1023,   18,   18, 0x08,
    1062, 1023,   18,   18, 0x08,
    1096,   18,   18,   18, 0x08,
    1138,   18,   18,   18, 0x08,

 // properties: name, type, flags
    1201, 1178, 0x0009510b,
    1232, 1207, 0x0009510b,
    1270, 1249, 0x0009510b,
    1293, 1275, 0x0009510b,
    1310, 1275, 0x0009510b,

       0        // eod
};

static const char qt_meta_stringdata_QtTableController[] = {
    "QtTableController\0\0row,column,button\0"
    "cellClicked(int,int,Qt::MouseButton)\0"
    "current,previous\0"
    "viewChanged(QtGraphicsTableView*,QtGraphicsTableView*)\0"
    "modelChanged(QtTableModelInterface*,QtTableModelInterface*)\0"
    "selectionManagerChanged(QtTableSelectionManager*,QtTableSelectionManag"
    "er*)\0"
    "verticalHeaderChanged(QtGraphicsHeader*,QtGraphicsHeader*)\0"
    "horizontalHeaderChanged(QtGraphicsHeader*,QtGraphicsHeader*)\0"
    "value\0verticalScrollValueChanged(qreal)\0"
    "horizontalScrollValueChanged(qreal)\0"
    "setVerticalScrollValue(qreal)\0"
    "setHorizontalScrollValue(qreal)\0"
    "_q_viewDestroyed()\0_q_modelDestroyed()\0"
    "_q_selectionsDestroyed()\0"
    "_q_verticalHeaderDestroyed()\0"
    "_q_horizontalHeaderDestroyed()\0"
    "row,modifiers\0_q_rowPressed(int,Qt::KeyboardModifiers)\0"
    "_q_rowReleased(int,Qt::KeyboardModifiers)\0"
    "_q_rowDragSelected(int,Qt::KeyboardModifiers)\0"
    "column,modifiers\0"
    "_q_columnPressed(int,Qt::KeyboardModifiers)\0"
    "_q_columnReleased(int,Qt::KeyboardModifiers)\0"
    "_q_columnDragSelected(int,Qt::KeyboardModifiers)\0"
    "row\0_q_firstRowChanged(int)\0column\0"
    "_q_firstColumnChanged(int)\0offset\0"
    "_q_verticalOffsetChanged(qreal)\0"
    "_q_horizontalOffsetChanged(qreal)\0"
    "_q_setHorizontalHeader(QtGraphicsHeader*)\0"
    "_q_setVerticalHeader(QtGraphicsHeader*)\0"
    "QtTableModelInterface*\0model\0"
    "QtTableSelectionManager*\0selectionManager\0"
    "QtGraphicsTableView*\0view\0QtGraphicsHeader*\0"
    "horizontalHeader\0verticalHeader\0"
};

const QMetaObject QtTableController::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_QtTableController,
      qt_meta_data_QtTableController, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTableController::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTableController::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTableController::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTableController))
        return static_cast<void*>(const_cast< QtTableController*>(this));
    return QObject::qt_metacast(_clname);
}

int QtTableController::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: cellClicked((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< Qt::MouseButton(*)>(_a[3]))); break;
        case 1: viewChanged((*reinterpret_cast< QtGraphicsTableView*(*)>(_a[1])),(*reinterpret_cast< QtGraphicsTableView*(*)>(_a[2]))); break;
        case 2: modelChanged((*reinterpret_cast< QtTableModelInterface*(*)>(_a[1])),(*reinterpret_cast< QtTableModelInterface*(*)>(_a[2]))); break;
        case 3: selectionManagerChanged((*reinterpret_cast< QtTableSelectionManager*(*)>(_a[1])),(*reinterpret_cast< QtTableSelectionManager*(*)>(_a[2]))); break;
        case 4: verticalHeaderChanged((*reinterpret_cast< QtGraphicsHeader*(*)>(_a[1])),(*reinterpret_cast< QtGraphicsHeader*(*)>(_a[2]))); break;
        case 5: horizontalHeaderChanged((*reinterpret_cast< QtGraphicsHeader*(*)>(_a[1])),(*reinterpret_cast< QtGraphicsHeader*(*)>(_a[2]))); break;
        case 6: verticalScrollValueChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 7: horizontalScrollValueChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 8: setVerticalScrollValue((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 9: setHorizontalScrollValue((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 10: d_func()->_q_viewDestroyed(); break;
        case 11: d_func()->_q_modelDestroyed(); break;
        case 12: d_func()->_q_selectionsDestroyed(); break;
        case 13: d_func()->_q_verticalHeaderDestroyed(); break;
        case 14: d_func()->_q_horizontalHeaderDestroyed(); break;
        case 15: d_func()->_q_rowPressed((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 16: d_func()->_q_rowReleased((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 17: d_func()->_q_rowDragSelected((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 18: d_func()->_q_columnPressed((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 19: d_func()->_q_columnReleased((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 20: d_func()->_q_columnDragSelected((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 21: d_func()->_q_firstRowChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 22: d_func()->_q_firstColumnChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 23: d_func()->_q_verticalOffsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 24: d_func()->_q_horizontalOffsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 25: d_func()->_q_setHorizontalHeader((*reinterpret_cast< QtGraphicsHeader*(*)>(_a[1]))); break;
        case 26: d_func()->_q_setVerticalHeader((*reinterpret_cast< QtGraphicsHeader*(*)>(_a[1]))); break;
        default: ;
        }
        _id -= 27;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< QtTableModelInterface**>(_v) = model(); break;
        case 1: *reinterpret_cast< QtTableSelectionManager**>(_v) = selectionManager(); break;
        case 2: *reinterpret_cast< QtGraphicsTableView**>(_v) = view(); break;
        case 3: *reinterpret_cast< QtGraphicsHeader**>(_v) = horizontalHeader(); break;
        case 4: *reinterpret_cast< QtGraphicsHeader**>(_v) = verticalHeader(); break;
        }
        _id -= 5;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setModel(*reinterpret_cast< QtTableModelInterface**>(_v)); break;
        case 1: setSelectionManager(*reinterpret_cast< QtTableSelectionManager**>(_v)); break;
        case 2: setView(*reinterpret_cast< QtGraphicsTableView**>(_v)); break;
        case 3: setHorizontalHeader(*reinterpret_cast< QtGraphicsHeader**>(_v)); break;
        case 4: setVerticalHeader(*reinterpret_cast< QtGraphicsHeader**>(_v)); break;
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
void QtTableController::cellClicked(int _t1, int _t2, Qt::MouseButton _t3)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)), const_cast<void*>(reinterpret_cast<const void*>(&_t3)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtTableController::viewChanged(QtGraphicsTableView * _t1, QtGraphicsTableView * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtTableController::modelChanged(QtTableModelInterface * _t1, QtTableModelInterface * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}

// SIGNAL 3
void QtTableController::selectionManagerChanged(QtTableSelectionManager * _t1, QtTableSelectionManager * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 3, _a);
}

// SIGNAL 4
void QtTableController::verticalHeaderChanged(QtGraphicsHeader * _t1, QtGraphicsHeader * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 4, _a);
}

// SIGNAL 5
void QtTableController::horizontalHeaderChanged(QtGraphicsHeader * _t1, QtGraphicsHeader * _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 5, _a);
}

// SIGNAL 6
void QtTableController::verticalScrollValueChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 6, _a);
}

// SIGNAL 7
void QtTableController::horizontalScrollValueChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 7, _a);
}
QT_END_MOC_NAMESPACE
