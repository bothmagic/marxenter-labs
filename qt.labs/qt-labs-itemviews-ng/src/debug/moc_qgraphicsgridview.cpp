/****************************************************************************
** Meta object code from reading C++ file 'qgraphicsgridview.h'
**
** Created: Thu 7. Jul 08:18:17 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../experimental/qgraphicsgridview.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qgraphicsgridview.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtGraphicsGridView[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       2,   14, // methods
       1,   24, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      37,   20,   19,   19, 0x08,
     113,   20,   19,   19, 0x08,

 // properties: name, type, flags
     144,  140, 0x02095103,

       0        // eod
};

static const char qt_meta_stringdata_QtGraphicsGridView[] = {
    "QtGraphicsGridView\0\0current,previous\0"
    "_q_selectionManagerChanged(QtListSelectionManager*,QtListSelectionMana"
    "ger*)\0"
    "_q_currentChanged(int,int)\0int\0"
    "sectionCount\0"
};

const QMetaObject QtGraphicsGridView::staticMetaObject = {
    { &QtGraphicsListView::staticMetaObject, qt_meta_stringdata_QtGraphicsGridView,
      qt_meta_data_QtGraphicsGridView, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtGraphicsGridView::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtGraphicsGridView::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtGraphicsGridView::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtGraphicsGridView))
        return static_cast<void*>(const_cast< QtGraphicsGridView*>(this));
    return QtGraphicsListView::qt_metacast(_clname);
}

int QtGraphicsGridView::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QtGraphicsListView::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: d_func()->_q_selectionManagerChanged((*reinterpret_cast< QtListSelectionManager*(*)>(_a[1])),(*reinterpret_cast< QtListSelectionManager*(*)>(_a[2]))); break;
        case 1: d_func()->_q_currentChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        default: ;
        }
        _id -= 2;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< int*>(_v) = sectionCount(); break;
        }
        _id -= 1;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setSectionCount(*reinterpret_cast< int*>(_v)); break;
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
QT_END_MOC_NAMESPACE
