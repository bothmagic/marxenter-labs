/****************************************************************************
** Meta object code from reading C++ file 'qkineticlistcontroller.h'
**
** Created: Thu 7. Jul 08:18:23 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../experimental/qkineticlistcontroller.h"
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qkineticlistcontroller.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtKineticListController[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       1,   14, // methods
       4,   19, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      25,   24,   24,   24, 0x0a,

 // properties: name, type, flags
      38,   32, (QMetaType::QReal << 24) | 0x00095103,
      53,   32, (QMetaType::QReal << 24) | 0x00095103,
      80,   75, 0x01095103,
      97,   75, 0x01095103,

       0        // eod
};

static const char qt_meta_stringdata_QtKineticListController[] = {
    "QtKineticListController\0\0stop()\0qreal\0"
    "scrollVelocity\0maximumScrollVelocity\0"
    "bool\0overshootEnabled\0centerOnItemEnabled\0"
};

const QMetaObject QtKineticListController::staticMetaObject = {
    { &QtListController::staticMetaObject, qt_meta_stringdata_QtKineticListController,
      qt_meta_data_QtKineticListController, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtKineticListController::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtKineticListController::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtKineticListController::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtKineticListController))
        return static_cast<void*>(const_cast< QtKineticListController*>(this));
    return QtListController::qt_metacast(_clname);
}

int QtKineticListController::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QtListController::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: stop(); break;
        default: ;
        }
        _id -= 1;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< qreal*>(_v) = scrollVelocity(); break;
        case 1: *reinterpret_cast< qreal*>(_v) = maximumScrollVelocity(); break;
        case 2: *reinterpret_cast< bool*>(_v) = isOvershootEnabled(); break;
        case 3: *reinterpret_cast< bool*>(_v) = isCenterOnItemEnabled(); break;
        }
        _id -= 4;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setScrollVelocity(*reinterpret_cast< qreal*>(_v)); break;
        case 1: setMaximumScrollVelocity(*reinterpret_cast< qreal*>(_v)); break;
        case 2: setOvershootEnabled(*reinterpret_cast< bool*>(_v)); break;
        case 3: setCenterOnItemEnabled(*reinterpret_cast< bool*>(_v)); break;
        }
        _id -= 4;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 4;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 4;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}
QT_END_MOC_NAMESPACE
