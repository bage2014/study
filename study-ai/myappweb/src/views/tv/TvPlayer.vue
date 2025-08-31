<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Hls from 'hls.js'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const route = useRoute()
const router = useRouter()
const videoRef = ref<HTMLVideoElement | null>(null)
const hls = ref<Hls | null>(null)
const loading = ref(true)

// 添加提示消息状态
const message = ref('')
const messageType = ref('') // 'error', 'warning', 'success'
const showMessage = ref(false)

// 显示提示消息的函数
const showToast = (msg: string, type: 'error' | 'warning' | 'success' = 'error') => {
  message.value = msg
  messageType.value = type
  showMessage.value = true
  
  // 3秒后自动隐藏
  setTimeout(() => {
    showMessage.value = false
  }, 3000)
}

// 从路由参数中获取视频URL和标题
const videoUrl = ref(route.query.url as string || '')
const title = ref(route.query.title as string || 'TV Channel')

// 初始化播放器
const initPlayer = () => {
  if (!videoUrl.value) {
    showToast('视频URL不能为空', 'error')
    loading.value = false
    return
  }

  if (!videoRef.value) {
    loading.value = false
    return
  }

  // 检查浏览器是否支持HLS
  if (Hls.isSupported()) {
    hls.value = new Hls()
    
    // 监听HLS事件
    hls.value.on(Hls.Events.MANIFEST_PARSED, () => {
      loading.value = false
      videoRef.value?.play().catch(error => {
        console.error('自动播放失败:', error)
        showToast('自动播放失败，请手动点击播放', 'warning')
      })
    })
    
    hls.value.on(Hls.Events.ERROR, (_event, data) => {
      loading.value = false
      console.error('HLS播放错误:', data)
      showToast(`播放失败: ${data.details || '未知错误'}`, 'error')
    })
    
    // 加载视频源
    try {
      hls.value.loadSource(videoUrl.value)
      hls.value.attachMedia(videoRef.value)
    } catch (error) {
      loading.value = false
      console.error('加载视频源失败:', error)
      showToast('加载视频源失败', 'error')
    }
  } else if (videoRef.value.canPlayType('application/vnd.apple.mpegurl')) {
    // 对于原生支持HLS的浏览器（如Safari）
    videoRef.value.src = videoUrl.value
    videoRef.value.addEventListener('loadedmetadata', () => {
      loading.value = false
      videoRef.value?.play().catch(error => {
        console.error('自动播放失败:', error)
        showToast('自动播放失败，请手动点击播放', 'warning')
      })
    })
    
    videoRef.value.addEventListener('error', (error) => {
      loading.value = false
      console.error('视频播放错误:', error)
      showToast('视频播放失败', 'error')
    })
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 页面加载时初始化播放器
onMounted(() => {
  initPlayer()
})

// 页面卸载时清理资源
onUnmounted(() => {
  if (hls.value) {
    hls.value.destroy()
    hls.value = null
  }
})
</script>

<template>
  <div class="tv-player-page">
    <!-- 添加提示消息组件 -->
    <div v-if="showMessage" :class="['toast-message', messageType]">
      {{ message }}
    </div>
    
    <div class="tv-player-header">
      <button class="back-button" @click="goBack">
        ← 返回
      </button>
      <h2>{{ title }}</h2>
    </div>
    
    <div class="tv-player-container">
      <div class="video-wrapper">
        <div v-if="loading" class="loading-overlay">
          <div class="loading-spinner"></div>
          <span>{{ t('message.loading') }}</span>
        </div>
        <video
          ref="videoRef"
          controls
          autoplay
          muted
          playsinline
          class="tv-player"
          poster="data:image/svg+xml;charset=UTF-8,%3csvg xmlns%3d%22http%3a%2f%2fwww.w3.org%2f2000%2fsvg%22 width%3d%22800%22 height%3d%22450%22 viewBox%3d%220 0 800 450%22 fill%3d%22none%22%3e%3crect width%3d%22800%22 height%3d%22450%22 fill%3d%22%231a1a1a%22%3e%3c%2frect%3e%3cpath d%3d%22M300 300L500 225L300 150V300Z%22 fill%3d%22%23ffffff%22%3e%3c%2fpath%3e%3c%2fsvg%3e"
        >
          您的浏览器不支持HTML5视频播放
        </video>
      </div>
      
      <div class="video-info">
        <div class="video-url-display">
          <strong>{{ t('label.currentUrl') }}:</strong> {{ videoUrl || '无URL' }}
        </div>
        <div class="playback-tips">
          <p>{{ t('message.playbackTips') }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.tv-player-page {
  padding: 1rem;
  max-width: 1200px;
  margin: 0 auto;
}

/* 添加提示消息样式 */
.toast-message {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 12px 20px;
  border-radius: 4px;
  color: white;
  font-weight: 500;
  z-index: 1000;
  animation: slideIn 0.3s ease-out;
  max-width: 300px;
  word-wrap: break-word;
}

.toast-message.error {
  background-color: #f56c6c;
}

.toast-message.warning {
  background-color: #e6a23c;
}

.toast-message.success {
  background-color: #67c23a;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.tv-player-header {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
  gap: 1rem;
}

.back-button {
  padding: 0.5rem 1rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  transition: background-color 0.3s;
}

.back-button:hover {
  background-color: var(--primary-dark);
}

.tv-player-header h2 {
  color: var(--text-color);
  margin: 0;
  font-size: 1.5rem;
}

.tv-player-container {
  background-color: var(--bg-light);
  border-radius: var(--radius);
  padding: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.video-wrapper {
  position: relative;
  width: 100%;
  max-width: 100%;
  margin-bottom: 1rem;
  background-color: #000;
  border-radius: var(--radius);
  overflow: hidden;
}

/* 保持16:9的宽高比 */
.video-wrapper::before {
  content: '';
  display: block;
  padding-top: 56.25%; /* 16:9 比例 */
}

.tv-player {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: contain;
  background-color: #000;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.video-info {
  background-color: white;
  padding: 1rem;
  border-radius: var(--radius);
}

.video-url-display {
  margin-bottom: 1rem;
  word-break: break-all;
  font-size: 0.9rem;
  color: var(--text-color);
}

.playback-tips {
  font-size: 0.85rem;
  color: var(--text-light);
}

@media (max-width: 768px) {
  .tv-player-page {
    padding: 0.5rem;
  }
  
  .tv-player-header h2 {
    font-size: 1.2rem;
  }
  
  .toast-message {
    right: 10px;
    left: 10px;
    max-width: none;
  }
}
</style>