package com.shop_app.shared.validate;

import com.shop_app.shared.exceptions.IllegalArgumentException;
import lombok.NonNull;

import java.util.Collection;

final public class Validate {
    private Validate() {
    }

    /**
     * Check that the specified object reference is not null.
     *
     * @param obj the object reference to check for nullity
     * @param msg the message for exception
     * @param <T> IllegalAccessException if {@code obj} is {@code null}
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if{@code obj} is {@code null}
     */
    public static <T> T requiredNonNull(@NonNull T obj, String msg) {
        if (obj == null)
            throw new NullPointerException(msg);
        return obj;
    }

    /**
     * Ensure the given collection is neither null nor empty.
     *
     * @param coll the collection to check
     * @param msg  the message for exception
     * @throws IllegalArgumentException if collection is null or empty
     */
    public static <T extends Collection<?>> void notEmpty(T coll, String msg) {
        if (coll == null || coll.isEmpty())
            throw new IllegalArgumentException(msg);
    }
}
