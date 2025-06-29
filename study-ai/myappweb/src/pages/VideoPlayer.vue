<template>
  <div>
    <!-- 展示视频名称 -->
    <h1>{{ videoName }}</h1>
    <video ref="videoPlayer" controls></video>
    <!-- 错误提示 -->
    <p v-if="errorMessage">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import Hls from 'hls.js';

const route = useRoute();
const videoPlayer = ref(null);
// 新增视频名称变量
const videoName = ref('');
// 新增错误信息变量
const errorMessage = ref('');

onMounted(() => {
  try {
    const url = decodeURIComponent(route.query.url);
    // 解析视频名称
    videoName.value = decodeURIComponent(route.query.name) || '未知视频';

    if (videoPlayer.value && Hls.isSupported()) {
      const hls = new Hls();
      hls.loadSource(url);
      hls.attachMedia(videoPlayer.value);
      hls.on(Hls.Events.MANIFEST_PARSED, () => {
        videoPlayer.value.play();
      });
      // 错误处理
      hls.on(Hls.Events.ERROR, (event, data) => {
        if (data.fatal) {
          switch (data.type) {
            case Hls.ErrorTypes.NETWORK_ERROR:
              errorMessage.value = '网络错误，请检查网络连接。';
              break;
            case Hls.ErrorTypes.MEDIA_ERROR:
              errorMessage.value = '媒体文件错误，请尝试其他视频。';
              break;
            default:
              errorMessage.value = '播放视频时发生未知错误。';
          }
          hls.destroy();
        }
      });
    } else if (videoPlayer.value && videoPlayer.value.canPlayType('application/vnd.apple.mpegurl')) {
      videoPlayer.value.src = url;
      videoPlayer.value.addEventListener('error', () => {
        errorMessage.value = '播放视频时发生错误，请检查视频链接。';
      });
      videoPlayer.value.addEventListener('loadedmetadata', () => {
        videoPlayer.value.play();
      });
    }
  } catch (error) {
    errorMessage.value = `发生错误: ${error.message}`;
  }
});
</script>

<style scoped>
/* 这里可以添加页面样式 */
</style>