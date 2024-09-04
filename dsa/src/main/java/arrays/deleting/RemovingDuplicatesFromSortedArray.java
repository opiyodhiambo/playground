package arrays.deleting;

public class RemovingDuplicatesFromSortedArray {
    public int removeDuplicates(int[] nums) {
        int length = nums.length;

        for (int i = 1; i < length; i++) {
            if (nums[i] == nums[i - 1]) {

                for (int j = i; j < length - 1; j++) {
                    nums[j] = nums[j + 1];
                }
                length--;// Decrease the length because we removed one element
                i--;// Stay at the same index to check for duplicate elements from the same position
            }
        }
        return length;
    }
}
