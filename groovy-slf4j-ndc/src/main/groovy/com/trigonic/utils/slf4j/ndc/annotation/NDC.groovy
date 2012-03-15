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

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import com.trigonic.utils.slf4j.ndc.ast.NDCASTTransformation;

/**
 * Groovy AST annotation that causes an annotated method to be wrapped with {@link com.trigonic.utils.slf4j.ndc.NDC#push(String)}
 * and {@link com.trigonic.utils.slf4j.ndc.NDC#pop()} calls.
 * 
 * @see NdcAstTransformation
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@GroovyASTTransformationClass(classes=[NDCASTTransformation.class])
public @interface NDC {
    
    /**
     * Specifies the MDC key to push/pop using.
     */
    String key()
    
    /**
     * Specifies the base message to use before parameters are appended.  If empty, this will default to
     * <tt><i>SimpleClassName</i>.<i>methodName</i></tt>.
     */
    String baseMessage() default ""
    
    /**
     * Specifies extra parameters for the pushed NDC.  These will be appended to the message in name=value form.
     */
    NDCParam[] params() default []
    
}
