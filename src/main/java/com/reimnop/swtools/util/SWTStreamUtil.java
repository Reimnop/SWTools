package com.reimnop.swtools.util;

import java.util.stream.Stream;

public class SWTStreamUtil {
    public static <T> Iterable<T> wrapStream(Stream<T> stream) {
        return stream::iterator;
    }
}
