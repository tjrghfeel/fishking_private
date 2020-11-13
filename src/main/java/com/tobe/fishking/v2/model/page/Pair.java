package com.tobe.fishking.v2.model.page;

import lombok.Getter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.ToString;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class Pair<T, U> {
    private T first;
    private U second;

    public static <T, U> Pair of(T first, U second) {
        return Pair.builder()
                .first(first)
                .second(second)
                .build();
    }
}