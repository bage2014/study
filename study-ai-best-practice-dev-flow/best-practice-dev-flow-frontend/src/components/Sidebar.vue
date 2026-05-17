<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
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
  MagicStick
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const familyStore = useFamilyStore()

const selectedFamilyId = ref<number | undefined>(undefined)

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

onMounted(async () => {
  try {
    await familyStore.loadFamilies()
  } catch (error) {
    console.error('加载家族列表失败', error)
  }
})
</script>

<template>
  <aside class="sidebar">
    <div class="logo">
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
      >
        <el-icon :size="20">
          <component :is="item.icon" />
        </el-icon>
        <span>{{ item.name }}</span>
      </el-menu-item>
    </el-menu>
  </aside>
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
  color: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  margin-bottom: 4px;
}

.menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}
</style>