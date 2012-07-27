/**
 * $Id: DynamicAction.java 2578 2008-07-29 23:16:17Z osbald $
 *
 * Copyright (c) 2006, Sun Microsystems, Inc
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following 
 *     disclaimer in the documentation and/or other materials provided 
 *     with the distribution.
 *   * Neither the name of the PasswordStore project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jdesktop.incubator.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;

/**
 * @version $Revision: 2578 $
 */
public class DynamicAction extends AbstractAction {
    // Method to invoke
    private String methodName;

    // Target for the method
    private Object target;

    // Arguments to the method.
    private Object[] args;

    public DynamicAction(Object target, String methodName, Object... args) {
        this.target = target;
        this.methodName = methodName;
        this.args = args;
    }

    public void actionPerformed(ActionEvent e) {
        Class klass = target.getClass();
        try {
            Class[] argClasses = null;
            if (args != null && args.length > 0) {
                argClasses = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    argClasses[i] = args[i].getClass();
                }
            }
            Method mid = klass.getMethod(methodName, argClasses);
            if (mid != null) {
                if (args != null) {
                    mid.invoke(target, args);
                } else {
                    mid.invoke(target);
                }
            }
        } catch (Exception exception) {
            Application.getInstance().uncaughtException(exception);
        }
    }
}
