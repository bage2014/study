<template>
  <div>
    <div id="map" style="width: 100%; height: 500px;"></div>
    <div v-if="clickedPoint">
      当前点击坐标：经度 {{ clickedPoint.lng }}，纬度 {{ clickedPoint.lat }}
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import API_BASE_URL from '../api/config'
import { ElMessage } from 'element-plus'

const clickedPoint = ref(null)
const checkMapLoad = ref(null)

const savePointToBackend = async (point, time, address) => {
  try {
    const response = await fetch(`${API_BASE_URL}trajectorys/save`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        latitude: point.lat,
        longitude: point.lng,
        time: time,
        address: address
      }),
    });

    if (response.ok) {
      ElMessage.success('保存成功');
    }
  } catch (error) {
    console.error('保存坐标失败:', error);
  }
};

onMounted(() => {
  checkMapLoad.value = setInterval(() => {
    const mapElement = document.getElementById('map')
    if (mapElement && window.BMap) {
      clearInterval(checkMapLoad.value)
      const map = new window.BMap.Map(mapElement)
      const centerPoint = new window.BMap.Point(121.40, 31.23)
      map.centerAndZoom(centerPoint, 13)

      map.addEventListener('click', (e) => {
        // 阻止默认的信息窗口弹出
        // e.preventDefault();
        // e.stopPropagation();

        clickedPoint.value = {
          lng: e.point.lng,
          lat: e.point.lat
        }
        const date = new Date().toISOString();
        const geoc = new window.BMap.Geocoder();
        geoc.getLocation(e.point, (rs) => {
          const address = rs.address;
          savePointToBackend(e.point, date, address);
        });
      })
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
#map {
  margin-top: 20px;
}
</style>