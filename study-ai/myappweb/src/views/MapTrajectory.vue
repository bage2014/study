<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'

const mapRef = ref<HTMLDivElement>()
let map: any = null
let polyline: any = null
let marker: any = null
let animationInterval: any = null

// 模拟轨迹数据
const trajectoryData = [
  { lng: 116.397428, lat: 39.90923 }, // 北京
  { lng: 116.407428, lat: 39.91923 },
  { lng: 116.417428, lat: 39.92923 },
  { lng: 116.427428, lat: 39.93923 },
  { lng: 116.437428, lat: 39.94923 },
  { lng: 116.447428, lat: 39.93923 },
  { lng: 116.457428, lat: 39.92923 }
]

// 当前动画位置索引
const currentIndex = ref(0)

// 计算当前点
const currentPoint = computed(() => {
  return trajectoryData[currentIndex.value]
})

onMounted(() => {
  // 加载百度地图 API
  const script = document.createElement('script')
  script.src = 'https://api.map.baidu.com/api?v=3.0&ak=E0NV1lriEm1YlMtfmWr6ZkGKVNrOlRzA&callback=initMap'
  script.type = 'text/javascript'
  document.head.appendChild(script)

  // 定义初始化地图的全局函数
  // Replace window.initMap with:
  (window as any).initMap = () => {
    if (mapRef.value) {
      // 创建地图实例
      map = new BMap.Map(mapRef.value)

      // 设置中心点和缩放级别
      const point = new BMap.Point(trajectoryData[0].lng, trajectoryData[0].lat)
      map.centerAndZoom(point, 15)

      // 添加地图控件
      map.addControl(new BMap.NavigationControl({ type: BMAP_NAVIGATION_CONTROL_LARGE }))
      map.addControl(new BMap.ScaleControl())
      map.addControl(new BMap.OverviewMapControl())
      map.addControl(new BMap.MapTypeControl())

      // 绘制轨迹
      drawTrajectory()

      // 启动动画
      startAnimation()
    }
  }
  // And for the cleanup:
  if ((window as any).initMap) {
    delete (window as any).initMap
  }
})

onUnmounted(() => {
  // 清理地图实例
  if (map) {
    map.clearOverlays()
    map = null
  }
  // 移除全局函数
  if (window.initMap) {
    delete window.initMap
  }
  // 清除动画
  if (animationInterval) {
    clearInterval(animationInterval)
    animationInterval = null
  }
})

// 绘制轨迹函数
const drawTrajectory = () => {
  if (!map) return

  // 清除已有轨迹
  if (polyline) {
    map.removeOverlay(polyline)
  }

  // 创建坐标点数组
  const points = trajectoryData.map(item => new BMap.Point(item.lng, item.lat))

  // 创建折线
  polyline = new BMap.Polyline(points, {
    strokeColor: '#1E90FF',
    strokeWeight: 3,
    strokeOpacity: 0.8,
    strokeStyle: 'solid'
  })

  // 添加折线到地图
  map.addOverlay(polyline)

  // 添加起点和终点标记
  const startMarker = new BMap.Marker(points[0])
  const endMarker = new BMap.Marker(points[points.length - 1])
  map.addOverlay(startMarker)
  map.addOverlay(endMarker)

  // 设置信息窗口
  const startInfo = new BMap.InfoWindow('起点')
  const endInfo = new BMap.InfoWindow('终点')
  startMarker.addEventListener('click', () => startMarker.openInfoWindow(startInfo))
  endMarker.addEventListener('click', () => endMarker.openInfoWindow(endInfo))

  // 添加移动标记
  if (marker) {
    map.removeOverlay(marker)
  }
  marker = new BMap.Marker(points[0], {
    icon: new BMap.Icon('https://api.map.baidu.com/img/markers.png', new BMap.Size(23, 25), {
      offset: new BMap.Size(10, 25),
      imageOffset: new BMap.Size(0, 0 - 10 * 25)
    })
  })
  map.addOverlay(marker)

  // 调整地图视野以显示全部轨迹
  map.setViewport(points, { padding: [50, 50, 50, 50] })
}

// 开始动画
const startAnimation = () => {
  if (animationInterval) {
    clearInterval(animationInterval)
  }

  animationInterval = setInterval(() => {
    if (currentIndex.value >= trajectoryData.length - 1) {
      currentIndex.value = 0
    } else {
      currentIndex.value++
    }

    // 更新标记位置
    if (marker && map) {
      const point = new BMap.Point(trajectoryData[currentIndex.value].lng, trajectoryData[currentIndex.value].lat)
      marker.setPosition(point)

      // 显示当前位置信息
      const info = new BMap.InfoWindow(`位置: ${currentIndex.value + 1}/${trajectoryData.length}`)
      marker.openInfoWindow(info)
    }
  }, 1000)
}

// 重置动画
const resetAnimation = () => {
  currentIndex.value = 0
  if (marker && map) {
    const point = new BMap.Point(trajectoryData[0].lng, trajectoryData[0].lat)
    marker.setPosition(point)
  }
}
</script>

<template>
  <div class="map-container">
    <div class="map-header">
      <h2>地图轨迹展示</h2>
      <div class="location-info">
        <p>当前位置: {{ currentPoint.lng.toFixed(6) }}, {{ currentPoint.lat.toFixed(6) }}</p>
      </div>
    </div>
    <div ref="mapRef" class="map"></div>
    <div class="control-panel">
      <button @click="drawTrajectory">重新绘制轨迹</button>
      <button @click="resetAnimation">重置动画</button>
    </div>
  </div>
</template>

<style scoped>
.map-container {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.map-header {
  background-color: #fff;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h2 {
  font-size: 20px;
  color: #333;
  margin: 0;
}

.location-info {
  font-size: 14px;
  color: #666;
}

.map {
  flex: 1;
  width: 100%;
  min-height: 600px;
  border: none;
}

.control-panel {
  background-color: #fff;
  padding: 15px 20px;
  border-top: 1px solid #eee;
  display: flex;
  gap: 10px;
}

button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

button:hover {
  background-color: #3aa876;
  box-shadow: 0 2px 8px rgba(66, 185, 131, 0.3);
}

button:active {
  transform: translateY(1px);
}
</style>