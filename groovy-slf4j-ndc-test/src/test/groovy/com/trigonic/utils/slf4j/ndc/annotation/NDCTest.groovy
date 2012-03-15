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

package com.trigonic.utils.slf4j.ndc.annotation

import groovy.transform.Canonical

import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

class NDCTest {
    @Before
    void initiallyNullNDC() {
        AnnotatedTestClass.checkNDC(null, 'ndc')
    }

    @After
    void subsequentlyNullNDC() {
        AnnotatedTestClass.checkNDC(null, 'ndc')
    }
    
    ///// TESTS /////

    @Test
    void instanceMethod() {
        new AnnotatedTestClass().myInstanceMethod()
    }

    @Test
    void staticMethod() {
        AnnotatedTestClass.myStaticMethod()
    }

    @Test
    void withBaseMessage() {
        AnnotatedTestClass.myMethodWithBaseMessage()
    }

    @Test
    void withSimpleParameters() {
        AnnotatedTestClass.myMethodWithSimpleParameters('TheAnswer', 42)
    }

    @Test
    void withComplexParameters() {
        AnnotatedTestClass.myMethodWithComplexParameters(new Bean(name: 'TheAnswer', number: 42))
    }

    ///// SUPPORT /////

    @Canonical
    class Bean {
        String name
        Integer number
    }

    public static void main(String[] args) {
        def clazz = new GroovyClassLoader().parseClass("""
   
        @groovy.util.logging.Slf4j
        class Howdy { 
            @com.trigonic.utils.slf4j.ndc.annotation.NDC(key='ndc', params=[
                @com.trigonic.utils.slf4j.ndc.annotation.NDCParam('one'),
                @com.trigonic.utils.slf4j.ndc.annotation.NDCParam('two')
            ])
            static run(one, two) { 
                log.info 'howdy!'; 
            } 
        } 
        """)

        clazz.run(1, 2)
    }
}
