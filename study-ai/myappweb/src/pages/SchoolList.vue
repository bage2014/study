<template>
  <div>
    <el-card>
      <h2>个人学校信息维护</h2>
      <!-- 这里可以添加表单来维护学校信息 -->
      <el-form>
        <el-form-item label="学校名称">
          <el-input v-model="schoolName"></el-input>
        </el-form-item>
        <el-form-item label="入学时间">
          <el-date-picker v-model="enrollmentDate" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="毕业时间">
          <el-date-picker v-model="graduationDate" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveInfo">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const schoolName = ref('');
const enrollmentDate = ref(null);
const graduationDate = ref(null);

const saveInfo = () => {
  // 这里可以添加保存信息到后端的逻辑
  console.log('保存的学校信息:', {
    schoolName: schoolName.value,
    enrollmentDate: enrollmentDate.value,
    graduationDate: graduationDate.value
  });
};
</script>

<style scoped>
/* 可以添加样式 */
</style>