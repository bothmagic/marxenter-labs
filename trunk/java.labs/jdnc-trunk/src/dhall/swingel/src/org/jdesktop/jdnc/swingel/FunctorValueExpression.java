/**
 * FunctorValueExpression.java
 */

package org.jdesktop.jdnc.swingel;

import net.sf.jga.fn.BinaryFunctor;

public class FunctorValueExpression /*extends ValueExpression*/ {
    
    FunctorValueExpression(String exp, BinaryFunctor<ELContext,?,?> fn) {
        this.fn = fn;
        this.exp = exp;
    }

    private BinaryFunctor<ELContext,?,?> fn;
    private String exp;
    
    public BinaryFunctor<ELContext,?,?> getFunctor() { return fn; }


    // ValueExpression implementation
    
//     Class getExpectedType(){}
//     Class getType(ELContext context) {}
    
    Object getValue(ELContext context){
        return fn.fn(context,null);
    }
    
//     boolean isReadOnly(ELContext context) {}
//     void setValue(ELContext context, Object value)

    // Expression implementation

    String getExpressionString() { return exp; }
//     boolean isLiteralText()

    // Object implementation
    
//     boolean equals(Object obj){}
//     int hashCode() }{
} 
