#include "creator.h"

#include "../../src/qlistmodelinterface.h" // ### FIXME: qml has 3rdparty/qlistmodelinterfache.h

#include <QmlContext>
#include <QmlEngine>
#include <QmlComponent>

#include <QDebug>

// context

class Context : public QmlContext
{
public:
    Context(QmlEngine *parentEngine, QObject *parentObject = 0)
        : QmlContext(parentEngine, parentObject), m_index(-1), m_model(0) {}
    Context(QmlContext *parentContext, QObject *parentObject = 0)
        : QmlContext(parentContext, parentObject), m_index(-1), m_model(0) {}

    inline void setIndex(int index) { m_index = index; }
    inline int index() const { return m_index; }
    inline void setModel(QtListModelInterface *model) { m_model = model; }
    inline QtListModelInterface *model() const { return m_model; }
    inline void enableHack() { setHackEnabled(true); }
    inline void disableHack() { setHackEnabled(false); }

protected:
    void contextPropertyWrite(const QString &name, const QVariant &value);
    QVariant contextPropertyRead(const QString &name) const;

private:
    int m_index;
    QtListModelInterface *m_model;
};

void Context::contextPropertyWrite(const QString &name, const QVariant &value)
{
    QHash<int,QVariant> data;
    int role = m_model->roles().key(name.toLatin1()); // ### FIXME
    data.insert(role, value);
    m_model->setData(m_index, data);
}

QVariant Context::contextPropertyRead(const QString &name) const
{
    const int role = m_model->roles().key(name.toLatin1()); // ### FIXME
    return m_model->data(m_index).value(role);
}

// creator

Creator::Creator(QmlEngine *engine, QmlComponent *component)
    : QtGraphicsListViewItemCreatorBase(), m_engine(engine), m_component(component)
{
}

Creator::~Creator()
{
}

QGraphicsObject *Creator::create(int index, QtGraphicsListView *view)
{
    // setup the context for the item and set the data
    QmlContext *parentContext = QmlEngine::contextForObject(view);
    Context *context = parentContext ? new Context(parentContext) : new Context(m_engine);
    context->setIndex(index);
    context->setModel(view->model());
    // setting the properties is slow; find a way to delay this
    setContextProperties(index, context, context->model());

    // use the component to create the item
    QGraphicsObject *item = 0;
    if (QObject *obj = m_component->create(context)) {
        item = static_cast<QGraphicsObject*>(obj);
        item->setParentItem(view);
        context->setParent(item);
        m_contexts.insert(item, context);
        context->enableHack();
    }

    if (!context->parent())
        delete context;
    return item;
}

QGraphicsObject *Creator::reassign(int index, QGraphicsObject *item, QtGraphicsListView *view)
{
    Q_UNUSED(view);
    m_contexts.value(item)->setIndex(index);
    // setting the properties is slow; find a way to delay this
    // - they cause the bindings to trigger
    setContextProperties(index, m_contexts.value(item), m_contexts.value(item)->model());
    return item;
}

void Creator::update(int index, QGraphicsObject *item, const QList<int> &roles)
{
    Q_UNUSED(roles);
    // setting the properties is slow; find a way to delay this
    Context *context = m_contexts.value(item);
    setContextProperties(index, context, context->model());
}

void Creator::recycle(QGraphicsObject *item, QtGraphicsListView *view)
{
    Q_UNUSED(view);
    m_contexts.remove(item);
    delete item;
}

void Creator::setContextProperties(int index, QmlContext *context, QtListModelInterface *model) const
{
    if (context && model) {
        QHash<int, QByteArray> roles = model->roles();
        QHash<int, QVariant> data = model->data(index);
        QHash<int, QVariant>::ConstIterator it = data.constBegin();
        for (;it != data.constEnd(); ++it) {
            QByteArray name = roles.value(it.key());
            QVariant value = it.value();
            static_cast<Context*>(context)->disableHack();
            context->setContextProperty(name, value);
            static_cast<Context*>(context)->enableHack();
        }
    }
}

