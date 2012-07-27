/**
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
 *   * Neither the name of the TimingFramework project nor the names of its
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
package org.jdesktop.swingx.renderer.scott.extreme;

import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;


/**
 *
 * A RenderingComponentController specialized on the "extreme"
 * RendererPanel. 
 * 
 * @author Jeanette Winzenburg
 */
public class MessageFormProvider extends ComponentProvider<RendererPanel> {

    public MessageFormProvider() {
        this(false);
    }

    public MessageFormProvider(boolean hideImage) {
        super();
        if (hideImage) {
            rendererComponent.hideImage();
        }
    }

    @Override
    protected void configureState(CellContext context) {
        // reset the border ... this is a bit fishy ...
        rendererComponent.setDefaultBorder();
        if (!context.isSelected()) {
            rendererComponent.setDefaultForeground();
        }
    }

    @Override
    protected RendererPanel createRendererComponent() {
        return new RendererPanel();
    }

    @Override
    protected void format(CellContext context) {
        Object value = context.getValue();
        if (value instanceof Message) {
            rendererComponent.setMessage((Message) value);
        } else {
            rendererComponent.setMessage(null);
        }

    }
}
