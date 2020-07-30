/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 		Copyright 2020 illuminator3 aka Jonas Hardt

 		Licensed under the Apache License, Version 2.0 (the "License");
 		you may not use this file except in compliance with the License.
 		You may obtain a copy of the License at

 		   http://www.apache.org/licenses/LICENSE-2.0

 		Unless required by applicable law or agreed to in writing, software
 		distributed under the License is distributed on an "AS IS" BASIS,
 		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 		See the License for the specific language governing permissions and
 		limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package me.illuminator3.extendedjda.events

import groovyjarjarantlr4.v4.runtime.misc.MultiMap
import me.illuminator3.extendedjda.events.filters.EventFilter
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent

import java.lang.reflect.Method
import java.util.function.Predicate

@SuppressWarnings("unused" /* API */)
final
class EventManager
    implements net.dv8tion.jda.api.hooks.EventListener
{
    private static final List<EventListener>                                    REGISTERED_LISTENERS            = new ArrayList<>()
    private static final Map<Class<? extends GenericEvent>, Method>             EVENT_HANDLERS                  = new MultiMap<>() as Map<Class<? extends GenericEvent>, Method>
    private static final List<EventFilter>                                      REGISTERED_FILTERS              = new ArrayList<>()
                                                                                                                    // error; similiar to https://youtrack.jetbrains.com/issue/IDEA-228890
    private static final Predicate<Method>                                      METHOD_PREDICATE                = { method -> method = method as Method; method.getParameters().length == 1 && GenericEvent.class.isAssignableFrom(method.getParameters()[0].getType()) && method.getAnnotations().length >= 1 && method.getAnnotations().toList().stream().anyMatch { annotation -> annotation.annotationType() == Event.class }}
    private static final EventManager                                           $THIS                           = new EventManager()

    private static boolean                                                      REGISTERED                      = false

    static
    void registerFilter(EventFilter filter, JDA jda)
        throws UnsupportedOperationException
    {
        if (REGISTERED_FILTERS.contains(filter)) throw new UnsupportedOperationException("Filter is already registered")

        if (!REGISTERED)
        {
            REGISTERED = true

            jda.addEventListener($THIS)
        }

        REGISTERED_FILTERS.add(filter)
    }

    static
    void registerListener(EventListener listener, JDA jda)
        throws UnsupportedOperationException
    {
        if (REGISTERED_LISTENERS.contains(listener)) throw new UnsupportedOperationException("Listener is already registered")

        if (!REGISTERED)
        {
            REGISTERED = true

            jda.addEventListener($THIS)
        }

        def map = new HashMap<Class<? extends GenericEvent>, Method>()

        listener.getClass().getMethods().toList().each { method ->
            if (METHOD_PREDICATE.test(method))
                map.put(method.getParameters()[0].getType() as Class<? extends GenericEvent>, method)
        }

        map.sort { k1, k2 ->
            return (k1.getValue() as Method).getAnnotation(Event.class).priority() - (k2.getValue() as Method).getAnnotation(Event.class).priority()
        }

        EVENT_HANDLERS.putAll(map)
    }

    private static
    void handle(GenericEvent event)
    {
        def clazz = event.getClass() as Class<? extends GenericEvent>

        if (EVENT_HANDLERS.containsKey(clazz))
        {
            def handlers = EVENT_HANDLERS.get(clazz)

            handlers.each { method ->
                def cancel = false

                REGISTERED_FILTERS.each { filter ->
                    if (filter.filter(event, method.getDeclaringClass().newInstance() as EventListener)) cancel = true
                }

                if (cancel) return

                method.setAccessible(true)

                method.invoke(method.getDeclaringClass().newInstance(), event)
            }
        }
    }

    static
    void callEvent(GenericEvent event)
    {
        handle(event)
    }

    @Override
    void onEvent(GenericEvent genericEvent)
    {
        callEvent(genericEvent)
    }
}