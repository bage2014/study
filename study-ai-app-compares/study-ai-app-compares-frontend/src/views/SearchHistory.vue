<template>
  <div class="history-container">
    <header class="page-header">
      <div class="header-content">
        <h1 class="page-title">📜 搜索历史</h1>
        <button class="back-btn" @click="$router.push('/')">← 返回首页</button>
      </div>
    </header>

    <main class="page-content">
      <div v-if="searchStore.history.length === 0" class="empty-state">
        <div class="empty-icon">📋</div>
        <h3>暂无搜索记录</h3>
        <p>去首页搜索商品，开始比价之旅吧！</p>
      </div>

      <div v-else class="history-list">
        <div class="action-bar">
          <span class="history-count">共 {{ searchStore.history.length }} 条记录</span>
          <button class="clear-btn" @click="clearHistory">清空记录</button>
        </div>

        <div class="history-items">
          <div 
            v-for="(item, index) in searchStore.history" 
            :key="item.keyword"
            class="history-item"
          >
            <span class="item-index">{{ index + 1 }}</span>
            <button class="item-keyword" @click="goSearch(item.keyword)">
              {{ item.keyword }}
            </button>
            <span class="item-time">{{ item.timestamp }}</span>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { useSearchStore } from '../stores/search'

const searchStore = useSearchStore()

const goSearch = (keyword) => {
  searchStore.searchKeyword = keyword
  searchStore.clearResult()
  searchStore.search(keyword, searchStore.selectedAddressId)
  window.location.href = '/'
}

const clearHistory = () => {
  if (!confirm('确定要清空所有搜索记录吗？')) {
    return
  }
  searchStore.clearHistory()
}
</script>

<style scoped>
.history-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.page-header {
  padding: 40px 20px;
  text-align: center;
}

.header-content {
  color: white;
}

.page-title {
  font-size: 2rem;
  margin-bottom: 20px;
  font-weight: 700;
}

.back-btn {
  padding: 10px 20px;
  background: rgba(255,255,255,0.2);
  color: white;
  border: 1px solid rgba(255,255,255,0.3);
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.back-btn:hover {
  background: rgba(255,255,255,0.3);
}

.page-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px 40px;
}

.empty-state {
  background: white;
  border-radius: 16px;
  padding: 80px 40px;
  text-align: center;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.empty-state h3 {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 10px;
}

.empty-state p {
  color: #888;
}

.history-list {
  background: white;
  border-radius: 16px;
  overflow: hidden;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 25px;
  border-bottom: 1px solid #eee;
}

.history-count {
  font-size: 14px;
  color: #666;
}

.clear-btn {
  padding: 8px 16px;
  background: #f0f0f0;
  color: #666;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.clear-btn:hover {
  background: #ffeeba;
  color: #856404;
}

.history-items {
  padding: 10px 0;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 25px;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.3s;
}

.history-item:hover {
  background: #f8f9fa;
}

.history-item:last-child {
  border-bottom: none;
}

.item-index {
  width: 24px;
  height: 24px;
  background: #667eea;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
}

.item-keyword {
  flex: 1;
  text-align: left;
  background: none;
  border: none;
  font-size: 15px;
  color: #333;
  cursor: pointer;
  padding: 0;
}

.item-keyword:hover {
  color: #667eea;
  text-decoration: underline;
}

.item-time {
  font-size: 13px;
  color: #999;
}

@media (max-width: 768px) {
  .history-item {
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .item-time {
    width: 100%;
    margin-left: 39px;
  }
}
</style>