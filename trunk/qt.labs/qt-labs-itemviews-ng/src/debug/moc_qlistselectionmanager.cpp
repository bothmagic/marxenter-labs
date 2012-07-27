/****************************************************************************
** Meta object code from reading C++ file 'qlistselectionmanager.h'
**
** Created: Thu 7. Jul 08:17:52 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qlistselectionmanager.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qlistselectionmanager.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtListSelectionManager[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      13,   14, // methods
       3,   79, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       3,       // signalCount

 // signals: signature, parameters, type, tag, flags
      41,   24,   23,   23, 0x05,
      81,   65,   23,   23, 0x05,
     111,  104,   23,   23, 0x05,

 // slots: signature, parameters, type, tag, flags
     160,  152,   23,   23, 0x0a,
     187,  180,   23,   23, 0x0a,
     223,  206,   23,   23, 0x0a,
     270,  258,   23,   23, 0x2a,
     297,  291,   23,   23, 0x2a,
     314,   23,   23,   23, 0x0a,
     332,   23,   23,   23, 0x08,
     352,  258,   23,   23, 0x08,
     378,  258,   23,   23, 0x08,
     417,  403,   23,   23, 0x08,

 // properties: name, type, flags
     448,  444, 0x02095103,
     465,  460, 0x01095103,
     503,  489, 0x0009510b,

       0        // eod
};

static const char qt_meta_stringdata_QtListSelectionManager[] = {
    "QtListSelectionManager\0\0current,previous\0"
    "currentChanged(int,int)\0anchor,previous\0"
    "anchorChanged(int,int)\0change\0"
    "selectionsChanged(QtListSelectionChange)\0"
    "current\0setCurrentItem(int)\0anchor\0"
    "setAnchorItem(int)\0index,count,mode\0"
    "setSelected(int,int,SelectionMode)\0"
    "index,count\0setSelected(int,int)\0index\0"
    "setSelected(int)\0clearSelections()\0"
    "_q_modelDestroyed()\0_q_itemsInserted(int,int)\0"
    "_q_itemsRemoved(int,int)\0from,to,count\0"
    "_q_itemsMoved(int,int,int)\0int\0"
    "currentItem\0bool\0anchoredSelectionActive\0"
    "SelectionMode\0anchoredSelectionMode\0"
};

const QMetaObject QtListSelectionManager::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_QtListSelectionManager,
      qt_meta_data_QtListSelectionManager, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtListSelectionManager::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtListSelectionManager::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtListSelectionManager::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtListSelectionManager))
        return static_cast<void*>(const_cast< QtListSelectionManager*>(this));
    return QObject::qt_metacast(_clname);
}

int QtListSelectionManager::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: currentChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 1: anchorChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 2: selectionsChanged((*reinterpret_cast< const QtListSelectionChange(*)>(_a[1]))); break;
        case 3: setCurrentItem((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 4: setAnchorItem((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 5: setSelected((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< SelectionMode(*)>(_a[3]))); break;
        case 6: setSelected((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 7: setSelected((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 8: clearSelections(); break;
        case 9: d_func()->_q_modelDestroyed(); break;
        case 10: d_func()->_q_itemsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 11: d_func()->_q_itemsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 12: d_func()->_q_itemsMoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        default: ;
        }
        _id -= 13;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< int*>(_v) = currentItem(); break;
        case 1: *reinterpret_cast< bool*>(_v) = isAnchoredSelectionActive(); break;
        case 2: *reinterpret_cast< SelectionMode*>(_v) = anchoredSelectionMode(); break;
        }
        _id -= 3;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setCurrentItem(*reinterpret_cast< int*>(_v)); break;
        case 1: setAnchoredSelectionActive(*reinterpret_cast< bool*>(_v)); break;
        case 2: setAnchoredSelectionMode(*reinterpret_cast< SelectionMode*>(_v)); break;
        }
        _id -= 3;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 3;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 3;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}

// SIGNAL 0
void QtListSelectionManager::currentChanged(int _t1, int _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtListSelectionManager::anchorChanged(int _t1, int _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtListSelectionManager::selectionsChanged(const QtListSelectionChange & _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}
QT_END_MOC_NAMESPACE
