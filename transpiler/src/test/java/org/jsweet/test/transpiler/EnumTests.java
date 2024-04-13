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

import org.jsweet.test.transpiler.util.TranspilerTestRunner;
import org.jsweet.transpiler.JSweetContext;
import org.jsweet.transpiler.JSweetFactory;
import org.jsweet.transpiler.ModuleKind;
import org.jsweet.transpiler.extension.Java2TypeScriptAdapter;
import org.jsweet.transpiler.extension.PrinterAdapter;
import org.jsweet.transpiler.extension.StringEnumAdapter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import source.enums.ComplexEnumWithAbstractMethods;
import source.enums.ComplexEnums;
import source.enums.ComplexEnumsWithInterface;
import source.enums.ComplexInnerEnums;
import source.enums.EnumInSamePackage;
import source.enums.EnumWithPropOfSameType;
import source.enums.EnumWithStatics;
import source.enums.Enums;
import source.enums.EnumsImplementingInterfaces;
import source.enums.EnumsReflection;
import source.enums.ErasedEnum;
import source.enums.FailingEnums;
import source.enums.MyComplexEnum2;
import source.enums.PassingEnums;
import source.enums.RemovedStringEnums;
import source.enums.StringEnumType;
import source.enums.StringEnums;
import source.enums.SwitchWithEnumWrapper;
import source.enums.other.ComplexEnumsAccess;
import source.enums.other.EnumInOtherPackage;
import source.enums.other.EnumWrapper;
import source.enums.samepackage.VarbitCallerNotWorking;
import source.enums.samepackage.VarbitWrapper;
import source.enums.samepackage.Varbits;

public class EnumTests extends AbstractTest {

    class AddRootFactory extends JSweetFactory {
        @Override
        public Java2TypeScriptAdapter createAdapter(JSweetContext context) {
            return new Java2TypeScriptAdapter(super.createAdapter(context)) {
                {
                    context.addAnnotation("@Root", "source.enums");
                }
            };
        }
    }

    class EraseEnumFactory extends JSweetFactory {
        @Override
        public Java2TypeScriptAdapter createAdapter(JSweetContext context) {
            return new Java2TypeScriptAdapter(super.createAdapter(context)) {
                {
                    context.addAnnotation("@Erased", "source.enums.ErasedEnum");
                }
            };
        }
    }

    @Test
    public void testEnums() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            Assert.assertEquals(0, ((Number) r.get("value")).intValue());
            Assert.assertEquals("A", r.get("nameOfA"));
            Assert.assertEquals(0, ((Number) r.get("ordinalOfA")).intValue());
            Assert.assertEquals("A", r.get("valueOfA"));
            Assert.assertEquals(2, ((Number) r.get("valueOfC")).intValue());
            Assert.assertEquals("B", r.get("ref"));
            Assert.assertEquals("A", r.get("switch"));
            Assert.assertEquals(0, ((Number) r.get("compare1")).intValue());
            Assert.assertEquals(1, ((Number) r.get("compare2")).intValue());
        }, getSourceFile(EnumInSamePackage.class), getSourceFile(EnumInOtherPackage.class), getSourceFile(Enums.class));
    }

    @Test
    public void testComplexEnums() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            Assert.assertEquals(">static,2,--2--,ratio_2_1_5,true,true,true,true,2,2,0,true", r.get("trace"));
        }, getSourceFile(ComplexEnums.class));
    }

    @Test
    public void testEnumsReflection() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            Assert.assertEquals(">", r.get("trace"));
        }, getSourceFile(EnumsReflection.class));
    }

    @Test
    public void testComplexEnumsAccess() {
        eval(ModuleKind.commonjs, (logHandler, r) -> {
            logHandler.assertNoProblems();
            Assert.assertEquals(">static,2,--2--,ratio_2_1_5,true,true,true,true", r.get("trace2"));
            // Assert.assertEquals(">static,2,--2--,ratio_2_1_5,true,true,true,true,2,2",
            // r.get("trace2"));
        }, getSourceFile(MyComplexEnum2.class), getSourceFile(ComplexEnumsAccess.class));
    }

    @Test
    public void testComplexInnerEnums() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ComplexInnerEnums.class));
    }

    @Test
    public void testComplexEnumWithAbstractMethods() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            Assert.assertEquals(">ok1,ok2", r.get("trace"));
        }, getSourceFile(ComplexEnumWithAbstractMethods.class));
        transpilerTest().getTranspiler().setBundle(true);
        eval(ModuleKind.none, (logHandler, r) -> {
            assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            Assert.assertEquals(">ok1,ok2", r.get("trace"));
        }, getSourceFile(ComplexEnumWithAbstractMethods.class));
        transpilerTest().getTranspiler().setBundle(false);

        TranspilerTestRunner transpilerTest = new TranspilerTestRunner(getCurrentTestOutDir(), new AddRootFactory());
        transpilerTest.eval((logHandler, r) -> {
            assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            Assert.assertEquals(">ok1,ok2", r.get("trace"));
        }, getSourceFile(ComplexEnumWithAbstractMethods.class));
        transpilerTest.getTranspiler().setBundle(true);
        eval(ModuleKind.none, (logHandler, r) -> {
            assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            Assert.assertEquals(">ok1,ok2", r.get("trace"));
        }, getSourceFile(ComplexEnumWithAbstractMethods.class));
    }

    @Test
    public void testErasedEnum() {
        TranspilerTestRunner transpilerTest = new TranspilerTestRunner(getCurrentTestOutDir(), new EraseEnumFactory());
        transpilerTest.transpile(logHandler -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ErasedEnum.class));
    }

    @Test
    public void testComplexEnumsWithInterface() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ComplexEnumsWithInterface.class));
    }

    @Test
    public void testComplexEnumsWithStatics() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(EnumWithStatics.class));
    }

    @Test
    public void testRemovedStringEnums() {
        TranspilerTestRunner transpilerTest = new TranspilerTestRunner(getCurrentTestOutDir(), new JSweetFactory() {
            @Override
            public PrinterAdapter createAdapter(JSweetContext context) {
                return new StringEnumAdapter(super.createAdapter(context));
            }
        });
        transpilerTest.eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            assertEquals("A", r.get("value"));
        }, getSourceFile(RemovedStringEnums.class));
    }

    @Test
    public void testEnumsImplementingInterfaces() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(EnumsImplementingInterfaces.class));
    }

    @Test
    public void testEnumWithPropOfSameType() {
        transpile(logHandler -> {
            logHandler.assertNoProblems();
        }, getSourceFile(EnumWithPropOfSameType.class));
    }

    @Test
    @Ignore
    public void testFailingEnums() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(FailingEnums.class));
    }

    @Test
    public void testPassingEnums() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(PassingEnums.class));
    }

    @Test
    public void testSwitchWithEnumWrapper() {
        eval(ModuleKind.commonjs, (logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(EnumInOtherPackage.class), getSourceFile(EnumWrapper.class),
                getSourceFile(SwitchWithEnumWrapper.class));
    }

    @Test
    public void testStringEnums() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
            assertEquals("TEST3", r.get("value"));
            assertEquals("VAL2", r.get("value2"));
            assertEquals("V2", r.get("value2_getValue"));
        }, getSourceFile(StringEnumType.class), getSourceFile(StringEnums.class));
    }
    
    @Test
    public void testEnumWrapperSamePackage() {
        eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(Varbits.class), getSourceFile(VarbitWrapper.class), getSourceFile(VarbitCallerNotWorking.class));
    }
}
