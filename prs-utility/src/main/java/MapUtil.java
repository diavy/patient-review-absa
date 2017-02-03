import java.util.*;

/**
 * Created by Shawn on 8/30/16.
 */

public class MapUtil {
    public static <K> void addWeight(Map<K, Double> map, K key, double weight) {
        if (!map.containsKey(key)) map.put(key, 0.0);
        map.put(key, map.get(key) + weight);
    }


    public static <K> void addCount(Map<K, Integer> map, K key) {
        if (!map.containsKey(key)) map.put(key, 0);
        map.put(key, map.get(key) + 1);
    }

    public static void mergeWeight(Map<String, Double> to, Map<String, Double> from) {
        double val;
        for (String key : from.keySet()) {
            val = to.containsKey(key) ? to.get(key) + from.get(key) : from.get(key);
            to.put(key, val);
        }
    }

    public static void mergeCount(Map<String, Integer> to, Map<String, Integer> from) {
        int val;
        for (String key : from.keySet()) {
            val = to.containsKey(key) ? to.get(key) + from.get(key) : from.get(key);
            to.put(key, val);
        }
    }


    public static <K> List<K> getMaxValuedKeys(Map<K, Double> map) {
        List<K> keys = new ArrayList<K>();

        map = sortByValue(map, false);

        int cnt = 0;
        double val = 0;
        for (K key : map.keySet()) {
            if (cnt == 0) {
                val = map.get(key);
                keys.add(key);
            } else {
                if (val <= map.get(key))
                    keys.add(key);
                else
                    break;
            }
            cnt++;
        }

        return keys;
    }

    public static <K> List<K> getMinValuedKeys(Map<K, Double> map) {
        List<K> keys = new ArrayList<K>();

        map = sortByValue(map, true);

        int cnt = 0;
        double val = 0;
        for (K key : map.keySet()) {
            if (cnt == 0) {
                val = map.get(key);
                keys.add(key);
            } else {
                if (val >= map.get(key))
                    keys.add(key);
                else
                    break;
            }
            cnt++;
        }

        return keys;
    }


    public static <K extends Comparable<K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                return e1.getKey().compareTo(e2.getKey());
            }
        });
        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValue(Map<K, V> map) {
        return sortByValue(map, false);
    }

    /**
     * Returns a LinkedHashMap sorted in descending order by the Comparable values in the original map.
     */
    public static <K, V extends Comparable<V>> Map<K, V> sortByValue(Map<K, V> map, final boolean ascending) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                if (ascending) {
                    return (e1.getValue()).compareTo(e2.getValue());
                } else {
                    return -(e1.getValue()).compareTo(e2.getValue());
                }
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Returns a LinkedHashMap sorted in descending order by the Comparable values in the original map.
     */
    public static <K, V extends Comparable<V>> Map<K, V> sortByValue(Map<K, V> map, final Comparator<? super K> tieBreakComparator) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                int valueComparison = (e1.getValue()).compareTo(e2.getValue());
                return 0 == valueComparison ? tieBreakComparator.compare(e1.getKey(), e2.getKey()) : -valueComparison;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    /**
     * Returns a LinkedHashMap sorted in descending order by the Comparable values in the original map.
     */
    public static <K, V extends Number> Map<K, Double> normalize(Map<K, V> map) {
        Map<K, Double> res = new LinkedHashMap<K, Double>();

        double sum = 0.0;
        double val = 0.0;
        for (Number num : map.values()) sum += num.doubleValue();
        for (K key : map.keySet()) {
            val = map.get(key).doubleValue() / sum;
            res.put(key, val);
        }

        return res;
    }

    /**
     * Returns the entry with the highest value in the original map. Returns null if the original map is empty.
     */
    public static <K extends Comparable<? super K>, V extends Comparable<V>> Map<K, V> geTopMap(Map<K, V> map, int topN) {
        Map<K, V> topMap = new LinkedHashMap<K, V>();
        int cnt = 0;
        for (K key : map.keySet()) {
            topMap.put(key, map.get(key));
            if (++cnt >= topN) break;
        }
        return topMap;
    }

    /**
     * Returns the entry with the highest value in the original map. Returns null if the original map is empty.
     */
    public static <K, V extends Comparable<V>> Map<K, V> getSortedTopMap(Map<K, V> map, int topN) {
        Map<K, V> sorted = sortByValue(map, false);
        Map<K, V> top = new LinkedHashMap<K, V>();

        V v;
        int cnt = 0;
        for (K k : sorted.keySet()) {
            if (++cnt <= topN) {
                v = sorted.get(k);
                top.put(k, v);
            } else {
                break;
            }
        }

        return top;
    }


    /**
     * Returns the entry with the highest value in the original map. Returns null if the original map is empty.
     */
    public static <K, V extends Comparable<V>> Map<K, V> getSortedTopMap(Map<K, V> map, int topN, final Comparator<? super K> tieBreakComparator) {
        Map<K, V> sorted = sortByValue(map, tieBreakComparator);
        Map<K, V> top = new LinkedHashMap<K, V>();

        V v;
        int cnt = 0;
        for (K k : sorted.keySet()) {
            if (++cnt <= topN) {
                v = sorted.get(k);
                top.put(k, v);
            } else {
                break;
            }
        }

        return top;
    }

    /**
     * Returns the entry with the highest value in the original map. Returns null if the original map is empty.
     */
    public static <K extends Comparable<? super K>, V extends Comparable<V>> Map.Entry<K, V> getSortedFirstEntry(Map<K, V> map) {
        Map<K, V> sorted = sortByValue(map, false);
        Map.Entry<K, V> en = null;
        Iterator<Map.Entry<K, V>> it = sorted.entrySet().iterator();

        if (it.hasNext()) {
            en = it.next();
        }

        return en;
    }


    /**
     * Returns the entry with the highest value in the original map. Returns null if the original map is empty.
     */
    public static <K, V extends Comparable<V>> Map.Entry<K, V> getSortedFirstEntry(Map<K, V> map, final Comparator<? super K> tieBreakComparator) {
        Map<K, V> sorted = sortByValue(map, tieBreakComparator);
        Map.Entry<K, V> en = null;
        Iterator<Map.Entry<K, V>> it = sorted.entrySet().iterator();

        if (it.hasNext()) {
            en = it.next();
        }

        return en;
    }


    public static <K extends Comparable<? super K>, V extends Comparable<V>> K getSortedFirstEntryKey(Map<K, V> map) {
        Map.Entry<K, V> entry = getSortedFirstEntry(map);
        return null == entry ? null : entry.getKey();
    }


    public static <K, V extends Comparable<V>> K getSortedFirstEntryKey(Map<K, V> map, final Comparator<? super K> tieBreakComparator) {
        Map.Entry<K, V> entry = getSortedFirstEntry(map, tieBreakComparator);
        return null == entry ? null : entry.getKey();
    }


    public static Number getSortedFirstEntryValue(Map map) {
        Map.Entry entry = getSortedFirstEntry(map);
        return null == entry ? 0 : (Number) entry.getValue();
    }


    public static float getSortedFirstEntryFraction(Map map) {
        float sum = 0;
        for (Object count : map.values()) {
            sum += ((Number) count).floatValue();
        }
        for (Object key : map.keySet()) {
            float val = ((Number) map.get(key)).floatValue();
            map.put(key, val / sum);
        }

        return getSortedFirstEntryValue(map).floatValue();
    }

    /**
     * For when you have a Map of keys to sets of values.
     * Instead of checking if the Key exists and initializing if not,
     * just call getOrInitSet and it will initialize if it doesn't exist.
     *
     * @param map the map we are getting the set from
     * @param key the key for which we are getting/initializing
     * @param <K> the type of the keys in the map
     * @param <V> the type of the values in the map
     * @return the set of values for the given key, newly initialized set if none existed.
     */
    public static <K, V> Set<V> getOrInitSet(final K key, final Map<K, Set<V>> map) {
        Set<V> set = map.get(key);
        if (set == null) {
            set = new HashSet<V>();
            map.put(key, set);
        }

        return set;
    }
}


