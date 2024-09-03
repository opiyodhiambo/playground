package arrays.deleting;

/**
 * Given an integer array nums and an integer val, remove all occurrences of val in nums in-place.
 * The order of the elements may be changed.
 * Then return the number of elements in nums which are not equal to val.
 * <p>
 * Consider the number of elements in nums which are not equal to val be k, to get accepted, you need to do the following things:
 * <p>
 * Change the array nums such that the first k elements of nums contain the elements which are not equal to val.
 * The remaining elements of nums are not important as well as the size of nums.
 * Return k.
 */
public class RemovingElement {

    public int removeElement(int[] nums, int val) {
        int length = nums.length;

        for (int i = 0; i < length; i++) {

            if (nums[i] == val) {
                // If we find a `val` element, we shift everything after it to the left
                for (int j = i; j < length - 1; j++) {
                    nums[j] = nums[j + 1];
                }
                length--; // Decrease the length because we removed one element

                i--; // Stay at the same index to check the new element at this position
            }
        }
        return length;
    }
}
