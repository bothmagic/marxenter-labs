/****************************************************************************
** Meta object code from reading C++ file 'qgraphicsheader.h'
**
** Created: Thu 7. Jul 08:17:42 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../qgraphicsheader.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qgraphicsheader.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QtGraphicsHeader[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      33,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
      14,       // signalCount

 // signals: signature, parameters, type, tag, flags
      61,   18,   17,   17, 0x05,
     116,   87,   17,   17, 0x05,
     171,  148,   17,   17, 0x05,
     213,  148,   17,   17, 0x05,
     256,  148,   17,   17, 0x05,
     298,  148,   17,   17, 0x05,
     340,  148,   17,   17, 0x05,
     388,  148,   17,   17, 0x05,
     453,  435,   17,   17, 0x05,
     495,  482,   17,   17, 0x05,
     546,  527,   17,   17, 0x05,
     610,  591,   17,   17, 0x05,
     658,  646,   17,   17, 0x05,
     690,  683,   17,   17, 0x05,

 // slots: signature, parameters, type, tag, flags
     711,  683,   17,   17, 0x0a,
     734,  728,   17,   17, 0x0a,
     755,  646,   17,   17, 0x0a,
     794,  776,   17,   17, 0x0a,
     840,  820,   17,   17, 0x0a,
     867,  482,   17,   17, 0x2a,
     910,  889,   17,   17, 0x0a,
     938,  482,   17,   17, 0x2a,
     984,  961,   17,   17, 0x0a,
    1014,  482,   17,   17, 0x2a,
    1061, 1039,   17,   17, 0x0a,
    1090,  482,   17,   17, 0x2a,
    1133, 1114,   17,   17, 0x0a,
    1157, 1114,   17,   17, 0x0a,
    1181, 1114,   17,   17, 0x0a,
    1235, 1205,   17,   17, 0x0a,
    1256,  527,   17,   17, 0x0a,
    1315, 1297,   17,   17, 0x0a,
    1347,  482,   17,   17, 0x2a,

       0        // eod
};

static const char qt_meta_stringdata_QtGraphicsHeader[] = {
    "QtGraphicsHeader\0\0"
    "logicalIndex,visualIndexTo,visualIndexFrom\0"
    "sectionMoved(int,int,int)\0"
    "logicalIndex,newSize,oldSize\0"
    "sectionResized(int,qreal,qreal)\0"
    "logicalIndex,modifiers\0"
    "sectionPressed(int,Qt::KeyboardModifiers)\0"
    "sectionReleased(int,Qt::KeyboardModifiers)\0"
    "sectionClicked(int,Qt::KeyboardModifiers)\0"
    "sectionEntered(int,Qt::KeyboardModifiers)\0"
    "sectionDoubleClicked(int,Qt::KeyboardModifiers)\0"
    "sectionDragSelected(int,Qt::KeyboardModifiers)\0"
    "newCount,oldCount\0sectionCountChanged(int,int)\0"
    "logicalIndex\0sectionHandleDoubleClicked(int)\0"
    "logicalIndex,order\0"
    "sortIndicatorOrderChanged(int,Qt::SortOrder)\0"
    "logicalIndex,shown\0"
    "sortIndicatorShownChanged(int,bool)\0"
    "visualIndex\0firstSectionChanged(int)\0"
    "offset\0offsetChanged(qreal)\0"
    "setOffset(qreal)\0count\0setSectionCount(int)\0"
    "setFirstSection(int)\0logicalIndex,size\0"
    "setSectionSize(int,qreal)\0logicalIndex,hidden\0"
    "setSectionHidden(int,bool)\0"
    "setSectionHidden(int)\0logicalIndex,movable\0"
    "setSectionMovable(int,bool)\0"
    "setSectionMovable(int)\0logicalIndex,clickable\0"
    "setSectionClickable(int,bool)\0"
    "setSectionClickable(int)\0logicalIndex,selected\0"
    "setSectionSelected(int,bool)\0"
    "setSectionSelected(int)\0logicalIndex,count\0"
    "updateSections(int,int)\0insertSections(int,int)\0"
    "removeSections(int,int)\0"
    "visualIndexFrom,visualIndexTo\0"
    "moveSection(int,int)\0"
    "setSortIndicatorOrder(int,Qt::SortOrder)\0"
    "logicalIndex,show\0setSortIndicatorShown(int,bool)\0"
    "setSortIndicatorShown(int)\0"
};

const QMetaObject QtGraphicsHeader::staticMetaObject = {
    { &QGraphicsWidget::staticMetaObject, qt_meta_stringdata_QtGraphicsHeader,
      qt_meta_data_QtGraphicsHeader, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QtGraphicsHeader::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QtGraphicsHeader::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QtGraphicsHeader::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QtGraphicsHeader))
        return static_cast<void*>(const_cast< QtGraphicsHeader*>(this));
    return QGraphicsWidget::qt_metacast(_clname);
}

int QtGraphicsHeader::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QGraphicsWidget::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: sectionMoved((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< int(*)>(_a[3]))); break;
        case 1: sectionResized((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< qreal(*)>(_a[2])),(*reinterpret_cast< qreal(*)>(_a[3]))); break;
        case 2: sectionPressed((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 3: sectionReleased((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 4: sectionClicked((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 5: sectionEntered((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 6: sectionDoubleClicked((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 7: sectionDragSelected((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::KeyboardModifiers(*)>(_a[2]))); break;
        case 8: sectionCountChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 9: sectionHandleDoubleClicked((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 10: sortIndicatorOrderChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::SortOrder(*)>(_a[2]))); break;
        case 11: sortIndicatorShownChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 12: firstSectionChanged((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 13: offsetChanged((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 14: setOffset((*reinterpret_cast< qreal(*)>(_a[1]))); break;
        case 15: setSectionCount((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 16: setFirstSection((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 17: setSectionSize((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< qreal(*)>(_a[2]))); break;
        case 18: setSectionHidden((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 19: setSectionHidden((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 20: setSectionMovable((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 21: setSectionMovable((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 22: setSectionClickable((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 23: setSectionClickable((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 24: setSectionSelected((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 25: setSectionSelected((*reinterpret_cast< int(*)>(_a[1]))); break;
        case 26: updateSections((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 27: insertSections((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 28: removeSections((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 29: moveSection((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 30: setSortIndicatorOrder((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< Qt::SortOrder(*)>(_a[2]))); break;
        case 31: setSortIndicatorShown((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< bool(*)>(_a[2]))); break;
        case 32: setSortIndicatorShown((*reinterpret_cast< int(*)>(_a[1]))); break;
        default: ;
        }
        _id -= 33;
    }
    return _id;
}

// SIGNAL 0
void QtGraphicsHeader::sectionMoved(int _t1, int _t2, int _t3)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)), const_cast<void*>(reinterpret_cast<const void*>(&_t3)) };
    QMetaObject::activate(this, &staticMetaObject, 0, _a);
}

// SIGNAL 1
void QtGraphicsHeader::sectionResized(int _t1, qreal _t2, qreal _t3)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)), const_cast<void*>(reinterpret_cast<const void*>(&_t3)) };
    QMetaObject::activate(this, &staticMetaObject, 1, _a);
}

// SIGNAL 2
void QtGraphicsHeader::sectionPressed(int _t1, Qt::KeyboardModifiers _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 2, _a);
}

// SIGNAL 3
void QtGraphicsHeader::sectionReleased(int _t1, Qt::KeyboardModifiers _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 3, _a);
}

// SIGNAL 4
void QtGraphicsHeader::sectionClicked(int _t1, Qt::KeyboardModifiers _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 4, _a);
}

// SIGNAL 5
void QtGraphicsHeader::sectionEntered(int _t1, Qt::KeyboardModifiers _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 5, _a);
}

// SIGNAL 6
void QtGraphicsHeader::sectionDoubleClicked(int _t1, Qt::KeyboardModifiers _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 6, _a);
}

// SIGNAL 7
void QtGraphicsHeader::sectionDragSelected(int _t1, Qt::KeyboardModifiers _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 7, _a);
}

// SIGNAL 8
void QtGraphicsHeader::sectionCountChanged(int _t1, int _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 8, _a);
}

// SIGNAL 9
void QtGraphicsHeader::sectionHandleDoubleClicked(int _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 9, _a);
}

// SIGNAL 10
void QtGraphicsHeader::sortIndicatorOrderChanged(int _t1, Qt::SortOrder _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 10, _a);
}

// SIGNAL 11
void QtGraphicsHeader::sortIndicatorShownChanged(int _t1, bool _t2)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)), const_cast<void*>(reinterpret_cast<const void*>(&_t2)) };
    QMetaObject::activate(this, &staticMetaObject, 11, _a);
}

// SIGNAL 12
void QtGraphicsHeader::firstSectionChanged(int _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 12, _a);
}

// SIGNAL 13
void QtGraphicsHeader::offsetChanged(qreal _t1)
{
    void *_a[] = { 0, const_cast<void*>(reinterpret_cast<const void*>(&_t1)) };
    QMetaObject::activate(this, &staticMetaObject, 13, _a);
}
QT_END_MOC_NAMESPACE
