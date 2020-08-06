package com.bdp.demo.java8.stream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.stream.Stream;

//@SpringBootTest(classes = )
@ContextConfiguration
public class stream2Test {
    @Test
    public void streamTest() {
        Stream<String> d2 = Stream.of("a", "b", "c", "d", "e");
//        d2.forEach(s -> System.out.println("forEach: " + s));
        Stream<String> sorted = d2.sorted((s1, s2) -> {
            System.out.printf("sort: %s; %s\n", s1, s2);
            return s1.compareTo(s2);
        });
        Stream<String> a = sorted.filter(s -> {
            System.out.println("filter: " + s);
            return s.startsWith("a");
        });
        Stream<String> stringStream = a.map(s -> {
            System.out.println("map: " + s);
            return s.toUpperCase();
        });
        stringStream.forEach(s -> System.out.println("forEach: " + s));
    }
}