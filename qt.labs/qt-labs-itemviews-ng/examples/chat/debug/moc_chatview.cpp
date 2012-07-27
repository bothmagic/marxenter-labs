/****************************************************************************
** Meta object code from reading C++ file 'chatview.h'
**
** Created: Thu 7. Jul 08:23:36 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../chatview.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'chatview.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_ChatViewItem[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       0,    0, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

       0        // eod
};

static const char qt_meta_stringdata_ChatViewItem[] = {
    "ChatViewItem\0"
};

const QMetaObject ChatViewItem::staticMetaObject = {
    { &QtGraphicsListViewItem::staticMetaObject, qt_meta_stringdata_ChatViewItem,
      qt_meta_data_ChatViewItem, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &ChatViewItem::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *ChatViewItem::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *ChatViewItem::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_ChatViewItem))
        return static_cast<void*>(const_cast< ChatViewItem*>(this));
    return QtGraphicsListViewItem::qt_metacast(_clname);
}

int ChatViewItem::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QtGraphicsListViewItem::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    return _id;
}
static const uint qt_meta_data_ChatView[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
       2,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      22,   10,    9,    9, 0x0a,
      63,   45,    9,    9, 0x0a,

       0        // eod
};

static const char qt_meta_stringdata_ChatView[] = {
    "ChatView\0\0index,count\0itemsInserted(int,int)\0"
    "index,count,roles\0"
    "itemsChanged(int,int,QList<QByteArray>)\0"
};

const QMetaObject ChatView::staticMetaObject = {
    { &QtGraphicsListView::staticMetaObject, qt_meta_stringdata_ChatView,
      qt_meta_data_ChatView, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &ChatView::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *ChatView::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *ChatView::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_ChatView))
        return static_cast<void*>(const_cast< ChatView*>(this));
    return QtGraphicsListView::qt_metacast(_clname);
}

int ChatView::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QtGraphicsListView::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: itemsInserted((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2]))); break;
        case 1: itemsChanged((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])),(*reinterpret_cast< const QList<QByteArray>(*)>(_a[3]))); break;
        default: ;
        }
        _id -= 2;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
