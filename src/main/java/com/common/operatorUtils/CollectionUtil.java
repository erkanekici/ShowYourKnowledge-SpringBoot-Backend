package com.common.operatorUtils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionUtil {

    public static <T> List<T> getPageOfList(List<T> sourceList, int page, int pageSize) {
        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (sourceList == null || sourceList.size() < fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    public static <T> int getSizeWithNullCheck(List<T> list) {
        int size = 0;
        if (list != null) {
            size = list.size();
        }
        return size;
    }

    public static List<Long> convertBigIntegerListToLongList(List<BigInteger> bigIntegerList) {
        if (bigIntegerList != null) {
            return bigIntegerList.stream().mapToLong(BigInteger::longValue).boxed().collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
