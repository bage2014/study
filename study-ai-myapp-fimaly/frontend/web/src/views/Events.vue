<template>
  <div class="min-h-screen bg-gray-50">
    <Header title="历史记录">
      <template #actions>
        <button @click="openAddEventModal" class="btn-primary">
          添加事件
        </button>
      </template>
    </Header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div class="bg-white p-6 rounded-lg shadow animate-slide-up">
        <!-- Family Selector -->
        <div class="mb-6">
          <label for="family" class="block text-sm font-medium text-gray-700 mb-2">选择家族</label>
          <Select
            id="family"
            v-model="selectedFamilyId"
            :options="familyOptions"
            placeholder="请选择家族"
            @change="fetchEvents"
          />
        </div>

        <!-- Events List -->
        <div v-if="eventStore.loading" class="flex justify-center py-16">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        </div>
        <div v-else-if="events.length === 0" class="text-center py-16">
          <p class="text-gray-600">暂无事件数据</p>
        </div>
        <div v-else class="space-y-4">
          <div v-for="(event, index) in events" :key="event.id" class="border border-gray-200 rounded-lg p-5 hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1 cursor-pointer animate-fade-in" :style="{ animationDelay: `${index * 50}ms` }">
            <div class="flex justify-between items-start">
              <div>
                <div class="flex items-center">
                  <div class="w-10 h-10 rounded-full bg-gradient-to-br from-yellow-100 to-yellow-200 flex items-center justify-center mr-3 flex-shrink-0">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                  </div>
                  <div>
                    <h3 class="font-semibold text-gray-900 text-lg">{{ event.title }}</h3>
                    <p class="text-sm text-gray-500 mt-0.5">{{ event.date }}</p>
                  </div>
                </div>
              </div>
              <div class="flex space-x-2">
                <button @click.stop="editEvent(event)" class="p-2 text-green-600 hover:text-green-800 hover:bg-green-50 rounded-lg transition-all duration-200 transform hover:scale-110">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>
                <button @click.stop="deleteEvent(event.id)" class="p-2 text-red-600 hover:text-red-800 hover:bg-red-50 rounded-lg transition-all duration-200 transform hover:scale-110">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </div>
            <p class="mt-4 text-gray-700 leading-relaxed">{{ event.description }}</p>
            <div v-if="event.relatedMembers" class="mt-3 flex items-center">
              <span class="text-sm font-medium text-gray-500">相关成员:</span>
              <span class="text-sm text-green-600 ml-2">{{ event.relatedMembers }}</span>
            </div>
            <div v-if="event.photo" class="mt-4">
              <img :src="event.photo" :alt="event.name" class="max-w-xs rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300">
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Add/Edit Event Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 animate-fade-in" @click.self="showModal = false">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
        <div class="flex items-center mb-4">
          <div class="w-10 h-10 rounded-lg bg-gradient-to-br from-orange-100 to-orange-200 flex items-center justify-center mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-orange-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900">{{ editingEvent ? '编辑事件' : '添加事件' }}</h3>
        </div>
        <form @submit.prevent="handleSubmit">
          <div class="space-y-4">
            <div>
              <label for="name" class="block text-sm font-medium text-gray-700 mb-1.5">事件名称</label>
              <input type="text" id="name" v-model="form.name" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="description" class="block text-sm font-medium text-gray-700 mb-1.5">描述</label>
              <textarea id="description" v-model="form.description" rows="3" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200"></textarea>
            </div>
            <div>
              <label for="eventDate" class="block text-sm font-medium text-gray-700 mb-1.5">日期</label>
              <input type="date" id="eventDate" v-model="form.eventDate" required class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
            <div>
              <label for="relatedMembers" class="block text-sm font-medium text-gray-700 mb-1.5">相关成员</label>
              <input type="text" id="relatedMembers" v-model="form.relatedMembers" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200" placeholder="用逗号分隔多个成员ID">
            </div>
            <div>
              <label for="photo" class="block text-sm font-medium text-gray-700 mb-1.5">照片</label>
              <input type="file" id="photo" @change="handleFileChange" class="w-full px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200">
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button type="button" @click="showModal = false" class="btn-secondary">
              取消
            </button>
            <button type="submit" :disabled="eventStore.loading" class="btn-primary">
              {{ eventStore.loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Delete Confirm Modal -->
    <ConfirmModal 
      :visible="showDeleteConfirm"
      title="确认删除"
      message="确定要删除该事件吗？此操作不可撤销。"
      type="delete"
      @confirm="confirmDelete"
      @cancel="showDeleteConfirm = false"
    />

  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'
import { useEventStore } from '../stores/event'
import { useRouter } from 'vue-router'
import Header from '../components/Header.vue'
import ConfirmModal from '../components/ConfirmModal.vue'
import Select from '../components/Select.vue'

export default {
  name: 'Events',
  components: {
    Header,
    Select,
    ConfirmModal
  },
  setup() {
    const familyStore = useFamilyStore()
    const eventStore = useEventStore()
    const router = useRouter()
    const selectedFamilyId = ref('')
    const showModal = ref(false)
    const showDeleteConfirm = ref(false)
    const editingEvent = ref(null)
    const deletingEventId = ref(null)
    const selectedFile = ref(null)

    const form = ref({
      name: '',
      description: '',
      eventDate: '',
      relatedMembers: ''
    })

    const showToastMsg = (message, type = 'info') => {
      window.showToastMessage(message, type)
    }

    const navigateTo = (path) => {
      router.push(path)
    }

    const events = computed(() => {
      return eventStore.getEventsByFamilyId(selectedFamilyId.value)
    })

    const familyOptions = computed(() => {
      return familyStore.families.map(family => ({
        value: family.id,
        label: family.name
      }))
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

    const deleteEvent = (eventId) => {
      deletingEventId.value = eventId
      showDeleteConfirm.value = true
    }

    const confirmDelete = async () => {
      try {
        await eventStore.deleteEvent(deletingEventId.value)
        showToastMsg('事件删除成功', 'success')
      } catch (error) {
        showToastMsg('事件删除失败: ' + (error.response?.data?.message || error.message), 'error')
      } finally {
        showDeleteConfirm.value = false
        deletingEventId.value = null
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
          showToastMsg('事件更新成功', 'success')
        } else {
          // 添加事件
          await eventStore.createEvent(eventData)
          showToastMsg('事件添加成功', 'success')
        }
        showModal.value = false
      } catch (error) {
        showToastMsg('操作失败: ' + (error.response?.data?.message || error.message), 'error')
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
      showDeleteConfirm,
      editingEvent,
      form,
      navigateTo,
      fetchEvents,
      openAddEventModal,
      editEvent,
      deleteEvent,
      confirmDelete,
      handleFileChange,
      handleSubmit,
      showToastMsg
    }
  }
}
</script>