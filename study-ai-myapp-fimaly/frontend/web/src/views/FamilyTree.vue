<template>
  <div class="min-h-screen bg-gray-50">
    <div class="container mx-auto px-4 py-8">
      <h1 class="text-3xl font-bold text-center text-gray-800 mb-8">家族树</h1>
      <div class="bg-white rounded-lg shadow-md p-6">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-xl font-semibold text-gray-700">{{ currentFamily.name }}</h2>
          <div class="space-x-2">
            <button @click="openAddMemberModal" class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500">
              添加成员
            </button>
            <button @click="openAddRelationshipModal" class="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500">
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

    <!-- Add Member Modal -->
    <div v-if="showAddMemberModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">添加成员</h3>
        <form @submit.prevent="handleAddMember" class="space-y-4">
          <div>
            <label for="name" class="block text-sm font-medium text-gray-700">姓名</label>
            <input type="text" id="name" v-model="memberForm.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
          </div>
          <div>
            <label for="gender" class="block text-sm font-medium text-gray-700">性别</label>
            <select id="gender" v-model="memberForm.gender" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
              <option value="">请选择</option>
              <option value="male">男</option>
              <option value="female">女</option>
            </select>
          </div>
          <div>
            <label for="birthDate" class="block text-sm font-medium text-gray-700">出生日期</label>
            <input type="date" id="birthDate" v-model="memberForm.birthDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
          </div>
          <div>
            <label for="deathDate" class="block text-sm font-medium text-gray-700">去世日期</label>
            <input type="date" id="deathDate" v-model="memberForm.deathDate" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
          </div>
          <div>
            <label for="details" class="block text-sm font-medium text-gray-700">详细信息</label>
            <textarea id="details" v-model="memberForm.details" rows="3" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"></textarea>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showAddMemberModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
              取消
            </button>
            <button type="submit" :disabled="loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50">
              {{ loading ? '添加中...' : '添加成员' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Add Relationship Modal -->
    <div v-if="showAddRelationshipModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">添加关系</h3>
        <form @submit.prevent="handleAddRelationship" class="space-y-4">
          <div>
            <label for="member1Id" class="block text-sm font-medium text-gray-700">成员1 ID</label>
            <input type="number" id="member1Id" v-model="relationshipForm.member1Id" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
          </div>
          <div>
            <label for="member2Id" class="block text-sm font-medium text-gray-700">成员2 ID</label>
            <input type="number" id="member2Id" v-model="relationshipForm.member2Id" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
          </div>
          <div>
            <label for="relationshipType" class="block text-sm font-medium text-gray-700">关系类型</label>
            <select id="relationshipType" v-model="relationshipForm.relationshipType" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
              <option value="">请选择关系类型</option>
              <option value="FATHER">父亲</option>
              <option value="MOTHER">母亲</option>
              <option value="SON">儿子</option>
              <option value="DAUGHTER">女儿</option>
              <option value="HUSBAND">丈夫</option>
              <option value="WIFE">妻子</option>
              <option value="BROTHER">兄弟</option>
              <option value="SISTER">姐妹</option>
              <option value="GRANDFATHER">祖父</option>
              <option value="GRANDMOTHER">祖母</option>
              <option value="GRANDSON">孙子</option>
              <option value="GRANDDAUGHTER">孙女</option>
              <option value="UNCLE">叔叔</option>
              <option value="AUNT">阿姨</option>
              <option value="COUSIN">堂表亲</option>
            </select>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showAddRelationshipModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
              取消
            </button>
            <button type="submit" :disabled="loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50">
              {{ loading ? '添加中...' : '添加关系' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useFamilyStore } from '../stores/family';
import { useRelationshipStore } from '../stores/relationship';

const familyStore = useFamilyStore();
const relationshipStore = useRelationshipStore();
const currentFamily = ref({ name: '我的家族' });
const showAddMemberModal = ref(false);
const showAddRelationshipModal = ref(false);
const loading = ref(false);

const memberForm = ref({
  name: '',
  gender: '',
  birthDate: '',
  deathDate: '',
  details: ''
});

const relationshipForm = ref({
  member1Id: '',
  member2Id: '',
  relationshipType: ''
});

onMounted(async () => {
  await familyStore.fetchFamilies();
  if (familyStore.families.length > 0) {
    currentFamily.value = familyStore.families[0];
  }
});

const openAddMemberModal = () => {
  memberForm.value = {
    name: '',
    gender: '',
    birthDate: '',
    deathDate: '',
    details: ''
  };
  showAddMemberModal.value = true;
};

const openAddRelationshipModal = () => {
  relationshipForm.value = {
    member1Id: '',
    member2Id: '',
    relationshipType: ''
  };
  showAddRelationshipModal.value = true;
};

const handleAddMember = async () => {
  loading.value = true;
  try {
    if (familyStore.families.length > 0) {
      const familyId = familyStore.families[0].id;
      await familyStore.addFamilyMember(familyId, memberForm.value);
      alert('成员添加成功');
      showAddMemberModal.value = false;
    } else {
      alert('请先创建家族');
    }
  } catch (error) {
    alert('成员添加失败: ' + (error.response?.data?.message || error.message));
  } finally {
    loading.value = false;
  }
};

const handleAddRelationship = async () => {
  loading.value = true;
  try {
    await relationshipStore.createRelationship(relationshipForm.value);
    alert('关系添加成功');
    showAddRelationshipModal.value = false;
  } catch (error) {
    alert('关系添加失败: ' + (error.response?.data?.message || error.message));
  } finally {
    loading.value = false;
  }
};
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