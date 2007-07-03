/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Paul Hammaant                                            *
 *****************************************************************************/

package org.picocontainer.monitors;

import static org.picocontainer.monitors.ComponentMonitorHelper.format;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Member;

import org.picocontainer.ComponentMonitor;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;

/**
 * A {@link ComponentMonitor} which writes to a {@link Writer}. 
 * 
 * @author Paul Hammant
 * @author Aslak Helles&oslash;y
 * @author Mauro Talevi
 * @version $Revision: 1882 $
 */
public class WriterComponentMonitor implements ComponentMonitor {

    private final PrintWriter out;
    private final ComponentMonitor delegate;

    public WriterComponentMonitor(Writer out) {
        this(out, NullComponentMonitor.getInstance());
    }

    public WriterComponentMonitor(Writer out, ComponentMonitor delegate) {
        this.out = new PrintWriter(out);
        this.delegate = delegate;
    }

    public Constructor instantiating(PicoContainer container, ComponentAdapter componentAdapter,
                                     Constructor constructor) {
        out.println(format(ComponentMonitorHelper.INSTANTIATING, ComponentMonitorHelper.toString(constructor)));
        return delegate.instantiating(container, componentAdapter, constructor);
    }

    public void instantiated(PicoContainer container, ComponentAdapter componentAdapter,
                             Constructor constructor,
                             Object instantiated,
                             Object[] injected,
                             long duration) {
        out.println(format(ComponentMonitorHelper.INSTANTIATED, ComponentMonitorHelper.toString(constructor), duration, instantiated.getClass().getName(), ComponentMonitorHelper.toString(injected)));
        delegate.instantiated(container, componentAdapter, constructor, instantiated, injected, duration);
    }

    public void instantiationFailed(PicoContainer container,
                                    ComponentAdapter componentAdapter,
                                    Constructor constructor,
                                    Exception cause) {
        out.println(format(ComponentMonitorHelper.INSTANTIATION_FAILED, ComponentMonitorHelper.toString(constructor), cause.getMessage()));
        delegate.instantiationFailed(container, null, constructor, cause);
    }

    public void invoking(PicoContainer container,
                         ComponentAdapter componentAdapter,
                         Member member,
                         Object instance) {
        out.println(format(ComponentMonitorHelper.INVOKING, ComponentMonitorHelper.toString(member), instance));
        delegate.invoking(container, componentAdapter, member, instance);
    }

    public void invoked(PicoContainer container,
                        ComponentAdapter componentAdapter,
                        Method method,
                        Object instance,
                        long duration) {
        out.println(format(ComponentMonitorHelper.INVOKED, ComponentMonitorHelper.toString(method), instance, duration));
        delegate.invoked(container, componentAdapter, method, instance, duration);
    }

    public void invocationFailed(Member member, Object instance, Exception cause) {
        out.println(format(ComponentMonitorHelper.INVOCATION_FAILED, ComponentMonitorHelper.toString(member), instance, cause.getMessage()));
        delegate.invocationFailed(member, instance, cause);
    }

    public void lifecycleInvocationFailed(MutablePicoContainer container,
                                          ComponentAdapter componentAdapter, Method method,
                                          Object instance,
                                          RuntimeException cause) {
        out.println(format(ComponentMonitorHelper.LIFECYCLE_INVOCATION_FAILED, ComponentMonitorHelper.toString(method), instance, cause.getMessage()));
        delegate.lifecycleInvocationFailed(container, componentAdapter, method, instance, cause);
    }

    public Object noComponentFound(MutablePicoContainer container, Object componentKey) {
        out.println(format(ComponentMonitorHelper.NO_COMPONENT, componentKey));
        return delegate.noComponentFound(container, componentKey);
    }
}
