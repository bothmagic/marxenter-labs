/*
 * $Id: AbstractEnum.java 2997 2009-01-30 13:48:26Z kschaefe $
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.constants;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jdesktop.swingx.util.Contract;

/**
 * An improved set of location constants. This class is designed as a
 * replacement for {@code SwingConstants}.
 * <p>
 * This class replicates behavior of {@link Enum} but does not extend it. It is
 * not possible to create abstract enums (at this time) and {@code Location} is
 * abstract. The choice of abstract class over interface was to ensure
 * behavioral constraints, provide a single common ancestor, and prevent any
 * class from becoming a "location."
 * 
 * @author Karl George Schaefer
 * @author Josh Bloch (original Enum code)
 * @author Neal Gafter (original Enum code)
 */
@SuppressWarnings("serial")
public abstract class AbstractEnum<E extends AbstractEnum<?>> implements
        Comparable<E>, Serializable {
    /**
     * This lock must be held whenever accessing the const directory.
     * <p>
     * TODO review this lock. Not sure it is necessary since class
     * initialization occurs serially. Need a Bill Pugh or similar to review.
     */
    private static final Lock CONST_DIRECTORY_LOCK = new ReentrantLock();

    @SuppressWarnings("unchecked")
    private static final Map<Class<? extends AbstractEnum>, Map<String, AbstractEnum>> CONST_DIRECTORY
            = new HashMap<Class<? extends AbstractEnum>, Map<String, AbstractEnum>>();
    
    private final String name;

    private final int ordinal;

    /**
     * Creates an abstract enum with the specified name.
     * 
     * @param name
     *            the name of the enum
     * @throws IllegalStateException
     *             if two enums of the same class type have the same name
     */
    @SuppressWarnings("unchecked")
    protected AbstractEnum(String name) {
        validateEnum(name);
        
        try {
            CONST_DIRECTORY_LOCK.lock();
            
            if (!CONST_DIRECTORY.containsKey(getClass())) {
                CONST_DIRECTORY.put(getClass(), new HashMap<String, AbstractEnum>());
            }
            
            Map<String, AbstractEnum> consts = CONST_DIRECTORY.get(getClass());
            
            if (consts.containsKey(name)) {
                throw new IllegalStateException("duplicate name " + name);
            }
            
            this.name = name;
            ordinal = consts.size();
            
            consts.put(name, this);
        } finally {
            CONST_DIRECTORY_LOCK.unlock();
        }
    }

    /**
     * This method ensures that the enum is validly declared. To be a validly declared enum, the
     * following constrains must hold:
     * <ol>
     * <li>The class is final.</li>
     * <li>All constructors must be private.</li>
     * <li>All {@link #name() names} must be valid field identifiers.</li>
     * </ol>
     * 
     * @param name
     *            the name to check
     */
    private void validateEnum(String name) {
        // ensure that the name matches some field
        // (can't ensure that it's the same one)
        try {
            if (!Modifier.isFinal(getClass().getModifiers())) {
                throw new InstantiationError("must be declared final");
            }
            
            for (Constructor<?> c : getClass().getDeclaredConstructors()) {
                if (!Modifier.isPrivate(c.getModifiers())) {
                    throw new InstantiationError("must have private constructor");
                }
            }
            
            //we could do a stronger check of field.name = this.name and
            //getFields()[i] == this where i == ordinal
            getClass().getField(name);
        } catch (SecurityException ignore) {
            //in a sandbox; ignore
            //most likely a different exception will occur down the line
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Returns the location constant of the specified location type with the
     * specified name. The name must match exactly an identifier used to declare
     * a location constant in this type. (Extraneous whitespace characters are
     * not permitted.)
     * 
     * @param enumType
     *            the {@code Class} object of the location type from which to
     *            return a constant
     * @param name
     *            the name of the constant to return
     * @return the location constant of the specified location type with the
     *         specified name
     * @throws IllegalArgumentException
     *             if the specified location type has no constant with the
     *             specified name, or the specified class object does not
     *             represent a location type
     * @throws NullPointerException
     *             if {@code locationType} or {@code name} is null
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractEnum<?>> T valueOf(Class<T> enumType,
            String name) {
        Contract.asNotNull(enumType, "enumType is null");
        Contract.asNotNull(name, "name is null");
        
        if (Modifier.isAbstract(enumType.getModifiers())) {
            throw new IllegalArgumentException("cannot generate enum for abstract type");
        }
        
        try {
            CONST_DIRECTORY_LOCK.lock();
            
            if (CONST_DIRECTORY.containsKey(enumType)) {
                Map<String, AbstractEnum> consts = CONST_DIRECTORY.get(enumType);
                
                if (consts.containsKey(name)) {
                    return (T) consts.get(name);
                }
            }
        } finally {
            CONST_DIRECTORY_LOCK.unlock();
        }
        
        throw new IllegalArgumentException(
                "no constant named " + name + " for class " + enumType);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends AbstractEnum<?>> T[] values(Class<T> locationType) {
        Contract.asNotNull(locationType, "locationType is null");
        
        try {
            CONST_DIRECTORY_LOCK.lock();
            
            if (CONST_DIRECTORY.containsKey(locationType)) {
                T[] values = (T[]) Array.newInstance(locationType, 0);
                
                values = ((Collection<T>) CONST_DIRECTORY.get(locationType).values()).toArray(values);
                
                Arrays.sort(values);
                
                return values;
            }
        } finally {
            CONST_DIRECTORY_LOCK.unlock();
        }
        
        //guard against erasure
        throw new IllegalArgumentException("invalid class type");
    }

    /**
     * Returns the name of this location constant, exactly as declared in its
     * location declaration.
     * 
     * <b>Most programmers should use the {@link #toString} method in preference
     * to this one, as the toString method may return a more user-friendly
     * name.</b> This method is designed primarily for use in specialized
     * situations where correctness depends on getting the exact name, which
     * will not vary from release to release.
     * 
     * @return the name of this location constant
     */
    public final String name() {
        return name;
    }

    /**
     * Returns the ordinal of this enumeration constant (its position in its
     * location declaration, where the initial constant is assigned an ordinal
     * of zero).
     * 
     * Most programmers will have no use for this method.
     * 
     * @return the ordinal of this enumeration constant
     */
    public final int ordinal() {
        return ordinal;
    }

    /**
     * Returns the name of this location constant, as contained in the
     * declaration. This method may be overridden, though it typically isn't
     * necessary or desirable. A location type should override this method when
     * a more "programmer-friendly" string form exists.
     * 
     * @return the name of this location constant
     */
    @Override
    public String toString() {
        return name();
    }

    /**
     * Returns true if the specified object is equal to this enum constant.
     * 
     * @param other
     *            the object to be compared for equality with this object.
     * @return true if the specified object is equal to this enum constant.
     */
    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Returns a hash code for this location constant.
     * 
     * @return a hash code for this location constant.
     */
    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * Throws CloneNotSupportedException. This guarantees that locations are
     * never cloned, which is necessary to preserve their "singleton" status.
     * 
     * @return (never returns)
     */
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * {@inheritDoc}
     */
    public final int compareTo(E o) {
        return this.ordinal - o.ordinal;
    }

    /**
     * prevent default deserialization
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        throw new InvalidObjectException("can't deserialize location");
    }

    /**
     * prevent default deserialization
     */
    @SuppressWarnings("unused")
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("can't deserialize location");
    }

    /**
     * location classes cannot have finalize methods.
     */
    protected final void finalize() {
    }
}
