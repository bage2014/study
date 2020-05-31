# study-iptv #

应用APP 看电视



link 

https://p.codekk.com/detail/Android/mengzhidaren/Vlc-sdk-lib



https://www.videolan.org/vlc/download-android.html



https://code.videolan.org/videolan/vlc-android/



google https://exoplayer.dev/

recycleview https://github.com/Perflyst/Twire



常用类：：

```
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;
```





Web 端显示

https://github.com/Chimeejs/chimee-kernel-hls
https://github.com/Chimeejs/chimee/blob/master/doc/zh-cn/README.md

https://player.alicdn.com/aliplayer/index.html

### 功能概要 ###

| 父模块 | 子模块   | 功能项说明               | 重要、紧急  | 进度 |
| ------ | -------- | ------------------------ | ----------- | ---- |
| TV     | 播放列表 | 加载所有可播放的电视频道 | 重要 + 紧急 | 100% |
|        | 播放列表 | 下拉刷新                 | 重要 + 紧急 | 100% |
|        | 播放页面 | 全屏显示                 | 重要 + 紧急 | 100% |
|        | 播放页面 | 电视播放                 | 重要 + 紧急 | 100% |
|        | 播放页面 | 返回按钮                 | 重要 + 紧急 | 100% |

| API          | URL                | 参数概要 |
| ------------ | ------------------ | -------- |
| 查询播放频道 | /api/tv/query/page |          |
|              | /api/tv/query/all  |          |
|              |                    |          |
|              |                    |          |
|              |                    |          |