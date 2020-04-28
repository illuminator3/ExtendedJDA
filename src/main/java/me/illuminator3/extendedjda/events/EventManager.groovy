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

import me.illuminator3.extendedjda.events.filters.EventFilter
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent

import java.lang.reflect.Method

@SuppressWarnings("unused" /* API */)
final
class EventManager
    implements net.dv8tion.jda.api.hooks.EventListener
{
    private static final List<EventListener>                                    REGISTERED_LISTENERS            = new ArrayList<>()
    private static final Map<Class<? extends GenericEvent>, Method>             EVENT_HANDLERS                  = new HashMap<>()
    private static final List<EventFilter>                                      REGISTERED_FILTERS              = new ArrayList<>()

    private static boolean                                                      init                            = false

    static
    void init(final JDA jda)
    {
        if (init) throw new UnsupportedOperationException("Already initialized")

        init = true

        jda.addEventListener(this)
    }

    static
    void registerFilter(final EventFilter filter)
        throws UnsupportedOperationException
    {
        if (REGISTERED_FILTERS.contains(filter)) throw new UnsupportedOperationException("Filter is already registered")

        REGISTERED_FILTERS.add(filter)
    }

    static
    void registerListener(final EventListener listener)
        throws UnsupportedOperationException
    {
        if (REGISTERED_LISTENERS.contains(listener)) throw new UnsupportedOperationException("Listener is already registered")

        def checker = new Object() {
            boolean check(final Method method)
            {
                return  method.getParameters().length == 1
                        &&
                        GenericEvent.class.isAssignableFrom(method.getParameters()[0].getType())
                        &&
                        method.getAnnotations().length >= 1
                        &&
                        method.getAnnotations().toList().stream().anyMatch { annotation ->
                            annotation.annotationType() == Event.class
                        }
            }
        }

        listener.getClass().getMethods().toList().each { method ->
            if (checker.check(method))
                EVENT_HANDLERS.put(method.getParameters()[0].getType() as Class<? extends GenericEvent>, method)
        }
    }

    private static
    void handle(final GenericEvent event)
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
    void callEvent(final GenericEvent event)
    {
        handle(event)
    }

    @Override
    void onEvent(GenericEvent genericEvent)
    {
        callEvent(genericEvent)
    }
}