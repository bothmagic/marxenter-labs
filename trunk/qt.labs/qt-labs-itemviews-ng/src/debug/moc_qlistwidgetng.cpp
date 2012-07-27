/****************************************************************************
** Meta object code from reading C++ file 'qlistwidgetng.h'
**
** Created: Thu 7. Jul 08:17:53 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qlistwidgetng.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qlistwidgetng.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtListWidgetNG[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      11,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      28,   16,   15,   15, 0x08,
      54,   16,   15,   15, 0x08,
      93,   79,   15,   15, 0x08,
     137,  120,   15,   15, 0x08,
     173,   15,   15,   15, 0x08,
     187,   15,   15,   15, 0x08,
     201,   15,   15,   15, 0x08,
     240,  223,   15,   15, 0x08,
     301,  223,   15,   15, 0x08,
     377,  223,   15,   15, 0x08,
     439,  433,   15,   15, 0x08,

       0        // eod
};

static const char qt_meta_stringdata_QtListWidgetNG[] = {
    "QtListWidgetNG\0\0index,count\0"
    "_q_itemsInserted(int,int)\0"
    "_q_itemsRemoved(int,int)\0from,to,count\0"
    "_q_itemsMoved(int,int,int)\0from,count,roles\0"
    "_q_itemsChanged(int,int,QList<int>)\0"
    "_q_showView()\0_q_hideView()\0"
    "_q_updateGeometries()\0current,previous\0"
    "_q_modelChanged(QtListModelInterface*,QtListModelInterface*)\0"
    "_q_selectionManagerChanged(QtListSelectionManager*,QtListSelectionMana"
    "ger*)\0"
    "_q_viewChanged(QtGraphicsListView*,QtGraphicsListView*)\0"
    "index\0_q_firstIndexChanged(int)\0"
};

const QMetaObject QtListWidgetNG::staticMetaObject = {
    { &QAbstractScrollArea::staticMetaObject, qt_meta_stringdata_QtListWidgetNG,
      qt_meta_data_QtListWidgetNG, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtListWidgetNG::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtListWidgetNG::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtListWidgetNG::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtListWidgetNG))
        return static_cast<void*>(const_cast< QtListWidgetNG*>(this));
    return QAbstractScrollArea::qt_metacast(_clname);
}

int QtListWidgetNG::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QAbstractScrollArea::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: d_func()->_q_itemsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 1: d_func()->_q_itemsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 2: d_func()->_q_itemsMoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 3: d_func()->_q_itemsChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< const QList<int>(*)>(_a[3]))); break;
        case 4: d_func()->_q_showView(); break;
        case 5: d_func()->_q_hideView(); break;
        case 6: d_func()->_q_updateGeometries(); break;
        case 7: d_func()->_q_modelChanged((*reinterpret_cast< QtListModelInterface*(*)>(_a[1])),(*reinterpret_cast< QtListModelInterface*(*)>(_a[2]))); break;
        case 8: d_func()->_q_selectionManagerChanged((*reinterpret_cast< QtListSelectionManager*(*)>(_a[1])),(*reinterpret_cast< QtListSelectionManager*(*)>(_a[2]))); break;
        case 9: d_func()->_q_viewChanged((*reinterpret_cast< QtGraphicsListView*(*)>(_a[1])),(*reinterpret_cast< QtGraphicsListView*(*)>(_a[2]))); break;
        case 10: d_func()->_q_firstIndexChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        default: ;
        }
        _id -= 11;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
