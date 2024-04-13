package org.jsweet.test.transpiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsweet.test.transpiler.util.TranspilerTestRunner;
import org.jsweet.transpiler.JSweetContext;
import org.jsweet.transpiler.JSweetFactory;
import org.jsweet.transpiler.ModuleKind;
import org.jsweet.transpiler.extension.PrinterAdapter;
import org.jsweet.transpiler.extension.RemoveJavaDependenciesAdapter;
import org.jsweet.transpiler.extension.RemoveJavaDependenciesES6Adapter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import source.nativestructures.ArraysSort;
import source.nativestructures.Collections;
import source.nativestructures.Dates;
import source.nativestructures.ES6Maps;
import source.nativestructures.ES6Sets;
import source.nativestructures.Exceptions;
import source.nativestructures.ExtendsJDK;
import source.nativestructures.ExtendsJDKAnonymous;
import source.nativestructures.ExtendsJDKInterface;
import source.nativestructures.ExtendsJDKRegular;
import source.nativestructures.Input;
import source.nativestructures.Iterators;
import source.nativestructures.Lists;
import source.nativestructures.MapExtended;
import source.nativestructures.Maps;
import source.nativestructures.NativeArrays;
import source.nativestructures.NativeStringBuilder;
import source.nativestructures.NativeSystem;
import source.nativestructures.Numbers;
import source.nativestructures.ObjectMaps;
import source.nativestructures.OverloadWithNative;
import source.nativestructures.Properties;
import source.nativestructures.Reflect;
import source.nativestructures.Sets;
import source.nativestructures.Strings;
import source.nativestructures.WeakReferences;

public class NativeStructuresTests extends AbstractTest {

    @Test
    public void testArraysSort() {
        TranspilerTestRunner transpilerTest = new TranspilerTestRunner(getCurrentTestOutDir(), new JSweetFactory() {
            @Override
            public PrinterAdapter createAdapter(JSweetContext context) {
                return new RemoveJavaDependenciesAdapter(super.createAdapter(context));
            }
        });

        transpilerTest.eval((logHandler, r) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ArraysSort.class));
    }

    @Test
    public void testCollections() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("1,a,1,b,3,4,d,a,d,0,0,0,a,a,2,a,true,false,3,c,c,a,b,c,a,b,c,b,1,c,b,a,b,c,a,0,"
                    + "true,true,it,true,1,array[a,b,c],0,false,true,array[a,b,c],array[c,b,a],listAfterReverse=[c, b, a],[aa, bb, cc],-3,-3,cc,[aa, bb],array[a,a,a],array[a,b,a],"
                    + "listAfterAddAll=[a, d, e, b, c],"
            // queues
                    + "false,[c, a, b],c,[a, b],b,[a],a,null,true,null,"
            // removeAll, retainAll, containsAll, disjoint
                    + "[a, b, c],[d, e, f],false,list.containsAll(list2)=true,false,true," //
                    + "[nyan, nyan, nyan, nyan, nyan, nyan, nyan]," //
            // arrayList copy constructor
                    + "a2.size=2,a2[0]=foo,a2[1]=bar", //
                    result.get("trace"));
        }, getSourceFile(Collections.class));
    }

    @Test
    public void testLists() {
        eval((logHandler, result) -> {
            logHandler.assertNoProblems();
            assertEquals("LinkedList___first__value", result.get("LinkedList_peekFirst"));
            assertEquals("LinkedList___third__value", result.get("LinkedList_peekLast"));
        }, getSourceFile(Lists.class));
    }

    @Test
    public void testSets() {
        eval((logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(Sets.class));
    }

    @Test
    public void testES6Sets() {
        TranspilerTestRunner transpilerTest = new TranspilerTestRunner(getCurrentTestOutDir(), new JSweetFactory() {
            @Override
            public PrinterAdapter createAdapter(JSweetContext context) {
                return new RemoveJavaDependenciesES6Adapter(super.createAdapter(context));
            }
        });
        transpilerTest.eval((logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ES6Sets.class));
    }

    @Test
    public void testES6Maps() {
        TranspilerTestRunner transpilerTest = new TranspilerTestRunner(getCurrentTestOutDir(), new JSweetFactory() {
            @Override
            public PrinterAdapter createAdapter(JSweetContext context) {
                return new RemoveJavaDependenciesES6Adapter(super.createAdapter(context));
            }
        });
        transpilerTest.eval((logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ES6Maps.class));
    }

    @Test
    public void testStringBuilder() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("a,abc,2,a,abc,ab,X,tEst,E,4,tst,tt,:qqqq,:aaaaqqqq,aaqqqq,aaqq", result.get("trace"));
        }, getSourceFile(NativeStringBuilder.class));
    }

    @Test
    public void testWeakReferences() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("test,1", result.get("trace"));
        }, getSourceFile(WeakReferences.class));
    }

    @Test
    public void testOverloadWithNative() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("1,2," + //
            "overloadWithClass0:10,overloadWithClass1:foo", result.get("trace"));
        }, getSourceFile(OverloadWithNative.class));
    }

    @Test
    public void testExceptions() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("test,test,finally,test2,test3", result.get("trace"));
        }, getSourceFile(Exceptions.class));
    }

    @Test
    public void testMaps() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("1,a,2,b,2,a,true,[1, 2],[a, b],a,1,true,size2=2,1,2,[],empty=true,-null-,1,a,2,b",
                    result.get("trace"));
        }, getSourceFile(Maps.class));
    }

    @Test
    public void testMapExtended() {
        eval((logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(MapExtended.class));
    }

    @Test
    public void testProperties() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals(
                    "1,a,2,b,2,a,true,[1, 2],[1, 2],isEmpty=false,[a, b],1,true,1,2,size=1,unknown=null,size=2,[],size=0,isEmpty=true,size2=2,isEmpty2=false,-null-",
                    result.get("trace"));
        }, getSourceFile(Properties.class));
    }

    @Test
    public void testObjectMaps() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("1,a,2,b,2,a,true,[1, 2],[a, b],1,true,1,2,[],-null-", result.get("trace"));
        }, getSourceFile(ObjectMaps.class));
    }

    @Test
    public void testNativeArrays() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("3,a,b,c,true,false,false,false,true", result.get("trace"));
        }, getSourceFile(NativeArrays.class));
    }

    @Test
    public void testSystem() {
        eval((logHandler, result) -> {
            assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("true,true", result.get("trace"));
            assertTrue(result.get("nanoTime"));
        }, getSourceFile(NativeSystem.class));
    }

    @Test
    public void testDates() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("true,114937200000,1973,7,23,7,0,0", result.get("trace"));
        }, getSourceFile(Dates.class));
    }

    @Test
    public void testInput() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("t,h,s,t", result.get("trace"));
        }, getSourceFile(Input.class));
    }

    @Test
    public void testReflect() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals("constructor", result.get("trace"));
        }, getSourceFile(Reflect.class));
    }

    @Test
    public void testNumbers() {
        eval((logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals(">,1", result.get("trace"));
        }, getSourceFile(Numbers.class));
    }

    @Test
    public void testStrings() {
        eval(ModuleKind.none, (logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals(">,0,ABC,abc,abcd,AB,b,Hello, I'm \"Renaud\",Hello, \"Renaud\"", result.get("trace"));
        }, getSourceFile(Strings.class));
        transpilerTest().getTranspiler().setUseSingleQuotesForStringLiterals(true);
        eval(ModuleKind.none, (logHandler, result) -> {
            Assert.assertEquals("There should be no errors", 0, logHandler.reportedProblems.size());
            assertEquals(">,0,ABC,abc,abcd,AB,b,Hello, I'm \"Renaud\",Hello, \"Renaud\"", result.get("trace"));
        }, getSourceFile(Strings.class));
        transpilerTest().getTranspiler().setUseSingleQuotesForStringLiterals(false);
    }

    @Ignore
    @Test
    public void testExtendsJDK() {
        // TODO: fix extension with non-static inner classes
        eval((logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ExtendsJDK.class));
    }

    @Ignore
    @Test
    public void testExtendsJDKRegular() {
        eval((logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ExtendsJDKRegular.class));
    }

    @Test
    public void testExtendsJDKInterface() {
        eval(ModuleKind.none, (logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ExtendsJDKInterface.class));
    }

    @Ignore
    @Test
    public void testExtendsJDKAnonyous() {
        // TODO: fix extension with non-static inner classes
        eval((logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(ExtendsJDKAnonymous.class));
    }

    @Test
    public void testIterators() {
        eval(ModuleKind.none, (logHandler, result) -> {
            logHandler.assertNoProblems();
        }, getSourceFile(Iterators.class));
    }

}