package com.bage.web.getpost;

/**
 * GET POST的区别
 * @author bage
 *
 */
public class GetPost {

	public static void main(String[] args) {
		// 区别
		/*
		GET 方法
		请注意，查询字符串（名称/值对）是在 GET 请求的 URL 中发送的：

		/test/demo_form.asp?name1=value1&name2=value2
		有关 GET 请求的其他一些注释：

		GET 请求可被缓存
		GET 请求保留在浏览器历史记录中
		GET 请求可被收藏为书签
		GET 请求不应在处理敏感数据时使用
		GET 请求有长度限制
		GET 请求只应当用于取回数据
		POST 方法
		请注意，查询字符串（名称/值对）是在 POST 请求的 HTTP 消息主体中发送的：

		POST /test/demo_form.asp HTTP/1.1
		Host: w3schools.com
		name1=value1&name2=value2
		有关 POST 请求的其他一些注释：

		POST 请求不会被缓存
		POST 请求不会保留在浏览器历史记录中
		POST 不能被收藏为书签
		POST 请求对数据长度没有要求
		*/
		
		
		/*
		比较 GET 与 POST
		下面的表格比较了两种 HTTP 方法：GET 和 POST。

		 	GET	POST
		后退按钮/刷新	无害	数据会被重新提交（浏览器应该告知用户数据会被重新提交）。
		书签	可收藏为书签	不可收藏为书签
		缓存	能被缓存	不能缓存
		编码类型	application/x-www-form-urlencoded	application/x-www-form-urlencoded 或 multipart/form-data。为二进制数据使用多重编码。
		历史	参数保留在浏览器历史中。	参数不会保存在浏览器历史中。
		对数据长度的限制	是的。当发送数据时，GET 方法向 URL 添加数据；URL 的长度是受限制的（URL 的最大长度是 2048 个字符）。	无限制。
		对数据类型的限制	只允许 ASCII 字符。	没有限制。也允许二进制数据。
		安全性	
		与 POST 相比，GET 的安全性较差，因为所发送的数据是 URL 的一部分。

		在发送密码或其他敏感信息时绝不要使用 GET ！

		POST 比 GET 更安全，因为参数不会被保存在浏览器历史或 web 服务器日志中。
		可见性	数据在 URL 中对所有人都是可见的。	数据不会显示在 URL 中。
		 */
		
	}
}
