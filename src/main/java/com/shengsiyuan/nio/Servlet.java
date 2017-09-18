package com.shengsiyuan.nio;

/**
 * Created by ququ1 on 2017/9/15.
 */
public class Servlet {

    public void doGet(String str) {
        new Demo().demo(str);
    }


}

class Demo {
    void demo(String str) {
        new Test().test(str);
    }
}

class Test {
    void test(String str) {
        System.out.println(str);
    }
}


