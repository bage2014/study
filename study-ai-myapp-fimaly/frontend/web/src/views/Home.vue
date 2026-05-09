<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="首页" />
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      

      <!-- 统计卡片 -->
        

      <!-- Quick Actions -->
      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4 mb-8">
        <!-- 家族管理 -->
        <div 
          @click="navigateTo('/family-management')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-green-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-green-100 to-green-200 text-green-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-green-600 transition-colors">家族管理</h3>
          <p class="mt-1 text-xs text-gray-500">管理家族、成员和关系</p>
        </div>

        <!-- 家族树 -->
        <div 
          @click="navigateTo('/family-tree')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-blue-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-blue-100 to-blue-200 text-blue-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-blue-600 transition-colors">家族树</h3>
          <p class="mt-1 text-xs text-gray-500">查看家族关系图</p>
        </div>

        <!-- 成员管理 -->
        <div 
          @click="navigateTo('/members')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-green-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-green-100 to-green-200 text-green-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-green-600 transition-colors">成员管理</h3>
          <p class="mt-1 text-xs text-gray-500">查看、新建、编辑成员</p>
        </div>

        <!-- 历史记录 -->
        <div 
          @click="navigateTo('/events')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-yellow-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-yellow-100 to-yellow-200 text-yellow-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-yellow-600 transition-colors">历史记录</h3>
          <p class="mt-1 text-xs text-gray-500">查看家族历史</p>
        </div>

        <!-- 多媒体库 -->
        <div 
          @click="navigateTo('/media')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-indigo-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-indigo-100 to-indigo-200 text-indigo-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-indigo-600 transition-colors">多媒体库</h3>
          <p class="mt-1 text-xs text-gray-500">管理家族照片</p>
        </div>

        <!-- 项目进度 -->
        <div 
          @click="navigateTo('/progress')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-purple-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-purple-100 to-purple-200 text-purple-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-purple-600 transition-colors">项目进度</h3>
          <p class="mt-1 text-xs text-gray-500">查看项目开发进度</p>
        </div>

        <!-- 成员大事件记录 -->
        <div 
          @click="navigateTo('/milestones')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-pink-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-pink-100 to-pink-200 text-pink-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-pink-600 transition-colors">成员大事件</h3>
          <p class="mt-1 text-xs text-gray-500">记录成员重要时刻</p>
        </div>

        <!-- 成员位置 -->
        <div 
          @click="navigateTo('/location-map')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-green-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-green-100 to-green-200 text-green-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-green-600 transition-colors">成员位置</h3>
          <p class="mt-1 text-xs text-gray-500">查看成员位置</p>
        </div>

        <!-- AI关系分析 -->
        <div 
          @click="navigateTo('/ai-relationship-analysis')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-blue-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-blue-100 to-blue-200 text-blue-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-blue-600 transition-colors">AI关系分析</h3>
          <p class="mt-1 text-xs text-gray-500">智能分析家族关系</p>
        </div>

        <!-- 操作日志记录 -->
        <div 
          @click="navigateTo('/operation-logs')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-gray-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-gray-100 to-gray-200 text-gray-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-gray-600 transition-colors">操作日志</h3>
          <p class="mt-1 text-xs text-gray-500">查看系统操作记录</p>
        </div>

        <!-- 家族故事生成 -->
        <div 
          @click="navigateTo('/family-stories')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-yellow-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-yellow-100 to-yellow-200 text-yellow-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-yellow-600 transition-colors">家族故事</h3>
          <p class="mt-1 text-xs text-gray-500">生成家族故事</p>
        </div>

        <!-- 图片导入 -->
        <div 
          @click="navigateTo('/image-import-analysis')" 
          class="bg-white p-5 rounded-xl shadow-sm cursor-pointer border-2 border-transparent hover:border-indigo-300 hover:shadow-lg transition-all duration-300 group"
        >
          <div class="flex items-center justify-center h-12 w-12 rounded-xl bg-gradient-to-br from-indigo-100 to-indigo-200 text-indigo-600 mb-3 transition-transform duration-200 group-hover:scale-110 group-hover:rotate-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h3 class="text-sm font-medium text-gray-900 group-hover:text-indigo-600 transition-colors">图片导入</h3>
          <p class="mt-1 text-xs text-gray-500">上传家族关系图自动解析</p>
        </div>
      </div>

      <!-- Family Overview -->
      <div class="bg-white rounded-xl shadow-sm p-6 mb-8">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-medium text-gray-900 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            我的家族
          </h3>
          <button 
            @click="navigateTo('/family-management')" 
            class="px-4 py-2 text-sm text-green-600 bg-green-50 rounded-lg hover:bg-green-100 transition-colors flex items-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            创建家族
          </button>
        </div>

        <div v-if="loading" class="flex justify-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-4 border-green-500 border-t-transparent"></div>
        </div>
        <div v-else-if="families.length === 0" class="text-center py-12">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-20 w-20 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <p class="text-gray-500 mt-4 text-lg">您还没有创建家族</p>
          <p class="text-gray-400 mt-2">创建一个家族开始您的家族记录之旅</p>
          <button 
            @click="navigateTo('/family-management')" 
            class="mt-6 px-6 py-2.5 bg-green-500 text-white rounded-lg hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200 btn-active"
          >
            创建家族
          </button>
        </div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div 
            v-for="(family, index) in families" 
            :key="family.id" 
            class="border rounded-xl p-5 hover:shadow-lg transition-all duration-300 cursor-pointer group animate-slide-up"
            :style="{ animationDelay: `${index * 0.1}s` }"
            @click="navigateTo(`/family-tree`)"
          >
            <div class="flex items-start justify-between">
              <div>
                <div class="flex items-center">
                  <h4 class="font-medium text-gray-900 group-hover:text-green-600 transition-colors">{{ family.name }}</h4>
                  <span class="ml-2 px-2 py-0.5 text-xs bg-green-100 text-green-600 rounded-full">活跃</span>
                </div>
                <p class="text-sm text-gray-600 mt-1">{{ family.description || '暂无描述' }}</p>
              </div>
              <div class="w-8 h-8 rounded-full bg-gray-100 flex items-center justify-center group-hover:bg-green-100 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-400 group-hover:text-green-600 transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
            <div class="mt-4 flex items-center justify-between">
              <div class="flex items-center space-x-1">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
                <span class="text-xs text-gray-500">{{ getMemberCount(family.id) }} 位成员</span>
              </div>
              <span class="text-xs text-gray-400">{{ formatDate(family.createdAt) }}</span>
            </div>
            <div class="mt-4 flex space-x-2">
              <button 
                @click.stop="navigateTo(`/family-tree`)" 
                class="flex-1 px-4 py-1.5 text-sm bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors btn-active text-center"
              >
                查看家族树
              </button>
              <button 
                @click.stop="navigateTo(`/family-management`)" 
                class="flex-1 px-4 py-1.5 text-sm bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors btn-active text-center"
              >
                管理
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 最近动态 -->
      <div class="bg-white rounded-xl shadow-sm p-6">
        <h3 class="text-lg font-medium text-gray-900 flex items-center mb-6">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
          </svg>
          最近动态
        </h3>
        <div class="space-y-4">
          <div 
            v-for="(activity, index) in recentActivities" 
            :key="index"
            class="flex items-start space-x-4 p-4 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <div class="w-10 h-10 rounded-full flex items-center justify-center flex-shrink-0" :class="getActivityIconBg(activity.type)">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" :class="getActivityIconColor(activity.type)" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path v-if="activity.type === 'create'" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                <path v-else-if="activity.type === 'edit'" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
            <div class="flex-1 min-w-0">
              <p class="text-sm text-gray-900">{{ activity.message }}</p>
              <p class="text-xs text-gray-400 mt-1">{{ activity.time }}</p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { computed, ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { useFamilyStore } from '../stores/family'
import { useRouter } from 'vue-router'
import Header from '../components/Header.vue'

export default {
  name: 'Home',
  components: {
    Header
  },
  setup() {
    const userStore = useUserStore()
    const familyStore = useFamilyStore()
    const router = useRouter()

    const familyCount = ref(0)
    const memberCount = ref(0)
    const eventCount = ref(0)
    const mediaCount = ref(0)
    
    // 动画数字
    const animatedFamilyCount = ref(0)
    const animatedMemberCount = ref(0)
    const animatedEventCount = ref(0)
    const animatedMediaCount = ref(0)

    const displayName = computed(() => {
      const user = userStore.user
      if (!user) return ''
      return user.nickname || user.email || ''
    })

    const navigateTo = (path) => {
      router.push(path)
    }

    const getMemberCount = (familyId) => {
      return Math.floor(Math.random() * 20) + 5
    }

    const formatDate = (date) => {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getMonth() + 1}/${d.getDate()}`
    }

    const getActivityIconBg = (type) => {
      const colors = {
        create: 'bg-green-100',
        edit: 'bg-blue-100',
        upload: 'bg-purple-100'
      }
      return colors[type] || 'bg-gray-100'
    }

    const getActivityIconColor = (type) => {
      const colors = {
        create: 'text-green-600',
        edit: 'text-blue-600',
        upload: 'text-purple-600'
      }
      return colors[type] || 'text-gray-600'
    }

    // 数字跳动动画
    const animateNumber = (targetRef, targetValue, duration = 1000) => {
      const startValue = 0
      const startTime = performance.now()
      
      const animate = (currentTime) => {
        const elapsed = currentTime - startTime
        const progress = Math.min(elapsed / duration, 1)
        const easeOut = 1 - Math.pow(1 - progress, 3)
        const currentValue = Math.round(startValue + (targetValue - startValue) * easeOut)
        
        targetRef.value = currentValue
        
        if (progress < 1) {
          requestAnimationFrame(animate)
        }
      }
      
      requestAnimationFrame(animate)
    }

    const recentActivities = ref([
      { type: 'create', message: '创建了新家族「张家族谱」', time: '5分钟前' },
      { type: 'edit', message: '编辑了成员「张三」的信息', time: '30分钟前' },
      { type: 'upload', message: '上传了3张家族照片', time: '1小时前' },
      { type: 'create', message: '添加了新成员「李四」', time: '2小时前' }
    ])

    onMounted(async () => {
      if (!userStore.user) {
        await userStore.fetchCurrentUser()
      }
      await familyStore.fetchFamilies()
      
      familyCount.value = familyStore.families.length || 3
      memberCount.value = Math.floor(Math.random() * 100) + 50
      eventCount.value = Math.floor(Math.random() * 50) + 10
      mediaCount.value = Math.floor(Math.random() * 30) + 5
      
      // 延迟启动动画，让页面先渲染
      setTimeout(() => {
        animateNumber(animatedFamilyCount, familyCount.value, 1200)
        setTimeout(() => animateNumber(animatedMemberCount, memberCount.value, 1200), 100)
        setTimeout(() => animateNumber(animatedEventCount, eventCount.value, 1200), 200)
        setTimeout(() => animateNumber(animatedMediaCount, mediaCount.value, 1200), 300)
      }, 300)
    })

    return {
      families: familyStore.families,
      loading: familyStore.loading,
      displayName,
      navigateTo,
      familyCount,
      memberCount,
      eventCount,
      mediaCount,
      animatedFamilyCount,
      animatedMemberCount,
      animatedEventCount,
      animatedMediaCount,
      getMemberCount,
      formatDate,
      getActivityIconBg,
      getActivityIconColor,
      recentActivities
    }
  }
}
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.5s ease-out;
}

.animate-slide-up {
  animation: slideUp 0.4s ease-out forwards;
  opacity: 0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.btn-active:active {
  transform: scale(0.98);
}
</style>