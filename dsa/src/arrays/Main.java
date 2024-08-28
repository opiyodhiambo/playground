package arrays;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        //DVD collection
        DVD[] dvdCollection = new DVD[15];
        int numberOfDVDs = 0;
        dvdCollection[7] = new DVD("The Avengers", 2012, "Joss Whedon");;
        dvdCollection[0] = new DVD("The Lion King", 2019, "John Favreau");
        dvdCollection[5] = new DVD("The Incredibles", 2004, "Brad Bird");;
        dvdCollection[9] = new DVD("Finding Dory", 2016, "Andrew Stanton");
        dvdCollection[2] = new DVD("Star Wars", 1977, "George Lucas");

        //Max Consecutive Ones
        int[] randomArrays = {1,1,0,1,1,1};
        MaxConsecutiveOnes maxConsecutiveOnes = new MaxConsecutiveOnes();
        int maxConsecutiveOnesSolution = maxConsecutiveOnes.findMaxConsecutiveOnes(randomArrays);

        //Find Numbers with Even Number of Digits
        NumberOfEvenDigitNumbers numberOfEvenDigitNumbers = new NumberOfEvenDigitNumbers();
        int[] inputArrays = {12,345,2,6,7896};
        int numberOfEvenDigitNumbersSolution = numberOfEvenDigitNumbers.findNumbers(inputArrays);

        //Squares of a Sorted Array
        int[] arraysToSquare = {-4,-1,0,3,10};
        SquaresOfASortedArray squaresOfASortedArray = new SquaresOfASortedArray();
        int[] squaresOfASortedArraySolution = squaresOfASortedArray.sortedSquares(arraysToSquare);


        for (int i = 0; i < dvdCollection.length; i++) {
            if (dvdCollection[i] != null) {
                numberOfDVDs ++;
                System.out.println(dvdCollection[i]);
            }
        }
        System.out.println("The array has a capacity of " + dvdCollection.length);
        System.out.println("The array has a length of " + numberOfDVDs);
        System.out.println("Maximum number of consecutive numbers in the array is " + maxConsecutiveOnesSolution);
        System.out.println("Number of elements with even digits is " + numberOfEvenDigitNumbersSolution);
        System.out.println("After squaring, the array becomes " + Arrays.toString(squaresOfASortedArraySolution));

    }
}

