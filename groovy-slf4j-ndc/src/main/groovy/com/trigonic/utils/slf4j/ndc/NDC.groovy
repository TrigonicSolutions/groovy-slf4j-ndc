/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trigonic.utils.slf4j.ndc

import java.util.LinkedList
import java.util.List
import java.util.Map

import org.slf4j.MDC;

/**
 * Implement nested diagnostic context (NDC) behavior for slf4j using a mapped diagnostic context ({@link MDC}). Since the
 * MDC is used, each effective NDC is identified by a key in the MDC. 
 */
class NDC {
    static StackThreadLocal stackThreadLocal = new StackThreadLocal()
    
    /**
     * Pushes a message onto the identified NDC.
     */
    static void push(String key, String message) {
        stackThreadLocal.getStack(key).push(message)
        MDC.put(key, message)
    }
    
    /**
     * Pops the current message off the identified NDC.  If messages remain on the stack, the next one is used.  Otherwise,
     * the key is removed from the MDC.
     */
    static pop(String key) {
        MDC.put(key, stackThreadLocal.getStack(key).pop())
        def current = get(key)
        if (current == null) {
            MDC.remove(key)
        } else {
            MDC.put(key, current)
        }
    }
    
    /**
     * Retrieves the current message for the identified NDC, or null if there are none present.
     */
    static get(String key) {
        def stack = stackThreadLocal.getStack(key)
        stack.empty ? null : stack[-1]
    }
    
    /**
     * Helper {@link ThreadLocal} subclass for interacting with a thread-local stack.
     */
    static class StackThreadLocal extends ThreadLocal<Map<String, List<String>>> {
        @Override
        Map<String, List<String>> initialValue() {
            new TreeMap<String, List<String>>() {
                List<String> get(key) {
                    // if no value for this key, create an empty list and return that
                    List<String> result = super.get(key)
                    if (result == null) {
                        result = []
                        put(key, result)
                    } 
                    result
                }
            }
        }
        
        /**
         * Shorthand for calling {@link ThreadLocal#get()} then {@link Map#get(Object)} with the
         * specified key.
         */
        List<String> getStack(String key) {
            get().get(key)
        }
    }
}
