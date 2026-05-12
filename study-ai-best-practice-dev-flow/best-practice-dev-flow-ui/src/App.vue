<template>
  <div class="app-container">
    <a-layout class="layout-container">
      <a-layout-header class="header">
        <div class="logo-container">
          <div class="logo">
            <UserOutlined class="logo-icon" />
            <span class="logo-text">DevFlow</span>
          </div>
        </div>
        <div class="header-menu">
          <a-menu theme="dark" mode="horizontal" :defaultSelectedKeys="[currentPage]">
            <a-menu-item key="home" @click="currentPage = 'home'">
              <template #icon><HomeOutlined /></template>
              首页
            </a-menu-item>
            <a-menu-item key="users" v-if="currentUser" @click="currentPage = 'users'">
              <template #icon><UserOutlined /></template>
              用户管理
            </a-menu-item>
            <a-menu-item key="track" v-if="currentUser" @click="currentPage = 'track'">
              <template #icon><CompassOutlined /></template>
              用户轨迹
            </a-menu-item>
          </a-menu>
          <div class="header-actions">
            <template v-if="currentUser">
              <a-dropdown>
                <a class="user-info">
                  <UserOutlined />
                  <span>{{ currentUser.username }}</span>
                  <DownOutlined class="dropdown-icon" />
                </a>
                <template #overlay>
                  <a-menu>
                    <a-menu-item key="profile">
                      <template #icon><UserOutlined /></template>
                      个人中心
                    </a-menu-item>
                    <a-menu-divider />
                    <a-menu-item key="logout" @click="handleLogout">
                      <template #icon><LogoutOutlined /></template>
                      退出登录
                    </a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </template>
            <template v-else>
              <a-button type="primary" @click="showLoginModal = true" class="login-btn">
                <template #icon><LoginOutlined /></template>
                登录
              </a-button>
            </template>
          </div>
        </div>
      </a-layout-header>
      
      <a-layout-content class="content">
        <div class="content-wrapper">
          <template v-if="currentPage === 'home'">
            <div class="home-page">
              <a-card class="welcome-card" :bordered="false">
                <div class="welcome-content">
                  <div class="welcome-icon">
                    <StarOutlined />
                  </div>
                  <h1 class="welcome-title">欢迎使用 DevFlow</h1>
                  <p class="welcome-desc">最佳实践开发流程管理系统</p>
                  <div class="welcome-stats">
                    <a-statistic title="在线用户" :value="onlineCount" suffix="人" />
                    <a-statistic title="轨迹记录" :value="trackCount" suffix="条" />
                    <a-statistic title="活跃项目" :value="projectCount" suffix="个" />
                  </div>
                </div>
              </a-card>
              
              <div class="feature-cards">
                <a-card hoverable class="feature-card">
                  <div class="feature-icon user-icon">
                    <UserOutlined />
                  </div>
                  <h3>用户管理</h3>
                  <p>管理系统用户，支持注册、登录和权限管理</p>
                  <a-button type="link" v-if="currentUser" @click="currentPage = 'users'">查看详情</a-button>
                </a-card>
                
                <a-card hoverable class="feature-card">
                  <div class="feature-icon track-icon">
                    <MapOutlined />
                  </div>
                  <h3>轨迹追踪</h3>
                  <p>记录和展示用户位置轨迹，支持地图可视化</p>
                  <a-button type="link" v-if="currentUser" @click="currentPage = 'track'">查看详情</a-button>
                </a-card>
                
                <a-card hoverable class="feature-card">
                  <div class="feature-icon chart-icon">
                    <BarChartOutlined />
                  </div>
                  <h3>数据分析</h3>
                  <p>提供数据统计和分析报表功能</p>
                  <a-button type="link" disabled>即将上线</a-button>
                </a-card>
              </div>
            </div>
          </template>
          
          <template v-else-if="currentPage === 'users'">
            <UserTable ref="userTableRef" />
          </template>
          
          <template v-else-if="currentPage === 'track'">
            <a-card title="用户轨迹" :bordered="false" class="track-card">
              <TrackMap ref="trackMapRef" />
            </a-card>
          </template>
        </div>
      </a-layout-content>
      
      <a-layout-footer class="footer">
        <p>© 2024 DevFlow - 最佳实践开发流程管理系统</p>
      </a-layout-footer>
    </a-layout>

    <LoginModal
      v-model:visible="showLoginModal"
      @login-success="handleLoginSuccess"
      @show-register="showRegisterModal = true"
    />
    
    <RegisterModal
      v-model:visible="showRegisterModal"
      @register-success="handleRegisterSuccess"
      @show-login="showLoginModal = true"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { 
  HomeOutlined, 
  UserOutlined, 
  CompassOutlined, 
  DownOutlined, 
  LoginOutlined, 
  LogoutOutlined,
  StarOutlined,
  BarChartOutlined
} from '@ant-design/icons-vue'
import LoginModal from './components/LoginModal.vue'
import RegisterModal from './components/RegisterModal.vue'
import UserTable from './components/UserTable.vue'
import TrackMap from './components/TrackMap.vue'

const currentPage = ref('home')
const showLoginModal = ref(false)
const showRegisterModal = ref(false)
const currentUser = ref(null)
const userTableRef = ref(null)
const trackMapRef = ref(null)

const onlineCount = ref(0)
const trackCount = ref(0)
const projectCount = ref(0)

const handleLoginSuccess = (user) => {
  currentUser.value = user
  currentPage.value = 'home'
  loadStats()
}

const handleRegisterSuccess = (user) => {
  showRegisterModal.value = false
  showLoginModal.value = true
}

const handleLogout = () => {
  currentUser.value = null
  currentPage.value = 'home'
}

const loadStats = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/users')
    const users = await response.json()
    onlineCount.value = users.length
    
    const trackResponse = await fetch('http://localhost:8080/api/trackpoints?userId=1')
    const tracks = await trackResponse.json()
    trackCount.value = tracks.length
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style>
.app-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.layout-container {
  min-height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.logo-container {
  flex: 1;
}

.logo {
  display: flex;
  align-items: center;
  color: white;
  font-size: 20px;
  font-weight: bold;
}

.logo-icon {
  font-size: 24px;
  margin-right: 8px;
}

.logo-text {
  letter-spacing: 1px;
}

.header-menu {
  display: flex;
  align-items: center;
  gap: 24px;
}

.header-actions {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.1);
  cursor: pointer;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.2);
}

.user-info span {
  margin: 0 8px;
}

.dropdown-icon {
  font-size: 12px;
}

.login-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  padding: 4px 20px;
}

.login-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.content {
  padding: 24px;
}

.content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
}

.home-page {
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 48px;
  margin-bottom: 24px;
}

.welcome-content {
  text-align: center;
  color: white;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 24px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
}

.welcome-title {
  font-size: 36px;
  font-weight: bold;
  margin: 0 0 12px;
}

.welcome-desc {
  font-size: 18px;
  opacity: 0.9;
  margin: 0 0 32px;
}

.welcome-stats {
  display: flex;
  justify-content: center;
  gap: 64px;
}

.welcome-stats :deep(.ant-statistic-title) {
  color: rgba(255, 255, 255, 0.8);
}

.welcome-stats :deep(.ant-statistic-content) {
  color: white;
}

.feature-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.feature-card {
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 16px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.track-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.chart-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.feature-card h3 {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: 600;
}

.feature-card p {
  margin: 0 0 16px;
  color: #8c8c8c;
  font-size: 14px;
}

.track-card {
  border-radius: 12px;
}

.footer {
  text-align: center;
  background: #001529;
  color: rgba(255, 255, 255, 0.6);
  padding: 24px;
}

.footer p {
  margin: 0;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    padding: 16px;
  }
  
  .header-menu {
    flex-wrap: wrap;
    justify-content: center;
    gap: 12px;
    margin-top: 12px;
  }
  
  .welcome-stats {
    flex-direction: column;
    gap: 16px;
  }
  
  .feature-cards {
    grid-template-columns: 1fr;
  }
}
</style>