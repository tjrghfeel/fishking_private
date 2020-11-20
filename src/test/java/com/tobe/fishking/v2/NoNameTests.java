package com.tobe.fishking.v2;

import org.junit.jupiter.api.Test;

public class NoNameTests {

    @Test
    void noNameTest(){
        System.out.println(String.format("%03d",(int)(Math.random()*1000)));
    }
}
