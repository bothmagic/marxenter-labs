/*
 * $Id: PatternValidator.java 2364 2008-03-27 12:58:16Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.validator;

import java.util.regex.Pattern;

/**
 * A validator to ensure that a string matches a regular expression pattern.
 */
public class PatternValidator implements Validator<String> {
    private Pattern pattern;
    
    /**
     * Creates a validator for ensuring that the
     * {@code String matches the specified pattern.
     * 
     * @param regex
     *            a string regular expression pattern
     */
    public PatternValidator(String regex) {
        this(Pattern.compile(regex));
    }
    
    /**
     * Creates a validator for ensuring that the
     * {@code String matches the specified pattern.
     * 
     * @param pattern
     *            a compiled regular expression pattern
     */
    public PatternValidator(Pattern pattern) {
        this.pattern = pattern;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(String object) {
        return pattern.matcher(object).matches();
    }
}
