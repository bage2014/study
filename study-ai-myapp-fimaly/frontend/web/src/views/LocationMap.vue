<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="家族地理位置" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

    <!-- 家族选择器 -->
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1">选择家族</label>
      <select v-model="selectedFamilyId" @change="fetchFamilyMembers" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
        <option value="">请选择家族</option>
        <option v-for="family in families" :key="family.id" :value="family.id">
          {{ family.name }}
        </option>
      </select>
    </div>

    <!-- 成员列表 -->
    <div v-if="familyMembers.length > 0" class="mb-4">
      <h2 class="text-lg font-semibold mb-2">家族成员</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-2">
        <div 
          v-for="member in familyMembers" 
          :key="member.id"
          @click="selectMember(member)"
          class="border border-gray-200 rounded-md p-2 cursor-pointer hover:bg-gray-50"
          :class="selectedMemberId === member.id ? 'bg-green-50 border-green-500' : ''"
        >
          <div class="font-medium">{{ member.name }}</div>
          <div class="text-sm text-gray-600">{{ getMemberLocation(member.id) || '无位置信息' }}</div>
        </div>
      </div>
    </div>

    <!-- 地图容器 -->
    <div v-if="selectedFamilyId" class="mt-4">
      <div id="map-container" class="w-full h-[500px] border border-gray-200 rounded-lg"></div>
    </div>

    <!-- 添加/编辑位置模态框 -->
    <div v-if="showLocationModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h2 class="text-xl font-bold mb-4">{{ editingLocation ? '编辑位置' : '添加位置' }}</h2>
        
        <form @submit.prevent="saveLocation">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">位置名称</label>
            <input v-model="formData.name" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
          </div>
          
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">位置类型</label>
            <select v-model="formData.locationType" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
              <option value="home">家</option>
              <option value="work">工作</option>
              <option value="school">学校</option>
              <option value="other">其他</option>
            </select>
          </div>
          
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">地址</label>
            <input v-model="formData.address" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
          </div>
          
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">纬度</label>
            <input v-model="formData.latitude" type="number" step="0.000001" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
          </div>
          
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">经度</label>
            <input v-model="formData.longitude" type="number" step="0.000001" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500">
          </div>
          
          <div class="mb-4">
            <label class="flex items-center">
              <input v-model="formData.isShared" type="checkbox" class="mr-2">
              <span class="text-sm text-gray-700">共享位置</span>
            </label>
          </div>
          
          <div class="mb-4">
            <label class="flex items-center">
              <input v-model="formData.isPrimary" type="checkbox" class="mr-2">
              <span class="text-sm text-gray-700">设为常用地</span>
            </label>
          </div>
          
          <div class="flex justify-end space-x-2">
            <button type="button" @click="showLocationModal = false; resetForm()" class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
              取消
            </button>
            <button type="submit" class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md">
              保存
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div v-if="selectedMemberId" class="mt-4 flex space-x-2">
      <button @click="showAddLocationModal" class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md">
        添加位置
      </button>
      <button @click="getCurrentLocation" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md">
        获取当前位置
      </button>
    </div>
  </main>
</div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useLocationStore } from '../stores/location'
import { useMemberStore } from '../stores/member'
import { useFamilyStore } from '../stores/family'
import { useUserStore } from '../stores/user'
import Header from '../components/Header.vue'

const locationStore = useLocationStore()
const memberStore = useMemberStore()
const familyStore = useFamilyStore()
const userStore = useUserStore()

const selectedFamilyId = ref('')
const selectedMemberId = ref('')
const showLocationModal = ref(false)
const editingLocation = ref(false)
const currentLocationId = ref(null)
let map = null
let markers = []

const formData = ref({
  name: '',
  locationType: 'home',
  address: '',
  latitude: 0,
  longitude: 0,
  isShared: true,
  isPrimary: false
})

const families = computed(() => familyStore.families)
const familyMembers = computed(() => {
  if (!selectedFamilyId.value) return []
  return memberStore.members.filter(member => member.family.id === selectedFamilyId.value)
})

onMounted(async () => {
  if (!userStore.user) {
    await userStore.fetchCurrentUser()
  }
  await familyStore.fetchFamilies()
  // 初始化地图
  initMap()
})

watch(selectedFamilyId, async (newFamilyId) => {
  if (newFamilyId) {
    await memberStore.fetchMembers()
    selectedMemberId.value = ''
    updateMap()
  }
})

watch(selectedMemberId, async (newMemberId) => {
  if (newMemberId) {
    await locationStore.fetchLocationsByMemberId(newMemberId)
    updateMap()
  }
})

const fetchFamilyMembers = async () => {
  if (selectedFamilyId.value) {
    await memberStore.fetchMembers()
    selectedMemberId.value = ''
    updateMap()
  }
}

const selectMember = (member) => {
  selectedMemberId.value = member.id
}

const initMap = () => {
  // 这里使用高德地图API初始化地图
  // 实际项目中需要替换为真实的API密钥
  if (window.AMap) {
    map = new window.AMap.Map('map-container', {
      zoom: 11,
      center: [116.397428, 39.90923]
    })
  }
}

const updateMap = () => {
  if (!map) return

  // 清除现有标记
  markers.forEach(marker => marker.remove())
  markers = []

  if (selectedFamilyId.value) {
    // 显示家族所有成员的共享位置
    familyMembers.value.forEach(member => {
      const sharedLocations = locationStore.getSharedLocationsByMemberId(member.id)
      sharedLocations.forEach(location => {
        const marker = new window.AMap.Marker({
          position: [location.longitude, location.latitude],
          title: `${member.name} - ${location.name}`,
          map: map
        })
        markers.push(marker)
      })
    })
  } else if (selectedMemberId.value) {
    // 显示选中成员的所有位置
    const locations = locationStore.getLocationsByMemberId(selectedMemberId.value)
    locations.forEach(location => {
      const marker = new window.AMap.Marker({
        position: [location.longitude, location.latitude],
        title: location.name,
        map: map
      })
      markers.push(marker)
    })
  }
}

const getMemberLocation = (memberId) => {
  const primaryLocation = locationStore.getPrimaryLocationByMemberId(memberId)
  return primaryLocation ? primaryLocation.name : null
}

const showAddLocationModal = () => {
  resetForm()
  editingLocation.value = false
  showLocationModal.value = true
}

const resetForm = () => {
  formData.value = {
    name: '',
    locationType: 'home',
    address: '',
    latitude: 0,
    longitude: 0,
    isShared: true,
    isPrimary: false
  }
  currentLocationId.value = null
  editingLocation.value = false
}

const saveLocation = async () => {
  const locationData = {
    ...formData.value,
    member: {
      id: selectedMemberId.value
    }
  }

  if (editingLocation.value) {
    await locationStore.updateLocation(currentLocationId.value, locationData)
  } else {
    await locationStore.createLocation(locationData)
  }

  showLocationModal.value = false
  resetForm()
  updateMap()
}

const getCurrentLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        formData.value.latitude = position.coords.latitude
        formData.value.longitude = position.coords.longitude
        showLocationModal.value = true
        editingLocation.value = false
      },
      (error) => {
        console.error('Error getting location:', error)
        alert('无法获取当前位置，请手动输入')
      }
    )
  } else {
    alert('您的浏览器不支持地理定位')
  }
}
</script>

<style scoped>
#map-container {
  width: 100%;
  height: 500px;
}
</style>
