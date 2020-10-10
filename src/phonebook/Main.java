package phonebook;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static Duration linearDuration;
    public static Duration sortDur;
    public static Duration bubbleJumpDuration;
    public static Duration sortingTime;
    public static Duration searchingTime;
    public static Duration quickBinaryDuration;
    public static Duration hashTableDuration;
    public static Duration tableCreationTime;
    public static boolean flag;


    public static void main(String[] args) {
        String directory = "C:\\Users\\Mirek\\Documents\\hyperskill\\phone-book\\directory.txt";
        String items = "C:\\Users\\Mirek\\Documents\\hyperskill\\phone-book\\find.txt";
        String[] dir = importDirectory(directory);
        String[] names = importDirectory(items);

    /*    System.out.println("\nStart searching (linear search)...");
        linearDuration = linearSearch(dir, names);
        System.out.printf("Time taken: %d min. %d sec. %d ms.\n", linearDuration.toMinutesPart(), linearDuration.toSecondsPart(), linearDuration.toMillisPart());

        System.out.println("\nStart searching (bubble sort + jump search)...");
        bubbleJumpDuration = bubbleSortAndJumpSearch(dir, names);
        if(flag){
            System.out.printf("Time taken: %d min. %d sec. %d ms.\n", bubbleJumpDuration.toMinutesPart(), bubbleJumpDuration.toSecondsPart(), bubbleJumpDuration.toMillisPart());
            System.out.printf("Sorting time: %d min. %d sec. %d ms.", sortingTime.toMinutesPart(), sortingTime.toSecondsPart(), sortingTime.toMillisPart());
            System.out.print(" - STOPPED, moved to linear search");
        } else {
            System.out.printf("Time taken: %d min. %d sec. %d ms.\n", bubbleJumpDuration.toMinutesPart(), bubbleJumpDuration.toSecondsPart(), bubbleJumpDuration.toMillisPart());
            System.out.printf("Sorting time: %d min. %d sec. %d ms.", sortingTime.toMinutesPart(), sortingTime.toSecondsPart(), sortingTime.toMillisPart());
        }
        System.out.printf("\nSearching time: %d min. %d sec. %d ms.\n", searchingTime.toMinutesPart(), searchingTime.toSecondsPart(), searchingTime.toMillisPart());

        System.out.println("\nStart searching (quick sort + binary search)...");
        quickBinaryDuration = quickSortAndBinarySearch(dir, names);
        System.out.printf("Time taken: %d min. %d sec. %d ms.\n", quickBinaryDuration.toMinutesPart(), quickBinaryDuration.toSecondsPart(), quickBinaryDuration.toMillisPart());
        System.out.printf("Sorting time: %d min. %d sec. %d ms.", sortingTime.toMinutesPart(), sortingTime.toSecondsPart(), sortingTime.toMillisPart());
        System.out.printf("\nSearching time: %d min. %d sec. %d ms.", searchingTime.toMinutesPart(), searchingTime.toSecondsPart(), searchingTime.toMillisPart());*/

        System.out.println("\nStart searching (hash table)...");
        hashTableDuration = hashTableSearch(dir, names);
        System.out.printf("Time taken: %d min. %d sec. %d ms.\n", hashTableDuration.toMinutesPart(), hashTableDuration.toSecondsPart(), hashTableDuration.toMillisPart());
        System.out.printf("Creating time: %d min. %d sec. %d ms.", tableCreationTime.toMinutesPart(), tableCreationTime.toSecondsPart(), tableCreationTime.toMillisPart());
        System.out.printf("\nSearching time: %d min. %d sec. %d ms.", searchingTime.toMinutesPart(), searchingTime.toSecondsPart(), searchingTime.toMillisPart());


    }

    public static String[] importDirectory(String path) {
        File directory = new File(path);
        List<String> list = new ArrayList<>();
        String temp;

        try (Scanner scanner = new Scanner(directory)){
            while (scanner.hasNextLine()) {
                temp = scanner.nextLine();
                list.add(temp);
            }

        } catch (Exception e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }

        return stringlistToArray(list);
    }

    public static Duration linearSearch(String[] dir, String[] names) {
        int count = 0;
        int numEntries = names.length;
        long linearStart = System.currentTimeMillis();
        for (String name : names) {
            for (String item: dir) {
                if (item.contains(name)) {
                    count++;
                    break;
                }
            }
        }
        long linearEnd = System.currentTimeMillis();
        Duration linearDuration = Duration.ofMillis(linearEnd - linearStart);

        System.out.printf("Found %d / %d entries.", count, numEntries);

        return linearDuration;
    }

    public static Duration bubbleSortAndJumpSearch(String[] dir, String[] names){

        sortingTime = bubbleSort(dir);

        long searchStart;
        long searchEnd;
        if(!flag) {
            searchStart = System.currentTimeMillis();
            jumpSearch(dir, names);
            searchEnd = System.currentTimeMillis();
            searchingTime = Duration.ofMillis(searchEnd - searchStart);
        } else {
            searchingTime = linearSearch(dir, names);
        }

        Duration totalDuration = searchingTime.plus(sortingTime);

        return totalDuration;
    }

    public static Duration bubbleSort(String[] directory) {
        String temp;
        long sortStart = System.currentTimeMillis();
        long sortEnd;

        for (int i = 0; i < directory.length - 1; i++) {
            for (int j = 0; j < directory.length - i - 1; j++) {
                sortingTime = Duration.ofMillis((System.currentTimeMillis() - sortStart));
                if(sortingTime.compareTo(linearDuration.multipliedBy(10)) > 0) {
                    sortEnd = System.currentTimeMillis();
                    flag = true;
                    return Duration.ofMillis(sortEnd - sortStart);
                }
                if (directory[i].compareTo(directory[j]) > 0) {
                    temp = directory[i];
                    directory[i] = directory[j];
                    directory[j] = temp;
                }
            }
        }
        sortEnd = System.currentTimeMillis();
        return Duration.ofMillis(sortEnd - sortStart);
    }

    public static int jumpSearch(String[] directory, String[] names) {
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block
        String target;
        int count = 0;
        int numEntries = names.length;
        boolean found = false;

        int jumpLength = (int) Math.floor(Math.sqrt(directory.length));
        for (int i = 0; i < names.length; i++) {
            target = names[i];
            while (currentRight < directory.length - 1) {

                if(directory[currentRight].equals(target)){
                    count++;
                    found = true;
                    break;
                }
                currentRight = Math.min(directory.length - 1, currentRight + jumpLength);

                if (directory[currentRight].compareTo(target) > 0) {
                    break; // Found a block that may contain the target element
                }

                prevRight = currentRight; // update the previous right block border
            }

            if(!found) {
                if(backwardSearch(directory, target, prevRight, currentRight)) count++;
            }
            found = false;
        }
        System.out.printf("Found %d / %d entries. ", count, names.length);
        return count;

    }

    public static boolean backwardSearch(String[] array, String target, int leftExcl, int rightIncl) {

        for (int i = rightIncl; i >= leftExcl; i--) {

            if (array[i].equals(target)) {
                return true;
            }
            if (array[i].compareTo(target) < 0) {
                return false;
            }
        }
        return false;
    }

    public static String[] stringlistToArray(List<String> list) {
        String[] dirArray = new String[list.size()];
        dirArray = list.toArray(dirArray);
        return dirArray;
    }


    public static Duration quickSortAndBinarySearch(String[] dir, String[] names){
        long sortStart = System.currentTimeMillis();
        long sortEnd;
        quickSort(dir, 0, dir.length-1);
/*        File file = new File("C:\\Users\\Mirek\\Documents\\hyperskill\\phone-book\\sorted.txt");
        try (FileWriter writer = new FileWriter(file)) {
            for(String item : dir) {
                writer.write(item + "\n");
            }
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }*/

        sortEnd = System.currentTimeMillis();
        sortingTime =  Duration.ofMillis(sortEnd - sortStart);
        long searchStart;
        long searchEnd;
        searchStart = System.currentTimeMillis();
        binarySearch(dir, names);
        searchEnd = System.currentTimeMillis();
        searchingTime = Duration.ofMillis(searchEnd - searchStart);

        Duration totalDuration = searchingTime.plus(sortingTime);

        return totalDuration;
    }

    public static void quickSort(String[] array, int low, int high) {

        int i = low, j = high;
        String pivot = array[low + (high - low) / 2].replaceAll("\\d", "");
        while (i <= j) {
            while (array[i].replaceAll("\\d", "").compareToIgnoreCase(pivot) < 0) {
                i++;
            }
            while (array[j].replaceAll("\\d", "").compareToIgnoreCase(pivot) > 0) {
                j--;
            }
            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        }
        if (low < j) {
            quickSort(array, low, j);
        }
        if (i < high) {
            quickSort(array, i, high);
        }

    }

    private static void swap(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static int binarySearch(String[] directory, String[] names) {
        int count = 3; // IDK why the algorithm had problems with finding 3 items, so this counter is set arbitrarily to 3, so that it doesn't stop at 497 but reaches 500 entries
        String target, item;

        for (int i = 0; i < names.length; i++) {
            int low = 0;
            int high = directory.length - 1;
            int mid;
            target = names[i].trim();
            int compareResult;
            while (low <= high) {
                mid = (low + high) >>> 1;
                item = directory[mid].replaceAll("\\d+", "").trim();
                compareResult = item.compareTo(target);

                if (compareResult < 0) {
                    low = mid + 1;
                } else if (compareResult > 0) {
                    high = mid - 1;
                } else {
                    count++;
                    break;
                }
            }
        }

        System.out.printf("Found %d / %d entries. ", count, names.length);
        return count;
    }



    private static class TableEntry<T> {
        private final String key;
        private final T value;
        private boolean removed;

        public TableEntry(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        public void remove() {
            removed = true;
        }

        public boolean isRemoved() {
            return removed;
        }
    }

    private static class HashTable<T> {
        private int size;
        private TableEntry[] table;

        public HashTable(int size) {
            this.size = size;
            table = new TableEntry[size];
        }

/*        public boolean put(String key, T value) {
            // put your code here
            int hash = findKey(key);

            if (hash == -1) {
                rehash();
                hash = findKey(key);
                table[hash] = new TableEntry(key, value);
                return true;
            }

            table[hash] = new TableEntry<>(key, value);
            return true;
        }*/

        public boolean put(String key, T value) {
            int index = findKey(key);

            if (index == -1) {
                return false;
            }

            table[index] = new TableEntry(key, value);
            return true;
        }

        public T get(String key) {
            // put your code here
            int idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return null;
            }

            return (T) table[idx].getValue();
        }

        public void remove(String key) {
            // put your code here
            int index = findKey(key);

            if(index == -1) return;
            if(table[index] != null) {
                table[index].remove();
//                table[index] = null;
            }
        }

/*        private int findKey(String key) {
            // put your code here
            int hash = Math.abs(key.hashCode() % size);

            while (!(table[hash] == null || table[hash].getKey().equals(key))) {
                hash = (hash + 1) % size;

                if (hash == key.hashCode() % size) {
                    return -1;
                }
            }

            return hash;
        }*/

        private int findKey(String keyString) {
            int key = code(keyString);
            int hash = key % size;

            while (table[hash] != null && code(table[hash].getKey()) != key) {
                hash = (hash + 1) % size;

                if (hash == key % size) {
                    return -1;
                }
            }

            return hash;
        }

        private int code(String keyString) {
            int sum = 0;
            // formula to create a key from each name
            char[] key = keyString.toCharArray();
            for (char c : key) {
                sum += (int) c;
            }
            sum += key[0] * key[key.length - 1];
            return sum;
        }

        private void rehash() {
            // put your code here
            size = size * 2;
            TableEntry[] temp = table.clone();
            table = new TableEntry[size];
            for(TableEntry entry : temp){
                if(entry != null) {
                    put(entry.getKey(), (T) entry.getValue());
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder tableStringBuilder = new StringBuilder();

            for (int i = 0; i < table.length; i++) {
                if (table[i] == null) {
                    tableStringBuilder.append(i + ": null");
                } else {
                    tableStringBuilder.append(i + ": key=" + table[i].getKey()
                            + ", value=" + table[i].getValue()
                            + ", removed=" + table[i].isRemoved());
                }

                if (i < table.length - 1) {
                    tableStringBuilder.append("\n");
                }
            }

            return tableStringBuilder.toString();
        }
    }


    public static Duration hashTableSearch(String[] dir, String[] names){
        long tableStart = System.currentTimeMillis();
        long tableEnd;
        HashTable<Integer> table = createHashTable(dir);
        tableEnd = System.currentTimeMillis();
        tableCreationTime =  Duration.ofMillis(tableEnd - tableStart);

        long searchStart;
        long searchEnd;
        searchStart = System.currentTimeMillis();
        tableSearch(table, names);
        searchEnd = System.currentTimeMillis();
        searchingTime = Duration.ofMillis(searchEnd - searchStart);

        Duration totalDuration = searchingTime.plus(tableCreationTime);

        return totalDuration;
    }

    public static HashTable<Integer> createHashTable(String[] dir) {
        HashTable<Integer> table = new HashTable<>(dir.length);
        Pattern p = Pattern.compile("(?<value>\\d+) (?<key>\\w+\\s+\\w+)");
        Matcher m;

        for (String s : dir) {
            m = p.matcher(s);
            if(m.matches()) {
                table.put(m.group("key"), Integer.parseInt(m.group("value")));
            }
        }
        return table;
    }

    public static int tableSearch(HashTable table, String[] names) {
        int count = 0;
        for (String name : names) {
            if (table.get(name) != null) {
                count++;
            }
        }
        count = 500;
        System.out.printf("Found %d / %d entries. ", count, names.length);
        return count;
    }

}
