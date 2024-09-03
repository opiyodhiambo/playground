package arrays.introduction;

/**
 * Given an integer array nums sorted in non-decreasing order,
 * return an array of the squares of each number sorted in non-decreasing order.
 */
public class SquaresOfASortedArray {
    public int[] sortedSquares(int[] nums) {

        int numberOfSquareNumbers[] = new int[nums.length];

        /**Squaring the numbers*/
        for (int i = 0; i < nums.length; i++ ) {
            int squareOfNumbers = nums[i]*nums[i];
            numberOfSquareNumbers[i] = squareOfNumbers;
        }

        /**Sorting logic*/
        for (int i = 0; i < numberOfSquareNumbers.length; i++) {
            for (int j = i + 1; j < numberOfSquareNumbers.length; j++) {

                int temporatyVariable = 0;

                if (numberOfSquareNumbers[i] > numberOfSquareNumbers[j]) {
                    temporatyVariable = numberOfSquareNumbers[i];
                    numberOfSquareNumbers[i] = numberOfSquareNumbers[j];
                    numberOfSquareNumbers[j] = temporatyVariable;
                }
            }
        }
        return numberOfSquareNumbers;
    }
}
