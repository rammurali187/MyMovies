package com.example.mymovies.utility;

import java.util.Collection;


public final class Lists {

    public static <E> boolean isEmpty(Collection<E> list) {
        return (list == null || list.size() == 0);
    }

}
