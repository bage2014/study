<template>
  <div>
    <div class="map-container" ref="mapContainer"></div>
    <div class="map-actions">
      <a-button type="primary" @click="clearAllPoints">
        <template #icon>
          <DeleteOutlined />
        </template>
        清空轨迹
      </a-button>
    </div>
    
    <a-modal
      v-model:open="isModalVisible"
      title="添加轨迹点"
      :footer="null"
      width="400px"
    >
      <a-form :model="newPoint" layout="vertical">
        <a-form-item label="位置名称" :required="true">
          <a-input
            v-model:value="newPoint.name"
            placeholder="请输入位置名称"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-input
            v-model:value="newPoint.description"
            placeholder="请输入描述"
          />
        </a-form-item>
        <a-form-item label="纬度">
          <a-input
            v-model:value="newPoint.latitude"
            disabled
          />
        </a-form-item>
        <a-form-item label="经度">
          <a-input
            v-model:value="newPoint.longitude"
            disabled
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button @click="isModalVisible = false">取消</a-button>
            <a-button type="primary" @click="savePoint">保存</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DeleteOutlined } from '@ant-design/icons-vue'

const mapContainer = ref(null)
const isModalVisible = ref(false)
const trackPoints = ref([])
const newPoint = ref({
  name: '',
  description: '',
  latitude: 0,
  longitude: 0
})
const baiduMap = ref(null)
const markers = ref([])
const polyline = ref(null)

const loadMap = async () => {
  if (window.BMapGL) {
    initMap()
  } else {
    const script = document.createElement('script')
    script.src = 'https://api.map.baidu.com/api?v=3.0&ak=你的百度地图API密钥'
    script.onload = initMap
    document.head.appendChild(script)
  }
}

const initMap = () => {
  baiduMap.value = new window.BMapGL.Map(mapContainer.value)
  baiduMap.value.centerAndZoom(new window.BMapGL.Point(116.4074, 39.9042), 12)
  baiduMap.value.enableScrollWheelZoom(true)
  baiduMap.value.addEventListener('click', handleMapClick)
  loadTrackPoints()
}

const handleMapClick = (e) => {
  newPoint.value = {
    name: '',
    description: '',
    latitude: e.point.lat,
    longitude: e.point.lng
  }
  isModalVisible.value = true
}

const loadTrackPoints = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/trackpoints?userId=1')
    const data = await response.json()
    trackPoints.value = data
    renderTrackPoints()
  } catch (error) {
    console.error('加载轨迹点失败:', error)
  }
}

const renderTrackPoints = () => {
  markers.value.forEach(marker => {
    baiduMap.value.removeOverlay(marker)
  })
  markers.value = []
  
  if (polyline.value) {
    baiduMap.value.removeOverlay(polyline.value)
    polyline.value = null
  }
  
  if (trackPoints.value.length === 0) return
  
  trackPoints.value.forEach((point, index) => {
    const marker = new window.BMapGL.Marker(new window.BMapGL.Point(point.longitude, point.latitude))
    const label = new window.BMapGL.Label(`${index + 1}`, { offset: new window.BMapGL.Size(10, -10) })
    marker.setLabel(label)
    marker.addEventListener('click', () => {
      handleMarkerClick(point)
    })
    baiduMap.value.addOverlay(marker)
    markers.value.push(marker)
  })
  
  const points = trackPoints.value.map(p => new window.BMapGL.Point(p.longitude, p.latitude))
  polyline.value = new window.BMapGL.Polyline(points, {
    strokeColor: '#1890ff',
    strokeWeight: 3,
    strokeOpacity: 0.8
  })
  baiduMap.value.addOverlay(polyline.value)
}

const handleMarkerClick = (point) => {
  Modal.confirm({
    title: '删除轨迹点',
    content: `确定要删除 "${point.name || '未命名点'}" 吗？`,
    okText: '删除',
    cancelText: '取消',
    onOk: async () => {
      await deleteTrackPoint(point.id)
    }
  })
}

const deleteTrackPoint = async (id) => {
  try {
    await fetch(`http://localhost:8080/api/trackpoints/${id}`, {
      method: 'DELETE'
    })
    message.success('删除成功')
    loadTrackPoints()
  } catch (error) {
    message.error('删除失败')
  }
}

const savePoint = async () => {
  if (!newPoint.value.name.trim()) {
    message.warning('请输入位置名称')
    return
  }
  
  try {
    await fetch('http://localhost:8080/api/trackpoints', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newPoint.value)
    })
    message.success('保存成功')
    isModalVisible.value = false
    loadTrackPoints()
  } catch (error) {
    message.error('保存失败')
  }
}

const clearAllPoints = () => {
  Modal.confirm({
    title: '清空轨迹',
    content: '确定要清空所有轨迹点吗？此操作不可恢复。',
    okText: '清空',
    cancelText: '取消',
    onOk: async () => {
      try {
        for (const point of trackPoints.value) {
          await fetch(`http://localhost:8080/api/trackpoints/${point.id}`, {
            method: 'DELETE'
          })
        }
        message.success('清空成功')
        trackPoints.value = []
        renderTrackPoints()
      } catch (error) {
        message.error('清空失败')
      }
    }
  })
}

onMounted(() => {
  loadMap()
})
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 500px;
  border-radius: 8px;
}

.map-actions {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>