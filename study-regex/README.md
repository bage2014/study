# study-regex #

学习正则表达式的笔记

## 参考链接 ##
- learn-regex [https://github.com/ziishaned/learn-regex/blob/master/translations/README-cn.md](https://github.com/ziishaned/learn-regex/blob/master/translations/README-cn.md)
- 101 https://regex101.com/r/dmRygT/1
- 验证方式 浏览器（"The cat sat on cat.".match(/.at/g)）

## 元字符 ##

| 元字符 | 描述                                                         |
| ------ | ------------------------------------------------------------ |
| .      | 匹配任意单个字符，换行符除外！                               |
| [ ]    | 匹配方括号内的任意字符                                       |
| [^ ]   | 否定的字符种类。匹配除了方括号里的任意字符                   |
| *      | 匹配>=0个重复的在*号之前的字符。                             |
| +      | 匹配>=1个重复的+号前的字符。                                 |
| ?      | 标记?之前的字符为可选.                                       |
| {n,m}  | 匹配num个大括号之间的字符 (n <= num <= m).                   |
| (xyz)  | 字符集，匹配与 xyz 完全相等的字符串.                         |
| \|     | 或运算符，匹配符号前或后的字符.                              |
| \      | 转义字符,用于匹配一些保留的字符 `[ ] ( ) { } . * + ? ^ $ \ |` |
| ^      | 从开始行开始匹配.                                            |
| $      | 从末端开始匹配.                                              |

### 点运算符 . 

`.`匹配任意单个字符，但不匹配换行符。
例如，表达式`.ar`匹配一个任意字符后面跟着是`a`和`r`的字符串。

<pre>
".ar" => The <a href="#learn-regex"><strong>car</strong></a> <a href="#learn-regex"><strong>par</strong></a>ked in the <a href="#learn-regex"><strong>gar</strong></a>age.
</pre>

### 字符集 []

表达式 `ar[.]` 匹配 `ar.`字符串

<pre>
"ar[.]" => A garage is a good place to park a c<a href="#learn-regex"><strong>ar.</strong></a>
</pre>

### 否定字符集 ^

一般来说 `^` 表示一个字符串的开头，但它用在一个方括号的开头的时候，它表示这个字符集是否定的。
例如，表达式`[^c]ar` 匹配一个后面跟着`ar`的除了`c`的任意字符。

<pre>
"[^c]ar" => The car <a href="#learn-regex"><strong>par</strong></a>ked in the <a href="#learn-regex"><strong>gar</strong></a>age.
</pre>

### 重复次数 + * ?

后面跟着元字符 `+`，`*` or `?` 的，用来指定匹配子模式的次数。
这些元字符在不同的情况下有着不同的意思。

<pre>
"(at)+" => The f<a href="#learn-regex"><strong>at</strong></a> <a href="#learn-regex"><strong>catat</strong></a> on the mt.
</pre>

### 重复次数{} 

在正则表达式中 `{}` 是一个量词，常用来一个或一组字符可以重复出现的次数。
例如， 表达式 `[0-9]{2,3}` 匹配最少 2 位最多 3 位 0~9 的数字。

<pre>
"[0-9]{2,3}" => The number was 9.<a href="#learn-regex"><strong>999</strong></a>7 but we rounded it off to <a href="#learn-regex"><strong>10</strong></a>.0.
</pre>

### 特征标群(...) 

特征标群是一组写在 `(...)` 中的子模式。例如之前说的 `{}` 是用来表示前面一个字符出现指定次数。但如果在 `{}` 前加入特征标群则表示整个标群内的字符重复 N 次。例如，表达式 `(ab)*` 匹配连续出现 0 或更多个 `ab`。

我们还可以在 `()` 中用或字符 `|` 表示或。例如，`(c|g|p)ar` 匹配 `car` 或 `gar` 或 `par`.

<pre>
"(c|g|p)ar" => The <a href="#learn-regex"><strong>car</strong></a> is <a href="#learn-regex"><strong>par</strong></a>ked in the <a href="#learn-regex"><strong>gar</strong></a>age.
</pre>

###  或运算符|

或运算符就表示或，用作判断条件。

例如 `(T|t)he|car` 匹配 `(T|t)he` 或 `car`。

<pre>
"(T|t)he|car" => <a href="#learn-regex"><strong>The</strong></a> <a href="#learn-regex"><strong>car</strong></a> is parked in <a href="#learn-regex"><strong>the</strong></a> garage.
</pre>

### 转码特殊字符\

反斜线 `\` 在表达式中用于转码紧跟其后的字符。用于指定 `{ } [ ] / \ + * . $ ^ | ?` 这些特殊字符。如果想要匹配这些特殊字符则要在其前面加上反斜线 `\`。

例如 `.` 是用来匹配除换行符外的所有字符的。如果想要匹配句子中的 `.` 则要写成 `\.` 以下这个例子 `\.?`是选择性匹配`.`

<pre>
"(f|c|m)at\.?" => The <a href="#learn-regex"><strong>fat</strong></a> <a href="#learn-regex"><strong>cat</strong></a> sat on the <a href="#learn-regex"><strong>mat.</strong></a>
</pre>

### 锚点 ^ $

在正则表达式中，想要匹配指定开头或结尾的字符串就要使用到锚点。`^` 指定开头，`$` 指定结尾。

<pre>
"(at)+.$" => The fat catat on the m<a href="#learn-regex"><strong>at.</strong></a>
</pre>

## 简写字符集

| 简写 | 描述                                               |
| ---- | -------------------------------------------------- |
| .    | 除换行符外的所有字符                               |
| \w   | 匹配所有字母数字，等同于 `[a-zA-Z0-9_]`            |
| \W   | 匹配所有非字母数字，即符号，等同于： `[^\w]`       |
| \d   | 匹配数字： `[0-9]`                                 |
| \D   | 匹配非数字： `[^\d]`                               |
| \s   | 匹配所有空格字符，等同于： `[\t\n\f\r\p{Z}]`       |
| \S   | 匹配所有非空格字符： `[^\s]`                       |
| \f   | 匹配一个换页符                                     |
| \n   | 匹配一个换行符                                     |
| \r   | 匹配一个回车符                                     |
| \t   | 匹配一个制表符                                     |
| \v   | 匹配一个垂直制表符                                 |
| \p   | 匹配 CR/LF（等同于 `\r\n`），用来匹配 DOS 行终止符 |

## 零宽度断言（前后预查）

| 符号 | 描述            |
| ---- | --------------- |
| ?=   | 正先行断言-存在 |
| ?!   | 负先行断言-排除 |
| ?<=  | 正后发断言-存在 |
| ?<!  | 负后发断言-排除 |

### 正先行断言?=

例如，表达式 `(T|t)he(?=\sfat)` 匹配 `The` 和 `the`，在括号中我们又定义了正先行断言 `(?=\sfat)` ，即 `The` 和 `the` 后面紧跟着 `(空格)fat`。

<pre>
"(T|t)he(?=\sfat)" => <a href="#learn-regex"><strong>The</strong></a> fat cat sat on the mat.
</pre>

### 负先行断言?!

负先行断言 `?!` 用于筛选所有匹配结果，筛选条件为 其后不跟随着断言中定义的格式。
`正先行断言`  定义和 `负先行断言` 一样，区别就是 `=` 替换成 `!` 也就是 `(?!...)`。

表达式 `(T|t)he(?!\sfat)` 匹配 `The` 和 `the`，且其后不跟着 `(空格)fat`。

<pre>
"(T|t)he(?!\sfat)" => The fat cat sat on <a href="#learn-regex"><strong>the</strong></a> mat.
</pre>

### 正后发断言?<=

正后发断言 记作`(?<=...)` 用于筛选所有匹配结果，筛选条件为 其前跟随着断言中定义的格式。
例如，表达式 `(?<=(T|t)he\s)(fat|mat)` 匹配 `fat` 和 `mat`，且其前跟着 `The` 或 `the`。

<pre>
"(?<=(T|t)he\s)(fat|mat)" => The <a href="#learn-regex"><strong>fat</strong></a> cat sat on the <a href="#learn-regex"><strong>mat</strong></a>.
</pre>

### 负后发断言?<!

负后发断言 记作 `(?<!...)` 用于筛选所有匹配结果，筛选条件为 其前不跟随着断言中定义的格式。
例如，表达式 `(?<!(T|t)he\s)(cat)` 匹配 `cat`，且其前不跟着 `The` 或 `the`。

<pre>
"(?&lt;!(T|t)he\s)(cat)" => The cat sat on <a href="#learn-regex"><strong>cat</strong></a>.
</pre>

## 标志

| 标志 | 描述                                                  |
| ---- | ----------------------------------------------------- |
| i    | 忽略大小写。                                          |
| g    | 全局搜索。                                            |
| m    | 多行修饰符：锚点元字符 `^` `$` 工作范围在每行的起始。 |

### 忽略大小写i

修饰语 `i` 用于忽略大小写。
例如，表达式 `/The/gi` 表示在全局搜索 `The`，在后面的 `i` 将其条件修改为忽略大小写，则变成搜索 `the` 和 `The`，`g` 表示全局搜索。

<pre>
"The" => <a href="#learn-regex"><strong>The</strong></a> fat cat sat on the mat.
</pre>

<pre>
"/The/gi" => <a href="#learn-regex"><strong>The</strong></a> fat cat sat on <a href="#learn-regex"><strong>the</strong></a> mat.
</pre>

### 全局搜索g

修饰符 `g` 常用于执行一个全局搜索匹配，即（不仅仅返回第一个匹配的，而是返回全部）。
例如，表达式 `/.(at)/g` 表示搜索 任意字符（除了换行）+ `at`，并返回全部结果。

<pre>
"/.(at)/" => The <a href="#learn-regex"><strong>fat</strong></a> cat sat on the mat.
</pre>

<pre>
"/.(at)/g" => The <a href="#learn-regex"><strong>fat</strong></a> <a href="#learn-regex"><strong>cat</strong></a> <a href="#learn-regex"><strong>sat</strong></a> on the <a href="#learn-regex"><strong>mat</strong></a>.
</pre>

### 多行修饰符m

多行修饰符 `m` 常用于执行一个多行匹配。

像之前介绍的 `(^,$)` 用于检查格式是否是在待检测字符串的开头或结尾。但我们如果想要它在每行的开头和结尾生效，我们需要用到多行修饰符 `m`。

例如，表达式 `/at(.)?$/gm` 表示小写字符 `a` 后跟小写字符 `t` ，末尾可选除换行符外任意字符。根据 `m` 修饰符，现在表达式匹配每行的结尾。

<pre>
"/.at(.)?$/" => The fat
                cat sat
                on the <a href="#learn-regex"><strong>mat.</strong></a>
</pre>

<pre>
"/.at(.)?$/gm" => The <a href="#learn-regex"><strong>fat</strong></a>
                  cat <a href="#learn-regex"><strong>sat</strong></a>
                  on the <a href="#learn-regex"><strong>mat.</strong></a>
</pre>

## 贪婪匹配与惰性匹配（Greedy vs lazy matching）

正则表达式默认采用贪婪匹配模式，在该模式下意味着会匹配尽可能长的子串。我们可以使用 `?` 将贪婪匹配模式转化为惰性匹配模式。

