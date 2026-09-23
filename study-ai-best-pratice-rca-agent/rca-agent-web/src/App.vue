<template>
  <el-container class="layout-container">
    <el-header class="layout-header">
      <div class="brand">
        <el-icon :size="24" color="#409EFF"><Monitor /></el-icon>
        <span>RCA Agent · 根因分析平台</span>
      </div>
      <el-menu mode="horizontal" :default-active="route.path" router class="nav-menu">
        <el-menu-item index="/analyze">RCA 分析</el-menu-item>
        <el-menu-item index="/health">服务健康</el-menu-item>
      </el-menu>
    </el-header>
    <el-main class="layout-main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { Monitor } from '@element-plus/icons-vue'

const route = useRoute()
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.layout-header {
  display: flex;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
.brand {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-right: 32px;
  white-space: nowrap;
}
.nav-menu {
  border-bottom: none;
  flex: 1;
}
.layout-main {
  background: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}
</style>
