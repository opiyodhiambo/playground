package arrays;

/**
 * Given an array nums of integers,
 * return how many of them contain an even number of digits.
 */

public class NumberOfEvenDigitNumbers {
    public int findNumbers(int[] nums) {

        int numberOfEvenDigitNumbers = 0;

        for (int i = 0; i < nums.length; i++) {

            int digitCount = Integer.toString(nums[i]).length();
            if (digitCount % 2 == 0) {
                numberOfEvenDigitNumbers++;
            }
        }
        return numberOfEvenDigitNumbers;

    }
}
