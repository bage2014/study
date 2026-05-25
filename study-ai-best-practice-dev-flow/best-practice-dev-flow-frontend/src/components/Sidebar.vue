<script setup lang="ts">
import { ref, watch, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useFamilyStore } from '@/stores/family'
import {
  HomeFilled,
  UserFilled,
  Link,
  Connection,
  Calendar,
  PictureFilled,
  LocationFilled,
  MagicStick,
  Menu,
  Close
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const familyStore = useFamilyStore()

const selectedFamilyId = ref<number | undefined>(undefined)
const showMobileMenu = ref(false)

const isMobile = computed(() => {
  if (typeof window !== 'undefined') {
    return window.innerWidth <= 768
  }
  return false
})

watch(() => familyStore.currentFamily, (family) => {
  if (family) {
    selectedFamilyId.value = family.id
  }
}, { immediate: true })

const menuItems = [
  { name: '家族管理', path: '/family', icon: HomeFilled },
  { name: '成员管理', path: '/members', icon: UserFilled },
  { name: '关系管理', path: '/relationships', icon: Link },
  { name: '家族树', path: '/family-tree', icon: Connection },
  { name: '历史事件', path: '/events', icon: Calendar },
  { name: '多媒体库', path: '/media', icon: PictureFilled },
  { name: '轨迹追踪', path: '/track', icon: LocationFilled },
  { name: 'AI功能', path: '/ai', icon: MagicStick }
]

function handleLogout() {
  userStore.logout()
  router.push('/login')
}

function handleFamilyChange(val: number) {
  const family = familyStore.families.find(f => f.id === val)
  if (family) {
    familyStore.setCurrentFamily(family)
  }
}

function closeMobileMenu() {
  showMobileMenu.value = false
}

onMounted(async () => {
  try {
    await familyStore.loadFamilies()
  } catch (error) {
    console.error('加载家族列表失败', error)
  }
})
</script>

<template>
  <aside class="sidebar" :class="{ 'sidebar-mobile': isMobile, 'sidebar-mobile-open': showMobileMenu }">
    <div class="sidebar-overlay" v-if="showMobileMenu" @click="closeMobileMenu"></div>
    
    <div class="sidebar-content">
      <div class="mobile-header">
        <h2>族谱系统</h2>
        <button class="close-btn" @click="closeMobileMenu">
          <Close />
        </button>
      </div>

      <div class="logo" v-if="!isMobile">
        <h2>族谱系统</h2>
      </div>

      <div class="user-info">
        <div class="avatar">
          <el-icon class="avatar-icon" size="40"><UserFilled /></el-icon>
        </div>
        <div class="user-detail">
          <span class="username">{{ userStore.user?.username }}</span>
          <button class="logout-btn" @click="handleLogout">退出</button>
        </div>
      </div>

      <div class="family-selector">
        <el-select 
          v-model="selectedFamilyId" 
          placeholder="选择家族"
          class="family-select"
          clearable
          @change="handleFamilyChange"
        >
          <el-option 
            v-for="family in familyStore.families" 
            :key="family.id" 
            :label="family.name" 
            :value="family.id" 
          />
        </el-select>
      </div>

      <el-menu 
        class="menu" 
        :default-active="route.path"
        :router="true"
        mode="vertical"
      >
        <el-menu-item 
          v-for="item in menuItems" 
          :key="item.path" 
          :index="item.path"
          @click="closeMobileMenu"
        >
          <el-icon :size="20">
            <component :is="item.icon" />
          </el-icon>
          <span>{{ item.name }}</span>
        </el-menu-item>
      </el-menu>
    </div>
  </aside>

  <button class="mobile-menu-toggle" @click="showMobileMenu = !showMobileMenu" v-if="isMobile">
    <Menu />
  </button>
</template>

<style scoped>
.sidebar {
  width: 220px;
  min-height: 100vh;
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  color: white;
  display: flex;
  flex-direction: column;
  padding: 20px;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 100;
  transition: transform 0.3s ease;
}

.sidebar-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.mobile-header {
  display: none;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  margin-bottom: 16px;
}

.mobile-header h2 {
  margin: 0;
  font-size: 18px;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  padding: 8px;
}

.sidebar-overlay {
  display: none;
}

.logo {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  margin-bottom: 20px;
}

.logo h2 {
  font-size: 20px;
  font-weight: bold;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  margin-bottom: 20px;
}

.avatar {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  color: white;
}

.user-detail {
  flex: 1;
}

.username {
  display: block;
  font-size: 14px;
  font-weight: bold;
}

.logout-btn {
  background: none;
  border: none;
  color: #999;
  font-size: 12px;
  cursor: pointer;
  padding: 0;
  margin-top: 4px;
}

.logout-btn:hover {
  color: #ff6b6b;
}

.family-selector {
  margin-bottom: 20px;
}

.family-select {
  width: 100%;
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
}

.menu {
  flex: 1;
  border: none;
}

.menu :deep(.el-menu-item) {
  color: #ffffff !important;
  border-radius: 8px;
  margin-bottom: 4px;
  opacity: 1 !important;
  background-color: transparent !important;
}

.menu :deep(.el-menu-item) span {
  color: #ffffff !important;
  opacity: 1 !important;
}

.menu :deep(.el-menu-item) svg {
  color: #ffffff !important;
  opacity: 1 !important;
}

.menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.15) !important;
  color: #ffffff !important;
}

.menu :deep(.el-menu-item:hover) span {
  color: #ffffff !important;
}

.menu :deep(.el-menu-item:hover) svg {
  color: #ffffff !important;
}

.menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  color: #ffffff !important;
  font-weight: 500;
}

.menu :deep(.el-menu-item.is-active) span {
  color: #ffffff !important;
}

.menu :deep(.el-menu-item.is-active) svg {
  color: #ffffff !important;
}

.mobile-menu-toggle {
  display: none;
  position: fixed;
  left: 16px;
  top: 16px;
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 50%;
  color: white;
  font-size: 20px;
  cursor: pointer;
  z-index: 101;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

@media screen and (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
    width: 260px;
  }
  
  .sidebar-mobile-open {
    transform: translateX(0);
  }
  
  .sidebar-overlay {
    display: block;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 99;
  }
  
  .sidebar-content {
    position: relative;
    z-index: 100;
  }
  
  .mobile-header {
    display: flex;
  }
  
  .logo {
    display: none;
  }
  
  .user-info {
    padding: 16px;
    margin-bottom: 16px;
  }
  
  .avatar {
    width: 56px;
    height: 56px;
  }
  
  .username {
    font-size: 16px;
  }
  
  .logout-btn {
    font-size: 14px;
  }
  
  .family-selector {
    margin-bottom: 16px;
  }
  
  .mobile-menu-toggle {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .menu :deep(.el-menu-item) {
    padding: 14px 16px !important;
    font-size: 15px;
  }
  
  .menu :deep(.el-menu-item) svg {
    font-size: 20px;
  }
}

@media screen and (min-width: 769px) and (max-width: 1024px) {
  .sidebar {
    width: 200px;
    padding: 16px;
  }
  
  .logo h2 {
    font-size: 18px;
  }
  
  .user-info {
    padding: 10px;
    gap: 10px;
  }
  
  .avatar {
    width: 44px;
    height: 44px;
  }
  
  .username {
    font-size: 13px;
  }
}
</style>