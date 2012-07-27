// ============================================================================
// $Id: TestSwingEL.java 620 2005-08-19 22:38:33Z david_hall $
// Copyright (c) 2005  David A. Hall
// ============================================================================
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// ============================================================================

package org.jdesktop.jdnc.swingel;

import junit.framework.TestCase;

/**
 * Exercises SwingEL
 * <p>
 * Copyright &copy; 2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 */

public class TestSwingEL extends TestCase {
    public TestSwingEL (String name){ super(name); }

    private SwingEL parser;
   
    public void setUp() {
        parser = new SwingEL(System.in);
    }

    public void tearDown() {
    }

    public void testLiteralText() throws ParseException {
        testLiteral("");
        testLiteral("foo");
        testLiteral("foo$");
        testLiteral("foo#");
        testLiteral("foo$#");
        testLiteral("foo$bar");
        testLiteral("$");
        testLiteral("#");
        testLiteral("\\$");
        testLiteral("\\#");
        testLiteral("\\${");
        testLiteral("\\#{");
    }

    public void testEmptyExpressions() throws ParseException {
        testScript("${ }", "");
        testScript("--${ }--", "----");
        testScript("${ }${ }", "");
        testScript("--${ }${ }--", "----");
        testScript("--${ }--${ }--", "------");
    }

    public void testLiteralExpressions() throws ParseException {
        testScript("${0}", 0);
        testScript("1${0}", "10");
        testScript("${0}1", "01");
        testScript("${0}${0}", "00");
        testScript("${1+2}+3", "3+3");
    }
    

    private void testLiteral(String script) throws ParseException {
        testScript(script, script);
    }
    
    private void testScript(String script, Object expect) throws ParseException {
        FunctorValueExpression exp = parser.parseExpression(script);
        System.out.println("");

        Object result = exp.getFunctor().fn(null,null);
        
        System.out.println("Expression:["+ exp.getExpressionString()+"]");
        System.out.println("Functor:   " + exp.getFunctor());
        System.out.println("Expecting: " + expect);
        System.out.println("Result:    " + result);
        System.out.println(expect.equals(result));
        
        assertEquals(expect, result);
    }

    
    static public void main (String[] args) {
        junit.swingui.TestRunner.run(TestSwingEL.class);
    }

}

