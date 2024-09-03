package arrays.inserting;

/**
 * Given a fixed-length integer array arr, duplicate each occurrence of zero, shifting the remaining elements to the right.
 *
 * Note that elements beyond the length of the original array are not written.
 * Do the above modifications to the input array in place and do not return anything.
 */
public class DuplicateZeros {
    public void duplicateZeros(int[] arr) {

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0 && i + 1 < arr.length) {

                int targetIndex = i + 1;
                for (int j = arr.length - 1; j >= targetIndex; j--) {
                    arr[j] = arr[j - 1];
                }
                arr[targetIndex] = 0;
                i++;
            }
        }
    }
}

