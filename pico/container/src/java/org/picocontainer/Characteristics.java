/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by                                                          *
 *****************************************************************************/
package org.picocontainer;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Collection of immutable properties, holding behaviour characteristics.  See 
 * <a href="http://www.picocontainer.org/behaviors.html">The PicoContainer Website</a> for details on the usage
 * of Characteristics.
 * 
 * @author Paul Hammant
 * @see org.picocontainer.ComponentAdapter
 * @see org.picocontainer.Behavior
 */
@SuppressWarnings("serial")
public final class Characteristics {

    private static final String _INJECTION = "injection";
    private static final String _NONE = "none";
    private static final String _CONSTRUCTOR = "constructor";
    private static final String _METHOD = "method";
    private static final String _SETTER = "setter";
    private static final String _CACHE = "cache";
    private static final String _SYNCHRONIZING = "synchronizing";
    private static final String _LOCKING = "locking";
    private static final String _HIDE_IMPL = "hide-impl";
    private static final String _PROPERTY_APPLYING = "property-applying";
    private static final String _AUTOMATIC = "automatic";
    private static final String _USE_NAMES = "use-parameter-names";
    private static final String _ENABLE_CIRCULAR = "enable-circular";
    private static final String _GUARD = "guard";
    private static final String _EMJECTION = "emjection_enabled";

    /**
     * Since properties use strings, we supply String constants for Boolean conditions.
     */
    public static final String FALSE = "false";

    /**
     * Since properties use strings, we supply String constants for Boolean conditions.
     */
    public static final String TRUE = "true";

    /**
     * Turns on constructor injection.
     * @see org.picocontainer.injectors.ConstructorInjection
     */
    public static final Properties CDI = immutable(_INJECTION, _CONSTRUCTOR);

    /**
     * Turns on Setter Injection.
     * @see org.picocontainer.injectors.SetterInjection
     */
    public static final Properties SDI = immutable(_INJECTION, _SETTER);

    /**
     * Turns on Method Injection.
     */
    public static final Properties METHOD_INJECTION = immutable(_INJECTION, _METHOD);

    /**
     * Turns off Caching of component instances.  (Often referred to in other circles
     * as singleton). 
     * @see org.picocontainer.behaviors.Caching
     */
    public static final Properties NO_CACHE = immutable(_CACHE, FALSE);

    /**
     * Turns on Caching of component instances.  (Often referred to in other circles
     * as singleton)
     * @see org.picocontainer.behaviors.Caching
     */
    public static final Properties CACHE = immutable(_CACHE, TRUE);

    /**
     * Turns on synchronized access to the component instance.  (Under JDK 1.5 conditions,
     * it will be better to use {@link #LOCK} instead.
     * @see org.picocontainer.behaviors.Synchronizing
     */
    public static final Properties SYNCHRONIZE = immutable(_SYNCHRONIZING, TRUE);

    
    /**
     * Turns off synchronized access to the component instance.
     * @see org.picocontainer.behaviors.Synchronizing
     */
    public static final Properties NO_SYNCHRONIZE = immutable(_SYNCHRONIZING, FALSE);
    
    /**
     * Uses a java.util.concurrent.Lock to provide faster access than synchronized.
     * @see org.picocontainer.behaviors.Locking
     */
    public static final Properties LOCK = immutable(_LOCKING, TRUE);

    /**
     * Turns off locking synchronization.
     * @see org.picocontainer.behaviors.Locking
     */
    public static final Properties NO_LOCK = immutable(_LOCKING, FALSE);
    
    /**
     * Synonym for {@link #CACHE CACHE}.
     * @see org.picocontainer.behaviors.Caching
     */
    public static final Properties SINGLE = CACHE;

    /**
     * Synonym for {@link #NO_CACHE NO_CACHE}.
     * @see org.picocontainer.behaviors.Caching
     */
    public static final Properties NO_SINGLE = NO_CACHE;
    
    /**
     * Turns on implementation hiding.  You may use the JDK Proxy implementation included
     * in this version, <strong>or</strong> the ASM-based implementation hiding method
     * included in PicoContainer Gems.  However, you cannot use both in a single PicoContainer
     * instance.
     */
    public static final Properties HIDE_IMPL = immutable(_HIDE_IMPL, TRUE);

    /**
     * Turns off implementation hiding.
     * @see #HIDE_IMPL for more information.
     */
    public static final Properties NO_HIDE_IMPL = immutable(_HIDE_IMPL, FALSE);

    public static final Properties ENABLE_CIRCULAR = immutable(_ENABLE_CIRCULAR, TRUE);
    
    public static final Properties NONE = immutable(_NONE, "");

    /**
     * Turns on bean-setting property applications where certain simple properties are set
     * after the object is created based.
     */
    public static final Properties PROPERTY_APPLYING = immutable(_PROPERTY_APPLYING, TRUE);
    
    /**
     * Turns off bean-setting property applications.
     * @see org.picocontainer.behaviors.PropertyApplying
     */
    public static final Properties NO_PROPERTY_APPLYING = immutable(_PROPERTY_APPLYING, FALSE);

    public static final Properties AUTOMATIC = immutable(_AUTOMATIC, TRUE);

    public static final Properties USE_NAMES = immutable(_USE_NAMES, TRUE);

    public static final Properties EMJECTION_ENABLED = immutable(_EMJECTION, TRUE);

    public static final Properties GUARD = immutable(_GUARD, "guard");

    public static final Properties GUARD(String with) {
        return immutable(_GUARD, with);
    };

    /**
     * Transforms a single name value pair unto a <em>read only</em> {@linkplain java.util.Properties}
     * instance.
     * <p>Example Usage:</p>
     * <pre>
     * 		Properties readOnly = immutable("oneKey","oneValue"};
     * 		assert readOnly.getProperty("oneKey") != null);
     * </pre>
     * @param name the property key.
     * @param value the property value.
     * @return Read Only properties instance.
     */
    public static Properties immutable(String name, String value) {
        return new ImmutableProperties(name, value);
    }
    
    /**
     * Read only property set.  Once constructed, all methods that modify state will
     * throw UnsupportedOperationException.
     * @author Paul Hammant.
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static class ImmutableProperties extends Properties {

        private final Map<Object, Object> store;

        private final Set<String> stringNameSet;

        @Override
        public int size() {
            return store.size();
        }

        @Override
        public boolean isEmpty() {
            return store.isEmpty();
        }

        @Override
        public Enumeration<Object> keys() {
            return Collections.enumeration(store.keySet());
        }

        @Override
        public Enumeration<Object> elements() {
            return Collections.enumeration(store.values());
        }

        @Override
        public boolean contains(Object value) {
            return containsValue(value);
        }

        @Override
        public boolean containsValue(Object value) {
            return store.containsValue(value);
        }

        @Override
        public boolean containsKey(Object key) {
            return store.containsKey(key);
        }

        @Override
        public Object get(Object key) {
            return store.get(key);
        }

        @Override
        public Object clone() {
            Map.Entry entry = store.entrySet().iterator().next();
            return new ImmutableProperties((String) entry.getKey(), (String) entry.getValue());
        }

        @Override
        public String toString() {
            return store.toString();
        }

        @Override
        public Set<Object> keySet() {
            return store.keySet();
        }

        @Override
        public Set<Map.Entry<Object, Object>> entrySet() {
            return store.entrySet();
        }

        @Override
        public Collection<Object> values() {
            return store.values();
        }

        @Override
        public Set<String> stringPropertyNames() {
            return stringNameSet;
        }

        @Override
        public Enumeration<?> propertyNames() {
            return Collections.enumeration(store.keySet());
        }

        @Override
        public String getProperty(String key, String defaultValue) {
            String value = getProperty(key);
            return value != null ? value : defaultValue;
        }

        @Override
        public String getProperty(String key) {
            return (String) store.get(key);
        }

        public ImmutableProperties(String name, String value) {
            super();
            super.put(name, value);

            store = Collections.singletonMap((Object)name, (Object)value);
            stringNameSet = Collections.singleton(name);
        }

        /**
         * Read Only Object:  will throw UnsupportedOperationException.
         */
        @Override
        public Object remove( Object o) {
            throw new UnsupportedOperationException("immutable properties are read only");
        }

        /**
         * Read Only Object:  will throw UnsupportedOperationException.
         */
        @Override
        public Object setProperty(String string, String string1) {
            throw new UnsupportedOperationException("immutable properties are read only");
        }

        /**
         * Read Only Object:  will throw UnsupportedOperationException.
         */
		@Override
		public void clear() {
            throw new UnsupportedOperationException("immutable properties are read only");
		}

		@Override
		public Object put(Object key, Object value) {
            throw new UnsupportedOperationException("immutable properties are read only");
		}

        /**
         * Read Only Object:  will throw UnsupportedOperationException.
         */
		@Override
		public void putAll(Map<? extends Object, ? extends Object> t) {
            throw new UnsupportedOperationException("immutable properties are read only");
		}


    }

}
