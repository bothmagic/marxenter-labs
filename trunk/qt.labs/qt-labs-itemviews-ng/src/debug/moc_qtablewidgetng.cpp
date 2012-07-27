/****************************************************************************
** Meta object code from reading C++ file 'qtablewidgetng.h'
**
** Created: Thu 7. Jul 08:18:04 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtablewidgetng.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtablewidgetng.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTableWidgetNG[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      10,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      24,   17,   16,   16, 0x08,
      53,   43,   16,   16, 0x08,
      78,   43,   16,   16, 0x08,
     116,  102,   16,   16, 0x08,
     155,  142,   16,   16, 0x08,
     183,  142,   16,   16, 0x08,
     210,  102,   16,   16, 0x08,
     239,   16,   16,   16, 0x08,
     253,   16,   16,   16, 0x08,
     267,   16,   16,   16, 0x08,

       0        // eod
};

static const char qt_meta_stringdata_QtTableWidgetNG[] = {
    "QtTableWidgetNG\0\0region\0_q_update(QRegion)\0"
    "row,count\0_q_rowsInserted(int,int)\0"
    "_q_rowsRemoved(int,int)\0from,to,count\0"
    "_q_rowsMoved(int,int,int)\0column,count\0"
    "_q_columnsInserted(int,int)\0"
    "_q_columnsRemoved(int,int)\0"
    "_q_columnsMoved(int,int,int)\0_q_showView()\0"
    "_q_hideView()\0_q_updateGeometries()\0"
};

const QMetaObject QtTableWidgetNG::staticMetaObject = {
    { &QGraphicsView::staticMetaObject, qt_meta_stringdata_QtTableWidgetNG,
      qt_meta_data_QtTableWidgetNG, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTableWidgetNG::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTableWidgetNG::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTableWidgetNG::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTableWidgetNG))
        return static_cast<void*>(const_cast< QtTableWidgetNG*>(this));
    return QGraphicsView::qt_metacast(_clname);
}

int QtTableWidgetNG::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QGraphicsView::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: d_func()->_q_update((*reinterpret_cast< const QRegion(*)>(_a[1]))); break;
        case 1: d_func()->_q_rowsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 2: d_func()->_q_rowsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 3: d_func()->_q_rowsMoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 4: d_func()->_q_columnsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 5: d_func()->_q_columnsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 6: d_func()->_q_columnsMoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 7: d_func()->_q_showView(); break;
        case 8: d_func()->_q_hideView(); break;
        case 9: d_func()->_q_updateGeometries(); break;
        default: ;
        }
        _id -= 10;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
