package edu.jcourse.mapper;

public interface Mapper<F, T> {

    T mapFrom(F f);
}
