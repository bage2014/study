import { User } from '../familyStore';

export function generateRelationshipHtml(user: User | null, selectedFamilyId: string | undefined): string {
  const userName = user?.nickname || user?.email || '用户';

  const relationshipLabels: Record<string, string> = {
    father: '父亲',
    mother: '母亲',
    husband: '丈夫',
    wife: '妻子',
    son: '儿子',
    daughter: '女儿',
    brother: '兄弟',
    sister: '姐妹',
    grandfather: '祖父',
    grandmother: '祖母',
    grandson: '孙子',
    granddaughter: '孙女',
    uncle: '叔叔',
    aunt: '姑姑',
    nephew: '侄子',
    niece: '侄女',
    cousin: '堂/表兄弟姐妹',
  };

  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>家庭族谱 - 关系管理</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
  </style>
</head>
<body class="bg-gray-50 min-h-screen" data-family-id="${selectedFamilyId || ''}" data-labels='${JSON.stringify(relationshipLabels)}'>
  <header class="bg-white shadow-sm sticky top-0 z-10">
    <div class="max-w-6xl mx-auto px-4 py-4">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-4">
          <button onclick="navigateTo('home')" class="text-gray-500 hover:text-gray-700">← 返回</button>
          <h1 class="text-xl font-bold text-gray-800">关系管理</h1>
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
        <button onclick="openCreateModal()" class="bg-green-500 hover:bg-green-600 text-white py-2 px-6 rounded-lg font-medium transition-all">
          + 添加关系
        </button>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <div id="relationshipList" class="bg-white rounded-xl shadow-sm p-6">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">关系列表</h3>
        <div id="relationshipItems" class="space-y-3">
          <div class="text-center py-8 text-gray-500">
            <svg class="w-12 h-12 mx-auto mb-3 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"></path>
            </svg>
            <p>请选择一个家族查看关系列表</p>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-sm p-6">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">关系类型说明</h3>
        <div class="space-y-2">
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">father</span>
            <span class="font-medium text-green-700">父亲</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">mother</span>
            <span class="font-medium text-green-700">母亲</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">husband</span>
            <span class="font-medium text-green-700">丈夫</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">wife</span>
            <span class="font-medium text-green-700">妻子</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">son</span>
            <span class="font-medium text-green-700">儿子</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">daughter</span>
            <span class="font-medium text-green-700">女儿</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">brother</span>
            <span class="font-medium text-green-700">兄弟</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">sister</span>
            <span class="font-medium text-green-700">姐妹</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">grandfather</span>
            <span class="font-medium text-green-700">祖父</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">grandmother</span>
            <span class="font-medium text-green-700">祖母</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">grandson</span>
            <span class="font-medium text-green-700">孙子</span>
          </div>
          <div class="flex justify-between items-center p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">granddaughter</span>
            <span class="font-medium text-green-700">孙女</span>
          </div>
        </div>
      </div>
    </div>
  </main>

  <div id="createModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-md mx-4">
      <h3 class="text-lg font-bold text-gray-800 mb-4">添加关系</h3>
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">成员1 *</label>
          <select id="member1" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
            <option value="">请选择成员1</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">关系类型 *</label>
          <select id="relationshipType" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
            <option value="">请选择关系类型</option>
            <option value="father">父亲</option>
            <option value="mother">母亲</option>
            <option value="husband">丈夫</option>
            <option value="wife">妻子</option>
            <option value="son">儿子</option>
            <option value="daughter">女儿</option>
            <option value="brother">兄弟</option>
            <option value="sister">姐妹</option>
            <option value="grandfather">祖父</option>
            <option value="grandmother">祖母</option>
            <option value="grandson">孙子</option>
            <option value="granddaughter">孙女</option>
            <option value="uncle">叔叔</option>
            <option value="aunt">姑姑</option>
            <option value="nephew">侄子</option>
            <option value="niece">侄女</option>
            <option value="cousin">堂/表兄弟姐妹</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">成员2 *</label>
          <select id="member2" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
            <option value="">请选择成员2</option>
          </select>
        </div>
      </div>
      <div class="flex gap-3 mt-6">
        <button onclick="closeModal('createModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="createRelationship()" class="flex-1 bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg font-medium">保存</button>
      </div>
    </div>
  </div>

  <div id="confirmModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-sm mx-4">
      <h3 class="text-lg font-bold text-gray-800 mb-2">确认删除</h3>
      <p class="text-gray-500 mb-6">确定要删除该关系吗？此操作不可撤销。</p>
      <input type="hidden" id="deleteRelationshipId">
      <div class="flex gap-3">
        <button onclick="closeModal('confirmModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="deleteRelationship()" class="flex-1 bg-red-500 hover:bg-red-600 text-white py-2 px-4 rounded-lg font-medium">删除</button>
      </div>
    </div>
  </div>

  <script>
    var currentFamilyId = document.body.getAttribute('data-family-id') || '';
    var relationshipLabels = JSON.parse(document.body.getAttribute('data-labels') || '{}');
    var memberMap = {};

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
          await loadRelationships();
        }
      } catch (error) {
        console.error('Failed to load families:', error);
      }
    }

    document.getElementById('familySelector').addEventListener('change', async function(e) {
      currentFamilyId = e.target.value;
      await loadRelationships();
    });

    async function loadRelationships() {
      if (!currentFamilyId) {
        document.getElementById('relationshipItems').innerHTML = '<div class="text-center py-8 text-gray-500"><svg class="w-12 h-12 mx-auto mb-3 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"></path></svg><p>请选择一个家族查看关系列表</p></div>';
        return;
      }

      try {
        var membersResult = await mcpCallTool('listMembers', { familyId: currentFamilyId });
        var members = membersResult.members;
        memberMap = {};
        members.forEach(function(m) {
          memberMap[m.id] = m;
        });

        var relationshipsResult = await mcpCallTool('listRelationships', { familyId: currentFamilyId });
        var relationships = relationshipsResult.relationships;

        if (relationships.length === 0) {
          document.getElementById('relationshipItems').innerHTML = '<div class="text-center py-8 text-gray-500">暂无关系</div>';
          return;
        }

        var html = '';
        relationships.forEach(function(rel) {
          var member1 = memberMap[rel.memberId1];
          var member2 = memberMap[rel.memberId2];
          var label = relationshipLabels[rel.relationshipType] || rel.relationshipType;

          html += '<div class="flex items-center justify-between p-4 bg-gray-50 rounded-xl">' +
            '<div class="flex items-center gap-4">' +
            '<div class="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center text-green-600 font-bold">' + (member1 ? member1.name.charAt(0) : '?') + '</div>' +
            '<div>' +
            '<div class="font-medium text-gray-800">' + (member1 ? member1.name : '未知成员') + '</div>' +
            '</div>' +
            '<div class="flex flex-col items-center px-4">' +
            '<div class="text-green-600 font-semibold">' + label + '</div>' +
            '<svg class="w-4 h-4 text-gray-400 mt-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"></path></svg>' +
            '</div>' +
            '<div class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 font-bold">' + (member2 ? member2.name.charAt(0) : '?') + '</div>' +
            '<div>' +
            '<div class="font-medium text-gray-800">' + (member2 ? member2.name : '未知成员') + '</div>' +
            '</div>' +
            '</div>' +
            '<button onclick="openDeleteModal(\'' + rel.id + '\')" class="text-red-500 hover:text-red-600 p-2"><svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg></button>' +
            '</div>';
        });

        document.getElementById('relationshipItems').innerHTML = html;
      } catch (error) {
        console.error('Failed to load relationships:', error);
      }
    }

    function openCreateModal() {
      loadMemberOptions();
      document.getElementById('member1').value = '';
      document.getElementById('relationshipType').value = '';
      document.getElementById('member2').value = '';
      document.getElementById('createModal').style.display = 'flex';
    }

    async function loadMemberOptions() {
      if (!currentFamilyId) return;

      try {
        var result = await mcpCallTool('listMembers', { familyId: currentFamilyId });
        var members = result.members;

        var options = '<option value="">请选择成员</option>';
        members.forEach(function(m) {
          options += '<option value="' + m.id + '">' + m.name + ' (' + (m.gender === 'male' ? '男' : '女') + ')</option>';
        });

        document.getElementById('member1').innerHTML = options;
        document.getElementById('member2').innerHTML = options;
      } catch (error) {
        console.error('Failed to load members:', error);
      }
    }

    async function createRelationship() {
      var memberId1 = document.getElementById('member1').value;
      var memberId2 = document.getElementById('member2').value;
      var relationshipType = document.getElementById('relationshipType').value;

      if (!memberId1 || !memberId2 || !relationshipType) {
        alert('请填写完整信息');
        return;
      }

      if (memberId1 === memberId2) {
        alert('成员1和成员2不能是同一个人');
        return;
      }

      try {
        var result = await mcpCallTool('createRelationship', { memberId1: memberId1, memberId2: memberId2, relationshipType: relationshipType });

        if (result.success) {
          closeModal('createModal');
          await loadRelationships();
        } else {
          alert(result.message);
        }
      } catch (error) {
        alert('创建失败，请重试');
      }
    }

    function openDeleteModal(relationshipId) {
      document.getElementById('deleteRelationshipId').value = relationshipId;
      document.getElementById('confirmModal').style.display = 'flex';
    }

    async function deleteRelationship() {
      var relationshipId = document.getElementById('deleteRelationshipId').value;

      try {
        var result = await mcpCallTool('deleteRelationship', { relationshipId: relationshipId });

        if (result.success) {
          closeModal('confirmModal');
          await loadRelationships();
        } else {
          alert(result.message);
        }
      } catch (error) {
        alert('删除失败，请重试');
      }
    }

    document.addEventListener('DOMContentLoaded', function() {
      loadFamilies();
    });
  </script>
</body>
</html>`;
}