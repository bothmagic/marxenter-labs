/****************************************************************************
** Meta object code from reading C++ file 'qtablemodeladaptor.h'
**
** Created: Thu 7. Jul 08:18:01 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtablemodeladaptor.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtablemodeladaptor.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTableModelAdaptor[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       9,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      21,   20,   20,   20, 0x08,
      43,   41,   20,   20, 0x08,
      86,   83,   20,   20, 0x08,
     132,   83,   20,   20, 0x08,
     169,   83,   20,   20, 0x08,
     205,   83,   20,   20, 0x08,
     245,   83,   20,   20, 0x08,
     284,   20,   20,   20, 0x08,
     303,   20,   20,   20, 0x08,

       0        // eod
};

static const char qt_meta_stringdata_QtTableModelAdaptor[] = {
    "QtTableModelAdaptor\0\0_q_modelDestroyed()\0"
    ",\0_q_dataChanged(QModelIndex,QModelIndex)\0"
    ",,\0_q_headerDataChanged(Qt::Orientation,int,int)\0"
    "_q_rowsInserted(QModelIndex,int,int)\0"
    "_q_rowsRemoved(QModelIndex,int,int)\0"
    "_q_columnsInserted(QModelIndex,int,int)\0"
    "_q_columnsRemoved(QModelIndex,int,int)\0"
    "_q_layoutChanged()\0_q_modelReset()\0"
};

const QMetaObject QtTableModelAdaptor::staticMetaObject = {
    { &QtTableModelInterface::staticMetaObject, qt_meta_stringdata_QtTableModelAdaptor,
      qt_meta_data_QtTableModelAdaptor, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTableModelAdaptor::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTableModelAdaptor::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTableModelAdaptor::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTableModelAdaptor))
        return static_cast<void*>(const_cast< QtTableModelAdaptor*>(this));
    return QtTableModelInterface::qt_metacast(_clname);
}

int QtTableModelAdaptor::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QtTableModelInterface::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: d_func()->_q_modelDestroyed(); break;
        case 1: d_func()->_q_dataChanged((*reinterpret_cast< const QModelIndex(*)>(_a[1])),(*reinterpret_cast< const QModelIndex(*)>(_a[2]))); break;
        case 2: d_func()->_q_headerDataChanged((*reinterpret_cast< Qt::Orientation(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 3: d_func()->_q_rowsInserted((*reinterpret_cast< const QModelIndex(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 4: d_func()->_q_rowsRemoved((*reinterpret_cast< const QModelIndex(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 5: d_func()->_q_columnsInserted((*reinterpret_cast< const QModelIndex(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 6: d_func()->_q_columnsRemoved((*reinterpret_cast< const QModelIndex(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 7: d_func()->_q_layoutChanged(); break;
        case 8: d_func()->_q_modelReset(); break;
        default: ;
        }
        _id -= 9;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
