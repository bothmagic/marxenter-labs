/*
 * Created on 02.11.2007
 *
 */
package org.jdesktop.beansbindingx.validator;

import org.jdesktop.beansbinding.Validator;

public class NotEmptyValidator extends Validator<String> {

    @Override
    public Result validate(String value) {
        return empty(value) ? createResult(value) : null;
    }

    /**
     * PENDING: need to make localizable
     * @param value
     * @return
     */
    private Result createResult(String value) {
        return new Result(value, "value must not be empty");
    }

    private boolean empty(String string) {
        return (string == null) || "".equals(string);
    }

}
