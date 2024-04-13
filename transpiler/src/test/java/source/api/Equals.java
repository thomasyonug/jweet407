package source.api;

import static jsweet.util.Lang.$export;

import def.js.Array;

public class Equals {

    static Array<String> trace = new Array<String>();

    static Integer int1 = 0;
    static Integer int2 = 0;
    static Integer int3 = 0;
    static Integer int4 = 0;
    static Integer int5 = 0;

    public static void main(String[] args) {
        trace.push("" + equals("a", "b"));
        trace.push("" + equals("a", "a"));
        trace.push("" + equals(1, 2));
        trace.push("" + equals(2, 2));
        trace.push("" + equals(new MyObject1("a"), new MyObject1("b")));
        trace.push("" + equals(new MyObject1("a"), new MyObject1("a")));
        trace.push("" + equals(new MyObject2("a"), new MyObject2("b")));
        trace.push("" + equals(new MyObject2("a"), new MyObject2("a")));
        MyObject2 o = new MyObject2("a");
        trace.push("" + equals(o, o));
        $export("trace", trace.join(","));
        // ensures that this kind of test works fine
        if (!int1.equals(int2 == int3 ? int4 : int5)) {
        }

        boolean areEqual = Equals.<MyObject2>testWithGenerics(o, o);
        $export("testWithGenerics0", areEqual);
        areEqual = Equals.<MyObject2>testWithGenerics(o, null);
        $export("testWithGenerics1", areEqual);
    }

    static <T> boolean testWithGenerics(T left, T right) {
        boolean equals = left.equals(right);
        return equals;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

}

class MyObject1 {

    String data;

    public MyObject1(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyObject1)) {
            return false;
        }
        return data.equals(((MyObject1) obj).data);
    }

}

class MyObject2 {

    String data;

    public MyObject2(String data) {
        this.data = data;
    }

}

class MyObject3 {

    MyInterface data;

    public MyObject3(MyInterface data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyObject3)) {
            return false;
        }
        return data.equals(((MyObject3) obj).data);
    }

}

interface MyInterface {

    void m();

}
