<template>
  <el-card class="filter-card">
    <div class="search-container">
      <div class="date-pickers">
        <el-date-picker
          v-model="startDate"
          type="date"
          placeholder="开始日期"
          @change="loadTrajectoryData"
          size="large"
        />
        <el-date-picker
          v-model="endDate"
          type="date"
          placeholder="结束日期"
          @change="loadTrajectoryData"
          size="large"
        />
      </div>
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalPages * pageSize"
        layout="prev, pager, next, sizes"
        :page-sizes="[10, 20, 50, 100]"
        @current-change="loadTrajectoryData"
        @size-change="loadTrajectoryData"
        class="center-pagination"
      />
    </div>
    <div id="map" style="width: 100%; height: 500px;"></div>
  </el-card>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import { ElPagination, ElDatePicker } from 'element-plus'
import API_BASE_URL from '../api/config'

const checkMapLoad = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const map = ref(null)
const today = new Date()
const sevenDaysAgo = new Date()
sevenDaysAgo.setDate(today.getDate() - 7)
const startDate = ref(sevenDaysAgo)
const endDate = ref(today)

// 移除随机生成坐标点函数

const createPoint = (item) => {
  return new window.BMap.Point(item.longitude, item.latitude)
}

const formatDate = (date) => {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const seconds = String(d.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

const loadTrajectoryData = async () => {
  if (!map.value) return
  try {
    // 清除现有标记和折线
    map.value.clearOverlays()
    // 构建查询参数
    let queryParams = `page=${currentPage.value - 1}&size=${pageSize.value}`
    if (startDate.value && endDate.value) {
      const start = formatDate(startDate.value)
      const end = formatDate(endDate.value)
      queryParams += `&startTime=${start}&endTime=${end}`
    } else if (startDate.value) {
      const start = formatDate(startDate.value)
      queryParams += `&startTime=${start}`
    } else if (endDate.value) {
      const end = formatDate(endDate.value)
      queryParams += `&endTime=${end}`
    }
    // 从后台请求数据
    const response = await fetch(`${API_BASE_URL}trajectorys/query?${queryParams}`)
    const data = await response.json()
    totalPages.value = data.totalPages
    // 倒序处理数据
    const reversedData = [...data.content].reverse()
    const trajectoryPoints = reversedData.map(createPoint)

    // 在地图上添加标记点
    reversedData.forEach((item, index) => {
      const point = createPoint(item)
      const label = new window.BMap.Label(`${index + 1}`, { offset: new window.BMap.Size(2, 2) })
      const marker = new window.BMap.Marker(point)
      marker.setLabel(label)
      const infoWindow = new window.BMap.InfoWindow(`地址： ${item.address} </br> 发生时间：${item.time}`)
      marker.addEventListener('mouseover', () => {
        map.value.openInfoWindow(infoWindow, point)
      })
      map.value.addOverlay(marker)
    })

    // 按时间倒序链接这些点
    const polyline = new window.BMap.Polyline(trajectoryPoints, {
      strokeColor: "blue",
      strokeWeight: 2,
      strokeOpacity: 1
    })
    map.value.addOverlay(polyline)

    // 调整地图视野以显示所有坐标点
    if (trajectoryPoints.length > 0) {
      map.value.setViewport(trajectoryPoints)
    }
  } catch (error) {
    console.error('请求轨迹数据失败:', error)
  }
}

onMounted(async () => {
  checkMapLoad.value = setInterval(async () => {
    const mapElement = document.getElementById('map')
    console.log('checkMapLoad', mapElement)
    if (mapElement && window.BMap) {
      clearInterval(checkMapLoad.value)
      // 初始化地图
      map.value = new window.BMap.Map(mapElement)
      // 设置地图中心为长宁区大致中心位置
      const centerPoint = new window.BMap.Point(121.40, 31.23)
      map.value.centerAndZoom(centerPoint, 13)

      // 添加缩放控件
      // map.value.addControl(new window.BMap.ZoomControl())
      
      loadTrajectoryData()
    }
  }, 100)
})

onUnmounted(() => {
  if (checkMapLoad.value) {
    clearInterval(checkMapLoad.value)
  }
})
</script>

<style scoped>
.search-container {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.date-pickers {
  display: flex;
  gap: 10px;
}

.filter-card {
  margin-bottom: 20px;
}
#map {
  margin-top: 20px;
}
.el-input.is-active.el-input__inner, .el-input__inner:focus{
  border-color: #ABB3CC;
}
.center-pagination {
  display: flex;
  align-items: center;
}
.map-card {
  margin-top: 20px;
}
</style>