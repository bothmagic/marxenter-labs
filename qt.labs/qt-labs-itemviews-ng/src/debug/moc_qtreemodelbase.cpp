/****************************************************************************
** Meta object code from reading C++ file 'qtreemodelbase.h'
**
** Created: Thu 7. Jul 08:18:21 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtreemodelbase.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtreemodelbase.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTreeModelBase[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       3,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       3,       // signalCount

 // signals: signature, parameters, type, tag, flags
      26,   17,   16,   16, 0x05,
      76,   17,   16,   16, 0x05,
     140,  125,   16,   16, 0x05,

       0        // eod
};

static const char qt_meta_stringdata_QtTreeModelBase[] = {
    "QtTreeModelBase\0\0it,count\0"
    "itemsInserted(QtTreeModelBase::iterator_base,int)\0"
    "itemsRemoved(QtTreeModelBase::iterator_base,int)\0"
    "it,count,roles\0"
    "itemsChanged(QtTreeModelBase::iterator_base,int,QList<QByteArray>)\0"
};

const QMetaObject QtTreeModelBase::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_QtTreeModelBase,
      qt_meta_data_QtTreeModelBase, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTreeModelBase::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTreeModelBase::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTreeModelBase::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTreeModelBase))
        return static_cast<void*>(const_cast< QtTreeModelBase*>(this));
    return QObject::qt_metacast(_clname);
}

int QtTreeModelBase::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: itemsInserted((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 1: itemsRemoved((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 2: itemsChanged((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< const QList<QByteArray>(*)>(_a[3]))); break;
        default: ;
        }
        _id -= 3;
    }
    return _id;
}

// SIGNAL 0
void QtTreeModelBase::itemsInserted(const QtTreeModelBase::iterator_base & _t1, int _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtTreeModelBase::itemsRemoved(const QtTreeModelBase::iterator_base & _t1, int _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtTreeModelBase::itemsChanged(const QtTreeModelBase::iterator_base & _t1, int _t2, const QList<QByteArray> & _t3)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)), const_cast<void*>(reinterpret_cast<const void*>(&_t3)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}
QT_END_MOC_NAMESPACE
