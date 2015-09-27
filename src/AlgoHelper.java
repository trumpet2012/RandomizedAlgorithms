import java.util.concurrent.ThreadLocalRandom;

/**
 * William Trent Holliday
 * 9/26/15
 */
public class AlgoHelper {

    protected static void swap(int[] arr, int left, int right){
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }

    protected static int rand_partition(int[] arr, int lower, int upper){
        int rand_index = random_number(lower, upper);
        swap(arr, lower, rand_index);
        return partition(arr, lower, upper);
    }

    protected static int partition(int[] arr, int lower, int upper){
        int pivot = arr[lower];
        int lowerIndex = lower - 1;
        int upperIndex = upper + 1;

        while(true){
            // Find the position of a number to the right of the pivot that should be
            // on the left of the pivot
            do {
                upperIndex -=1;
            } while(arr[upperIndex] > pivot);

            // Find the position of a number to the left of the pivot that should be
            // on the right of the pivot
            do {
                lowerIndex += 1;
            } while(arr[lowerIndex] < pivot);

            if (lowerIndex < upperIndex){
                swap(arr, lowerIndex, upperIndex);
            }else{
                return upperIndex;
            }

        }
    }

    protected static int random_number(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
