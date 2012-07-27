/*
 * Created on 18.06.2008
 *
 */
package netbeans.xoutline;

import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingProvider;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * A ComponentProvider specialized for hierarchical column in a 
 * TreeTable.
 * 
 */
public class BasicTreeTablePanelProvider extends ComponentProvider<BasicTreeTablePanel> {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(BasicTreeTablePanelProvider.class.getName());
    protected ComponentProvider<?> wrappee;
    private boolean unwrapUserObject;

    /**
     * Instantiates a BasicTreeTablePanelProvider with default LabelProvider.
     * 
     */
    public BasicTreeTablePanelProvider() {
        this((ComponentProvider<?>) null);
    }

    /**
     * Instantiates a BasicTreeTablePanelProvider with default wrappee. Uses the 
     * given IconValue to configure the icon. Uses the given StringValue
     * to configure the wrappee. 
     * 
     * @param iconValue the IconValue to use for configuring the icon.
     * @param wrappeeStringValue the StringValue to use in the wrappee.
     */
    public BasicTreeTablePanelProvider(IconValue iconValue, StringValue wrappeeStringValue) {
        this(wrappeeStringValue);
        setStringValue(new MappedValue(null, iconValue));
    }

    /**
     * Instantiates a BasicTreeTablePanelProvider with default wrappee. Uses the 
     * given IconValue to configure the icon. 
     * 
     * @param iconValue the IconValue to use for configuring the icon.
     */
    public BasicTreeTablePanelProvider(IconValue iconValue) {
        this();
        setStringValue(new MappedValue(null, iconValue));
    }
   
    /**
     * Instantiates a BasicTreeTablePanelProvider with default wrappee configured
     * with the given StringValue. 
     * 
     * PENDING: we have a slight semantic glitch compared to super because
     * the given StringValue is <b>not</b> for use in this provider but for use 
     * in the wrappee!
     * 
     * @param wrappeeStringValue the StringValue to use in the wrappee.
     */
    public BasicTreeTablePanelProvider(StringValue wrappeeStringValue) {
        this(new LabelProvider(wrappeeStringValue));
    }

    /**
     * Instantiates a WrappingProvider with the given delegate
     * provider for the node content. If null, a default 
     * LabelProvider will be used. 
     * 
     * @param delegate the provider to use as delegate
     */
    public BasicTreeTablePanelProvider(ComponentProvider<?> delegate) {
        this(delegate, true);
    }
    
    /**
     * Instantiates a BasicTreeTablePanelProvider with the given delegate
     * provider for the node content and unwrapUserObject property. 
     * If the delegate is null, a default LabelProvider will be used. 
     * 
     * @param delegate the provider to use as delegate
     * @param unwrapUserObject a flag indicating whether this provider
     * should auto-unwrap the userObject from the context value. 
     */
    public BasicTreeTablePanelProvider(ComponentProvider<?> delegate, boolean unwrapUserObject) {
         super();
        // PENDING JW: this is inherently unsafe - must not call 
        // non-final methods from constructor
        setWrappee(delegate);
        setStringValue(StringValues.EMPTY);
        setUnwrapUserObject(unwrapUserObject);
    }

    /**
     * Sets the given provider as delegate for the node content. 
     * If the delegate is null, a default LabelProvider is set.
     * If the delegate is of type WrappingProvider, it is "deconstructed".
     * That is we use its wrappee as delegate and its StringValue for
     * ourselves. <p>
     * 
     * PENDING: simplify/better expose deconstruction? <p>
     *  PENDING: rename to setDelegate?<p>
     *  
     *  
     * @param delegate the provider to use as delegate. 
     */
    public void setWrappee(ComponentProvider<?> delegate) {
        if (delegate == null) {
            delegate = new LabelProvider();
        }
        if (delegate instanceof WrappingProvider) {
            setStringValue(delegate.getStringValue());
            delegate = ((WrappingProvider) delegate).getWrappee();
        }
        this.wrappee = delegate;
    }

    /**
     * Returns the delegate provider used to render the node content.
     * 
     * @return the provider used for rendering the node content.
     */
    public ComponentProvider<?> getWrappee() {
        return wrappee;
    }
    
    /**
     * Sets the unwrapUserObject property. If true, this provider 
     * replaces a context value of type XXNode with its user object before
     * delegating to the wrappee. Otherwise the value is passed as-is always.<p>
     * 
     * The default value is true.
     * 
     * @param unwrap
     * @see #getUnwrapUserObject()
     */
    public void setUnwrapUserObject(boolean unwrap) {
        this.unwrapUserObject = unwrap;
    }
    
    /**
     * Returns a boolean indicating whether this provider tries to unwrap 
     * a userObject from a tree/table/node type value before delegating the
     * context. 
     * 
     * @return a flag indicating the auto-unwrap property.
     * 
     * @see #setUnwrapUserObject(boolean)
     */
    public boolean getUnwrapUserObject() {
        return unwrapUserObject;
    }
    
    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to comply to contract: returns the string representation as 
     * provided by the wrappee (as this level has no string rep). Must do the
     * same unwrapping magic as in configuring the rendering component if the
     * unwrapUserObject property is true. <p>
     * 
     * 
     * @param value the Object to get a String representation for.
     * 
     * @see #setUnwrapUserObject(boolean)
     * @see #getUnwrappedValue(Object)
     */
    @Override
    public String getString(Object value) {
        value = getUnwrappedValue(value);
        return wrappee.getString(value);
    }

    /**
     * Returns the value as it should be passed to the delegate. If the unwrapUserObject
     * property is true, tries return a userObject as appropriate for the value type.
     * Returns the given value itself, ff the property is false or the type does 
     * not support the notion of userObject<p>
     * 
     * Here: unwraps userObject of DefaultMutableTreeNode and TreeTableNode.<p>
     * 
     * @param value the value to possibly unwrap
     * @return the userObject if the value has an appropriate type and the 
     *   unwrapUserObject property is true, otherwise returns the value unchanged.
     *   
     * @see #setUnwrapUserObject(boolean)
     * @see #getString(Object)
     * @see #getRendererComponent(CellContext)  
     */
    protected Object getUnwrappedValue(Object value) {
        if (!getUnwrapUserObject()) return value;
        if (value instanceof DefaultMutableTreeNode) {
            value = ((DefaultMutableTreeNode) value).getUserObject();
        } else if (value instanceof TreeTableNode) {
            TreeTableNode node = (TreeTableNode) value;
            value = node.getUserObject();
        }
        return value;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BasicTreeTablePanel getRendererComponent(CellContext context) {
        if (context != null) {
            rendererComponent.setComponent(wrappee.getRendererComponent(null));
            Object oldValue = adjustContextValue(context);
            // PENDING JW: sequence of config?
            // A - first wrappee, then this allows to override configure/format methods
            // of this class and overrule the wrappee, problem with borders?
            // B - first this, then wrappee allows overrule by overriding getRendererComp
            // would take control from wrappee (f.i. Hyperlink foreground)
            wrappee.getRendererComponent(context);
            super.getRendererComponent(context);
            restoreContextValue(context, oldValue);
            return rendererComponent;
        }
        // PENDING JW: Findbugs barking [NP] Load of known null value
        // probably can move the return rendererComponent from the if
        // to here (the contract is to return the comp as-is if the
        // context is null) - so we can do it here instead of delegating
        // to super?
        return super.getRendererComponent(context);
    }

    /**
     * Restores the context value to the old value.
     * 
     * @param context the CellContext to restore.
     * @param oldValue the value to restore the context to.
     */
    protected void restoreContextValue(CellContext context, Object oldValue) {
        context.replaceValue(oldValue);
    }

    /**
     * Replace the context's value with the userobject if the value is a type
     * supporting the notion of userObject and this provider's unwrapUserObject
     * property is true. Otherwise does nothing.<p>
     * 
     * Subclasses may override but must guarantee to return the original 
     * value for restoring. 
     * 
     * @param context the context to adjust
     * @return the old context value
     * 
     * @see #setUnwrapUserObject(boolean)
     * @see #getString(Object)
     */
    protected Object adjustContextValue(CellContext context) {
        Object oldValue = context.getValue();
        if (getUnwrapUserObject()) {
            context.replaceValue(getUnwrappedValue(oldValue));
        }
        return oldValue;
    }


    @Override
    protected void configureState(CellContext context) {
        if (context instanceof TreeTableCellContext) {
            rendererComponent.setIndent(((TreeTableCellContext) context).getVisualDepth());
            rendererComponent.setHandle(((TreeTableCellContext) context).getHandle());
        }            
//        wrappee.getRendererComponent(null).setBorder(BorderFactory.createLineBorder(Color.RED));

//        rendererComponent.setCellBorder(BorderFactory.createLineBorder(Color.RED));
    }
    
    /**
     * {@inheritDoc} <p>
     * 
     * Here: implemented to set the icon.
     */
    @Override
    protected void format(CellContext context) {
        rendererComponent.setIcon(getValueAsIcon(context));
    }

    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to fallback to the default icons supplied by the 
     * context if super returns null.
     *   
     */
    @Override
    protected Icon getValueAsIcon(CellContext context) {
        Icon icon = super.getValueAsIcon(context);
        if (icon == null) {
            return context.getIcon();
        }
        return IconValue.NULL_ICON == icon ? null : icon;
    }
    
    @Override
    protected BasicTreeTablePanel createRendererComponent() {
        return new BasicTreeTablePanel();
    }

//    @Override
//    protected BasicTreeTablePanelReverted createRendererComponent() {
//        return new BasicTreeTablePanelReverted();
//    }
}
