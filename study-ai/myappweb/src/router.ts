import { createRouter, createWebHashHistory } from 'vue-router'

import Demo from "./pages/Demo.vue";
import Home from "./pages/Home.vue";
import Test from "./pages/Test.vue";
import Activities from "./pages/Activities.vue";
import VideoList from "./pages/VideoList.vue";
import SchoolList from "./pages/SchoolList.vue";
import VideoPlayer from "./pages/VideoPlayer.vue";
import MapTrajectory from "./pages/MapTrajectory.vue";
import TrajectoryCheckpoint from "./pages/TrajectoryCheckpoint.vue";
import Login from "./pages/Login.vue";
import Register from "./pages/Register.vue";

// 定义一些路由，新增地图轨迹页面
// 每个路由都需要映射到一个组件。
// 我们后面再讨论嵌套路由。
const routes = [
  { path: '/', component: Home },
  { path: '/demo', component: Demo },
  { path: '/test', component: Test },
  { path: '/activities', component: Activities },
  { path: '/video-list', component: VideoList },
  { path: '/school-list', component: SchoolList },
  { path: '/video-player', component: VideoPlayer },
  { path: '/map-trajectory', component: MapTrajectory },
  { path: '/trajectory-checkpoint', component: TrajectoryCheckpoint },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
]

// 3. 创建路由实例并传递 `routes` 配置
// 你可以在这里输入更多的配置，但我们在这里
// 暂时保持简单
const router = createRouter({
  // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
  history: createWebHashHistory(),
  routes, // `routes: routes` 的缩写
})

export default router;

