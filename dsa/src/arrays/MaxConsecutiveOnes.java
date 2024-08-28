package arrays;

/**
 * Given a binary array, find the maximum number of consecutive 1s in this array.
 */

class MaxConsecutiveOnes {
    public int findMaxConsecutiveOnes(int[] numbers) {

        int numberOfConsecutiveOnes = 0;
        int maxNumberOfConsecutiveOnes = 0;

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 1) {
                numberOfConsecutiveOnes++;

                if (numberOfConsecutiveOnes > maxNumberOfConsecutiveOnes) {
                    maxNumberOfConsecutiveOnes = numberOfConsecutiveOnes;
                }

            } else {
                numberOfConsecutiveOnes = 0;
            }
        }

        return maxNumberOfConsecutiveOnes;
    }
}

