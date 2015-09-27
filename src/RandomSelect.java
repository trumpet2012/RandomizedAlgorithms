/**
 * William Trent Holliday
 * 9/26/15
 */
public class RandomSelect{

    public int rand_select(int[] arr, int min, int max, int find_pos){
        if (min == max) return arr[min];
        int partition = AlgoHelper.rand_partition(arr, min, max);
        int partition_position = partition - min + 1;
        if (find_pos == partition_position) return arr[partition];
        else if (find_pos < partition_position) return rand_select(arr, min, partition - 1, find_pos);
        else return rand_select(arr, partition + 1, max, find_pos - partition_position);
    }

    public AlgoResult execute(int[] arr, int find_pos){
        int[] copy = arr.clone();
        long start = System.nanoTime();
        int result = rand_select(copy, 0, copy.length - 1, find_pos);
        long end = System.nanoTime();
        return new AlgoResult(arr.length, end-start, result);
    }

}
