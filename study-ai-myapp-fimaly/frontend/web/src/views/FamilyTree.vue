<template>
  <div class="min-h-screen bg-gray-50">
    <div class="container mx-auto px-4 py-8">
      <h1 class="text-3xl font-bold text-center text-gray-800 mb-8">家族树</h1>
      <div class="bg-white rounded-lg shadow-md p-6">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-xl font-semibold text-gray-700">{{ currentFamily.name }}</h2>
          <div class="space-x-2">
            <button class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500">
              添加成员
            </button>
            <button class="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500">
              添加关系
            </button>
          </div>
        </div>
        <div class="family-tree-container">
          <div class="family-tree">
            <!-- 家族树可视化 -->
            <div class="flex justify-center">
              <div class="tree-node bg-white border-2 border-blue-500 rounded-full w-16 h-16 flex items-center justify-center font-semibold text-blue-600">
                我
              </div>
            </div>
            <div class="flex justify-center mt-8 space-x-16">
              <div class="tree-node bg-white border-2 border-gray-400 rounded-full w-16 h-16 flex items-center justify-center font-semibold text-gray-600">
                父亲
              </div>
              <div class="tree-node bg-white border-2 border-gray-400 rounded-full w-16 h-16 flex items-center justify-center font-semibold text-gray-600">
                母亲
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useFamilyStore } from '../stores/family';

const familyStore = useFamilyStore();
const currentFamily = ref({ name: '我的家族' });

onMounted(async () => {
  await familyStore.fetchFamilies();
  if (familyStore.families.length > 0) {
    currentFamily.value = familyStore.families[0];
  }
});
</script>

<style scoped>
@import "tailwindcss";

.family-tree-container {
  overflow-x: auto;
  padding: 20px 0;
}

.family-tree {
  min-width: 800px;
}

.tree-node {
  position: relative;
}

.tree-node::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  width: 2px;
  height: 20px;
  background-color: #e5e7eb;
  transform: translateX(-50%);
}
</style>