package de.mt.jackrabbit.browser.ui;

import java.io.IOException;
import org.openide.actions.SaveAction;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public final class SomeAction extends SaveAction {

  protected void performAction(Node[] activatedNodes)  {
    try {
      SaveCookie saveCookie = activatedNodes[0].getLookup().lookup(SaveCookie.class);
      saveCookie.save();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  protected int mode() {
    return CookieAction.MODE_ALL;
  }

  public String getName() {
    return NbBundle.getMessage(SomeAction.class, "CTL_SomeAction");
  }

  protected Class[] cookieClasses() {
    return new Class[]{SaveCookie.class};
  }

  @Override
  protected void initialize() {
    super.initialize();
    // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
    putValue("noIconInMenu", Boolean.TRUE);
  }

  public HelpCtx getHelpCtx() {
    return HelpCtx.DEFAULT_HELP;
  }

  @Override
  protected boolean asynchronous() {
    return false;
  }

  public void save() throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
  
  
}