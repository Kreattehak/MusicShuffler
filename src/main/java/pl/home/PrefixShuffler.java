package pl.home;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PrefixShuffler {
    private Stack<Integer> shuffle = new Stack<>();

    void initializeShuffle(int fileCount) {
        List<Integer> rangeOfNumbers = IntStream.range(1, fileCount + 1)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(rangeOfNumbers);
        rangeOfNumbers.forEach(shuffle::push);
    }

    Integer getShuffle() {
        return shuffle.pop();
    }
}
