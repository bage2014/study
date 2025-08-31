<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const mapRef = ref<HTMLDivElement>()
const mapInstance = ref<any>(null)
const polylineInstance = ref<any>(null)
const markerInstance = ref<any>(null)
const animationInterval = ref<any>(null)
const isLoading = ref(true)
const error = ref('')
const isAnimationRunning = ref(false)

// 轨迹数据 - 可以从API获取
const trajectoryData = ref([
  { lng: 116.397428, lat: 39.90923, name: '起点' }, // 北京
  { lng: 116.407428, lat: 39.91923, name: '位置1' },
  { lng: 116.417428, lat: 39.92923, name: '位置2' },
  { lng: 116.427428, lat: 39.93923, name: '位置3' },
  { lng: 116.437428, lat: 39.94923, name: '位置4' },
  { lng: 116.447428, lat: 39.93923, name: '位置5' },
  { lng: 116.457428, lat: 39.92923, name: '终点' }
])

// 当前动画位置索引
const currentIndex = ref(0)
const animationSpeed = ref(1000) // 动画速度（毫秒）

// 计算当前点
const currentPoint = computed(() => {
  return trajectoryData.value[currentIndex.value]
})

// 计算进度百分比
const progressPercentage = computed(() => {
  return ((currentIndex.value + 1) / trajectoryData.value.length) * 100
})

// 初始化百度地图
const initBaiduMap = () => {
  return new Promise<void>((resolve, reject) => {
    if (typeof BMap !== 'undefined') {
      resolve()
      return
    }

    // 检查是否已加载脚本
    const existingScript = document.querySelector('script[src*="api.map.baidu.com"]')
    if (existingScript) {
      const checkLoaded = setInterval(() => {
        if (typeof BMap !== 'undefined') {
          clearInterval(checkLoaded)
          resolve()
        }
      }, 100)
      return
    }

    // 定义全局回调函数
    (window as any).initBaiduMapCallback = () => {
      resolve()
    }
    
  })
}

// 初始化地图
const initMap = async () => {
  try {
    isLoading.value = true
    error.value = ''

    // 加载百度地图API
    await initBaiduMap()

    if (!mapRef.value) {
      throw new Error('地图容器不存在')
    }

    // 创建地图实例
    mapInstance.value = new BMap.Map(mapRef.value)

    // 设置中心点和缩放级别
    const startPoint = trajectoryData.value[0]
    const point = new BMap.Point(startPoint.lng, startPoint.lat)
    mapInstance.value.centerAndZoom(point, 15)

    // 添加地图控件
    mapInstance.value.addControl(new BMap.NavigationControl({ type: BMAP_NAVIGATION_CONTROL_LARGE }))
    mapInstance.value.addControl(new BMap.ScaleControl())
    mapInstance.value.addControl(new BMap.OverviewMapControl())
    mapInstance.value.addControl(new BMap.MapTypeControl())

    // 启用地图滚轮缩放
    mapInstance.value.enableScrollWheelZoom(true)

    // 绘制轨迹
    await drawTrajectory()

    isLoading.value = false
  } catch (err) {
    console.error('地图初始化失败:', err)
    error.value = '地图加载失败，请检查网络连接'
    isLoading.value = false
  }
}

// 绘制轨迹函数
const drawTrajectory = () => {
  if (!mapInstance.value) return

  try {
    // 清除已有轨迹
    if (polylineInstance.value) {
      mapInstance.value.removeOverlay(polylineInstance.value)
    }

    // 创建坐标点数组
    const points = trajectoryData.value.map(item => new BMap.Point(item.lng, item.lat))

    // 创建折线
    polylineInstance.value = new BMap.Polyline(points, {
      strokeColor: '#1E90FF',
      strokeWeight: 4,
      strokeOpacity: 0.8,
      strokeStyle: 'solid'
    })

    // 添加折线到地图
    mapInstance.value.addOverlay(polylineInstance.value)

    // 添加起点和终点标记
    const startMarker = new BMap.Marker(points[0])
    const endMarker = new BMap.Marker(points[points.length - 1])
    mapInstance.value.addOverlay(startMarker)
    mapInstance.value.addOverlay(endMarker)

    // 设置信息窗口
    const startInfo = new BMap.InfoWindow(`<div style="padding: 10px;"><strong>${trajectoryData.value[0].name}</strong><br>坐标: ${trajectoryData.value[0].lng.toFixed(6)}, ${trajectoryData.value[0].lat.toFixed(6)}</div>`)
    const endInfo = new BMap.InfoWindow(`<div style="padding: 10px;"><strong>${trajectoryData.value[trajectoryData.value.length - 1].name}</strong><br>坐标: ${trajectoryData.value[trajectoryData.value.length - 1].lng.toFixed(6)}, ${trajectoryData.value[trajectoryData.value.length - 1].lat.toFixed(6)}</div>`)
    
    startMarker.addEventListener('click', () => startMarker.openInfoWindow(startInfo))
    endMarker.addEventListener('click', () => endMarker.openInfoWindow(endInfo))

    // 添加移动标记
    if (markerInstance.value) {
      mapInstance.value.removeOverlay(markerInstance.value)
    }
    
    markerInstance.value = new BMap.Marker(points[0], {
      icon: new BMap.Icon('https://api.map.baidu.com/img/markers.png', new BMap.Size(23, 25), {
        offset: new BMap.Size(10, 25),
        imageOffset: new BMap.Size(0, 0 - 10 * 25)
      })
    })
    mapInstance.value.addOverlay(markerInstance.value)

    // 调整地图视野以显示全部轨迹
    mapInstance.value.setViewport(points, { padding: [50, 50, 50, 50] })
  } catch (err) {
    console.error('绘制轨迹失败:', err)
    error.value = '轨迹绘制失败'
  }
}

// 开始动画
const startAnimation = () => {
  if (animationInterval.value) {
    stopAnimation()
  }

  isAnimationRunning.value = true
  
  animationInterval.value = setInterval(() => {
    if (currentIndex.value >= trajectoryData.value.length - 1) {
      // 到达终点，可以选择停止或循环
      stopAnimation()
      // 如果要循环，取消下面这行的注释
      // currentIndex.value = 0
    } else {
      currentIndex.value++
    }

    // 更新标记位置
    if (markerInstance.value && mapInstance.value) {
      const point = new BMap.Point(trajectoryData.value[currentIndex.value].lng, trajectoryData.value[currentIndex.value].lat)
      markerInstance.value.setPosition(point)

      // 显示当前位置信息
      const currentData = trajectoryData.value[currentIndex.value]
      const info = new BMap.InfoWindow(`<div style="padding: 10px;"><strong>${currentData.name}</strong><br>坐标: ${currentData.lng.toFixed(6)}, ${currentData.lat.toFixed(6)}<br>进度: ${currentIndex.value + 1}/${trajectoryData.value.length}</div>`)
      markerInstance.value.openInfoWindow(info)
    }
  }, animationSpeed.value)
}

// 停止动画
const stopAnimation = () => {
  if (animationInterval.value) {
    clearInterval(animationInterval.value)
    animationInterval.value = null
  }
  isAnimationRunning.value = false
}

// 重置动画
const resetAnimation = () => {
  stopAnimation()
  currentIndex.value = 0
  if (markerInstance.value && mapInstance.value) {
    const point = new BMap.Point(trajectoryData.value[0].lng, trajectoryData.value[0].lat)
    markerInstance.value.setPosition(point)
  }
}

// 调整动画速度
const changeSpeed = (speed: number) => {
  animationSpeed.value = speed
  // 如果动画正在运行，重新启动以应用新速度
  if (isAnimationRunning.value) {
    stopAnimation()
    startAnimation()
  }
}

// 生命周期钩子
onMounted(() => {
  initMap()
})

onUnmounted(() => {
  // 清理地图实例
  if (mapInstance.value) {
    mapInstance.value.clearOverlays()
    mapInstance.value = null
  }
  
  // 移除全局函数
  if ((window as any).initBaiduMapCallback) {
    delete (window as any).initBaiduMapCallback
  }
  
  // 清除动画
  stopAnimation()
})
</script>

<template>
  <div class="map-trajectory-container">
    <!-- 头部信息 -->
    <div class="map-header">
      <div class="header-left">
        <h2>{{ t('map.trajectoryTitle') || '地图轨迹展示' }}</h2>
        <div class="location-info" v-if="!isLoading && !error">
          <span class="location-label">{{ t('map.currentLocation') || '当前位置' }}:</span>
          <span class="location-coords">{{ currentPoint.lng.toFixed(6) }}, {{ currentPoint.lat.toFixed(6) }}</span>
          <span class="location-name">- {{ currentPoint.name }}</span>
        </div>
      </div>
      <div class="header-right">
        <div class="progress-info" v-if="!isLoading && !error">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: progressPercentage + '%' }"></div>
          </div>
          <span class="progress-text">{{ currentIndex + 1 }}/{{ trajectoryData.length }}</span>
        </div>
      </div>
    </div>

    <!-- 地图容器 -->
    <div class="map-wrapper">
      <!-- 加载状态 -->
      <div v-if="isLoading" class="loading-overlay">
        <div class="loading-spinner"></div>
        <span>{{ t('map.loadingMap') || '地图加载中...' }}</span>
      </div>
      
      <!-- 错误状态 -->
      <div v-if="error" class="error-overlay">
        <div class="error-icon">⚠️</div>
        <div class="error-message">{{ error }}</div>
        <button class="retry-button" @click="initMap">{{ t('map.retry') || '重试' }}</button>
      </div>
      
      <!-- 地图 -->
      <div ref="mapRef" class="map"></div>
    </div>

    <!-- 控制面板 -->
    <div class="control-panel">
      <div class="control-group">
        <button 
          @click="isAnimationRunning ? stopAnimation() : startAnimation()"
          :class="['action-button', isAnimationRunning ? 'stop-button' : 'start-button']"
        >
          {{ isAnimationRunning ? (t('map.stopAnimation') || '停止动画') : (t('map.startAnimation') || '开始动画') }}
        </button>
        
        <button @click="resetAnimation" class="action-button reset-button">
          {{ t('map.resetAnimation') || '重置动画' }}
        </button>
        
        <button @click="drawTrajectory" class="action-button redraw-button">
          {{ t('map.redrawTrajectory') || '重新绘制轨迹' }}
        </button>
      </div>
      
      <div class="speed-control">
        <span class="speed-label">{{ t('map.animationSpeed') || '动画速度' }}:</span>
        <div class="speed-buttons">
          <button 
            @click="changeSpeed(2000)" 
            :class="['speed-button', { active: animationSpeed === 2000 }]"
          >
            {{ t('map.slow') || '慢速' }}
          </button>
          <button 
            @click="changeSpeed(1000)" 
            :class="['speed-button', { active: animationSpeed === 1000 }]"
          >
            {{ t('map.normal') || '正常' }}
          </button>
          <button 
            @click="changeSpeed(500)" 
            :class="['speed-button', { active: animationSpeed === 500 }]"
          >
            {{ t('map.fast') || '快速' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.map-trajectory-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-color, #f5f5f5);
}

.map-header {
  background-color: var(--card-bg, #fff);
  padding: 16px 24px;
  border-bottom: 1px solid var(--border-color, #e0e0e0);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

h2 {
  font-size: 20px;
  color: var(--text-primary, #333);
  margin: 0;
  font-weight: 600;
}

.location-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.location-label {
  color: var(--text-secondary, #666);
}

.location-coords {
  color: var(--text-primary, #333);
  font-family: 'Courier New', monospace;
  font-weight: 500;
}

.location-name {
  color: var(--primary-color, #1E90FF);
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
}

.progress-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.progress-bar {
  width: 120px;
  height: 6px;
  background-color: var(--progress-bg, #e0e0e0);
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: var(--primary-color, #1E90FF);
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 12px;
  color: var(--text-secondary, #666);
  min-width: 40px;
}

.map-wrapper {
  flex: 1;
  position: relative;
  min-height: 400px;
}

.map {
  width: 100%;
  height: 100%;
  border: none;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(30, 144, 255, 0.3);
  border-top-color: var(--primary-color, #1E90FF);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 12px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.95);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 10;
  gap: 16px;
}

.error-icon {
  font-size: 48px;
}

.error-message {
  font-size: 16px;
  color: var(--error-color, #f44336);
  text-align: center;
  max-width: 300px;
}

.retry-button {
  padding: 8px 16px;
  background-color: var(--primary-color, #1E90FF);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.retry-button:hover {
  background-color: var(--primary-dark, #1876CC);
}

.control-panel {
  background-color: var(--card-bg, #fff);
  padding: 16px 24px;
  border-top: 1px solid var(--border-color, #e0e0e0);
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.control-group {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.action-button {
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  min-width: 120px;
}

.start-button {
  background-color: var(--success-color, #4CAF50);
  color: white;
}

.start-button:hover {
  background-color: var(--success-dark, #45a049);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
}

.stop-button {
  background-color: var(--error-color, #f44336);
  color: white;
}

.stop-button:hover {
  background-color: var(--error-dark, #da190b);
  box-shadow: 0 2px 8px rgba(244, 67, 54, 0.3);
}

.reset-button {
  background-color: var(--warning-color, #FF9800);
  color: white;
}

.reset-button:hover {
  background-color: var(--warning-dark, #F57C00);
  box-shadow: 0 2px 8px rgba(255, 152, 0, 0.3);
}

.redraw-button {
  background-color: var(--info-color, #2196F3);
  color: white;
}

.redraw-button:hover {
  background-color: var(--info-dark, #1976D2);
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
}

.action-button:active {
  transform: translateY(1px);
}

.speed-control {
  display: flex;
  align-items: center;
  gap: 12px;
}

.speed-label {
  font-size: 14px;
  color: var(--text-secondary, #666);
  white-space: nowrap;
}

.speed-buttons {
  display: flex;
  gap: 8px;
}

.speed-button {
  padding: 6px 12px;
  border: 1px solid var(--border-color, #e0e0e0);
  background-color: var(--card-bg, #fff);
  color: var(--text-primary, #333);
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s ease;
}

.speed-button:hover {
  border-color: var(--primary-color, #1E90FF);
  color: var(--primary-color, #1E90FF);
}

.speed-button.active {
  background-color: var(--primary-color, #1E90FF);
  color: white;
  border-color: var(--primary-color, #1E90FF);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .map-trajectory-container {
    height: 100vh;
  }
  
  .map-header {
    padding: 12px 16px;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .header-right {
    width: 100%;
  }
  
  .progress-info {
    width: 100%;
    justify-content: space-between;
  }
  
  .control-panel {
    padding: 12px 16px;
    flex-direction: column;
    align-items: stretch;
  }
  
  .control-group {
    justify-content: center;
  }
  
  .speed-control {
    justify-content: center;
  }
  
  .action-button {
    min-width: 100px;
    padding: 8px 12px;
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  h2 {
    font-size: 18px;
  }
  
  .location-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
  
  .control-group {
    flex-direction: column;
    width: 100%;
  }
  
  .action-button {
    width: 100%;
  }
}
</style>