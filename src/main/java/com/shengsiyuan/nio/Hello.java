package com.shengsiyuan.nio;



class A {
    public String show(D obj) {
        return "A and D";
    }

    public String show(A obj) {
        return "A and A";
    }
}

class B extends A {
    public String show(B obj) {
        return ("B and B");
    }

    public String show(A obj) {
        return ("B and A");
    }
}

class C extends B {

}

class D extends B{

}

public class Hello extends B {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new B();
        B b = new B();
        C c = new C();
        D d = new D();

        System.out.println(a1.show(b)); // aa
        System.out.println(a1.show(c)); // aa
        System.out.println(a1.show(d)); // ad

        System.out.println(a2.show(b)); // ba
        System.out.println(a2.show(c)); // ba
        System.out.println(a2.show(d)); // ad

        System.out.println(b.show(b)); // bb
        System.out.println(b.show(c)); // bb
        System.out.println(b.show(d)); // ad

    }
}


