<script lang="ts" setup>

</script>

<template>
  <el-menu class="el-menu-demo" mode="horizontal">
    <el-menu-item index="1"><router-link to="/"><el-avatar :size="50" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
 /></router-link></el-menu-item>
    <el-menu-item index="1"><router-link to="/">Tutorials</router-link></el-menu-item>
    <el-menu-item index="2"><router-link to="/activities">活动列表</router-link></el-menu-item>
    <el-menu-item index="3"><router-link to="/video-list">视频列表</router-link></el-menu-item>
    <el-menu-item index="4"><router-link to="/school-list">学校列表</router-link></el-menu-item>
    <el-menu-item index="5"><router-link to="/map-trajectory">地图轨迹</router-link></el-menu-item>
    <el-menu-item index="6"><router-link to="/trajectory-checkpoint">轨迹检查点</router-link></el-menu-item>
  </el-menu>
</template>
