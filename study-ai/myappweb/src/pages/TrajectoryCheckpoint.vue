<template>
  <div>
    <div id="map" style="width: 100%; height: 500px;"></div>
    <div v-if="clickedPoint">
      当前点击坐标：经度 {{ clickedPoint.lng }}，纬度 {{ clickedPoint.lat }}
    </div>
  </div>
</template>

<script setup lang="ts">
// 修复：添加缺失的导入
import { ref, onMounted, onUnmounted, defineEmits } from 'vue';
import { ElMessage } from 'element-plus';
import { request } from '@/api/request';

// 修复：定义缺失的变量
const checkpointName = ref('');
const currentPoint = ref(null);
const dialogVisible = ref(false);
const emit = defineEmits(['save-success']);

const clickedPoint = ref(null)
const checkMapLoad = ref(null)

const saveCheckpoint = async () => {
  if (!checkpointName.value) {
    ElMessage.warning('请输入 checkpoint 名称');
    return;
  }

  try {
    // 替换fetch为request<T>
    await request<{ message: string }>('/trajectorys/save', {
      method: 'POST',
      body: JSON.stringify({
        name: checkpointName.value,
        longitude: currentPoint.value.lng,
        latitude: currentPoint.value.lat
      }),
    });
    ElMessage.success('Checkpoint saved successfully');
    emit('save-success');
    dialogVisible.value = false;
  } catch (error) {
    console.error('保存失败:', error);
    ElMessage.error('保存失败，请重试');
  }
};

// 修复：添加缺失的函数定义
const savePointToBackend = async (point, date, address) => {
  try {
    await request<{ message: string }>('/trajectorys/save', {
      method: 'POST',
      body: JSON.stringify({
        longitude: point.lng,
        latitude: point.lat,
        address: address,
        timestamp: date
      }),
    });
    ElMessage.success('坐标已保存');
  } catch (error) {
    console.error('保存坐标失败:', error);
    ElMessage.error('保存坐标失败');
  }
};

onMounted(() => {
  checkMapLoad.value = setInterval(() => {
    const mapElement = document.getElementById('map')
    if (mapElement && window.BMap) {
      clearInterval(checkMapLoad.value)
      const map = new window.BMap.Map(mapElement,{ enableMapClick:false})
      const centerPoint = new window.BMap.Point(121.40, 31.23)
      map.centerAndZoom(centerPoint, 13)
      // 添加点击事件监听并阻止默认弹窗

      map.addEventListener('click', (e) => {
        // 阻止默认的信息窗口弹出
        const point = e.point;
        console.log('点击经纬度:', `经度: ${point.lng}, 纬度: ${point.lat}`);
        
        clickedPoint.value = {
          lng: e.point.lng,
          lat: e.point.lat,
          address: ''
        }

        const date = new Date().toISOString();
        const geocoder = new window.BMap.Geocoder();
        geocoder.getLocation(point, function(rs) {
          const address = rs.address;
          console.log('地点信息:', address);
          clickedPoint.value.address = address;
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