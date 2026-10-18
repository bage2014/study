<template>
  <el-container style="height: 100vh">
    <el-header style="background:#409eff; color:#fff; display:flex; align-items:center; gap:12px">
      <el-icon size="22"><Reading /></el-icon>
      <span style="font-size:18px; font-weight:600">Demo Library</span>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>
