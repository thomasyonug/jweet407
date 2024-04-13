/* 
 * JSweet - http://www.jsweet.org
 * Copyright (C) 2015 CINCHEO SAS <renaud.pawlak@cincheo.fr>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jsweet.test.transpiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsweet.transpiler.JSweetProblem;
import org.jsweet.transpiler.ModuleKind;
import org.jsweet.transpiler.SourceFile;
import org.jsweet.transpiler.util.EvaluationResult;
import org.junit.Assert;
import org.junit.Test;

import source.syntax.AnnotationQualifiedNames;
import source.syntax.AsyncAwaitPropagation;
import source.syntax.Casts;
import source.syntax.DocComments;
import source.syntax.DynamicInvoke;
import source.syntax.FinalVariables;
import source.syntax.FinalVariablesRuntime;
import source.syntax.GlobalsCastMethod;
import source.syntax.GlobalsInvocation;
import source.syntax.Keywords;
import source.syntax.Labels;
import source.syntax.LambdaExpression;
import source.syntax.LambdasWithInterfaces;
import source.syntax.Literals;
import source.syntax.Looping;
import source.syntax.MemberReferences;
import source.syntax.QualifiedNames;
import source.syntax.References;
import source.syntax.SpecialFunctions;
import source.syntax.StatementsWithNoBlocks;
import source.syntax.SuperInvocation;
import source.syntax.UnreachableCode;
import source.syntax.ValidIndexedAccesses;
import source.syntax.function.KeywordWithPackage;

public class SyntaxTests extends AbstractTest {

    @Test
    public void testReferences() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            Assert.assertEquals("foo", r.get("s"));
            Assert.assertEquals((Number) 5, r.get("i"));
        }, getSourceFile(References.class));
    }

    @Test
    public void testKeywords() {
        eval((logHandler, r) -> {
            assertEquals(17, logHandler.reportedProblems.size());
            for (JSweetProblem problem : logHandler.reportedProblems) {
                assertEquals(JSweetProblem.JS_KEYWORD_CONFLICT, problem);
            }
            assertEquals("a,1,f,2,abc", r.get("trace"));
            assertEquals("otherForVarKeyword", r.get("varKeyword"));
            assertEquals("test3", r.get("with"));
        }, getSourceFile(KeywordWithPackage.class), getSourceFile(Keywords.class));
    }

    @Test
    public void testUnreachable() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            assertEquals("OUI", r.get("reachableExecuted"));
        }, getSourceFile(UnreachableCode.class));
    }

    @Test
    public void testStatementsWithNoBlocks() {
        eval((logHandler, result) -> {
            logHandler.assertNoProblems();
            assertEquals("aa,bb,0,1,2", result.get("trace"));
        }, getSourceFile(StatementsWithNoBlocks.class));
    }

    @Test
    public void testQualifiedNames() {
        transpile((logHandler) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(QualifiedNames.class));
    }

    @Test
    public void testAnnotationQualifiedNames() {
        transpile((logHandler) -> {
            Assert.assertEquals("Missing expected error", 1, logHandler.reportedProblems.size());
            Assert.assertEquals("Wrong type of expected error", JSweetProblem.INVALID_METHOD_BODY_IN_INTERFACE,
                    logHandler.reportedProblems.get(0));
        }, getSourceFile(AnnotationQualifiedNames.class));
    }

    @Test
    public void testGlobalsInvocation() {
        transpile((logHandler) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(GlobalsInvocation.class));
    }

    @Test
    public void testSpecialFunctions() {
        System.setProperty("jsweet.forceDeprecatedApplySupport", "true");
        transpile((logHandler) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(SpecialFunctions.class));
    }

    @Test
    public void testDeprecatedApply() {
        transpile((logHandler) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(SpecialFunctions.class));
    }

    @Test
    public void testLabels() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(Labels.class));
    }

    @Test
    public void testFinalVariables() {
        transpile((logHandler) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(FinalVariables.class));
    }

    @Test
    public void testConstVariables() throws IOException {
        SourceFile f1 = getSourceFile(FinalVariables.class);
        SourceFile f2 = getSourceFile(GlobalsInvocation.class);
        transpilerTest().transpile(logHandler -> {
            logHandler.assertNoProblems();
        }, f1, f2);
        String generatedCode = FileUtils.readFileToString(f1.getTsFile());
        Assert.assertTrue(generatedCode.contains("const explicitFinalString"));
        Assert.assertTrue(generatedCode.contains("let explicitFinalStringWithDeferredAssignment"));
        Assert.assertFalse(generatedCode.contains("const explicitFinalStringWithDeferredAssignment"));
        Assert.assertTrue(generatedCode.contains("const implicitFinalString"));
        Assert.assertTrue(generatedCode.contains("let notFinalString"));

        generatedCode = FileUtils.readFileToString(f2.getTsFile());
        Assert.assertTrue(generatedCode.contains("const explicitFinalGlobal"));
        Assert.assertTrue(generatedCode.contains("let implicitFinalGlobal"));
        Assert.assertTrue(generatedCode.contains("let notFinalGlobal"));
    }

    @Test
    public void testFinalVariablesRuntime() {
        try {
            TestTranspilationHandler logHandler = new TestTranspilationHandler();
            EvaluationResult r = transpilerTest().getTranspiler().eval("Java", logHandler,
                    getSourceFile(FinalVariablesRuntime.class));
            logHandler.assertNoProblems();
            Assert.assertEquals("Wrong behavior output trace", "11223344", r.get("out").toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occured while running test");
        }
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            Assert.assertEquals("Wrong behavior output trace", "11223344", r.get("out").toString());
        }, getSourceFile(FinalVariablesRuntime.class));

    }

    @Test
    public void testDynamicInvoke() {
        eval((logHandler, r) -> {
            assertEquals(true, r.get("a_1"));
            assertEquals(true, r.get("a"));
            assertEquals(true, r.get("b"));
            assertEquals(true, r.get("c"));
            assertEquals(true, r.get("d"));
            assertEquals(true, r.get("e"));
            assertEquals("5;true;foo", r.get("f"));
        }, getSourceFile(DynamicInvoke.class));
    }

    @Test
    public void testValidIndexedAccesses() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();

            assertEquals("value", r.get("field1"));
            assertNull(r.get("field2"));
            assertNull(r.get("field3"));
            assertEquals("value4", r.get("field4"));
            assertEquals("value5", r.get("field5"));
        }, getSourceFile(ValidIndexedAccesses.class));
    }

    @Test
    public void testGlobalCastMethod() {
        transpile((logHandler) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(GlobalsCastMethod.class));
    }

    @Test
    public void testDocComments() {
        SourceFile f = getSourceFile(DocComments.class);
        transpile(logHandler -> {
            assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            try {
                String generatedCode = FileUtils.readFileToString(f.getTsFile());
                assertTrue(generatedCode.contains("This is a test of comment."));
                assertTrue(generatedCode.contains("A method, which has some doc comment."));
                assertTrue(generatedCode.contains("This is a constant field."));
                assertTrue(generatedCode.contains("@param {string} s1 string 1"));
                assertTrue(generatedCode.contains("A constructor for C"));
                assertFalse(generatedCode.contains("A class comment to be erased"));
                assertTrue(generatedCode.contains("A class comment to be used"));
                assertTrue(generatedCode.contains("@param {string} sToBeDocumented"));
                assertTrue(generatedCode.contains("@param {string} base1"));
                assertTrue(generatedCode.contains("@param {number} base2"));
                assertFalse(generatedCode.contains("Sub overload."));
                assertTrue(generatedCode.contains("Main overload."));
                assertTrue(generatedCode.contains("@param {*} i is an interface"));
                assertTrue(generatedCode.contains("@param {number[]} aList"));
                if (transpilerTest().getTranspiler().getModuleKind() == ModuleKind.commonjs) {
                    assertTrue(Pattern.compile("\\* @property \\{E\\} XX_B\\s*\\* Test enum").matcher(generatedCode)
                            .find());
                    assertTrue(generatedCode.contains("* @property {E} XX_C"));
                } else {
                    assertTrue(Pattern.compile("\\* @property \\{source\\.syntax\\.E\\} XX_B\\s*\\* Test enum")
                            .matcher(generatedCode).find());
                    assertTrue(generatedCode.contains("* @property {source.syntax.E} XX_C"));
                }
                assertTrue(generatedCode.contains("@author Renaud Pawlak"));
                assertTrue(generatedCode.contains("@author Notto Beerased"));
            } catch (Exception e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }, f);
    }

    @Test
    public void testLiterals() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            Assert.assertEquals(1, r.<Number>get("l"));
            Assert.assertEquals(1, r.<Number>get("f"));
            Assert.assertEquals("c'est l'été!", r.<String>get("s"));
            Assert.assertEquals("é", r.<String>get("c"));
        }, getSourceFile(Literals.class));
    }

    @Test
    public void testLooping() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(Looping.class));
    }

    @Test
    public void testLambdasWithInterfaces() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            assertEquals("ok1,ok2,ok3,ok4,ok5", r.get("trace"));
        }, getSourceFile(LambdasWithInterfaces.class));
    }

    @Test
    public void testCasts() {
        transpile((logHandler) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(Casts.class));
    }

    @Test
    public void testSuperInvocation() {
        transpile(logHandler -> logHandler.assertNoProblems(), getSourceFile(SuperInvocation.class));
    }

    @Test
    public void testLambdaExpression() {
        transpile((logHandler) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(LambdaExpression.class));
    }

    @Test
    public void testMemberReferences() {
        transpile(TestTranspilationHandler::assertNoProblems, getSourceFile(MemberReferences.class));
    }

    @Test
    public void testAsyncAwaitPropagation() {
        transpilerTest().getTranspiler().setAutoPropagateAsyncAwaits(true);
        transpile(ModuleKind.none, logHandler -> {
            logHandler.assertNoProblems();
        }, getSourceFile(AsyncAwaitPropagation.class));
        transpilerTest().getTranspiler().setAutoPropagateAsyncAwaits(false);
    }
}
