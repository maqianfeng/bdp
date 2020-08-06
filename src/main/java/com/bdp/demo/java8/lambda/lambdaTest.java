package com.bdp.demo.java8.lambda;

import com.bdp.demo.java8.Annotations.Hint;
import com.bdp.demo.java8.Annotations.Hints;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * java8新特性 https://zhuanlan.zhihu.com/p/33253953?refer=java8
 */
public class lambdaTest {


    public static void main(String[] args) {
        //formula对象以匿名对象的形式实现了Formula接口。
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };
        formula.calculate(100); // 100.0
        formula.sqrt(16); // 4.0


        //使用Java 8之前的方法来实现对一个string列表进行排序：
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });

//    静态工具方法Collections.sort接受一个list和一个Comparator接口作为输入参数，
//    Comparator的实现类可以对输入的list中的元素进行比较。通常情况下，你可以直接用创建匿名Comparator对象，
//    并把它作为参数传递给sort方法。
//    除了创建匿名对象以外，Java 8 还提供了一种更简洁的方式，Lambda表达式。
        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });
//    你可以看到，这段代码就比之前的更加简短和易读。但是，它还可以更加简短：

        Collections.sort(names, (String a, String b) -> b.compareTo(a));
//    只要一行代码，包含了方法体。你甚至可以连大括号对 {} 和return关键字都省略不要。不过这还不是最短的写法：

        Collections.sort(names, (a, b) -> b.compareTo(a));
//    Java编译器能够自动识别参数的类型，所以你就可以省略掉类型不写。让我们再深入地研究一下lambda表达式的威力吧。
        Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
        Integer converted = converter.convert("123");
        System.out.println(converted); // 123


        //    方法和构造函数引用
        //    上面的代码实例可以通过静态方法引用，使之更加简洁：
        Converter<String, Integer> converter2 = Integer::valueOf;
        Integer converted2 = converter2.convert("123");
        System.out.println(converted2); // 123

        // Java 8允许你通过 ::关键字获取方法或者构造函数的的引用。
        // 上面的例子就演示了如何引用一个静态方法。而且我们还可以对一个对象的方法进行引用：
        Something something = new Something();
        Converter<String, String> converter3 = something::startsWith;
        String converted3 = converter3.convert("Java");
        System.out.println(converted3); // "J"

        /**
         * 让我们看看如何使用 ::关键字引用构造函数。
         * 首先我们定义一个示例bean，包含不同的构造方法：{@link com.bdp.demo.java8.lambda.Person}
         * 接下来，我们定义一个person工厂接口，用来创建新的person对象：{@link PersonFactory}
         * 然后我们通过构造函数引用来把所有东西拼到一起，而不是像以前一样，通过手动实现一个工厂来这么做。
         */
        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Peter", "Parker");
        //我们通过 Person::new 来创建一个Person类构造函数的引用。
        //Java编译器会自动地选择合适的构造函数来匹配PersonFactory.create函数的签名，并选择正确的构造函数形式。

        /**
         * Lambda的范围
         * 对于lambda表达式外部的变量，其访问权限的粒度与匿名对象的方式非常类似。你能够访问局部对应的外部区域的局部final变量，以及成员变量和静态变量。
         * <p>
         * 访问局部变量
         * 我们可以访问lambda表达式外部的final局部变量：
         */
        final int num = 1;
        Converter<Integer, String> stringConverter = (from) -> String.valueOf(from + num);
        stringConverter.convert(2); // 3
        //但是与匿名对象不同的是，变量num并不需要一定是final。下面的代码依然是合法的：
        int num1 = 1;
        Converter<Integer, String> stringConverter1 = (from) -> String.valueOf(from + num);
        stringConverter1.convert(2); // 3
        //        然而， num 在编译的时候被隐式地当做 final 变量来处理。下面的代码就不合法：
        int num3 = 1;
        Converter<Integer, String> stringConverter3 = (from) -> String.valueOf(from + num);
        num3 = 3;
//        在 lambda 表达式内部企图改变num的值也是不允许的。


        /**
         * 访问成员变量和静态变量
         * 与局部变量不同，我们在lambda表达式的内部能获取到对成员变量或静态变量的读写权。这种访问行为在匿名对象里是非常典型的。
         *
         * 访问默认接口方法
         * 还记得第一节里面formula的那个例子么？接口Formula定义了一个默认的方法sqrt，
         * 该方法能够访问formula所有的对象实例，包括匿名对象。这个对lambda表达式来讲则无效。
         * <p>
         * 默认方法无法在lambda表达式内部被访问。因此下面的代码是无法通过编译的：
         */
//        Formula formula2 = (a) -> sqrt(a * 100);

        /**
         * 内置函数式接口
         * JDK 1.8API中包含了很多内置的函数式接口。有些是在以前版本的Java中大家耳熟能详的，例如Comparator接口，或者Runnable接口。
         * 对这些现成的接口进行实现可以通过 @FunctionalInterface 注解来启用Lambda功能支持。
         * 此外，Java 8 API 还提供了很多新的函数式接口，来降低程序员的工作负担。
         * 有些新的接口已经在Google Guava库中很有名了。
         */

        /**
         * Predicates 断言
         * Predicate是一个布尔类型的函数，该函数只有一个输入参数。Predicate接口包含了多种默认方法，用于处理复杂的逻辑动词（and,or，negate）
         */
        Predicate<String> predicate = (s) -> s.length() > 0;
        predicate.test("foo"); // true
        predicate.negate().test("foo"); // false

        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();

        /**
         * Functions
         * Function接口接收一个参数，并返回单一的结果。默认方法可以将多个函数串在一起（compse, andThen）
         */
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        backToString.apply("123"); // "123"


        /**
         * Suppliers
         * Supplier接口产生一个给定类型的结果。与Function不同的是，Supplier没有输入参数。
         */
        Supplier<Person> personSupplier = Person::new;
        personSupplier.get(); // new Person

        /**
         * Consumers
         * Consumer代表了在一个输入参数上需要进行的操作。
         */
        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
        greeter.accept(new Person("Luke", "Skywalker"));

        /**
         * Comparators
         * Comparator接口在早期的Java版本中非常著名。Java 8 为这个接口添加了不同的默认方法。
         */
        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");

        comparator.compare(p1, p2); // > 0
        comparator.reversed().compare(p1, p2); // < 0

        /**
         * Optionals
         * Optional不是一个函数式接口，而是一个精巧的工具接口，用来防止NullPointerEception产生。
         * Optional是一个简单的值容器，这个值可以是null，也可以是non - null。
         * 考虑到一个方法可能会返回一个non - null的值，也可能返回一个空值。
         * 为了不直接返回null，我们在Java 8中就返回一个Optional.
         */
        Optional<String> optional = Optional.of("bam");

        optional.isPresent(); // true
        optional.get(); // "bam"
        optional.orElse("fallback"); // "bam"
        optional.ifPresent((s) -> System.out.println(s.charAt(0))); // "b"


        /**
         * Streams
         * java.util.Stream表示了某一种元素的序列，在这些元素上可以进行各种操作。
         * Stream操作可以是中间操作，也可以是完结操作。
         * 完结操作会返回一个某种类型的值，而中间操作会返回流对象本身，
         * 并且你可以通过多次调用同一个流操作方法来将操作结果串起来
         * （就像StringBuffer的append方法一样）
         * Stream是在一个源的基础上创建出来的，
         * 例如java.util.Collection中的list或者set（map不能作为Stream的源）
         * Stream操作往往可以通过顺序或者并行两种方式来执行。
         *
         * 我们先了解一下序列流。首先，我们通过string类型的list的形式创建示例数据：
         * Java 8 中的Collections类的功能已经有所增强，
         * 你可以之直接通过调用Collections.stream() 或者Collection.parallelStream() 方法
         * 来创建一个流对象。下面的章节会解释这个最常用的操作。
         */
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        /**
         * Filter
         * Filter接受一个predicate接口类型的变量，并将所有流对象中的元素进行过滤。
         * 该操作是一个中间操作，因此它允许我们在返回结果的基础上再进行其他的流操作（forEach）。
         *
         * ForEach接受一个function接口类型的变量，用来执行对每一个元素的操作。ForEach是一个中止操作。
         * 它不返回流，所以我们不能再调用其他的流操作。
         */
        stringCollection.stream().filter((s) -> s.startsWith("a")).forEach(System.out::println);// "aaa2", "aaa1"


        /**
         * Sorted
         * Sorted是一个中间操作，能够返回一个排过序的流对象的视图。流对象中的元素会默认按照自然顺序进行排序，
         * 除非你自己指定一个Comparator接口来改变排序规则。
         * 一定要记住，sorted只是创建一个流对象排序的视图，而不会改变原来集合中元素的顺序。原来string集合中的元素顺序是没有改变的。
         */
        stringCollection.stream().sorted().filter((s) -> s.startsWith("a")).forEach(System.out::println);// "aaa1", "aaa2"

        System.out.println(stringCollection);// ddd2, aaa2, bbb1, aaa1, bbb3, ccc, bbb2, ddd1

        /**
         *  Map
         *  map是一个对于流对象的中间操作，通过给定的方法，它能够把流对象中的每一个元素对应到另外一个对象上。
         *  下面的例子就演示了如何把每个string都转换成大写的string.不但如此，你还可以把每一种对象映射成为其他类型。
         *  对于带泛型结果的流对象，具体的类型还要由传递给map的泛型方法来决定。
         */
        stringCollection.stream().map(String::toUpperCase).sorted((a, b) -> b.compareTo(a)).forEach(System.out::println);// "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"

        /**
         * Match
         * 匹配操作有多种不同的类型，都是用来判断某一种规则是否与流对象相互吻合的。
         * 所有的匹配操作都是终结操作，只返回一个boolean类型的结果。
         */
        boolean anyStartsWithA = stringCollection.stream().anyMatch((s) -> s.startsWith("a"));
        System.out.println(anyStartsWithA); // true

        boolean allStartsWithA = stringCollection.stream().allMatch((s) -> s.startsWith("a"));
        System.out.println(allStartsWithA); // false

        boolean noneStartsWithZ = stringCollection.stream().noneMatch((s) -> s.startsWith("z"));

        System.out.println(noneStartsWithZ); // true

        /**
         *  Count
         *  Count是一个终结操作，它的作用是返回一个数值，用来标识当前流对象中包含的元素数量。
         */
        long startsWithB = stringCollection.stream().filter((s) -> s.startsWith("b")).count();
        System.out.println(startsWithB); // 3


        /**
         * Reduce
         * 该操作是一个终结操作，它能够通过某一个方法，对元素进行削减操作。该操作的结果会放在一个Optional变量里返回。
         */
        Optional<String> reduced = stringCollection.stream().sorted().reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(System.out::println);// "aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2"

        /**
         * Parallel Streams
         * 流操作可以是顺序的，也可以是并行的。顺序操作通过单线程执行，而并行操作则通过多线程执行。
         *
         * 下面的例子就演示了如何使用并行流进行操作来提高运行效率，代码非常简单。
         * 首先我们创建一个大的list，里面的元素都是唯一的：
         */
        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        /**
         * 现在，我们测量一下对这个集合进行排序所使用的时间。
         *  顺序排序
         */
        long t0 = System.nanoTime();
        long count = values.stream().sorted().count();
        System.out.println(count);
        long t1 = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("sequential sort took: %d ms", millis));// sequential sort took: 899 ms

        //并行排序
        long t2 = System.nanoTime();
        long count1 = values.parallelStream().sorted().count();
        System.out.println(count1);
        long t3 = System.nanoTime();
        long millis1 = TimeUnit.NANOSECONDS.toMillis(t3 - t2);
        System.out.println(String.format("parallel sort took: %d ms", millis1));// parallel sort took: 472 ms
        //如你所见，所有的代码段几乎都相同，唯一的不同就是把stream() 改成了parallelStream(), 结果并行排序快了50 %。

        /**
         *  Map
         *  正如前面已经提到的那样，map是不支持流操作的。而更新后的map现在则支持多种实用的新方法，来完成常规的任务。
         */
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);//Absent 缺席的
        }
        map.forEach((id, val) -> System.out.println(val));
        //上面的代码风格是完全自解释的：putIfAbsent避免我们将null写入；forEach接受一个消费者对象，从而将操作实施到每一个map中的值上。
        //下面的这个例子展示了如何使用函数来计算map的编码
        map.computeIfPresent(3, (num2, val) -> val + num2);
        map.get(3); // val33

        map.computeIfPresent(9, (num4, val) -> null);
        map.containsKey(9); // false

        map.computeIfAbsent(23, num5 -> "val" + num5);
        map.containsKey(23); // true

        map.computeIfAbsent(3, num6 -> "bam");
        map.get(3); // val33


        //接下来，我们将学习，当给定一个key值时，如何把一个实例从对应的key中移除：
        map.remove(3, "val3");
        map.get(3); // val33

        map.remove(3, "val33");
        map.get(3); // null


        //另一个有用的方法：
        map.getOrDefault(42, "not found"); // not found
        //将map中的实例合并也是非常容易的：
        map.merge(9, "val9", String::concat);
        map.get(9); // val9

        map.merge(9, "concat", String::concat);
        map.get(9); // val9concat
        //合并操作先看map中是否没有特定的key/value存在，如果是，则把key/value存入map，
        // 否则merging函数就会被调用，对现有的数值进行修改。

        /**
         *  时间日期API
         *  Java 8 包含了全新的时间日期API，这些功能都放在了java.time包下。
         *  新的时间日期API是基于Joda - Time库开发的，但是也不尽相同。下面的例子就涵盖了大多数新的API的重要部分。
         *
         *  Clock
         *  Clock提供了对当前时间和日期的访问功能。Clock是对当前时区敏感的，
         *  并可用于替代System.currentTimeMillis() 方法来获取当前的毫秒时间。当前时间线上的时刻可以用Instance类来表示。
         *  Instance也能够用于创建原先的java.util.Date对象。
         */
        Clock clock = Clock.systemDefaultZone();
        long millis2 = clock.millis();

        Instant instant = clock.instant();
        Date legacyDate = Date.from(instant); // legacy java.util.Date

        /**
         * Timezones
         * 时区类可以用一个ZoneId来表示。时区类的对象可以通过静态工厂方法方便地获取。
         * 时区类还定义了一个偏移量，用来在当前时刻或某时间与目标时区时间之间进行转换。
         */
        System.out.println(ZoneId.getAvailableZoneIds());
        // prints all available timezone ids

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        System.out.println(zone1.getRules());
        System.out.println(zone2.getRules());

        // ZoneRules[currentStandardOffset=+01:00]
        // ZoneRules[currentStandardOffset=-03:00]

        /**
         * LocalTime
         * 本地时间类表示一个没有指定时区的时间，例如，10 p.m.或者17:30:15，
         * 下面的例子会用上面的例子定义的时区创建两个本地时间对象。然后我们会比较两个时间，并计算它们之间的小时和分钟的不同。
         */
        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);
        System.out.println(now1.isBefore(now2)); // false
        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);
        System.out.println(hoursBetween); // -3
        System.out.println(minutesBetween); // -239


        //LocalTime是由多个工厂方法组成，其目的是为了简化对时间对象实例的创建和操作，包括对时间字符串进行解析的操作。
        LocalTime late = LocalTime.of(23, 59, 59);
        System.out.println(late); // 23:59:59

        DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);
        LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
        System.out.println(leetTime); // 13:37

        /**
         *  LocalDate
         *  本地时间表示了一个独一无二的时间，例如：2014 - 03 - 11。
         *  这个时间是不可变的，与LocalTime是同源的。
         *  下面的例子演示了如何通过加减日，月，年等指标来计算新的日期。记住，每一次操作都会返回一个新的时间对象。
         */
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        LocalDate yesterday = tomorrow.minusDays(2);

        LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
        System.out.println(dayOfWeek); // FRIDAY

        //解析字符串并形成LocalDate对象，这个操作和解析LocalTime一样简单。
        DateTimeFormatter germanFormatter2 = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
        LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter2);
        System.out.println(xmas); // 2014-12-24

        /**
         * LocalDateTime
         * LocalDateTime表示的是日期 - 时间。它将刚才介绍的日期对象和时间对象结合起来，形成了一个对象实例。LocalDateTime是不可变的，与LocalTime和LocalDate的工作原理相同。
         * 我们可以通过调用方法来获取日期时间对象中特定的数据域。
         */
        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);
        DayOfWeek dayOfWeek2 = sylvester.getDayOfWeek();
        System.out.println(dayOfWeek2); // WEDNESDAY

        Month month = sylvester.getMonth();
        System.out.println(month); // DECEMBER

        long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println(minuteOfDay); // 1439


        //如果再加上的时区信息，LocalDateTime能够被转换成Instance实例。Instance能够被转换成以前的java.util.Date对象。
        Instant instant1 = sylvester.atZone(ZoneId.systemDefault()).toInstant();
        Date legacyDate1 = Date.from(instant1);
        System.out.println(legacyDate1); // Wed Dec 31 23:59:59 CET 2014

        //格式化日期 - 时间对象就和格式化日期对象或者时间对象一样。
        // 除了使用预定义的格式以外，我们还可以创建自定义的格式化对象，然后匹配我们自定义的格式。
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm");

        LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter);
        String string = formatter.format(parsed);
        System.out.println(string); // Nov 03, 2014 - 07:13
        //不同于 java.text.NumberFormat，新的DateTimeFormatter类是不可变的，也是线程安全的。


        /**
         * Annotations
         * Java 8 中的注解是可重复的。
         */

        //变体1：使用注解容器（老方法）
//        @Hints({@Hint("hint1"), @Hint("hint2")})
//        class Person {
//        }

        //变体2：使用可重复注解（新方法）
//        @Hint("hint1")
//        @Hint("hint2")
//        class Person {
//        }
        //使用变体2，Java编译器能够在内部自动对 @Hint进行设置。这对于通过反射来读取注解信息来说，是非常重要的。

        Hint hint = Person.class.getAnnotation(Hint.class);
        System.out.println(hint); // null

        Hints hints1 = Person.class.getAnnotation(Hints.class);
        System.out.println(hints1.value().length); // 2

        Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
        System.out.println(hints2.length); // 2
        //尽管我们绝对不会在Person类上声 @Hints注解，但是它的信息仍然可以通过getAnnotation(Hints.class) 来读取。
        // 并且，getAnnotationsByType方法会更方便，因为它赋予了所有 @Hints注解的方法直接的访问权限。

    }
}
