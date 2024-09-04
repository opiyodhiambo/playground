package arrays.searching;

import java.util.HashSet;
import java.util.Set;

/**
 * Given an array arr of integers, check if there exist two indices i and j such that :
 *
 *     i != j
 *     0 <= i, j < arr.length
 *     arr[i] == 2 * arr[j]
 */
public class SearchingAnElementAndItsDouble {
    public boolean checkIfExists(int[] arr){
        // Loop from i = 0 to arr.length, maintaining in a hashTable the array elements from [0, i - 1].
        Set<Integer> hasTable = new HashSet<>();

        for (int num: arr) {
            // On each step of the loop check if we have seen the element 2 * arr[i] so far or arr[i] / 2 was seen if arr[i] % 2 == 0.
            if (hasTable.contains(num * 2) ||(hasTable.contains(num / 2) && num % 2 == 0)) {
                return true;
            }
            hasTable.add(num);
        }
        return false;
    }
}
