package ua.procamp.streams.stream;

import java.util.function.IntConsumer;

public interface Generator {
    void generate(IntConsumer consumer);

}
