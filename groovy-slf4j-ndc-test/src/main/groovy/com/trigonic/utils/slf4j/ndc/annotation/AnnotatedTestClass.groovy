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

import static org.junit.Assert.assertEquals

class AnnotatedTestClass {
    static void checkNDC(String expectedValue, String key) {
        assertEquals(expectedValue, com.trigonic.utils.slf4j.ndc.NDC.get(key))
    }

    @NDC(key='ndc')
    void myInstanceMethod() {
        checkNDC('AnnotatedTestClass.myInstanceMethod', 'ndc');
    }

    @NDC(key='ndc')
    static void myStaticMethod() {
        checkNDC('AnnotatedTestClass.myStaticMethod', 'ndc');
    }

    @NDC(key='ndc', baseMessage='message')
    static void myMethodWithBaseMessage() {
        checkNDC('message', 'ndc');
    }

    @NDC(key='ndc', params=[
        @NDCParam('name'),
        @NDCParam('number')
    ])
    static void myMethodWithSimpleParameters(String name, int number) {
        checkNDC("AnnotatedTestClass.myMethodWithSimpleParameters name=${name} number=${number}", 'ndc');
    }

    @NDC(key='ndc', params=[
        @NDCParam(name='name', value='bean.name'),
        @NDCParam(name='number', value='bean.number')
    ])
    static void myMethodWithComplexParameters(bean) {
        checkNDC("AnnotatedTestClass.myMethodWithComplexParameters name=${bean.name} number=${bean.number}", 'ndc');
    }
}
