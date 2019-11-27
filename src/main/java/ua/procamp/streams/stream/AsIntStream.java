package ua.procamp.streams.stream;

import ua.procamp.streams.function.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsIntStream implements IntStream {
    private final Generator generator;
    private Integer result;
    private int count;

    private AsIntStream(Generator generator) {
        this.generator = generator;
    }

    public static IntStream of(int... values) {
        return new AsIntStream(consumer -> {
            for (int value : values) {
                consumer.accept(value);
            }
        });
    }

    @Override
    public Double average() {
        generator.generate(value -> {
            count++;
            if (result == null) {
                result = value;
            } else {
                result += value;
            }
        });
        checkEmptyResult();
        return (double) result / count;
    }

    @Override
    public Integer max() {
        generator.generate(value -> {
            if (result == null || result < value) {
                result = value;
            }
        });
        checkEmptyResult();
        return result;
    }

    @Override
    public Integer min() {
        generator.generate(value -> {
            if (result == null || result > value) {
                result = value;
            }
        });
        checkEmptyResult();
        return result;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        Objects.requireNonNull(func);
        return new AsIntStream(consumer -> generator.generate(
                value -> {
                    IntStream intStream = func.applyAsIntStream(value);
                    intStream.forEach(consumer::accept);
                }
        ));
    }

    @Override
    public long count() {
        generator.generate(value -> count++);
        return count;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        return new AsIntStream(generatorContext -> generator.generate(value -> {
            if (predicate.test(value)) {
                generatorContext.accept(value);
            }
        }));
    }

    @Override
    public void forEach(IntConsumer action) {
        generator.generate(action::accept);
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        return new AsIntStream(generatorContext -> generator.generate(
                value -> generatorContext.accept(mapper.apply(value))
        ));
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        result = identity;
        generator.generate(value -> result = op.apply(result, value));
        return result;
    }

    @Override
    public Integer sum() {
        generator.generate(value -> {
            if (result == null) {
                result = value;
            } else {
                result += value;
            }
        });
        checkEmptyResult();
        return result;
    }

    @Override
    public int[] toArray() {
        List<Integer> list = new ArrayList<>();
        forEach(list::add);
        return toIntArray(list);
    }

    private int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list) {
            ret[i++] = e;
        }
        return ret;
    }

    private void checkEmptyResult() {
        if (result == null) {
            throw new IllegalArgumentException("Result stream is empty");
        }
    }
}
