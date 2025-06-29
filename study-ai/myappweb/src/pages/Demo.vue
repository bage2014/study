<script setup lang="ts">
import { ref } from "vue";
import { ElMessage } from 'element-plus'

const count = ref(0);
const input = ref("element-plus");

const curDate = ref('')

const toast = () => {
  ElMessage.success('Hello')
}
</script>

<template>
  <h1>hello -vue </h1>
</template>

<style>
.ep-button {
  margin: 4px;
}
</style>
