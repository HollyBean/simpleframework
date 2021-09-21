package org.simpleframework.util;

import java.util.Collection;

public class ValidateUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
