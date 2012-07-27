/****************************************************************************
** Meta object code from reading C++ file 'qtreeselectionmanager.h'
**
** Created: Thu 7. Jul 08:18:13 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtreeselectionmanager.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtreeselectionmanager.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTreeSelectionManager[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      12,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       3,       // signalCount

 // signals: signature, parameters, type, tag, flags
      41,   24,   23,   23, 0x05,
     119,   24,   23,   23, 0x05,
     206,  196,   23,   23, 0x05,

 // slots: signature, parameters, type, tag, flags
     244,  241,   23,   23, 0x0a,
     280,  241,   23,   23, 0x0a,
     320,  315,   23,   23, 0x0a,
     373,  358,   23,   23, 0x0a,
     416,  196,   23,   23, 0x2a,
     445,  196,   23,   23, 0x0a,
     481,   23,   23,   23, 0x0a,
     499,   23,   23,   23, 0x08,
     528,  519,   23,   23, 0x08,

       0        // eod
};

static const char qt_meta_stringdata_QtTreeSelectionManager[] = {
    "QtTreeSelectionManager\0\0current,previous\0"
    "currentChanged(QtTreeModelBase::iterator_base,QtTreeModelBase::iterato"
    "r_base)\0"
    "anchorChanged(QtTreeModelBase::iterator_base,QtTreeModelBase::iterator"
    "_base)\0"
    "selection\0selectionsChanged(QtTreeSelection)\0"
    "it\0setCurrentItem(QtTreeModelIterator)\0"
    "setAnchorItem(QtTreeModelIterator)\0"
    "mode\0setAnchorSelectionMode(SelectionMode)\0"
    "selection,mode\0"
    "setSelected(QtTreeSelection,SelectionMode)\0"
    "setSelected(QtTreeSelection)\0"
    "setAnchorSelection(QtTreeSelection)\0"
    "clearSelections()\0_q_modelDestroyed()\0"
    "it,count\0_q_itemsRemoved(QtTreeModelBase::iterator_base,int)\0"
};

const QMetaObject QtTreeSelectionManager::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_QtTreeSelectionManager,
      qt_meta_data_QtTreeSelectionManager, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTreeSelectionManager::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTreeSelectionManager::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTreeSelectionManager::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTreeSelectionManager))
        return static_cast<void*>(const_cast< QtTreeSelectionManager*>(this));
    return QObject::qt_metacast(_clname);
}

int QtTreeSelectionManager::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: currentChanged((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[2]))); break;
        case 1: anchorChanged((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[2]))); break;
        case 2: selectionsChanged((*reinterpret_cast< const QtTreeSelection(*)>(_a[1]))); break;
        case 3: setCurrentItem((*reinterpret_cast< const QtTreeModelIterator(*)>(_a[1]))); break;
        case 4: setAnchorItem((*reinterpret_cast< const QtTreeModelIterator(*)>(_a[1]))); break;
        case 5: setAnchorSelectionMode((*reinterpret_cast< SelectionMode(*)>(_a[1]))); break;
        case 6: setSelected((*reinterpret_cast< const QtTreeSelection(*)>(_a[1])),(*reinterpret_cast< SelectionMode(*)>(_a[2]))); break;
        case 7: setSelected((*reinterpret_cast< const QtTreeSelection(*)>(_a[1]))); break;
        case 8: setAnchorSelection((*reinterpret_cast< const QtTreeSelection(*)>(_a[1]))); break;
        case 9: clearSelections(); break;
        case 10: d_func()->_q_modelDestroyed(); break;
        case 11: d_func()->_q_itemsRemoved((*reinterpret_cast< const QtTreeModelBase::iterator_base(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        default: ;
        }
        _id -= 12;
    }
    return _id;
}

// SIGNAL 0
void QtTreeSelectionManager::currentChanged(const QtTreeModelBase::iterator_base & _t1, const QtTreeModelBase::iterator_base & _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtTreeSelectionManager::anchorChanged(const QtTreeModelBase::iterator_base & _t1, const QtTreeModelBase::iterator_base & _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtTreeSelectionManager::selectionsChanged(const QtTreeSelection & _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}
QT_END_MOC_NAMESPACE
