<template>
  <div class="message-container">
    <div class="message-header">
      <h1>{{ t('menu.messages') }}</h1>
      <button class="send-message-btn" @click="showSendModal = true">
        {{ t('button.sendMessage') }}
      </button>
    </div>

    <!-- 消息列表区域 -->
    <div class="message-list">
      <div v-if="isLoading" class="loading-indicator">
        {{ t('message.loading') }}...
      </div>
      <div v-else-if="messages.length === 0" class="empty-state">
        {{ t('message.noMessagesFound') }}
      </div>
      <div v-else class="messages-wrapper">
        <div v-for="message in messages" :key="message.id" class="message-item">
          <div class="message-avatar">
            <img :src="message.senderAvatar" alt="{{ message.senderName }}" />
          </div>
          <div class="message-content">
            <div class="message-header-info">
              <span class="sender-name">{{ message.senderName }}</span>
              <span class="message-time">{{ formatDate(message.createTime) }}</span>
              <span v-if="!message.isRead" class="unread-indicator">{{ t('label.unread') }}</span>
            </div>
            <p class="message-text">{{ message.content }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <div v-if="totalElements > 0" class="pagination-container">
      <div class="pagination-info">
        {{ t('label.showingResults', { current: Math.min(currentPage * pageSize, totalElements), total: totalElements }) }}
      </div>
      <div class="pagination-controls">
        <button @click="changePage(1)" :disabled="currentPage === 1">
          {{ t('button.firstPage') }}
        </button>
        <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">
          {{ t('button.prevPage') }}
        </button>
        <span class="page-info">
          {{ t('label.page', { page: currentPage, total: totalPages }) }}
        </span>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">
          {{ t('button.nextPage') }}
        </button>
        <button @click="changePage(totalPages)" :disabled="currentPage === totalPages">
          {{ t('button.lastPage') }}
        </button>
        <select v-model="pageSize" @change="changePageSize">
          <option value="5">5</option>
          <option value="10">10</option>
          <option value="20">20</option>
          <option value="50">50</option>
        </select>
      </div>
    </div>

    <!-- 发送消息弹窗 -->
    <div v-if="showSendModal" class="modal-overlay" @click="showSendModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>{{ t('menu.sendMessage') }}</h2>
          <button class="close-btn" @click="showSendModal = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="messageContent">{{ t('label.messageContent') }}:</label>
            <textarea 
              id="messageContent" 
              v-model="messageContent"
              placeholder="{{ t('placeholder.messageContent') }}"
              rows="4"
              :disabled="isSending"
            ></textarea>
          </div>
          <div v-if="sendMessageError" class="error-message">
            {{ sendMessageError }}
          </div>
        </div>
        <div class="modal-footer">
          <button @click="showSendModal = false" :disabled="isSending">
            {{ t('button.cancel') }}
          </button>
          <button @click="sendMessage" :disabled="isSending || !messageContent.trim()">
            {{ isSending ? t('button.sending') : t('button.send') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import http from '../components/http/http.service';
import type { ApiResponse } from '../components/http/types';

const { t } = useI18n();

// 类型定义
interface Message {
  id: number;
  senderId: number;
  content: string;
  type: number;
  isRead: boolean;
  createTime: string;
  readTime: string | null;
  senderName: string;
  senderAvatar: string;
  receiverName: string;
  receiverAvatar: string;
}

interface MessageResponse {
  messages: Message[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

// 状态管理
const messages = ref<Message[]>([]);
const isLoading = ref(false);
const currentPage = ref(1);
const pageSize = ref(5);
const totalElements = ref(0);
const totalPages = ref(0);

// 发送消息弹窗状态
const showSendModal = ref(false);
const messageContent = ref('');
const isSending = ref(false);
const sendMessageError = ref('');

// 查询消息列表
const fetchMessages = async () => {
  isLoading.value = true;
  try {
    const response = await http.post<ApiResponse<MessageResponse>>('/message/query', {
        page: currentPage.value,
        size: pageSize.value
    });
    
    messages.value = response.data.messages;
    totalElements.value = response.data.totalElements;
    totalPages.value = response.data.totalPages;
    currentPage.value = response.data.currentPage;
    pageSize.value = response.data.pageSize;
  } catch (error) {
    console.error('Failed to fetch messages:', error);
    // 这里可以添加错误处理逻辑
  } finally {
    isLoading.value = false;
  }
};

// 发送消息
const sendMessage = async () => {
  if (!messageContent.value.trim()) {
    sendMessageError.value = t('message.incompleteFields');
    return;
  }
  
  isSending.value = true;
  sendMessageError.value = '';
  
  try {
    await http.get<ApiResponse<any>>('/message/send', {
      params: {
        content: messageContent.value.trim()
      }
    });
    
    // 发送成功后重置表单并关闭弹窗
    messageContent.value = '';
    showSendModal.value = false;
    
    // 重新加载消息列表
    await fetchMessages();
  } catch (error) {
    console.error('Failed to send message:', error);
    sendMessageError.value = t('message.sendMessageFailed');
  } finally {
    isSending.value = false;
  }
};

// 切换页面
const changePage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    fetchMessages();
  }
};

// 改变页面大小
const changePageSize = () => {
  currentPage.value = 1;
  fetchMessages();
};

// 格式化日期
const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date);
};

// 初始加载消息列表
onMounted(() => {
  fetchMessages();
});
</script>

<style scoped>
.message-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.send-message-btn {
  padding: 0.5rem 1rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius);
  cursor: pointer;
  font-weight: 500;
}

.send-message-btn:hover {
  opacity: 0.9;
}

.message-list {
  background-color: var(--bg-light);
  border-radius: var(--radius);
  padding: 1rem;
  margin-bottom: 1.5rem;
}

.loading-indicator,
.empty-state {
  text-align: center;
  padding: 2rem;
  color: var(--text-light);
}

.message-item {
  display: flex;
  gap: 1rem;
  padding: 1rem;
  border-bottom: 1px solid var(--border-color);
  transition: background-color 0.2s ease;
}

.message-item:last-child {
  border-bottom: none;
}

.message-item:hover {
  background-color: var(--bg-color);
}

.message-avatar img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.message-content {
  flex: 1;
}

.message-header-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.sender-name {
  font-weight: 500;
  color: var(--text-color);
}

.message-time {
  color: var(--text-light);
  font-size: 0.875rem;
}

.unread-indicator {
  background-color: var(--primary-color);
  color: white;
  font-size: 0.75rem;
  padding: 0.125rem 0.5rem;
  border-radius: 0.75rem;
}

.message-text {
  margin: 0;
  color: var(--text-color);
  word-break: break-word;
}

/* 分页样式 */
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background-color: var(--bg-light);
  border-radius: var(--radius);
}

.pagination-info {
  color: var(--text-light);
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.pagination-controls button {
  padding: 0.25rem 0.75rem;
  border: 1px solid var(--border-color);
  background-color: var(--bg-color);
  color: var(--text-color);
  border-radius: var(--radius);
  cursor: pointer;
}

.pagination-controls button:hover:not(:disabled) {
  background-color: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.pagination-controls button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  padding: 0 0.5rem;
}

.pagination-controls select {
  padding: 0.25rem;
  border: 1px solid var(--border-color);
  background-color: var(--bg-color);
  color: var(--text-color);
  border-radius: var(--radius);
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: var(--bg-color);
  border-radius: var(--radius);
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h2 {
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--text-light);
}

.modal-body {
  padding: 1rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  background-color: var(--bg-light);
  color: var(--text-color);
  box-sizing: border-box;
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.error-message {
  color: var(--error-color);
  margin-top: 0.5rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1rem;
  border-top: 1px solid var(--border-color);
}

.modal-footer button {
  padding: 0.75rem 1.5rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  cursor: pointer;
  font-weight: 500;
}

.modal-footer button:first-child {
  background-color: var(--bg-light);
  color: var(--text-color);
}

.modal-footer button:last-child {
  background-color: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.modal-footer button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>