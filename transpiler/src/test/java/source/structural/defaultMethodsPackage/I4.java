package source.structural.defaultMethodsPackage;

public interface I4 {

    default void m4() {
        System.out.println("m4");
    }

    void o3(boolean b);
}