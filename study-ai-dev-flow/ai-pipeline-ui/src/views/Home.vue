<template>
  <div class="max-w-2xl mx-auto">
    <div class="bg-white rounded-xl shadow-lg overflow-hidden">
      <div class="bg-gradient-to-r from-indigo-600 to-purple-600 px-6 py-8 text-center">
        <h1 class="text-3xl font-bold text-white mb-2">AI Pipeline Platform</h1>
        <p class="text-indigo-100">智能软件开发流水线</p>
      </div>
      <div class="p-6">
        <form @submit.prevent="handleSubmit">
          <div class="mb-4">
            <label class="block text-gray-700 font-medium mb-2">需求描述</label>
            <textarea
              v-model="requirementMd"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none resize-none"
              rows="4"
              placeholder="请输入您的需求，例如：添加健康检查端点，返回服务状态信息"
              required
            ></textarea>
          </div>
          <div class="mb-6">
            <label class="block text-gray-700 font-medium mb-2">项目本地路径</label>
            <input
              v-model="projectLocalPath"
              type="text"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
              required
            />
          </div>
          <button
            type="submit"
            :disabled="loading"
            class="w-full bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-medium py-3 rounded-lg hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            {{ loading ? '启动中...' : '启动流水线' }}
          </button>
        </form>
        <div v-if="error" class="mt-4 alert alert-danger">
          <strong>错误：</strong>{{ error }}
        </div>
        <div class="mt-8 pt-6 border-t">
          <h4 class="font-semibold text-gray-700 mb-3">功能特性</h4>
          <div class="grid grid-cols-2 gap-3">
            <div class="flex items-center">
              <span class="bg-green-100 text-green-600 rounded-full w-6 h-6 flex items-center justify-center text-sm mr-2">✓</span>
              <span class="text-gray-600">需求分析</span>
            </div>
            <div class="flex items-center">
              <span class="bg-green-100 text-green-600 rounded-full w-6 h-6 flex items-center justify-center text-sm mr-2">✓</span>
              <span class="text-gray-600">自动编码</span>
            </div>
            <div class="flex items-center">
              <span class="bg-green-100 text-green-600 rounded-full w-6 h-6 flex items-center justify-center text-sm mr-2">✓</span>
              <span class="text-gray-600">测试生成</span>
            </div>
            <div class="flex items-center">
              <span class="bg-green-100 text-green-600 rounded-full w-6 h-6 flex items-center justify-center text-sm mr-2">✓</span>
              <span class="text-gray-600">代码审查</span>
            </div>
            <div class="flex items-center">
              <span class="bg-green-100 text-green-600 rounded-full w-6 h-6 flex items-center justify-center text-sm mr-2">✓</span>
              <span class="text-gray-600">项目构建</span>
            </div>
            <div class="flex items-center">
              <span class="bg-green-100 text-green-600 rounded-full w-6 h-6 flex items-center justify-center text-sm mr-2">✓</span>
              <span class="text-gray-600">应用部署</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { pipelineApi } from '../api'

const router = useRouter()
const requirementMd = ref('')
const projectLocalPath = ref('/Users/bage/bage/github/study/study-ai-dev-flow/demo-backend')
const loading = ref(false)
const error = ref('')

const handleSubmit = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await pipelineApi.startPipeline({
      requirementMd: requirementMd.value,
      projectLocalPath: projectLocalPath.value
    })
    router.push(`/pipelines/${response.data.pipelineId}`)
  } catch (e) {
    error.value = e.response?.data?.message || '启动失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>
