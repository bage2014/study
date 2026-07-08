import { User } from '../familyStore';

export function generateHistoryHtml(user: User | null, selectedFamilyId: string | undefined): string {
  const userName = user?.nickname || user?.email || '用户';

  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>家庭族谱 - 历史记录</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
    .timeline-line {
      position: absolute;
      left: 18px;
      top: 0;
      bottom: 0;
      width: 2px;
      background: #E5E7EB;
    }
    .timeline-item {
      position: relative;
      padding-left: 48px;
      padding-bottom: 24px;
    }
    .timeline-item:last-child {
      padding-bottom: 0;
    }
    .timeline-dot {
      position: absolute;
      left: 10px;
      top: 4px;
      width: 16px;
      height: 16px;
      border-radius: 50%;
      border: 3px solid white;
    }
    .timeline-dot.event {
      background: #10B981;
    }
    .timeline-dot.milestone {
      background: #3B82F6;
    }
    .timeline-dot.log {
      background: #6B7280;
    }
    @media (min-width: 640px) {
      .timeline-line {
        left: 24px;
      }
      .timeline-item {
        padding-left: 60px;
        padding-bottom: 30px;
      }
      .timeline-dot {
        left: 16px;
        width: 18px;
        height: 18px;
      }
    }
  </style>
</head>
<body class="bg-gray-50 min-h-screen">
  <header class="bg-white shadow-sm sticky top-0 z-10">
    <div class="max-w-6xl mx-auto px-4 py-4">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-4">
          <button onclick="navigateTo('home')" class="text-gray-500 hover:text-gray-700">← 返回</button>
          <h1 class="text-xl font-bold text-gray-800">历史记录</h1>
        </div>
        <div class="flex items-center gap-4">
          <span class="text-gray-600">${userName}</span>
        </div>
      </div>
    </div>
  </header>

  <main class="max-w-6xl mx-auto px-4 py-8">
    <div class="bg-white rounded-xl shadow-sm p-4 mb-6">
      <div class="flex flex-wrap gap-4 items-center">
        <div class="flex items-center gap-2">
          <label class="font-medium text-gray-700">选择家族：</label>
          <select id="familySelector" class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
            <option value="">请选择家族</option>
          </select>
        </div>
        <div class="flex gap-2">
          <button onclick="filterByType('all')" id="filterAll" class="px-4 py-2 bg-green-500 text-white rounded-lg text-sm font-medium">全部</button>
          <button onclick="filterByType('event')" id="filterEvent" class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg text-sm font-medium">家族事件</button>
          <button onclick="filterByType('milestone')" id="filterMilestone" class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg text-sm font-medium">成员大事件</button>
          <button onclick="filterByType('log')" id="filterLog" class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg text-sm font-medium">操作日志</button>
        </div>
        <button onclick="openCreateModal()" class="bg-green-500 hover:bg-green-600 text-white py-2 px-6 rounded-lg font-medium transition-all ml-auto">
          + 添加事件
        </button>
      </div>
    </div>

    <div class="bg-white rounded-xl shadow-sm p-6">
      <div class="relative">
        <div class="timeline-line"></div>
        <div id="timelineContent">
          <div class="text-center py-12 text-gray-500">
            <svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
            </svg>
            <p>请选择一个家族查看历史记录</p>
          </div>
        </div>
      </div>
    </div>
  </main>

  <div id="createModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-md mx-4">
      <h3 class="text-lg font-bold text-gray-800 mb-4">添加家族事件</h3>
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">事件名称 *</label>
          <input type="text" id="eventName" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none" placeholder="请输入事件名称">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">发生日期 *</label>
          <input type="date" id="eventDate" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">描述</label>
          <textarea id="eventDescription" rows="3" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none" placeholder="请输入事件描述"></textarea>
        </div>
      </div>
      <div class="flex gap-3 mt-6">
        <button onclick="closeModal('createModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="createEvent()" class="flex-1 bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg font-medium">保存</button>
      </div>
    </div>
  </div>

  <script>
    var currentFamilyId = '${selectedFamilyId || ''}';
    var currentFilter = 'all';

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
          payload: { toolName: toolName, params: params }
        }, '*');
      });
    }

    function navigateTo(page) {
      window.parent.postMessage({ type: 'navigate', page: page }, '*');
    }

    function closeModal(modalId) {
      document.getElementById(modalId).style.display = 'none';
    }

    async function loadFamilies() {
      try {
        var result = await mcpCallTool('listFamilies', {});
        var families = result.families;
        var selector = document.getElementById('familySelector');
        selector.innerHTML = '<option value="">请选择家族</option>';
        
        families.forEach(function(family) {
          var option = document.createElement('option');
          option.value = family.id;
          option.textContent = family.name;
          if (family.id === currentFamilyId) {
            option.selected = true;
          }
          selector.appendChild(option);
        });

        if (currentFamilyId) {
          await loadHistory();
        }
      } catch (error) {
        console.error('Failed to load families:', error);
      }
    }

    document.getElementById('familySelector').addEventListener('change', async function(e) {
      currentFamilyId = e.target.value;
      await loadHistory();
    });

    function filterByType(type) {
      currentFilter = type;
      document.getElementById('filterAll').className = type === 'all' ? 'px-4 py-2 bg-green-500 text-white rounded-lg text-sm font-medium' : 'px-4 py-2 bg-gray-200 text-gray-700 rounded-lg text-sm font-medium';
      document.getElementById('filterEvent').className = type === 'event' ? 'px-4 py-2 bg-green-500 text-white rounded-lg text-sm font-medium' : 'px-4 py-2 bg-gray-200 text-gray-700 rounded-lg text-sm font-medium';
      document.getElementById('filterMilestone').className = type === 'milestone' ? 'px-4 py-2 bg-green-500 text-white rounded-lg text-sm font-medium' : 'px-4 py-2 bg-gray-200 text-gray-700 rounded-lg text-sm font-medium';
      document.getElementById('filterLog').className = type === 'log' ? 'px-4 py-2 bg-green-500 text-white rounded-lg text-sm font-medium' : 'px-4 py-2 bg-gray-200 text-gray-700 rounded-lg text-sm font-medium';
      loadHistory();
    }

    async function loadHistory() {
      if (!currentFamilyId) {
        document.getElementById('timelineContent').innerHTML = '<div class="text-center py-12 text-gray-500"><svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg><p>请选择一个家族查看历史记录</p></div>';
        return;
      }

      try {
        var result = await mcpCallTool('listEvents', { familyId: currentFamilyId });
        var events = result.events || [];

        if (currentFilter !== 'all') {
          events = events.filter(function(e) { return e.type === currentFilter; });
        }

        events.sort(function(a, b) {
          return new Date(b.date) - new Date(a.date);
        });

        if (events.length === 0) {
          document.getElementById('timelineContent').innerHTML = '<div class="text-center py-12 text-gray-500">暂无记录</div>';
          return;
        }

        var html = '';
        events.forEach(function(event) {
          var dotClass = event.type === 'event' ? 'event' : (event.type === 'milestone' ? 'milestone' : 'log');
          var badgeClass = event.type === 'event' ? 'bg-green-100 text-green-700' : (event.type === 'milestone' ? 'bg-blue-100 text-blue-700' : 'bg-gray-100 text-gray-700');
          var badgeText = event.type === 'event' ? '家族事件' : (event.type === 'milestone' ? '成员大事件' : '操作日志');

          html += '<div class="timeline-item">' +
            '<div class="timeline-dot ' + dotClass + '"></div>' +
            '<div class="bg-gray-50 rounded-xl p-3">' +
            '<div class="flex flex-wrap items-center gap-2 mb-1.5">' +
            '<span class="' + badgeClass + ' text-xs px-2 py-0.5 rounded-full font-medium">' + badgeText + '</span>' +
            '<span class="text-gray-500 text-xs sm:text-sm">' + event.date + '</span>' +
            '</div>' +
            '<h4 class="font-semibold text-gray-800 text-sm mb-1">' + event.title + '</h4>' +
            '<p class="text-gray-600 text-sm leading-relaxed">' + event.description + '</p>';

          if (event.relatedMemberName) {
            html += '<div class="mt-1.5 text-xs text-gray-500">相关成员：' + event.relatedMemberName + '</div>';
          }

          if (event.operator) {
            html += '<div class="mt-1 text-xs text-gray-500">操作人：' + event.operator + '</div>';
          }

          html += '</div></div>';
        });

        document.getElementById('timelineContent').innerHTML = html;
      } catch (error) {
        console.error('Failed to load history:', error);
        document.getElementById('timelineContent').innerHTML = '<div class="text-center py-12 text-gray-500">加载历史记录失败</div>';
      }
    }

    function openCreateModal() {
      document.getElementById('eventName').value = '';
      document.getElementById('eventDate').value = '';
      document.getElementById('eventDescription').value = '';
      document.getElementById('createModal').style.display = 'flex';
    }

    async function createEvent() {
      var name = document.getElementById('eventName').value;
      var date = document.getElementById('eventDate').value;
      var description = document.getElementById('eventDescription').value;

      if (!name || !date) {
        alert('请填写事件名称和日期');
        return;
      }

      try {
        await mcpCallTool('createEvent', {
          familyId: currentFamilyId,
          type: 'event',
          title: name,
          description: description,
          date: date,
        });
        closeModal('createModal');
        await loadHistory();
      } catch (error) {
        console.error('Failed to create event:', error);
        alert('创建事件失败');
      }
    }

    document.addEventListener('DOMContentLoaded', function() {
      loadFamilies();
    });
  </script>
</body>
</html>`;
}