# study-xfyun-lfasr #
科大讯飞语音文件转文本

## 参考链接 ##
- 语音转写DEMO [https://doc.xfyun.cn/rest_api/%E8%AF%AD%E9%9F%B3%E8%BD%AC%E5%86%99.html](https://doc.xfyun.cn/rest_api/%E8%AF%AD%E9%9F%B3%E8%BD%AC%E5%86%99.html "语音转写官网")
- 语音转写DEMO2 [https://www.xfyun.cn/doccenter/lfasr#go_operate_guid](https://www.xfyun.cn/doccenter/lfasr#go_operate_guid "语音转写官网")

## 注意事项 ##
- resources\config.properties的store_path属性值，改为比如：E\:/workspace
- 调整循环等待音频处理程序睡眠时间（默认20秒，需要改小，比如500毫秒）

