# study-cryption #
常用加密算法实现

## 参考链接 ##
- 谷歌CryptoJS： [https://github.com/brix/crypto-js](https://github.com/brix/crypto-js)
- Bitwiseshiftleft-sjcl：[https://github.com/bitwiseshiftleft/sjcl](https://github.com/bitwiseshiftleft/sjcl)
- lugeek实现：[https://github.com/lugeek/Encryption/](https://github.com/lugeek/Encryption/)
- 【Java安全】关于Java中常用加密/解密方法的实现：[https://www.cnblogs.com/tancky/p/6409823.html](https://www.cnblogs.com/tancky/p/6409823.html)
- JAVA中常用的加密算法总结：[https://blog.csdn.net/chenbing81/article/details/51914151](https://blog.csdn.net/chenbing81/article/details/51914151)、[http://blog.sina.com.cn/s/blog_6259ebd50102v57c.html](http://blog.sina.com.cn/s/blog_6259ebd50102v57c.html)


## 常用算法 ##

### base64 ###

Base64是网络上最常见的用于传输8Bit字节码的编码方式之一，Base64就是一种基于64个可打印字符来表示二进制数据的方法。
Base64要求把每三个8Bit的字节转换为四个6Bit的字节（3*8 = 4*6 = 24），然后把6Bit再添两位高位0，组成四个8Bit的字节，也就是说，**转换后的字符串理论上将要比原来的长1/3**。
ase64索引表，字符选用了"A-Z、a-z、0-9、+、/" 64个可打印字符


- 算法思想
如果是字符串转换为Base64码， 会先把对应的字符串转换为ascll码表对应的数字， 然后再把数字转换为2进制， 比如a的ascll码味97， 97的二进制是：01100001， 把8个二进制提取成6个，剩下的2个二进制和后面的二进制继续拼接， 最后再把6个二进制码转换为Base64对于的编码，（当转换到最后， 最后的字符不足3个字符咋办， 如果不足三个字符的话，我们直接在最后添加‘＝’）

- 应用场景
1.	加密URL
2.	传输二进制数据，如邮件主题及图片等
3.	加密cookie

### AES ###
参考链接 [https://blog.csdn.net/simple_man_just/article/details/69258923](https://blog.csdn.net/simple_man_just/article/details/69258923)

密码算法要求是可逆的，这样解密算法才能正确的恢复明文。拿AES来说，在密钥固定的情况下，明文和密文在整个输入空间是一一对应的。因此算法的各个部件也都是可逆的，再将各个部件的操作顺序设计成可逆的，密文就能正确的解密了。

美国国家标准技术研究所在2001年发布了高级加密标准（AES）。AES是一个对称分组密码算法，旨在取代DES成为广泛使用的标准。
 
根据使用的密码长度，AES最常见的有3种方案，用以适应不同的场景要求，分别是AES-128、AES-192和AES-256。本文主要对AES-128进行介绍，另外两种的思路基本一样，只是轮数会适当增加











