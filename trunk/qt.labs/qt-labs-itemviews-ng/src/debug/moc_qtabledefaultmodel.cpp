/****************************************************************************
** Meta object code from reading C++ file 'qtabledefaultmodel.h'
**
** Created: Thu 7. Jul 08:17:59 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qtabledefaultmodel.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qtabledefaultmodel.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtTableDefaultModel[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       8,   14, // methods
       2,   54, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      25,   21,   20,   20, 0x0a,
      40,   21,   20,   20, 0x0a,
      69,   55,   20,   20, 0x0a,
      93,   86,   20,   20, 0x0a,
     111,   86,   20,   20, 0x0a,
     149,  129,   20,   20, 0x0a,
     169,   20,   20,   20, 0x0a,
     185,   20,   20,   20, 0x0a,

 // properties: name, type, flags
     197,  193, 0x02095103,
     206,  193, 0x02095103,

       0        // eod
};

static const char qt_meta_stringdata_QtTableDefaultModel[] = {
    "QtTableDefaultModel\0\0row\0insertRow(int)\0"
    "removeRow(int)\0fromRow,toRow\0"
    "moveRow(int,int)\0column\0insertColumn(int)\0"
    "removeColumn(int)\0fromColumn,toColumn\0"
    "moveColumn(int,int)\0clearContents()\0"
    "clear()\0int\0rowCount\0columnCount\0"
};

const QMetaObject QtTableDefaultModel::staticMetaObject = {
    { &QtTableModelInterface::staticMetaObject, qt_meta_stringdata_QtTableDefaultModel,
      qt_meta_data_QtTableDefaultModel, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtTableDefaultModel::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtTableDefaultModel::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtTableDefaultModel::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtTableDefaultModel))
        return static_cast<void*>(const_cast< QtTableDefaultModel*>(this));
    return QtTableModelInterface::qt_metacast(_clname);
}

int QtTableDefaultModel::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QtTableModelInterface::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: insertRow((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 1: removeRow((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 2: moveRow((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 3: insertColumn((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 4: removeColumn((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 5: moveColumn((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 6: clearContents(); break;
        case 7: clear(); break;
        default: ;
        }
        _id -= 8;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< int*>(_v) = rowCount(); break;
        case 1: *reinterpret_cast< int*>(_v) = columnCount(); break;
        }
        _id -= 2;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setRowCount(*reinterpret_cast< int*>(_v)); break;
        case 1: setColumnCount(*reinterpret_cast< int*>(_v)); break;
        }
        _id -= 2;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 2;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 2;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 2;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 2;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 2;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 2;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}
QT_END_MOC_NAMESPACE
