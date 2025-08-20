<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'

const { t } = useI18n()
const router = useRouter()

// 跳转到TV列表页面
const goToTvList = () => {
  router.push('/tv-list')
}

// 跳转到地图轨迹页面
const goToMapTrajectory = () => {
  router.push('/map-trajectory')
}
</script>

<template>
  <div class="home-page">
    
    <div class="menu-cards">
      <div class="card" @click="goToTvList">
        <div class="card-icon tv-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <rect x="2" y="7" width="20" height="15" rx="2" ry="2"></rect>
            <polyline points="17 2 12 7 7 2"></polyline>
          </svg>
        </div>
        <div class="card-content">
          <h3>{{ t('menu.tvList') }}</h3>
          <p>{{ t('menu.tvListDescription') }}</p>
        </div>
        <div class="card-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="5" y1="12" x2="19" y2="12"></line>
            <polyline points="12 5 19 12 12 19"></polyline>
          </svg>
        </div>
      </div>
      
      <div class="card" @click="goToMapTrajectory">
        <div class="card-icon map-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="3 6 9 3 15 6 21 3 21 18 15 21 9 18 3 21 3 6"></polygon>
            <line x1="9" y1="3" x2="9" y2="18"></line>
            <line x1="15" y1="6" x2="15" y2="21"></line>
          </svg>
        </div>
        <div class="card-content">
          <h3>{{ t('menu.mapTrajectory') }}</h3>
          <p>{{ t('menu.mapTrajectoryDescription') }}</p>
        </div>
        <div class="card-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="5" y1="12" x2="19" y2="12"></line>
            <polyline points="12 5 19 12 12 19"></polyline>
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-section {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem;
  background-color: var(--bg-light);
  border-radius: var(--radius);
}

.welcome-section h1 {
  color: var(--primary-color);
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
}

.welcome-section p {
  color: var(--text-light);
  font-size: 1.2rem;
  max-width: 800px;
  margin: 0 auto;
}

.menu-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.card {
  display: flex;
  flex-direction: column;
  padding: 2rem;
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  background-color: var(--bg-color);
  cursor: pointer;
  transition: all 0.3s ease;
  height: 100%;
  border: 1px solid var(--border-color);
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  border-color: var(--primary-color);
}

.card-icon {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  margin-bottom: 1.5rem;
  background-color: var(--bg-light);
  color: var(--primary-color);
}

.tv-icon {
  background-color: rgba(66, 185, 131, 0.1);
}

.map-icon {
  background-color: rgba(100, 108, 255, 0.1);
}

.card-content h3 {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  color: var(--text-color);
}

.card-content p {
  color: var(--text-light);
  margin-bottom: 1.5rem;
  flex-grow: 1;
}

.card-arrow {
  display: flex;
  justify-content: flex-end;
  color: var(--primary-color);
}

@media (max-width: 768px) {
  .welcome-section {
    padding: 1.5rem;
  }
  
  .welcome-section h1 {
    font-size: 2rem;
  }
  
  .welcome-section p {
    font-size: 1rem;
  }
  
  .menu-cards {
    grid-template-columns: 1fr;
  }
  
  .card {
    padding: 1.5rem;
  }
}
</style>