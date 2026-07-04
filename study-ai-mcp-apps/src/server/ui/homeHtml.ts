import { User } from '../familyStore';

export function generateHomeHtml(user: User | null): string {
  const userName = user?.nickname || user?.email || '用户';

  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>家庭族谱 - 首页</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
    .card-hover {
      transition: all 0.3s ease;
    }
    .card-hover:hover {
      transform: translateY(-4px);
      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
    }
  </style>
</head>
<body class="bg-gray-50 min-h-screen">
  <header class="bg-white shadow-sm sticky top-0 z-10">
    <div class="max-w-6xl mx-auto px-4 py-4">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-green-500 rounded-full flex items-center justify-center">
            <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
            </svg>
          </div>
          <h1 class="text-xl font-bold text-gray-800">家庭族谱</h1>
        </div>
        <div class="flex items-center gap-4">
          <span class="text-gray-600">欢迎，${userName}</span>
          <button onclick="navigateTo('login')" class="text-gray-500 hover:text-gray-700">退出登录</button>
        </div>
      </div>
    </div>
  </header>

  <main class="max-w-6xl mx-auto px-4 py-8">
    <div class="mb-8">
      <h2 class="text-2xl font-bold text-gray-800">欢迎回来</h2>
      <p class="text-gray-500 mt-1">管理您的家族信息，传承家族文化</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div onclick="navigateTo('familyTree')" class="bg-white rounded-xl p-6 shadow-md card-hover cursor-pointer border border-gray-100">
        <div class="w-14 h-14 bg-green-100 rounded-xl flex items-center justify-center mb-4">
          <svg class="w-7 h-7 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-800 mb-2">家族树</h3>
        <p class="text-gray-500 text-sm">可视化展示家族成员关系，直观了解家族结构</p>
      </div>

      <div onclick="navigateTo('familyManage')" class="bg-white rounded-xl p-6 shadow-md card-hover cursor-pointer border border-gray-100">
        <div class="w-14 h-14 bg-blue-100 rounded-xl flex items-center justify-center mb-4">
          <svg class="w-7 h-7 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"></path>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-800 mb-2">家族管理</h3>
        <p class="text-gray-500 text-sm">创建和管理家族信息，维护家族数据</p>
      </div>

      <div onclick="navigateTo('memberManage')" class="bg-white rounded-xl p-6 shadow-md card-hover cursor-pointer border border-gray-100">
        <div class="w-14 h-14 bg-purple-100 rounded-xl flex items-center justify-center mb-4">
          <svg class="w-7 h-7 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-800 mb-2">成员管理</h3>
        <p class="text-gray-500 text-sm">添加和管理家族成员，记录详细信息</p>
      </div>

      <div onclick="navigateTo('relationship')" class="bg-white rounded-xl p-6 shadow-md card-hover cursor-pointer border border-gray-100">
        <div class="w-14 h-14 bg-orange-100 rounded-xl flex items-center justify-center mb-4">
          <svg class="w-7 h-7 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"></path>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-800 mb-2">关系管理</h3>
        <p class="text-gray-500 text-sm">管理成员之间的关系，维护家族血缘网络</p>
      </div>

      <div onclick="navigateTo('history')" class="bg-white rounded-xl p-6 shadow-md card-hover cursor-pointer border border-gray-100">
        <div class="w-14 h-14 bg-red-100 rounded-xl flex items-center justify-center mb-4">
          <svg class="w-7 h-7 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-800 mb-2">历史记录</h3>
        <p class="text-gray-500 text-sm">记录家族重要事件和成员大事件，传承家族历史</p>
      </div>
    </div>

    <div class="mt-12">
      <h3 class="text-xl font-bold text-gray-800 mb-6">快捷统计</h3>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div class="bg-white rounded-xl p-6 shadow-sm">
          <div class="text-3xl font-bold text-green-600" id="familyCount">0</div>
          <div class="text-gray-500 text-sm mt-1">家族数量</div>
        </div>
        <div class="bg-white rounded-xl p-6 shadow-sm">
          <div class="text-3xl font-bold text-blue-600" id="memberCount">0</div>
          <div class="text-gray-500 text-sm mt-1">成员数量</div>
        </div>
        <div class="bg-white rounded-xl p-6 shadow-sm">
          <div class="text-3xl font-bold text-purple-600" id="maleCount">0</div>
          <div class="text-gray-500 text-sm mt-1">男性成员</div>
        </div>
        <div class="bg-white rounded-xl p-6 shadow-sm">
          <div class="text-3xl font-bold text-pink-600" id="femaleCount">0</div>
          <div class="text-gray-500 text-sm mt-1">女性成员</div>
        </div>
      </div>
    </div>
  </main>

  <script>
    function mcpCallTool(toolName, params) {
      return new Promise(function(resolve, reject) {
        var messageId = 'msg-' + Date.now() + '-' + Math.random();
        var handler = function(event) {
          if (event.data && event.data.messageId === messageId) {
            window.removeEventListener('message', handler);
            if (event.data.error) {
              reject(new Error(event.data.error));
            } else {
              resolve(event.data.response);
            }
          }
        };
        window.addEventListener('message', handler);
        window.parent.postMessage({
          messageId: messageId,
          type: 'tool',
          payload: { toolName, params }
        }, '*');
      });
    }

    function navigateTo(page) {
      window.parent.postMessage({ type: 'navigate', page: page }, '*');
    }

    async function loadStats() {
      try {
        var familiesResult = await mcpCallTool('listFamilies', {});
        var membersResult = await mcpCallTool('listMembers', {});

        document.getElementById('familyCount').textContent = familiesResult.families.length;
        document.getElementById('memberCount').textContent = membersResult.members.length;

        var maleCount = membersResult.members.filter(function(m) { return m.gender === 'male'; }).length;
        var femaleCount = membersResult.members.filter(function(m) { return m.gender === 'female'; }).length;

        document.getElementById('maleCount').textContent = maleCount;
        document.getElementById('femaleCount').textContent = femaleCount;
      } catch (error) {
        console.error('Failed to load stats:', error);
      }
    }

    document.addEventListener('DOMContentLoaded', function() {
      loadStats();
    });
  </script>
</body>
</html>`;
}