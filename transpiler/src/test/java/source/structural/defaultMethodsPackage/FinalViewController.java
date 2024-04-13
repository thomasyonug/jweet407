package source.structural.defaultMethodsPackage;

public class FinalViewController extends IntermediateAbstractViewController implements I5 {

    @Override
    public void o2(boolean b, CC1 cc1, II1 ii1) {
        System.out.println("o2-overriden");
    }

    @Override
    public void o3(boolean b) {
        System.out.println("o3-sub");
    }

}