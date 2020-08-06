package com.bdp.demo.java8.lambda;

public interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
