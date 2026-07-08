import { User } from '../familyStore';

export function generateFamilyManageHtml(user: User | null, selectedFamilyId: string | undefined): string {
  const userName = user?.nickname || user?.email || '用户';

  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>家庭族谱 - 家族管理</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
  </style>
</head>
<body class="bg-gray-50 min-h-screen">
  <div id="appData" style="display:none" data-family-id="${selectedFamilyId || ''}" data-user-name="${userName}"></div>
  <header class="bg-white shadow-sm sticky top-0 z-10">
    <div class="max-w-6xl mx-auto px-4 py-4">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-4">
          <button onclick="navigateTo('home')" class="text-gray-500 hover:text-gray-700">← 返回</button>
          <h1 class="text-xl font-bold text-gray-800">家族管理</h1>
        </div>
        <div class="flex items-center gap-4">
          <span class="text-gray-600" id="userNameDisplay"></span>
        </div>
      </div>
    </div>
  </header>

  <main class="max-w-6xl mx-auto px-4 py-8">
    <div class="flex flex-col lg:flex-row gap-6">
      <div class="w-full lg:w-64 flex-shrink-0">
        <div class="bg-white rounded-xl shadow-sm p-4 sticky top-24">
          <h3 class="font-semibold text-gray-800 mb-4">家族列表</h3>
          <div id="familyList" class="space-y-2">
            <div class="text-gray-500 text-sm text-center py-4">加载中...</div>
          </div>
          <button onclick="openCreateModal()" class="w-full mt-4 bg-green-500 hover:bg-green-600 text-white py-3 px-4 rounded-lg font-medium transition-all">
            + 新建家族
          </button>
        </div>
      </div>

      <div class="flex-1 min-w-0">
        <div id="familyContent" class="bg-white rounded-xl shadow-sm p-6">
          <div class="text-center py-12 text-gray-500">
            <svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
            </svg>
            <p>请选择一个家族查看详情</p>
          </div>
        </div>
      </div>
    </div>
  </main>

  <div id="createModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-md mx-4">
      <h3 class="text-lg font-bold text-gray-800 mb-4">新建家族</h3>
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">家族名称</label>
          <input type="text" id="familyName" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none" placeholder="请输入家族名称">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">家族描述</label>
          <textarea id="familyDescription" rows="3" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none" placeholder="请输入家族描述"></textarea>
        </div>
      </div>
      <div class="flex gap-3 mt-6">
        <button onclick="closeModal('createModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="createFamily()" class="flex-1 bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg font-medium">创建</button>
      </div>
    </div>
  </div>

  <div id="editModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-md mx-4">
      <h3 class="text-lg font-bold text-gray-800 mb-4">编辑家族</h3>
      <input type="hidden" id="editFamilyId">
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">家族名称</label>
          <input type="text" id="editFamilyName" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">家族描述</label>
          <textarea id="editFamilyDescription" rows="3" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"></textarea>
        </div>
      </div>
      <div class="flex gap-3 mt-6">
        <button onclick="closeModal('editModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="updateFamily()" class="flex-1 bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg font-medium">保存</button>
      </div>
    </div>
  </div>

  <div id="confirmModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-sm mx-4">
      <h3 class="text-lg font-bold text-gray-800 mb-2">确认删除</h3>
      <p class="text-gray-500 mb-6">确定要删除这个家族吗？此操作不可撤销。</p>
      <input type="hidden" id="deleteFamilyId">
      <div class="flex gap-3">
        <button onclick="closeModal('confirmModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="deleteFamily()" class="flex-1 bg-red-500 hover:bg-red-600 text-white py-2 px-4 rounded-lg font-medium">删除</button>
      </div>
    </div>
  </div>

  <script>
    var appData = document.getElementById('appData');
    var currentFamilyId = appData ? appData.getAttribute('data-family-id') || '' : '';
    var userName = appData ? appData.getAttribute('data-user-name') || '用户' : '用户';
    
    if (document.getElementById('userNameDisplay')) {
      document.getElementById('userNameDisplay').textContent = userName;
    }

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

    function openCreateModal() {
      document.getElementById('familyName').value = '';
      document.getElementById('familyDescription').value = '';
      document.getElementById('createModal').style.display = 'flex';
    }

    async function loadFamilies() {
      try {
        var result = await mcpCallTool('listFamilies', {});
        var families = result.families;
        var listHtml = '';

        if (families.length === 0) {
          listHtml = '<div class="text-gray-500 text-sm text-center py-4">暂无家族</div>';
        } else {
          families.forEach(function(family) {
            var isSelected = family.id === currentFamilyId;
            listHtml += '<div onclick="selectFamily(&#39;' + family.id + '&#39;)" class="p-3 rounded-lg cursor-pointer border transition-all ' + (isSelected ? 'bg-green-50 border-green-500' : 'bg-gray-50 border-transparent hover:border-gray-200') + '">' +
              '<div class="font-medium text-gray-800">' + family.name + '</div>' +
              '<div class="text-xs text-gray-500">' + family.createdAt + '</div>' +
              '</div>';
          });
        }

        document.getElementById('familyList').innerHTML = listHtml;

        if (currentFamilyId && families.length > 0) {
          await loadFamilyDetails(currentFamilyId);
        }
      } catch (error) {
        console.error('Failed to load families:', error);
      }
    }

    async function selectFamily(familyId) {
      currentFamilyId = familyId;
      await loadFamilies();
    }

    async function loadFamilyDetails(familyId) {
      try {
        var familyResult = await mcpCallTool('getFamilyById', { familyId: familyId });
        var family = familyResult.family;

        var membersResult = await mcpCallTool('listMembers', { familyId: familyId });
        var members = membersResult.members;

        var relationshipsResult = await mcpCallTool('listRelationships', { familyId: familyId });
        var relationships = relationshipsResult.relationships;

        if (!family) {
          document.getElementById('familyContent').innerHTML = '<div class="text-center py-12 text-gray-500">家族不存在</div>';
          return;
        }

        var contentHtml = '<div>' +
          '<div class="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-6 gap-4">' +
          '<div>' +
          '<h2 class="text-xl sm:text-2xl font-bold text-gray-800">' + family.name + '</h2>' +
          '<p class="text-gray-500 mt-1">' + family.description + '</p>' +
          '</div>' +
          '<div class="flex gap-2">' +
          '<button onclick="openEditModal(&#39;' + family.id + '&#39;)" class="bg-blue-500 hover:bg-blue-600 text-white py-2.5 px-4 rounded-lg text-sm font-medium">编辑</button>' +
          '<button onclick="openDeleteModal(&#39;' + family.id + '&#39;)" class="bg-red-500 hover:bg-red-600 text-white py-2.5 px-4 rounded-lg text-sm font-medium">删除</button>' +
          '</div>' +
          '</div>' +
          '<div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-8">' +
          '<div class="bg-green-50 rounded-xl p-4">' +
          '<div class="text-2xl font-bold text-green-600">' + members.length + '</div>' +
          '<div class="text-gray-500 text-sm">家族成员</div>' +
          '</div>' +
          '<div class="bg-blue-50 rounded-xl p-4">' +
          '<div class="text-2xl font-bold text-blue-600">' + relationships.length + '</div>' +
          '<div class="text-gray-500 text-sm">成员关系</div>' +
          '</div>' +
          '<div class="bg-purple-50 rounded-xl p-4">' +
          '<div class="text-2xl font-bold text-purple-600">' + family.createdAt + '</div>' +
          '<div class="text-gray-500 text-sm">创建时间</div>' +
          '</div>' +
          '</div>' +
          '<div class="grid grid-cols-1 sm:grid-cols-3 gap-3 mb-6">' +
          '<button onclick="navigateToFamilyTree()" class="bg-green-500 hover:bg-green-600 text-white py-3 px-4 rounded-lg font-medium">查看家族树</button>' +
          '<button onclick="navigateToMemberManage()" class="bg-blue-500 hover:bg-blue-600 text-white py-3 px-4 rounded-lg font-medium">管理成员</button>' +
          '<button onclick="navigateToRelationship()" class="bg-orange-500 hover:bg-orange-600 text-white py-3 px-4 rounded-lg font-medium">管理关系</button>' +
          '</div>';

        if (members.length > 0) {
          contentHtml += '<div class="mt-8">' +
            '<h3 class="text-lg font-semibold text-gray-800 mb-4">家族成员</h3>' +
            '<div class="space-y-2">';

          members.forEach(function(member) {
            var genderIcon = member.gender === 'male' ? '♂' : '♀';
            var statusBadge = member.deathDate ? '<span class="text-xs bg-gray-200 text-gray-600 px-2 py-0.5 rounded">已故</span>' : '';
            contentHtml += '<div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">' +
              '<div class="flex items-center gap-3">' +
              '<div class="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center text-green-600 font-bold">' + member.name.charAt(0) + '</div>' +
              '<div>' +
              '<div class="font-medium text-gray-800">' + member.name + ' ' + genderIcon + '</div>' +
              '<div class="text-xs text-gray-500">' + (member.birthDate || '') + ' - ' + (member.deathDate || '') + '</div>' +
              '</div>' +
              '</div>' +
              '<div>' + statusBadge + '</div>' +
              '</div>';
          });

          contentHtml += '</div></div>';
        }

        document.getElementById('familyContent').innerHTML = contentHtml;
      } catch (error) {
        console.error('Failed to load family details:', error);
      }
    }

    async function createFamily() {
      var name = document.getElementById('familyName').value;
      var description = document.getElementById('familyDescription').value;

      if (!name) {
        alert('请输入家族名称');
        return;
      }

      try {
        var result = await mcpCallTool('createFamily', { name: name, description: description });
        if (result.success) {
          closeModal('createModal');
          currentFamilyId = result.family.id;
          await loadFamilies();
        } else {
          alert(result.message);
        }
      } catch (error) {
        alert('创建失败，请重试');
      }
    }

    async function openEditModal(familyId) {
      try {
        var result = await mcpCallTool('getFamilyById', { familyId: familyId });
        if (result.family) {
          document.getElementById('editFamilyId').value = result.family.id;
          document.getElementById('editFamilyName').value = result.family.name;
          document.getElementById('editFamilyDescription').value = result.family.description;
          document.getElementById('editModal').style.display = 'flex';
        }
      } catch (error) {
        console.error('Failed to load family:', error);
      }
    }

    async function updateFamily() {
      var familyId = document.getElementById('editFamilyId').value;
      var name = document.getElementById('editFamilyName').value;
      var description = document.getElementById('editFamilyDescription').value;

      if (!name) {
        alert('请输入家族名称');
        return;
      }

      try {
        var result = await mcpCallTool('updateFamily', { familyId: familyId, name: name, description: description });
        if (result.success) {
          closeModal('editModal');
          await loadFamilies();
        } else {
          alert(result.message);
        }
      } catch (error) {
        alert('更新失败，请重试');
      }
    }

    function openDeleteModal(familyId) {
      document.getElementById('deleteFamilyId').value = familyId;
      document.getElementById('confirmModal').style.display = 'flex';
    }

    async function deleteFamily() {
      var familyId = document.getElementById('deleteFamilyId').value;

      try {
        var result = await mcpCallTool('deleteFamily', { familyId: familyId });
        if (result.success) {
          closeModal('confirmModal');
          currentFamilyId = '';
          await loadFamilies();
        } else {
          alert(result.message);
        }
      } catch (error) {
        alert('删除失败，请重试');
      }
    }

    function navigateToFamilyTree() {
      window.parent.postMessage({ type: 'navigate', page: 'familyTree', familyId: currentFamilyId }, '*');
    }

    function navigateToMemberManage() {
      window.parent.postMessage({ type: 'navigate', page: 'memberManage', familyId: currentFamilyId }, '*');
    }

    function navigateToRelationship() {
      window.parent.postMessage({ type: 'navigate', page: 'relationship', familyId: currentFamilyId }, '*');
    }

    document.addEventListener('DOMContentLoaded', function() {
      loadFamilies();
    });
  </script>
</body>
</html>`;
}