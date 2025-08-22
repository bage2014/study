# MessageController API接口文档
## 接口列表
接口名称 请求方法 请求路径 功能描述 发送消息 GET /message/send 发送一条消息给指定用户 发送邮件消息 GET /message/sendEmail 发送邮件消息 标记消息为已读 GET /message/{id}/read 将指定ID的消息标记为已读 删除消息 GET /message/{id} 删除指定ID的消息 查询消息列表 POST /message/query 根据条件查询消息列表

## 接口详情
### 1. 发送消息 请求信息
- 请求方法 : GET
- 请求路径 : /message/send
- 请求参数 : 无
- 请求体 : 无 参数说明
参数名 类型 是否必填 说明 receiverId Long 是 接收者ID content String 是 消息内容
 响应格式
- 响应体 : String
- 成功响应 : 返回"success" 错误响应
错误码 错误信息 描述 400 Bad Request 请求参数错误 500 Internal Server Error 服务器内部错误
 请求示例
```
curl -X GET "http://localhost:8080/
message/send?receiverId=123&
content=HelloWorld"
```
### 2. 发送邮件消息 请求信息
- 请求方法 : GET
- 请求路径 : /message/sendEmail
- 请求参数 : 无
- 请求体 : 无 参数说明
参数名 类型 是否必填 说明 receiverEmail String 是 接收者邮箱 content String 是 邮件内容
 响应格式
- 响应体 : String
- 成功响应 : 返回"success" 错误响应
错误码 错误信息 描述 400 Bad Request 请求参数错误 500 Internal Server Error 服务器内部错误
 请求示例
```
curl -X GET "http://localhost:8080/
message/sendEmail?
receiverEmail=test@example.com&
content=HelloEmail"
```
### 3. 标记消息为已读 请求信息
- 请求方法 : GET
- 请求路径 : /message/{id}/read
- 请求参数 : 无
- 请求体 : 无 参数说明
参数名 类型 是否必填 说明 id Long 是 消息ID
 响应格式
- 响应体 : String
- 成功响应 : 返回"success" 错误响应
错误码 错误信息 描述 400 Bad Request 请求参数错误 404 Not Found 消息不存在 500 Internal Server Error 服务器内部错误
 请求示例
```
curl -X GET "http://localhost:8080/
message/456/read"
```
### 4. 删除消息 请求信息
- 请求方法 : GET
- 请求路径 : /message/{id}
- 请求参数 : 无
- 请求体 : 无 参数说明
参数名 类型 是否必填 说明 id Long 是 消息ID
 响应格式
- 响应体 : String
- 成功响应 : 返回"success" 错误响应
错误码 错误信息 描述 400 Bad Request 请求参数错误 404 Not Found 消息不存在 500 Internal Server Error 服务器内部错误
 请求示例
```
curl -X GET "http://localhost:8080/
message/789"
```
### 5. 查询消息列表 请求信息
- 请求方法 : POST
- 请求路径 : /message/query
- 请求参数 : 无
- 请求体 : MessageQueryRequest对象 参数说明
参数名 类型 是否必填 说明 receiverId Long 否 接收者ID isRead Boolean 否 是否已读 type Integer 否 消息类型 content String 否 消息内容 page Integer 否 页码，默认1 size Integer 否 每页条数，默认10
 响应格式
- 响应体 : MessageListResponse对象 成功响应
```
{
  "code": 0,
  "message": "success",
  "data": {
    "messages": [
      {
        "id": 1,
        "senderId": 1001,
        "receiverId": 1002,
        "content": "测试消息",
        "type": 1,
        "isRead": false,
        "createTime": 
        "2023-05-20T10:30:00",
        "readTime": null,
        "senderName": "张三",
        "senderAvatar": "http://example.
        com/avatar/1001.png",
        "receiverName": "李四",
        "receiverAvatar": "http://example.
        com/avatar/1002.png"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "currentPage": 1,
    "pageSize": 10
  }
}
​``` 错误响应
错误码 错误信息 描述 400 Bad Request 请求参数错误 500 Internal Server Error 服务器内部错误
 请求示例
```
curl -X POST "http://localhost:8080/
message/query" \
  -H "Content-Type: application/json" \
  -d '{"receiverId": 1002, "isRead": 
  false, "page": 1, "size": 10}'
```
## 数据结构
### MessageItem
字段名 类型 说明 id Long 消息ID senderId Long 发送者ID receiverId Long 接收者ID content String 消息内容 type Integer 消息类型 isRead Boolean 是否已读 createTime String 创建时间 readTime String 阅读时间 senderName String 发送者名称 senderAvatar String 发送者头像 receiverName String 接收者名称 receiverAvatar String 接收者头像

### MessageListResponse
字段名 类型 说明 messages List<MessageItem> 消息列表 totalElements Long 总元素数量 totalPages Integer 总页数 currentPage Integer 当前页码 pageSize Integer 每页条数

### MessageQueryRequest
字段名 类型 说明 receiverId Long 接收者ID isRead Boolean 是否已读 type Integer 消息类型 content String 消息内容 page Integer 页码 size Integer 每页条数
```