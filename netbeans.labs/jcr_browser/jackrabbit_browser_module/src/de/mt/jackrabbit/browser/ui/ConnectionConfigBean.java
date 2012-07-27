/*
 * ConnectionConfigBean.java
 * 
 * Created on 05.10.2007, 10:24:14
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mt.jackrabbit.browser.ui;

import java.io.File;

/**
 *
 * @author taubert
 */
public class ConnectionConfigBean {
  
  private File repoDir;
  private File configFile;
  private String username;
  private char[] password;

  public File getConfigFile() {
    return configFile;
  }

  public void setConfigFile(File configFile) {
    this.configFile = configFile;
  }

  public char[] getPassword() {
    return password;
  }

  public void setPassword(char[] password) {
    this.password = password;
  }

  public File getRepoDir() {
    return repoDir;
  }

  public void setRepoDir(File repoDir) {
    this.repoDir = repoDir;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
  
  
  

}
