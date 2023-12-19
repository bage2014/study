package com.bage.jdk9;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 作者：长安不及十里
 链接：https://juejin.cn/post/7250734439709048869
 来源：稀土掘金
 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class StreamTest {

    public static void main(String[] args) {

        // takeWhile() 方法示例
        List<Integer> numbers = Stream.of(1, 2, 3, 4, 5, 6)
                .takeWhile(n -> n < 4)
                .collect(Collectors.toList());
        System.out.println("takeWhile 示例：" + numbers); // 输出：[1, 2, 3]

        // dropWhile() 方法示例
        List<Integer> numbers2 = Stream.of(1, 2, 3, 4, 5, 6)
                .dropWhile(n -> n < 4)
                .collect(Collectors.toList());
        System.out.println("dropWhile 示例：" + numbers2); // 输出：[4, 5, 6]

        // ofNullable() 方法示例
        String name = null;
        List<String> names = Stream.ofNullable(name)
                .collect(Collectors.toList());
        System.out.println("ofNullable 示例：" + names); // 输出：[]

        // iterate() 方法的重载示例
        List<Integer> evenNumbers = Stream.iterate(0, n -> n < 10, n -> n + 2)
                .collect(Collectors.toList());
        System.out.println("iterate 重载示例：" + evenNumbers); // 输出：[0, 2, 4, 6, 8]

        //

        //Stream 接口中的新方法示例
        Stream.of("Java", "Python", "C++")
                .forEachOrdered(System.out::println); // 输出：Java Python C++

    }
}
