/****************************************************************************
** Meta object code from reading C++ file 'qstyleitem.h'
**
** Created: Thu 5. May 20:59:33 2011
**      by: The Qt Meta Object Compiler version 62 (Qt 4.7.3)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../../../../qt-components-desktop/components/styleitem/qstyleitem.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'qstyleitem.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 62
#error "This file was generated using the moc from 4.7.3. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_QStyleItem[] = {

 // content:
       5,       // revision
       0,       // classname
       0,    0, // classinfo
      28,   14, // methods
      22,  154, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
      20,       // signalCount

 // signals: signature, parameters, type, tag, flags
      12,   11,   11,   11, 0x05,
      33,   11,   11,   11, 0x05,
      47,   11,   11,   11, 0x05,
      63,   11,   11,   11, 0x05,
      79,   11,   11,   11, 0x05,
      95,   11,   11,   11, 0x05,
     113,   11,   11,   11, 0x05,
     128,   11,   11,   11, 0x05,
     140,   11,   11,   11, 0x05,
     155,   11,   11,   11, 0x05,
     175,   11,   11,   11, 0x05,
     192,   11,   11,   11, 0x05,
     209,   11,   11,   11, 0x05,
     224,   11,   11,   11, 0x05,
     247,   11,   11,   11, 0x05,
     261,   11,   11,   11, 0x05,
     276,   11,   11,   11, 0x05,
     298,   11,   11,   11, 0x05,
     312,   11,   11,   11, 0x05,
     328,   11,   11,   11, 0x05,

 // slots: signature, parameters, type, tag, flags
     352,   11,  348,   11, 0x0a,
     382,   11,  373,   11, 0x0a,
     420,  407,  401,   11, 0x0a,
     446,   11,   11,   11, 0x0a,
     471,  467,  459,   11, 0x0a,
     511,  494,  488,   11, 0x0a,
     539,  535,   11,   11, 0x0a,
     560,   11,  348,   11, 0x0a,

 // properties: name, type, flags
     584,  579, 0x01495103,
     591,  579, 0x01495103,
     598,  579, 0x01495103,
     605,  579, 0x01495103,
     614,  579, 0x01495103,
     620,  579, 0x01495103,
     623,  579, 0x01495103,
     629,  579, 0x01495103,
     640,  459, 0x0a495103,
     652,  459, 0x0a495103,
     657,  459, 0x0a495103,
     671,  459, 0x0a495103,
     676,  459, 0x0a495001,
     682,  459, 0x0a495103,
     687,  459, 0x0a495103,
     694,  348, 0x02495103,
     702,  348, 0x02495103,
     710,  348, 0x02495103,
     716,  348, 0x02495103,
     729,  459, 0x0a095001,
     747,  740, 0x06095001,
     761,  348, 0x02495001,

 // properties: notify_signal_id
       2,
       3,
       4,
       5,
       6,
       7,
       8,
       9,
       0,
       1,
      13,
      14,
      15,
      17,
      18,
      10,
      11,
      12,
      16,
       0,
       0,
      19,

       0        // eod
};

static const char qt_meta_stringdata_QStyleItem[] = {
    "QStyleItem\0\0elementTypeChanged()\0"
    "textChanged()\0sunkenChanged()\0"
    "raisedChanged()\0activeChanged()\0"
    "selectedChanged()\0focusChanged()\0"
    "onChanged()\0hoverChanged()\0"
    "horizontalChanged()\0minimumChanged()\0"
    "maximumChanged()\0valueChanged()\0"
    "activeControlChanged()\0infoChanged()\0"
    "styleChanged()\0paintMarginsChanged()\0"
    "hintChanged()\0cursorChanged()\0"
    "fontHeightChanged()\0int\0pixelMetric(QString)\0"
    "QVariant\0styleHint(QString)\0QSize\0"
    "width,height\0sizeFromContents(int,int)\0"
    "updateItem()\0QString\0x,y\0hitTest(int,int)\0"
    "QRect\0subcontrolString\0subControlRect(QString)\0"
    "str\0showToolTip(QString)\0textWidth(QString)\0"
    "bool\0sunken\0raised\0active\0selected\0"
    "focus\0on\0hover\0horizontal\0elementType\0"
    "text\0activeControl\0info\0style\0hint\0"
    "cursor\0minimum\0maximum\0value\0paintMargins\0"
    "fontFamily\0double\0fontPointSize\0"
    "fontHeight\0"
};

const QMetaObject QStyleItem::staticMetaObject = {
    { &QDeclarativeItem::staticMetaObject, qt_meta_stringdata_QStyleItem,
      qt_meta_data_QStyleItem, 0 }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &QStyleItem::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *QStyleItem::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *QStyleItem::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_QStyleItem))
        return static_cast<void*>(const_cast< QStyleItem*>(this));
    return QDeclarativeItem::qt_metacast(_clname);
}

int QStyleItem::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QDeclarativeItem::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        switch (_id) {
        case 0: elementTypeChanged(); break;
        case 1: textChanged(); break;
        case 2: sunkenChanged(); break;
        case 3: raisedChanged(); break;
        case 4: activeChanged(); break;
        case 5: selectedChanged(); break;
        case 6: focusChanged(); break;
        case 7: onChanged(); break;
        case 8: hoverChanged(); break;
        case 9: horizontalChanged(); break;
        case 10: minimumChanged(); break;
        case 11: maximumChanged(); break;
        case 12: valueChanged(); break;
        case 13: activeControlChanged(); break;
        case 14: infoChanged(); break;
        case 15: styleChanged(); break;
        case 16: paintMarginsChanged(); break;
        case 17: hintChanged(); break;
        case 18: cursorChanged(); break;
        case 19: fontHeightChanged(); break;
        case 20: { int _r = pixelMetric((*reinterpret_cast< const QString(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< int*>(_a[0]) = _r; }  break;
        case 21: { QVariant _r = styleHint((*reinterpret_cast< const QString(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< QVariant*>(_a[0]) = _r; }  break;
        case 22: { QSize _r = sizeFromContents((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])));
            if (_a[0]) *reinterpret_cast< QSize*>(_a[0]) = _r; }  break;
        case 23: updateItem(); break;
        case 24: { QString _r = hitTest((*reinterpret_cast< int(*)>(_a[1])),(*reinterpret_cast< int(*)>(_a[2])));
            if (_a[0]) *reinterpret_cast< QString*>(_a[0]) = _r; }  break;
        case 25: { QRect _r = subControlRect((*reinterpret_cast< const QString(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< QRect*>(_a[0]) = _r; }  break;
        case 26: showToolTip((*reinterpret_cast< const QString(*)>(_a[1]))); break;
        case 27: { int _r = textWidth((*reinterpret_cast< const QString(*)>(_a[1])));
            if (_a[0]) *reinterpret_cast< int*>(_a[0]) = _r; }  break;
        default: ;
        }
        _id -= 28;
    }
#ifndef QT_NO_PROPERTIES
      else if (_c == QMetaObject::ReadProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: *reinterpret_cast< bool*>(_v) = sunken(); break;
        case 1: *reinterpret_cast< bool*>(_v) = raised(); break;
        case 2: *reinterpret_cast< bool*>(_v) = active(); break;
        case 3: *reinterpret_cast< bool*>(_v) = selected(); break;
        case 4: *reinterpret_cast< bool*>(_v) = focus(); break;
        case 5: *reinterpret_cast< bool*>(_v) = on(); break;
        case 6: *reinterpret_cast< bool*>(_v) = hover(); break;
        case 7: *reinterpret_cast< bool*>(_v) = horizontal(); break;
        case 8: *reinterpret_cast< QString*>(_v) = elementType(); break;
        case 9: *reinterpret_cast< QString*>(_v) = text(); break;
        case 10: *reinterpret_cast< QString*>(_v) = activeControl(); break;
        case 11: *reinterpret_cast< QString*>(_v) = info(); break;
        case 12: *reinterpret_cast< QString*>(_v) = style(); break;
        case 13: *reinterpret_cast< QString*>(_v) = hint(); break;
        case 14: *reinterpret_cast< QString*>(_v) = cursor(); break;
        case 15: *reinterpret_cast< int*>(_v) = minimum(); break;
        case 16: *reinterpret_cast< int*>(_v) = maximum(); break;
        case 17: *reinterpret_cast< int*>(_v) = value(); break;
        case 18: *reinterpret_cast< int*>(_v) = paintMargins(); break;
        case 19: *reinterpret_cast< QString*>(_v) = fontFamily(); break;
        case 20: *reinterpret_cast< double*>(_v) = fontPointSize(); break;
        case 21: *reinterpret_cast< int*>(_v) = fontHeight(); break;
        }
        _id -= 22;
    } else if (_c == QMetaObject::WriteProperty) {
        void *_v = _a[0];
        switch (_id) {
        case 0: setSunken(*reinterpret_cast< bool*>(_v)); break;
        case 1: setRaised(*reinterpret_cast< bool*>(_v)); break;
        case 2: setActive(*reinterpret_cast< bool*>(_v)); break;
        case 3: setSelected(*reinterpret_cast< bool*>(_v)); break;
        case 4: setFocus(*reinterpret_cast< bool*>(_v)); break;
        case 5: setOn(*reinterpret_cast< bool*>(_v)); break;
        case 6: setHover(*reinterpret_cast< bool*>(_v)); break;
        case 7: setHorizontal(*reinterpret_cast< bool*>(_v)); break;
        case 8: setElementType(*reinterpret_cast< QString*>(_v)); break;
        case 9: setText(*reinterpret_cast< QString*>(_v)); break;
        case 10: setActiveControl(*reinterpret_cast< QString*>(_v)); break;
        case 11: setInfo(*reinterpret_cast< QString*>(_v)); break;
        case 13: setHint(*reinterpret_cast< QString*>(_v)); break;
        case 14: setCursor(*reinterpret_cast< QString*>(_v)); break;
        case 15: setMinimum(*reinterpret_cast< int*>(_v)); break;
        case 16: setMaximum(*reinterpret_cast< int*>(_v)); break;
        case 17: setValue(*reinterpret_cast< int*>(_v)); break;
        case 18: setPaintMargins(*reinterpret_cast< int*>(_v)); break;
        }
        _id -= 22;
    } else if (_c == QMetaObject::ResetProperty) {
        _id -= 22;
    } else if (_c == QMetaObject::QueryPropertyDesignable) {
        _id -= 22;
    } else if (_c == QMetaObject::QueryPropertyScriptable) {
        _id -= 22;
    } else if (_c == QMetaObject::QueryPropertyStored) {
        _id -= 22;
    } else if (_c == QMetaObject::QueryPropertyEditable) {
        _id -= 22;
    } else if (_c == QMetaObject::QueryPropertyUser) {
        _id -= 22;
    }
#endif // QT_NO_PROPERTIES
    return _id;
}

// SIGNAL 0
void QStyleItem::elementTypeChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 0, 0);
}

// SIGNAL 1
void QStyleItem::textChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 1, 0);
}

// SIGNAL 2
void QStyleItem::sunkenChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 2, 0);
}

// SIGNAL 3
void QStyleItem::raisedChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 3, 0);
}

// SIGNAL 4
void QStyleItem::activeChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 4, 0);
}

// SIGNAL 5
void QStyleItem::selectedChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 5, 0);
}

// SIGNAL 6
void QStyleItem::focusChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 6, 0);
}

// SIGNAL 7
void QStyleItem::onChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 7, 0);
}

// SIGNAL 8
void QStyleItem::hoverChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 8, 0);
}

// SIGNAL 9
void QStyleItem::horizontalChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 9, 0);
}

// SIGNAL 10
void QStyleItem::minimumChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 10, 0);
}

// SIGNAL 11
void QStyleItem::maximumChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 11, 0);
}

// SIGNAL 12
void QStyleItem::valueChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 12, 0);
}

// SIGNAL 13
void QStyleItem::activeControlChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 13, 0);
}

// SIGNAL 14
void QStyleItem::infoChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 14, 0);
}

// SIGNAL 15
void QStyleItem::styleChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 15, 0);
}

// SIGNAL 16
void QStyleItem::paintMarginsChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 16, 0);
}

// SIGNAL 17
void QStyleItem::hintChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 17, 0);
}

// SIGNAL 18
void QStyleItem::cursorChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 18, 0);
}

// SIGNAL 19
void QStyleItem::fontHeightChanged()
{
    QMetaObject::activate(this, &staticMetaObject, 19, 0);
}
QT_END_MOC_NAMESPACE
