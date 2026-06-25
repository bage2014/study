<template>
  <div class="home-container">
    <header class="app-header">
      <div class="header-content">
        <h1 class="app-title">💰 价格比较助手</h1>
        <p class="app-subtitle">一键对比美团、京东外卖、淘宝闪购价格</p>
      </div>
    </header>

    <main class="main-content">
      <div class="search-section">
        <div class="search-box">
          <input
            v-model="localKeyword"
            type="text"
            class="search-input"
            placeholder="输入商品名称，如：生椰拿铁、汉堡套餐..."
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch(localKeyword.value)" :disabled="searchStore.loading">
            <span>搜索</span>
          </button>
        </div>

        <div class="address-selector">
          <label class="address-label">配送地址：</label>
          <select v-model="localAddressId" class="address-select">
            <option value="">请选择收货地址</option>
            <option v-for="addr in addressStore.addresses" :key="addr.id" :value="addr.id">
              {{ addr.receiverName }} - {{ addr.provinceCityDistrict }} {{ addr.detailAddress }}
            </option>
          </select>
          <button class="add-address-btn" @click="$router.push('/addresses')">+ 添加地址</button>
        </div>
      </div>

      <div v-if="searchStore.loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在查询各平台价格...</p>
      </div>

      <div v-else-if="searchStore.result" class="result-section">
        <div class="result-header">
          <h2>「{{ searchStore.result.productName }}」的价格对比结果</h2>
          <p class="address-info">配送至：{{ searchStore.result.address }}</p>
        </div>

        <div v-if="searchStore.result.bestRecommendation" class="recommendation-card">
          <div class="recommendation-badge">🏆 推荐购买</div>
          <div class="recommendation-content">
            <img :src="searchStore.result.bestRecommendation.platformLogo" alt="平台logo" class="platform-logo-small" />
            <div class="recommendation-info">
              <span class="platform-name">{{ searchStore.result.bestRecommendation.platform }}</span>
              <span class="product-name">{{ searchStore.result.bestRecommendation.productName }}</span>
              <span class="best-price">¥{{ searchStore.result.bestRecommendation.totalPrice.toFixed(2) }}</span>
            </div>
            <div class="recommendation-tags">
              <span class="tag">价格最低</span>
              <span class="tag">{{ searchStore.result.bestRecommendation.promotion }}</span>
            </div>
          </div>
        </div>

        <div class="price-cards">
          <div
            v-for="item in searchStore.result.prices"
            :key="item.platform"
            class="price-card"
            :class="{ 'best-price-card': searchStore.result.bestRecommendation && item.platform === searchStore.result.bestRecommendation.platform }"
          >
            <div class="card-header">
              <img :src="item.platformLogo" alt="平台logo" class="platform-logo" />
              <span class="platform-title">{{ item.platform }}</span>
            </div>
            <div class="card-body">
              <img :src="item.productImage" alt="商品图片" class="product-image" />
              <div class="product-info">
                <h3 class="product-title">{{ item.productName }}</h3>
                <p class="store-name">{{ item.storeName }}</p>
                <div class="store-meta">
                  <span class="rating">⭐ {{ item.storeRating }}</span>
                  <span class="delivery-time">⏱️ {{ item.deliveryTime }}分钟</span>
                </div>
              </div>
            </div>
            <div class="card-footer">
              <div class="price-detail">
                <span class="item-price">¥{{ item.price.toFixed(2) }}</span>
                <span class="delivery-fee">配送费 ¥{{ item.deliveryFee.toFixed(2) }}</span>
              </div>
              <div class="total-price">
                <span class="total-label">合计：</span>
                <span class="total-value">¥{{ item.totalPrice.toFixed(2) }}</span>
              </div>
              <div v-if="item.promotion" class="promotion-tag">{{ item.promotion }}</div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <div class="empty-icon">🔍</div>
        <h3>输入商品名称开始比价</h3>
        <p>支持对比美团、京东外卖、淘宝闪购三大平台</p>
        
        <div v-if="searchStore.history.length > 0" class="search-history">
          <h4>最近搜索</h4>
          <div class="history-tags">
            <button 
              v-for="item in searchStore.history" 
              :key="item.keyword"
              class="history-tag"
              @click="handleSearch(item.keyword)"
            >
              {{ item.keyword }}
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useAddressStore } from '../stores/address'
import { useSearchStore } from '../stores/search'

const addressStore = useAddressStore()
const searchStore = useSearchStore()

const localKeyword = ref('')
const localAddressId = ref('')

onMounted(() => {
  addressStore.loadAddresses()
})

const handleSearch = async (keyword = localKeyword.value) => {
  localKeyword.value = keyword
  try {
    await searchStore.search(keyword, localAddressId.value)
  } catch (error) {
    alert(error.message)
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.app-header {
  padding: 40px 20px;
  text-align: center;
}

.header-content {
  color: white;
}

.app-title {
  font-size: 2.5rem;
  margin-bottom: 10px;
  font-weight: 700;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
}

.app-subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 40px;
}

.search-section {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}

.search-box {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;
}

.search-input {
  flex: 1;
  padding: 15px 20px;
  font-size: 16px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  transition: border-color 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #667eea;
}

.search-btn {
  padding: 15px 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.search-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
}

.search-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.address-selector {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
}

.address-label {
  font-weight: 500;
  color: #333;
}

.address-select {
  padding: 10px 15px;
  font-size: 14px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  min-width: 300px;
  cursor: pointer;
}

.add-address-btn {
  padding: 10px 20px;
  background: #f0f0f0;
  color: #667eea;
  border: 2px dashed #667eea;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.add-address-btn:hover {
  background: #667eea;
  color: white;
  border-style: solid;
}

.loading-container {
  text-align: center;
  padding: 60px;
  background: white;
  border-radius: 16px;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.result-section {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.result-header {
  background: white;
  padding: 20px 30px;
  border-radius: 16px;
  margin-bottom: 20px;
}

.result-header h2 {
  color: #333;
  margin-bottom: 8px;
}

.address-info {
  color: #666;
  font-size: 14px;
}

.recommendation-card {
  background: linear-gradient(135deg, #ffd700 0%, #ffb700 100%);
  border-radius: 16px;
  padding: 25px;
  margin-bottom: 30px;
  box-shadow: 0 10px 30px rgba(255, 215, 0, 0.3);
}

.recommendation-badge {
  font-size: 14px;
  font-weight: 600;
  background: rgba(0,0,0,0.1);
  padding: 5px 15px;
  border-radius: 20px;
  display: inline-block;
  margin-bottom: 15px;
}

.recommendation-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.platform-logo-small {
  width: 50px;
  height: 50px;
  border-radius: 12px;
}

.recommendation-info {
  flex: 1;
}

.platform-name {
  display: block;
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.product-name {
  display: block;
  color: #555;
  font-size: 14px;
  margin-bottom: 10px;
}

.best-price {
  font-size: 2rem;
  font-weight: 700;
  color: #c41e3a;
}

.recommendation-tags {
  display: flex;
  gap: 10px;
}

.tag {
  background: rgba(0,0,0,0.15);
  padding: 5px 12px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: 500;
}

.price-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 25px;
}

.price-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 5px 20px rgba(0,0,0,0.08);
  transition: transform 0.3s, box-shadow 0.3s;
  border: 2px solid transparent;
}

.price-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0,0,0,0.12);
}

.best-price-card {
  border-color: #ffd700;
  box-shadow: 0 5px 20px rgba(255, 215, 0, 0.2);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 15px 20px;
  background: #f8f9fa;
}

.platform-logo {
  width: 35px;
  height: 35px;
  border-radius: 8px;
}

.platform-title {
  font-weight: 600;
  color: #333;
}

.card-body {
  padding: 20px;
  display: flex;
  gap: 15px;
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 10px;
}

.product-info {
  flex: 1;
}

.product-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.store-name {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.store-meta {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #888;
}

.card-footer {
  padding: 15px 20px;
  background: #fafafa;
  border-top: 1px solid #eee;
}

.price-detail {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.item-price {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.delivery-fee {
  font-size: 13px;
  color: #888;
}

.total-price {
  display: flex;
  align-items: baseline;
  gap: 5px;
  margin-bottom: 10px;
}

.total-label {
  font-size: 14px;
  color: #666;
}

.total-value {
  font-size: 24px;
  font-weight: 700;
  color: #c41e3a;
}

.promotion-tag {
  display: inline-block;
  background: #fff3cd;
  color: #856404;
  padding: 4px 12px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: 500;
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
  margin-bottom: 30px;
}

.search-history h4 {
  font-size: 1rem;
  color: #666;
  margin-bottom: 15px;
}

.history-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.history-tag {
  padding: 8px 16px;
  background: #f0f0f0;
  border: none;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.history-tag:hover {
  background: #667eea;
  color: white;
}

@media (max-width: 768px) {
  .app-title {
    font-size: 1.8rem;
  }
  
  .search-box {
    flex-direction: column;
  }
  
  .search-btn {
    width: 100%;
  }
  
  .address-selector {
    flex-direction: column;
    align-items: stretch;
  }
  
  .address-select {
    min-width: auto;
  }
  
  .price-cards {
    grid-template-columns: 1fr;
  }
  
  .recommendation-content {
    flex-direction: column;
    text-align: center;
  }
  
  .recommendation-tags {
    justify-content: center;
  }
}
</style>