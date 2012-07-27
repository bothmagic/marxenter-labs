/****************************************************************************
** Meta object code from reading C++ file 'qtableselectionmanager.h'
**
** Created: Thu 7. Jul 08:18:03 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtableselectionmanager.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtableselectionmanager.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTableSelectionManager[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      13,   14, // methods
       4,   79, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       3,       // signalCount

 // signals: signature, parameters, type, tag, flags
      77,   25,   24,   24, 0x05,
     159,  109,   24,   24, 0x05,
     198,  190,   24,   24, 0x05,

 // slots: signature, parameters, type, tag, flags
     257,  246,   24,   24, 0x0a,
     281,  246,   24,   24, 0x0a,
     315,  304,   24,   24, 0x0a,
     370,  364,   24,   24, 0x2a,
     405,   24,   24,   24, 0x0a,
     423,   24,   24,   24, 0x08,
     453,  443,   24,   24, 0x08,
     478,  443,   24,   24, 0x08,
     515,  502,   24,   24, 0x08,
     543,  502,   24,   24, 0x08,

 // properties: name, type, flags
     577,  570, 0x0009510b,
     589,  570, 0x0009510b,
     605,  600, 0x01095001,
     641,  627, 0x00095009,

       0        // eod
};

static const char qt_meta_stringdata_QtTableSelectionManager[] = {
    "QtTableSelectionManager\0\0"
    "currentRow,currentColumn,previousRow,previousColumn\0"
    "currentChanged(int,int,int,int)\0"
    "anchorRow,anchorColumn,previousRow,previousColumn\0"
    "anchorChanged(int,int,int,int)\0changed\0"
    "selectionsChanged(QList<QtTableSelectionRange>)\0"
    "row,column\0setCurrentCell(int,int)\0"
    "setAnchorCell(int,int)\0range,mode\0"
    "setSelected(QtTableSelectionRange,SelectionMode)\0"
    "range\0setSelected(QtTableSelectionRange)\0"
    "clearSelections()\0_q_modelDestroyed()\0"
    "row,count\0_q_rowsInserted(int,int)\0"
    "_q_rowsRemoved(int,int)\0column,count\0"
    "_q_columnsInserted(int,int)\0"
    "_q_columnsRemoved(int,int)\0QtCell\0"
    "currentCell\0anchorCell\0bool\0"
    "anchorSelectionActive\0SelectionMode\0"
    "selectionMode\0"
};

const QMetaObject QtTableSelectionManager::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_QtTableSelectionManager,
      qt_meta_data_QtTableSelectionManager, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTableSelectionManager::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTableSelectionManager::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTableSelectionManager::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTableSelectionManager))
        return static_cast<void*>(const_cast< QtTableSelectionManager*>(this));
    return QObject::qt_metacast(_clname);
}

int QtTableSelectionManager::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: currentChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3])),(*reinterpret_cast< int(*)>(_a[4]))); break;
        case 1: anchorChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3])),(*reinterpret_cast< int(*)>(_a[4]))); break;
        case 2: selectionsChanged((*reinterpret_cast< const QList<QtTableSelectionRange>(*)>(_a[1]))); break;
        case 3: setCurrentCell((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 4: setAnchorCell((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 5: setSelected((*reinterpret_cast< const QtTableSelectionRange(*)>(_a[1])),(*reinterpret_cast< SelectionMode(*)>(_a[2]))); break;
        case 6: setSelected((*reinterpret_cast< const QtTableSelectionRange(*)>(_a[1]))); break;
        case 7: clearSelections(); break;
        case 8: d_func()->_q_modelDestroyed(); break;
        case 9: d_func()->_q_rowsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 10: d_func()->_q_rowsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 11: d_func()->_q_columnsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 12: d_func()->_q_columnsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        default: ;
        }
        _id -= 13;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< QtCell*>(_v) = currentCell(); break;
        case 1: *reinterpret_cast< QtCell*>(_v) = anchorCell(); break;
        case 2: *reinterpret_cast< bool*>(_v) = isAnchorSelectionActive(); break;
        case 3: *reinterpret_cast< SelectionMode*>(_v) = selectionMode(); break;
        }
        _id -= 4;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setCurrentCell(*reinterpret_cast< QtCell*>(_v)); break;
        case 1: setAnchorCell(*reinterpret_cast< QtCell*>(_v)); break;
        }
        _id -= 4;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 4;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}

// SIGNAL 0
void QtTableSelectionManager::currentChanged(int _t1, int _t2, int _t3, int _t4)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)), const_cast<void*>(reinterpret_cast<const void*>(&_t3)), const_cast<void*>(reinterpret_cast<const void*>(&_t4)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtTableSelectionManager::anchorChanged(int _t1, int _t2, int _t3, int _t4)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)), const_cast<void*>(reinterpret_cast<const void*>(&_t3)), const_cast<void*>(reinterpret_cast<const void*>(&_t4)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtTableSelectionManager::selectionsChanged(const QList<QtTableSelectionRange> & _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}
QT_END_MOC_NAMESPACE
