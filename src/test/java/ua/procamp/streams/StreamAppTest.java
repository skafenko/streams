package ua.procamp.streams;

import org.junit.Before;
import org.junit.Test;
import ua.procamp.streams.stream.AsIntStream;
import ua.procamp.streams.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StreamAppTest {
    
    private IntStream intStream;

    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
    }

    @Test
    public void testStreamFlatForEach() {
        System.out.println("testStreamFlatForEach");
        String expResult = "-1001122334";
        IntStream stream = this.intStream.flatMap(value -> AsIntStream.of(value, value + 1));//-1, 0, 0, 1, 1, 2, 2, 3, 3, 4
        String result = StreamApp.streamForEach(stream);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testStreamOperations() {
        System.out.println("testStreamOperations");
        int expResult = 42;
        int result = StreamApp.streamOperations(intStream);
        assertEquals(expResult, result);        
    }

    @Test
    public void testStreamToArray() {
        System.out.println("testStreamToArray");
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = StreamApp.streamToArray(intStream);
        assertArrayEquals(expResult, result);        
    }

    @Test
    public void testStreamForEach() {
        System.out.println("testStreamForEach");
        String expResult = "-10123";
        String result = StreamApp.streamForEach(intStream);
        assertEquals(expResult, result);        
    }

    @Test
    public void testStreamMax() {
        System.out.println("testStreamMax");
        Integer expResult = 3;
        Integer result = intStream.max();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamMin() {
        System.out.println("testStreamMin");
        Integer expResult = -1;
        Integer result = intStream.min();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamAverage() {
        System.out.println("testStreamAverage");
        Double expResult = 1.0;
        Double result = intStream.average();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamSum() {
        System.out.println("testStreamSum");
        Integer expResult = 5;
        Integer result = intStream.sum();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamCount() {
        System.out.println("testStreamCount");
        long expResult = 5;
        long result = intStream.count();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamFilterFlatMapMapWithSum() {
        System.out.println("testStreamFilterFlatMapMapWithSum");
        Integer expResult = 27;
        Integer result = intStream
                .filter((x) -> x > 1)//2,3
                .flatMap(value -> AsIntStream.of(value * 2, value))//4, 2, 6, 3
                .map(operand -> operand + 3)//7, 5, 9, 6
                .sum();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamFilterInvokeWithMin() {
        System.out.println("testStreamFilterInvokeWithMin");
        Integer expResult = 2;
        Integer result = intStream
                .filter((x) -> x > 1)
                .min();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamFilterInvokeWithCount() {
        System.out.println("testStreamFilterInvokeWithCount");
        long expResult = 1;
        long result = intStream
                .filter((x) -> x > 2)
                .count();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamMapInvokeWithMax() {
        System.out.println("testStreamMapInvokeWithMax");
        Integer expResult = 6;
        Integer result = intStream
                .map((x) -> x * 2)//-2, 0, 2, 4, 6
                .max();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamFilterAndMapInvokeWithMax() {
        System.out.println("testStreamFilterAndMapInvokeWithMax");
        Integer expResult = 6;
        Integer result = intStream
                .filter((x) -> x > 1)//
                .map((x) -> x * 2)//-2, 0, 2, 4, 6
                .max();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamFilterAndFlapMapInvokeWithCount() {
        System.out.println("testStreamFilterAndFlapMapInvokeWithCount");
        long expResult = 9;
        long result = intStream
                .filter((x) -> x > 0)//1, 2, 3
                .flatMap((x) -> AsIntStream.of(x * 2, x + 1, x - 2))//2, 2, -1, 4, 3, 0, 6, 4, 1
                .count();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamFilterAndFlapMapInvokeWithAverage() {
        System.out.println("testStreamFilterAndFlapMapInvokeWithAverage");
        Double expResult = 3.0;
        Double result = intStream
                .filter((x) -> x > 0)//1, 2, 3
                .flatMap((x) -> AsIntStream.of(x * 2, x + 1, x))//2, 2, 1, 4, 3, 2, 6, 4, 3
                .average();
        assertEquals(expResult, result);
    }

    @Test
    public void testStreamFilterInvokeWithReduce() {
        System.out.println("testStreamFilterInvokeWithReduce");
        int expResult = 30;
        int result = intStream
                .filter((x) -> x > 0)//1, 2, 3
                .reduce(5, (left, right) -> left * right);//30
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStreamWithMin() {
        System.out.println("testEmptyStreamWithMin");
        intStream
                .filter((x) -> x > 5)
                .min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStreamWithMax() {
        System.out.println("testEmptyStreamWithMax");
        intStream
                .filter((x) -> x > 5)
                .max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStreamWithAverage() {
        System.out.println("testEmptyStreamWithAverage");
        intStream
                .filter((x) -> x > 5)
                .average();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStreamWithSum() {
        System.out.println("testEmptyStreamWithSum");
        intStream
                .filter((x) -> x > 5)
                .sum();
    }
}
