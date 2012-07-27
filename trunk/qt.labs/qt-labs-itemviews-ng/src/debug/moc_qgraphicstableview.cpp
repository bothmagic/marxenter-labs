/****************************************************************************
** Meta object code from reading C++ file 'qgraphicstableview.h'
**
** Created: Thu 7. Jul 08:17:57 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qgraphicstableview.h"
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qgraphicstableview.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtGraphicsTableView[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      26,   14, // methods
       9,  144, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       6,       // signalCount

 // signals: signature, parameters, type, tag, flags
      25,   21,   20,   20, 0x05,
      53,   46,   20,   20, 0x05,
      84,   77,   20,   20, 0x05,
     115,   77,   20,   20, 0x05,
     144,   20,   20,   20, 0x05,
     187,   20,   20,   20, 0x05,

 // slots: signature, parameters, type, tag, flags
     233,  228,   20,   20, 0x0a,
     252,   21,   20,   20, 0x0a,
     269,   46,   20,   20, 0x0a,
     289,   77,   20,   20, 0x0a,
     316,   77,   20,   20, 0x0a,
     341,   20,   20,   20, 0x0a,
     356,   20,   20,   20, 0x08,
     381,   20,   20,   20, 0x08,
     401,   20,   20,   20, 0x08,
     426,   20,   20,   20, 0x08,
     455,   20,   20,   20, 0x08,
     534,  486,   20,   20, 0x08,
     592,  585,   20,   20, 0x08,
     695,  643,   20,   20, 0x08,
     740,  730,   20,   20, 0x08,
     765,  730,   20,   20, 0x08,
     803,  789,   20,   20, 0x08,
     842,  829,   20,   20, 0x08,
     870,  829,   20,   20, 0x08,
     897,  789,   20,   20, 0x08,

 // properties: name, type, flags
     949,  926, 0x0009510b,
     959,  955, 0x02095103,
     968,  955, 0x02095103,
     986,  980, (QMetaType::QReal << 24) | 0x00095103,
    1003,  980, (QMetaType::QReal << 24) | 0x00095103,
    1036, 1018, 0x0009510b,
    1068, 1050, 0x0009510b,
    1085, 1050, 0x0009510b,
    1105, 1100, 0x01095003,

       0        // eod
};

static const char qt_meta_stringdata_QtGraphicsTableView[] = {
    "QtGraphicsTableView\0\0row\0firstRowChanged(int)\0"
    "column\0firstColumnChanged(int)\0offset\0"
    "horizontalOffsetChanged(qreal)\0"
    "verticalOffsetChanged(qreal)\0"
    "horizontalHeaderChanged(QtGraphicsHeader*)\0"
    "verticalHeaderChanged(QtGraphicsHeader*)\0"
    "show\0setGridShown(bool)\0setFirstRow(int)\0"
    "setFirstColumn(int)\0setHorizontalOffset(qreal)\0"
    "setVerticalOffset(qreal)\0updateLayout()\0"
    "_q_controllerDestroyed()\0_q_modelDestroyed()\0"
    "_q_selectionsDestroyed()\0"
    "_q_verticalHeaderDestroyed()\0"
    "_q_horizontalHeaderDestroyed()\0"
    "firstRow,firstColumn,rowCount,columnCount,roles\0"
    "_q_cellsChanged(int,int,int,int,QList<QByteArray>)\0"
    "change\0_q_selectionsChanged(QList<QtTableSelectionRange>)\0"
    "currentRow,currentColumn,previousRow,previousColumn\0"
    "_q_currentChanged(int,int,int,int)\0"
    "row,count\0_q_rowsInserted(int,int)\0"
    "_q_rowsRemoved(int,int)\0from,to,count\0"
    "_q_rowsMoved(int,int,int)\0column,count\0"
    "_q_columnsInserted(int,int)\0"
    "_q_columnsRemoved(int,int)\0"
    "_q_columnsMoved(int,int,int)\0"
    "QtTableModelInterface*\0model\0int\0"
    "firstRow\0firstColumn\0qreal\0horizontalOffset\0"
    "verticalOffset\0Qt::TextElideMode\0"
    "textElideMode\0QtGraphicsHeader*\0"
    "horizontalHeader\0verticalHeader\0bool\0"
    "isGridShown\0"
};

const QMetaObject QtGraphicsTableView::staticMetaObject = {
    { &QGraphicsWidget::staticMetaObject, qt_meta_stringdata_QtGraphicsTableView,
      qt_meta_data_QtGraphicsTableView, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtGraphicsTableView::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtGraphicsTableView::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtGraphicsTableView::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtGraphicsTableView))
        return static_cast<void*>(const_cast< QtGraphicsTableView*>(this));
    return QGraphicsWidget::qt_metacast(_clname);
}

int QtGraphicsTableView::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QGraphicsWidget::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: firstRowChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 1: firstColumnChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 2: horizontalOffsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 3: verticalOffsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 4: horizontalHeaderChanged((*reinterpret_cast< QtGraphicsHeader*(*)>(_a[1]))); break;
        case 5: verticalHeaderChanged((*reinterpret_cast< QtGraphicsHeader*(*)>(_a[1]))); break;
        case 6: setGridShown((*reinterpret_cast< bool(*)>(_a[1]))); break;
        case 7: setFirstRow((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 8: setFirstColumn((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 9: setHorizontalOffset((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 10: setVerticalOffset((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 11: updateLayout(); break;
        case 12: d_func()->_q_controllerDestroyed(); break;
        case 13: d_func()->_q_modelDestroyed(); break;
        case 14: d_func()->_q_selectionsDestroyed(); break;
        case 15: d_func()->_q_verticalHeaderDestroyed(); break;
        case 16: d_func()->_q_horizontalHeaderDestroyed(); break;
        case 17: d_func()->_q_cellsChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3])),(*reinterpret_cast< int(*)>(_a[4])),(*reinterpret_cast< const QList<QByteArray>(*)>(_a[5]))); break;
        case 18: d_func()->_q_selectionsChanged((*reinterpret_cast< const QList<QtTableSelectionRange>(*)>(_a[1]))); break;
        case 19: d_func()->_q_currentChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3])),(*reinterpret_cast< int(*)>(_a[4]))); break;
        case 20: d_func()->_q_rowsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 21: d_func()->_q_rowsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 22: d_func()->_q_rowsMoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 23: d_func()->_q_columnsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 24: d_func()->_q_columnsRemoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 25: d_func()->_q_columnsMoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        default: ;
        }
        _id -= 26;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< QtTableModelInterface**>(_v) = model(); break;
        case 1: *reinterpret_cast< int*>(_v) = firstRow(); break;
        case 2: *reinterpret_cast< int*>(_v) = firstColumn(); break;
        case 3: *reinterpret_cast< qreal*>(_v) = horizontalOffset(); break;
        case 4: *reinterpret_cast< qreal*>(_v) = verticalOffset(); break;
        case 5: *reinterpret_cast< Qt::TextElideMode*>(_v) = textElideMode(); break;
        case 6: *reinterpret_cast< QtGraphicsHeader**>(_v) = horizontalHeader(); break;
        case 7: *reinterpret_cast< QtGraphicsHeader**>(_v) = verticalHeader(); break;
        case 8: *reinterpret_cast< bool*>(_v) = isGridShown(); break;
        }
        _id -= 9;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setModel(*reinterpret_cast< QtTableModelInterface**>(_v)); break;
        case 1: setFirstRow(*reinterpret_cast< int*>(_v)); break;
        case 2: setFirstColumn(*reinterpret_cast< int*>(_v)); break;
        case 3: setHorizontalOffset(*reinterpret_cast< qreal*>(_v)); break;
        case 4: setVerticalOffset(*reinterpret_cast< qreal*>(_v)); break;
        case 5: setTextElideMode(*reinterpret_cast< Qt::TextElideMode*>(_v)); break;
        case 6: setHorizontalHeader(*reinterpret_cast< QtGraphicsHeader**>(_v)); break;
        case 7: setVerticalHeader(*reinterpret_cast< QtGraphicsHeader**>(_v)); break;
        case 8: setGridShown(*reinterpret_cast< bool*>(_v)); break;
        }
        _id -= 9;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 9;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 9;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 9;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 9;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 9;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 9;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}

// SIGNAL 0
void QtGraphicsTableView::firstRowChanged(int _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtGraphicsTableView::firstColumnChanged(int _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtGraphicsTableView::horizontalOffsetChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}

// SIGNAL 3
void QtGraphicsTableView::verticalOffsetChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 3, _a);
}

// SIGNAL 4
void QtGraphicsTableView::horizontalHeaderChanged(QtGraphicsHeader * _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 4, _a);
}

// SIGNAL 5
void QtGraphicsTableView::verticalHeaderChanged(QtGraphicsHeader * _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 5, _a);
}
QT_END_MOC_NAMESPACE
