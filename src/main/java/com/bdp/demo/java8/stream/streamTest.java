package com.bdp.demo.java8.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class streamTest {
    public static void main(String args[]) {

//        生成流：在 Java 8 中, 集合接口有两个方法来生成流：
//        stream() − 为集合创建串行流。
//        parallelStream() − 为集合创建并行流。[ˈpærəlel]n. 平行线；对比 vt. 使…与…平行 adj. 平行的；类似的，相同的

        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        //        forEach
//        Stream 提供了新的方法 'forEach' 来迭代流中的每个数据。以下代码片段使用 forEach 输出了10个随机数：
        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);

//        map
//        map 方法用于映射每个元素到对应的结果，以下代码片段使用 map 输出了元素对应的平方数：
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        // 获取对应的平方数
        List<Integer> squaresList = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());

//        filter
//        filter 方法用于通过设置的条件过滤出元素。以下代码片段使用 filter 方法过滤出空字符串：
        strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        // 获取空字符串的数量
        long count = strings.stream().filter(string -> string.isEmpty()).count();

//        limit
//        limit 方法用于获取指定数量的流。 以下代码片段使用 limit 方法打印出 10 条数据：

        random = new Random();
        random.ints().limit(10).forEach(System.out::println);

//        sorted
//        sorted 方法用于对流进行排序。以下代码片段使用 sorted 方法对输出的 10 个随机数进行排序：

        random = new Random();
        random.ints().limit(10).sorted().forEach(System.out::println);

//        并行（parallel）程序
//        parallelStream 是流并行处理程序的代替方法。以下实例我们使用 parallelStream 来输出空字符串的数量：
        strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        // 获取空字符串的数量
        count = strings.parallelStream().filter(string -> string.isEmpty()).count();
//        我们可以很容易的在顺序运行和并行直接切换。

//        Collectors
//        Collectors 类实现了很多归约操作，例如将流转换成集合和聚合元素。Collectors 可用于返回列表或字符串：
        strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);

//        统计
//        另外，一些产生统计结果的收集器也非常有用。它们主要用于int、double、long等基本类型上，它们可以用来产生类似如下的统计结果。
        numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());


        System.out.println("使用 Java 7: ");

        // 计算空字符串
        strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        System.out.println("列表: " + strings);
        count = getCountEmptyStringUsingJava7(strings);

        System.out.println("空字符数量为: " + count);
        count = getCountLength3UsingJava7(strings);

        System.out.println("字符串长度为 3 的数量为: " + count);

        // 删除空字符串
        filtered = deleteEmptyStringsUsingJava7(strings);
        System.out.println("筛选后的列表: " + filtered);

        // 删除空字符串，并使用逗号把它们合并起来
        mergedString = getMergedStringUsingJava7(strings, ", ");
        System.out.println("合并字符串: " + mergedString);
        numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        // 获取列表元素平方数
        squaresList = getSquares(numbers);
        System.out.println("平方数列表: " + squaresList);
        List<Integer> integers = Arrays.asList(1, 2, 13, 4, 15, 6, 17, 8, 19);

        System.out.println("列表: " + integers);
        System.out.println("列表中最大的数 : " + getMax(integers));
        System.out.println("列表中最小的数 : " + getMin(integers));
        System.out.println("所有数之和 : " + getSum(integers));
        System.out.println("平均数 : " + getAverage(integers));
        System.out.println("随机数: ");

        // 输出10个随机数
        random = new Random();

        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt());
        }

        System.out.println("使用 Java 8: ");
        System.out.println("列表: " + strings);

        count = strings.stream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串数量为: " + count);

        count = strings.stream().filter(string -> string.length() == 3).count();
        System.out.println("字符串长度为 3 的数量为: " + count);

        filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println("筛选后的列表: " + filtered);

        mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);

        squaresList = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.println("Squares List: " + squaresList);
        System.out.println("列表: " + integers);

        stats = integers.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());
        System.out.println("随机数: ");

        random.ints().limit(10).sorted().forEach(System.out::println);

        // 并行处理
        count = strings.parallelStream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串的数量为: " + count);








        List<String> myList =
                Arrays.asList("a1", "a2", "b1", "c2", "c1");

        myList
                .stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);



        Arrays.asList("a1", "a2", "a3")
                .stream()
                .findFirst()
                .ifPresent(System.out::println);  // a1
        Stream.of("a1", "a2", "a3")
                .findFirst()
                .ifPresent(System.out::println);  // a1

//        用于处理基本数据类型int,long和double。IntStream、LongStream和DoubleStream
//        IntStreams可以使用IntStream.range()来代替常规的for循环。
        IntStream.range(1, 4)
                .forEach(System.out::println);

//        所有这些原生Stream都像普通对象Stream一样工作，但有以下不同：原生Stream使用专门的lambda表达式，例如是IntFunction而不是Function，是IntPredicate，而不是Predicate。原生 Stream 支持额外的终端聚合操作sum()和average():
        Arrays.stream(new int[] {1, 2, 3})
                .map(n -> 2 * n + 1)
                .average()
                .ifPresent(System.out::println);  // 5.0
//        有时需要将一个普通对象Stream转换为原生 Stream，反之亦然。为此，对象Stream支持专门的映射操作mapToInt()、mapToLong()和mapToDouble:
        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .ifPresent(System.out::println);  // 3
//        原生Stream可以通过mapToObj()转换为对象Stream:
        IntStream.range(1, 4)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);
//        这里有一个组合示例:double的Stream首先映射到一个IntStream，而不是映射到字符串的对象Stream:
        Stream.of(1.0, 2.0, 3.0)
                .mapToInt(Double::intValue)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);


//        中间操作的一个重要特征是惰性。以下例子中，终端操作是缺失的:
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                });
//        在执行此代码片段时，不会向控制台输出任何内容。这是因为中间操作只在出现终端操作时执行。
//        让我们通过终端操作forEach来扩展上面的例子:
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                })
                .forEach(s -> System.out.println("forEach: " + s));
//        执行这段代码片段会在控制台输出:
//        filter:  d2
//        forEach: d2
//        filter:  a2
//        forEach: a2
//        filter:  b1
//        forEach: b1
//        filter:  b3
//        forEach: b3
//        filter:  c
//        forEach: c
//        结果的输出顺序可能令人惊讶。一种简单的方法是在Stream的所有元素上水平地执行操作。但此处相反，每个元素都沿着链垂直移动。第一个字符串“d2”先filter然后foreach，然后第二个字符串“a2”才被处理。
//        这种方式可以减少在每个元素上执行的实际操作数，如下例所示:
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                });

// map:      d2
// anyMatch: D2
// map:      a2
// anyMatch: A2
//        当predicate应用于给定的输入元素时，anyMatch将立即返回true。这对于第二个被传递的“A2”来说是正确的。由于stream链的垂直执行，在这种情况下，map只会执行两次。因此，map将尽可能少地被调用，而不是所有的元素映射到Stream中。
    }



    private static int getCountEmptyStringUsingJava7(List<String> strings) {
        int count = 0;

        for (String string : strings) {

            if (string.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private static int getCountLength3UsingJava7(List<String> strings) {
        int count = 0;

        for (String string : strings) {

            if (string.length() == 3) {
                count++;
            }
        }
        return count;
    }

    private static List<String> deleteEmptyStringsUsingJava7(List<String> strings) {
        List<String> filteredList = new ArrayList<String>();

        for (String string : strings) {

            if (!string.isEmpty()) {
                filteredList.add(string);
            }
        }
        return filteredList;
    }

    private static String getMergedStringUsingJava7(List<String> strings, String separator) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String string : strings) {

            if (!string.isEmpty()) {
                stringBuilder.append(string);
                stringBuilder.append(separator);
            }
        }
        String mergedString = stringBuilder.toString();
        return mergedString.substring(0, mergedString.length() - 2);
    }

    private static List<Integer> getSquares(List<Integer> numbers) {
        List<Integer> squaresList = new ArrayList<Integer>();

        for (Integer number : numbers) {
            Integer square = new Integer(number.intValue() * number.intValue());

            if (!squaresList.contains(square)) {
                squaresList.add(square);
            }
        }
        return squaresList;
    }

    private static int getMax(List<Integer> numbers) {
        int max = numbers.get(0);

        for (int i = 1; i < numbers.size(); i++) {

            Integer number = numbers.get(i);

            if (number.intValue() > max) {
                max = number.intValue();
            }
        }
        return max;
    }

    private static int getMin(List<Integer> numbers) {
        int min = numbers.get(0);

        for (int i = 1; i < numbers.size(); i++) {
            Integer number = numbers.get(i);

            if (number.intValue() < min) {
                min = number.intValue();
            }
        }
        return min;
    }

    private static int getSum(List numbers) {
        int sum = (int) (numbers.get(0));

        for (int i = 1; i < numbers.size(); i++) {
            sum += (int) numbers.get(i);
        }
        return sum;
    }

    private static int getAverage(List<Integer> numbers) {
        return getSum(numbers) / numbers.size();
    }

//    执行以上脚本，输出结果为：
//
//    $ javac Java8Tester.java
//    $ java Java8Tester
//    使用 Java 7:
//    列表: [abc, , bc, efg, abcd, , jkl]
//    空字符数量为: 2
//    字符串长度为 3 的数量为: 3
//    筛选后的列表: [abc, bc, efg, abcd, jkl]
//    合并字符串: abc, bc, efg, abcd, jkl
//    平方数列表: [9, 4, 49, 25]
//    列表: [1, 2, 13, 4, 15, 6, 17, 8, 19]
//    列表中最大的数 : 19
//    列表中最小的数 : 1
//    所有数之和 : 85
//    平均数 : 9
//    随机数:
//            -393170844
//            -963842252
//            447036679
//            -1043163142
//            -881079698
//            221586850
//            -1101570113
//            576190039
//            -1045184578
//            1647841045
//    使用 Java 8:
//    列表: [abc, , bc, efg, abcd, , jkl]
//    空字符串数量为: 2
//    字符串长度为 3 的数量为: 3
//    筛选后的列表: [abc, bc, efg, abcd, jkl]
//    合并字符串: abc, bc, efg, abcd, jkl
//    Squares List: [9, 4, 49, 25]
//    列表: [1, 2, 13, 4, 15, 6, 17, 8, 19]
//    列表中最大的数 : 19
//    列表中最小的数 : 1
//    所有数之和 : 85
//    平均数 : 9.444444444444445
//    随机数:
//            -1743813696
//            -1301974944
//            -1299484995
//            -779981186
//            136544902
//            555792023
//            1243315896
//            1264920849
//            1472077135
//            1706423674
//    空字符串的数量为: 2
}


