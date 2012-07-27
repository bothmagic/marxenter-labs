/*
 * Copyright (c) 2002-2011 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jgoodies.validation.extras;

import static com.jgoodies.common.base.Preconditions.checkNotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Timer;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.AbstractValidationResultModel;


/**
 * A ValidationResultModel that defers changes for a specified delay.
 * Useful to coalesce frequent validation result changes. For example
 * if a validation requires some heavy computation, you may desire
 * to perform the validation only for a "stable" input state after
 * a series of quick input changes.<p>
 *
 * Wraps a given subject ValidationResultModel and returns the subject
 * ValidationResult or the ValidationResult to be set as this model's result.
 * Observes subject validation result property changes and forwards them
 * to listeners of this model. If a ValidationResult is set to this model,
 * a Swing Timer is used to delay the ValidationResult commit to the subject.
 * A previously started timer - if any - will be stopped before.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.12 $
 */
public final class DelayedValidationResultModel extends AbstractValidationResultModel {

    /**
     * Refers to the underlying subject ValidationResultModel.
     */
    private final ValidationResultModel subject;

    /**
     * The Timer used to perform the delayed commit.
     */
    private final Timer timer;

    /**
     * Holds the most recent pending value. It is updated
     * every time {@code #setResult} is invoked.
     */
    private ValidationResult pendingValue = null;


    // Instance Creation ******************************************************

    /**
     * Constructs a DelayedValidationResultModel for the given subject
     * ValidationResultModel and the specified Timer delay in milliseconds.
     *
     * @param subject   the underlying (or wrapped) ValidationResultModel
     * @param delay     the milliseconds to wait before a change
     *     shall be committed
     */
    public DelayedValidationResultModel(ValidationResultModel subject, int delay) {
        this.subject = subject;
        this.timer = new Timer(delay, new ValidationResultCommitListener());
        timer.setRepeats(false);
        subject.addPropertyChangeListener(new SubjectPropertyChangeHandler());
    }


    // ValueModel Implementation ******************************************

    /**
     * Returns the subject's value or in case of a pending commit,
     * the pending new value.
     *
     * @return the subject's current or future value.
     */
    public ValidationResult getResult() {
        return hasPendingChange()
            ? pendingValue
            : subject.getResult();
    }


    /**
     * Sets the given new value after this model's delay.
     * Does nothing if the new value and the latest pending value are the same.
     *
     * @param newResult   the value to set
     *
     * @throws NullPointerException if the new result is {@code null}
     */
    public void setResult(ValidationResult newResult) {
        checkNotNull(newResult, "The new result must not be null.");
        if (newResult == pendingValue) {
            return;
        }
        if (hasPendingChange()) {
            timer.stop();
        }
        pendingValue = newResult;
        timer.start();
    }


    // Misc *******************************************************************

    private boolean hasPendingChange() {
        return timer.isRunning();
    }


    // Event Handling *****************************************************

    /**
     * Describes the delayed action to be performed by the timer.
     */
    private final class ValidationResultCommitListener implements ActionListener {

        /**
         * An ActionEvent has been fired by the Timer after its delay.
         * Commits the pending ValidationResult and stops the timer.
         */
        public void actionPerformed(ActionEvent e) {
            subject.setResult(pendingValue);
            timer.stop();
        }
    }


    /**
     * Forwards validation property changes in the subject.
     */
    private final class SubjectPropertyChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }


}
