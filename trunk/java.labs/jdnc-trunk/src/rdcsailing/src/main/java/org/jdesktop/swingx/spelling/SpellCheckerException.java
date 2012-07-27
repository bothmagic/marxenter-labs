/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx.spelling;

/**
 * This exception is thrown when the dictionary is unavailable.
 * Check the locale and whether a dictionary is available for the given locale.
 * @author Ryan Cuprak
 */
public class SpellCheckerException extends Exception {


    /**
     * Creates a new SpellCheckerException
     * @param message - message detailing why the dictionary isn't available.
     */
    public SpellCheckerException(String message) {
        super(message);
    }
}
