<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="家族地理位置" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">

    <!-- 家族选择器 -->
    <div class="bg-white p-6 rounded-xl shadow-lg mb-6 animate-slide-up">
      <div class="flex items-center mb-4">
        <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-orange-100 to-orange-200 flex items-center justify-center mr-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-orange-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
          </svg>
        </div>
        <h2 class="text-lg font-semibold text-gray-900">选择家族</h2>
      </div>
      <select v-model="selectedFamilyId" @change="fetchFamilyMembers" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200 hover:border-green-300">
        <option value="">请选择家族</option>
        <option v-for="family in families" :key="family.id" :value="family.id">
          {{ family.name }}
        </option>
      </select>
    </div>

    <!-- 成员列表 -->
    <div v-if="familyMembers.length > 0" class="bg-white p-6 rounded-xl shadow-lg mb-6 animate-slide-in-left">
      <div class="flex items-center mb-4">
        <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-100 to-blue-200 flex items-center justify-center mr-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
        </div>
        <h2 class="text-lg font-semibold text-gray-900">家族成员</h2>
      </div>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
        <div 
          v-for="(member, index) in familyMembers" 
          :key="member.id"
          @click="selectMember(member)"
          :class="['border rounded-xl p-4 cursor-pointer transition-all duration-300 transform', selectedMemberId === member.id ? 'border-green-500 bg-green-50 shadow-md scale-105' : 'border-gray-200 hover:shadow-md hover:-translate-y-1 hover:border-green-300']"
          :style="{ animationDelay: `${index * 30}ms` }"
          class="animate-fade-in"
        >
          <div class="font-semibold text-gray-900">{{ member.name }}</div>
          <div class="text-sm text-gray-600 mt-1">{{ getMemberLocation(member.id) || '无位置信息' }}</div>
        </div>
      </div>
    </div>

    <!-- 地图容器 -->
    <div v-if="selectedFamilyId" class="bg-white p-6 rounded-xl shadow-lg animate-slide-in-right">
      <div class="flex items-center mb-4">
        <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-cyan-100 to-cyan-200 flex items-center justify-center mr-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-cyan-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
          </svg>
        </div>
        <h2 class="text-lg font-semibold text-gray-900">位置地图</h2>
      </div>
      <div id="map-container" class="w-full h-[500px] border border-gray-200 rounded-xl"></div>
    </div>

    <!-- 添加/编辑位置模态框 -->
    <div v-if="showLocationModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 animate-fade-in">
      <div class="bg-white rounded-2xl p-6 w-full max-w-md animate-scale-in shadow-2xl">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
          </div>
          <h2 class="text-xl font-semibold text-gray-900">{{ editingLocation ? '编辑位置' : '添加位置' }}</h2>
        </div>
        
        <form @submit.prevent="saveLocation" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">位置名称</label>
            <input v-model="formData.name" type="text" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">位置类型</label>
            <select v-model="formData.locationType" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
              <option value="home">家</option>
              <option value="work">工作</option>
              <option value="school">学校</option>
              <option value="other">其他</option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">地址</label>
            <input v-model="formData.address" type="text" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
          </div>
          
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">纬度</label>
              <input v-model="formData.latitude" type="number" step="0.000001" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">经度</label>
              <input v-model="formData.longitude" type="number" step="0.000001" class="w-full border border-gray-200 rounded-xl px-4 py-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
          </div>
          
          <div class="flex space-x-4">
            <label class="flex items-center cursor-pointer">
              <input v-model="formData.isShared" type="checkbox" class="w-4 h-4 text-green-500 border-gray-300 rounded focus:ring-green-500">
              <span class="ml-2 text-sm text-gray-700">共享位置</span>
            </label>
            <label class="flex items-center cursor-pointer">
              <input v-model="formData.isPrimary" type="checkbox" class="w-4 h-4 text-green-500 border-gray-300 rounded focus:ring-green-500">
              <span class="ml-2 text-sm text-gray-700">设为常用地</span>
            </label>
          </div>
          
          <div class="flex justify-end space-x-3 pt-4">
            <button type="button" @click="showLocationModal = false; resetForm()" class="px-5 py-2.5 border border-gray-200 rounded-xl text-gray-700 hover:bg-gray-50 transition-all duration-200 hover:-translate-y-0.5">
              取消
            </button>
            <button type="submit" class="px-5 py-2.5 bg-green-500 text-white rounded-xl hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200 hover:-translate-y-0.5">
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
      <button @click="getCurrentLocation" class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md">
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
