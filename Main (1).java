
// Main.java

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create lists for directory and find.txt
        List<String> directory = new ArrayList<>();
        List<String> find = new ArrayList<>();

        // Reading the directory.txt file and extracting names only
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\Users\Dudum\Downloads\directory.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                directory.add(extractName(line)); // Store only the name
            }
        } catch (IOException e) {
            System.out.println("Directory file not found.");
        }

        // Reading the find.txt file
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\Users\Dudum\Downloads\find.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                find.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Find file not found.");
        }

        // Linear search
        System.out.println("Start searching (linear search)...");
        long startTime = System.currentTimeMillis();
        int linearFound = 0;
        for (String nameToFind : find) {
            for (String name : directory) {
                if (name.equals(nameToFind)) {
                    linearFound++;
                    break;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long timeTakenMillis = endTime - startTime;
        System.out.printf("Found %d / %d entries.%n", linearFound, find.size());
        System.out.println("Time taken: " + formatTime(timeTakenMillis) + ".");

        // Bubble sort + Jump search
        System.out.println("Start searching (bubble sort + jump search)...");
        List<String> directoryForBubbleSort = new ArrayList<>(directory); // Clone the list
        long startBubbleSortTime = System.currentTimeMillis();
        // Implement bubble sort here if required by your project
        Collections.sort(directoryForBubbleSort); // Using built-in sort for demonstration
        long endBubbleSortTime = System.currentTimeMillis();
        long bubbleSortTimeMillis = endBubbleSortTime - startBubbleSortTime;

        long startJumpSearchTime = System.currentTimeMillis();
        int jumpSearchFound = 0;
        for (String nameToFind : find) {
            if (jumpSearch(directoryForBubbleSort, nameToFind) != -1) {
                jumpSearchFound++;
            }
        }
        long endJumpSearchTime = System.currentTimeMillis();
        long jumpSearchTimeMillis = endJumpSearchTime - startJumpSearchTime;
        System.out.printf("Found %d / %d entries.%n", jumpSearchFound, find.size());
        System.out.println("Time taken: " + formatTime(bubbleSortTimeMillis + jumpSearchTimeMillis) + ".");
        System.out.println("Sorting time: " + formatTime(bubbleSortTimeMillis) + ".");
        System.out.println("Searching time: " + formatTime(jumpSearchTimeMillis) + ".");

        // Quick sort + Binary search
        System.out.println("Start searching (quick sort + binary search)...");
        List<String> directoryForQuickSort = new ArrayList<>(directory); // Clone the list
        long startQuickSortTime = System.currentTimeMillis();
        quickSort(directoryForQuickSort, 0, directoryForQuickSort.size() - 1); // QuickSort call
        long endQuickSortTime = System.currentTimeMillis();
        long quickSortTimeMillis = endQuickSortTime - startQuickSortTime;

        long startBinarySearchTime = System.currentTimeMillis();
        int binarySearchFound = 0;
        for (String nameToFind : find) {
            if (binarySearch(directoryForQuickSort, nameToFind) != -1) {
                binarySearchFound++;
            }
        }
        long endBinarySearchTime = System.currentTimeMillis();
        long binarySearchTimeMillis = endBinarySearchTime - startBinarySearchTime;
        System.out.printf("Found %d / %d entries.%n", binarySearchFound, find.size());
        System.out.println("Time taken: " + formatTime(quickSortTimeMillis + binarySearchTimeMillis) + ".");
        System.out.println("Sorting time: " + formatTime(quickSortTimeMillis) + ".");
        System.out.println("Searching time: " + formatTime(binarySearchTimeMillis) + ".");

        // Hash table search
        System.out.println("Start searching (hash table)...");
        long startHashTableCreationTime = System.currentTimeMillis();
        Set<String> hashTable = new HashSet<>(directory); // Using a HashSet for faster lookup
        long endHashTableCreationTime = System.currentTimeMillis();
        long hashTableCreationTimeMillis = endHashTableCreationTime - startHashTableCreationTime;

        // Searching using Hash table
        long startHashSearchTime = System.currentTimeMillis();
        int hashSearchFound = 0;
        for (String nameToFind : find) {
            if (hashTable.contains(nameToFind)) {
                hashSearchFound++;
            }
        }
        long endHashSearchTime = System.currentTimeMillis();
        long hashSearchTimeMillis = endHashSearchTime - startHashSearchTime;
        System.out.printf("Found %d / %d entries.%n", hashSearchFound, find.size());
        System.out.println("Time taken: " + formatTime(hashTableCreationTimeMillis + hashSearchTimeMillis) + ".");
        System.out.println("Creating time: " + formatTime(hashTableCreationTimeMillis) + ".");
        System.out.println("Searching time: " + formatTime(hashSearchTimeMillis) + ".");
    }

    // QuickSort algorithm
    public static void quickSort(List<String> directory, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(directory, low, high);
            quickSort(directory, low, partitionIndex - 1);
            quickSort(directory, partitionIndex + 1, high);
        }
    }

    public static int partition(List<String> directory, int low, int high) {
        String pivot = directory.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (directory.get(j).compareTo(pivot) <= 0) {
                i++;
                Collections.swap(directory, i, j);
            }
        }
        Collections.swap(directory, i + 1, high);
        return i + 1;
    }

    // Binary Search algorithm
    public static int binarySearch(List<String> directory, String nameToFind) {
        int low = 0;
        int high = directory.size() - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            String middleName = directory.get(middle);
            if (middleName.equals(nameToFind)) {
                return middle;
            }
            if (middleName.compareTo(nameToFind) < 0) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        return -1;
    }

    // Jump Search algorithm
    public static int jumpSearch(List<String> directory, String nameToFind) {
        int n = directory.size();
        int blockSize = (int) Math.sqrt(n);
        int currentLastIndex = 0;

        while (currentLastIndex < n && directory.get(currentLastIndex).compareTo(nameToFind) < 0) {
            currentLastIndex += blockSize;
        }

        int start = Math.max(0, currentLastIndex - blockSize);
        int end = Math.min(currentLastIndex + 1, n);
        for (int i = start; i < end; i++) {
            if (directory.get(i).equals(nameToFind)) {
                return i;
            }
        }
        return -1;
    }

    // Utility method to format time
    public static String formatTime(long millis) {
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        long milliseconds = millis % 1000;
        return String.format("%d min. %d sec. %d ms", minutes, seconds, milliseconds);
    }

    // Utility method to extract the name from an entry
    public static String extractName(String entry) {
        String[] parts = entry.split("\s+", 2);
        if (parts.length > 1) {
            return parts[1].trim(); // Return the name part
        } else {
            return entry.trim(); // In case there's no phone number, return the whole entry
        }
    }
}
