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

package com.trigonic.utils.slf4j.ndc.annotation;

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Annotation for arguments of methods annotated with {@link NDC}, this adds parameters to the diagnostic
 * context that gets pushed.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface NDCParam {
    /**
     * Specifies the name to be output for this parameter in the NDC.  If empty, {@link #value()} is used.
     */
    String name() default ""
    
    /**
     * Expression containing the value of this parameter.
     */
    String value()
}
