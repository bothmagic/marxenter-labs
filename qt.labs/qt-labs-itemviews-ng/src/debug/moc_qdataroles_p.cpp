/****************************************************************************
** Meta object code from reading C++ file 'qdataroles_p.h'
**
** Created: Thu 7. Jul 08:18:22 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qdataroles_p.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qdataroles_p.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtDataRoles[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       0,    0, // methods
       0,    0, // properties
       1,   14, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // enums: name, flags, count, data
      12, 0x0,   22,   18,

 // enum data: key, value
      25, uint(QtDataRoles::DisplayRole),
      37, uint(QtDataRoles::DecorationRole),
      52, uint(QtDataRoles::EditRole),
      61, uint(QtDataRoles::ToolTipRole),
      73, uint(QtDataRoles::StatusTipRole),
      87, uint(QtDataRoles::WhatsThisRole),
     101, uint(QtDataRoles::FontRole),
     110, uint(QtDataRoles::TextAlignmentRole),
     128, uint(QtDataRoles::BackgroundColorRole),
     148, uint(QtDataRoles::BackgroundRole),
     163, uint(QtDataRoles::TextColorRole),
     177, uint(QtDataRoles::ForegroundRole),
     192, uint(QtDataRoles::CheckStateRole),
     207, uint(QtDataRoles::AccessibleTextRole),
     226, uint(QtDataRoles::AccessibleDescriptionRole),
     252, uint(QtDataRoles::SizeHintRole),
     265, uint(QtDataRoles::DisplayPropertyRole),
     285, uint(QtDataRoles::DecorationPropertyRole),
     308, uint(QtDataRoles::ToolTipPropertyRole),
     328, uint(QtDataRoles::StatusTipPropertyRole),
     350, uint(QtDataRoles::WhatsThisPropertyRole),
     372, uint(QtDataRoles::UserRole),

       0        // eod
};

static const char qt_meta_stringdata_QtDataRoles[] = {
    "QtDataRoles\0ItemDataRole\0DisplayRole\0"
    "DecorationRole\0EditRole\0ToolTipRole\0"
    "StatusTipRole\0WhatsThisRole\0FontRole\0"
    "TextAlignmentRole\0BackgroundColorRole\0"
    "BackgroundRole\0TextColorRole\0"
    "ForegroundRole\0CheckStateRole\0"
    "AccessibleTextRole\0AccessibleDescriptionRole\0"
    "SizeHintRole\0DisplayPropertyRole\0"
    "DecorationPropertyRole\0ToolTipPropertyRole\0"
    "StatusTipPropertyRole\0WhatsThisPropertyRole\0"
    "UserRole\0"
};

const QMetaObject QtDataRoles::staticMetaObject = {
    { &QObject::staticMetaObject, qt_meta_stringdata_QtDataRoles,
      qt_meta_data_QtDataRoles, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtDataRoles::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtDataRoles::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtDataRoles::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtDataRoles))
        return static_cast<void*>(const_cast< QtDataRoles*>(this));
    return QObject::qt_metacast(_clname);
}

int QtDataRoles::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QObject::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    return _id;
}
QT_END_MOC_NAMESPACE
