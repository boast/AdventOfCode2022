package aoc.common;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class for collections
 */
public final class CollectionUtil {
    private CollectionUtil() {}
    
    /**
     * Partition a list into a list of lists.<br>
     * If the list size is not a multiple of the partition size, the last partition element will be smaller.
     * @param collection Collection to partition
     * @param partitionSize Size of each partition
     * @param <T> Type of the collection
     * @return List of partitions
     */
    public static <T> List<List<T>> partition(final List<T> collection, final int partitionSize)
    {
        return IntStream.rangeClosed(0, (collection.size() - 1) / partitionSize)
                        .mapToObj(i -> collection.subList(i * partitionSize,
                                                          Math.min((i + 1) * partitionSize, collection.size())))
                        .collect(Collectors.toList());
    }

}
