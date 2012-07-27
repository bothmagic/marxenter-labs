/****************************************************************************
** Meta object code from reading C++ file 'qlistfromtreeadaptor.h'
**
** Created: Thu 7. Jul 08:17:51 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qlistfromtreeadaptor.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qlistfromtreeadaptor.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtListFromTreeAdaptor[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       4,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      23,   22,   22,   22, 0x08,
      45,   43,   22,   22, 0x08,
      98,   43,   22,   22, 0x08,
     153,  150,   22,   22, 0x08,

       0        // eod
};

static const char qt_meta_stringdata_QtListFromTreeAdaptor[] = {
    "QtListFromTreeAdaptor\0\0_q_modelDestroyed()\0"
    ",\0_q_itemsInserted(QtTreeModelBase::iterator_base,int)\0"
    "_q_itemsRemoved(QtTreeModelBase::iterator_base,int)\0"
    ",,\0"
    "_q_itemsChanged(QtTreeModelBase::iterator_base,int,QList<QByteArray>)\0"
};

const QMetaObject QtListFromTreeAdaptor::staticMetaObject = {
    { &QtListModelInterface::staticMetaObject, qt_meta_stringdata_QtListFromTreeAdaptor,
      qt_meta_data_QtListFromTreeAdaptor, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtListFromTreeAdaptor::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtListFromTreeAdaptor::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtListFromTreeAdaptor::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtListFromTreeAdaptor))
        return static_cast<void*>(const_cast< QtListFromTreeAdaptor*>(this));
    return QtListModelInterface::qt_metacast(_clname);
}

int QtListFromTreeAdaptor::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QtListModelInterface::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: d_func()->_q_modelDestroyed(); break;
        case 1: d_func()->_q_itemsInserted((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 2: d_func()->_q_itemsRemoved((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 3: d_func()->_q_itemsChanged((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< const QList<QByteArray>(*)>(_a[3]))); break;
        default: ;
        }
        _id -= 4;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
