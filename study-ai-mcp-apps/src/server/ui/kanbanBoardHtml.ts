export const kanbanBoardHtml = `
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Kanban Board</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }
    .card-shadow { box-shadow: 0 1px 3px rgba(0,0,0,0.1), 0 1px 2px rgba(0,0,0,0.06); }
    .card-shadow-hover:hover { box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1), 0 4px 6px -2px rgba(0,0,0,0.05); }
    .dragging { opacity: 0.5; }
    .drag-over { border-color: #6366f1 !important; background-color: rgba(99, 102, 241, 0.1) !important; }
    .modal-overlay { background-color: rgba(0,0,0,0.5); backdrop-filter: blur(4px); }
  </style>
</head>
<body class="bg-gray-100 min-h-screen p-4">
  <div class="max-w-7xl mx-auto">
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-2xl font-bold text-gray-800">项目看板</h1>
      <button onclick="openAddModal()" class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors flex items-center gap-2">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
        </svg>
        添加卡片
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4" id="kanbanBoard">
    </div>
  </div>

  <div id="addModal" class="fixed inset-0 modal-overlay hidden flex items-center justify-center z-50">
    <div class="bg-white rounded-xl shadow-xl p-6 w-full max-w-md mx-4">
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-xl font-semibold text-gray-800">新建卡片</h2>
        <button onclick="closeAddModal()" class="text-gray-400 hover:text-gray-600">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>
      </div>
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">标题</label>
          <input type="text" id="newCardTitle" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500" placeholder="输入卡片标题">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
          <textarea id="newCardDesc" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500" rows="3" placeholder="输入卡片描述"></textarea>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">优先级</label>
          <select id="newCardPriority" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500">
            <option value="low">低</option>
            <option value="medium" selected>中</option>
            <option value="high">高</option>
            <option value="urgent">紧急</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">负责人</label>
          <input type="text" id="newCardAssignee" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500" placeholder="输入负责人">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">标签</label>
          <input type="text" id="newCardTags" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500" placeholder="用逗号分隔多个标签">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">截止日期</label>
          <input type="date" id="newCardDueDate" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
      </div>
      <div class="flex gap-3 mt-6">
        <button onclick="closeAddModal()" class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors">取消</button>
        <button onclick="createCard()" class="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors">创建</button>
      </div>
    </div>
  </div>

  <div id="editModal" class="fixed inset-0 modal-overlay hidden flex items-center justify-center z-50">
    <div class="bg-white rounded-xl shadow-xl p-6 w-full max-w-md mx-4">
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-xl font-semibold text-gray-800">编辑卡片</h2>
        <button onclick="closeEditModal()" class="text-gray-400 hover:text-gray-600">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>
      </div>
      <div class="space-y-4">
        <input type="hidden" id="editCardId">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">标题</label>
          <input type="text" id="editCardTitle" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
          <textarea id="editCardDesc" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500" rows="3"></textarea>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">优先级</label>
          <select id="editCardPriority" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500">
            <option value="low">低</option>
            <option value="medium">中</option>
            <option value="high">高</option>
            <option value="urgent">紧急</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">负责人</label>
          <input type="text" id="editCardAssignee" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">标签</label>
          <input type="text" id="editCardTags" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500" placeholder="用逗号分隔多个标签">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">截止日期</label>
          <input type="date" id="editCardDueDate" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500">
        </div>
      </div>
      <div class="flex gap-3 mt-6">
        <button onclick="deleteCard()" class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors">删除</button>
        <button onclick="closeEditModal()" class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors">取消</button>
        <button onclick="updateCard()" class="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors">保存</button>
      </div>
    </div>
  </div>

  <script>
    var columns = [
      { id: 'col-backlog', title: '待办', status: 'backlog', color: '#9ca3af' },
      { id: 'col-progress', title: '进行中', status: 'progress', color: '#3b82f6' },
      { id: 'col-review', title: '审核中', status: 'review', color: '#f59e0b' },
      { id: 'col-done', title: '已完成', status: 'done', color: '#10b981' },
    ];
    var cards = [];
    var draggedCardId = null;

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
          payload: {
            toolName: toolName,
            params: params
          }
        }, '*');
      });
    }

    function getPriorityColor(priority) {
      switch(priority) {
        case 'urgent': return 'bg-red-100 text-red-700';
        case 'high': return 'bg-orange-100 text-orange-700';
        case 'medium': return 'bg-yellow-100 text-yellow-700';
        case 'low': return 'bg-green-100 text-green-700';
        default: return 'bg-gray-100 text-gray-700';
      }
    }

    function getPriorityLabel(priority) {
      switch(priority) {
        case 'urgent': return '紧急';
        case 'high': return '高';
        case 'medium': return '中';
        case 'low': return '低';
        default: return priority;
      }
    }

    function formatDate(dateStr) {
      if (!dateStr) return '';
      var date = new Date(dateStr);
      return date.toLocaleDateString('zh-CN');
    }

    function renderCard(card) {
      var tagsHtml = card.tags.map(function(tag) {
        return '<span class="inline-block px-2 py-0.5 bg-gray-100 text-gray-600 text-xs rounded-full mr-1">' + tag + '</span>';
      }).join('');

      return '<div class="bg-white rounded-lg p-3 card-shadow card-shadow-hover cursor-move transition-all duration-200 border border-gray-100"' +
        ' draggable="true" data-card-id="' + card.id + '" data-card-status="' + card.status + '"' +
        ' ondragstart="handleDragStart(event)" ondragend="handleDragEnd(event)">' +
        '<div class="flex items-start justify-between mb-2">' +
        '<h4 class="font-medium text-gray-800 text-sm flex-1 pr-2">' + card.title + '</h4>' +
        '<button onclick="openEditModal(\\'' + card.id + '\\')" class="text-gray-400 hover:text-gray-600 p-1">' +
        '<svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
        '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>' +
        '</svg></button></div>' +
        (card.description ? '<p class="text-gray-500 text-xs mb-2 line-clamp-2">' + card.description + '</p>' : '') +
        '<div class="flex items-center justify-between text-xs">' +
        '<span class="' + getPriorityColor(card.priority) + ' px-2 py-0.5 rounded text-xs font-medium">' + getPriorityLabel(card.priority) + '</span>' +
        (card.assignee ? '<span class="text-gray-400">' + card.assignee + '</span>' : '') +
        (card.dueDate ? '<span class="text-gray-400">' + formatDate(card.dueDate) + '</span>' : '') +
        '</div>' +
        (tagsHtml ? '<div class="mt-2">' + tagsHtml + '</div>' : '') +
        '</div>';
    }

    function renderBoard() {
      var board = document.getElementById('kanbanBoard');
      board.innerHTML = columns.map(function(col) {
        var colCards = cards.filter(function(c) { return c.status === col.status; });
        return '<div class="bg-gray-50 rounded-xl p-3 border-2 border-transparent transition-colors"' +
          ' ondragenter="handleDragEnter(event)" ondragleave="handleDragLeave(event)"' +
          ' ondragover="handleDragOver(event)" ondrop="handleDrop(event)"' +
          ' data-column-status="' + col.status + '">' +
          '<div class="flex items-center gap-2 mb-3">' +
          '<div class="w-3 h-3 rounded-full" style="background-color: ' + col.color + '"></div>' +
          '<h3 class="font-semibold text-gray-700">' + col.title + '</h3>' +
          '<span class="bg-white text-gray-500 text-xs px-2 py-0.5 rounded-full">' + colCards.length + '</span>' +
          '</div>' +
          '<div class="space-y-2 min-h-[200px]" id="col-' + col.status + '">' +
          colCards.map(renderCard).join('') +
          '</div>' +
          '</div>';
      }).join('');
    }

    async function loadBoard() {
      try {
        var result = await mcpCallTool('getBoard', {});
        if (result && result.content) {
          var textContent = result.content.find(function(c) { return c.type === 'text'; });
          if (textContent && textContent.text) {
            var board = JSON.parse(textContent.text);
            cards = board.cards || [];
            renderBoard();
          }
        }
      } catch (error) {
        console.error('Failed to load board:', error);
      }
    }

    function handleDragStart(event) {
      draggedCardId = event.target.getAttribute('data-card-id');
      event.target.classList.add('dragging');
      event.dataTransfer.effectAllowed = 'move';
    }

    function handleDragEnd(event) {
      event.target.classList.remove('dragging');
      draggedCardId = null;
      document.querySelectorAll('[data-column-status]').forEach(function(col) {
        col.classList.remove('drag-over');
      });
    }

    function handleDragEnter(event) {
      if (draggedCardId) {
        event.target.closest('[data-column-status]').classList.add('drag-over');
      }
    }

    function handleDragLeave(event) {
      var col = event.target.closest('[data-column-status]');
      if (col) {
        col.classList.remove('drag-over');
      }
    }

    function handleDragOver(event) {
      event.preventDefault();
      event.dataTransfer.dropEffect = 'move';
    }

    async function handleDrop(event) {
      event.preventDefault();
      var col = event.target.closest('[data-column-status]');
      if (col && draggedCardId) {
        var newStatus = col.getAttribute('data-column-status');
        try {
          await mcpCallTool('moveCard', { cardId: draggedCardId, newStatus: newStatus });
          await loadBoard();
        } catch (error) {
          console.error('Failed to move card:', error);
        }
      }
      col.classList.remove('drag-over');
    }

    function openAddModal() {
      document.getElementById('addModal').classList.remove('hidden');
      document.getElementById('newCardTitle').focus();
    }

    function closeAddModal() {
      document.getElementById('addModal').classList.add('hidden');
      document.getElementById('newCardTitle').value = '';
      document.getElementById('newCardDesc').value = '';
      document.getElementById('newCardPriority').value = 'medium';
      document.getElementById('newCardAssignee').value = '';
      document.getElementById('newCardTags').value = '';
      document.getElementById('newCardDueDate').value = '';
    }

    async function createCard() {
      var title = document.getElementById('newCardTitle').value.trim();
      if (!title) return;

      var tagsInput = document.getElementById('newCardTags').value.trim();
      var tags = tagsInput ? tagsInput.split(',').map(function(t) { return t.trim(); }) : [];

      try {
        await mcpCallTool('createCard', {
          title: title,
          description: document.getElementById('newCardDesc').value.trim(),
          status: 'backlog',
          priority: document.getElementById('newCardPriority').value,
          assignee: document.getElementById('newCardAssignee').value.trim(),
          tags: tags,
          dueDate: document.getElementById('newCardDueDate').value,
        });
        closeAddModal();
        await loadBoard();
      } catch (error) {
        console.error('Failed to create card:', error);
      }
    }

    async function openEditModal(cardId) {
      var card = cards.find(function(c) { return c.id === cardId; });
      if (!card) return;

      document.getElementById('editCardId').value = card.id;
      document.getElementById('editCardTitle').value = card.title;
      document.getElementById('editCardDesc').value = card.description || '';
      document.getElementById('editCardPriority').value = card.priority;
      document.getElementById('editCardAssignee').value = card.assignee || '';
      document.getElementById('editCardTags').value = card.tags ? card.tags.join(', ') : '';
      document.getElementById('editCardDueDate').value = card.dueDate ? new Date(card.dueDate).toISOString().split('T')[0] : '';

      document.getElementById('editModal').classList.remove('hidden');
    }

    function closeEditModal() {
      document.getElementById('editModal').classList.add('hidden');
    }

    async function updateCard() {
      var cardId = document.getElementById('editCardId').value;
      var title = document.getElementById('editCardTitle').value.trim();
      if (!title || !cardId) return;

      var tagsInput = document.getElementById('editCardTags').value.trim();
      var tags = tagsInput ? tagsInput.split(',').map(function(t) { return t.trim(); }) : [];

      try {
        await mcpCallTool('updateCard', {
          cardId: cardId,
          title: title,
          description: document.getElementById('editCardDesc').value.trim(),
          priority: document.getElementById('editCardPriority').value,
          assignee: document.getElementById('editCardAssignee').value.trim(),
          tags: tags,
          dueDate: document.getElementById('editCardDueDate').value,
        });
        closeEditModal();
        await loadBoard();
      } catch (error) {
        console.error('Failed to update card:', error);
      }
    }

    async function deleteCard() {
      var cardId = document.getElementById('editCardId').value;
      if (!cardId) return;

      if (!confirm('确定要删除这个卡片吗？')) return;

      try {
        await mcpCallTool('deleteCard', { cardId: cardId });
        closeEditModal();
        await loadBoard();
      } catch (error) {
        console.error('Failed to delete card:', error);
      }
    }

    document.getElementById('addModal').addEventListener('click', function(e) {
      if (e.target === this) closeAddModal();
    });

    document.getElementById('editModal').addEventListener('click', function(e) {
      if (e.target === this) closeEditModal();
    });

    loadBoard();
  </script>
</body>
</html>
`;