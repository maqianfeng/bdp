package com.bdp.demo.java8.stream;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Stream
 * https://zhuanlan.zhihu.com/p/33313312
 */
public class stream2 {

    /**
     * Stream 如何工作
     * Stream 表示元素序列，并支持对这些元素进行不同类型的计算操作:
     */
    public static void main(String[] args) {

        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
        myList.stream().filter(s -> s.startsWith("c")).map(String::toUpperCase).sorted().forEach(System.out::println);
        // C1
        // C2


        /**
         * Stream 操作包括中间操作和终端操作。中间操作返回Stream，
         * 这样我们就可以在不使用分号的情况下串联多个中间操作，
         * 终端操作返回void或者一个非Stream结果值。
         * 在示例中, filter, map 和 sorted 是中间操作，而forEach是一个终端操作。
         * 所有有关stream操作的完整列表， 请参阅 Stream Javadoc。
         * 如上例中所示的Stream操作链也称为操作管道。
         * 大多数Stream操作接受某种lambda表达式参数，这是指定确切操作行为的函数接口。
         * 这些操作中的大多数必须是不干扰的，无状态的。那是什么意思 ?
         * 当不修改stream的底层数据源时，该函数是不干扰的，例如，在上面的例子中，
         * 没有lambda表达式通过添加或删除集合中的元素来修改myList。
         * 当操作的执行是确定的时候，函数是无状态的，例如，在上面的例子执行过程中，
         * 没有lambda表达式依赖于可能发生变化的外部作用域的任何可变变量或状态。
         * 不同类型的Stream可以从各种数据源创建Stream，
         * 特别是collections，List 和 Set，支持新方法 stream () 和parallelStream()，
         * 以创建顺序或并行Stream。
         * 并行Stream可以在多个线程上运行，并将在本教程的后部分中介绍。我们现在关注顺序Stream:
         */
        Arrays.asList("a1", "a2", "a3").stream().findFirst().ifPresent(System.out::println);
        // a1

        //在对象list上调用方法 stream () 返回一个常规对象Stream。但我们不需要创建集合来处理Stream，
        // 如下例所示:
        Stream.of("a1", "a2", "a3").findFirst().ifPresent(System.out::println);
        // a1

        //仅需要使用 Stream.of() 从一堆对象引用中创建一个Stream。
        //除了常规的对象Stream，Java 8 有特殊类型的Stream，
        // 用于处理基本数据类型int, long和double。你可能已经猜到了，
        // 它是IntStream、LongStream和DoubleStream。
        //IntStreams可以使用IntStream.range() 来代替常规的for循环。
        IntStream.range(1, 4).forEach(System.out::println);
        // 1
        // 2
        // 3

        //所有这些原生Stream都像普通对象Stream一样工作，但有以下不同：
        // 原生Stream使用专门的lambda表达式，例如是IntFunction而不是Function，
        // 是IntPredicate，而不是Predicate。断言
        //原生 Stream 支持额外的终端聚合操作sum() 和average():

        Arrays.stream(new int[]{1, 2, 3}).map(n -> 2 * n + 1).average().ifPresent(System.out::println);
        // 5.0

        //有时需要将一个普通对象Stream转换为原生 Stream，反之亦然。
        //为此，对象Stream支持专门的映射操作mapToInt()、 mapToLong() 和mapToDouble:
        Stream.of("a1", "a2", "a3").map(s -> s.substring(1)).mapToInt(Integer::parseInt).max().ifPresent(System.out::println);
        // 3

        //原生Stream可以通过mapToObj() 转换为对象Stream:
        IntStream.range(1, 4).mapToObj(i -> "a" + i).forEach(System.out::println);
        // a1
        // a2
        // a3

        //这里有一个组合示例:
        //double的Stream首先映射到一个IntStream，而不是映射到字符串的对象Stream:
        Stream.of(1.0, 2.0, 3.0)
                .mapToInt(Double::intValue)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);
        // a1
        // a2
        // a3

        //处理顺序
        //现在我们已经了解了如何创建和处理不同类型的Stream，让我们更深入地了解如何处理Stream操作。
        //中间操作的一个重要特征是惰性。以下例子中，终端操作是缺失的:
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                });
        //在执行此代码片段时，不会向控制台输出任何内容。这是因为中间操作只在出现终端操作时执行。
        //让我们通过终端操作forEach来扩展上面的例子:
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                }).forEach(s -> System.out.println("forEach: " + s));
        //执行这段代码片段会在控制台输出:
        //filter:
        //d2
        //forEach:
        //d2
        //filter:
        //a2
        //forEach:
        //a2
        //filter:
        //b1
        //forEach:
        //b1
        //filter:
        //b3
        //forEach:
        //b3
        //filter:
        //c
        //forEach:
        //c
        //结果的输出顺序可能令人惊讶。一种简单的方法是在Stream的所有元素上水平地执行操作。
        // 但此处相反，每个元素都沿着链垂直移动。第一个字符串“d2”先filter然后foreach，
        // 然后第二个字符串“a2”才被处理。这种方式可以减少在每个元素上执行的实际操作数，如下例所示:
        Stream.of("d2", "a2", "b1", "b3", "c").map(s -> {
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).anyMatch(s -> {
            System.out.println("anyMatch: " + s);
            return s.startsWith("A");
        });
        // map:      d2
        // anyMatch: D2
        // map:      a2
        // anyMatch: A2

        //当predicate应用于给定的输入元素时，anyMatch将立即返回true。
        // 这对于第二个被传递的“A2”来说是正确的。由于stream链的垂直执行，在这种情况下，
        // map只会执行两次。因此，map将尽可能少地被调用，而不是所有的元素映射到Stream中。


        //为什么顺序很重要
        //下一个示例包括两个中间操作 map 和 filter 以及终端操作forEach。
        // 我们再一次查看这些操作是如何执行的:
        Stream.of("d2", "a2", "b1", "b3", "c").map(s -> {
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).filter(s -> {
            System.out.println("filter: " + s);
            return s.startsWith("A");
        }).forEach(s -> System.out.println("forEach: " + s));

        // map:     d2
        // filter:  D2
        // map:     a2
        // filter:  A2
        // forEach: A2
        // map:     b1
        // filter:  B1
        // map:     b3
        // filter:  B3
        // map:     c
        // filter:  C
        //你可能已经猜到，底层集合中的每个字符串都被调用了5次map和filter，而forEach只调用一次。
        //如果我们改变操作的顺序，将filter移到链的开头，我们可以大大减少实际执行次数:
        Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> {
            System.out.println("filter: " + s);
            return s.startsWith("a");
        }).map(s -> {
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).forEach(s -> System.out.println("forEach: " + s));
        // filter:  d2
        // filter:  a2
        // map:     a2
        // forEach: A2
        // filter:  b1
        // filter:  b3
        // filter:  c
        // 现在，map只被调用一次，因此操作管道在大量元素输入时执行得更快。
        // 在编写复杂的方法链时，请记住这一点。

        //让我们通过一个额外的操作来扩展上面的示例，sorted: todo 跑下test
        Stream.of("d2", "a2", "b1", "b3", "c").sorted((s1, s2) -> {
            System.out.printf("sort: %s; %s\n", s1, s2);
            return s1.compareTo(s2);
        }).filter(s -> {
            System.out.println("filter: " + s);
            return s.startsWith("a");
        }).map(s -> {
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).forEach(s -> System.out.println("forEach: " + s));
        //排序是一种特殊的中间操作。这是所谓的状态操作，因为要对元素进行排序，你需要维护元素的状态。
        //执行此示例将在控制台输出:
        //sort:
        //a2;
        //d2
        //sort:
        //b1;
        //a2
        //sort:
        //b1;
        //d2
        //sort:
        //b1;
        //a2
        //sort:
        //b3;
        //b1
        //sort:
        //b3;
        //d2
        //sort:
        //c;
        //b3
        //sort:
        //c;
        //d2
        //filter:
        //a2
        //map:
        //a2
        //forEach:
        //A2
        //filter:
        //b1
        //filter:
        //b3
        //filter:
        //c
        //filter:
        //d2

        //首先，在整个输入集合上执行排序操作。换句话说，sorted是水平执行的。
        // 因此，在这个例子中，对输入集合中的每个元素进行多次组合，sorted被调用8次，。
        //我们再一次通过对链操作重排序来优化性能:
        Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> {
            System.out.println("filter: " + s);
            return s.startsWith("a");
        }).sorted((s1, s2) -> {
            System.out.printf("sort: %s; %s\n", s1, s2);
            return s1.compareTo(s2);
        }).map(s -> {
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).forEach(s -> System.out.println("forEach: " + s));
        // filter:  d2
        // filter:  a2
        // filter:  b1
        // filter:  b3
        // filter:  c
        // map:     a2
        // forEach: A2

        //在这个示例中，没有调用 sorted，因为filter将输入集合减少到一个元素。
        // 因此，对于大数据量的输入集合，性能会极大地提高。

        //Stream复用
        //Java 8 Stream 无法复用。一旦你调用任何终端操作，Stream就会关闭:
        java.util.stream.Stream<String> stream =
                Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));
        stream.anyMatch(s -> true);    // ok
        stream.noneMatch(s -> true);   // exception
        //在同一条Stream上的调用anyMatch之后调用noneMatch导致以下异常:
        //java.lang.IllegalStateException:
        //stream has
        //already been
        //operated upon
        //or closed
        //at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:229)
        //at java.util.stream.ReferencePipeline.noneMatch(ReferencePipeline.java:459)
        //at com.winterbe.java8.Streams5.test7(Streams5.java:38)
        //at com.winterbe.java8.Streams5.main(Streams5.java:28)
        //为了克服这个限制，必须为要执行的每一个终端操作创建一个新的Stream链，
        // 例如，我们可以创建一个Stream提供者来创建已构建所有中间操作的新Stream:
        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a"));
        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().noneMatch(s -> true);  // ok

        //每次调用 get () 构造一个新Stream，我们在此调用终端操作。

        //高级操作
        //stream支持各种不同的操作。我们已经了解了最重要的操作，如filter或map。
        //你可以学习其他的操作(参考Stream Javadoc)。我们将更深入地了解复杂的操作，collect flatMap和 reduce。
        //本节的大部分代码示例使用下面 person组成的list进行演示:
        class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        List<Person> persons =
                Arrays.asList(
                        new Person("Max", 18),
                        new Person("Peter", 23),
                        new Person("Pamela", 23),
                        new Person("David", 12));


        //Collect
        //Collect是一种非常有用的终端操作，可以将stream元素转换为不同类型的结果，
        // 例如List, Set or Map。
        //Collect 接受一个包含四个不同操作的Collector:supplier(供应者),
        // accumulator(累加器 [əˈkjuːmjəleɪtə(r)]), combiner 和 finisher。
        // 这听起来很复杂，优点是Java 8 通过Collectors类支持各种内置收集器。
        // 因此，对于最常见的操作，你不必自己实现Collector。

        //让我们从一个十分常见的用例开始:
        List<Person> filtered =
                persons.stream()
                        .filter(p -> p.name.startsWith("P"))
                        .collect(Collectors.toList());
        System.out.println(filtered);
        // [Peter, Pamela]
        //正如您所看到的,根据Stream的元素构建list 非常简单。
        //如果需要set而不是list 使用Collectors. toSet() 就可以。

        //下一个例子将所有人按年龄分组:
        Map<Integer, List<Person>> personsByAge =
                persons.stream()
                        .collect(Collectors.groupingBy(p -> p.age));

        personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

        // age 18: [Max]
        // age 23: [Peter, Pamela]
        // age 12: [David]

        //Collectors 是多功能的。您还可以在Stream的元素上创建聚合，例如计算平均年龄:
        Double averageAge =
                persons.stream()
                        .collect(Collectors.averagingInt(p -> p.age));
        System.out.println(averageAge);
        // 19.0
        //如果你对更全面的统计数据感兴趣，汇总Collectors返回一个专门的内置汇总统计对象。
        // 因此，我们可以简单地确定年龄最小值、最大值和算术平均值以及总和和数量。

        IntSummaryStatistics ageSummary =
                persons.stream().collect(Collectors.summarizingInt(p -> p.age));
        System.out.println(ageSummary);
        // IntSummaryStatistics{count=4, sum=76, min=12, average=19.000000, max=23}

        //下一个示例将所有 persons 合并成一个字符串:
        String phrase = persons
                .stream()
                .filter(p -> p.age >= 18)
                .map(p -> p.name)
                .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));
        System.out.println(phrase);
        // In Germany Max and Peter and Pamela are of legal age.
        //join collector接收一个分隔符以及可选的前缀和后缀。
        //为了将stream元素转换为map，我们必须指定键和值如何映射。
        // 请记住，映射的键必须是惟一的，否则会抛出IllegalStateException。
        // 你可以将合并函数作为额外参数传递，以绕过异常:
        Map<Integer, String> map = persons
                .stream()
                .collect(Collectors.toMap(
                        p -> p.age,
                        p -> p.name,
                        (name1, name2) -> name1 + ";" + name2));
        System.out.println(map);
        // {18=Max, 23=Peter;Pamela, 12=David}

        //现在我们知道了一些最强大的内置Collector，让我们尝试构建自专用的Collector。
        // 我们想要将Stream中所有person转换成一个由 | 管道字符分隔的大写字母组成的字符串。
        // 为了实现这一点， 我们通过 Collector.of() 创建了一个新的Collector。
        // 我们必须传递Collector的四个要素: supplier、accumulator、combiner和finisher。
        java.util.stream.Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(() -> new StringJoiner(" | "),// supplier
                        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner 组合器  [kəm'baɪnə]
                        StringJoiner::toString);                // finisher
        String names = persons.stream().collect(personNameCollector);
        System.out.println(names);  // MAX | PETER | PAMELA | DAVID

        //由于Java中的字符串是不可变的，
        // 所以我们需要一个类似StringJoiner的helper类来让collector构造我们的字符串。
        // supplier最初使用适当的分隔符构造了这样一个StringJoiner。
        //accumulator用于将每个人的大写名称添加到StringJoiner中。
        //combiner 知道如何将两个StringJoiners合并成一个。
        // 在最后一步中，finisher从StringJoiner中构造所需的字符串。

        //FlatMap
        //我们已经学习了如何利用map操作将Stream的对象转换为另一种类型的对象。
        // Map是有局限的，因为每个对象只能映射到一个对象。但是，如果我们想要将一个对象变换为多个对象，
        //或者将它变换成根本不存在的对象呢 ? 这就是flatMap发挥作用的地方。
        //FlatMap将stream的每个元素转换到其他对象的Stream。
        // 因此，每个对象将被转换为零个、一个或多个基于Stream的不同对象。
        // 这些stream的内容将被放置到flatMap操作的返回Stream中。
        //在我们看flatMap之前，我们需要一个合适的类型层次结构:

        class Bar {
            String name;

            Bar(String name) {
                this.name = name;
            }
        }
        class Foo {
            String name;
            List<Bar> bars = new ArrayList<>();

            Foo(String name) {
                this.name = name;
            }
        }
        //接下来，我们利用Stream相关知识实例化几个对象:
        List<Foo> foos = new ArrayList<>(); // create foos
        IntStream.range(1, 4).forEach(i -> foos.add(new Foo("Foo" + i)));// create bars
        foos.forEach(f -> IntStream.range(1, 4).forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));
        //现在我们有包含3个foos的list，每个foo都包含三个bars。
        //FlatMap接受一个函数，该函数必须返回对象stream。
        // 为了处理每个foo的bar对象，我们只需传递适当的函数:
        foos.stream().flatMap(f -> f.bars.stream()).forEach(b -> System.out.println(b.name));
        // Bar1 <- Foo1
        // Bar2 <- Foo1
        // Bar3 <- Foo1
        // Bar1 <- Foo2
        // Bar2 <- Foo2
        // Bar3 <- Foo2
        // Bar1 <- Foo3
        // Bar2 <- Foo3
        // Bar3 <- Foo3
        //如您所见，我们已经成功地将三个foo对象的stream转换成9个bar对象的stream。
        //最后，上述代码示例可以简化为stream操作的单管道:
        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .peek(f -> IntStream.range(1, 4)
                        .mapToObj(i -> new Bar("Bar" + i + " <- "+f.name))
                        .forEach(f.bars::add))
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));

        //FlatMap也可用于Java 8 引入的Optional类。


        // Optionals flatMap
        //操作返回另一个类型的可选对象。所以它可以被用来防止讨厌的null检查。
        //有这样一个层次分明的结构:

        class Inner {
            String foo;
        }
        class Nested {
            Inner inner;
        }
        class Outer {
            Nested nested;
        }
        //为了处理外部实例的内部字符串foo，必须添加多个空检查以防止可能的NullPointerExceptions:
        Outer outer = new Outer();
        if (outer.nested != null && outer.nested.inner != null) {
            System.out.println(outer.nested.inner.foo);
        }
        //利用optionals flatMap操作可以达到相同效果:
        Optional.of(new Outer())
                .flatMap(o -> Optional.ofNullable(o.nested))
                .flatMap(n -> Optional.ofNullable(n.inner))
                .flatMap(i -> Optional.ofNullable(i.foo))
                .ifPresent(System.out::println);
        //对flatMap的每次调用，如果对象存在，则返回包装对象的Optional，不存在，则返回空的Optional。

        //Reduce
        //reduce操作将stream的所有元素合并到一个结果中。
        // Java 8 支持三种不同的reduce方法。
        // 第一种将stream中元素reduce为一个。
        // 让我们看看如何使用这个方法来确定最年长的人:
        persons.stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
                .ifPresent(System.out::println);    // Pamela
        //reduce方法接受一个BinaryOperator累加器函数。
        // 这实际上是一个BiFunction，在这个例子中，两个操作数都有相同的类型Person。
        // BiFunctions类似于Function，但接受两个参数。
        //示例函数比较两个人的年龄，以返回年龄最大的人。
        //第二个reduce方法接受 实体值和BinaryOperator累加器。
        // 该方法可用于构建一个新的Person，它聚合来自于stream的其他人的的姓名和年龄:

        Person result =
                persons.stream()
                        .reduce(new Person("", 0), (p1, p2) -> {
                            p1.age += p2.age;
                            p1.name += p2.name;
                            return p1;
                        });
        System.out.format("name=%s; age=%s", result.name, result.age);
        // name=MaxPeterPamelaDavid; age=76


        //第三种 reduce
        //方法接受三个参数:
        //标识值、BiFunction累加器和BinaryOperator类型的组合函数。
        // 由于标识值类型并不局限于Person类型，所以我们可以确定所有人的年龄和:

        Integer ageSum = persons
                .stream()
                .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);
        System.out.println(ageSum);  // 76
        //你可以看到结果是76，但在底层到底发生了什么 ? 我们通过一些调试输出来扩展上面的代码 :
        Integer ageSum2 = persons
                .stream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

        // accumulator: sum=0; person=Max
        // accumulator: sum=18; person=Peter
        // accumulator: sum=41; person=Pamela
        // accumulator: sum=64; person=David
        //可以看到，accumulator函数完成所有工作。它首次被调用时初始值为0，第一个人是Max。
        // 在接下来的三个步骤中，sum持续增加到76的总年龄。

        //combiner 从未被调用?通过并行执行同样的stream程序可以解释这个秘密:
        Integer ageSum3 = persons
                .parallelStream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

        // accumulator: sum=0; person=Pamela
        // accumulator: sum=0; person=David
        // accumulator: sum=0; person=Max
        // accumulator: sum=0; person=Peter
        // combiner: sum1=18; sum2=23
        // combiner: sum1=23; sum2=12
        // combiner: sum1=41; sum2=35
        //并行执行此stream将产生完全不同的执行过程。
        // 现在这个combiner被调用了。由于accumulator是并行调用的，
        // 所以需要combiner来汇总分离的累计值。

        //我们在下一节深入研究并行stream。

        //并行 Stream
        //随着大数据的火热爆发，Java 8 中也越来越注重性能的提升。
        //在 Java 8 中， 不仅有顺序 Stream， 还有并行 Stream，
        // 可以极大地提升程序在处理大量的元素时的性能。
        // 其实， 并行 Stream 的秘密就在于 背后使用了通用的并发框架 ForkJoinPool ，
        //这是通过利用静态方法 ForkJoinPool.commonPool() 来实现的。
        //对于 ForkJoinPool， 其实际使用的线程数取决于机器背后的实际 CUP 数量。

        java.util.concurrent.ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.getParallelism());    // 3
        //在我的机器上， common pool 初始化的默认值为3。通过设置以下jvm参数可以减少或增加此值：

        //-Djava.util.concurrent.ForkJoinPool.common.parallelism = 5
        //在 Java 8 中， Collection 支持使用方法
        //parallelStream() 方法来直接创建出元素的并行 Stream。此外，
        //你也可以在一个顺序 Stream
        //上调用非终结函数 parallel () 来转换该 Stream 为一个并行 Stream。
        //为了深入了解并行 Stream
        //上操作的执行流程，我们使用如下的示例代码来分析：

        Arrays.asList("a1", "a2", "b1", "c2", "c1")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n", s, Thread.currentThread().getName());
                    return true;
                }).map(s -> {
            System.out.format("map: %s [%s]\n", s, Thread.currentThread().getName());
            return s.toUpperCase();
        }).forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
        //通过分析输出结果 我们可以很好地理解是哪个线程在执行并行Stream 上的操作：
        //filter:
        //b1[main]
        //filter:
        //a2[ForkJoinPool.commonPool - worker - 1]
        //map:
        //a2[ForkJoinPool.commonPool - worker - 1]
        //filter:
        //c2[ForkJoinPool.commonPool - worker - 3]
        //map:
        //c2[ForkJoinPool.commonPool - worker - 3]
        //filter:
        //c1[ForkJoinPool.commonPool - worker - 2]
        //map:
        //c1[ForkJoinPool.commonPool - worker - 2]
        //forEach:
        //C2[ForkJoinPool.commonPool - worker - 3]
        //forEach:
        //A2[ForkJoinPool.commonPool - worker - 1]
        //map:
        //b1[main]
        //forEach:
        //B1[main]
        //filter:
        //a1[ForkJoinPool.commonPool - worker - 3]
        //map:
        //a1[ForkJoinPool.commonPool - worker - 3]
        //forEach:
        //A1[ForkJoinPool.commonPool - worker - 3]
        //forEach:
        //C1[ForkJoinPool.commonPool - worker - 2]

        //正如你所看到的那样， 并行Stream 会利用所有可以从通用 ForkJoinPool 中获得的所有线程来执行操作。
        //那如果我们添加一个 sort 操作呢？
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n", s, Thread.currentThread().getName());
                    return true;
                }).map(s -> {
            System.out.format("map: %s [%s]\n", s, Thread.currentThread().getName());
            return s.toUpperCase();
        }).sorted((s1, s2) -> {
            System.out.format("sort: %s <> %s [%s]\n", s1, s2, Thread.currentThread().getName());
            return s1.compareTo(s2);
        }).forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
        //实际上，该程序的执行结果很可能如下所示：
        //filter:
        //c2[ForkJoinPool.commonPool - worker - 3]
        //filter:
        //c1[ForkJoinPool.commonPool - worker - 2]
        //map:
        //c1[ForkJoinPool.commonPool - worker - 2]
        //filter:
        //a2[ForkJoinPool.commonPool - worker - 1]
        //map:
        //a2[ForkJoinPool.commonPool - worker - 1]
        //filter:
        //b1[main]
        //map:
        //b1[main]
        //filter:
        //a1[ForkJoinPool.commonPool - worker - 2]
        //map:
        //a1[ForkJoinPool.commonPool - worker - 2]
        //map:
        //c2[ForkJoinPool.commonPool - worker - 3]
        //sort:
        //A2<> A1[
        //main]
        //sort:
        //B1<> A2[
        //main]
        //sort:
        //C2<> B1[
        //main]
        //sort:
        //C1<> C2[
        //main]
        //sort:
        //C1<> B1[
        //main]
        //sort:
        //C1<> C2[
        //main]
        //forEach:
        //A1[ForkJoinPool.commonPool - worker - 1]
        //forEach:
        //C2[ForkJoinPool.commonPool - worker - 3]
        //forEach:
        //B1[main]
        //forEach:
        //A2[ForkJoinPool.commonPool - worker - 2]
        //forEach:
        //C1[ForkJoinPool.commonPool - worker - 1]

        //可以发现：1，sort操作还是一个行式执行的操作，它会组合元素的向下传播。
        // 2， 次数的 sort 操作似乎是只是一个线程中执行，而不会使用多线程并发执行？
        //实际上， 并行 Stream 的 sort 操作会使用 Java 8 中的 Arrays.parallelSort() 方法，
        //而该方法会更加需要 sort 的元素的个数的数量来决定是否使用多线程来执行。
        // 如JavaDoc中所述，如果将按顺序或并行执行排序，则此方法将决定数组的长度：
        //如果指定数组的长度小于最小粒度，则使用适当的 Arrays.sort 方法对其进行排序。
        //在我们 Reduce 部分的最后一段中， 我们发现 组合函数只会被并行 Stream 所使用，
        //而不会被顺序 Stream 所使用。现在，让我们看看实际上使用的哪些线程：
        List<Person> persons1 = Arrays.asList(
                new Person("Max", 18),
                new Person("Peter", 23),
                new Person("Pamela", 23),
                new Person("David", 12));

        persons1.parallelStream()
                .reduce(0, (sum, p) -> {
                    System.out.format("accumulator: sum=%s; person=%s [%s]\n", sum, p, Thread.currentThread().getName());
                    return sum += p.age;
                }, (sum1, sum2) -> {
                    System.out.format("combiner: sum1=%s; sum2=%s [%s]\n", sum1, sum2, Thread.currentThread().getName());
                    return sum1 + sum2;
                });
        //执行这段代码，我们看一下可能的执行结果：
        //accumulator:
        //sum = 0;
        //person = Pamela; [main]
        //accumulator:
        //sum = 0;
        //person = Max;    [ForkJoinPool.commonPool - worker - 3]
        //accumulator:
        //sum = 0;
        //person = David;  [ForkJoinPool.commonPool - worker - 2]
        //accumulator:
        //sum = 0;
        //person = Peter;  [ForkJoinPool.commonPool - worker - 1]
        //combiner:
        //sum1 = 18;
        //sum2 = 23;     [ForkJoinPool.commonPool - worker - 1]
        //combiner:
        //sum1 = 23;
        //sum2 = 12;     [ForkJoinPool.commonPool - worker - 2]
        //combiner:
        //sum1 = 41;
        //sum2 = 35;     [ForkJoinPool.commonPool - worker - 2]
        //可以发现 累加器和组合器都会利用 ForkJoinPool 中的多个线程来执行。
        //可以发现， 并行 Stream 会充分利用 ForkJoinPool 中的多个线程来执行操作，
        //从而极大地调高程序的性能。当然，我们还比较记住的一件事就是：
        //并行 Stream 的某些操作， 比如 reduce 和 collect
        //需要另外的操作参数来执行必要的合并操作， 而这些操作可能是顺序 Stream 所不需要的。
        //注意：细心的你可能发现了， 所有的并行 Stream 是在分享同一个 JVM 范围内的通用 ForkJoinPool。
        //所以， 你应该避免在并行 Stream 执行慢速的、阻塞性的操作，
        //因为它们可能会减慢程序其他部分的执行速度和性能，
        //如果程序的其他部分也直接或者间接使用了 ForkJoinPool 的话。
    }
}
