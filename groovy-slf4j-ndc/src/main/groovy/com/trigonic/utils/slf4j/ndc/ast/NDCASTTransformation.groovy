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

package com.trigonic.utils.slf4j.ndc.ast

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.control.*
import org.codehaus.groovy.transform.*

@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class NDCASTTransformation extends AbstractASTTransformation {
    static final Class ANNOTATION_CLASS = com.trigonic.utils.slf4j.ndc.annotation.NDC.class
    static final ClassNode ANNOTATION_TYPE = ClassHelper.make(ANNOTATION_CLASS)
    
    static final Class NDC_CLASS = com.trigonic.utils.slf4j.ndc.NDC.class
    static final ClassNode NDC_TYPE = ClassHelper.make(NDC_CLASS)
    
    void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source)
        AnnotatedNode parentNode = nodes[1]
        AnnotationNode annotationNode = nodes[0]
        if (ANNOTATION_TYPE != annotationNode.classNode) return
        
        if (parentNode instanceof MethodNode) {
            MethodNode methodNode = (MethodNode) parentNode
            println "methodNode ${methodNode}"
            ClassNode classNode = methodNode.getDeclaringClass()
            Expression message = getMessage(annotationNode, methodNode)
            Expression key = annotationNode.getMember("key");

            ClassExpression typeExpression = new ClassExpression(NDC_TYPE)
            MethodCallExpression pushContext = new MethodCallExpression(typeExpression, "push", new ArgumentListExpression(key, message))
            MethodCallExpression popContext = new MethodCallExpression(typeExpression, "pop", new ArgumentListExpression(key))
            Statement originalCode = methodNode.getCode()

            BlockStatement newCode = new BlockStatement()
            newCode.addStatement(new ExpressionStatement(pushContext))
            newCode.addStatement(new TryCatchStatement(originalCode, new ExpressionStatement(popContext)))
            methodNode.setCode(newCode)
        }
    }
    
    Expression getMessage(AnnotationNode annotationNode, MethodNode methodNode) {
        // build a GString expression in string form
        String baseMessage = annotationNode.getMember("baseMessage")?.value ?:
            "${methodNode.declaringClass.nameWithoutPackage}.${methodNode.name}"
        StringBuilder result = new StringBuilder("\"${baseMessage}")
        ListExpression params = annotationNode.getMember("params")
        params?.expressions?.each { AnnotationConstantExpression annotationExpr ->
            AnnotationNode param = annotationExpr.value
            String value = param.getMember("value")?.value
            String name = param.getMember("name")?.value
            if (!name || name.empty) name = value
            String start = '${', end = '}'
            result.append(" ${name}=${start}${value}${end}")
        }
        result.append('"');
        println result
        
        // build AST from the above - the builder wraps the string in a block with a return, so extract expression from that
        List<ASTNode> nodes = new AstBuilder().buildFromString(result.toString())
        BlockStatement block = nodes[0]
        ReturnStatement ret = block.statements[0]
        return ret.expression;
    }
}
