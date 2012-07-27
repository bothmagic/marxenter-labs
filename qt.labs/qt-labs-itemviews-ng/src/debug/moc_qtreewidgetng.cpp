/****************************************************************************
** Meta object code from reading C++ file 'qtreewidgetng.h'
**
** Created: Thu 7. Jul 08:18:14 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtreewidgetng.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtreewidgetng.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTreeWidgetNG[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      13,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      25,   16,   15,   15, 0x08,
      67,   16,   15,   15, 0x08,
     110,   16,   15,   15, 0x08,
     153,   16,   15,   15, 0x08,
     209,  195,   15,   15, 0x08,
     270,   15,   15,   15, 0x08,
     284,   15,   15,   15, 0x08,
     298,   15,   15,   15, 0x08,
     337,  320,   15,   15, 0x08,
     388,  320,   15,   15, 0x08,
     464,  320,   15,   15, 0x08,
     526,  520,   15,   15, 0x08,
     560,  552,   15,   15, 0x08,

       0        // eod
};

static const char qt_meta_stringdata_QtTreeWidgetNG[] = {
    "QtTreeWidgetNG\0\0it,count\0"
    "_q_itemExpanded(QtTreeModelIterator&,int)\0"
    "_q_itemCollapsed(QtTreeModelIterator&,int)\0"
    "_q_itemsInserted(QtTreeModelIterator&,int)\0"
    "_q_itemsRemoved(QtTreeModelIterator&,int)\0"
    "from,to,count\0"
    "_q_itemsMoved(QtTreeModelIterator&,QtTreeModelIterator&,int)\0"
    "_q_showView()\0_q_hideView()\0"
    "_q_updateGeometries()\0current,previous\0"
    "_q_modelChanged(QtTreeModelBase*,QtTreeModelBase*)\0"
    "_q_selectionManagerChanged(QtTreeSelectionManager*,QtTreeSelectionMana"
    "ger*)\0"
    "_q_viewChanged(QtGraphicsTreeView*,QtGraphicsTreeView*)\0"
    "index\0_q_firstIndexChanged(int)\0section\0"
    "_q_firstSectionChanged(int)\0"
};

const QMetaObject QtTreeWidgetNG::staticMetaObject = {
    { &QGraphicsView::staticMetaObject, qt_meta_stringdata_QtTreeWidgetNG,
      qt_meta_data_QtTreeWidgetNG, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTreeWidgetNG::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTreeWidgetNG::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTreeWidgetNG::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTreeWidgetNG))
        return static_cast<void*>(const_cast< QtTreeWidgetNG*>(this));
    return QGraphicsView::qt_metacast(_clname);
}

int QtTreeWidgetNG::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QGraphicsView::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: d_func()->_q_itemExpanded((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 1: d_func()->_q_itemCollapsed((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 2: d_func()->_q_itemsInserted((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 3: d_func()->_q_itemsRemoved((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 4: d_func()->_q_itemsMoved((*reinterpret_cast< QtTreeModelIterator(*)>(_a[1])),(*reinterpret_cast< QtTreeModelIterator(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 5: d_func()->_q_showView(); break;
        case 6: d_func()->_q_hideView(); break;
        case 7: d_func()->_q_updateGeometries(); break;
        case 8: d_func()->_q_modelChanged((*reinterpret_cast< QtTreeModelBase*(*)>(_a[1])),(*reinterpret_cast< QtTreeModelBase*(*)>(_a[2]))); break;
        case 9: d_func()->_q_selectionManagerChanged((*reinterpret_cast< QtTreeSelectionManager*(*)>(_a[1])),(*reinterpret_cast< QtTreeSelectionManager*(*)>(_a[2]))); break;
        case 10: d_func()->_q_viewChanged((*reinterpret_cast< QtGraphicsTreeView*(*)>(_a[1])),(*reinterpret_cast< QtGraphicsTreeView*(*)>(_a[2]))); break;
        case 11: d_func()->_q_firstIndexChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 12: d_func()->_q_firstSectionChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        default: ;
        }
        _id -= 13;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
