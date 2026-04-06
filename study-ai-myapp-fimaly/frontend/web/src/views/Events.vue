<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <button @click="navigateTo('/home')" class="mr-4 text-gray-600 hover:text-gray-900">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
            </button>
            <h1 class="text-xl font-bold text-gray-900">历史记录</h1>
          </div>
          <div class="flex items-center">
            <button @click="openAddEventModal" class="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-700">
              添加事件
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-white p-6 rounded-lg shadow">
        <!-- Family Selector -->
        <div class="mb-6">
          <label for="family" class="block text-sm font-medium text-gray-700 mb-2">选择家族</label>
          <select id="family" v-model="selectedFamilyId" @change="fetchEvents" class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-primary focus:border-primary">
            <option value="">请选择家族</option>
            <option v-for="family in families" :key="family.id" :value="family.id">{{ family.name }}</option>
          </select>
        </div>

        <!-- Events List -->
        <div v-if="eventStore.loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>
        <div v-else-if="events.length === 0" class="text-center py-16">
          <p class="text-gray-600">暂无事件数据</p>
        </div>
        <div v-else class="space-y-4">
          <div v-for="event in events" :key="event.id" class="border rounded-md p-4 hover:shadow-md">
            <div class="flex justify-between items-start">
              <div>
                <h3 class="font-medium text-gray-900">{{ event.title }}</h3>
                <p class="text-sm text-gray-600 mt-1">{{ event.date }}</p>
              </div>
              <div>
                <button @click="editEvent(event)" class="text-green-600 hover:text-green-800 mr-3">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>
                <button @click="deleteEvent(event.id)" class="text-red-600 hover:text-red-800">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </div>
            <p class="mt-2 text-gray-700">{{ event.description }}</p>
            <div v-if="event.relatedMembers" class="mt-2">
              <span class="text-sm font-medium text-gray-500">相关成员:</span>
              <span class="text-sm text-gray-700 ml-2">{{ event.relatedMembers }}</span>
            </div>
            <div v-if="event.photo" class="mt-4">
              <img :src="event.photo" :alt="event.name" class="max-w-xs rounded-md">
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Add/Edit Event Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-medium text-gray-900 mb-4">{{ editingEvent ? '编辑事件' : '添加事件' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="space-y-4">
            <div>
              <label for="name" class="block text-sm font-medium text-gray-700">事件名称</label>
              <input type="text" id="name" v-model="form.name" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="description" class="block text-sm font-medium text-gray-700">描述</label>
              <textarea id="description" v-model="form.description" rows="3" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"></textarea>
            </div>
            <div>
              <label for="eventDate" class="block text-sm font-medium text-gray-700">日期</label>
              <input type="date" id="eventDate" v-model="form.eventDate" required class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
            <div>
              <label for="relatedMembers" class="block text-sm font-medium text-gray-700">相关成员</label>
              <input type="text" id="relatedMembers" v-model="form.relatedMembers" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary" placeholder="用逗号分隔多个成员ID">
            </div>
            <div>
              <label for="photo" class="block text-sm font-medium text-gray-700">照片</label>
              <input type="file" id="photo" @change="handleFileChange" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary">
            </div>
          </div>
          <div class="mt-6 flex justify-end">
            <button type="button" @click="showModal = false" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
              取消
            </button>
            <button type="submit" :disabled="eventStore.loading" class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary disabled:opacity-50">
              {{ eventStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'
import { useEventStore } from '../stores/event'
import { useRouter } from 'vue-router'

export default {
  name: 'Events',
  setup() {
    const familyStore = useFamilyStore()
    const eventStore = useEventStore()
    const router = useRouter()
    const selectedFamilyId = ref('')
    const showModal = ref(false)
    const editingEvent = ref(null)
    const selectedFile = ref(null)
    const form = ref({
      name: '',
      description: '',
      eventDate: '',
      relatedMembers: ''
    })

    const navigateTo = (path) => {
      router.push(path)
    }

    const events = computed(() => {
      return eventStore.getEventsByFamilyId(selectedFamilyId.value)
    })

    const fetchEvents = async () => {
      if (selectedFamilyId.value) {
        await eventStore.fetchEventsByFamilyId(selectedFamilyId.value)
      }
    }

    const openAddEventModal = () => {
      editingEvent.value = null
      form.value = {
        name: '',
        description: '',
        eventDate: '',
        relatedMembers: ''
      }
      selectedFile.value = null
      showModal.value = true
    }

    const editEvent = (event) => {
      editingEvent.value = event
      form.value = {
        name: event.title,
        description: event.description,
        eventDate: event.date,
        relatedMembers: event.relatedMembers
      }
      selectedFile.value = null
      showModal.value = true
    }

    const deleteEvent = async (eventId) => {
      if (confirm('确定要删除这个事件吗？')) {
        try {
          await eventStore.deleteEvent(eventId)
          alert('事件删除成功')
        } catch (error) {
          alert('事件删除失败: ' + (error.response?.data?.message || error.message))
        }
      }
    }

    const handleFileChange = (event) => {
      selectedFile.value = event.target.files[0]
    }

    const handleSubmit = async () => {
      try {
        const eventData = {
          title: form.value.name,
          description: form.value.description,
          date: form.value.eventDate,
          location: '', // 暂时为空，可根据需要添加
          familyId: selectedFamilyId.value
        }

        // 如果有文件上传，需要处理文件
        if (selectedFile.value) {
          const formData = new FormData()
          formData.append('file', selectedFile.value)
          formData.append('title', eventData.title)
          formData.append('description', eventData.description)
          formData.append('date', eventData.date)
          formData.append('location', eventData.location)
          formData.append('familyId', eventData.familyId)

          // 这里应该调用文件上传API
          // const uploadResponse = await axios.post('/api/upload', formData)
          // eventData.photo = uploadResponse.data.url
        }

        if (editingEvent.value) {
          // 编辑事件
          await eventStore.updateEvent(editingEvent.value.id, eventData)
          alert('事件更新成功')
        } else {
          // 添加事件
          await eventStore.createEvent(eventData)
          alert('事件添加成功')
        }
        showModal.value = false
      } catch (error) {
        alert('操作失败: ' + (error.response?.data?.message || error.message))
      }
    }

    onMounted(async () => {
      await familyStore.fetchFamilies()
    })

    return {
      familyStore,
      eventStore,
      families: familyStore.families,
      events,
      selectedFamilyId,
      showModal,
      editingEvent,
      form,
      navigateTo,
      fetchEvents,
      openAddEventModal,
      editEvent,
      deleteEvent,
      handleFileChange,
      handleSubmit
    }
  }
}
</script>