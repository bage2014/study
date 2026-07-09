import { User } from '../familyStore';

export function generateMemberManageHtml(user: User | null, selectedFamilyId: string | undefined): string {
  const userName = user?.nickname || user?.email || '用户';

  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>家庭族谱 - 成员管理</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
    .member-card {
      transition: all 0.2s ease;
    }
    .member-card:hover {
      box-shadow: 0 8px 16px rgba(0,0,0,0.1);
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
          <h1 class="text-xl font-bold text-gray-800">成员管理</h1>
        </div>
        <div class="flex items-center gap-4">
          <span class="text-gray-600" id="userNameDisplay"></span>
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
        <div class="flex-1 min-w-[200px]">
          <input type="text" id="searchInput" placeholder="搜索成员姓名、手机号或邮箱..." class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <button onclick="openCreateModal()" class="bg-green-500 hover:bg-green-600 text-white py-2 px-6 rounded-lg font-medium transition-all">
          + 添加成员
        </button>
      </div>
    </div>

    <div id="memberList" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <div class="text-center py-12 text-gray-500 col-span-full">
        <svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
        </svg>
        <p>请选择一个家族查看成员列表</p>
      </div>
    </div>
  </main>

  <div id="createModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-md mx-4 max-h-[90vh] overflow-y-auto">
      <h3 class="text-lg font-bold text-gray-800 mb-4">添加成员</h3>
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">姓名 *</label>
          <input type="text" id="memberName" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none" placeholder="请输入姓名">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">性别 *</label>
          <div class="flex gap-4">
            <label class="flex items-center gap-2">
              <input type="radio" name="gender" value="male" checked class="text-green-500 focus:ring-green-500">
              <span>男</span>
            </label>
            <label class="flex items-center gap-2">
              <input type="radio" name="gender" value="female" class="text-green-500 focus:ring-green-500">
              <span>女</span>
            </label>
          </div>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">出生日期</label>
          <input type="date" id="birthDate" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">去世日期</label>
          <input type="date" id="deathDate" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">手机号</label>
          <input type="tel" id="phone" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none" placeholder="请输入手机号">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">邮箱</label>
          <input type="email" id="email" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none" placeholder="请输入邮箱">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">详细信息</label>
          <textarea id="details" rows="3" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none" placeholder="请输入详细信息"></textarea>
        </div>
      </div>
      <div class="flex gap-3 mt-6">
        <button onclick="closeModal('createModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="createMember()" class="flex-1 bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg font-medium">保存</button>
      </div>
    </div>
  </div>

  <div id="editModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-md mx-4 max-h-[90vh] overflow-y-auto">
      <h3 class="text-lg font-bold text-gray-800 mb-4">编辑成员</h3>
      <input type="hidden" id="editMemberId">
      <div class="space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">姓名 *</label>
          <input type="text" id="editMemberName" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">性别 *</label>
          <div class="flex gap-4">
            <label class="flex items-center gap-2">
              <input type="radio" name="editGender" value="male" class="text-green-500 focus:ring-green-500">
              <span>男</span>
            </label>
            <label class="flex items-center gap-2">
              <input type="radio" name="editGender" value="female" class="text-green-500 focus:ring-green-500">
              <span>女</span>
            </label>
          </div>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">出生日期</label>
          <input type="date" id="editBirthDate" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">去世日期</label>
          <input type="date" id="editDeathDate" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">手机号</label>
          <input type="tel" id="editPhone" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">邮箱</label>
          <input type="email" id="editEmail" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">详细信息</label>
          <textarea id="editDetails" rows="3" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"></textarea>
        </div>
      </div>
      <div class="flex gap-3 mt-6">
        <button onclick="closeModal('editModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="updateMember()" class="flex-1 bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg font-medium">保存</button>
      </div>
    </div>
  </div>

  <div id="confirmModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" style="display: none;">
    <div class="bg-white rounded-xl p-6 w-full max-w-sm mx-4">
      <h3 class="text-lg font-bold text-gray-800 mb-2">确认删除</h3>
      <p class="text-gray-500 mb-6">确定要删除该成员吗？此操作不可撤销。</p>
      <input type="hidden" id="deleteMemberId">
      <div class="flex gap-3">
        <button onclick="closeModal('confirmModal')" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg font-medium">取消</button>
        <button onclick="deleteMember()" class="flex-1 bg-red-500 hover:bg-red-600 text-white py-2 px-4 rounded-lg font-medium">删除</button>
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
          await loadMembers();
        }
      } catch (error) {
        console.error('Failed to load families:', error);
      }
    }

    document.getElementById('familySelector').addEventListener('change', async function(e) {
      currentFamilyId = e.target.value;
      await loadMembers();
    });

    document.getElementById('searchInput').addEventListener('input', function() {
      loadMembers();
    });

    async function loadMembers() {
      if (!currentFamilyId) {
        document.getElementById('memberList').innerHTML = '<div class="text-center py-12 text-gray-500 col-span-full"><svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path></svg><p>请选择一个家族查看成员列表</p></div>';
        return;
      }

      try {
        var keyword = document.getElementById('searchInput').value;
        var members = [];
        
        if (keyword) {
          var searchResult = await mcpCallTool('searchMembers', { keyword, familyId: currentFamilyId });
          members = searchResult.members;
        } else {
          var listResult = await mcpCallTool('listMembers', { familyId: currentFamilyId });
          members = listResult.members;
        }

        if (members.length === 0) {
          document.getElementById('memberList').innerHTML = '<div class="text-center py-12 text-gray-500 col-span-full">暂无成员</div>';
          return;
        }

        var html = '';
        members.forEach(function(member) {
          var genderIcon = member.gender === 'male' ? '♂' : '♀';
          var genderColor = member.gender === 'male' ? 'bg-blue-100 text-blue-600' : 'bg-pink-100 text-pink-600';
          var deceasedBadge = member.deathDate ? '<span class="text-xs bg-gray-200 text-gray-600 px-2 py-0.5 rounded">已故</span>' : '';
          
          html += '<div class="bg-white rounded-xl p-4 shadow-sm member-card cursor-pointer" onclick="viewMemberDetail(&#39;' + member.id + '&#39;)">' +
            '<div class="flex items-center gap-4">' +
            '<div class="w-12 h-12 ' + genderColor + ' rounded-full flex items-center justify-center text-xl font-bold flex-shrink-0">' + member.name.charAt(0) + '</div>' +
            '<div class="flex-1 min-w-0">' +
            '<div class="flex items-center gap-2">' +
            '<span class="font-semibold text-gray-800">' + member.name + '</span>' +
            '<span class="text-gray-500">' + genderIcon + '</span>' +
            deceasedBadge +
            '</div>' +
            '<div class="text-xs text-gray-500 mt-1">' +
            (member.birthDate ? member.birthDate : '') + (member.deathDate ? ' - ' + member.deathDate : '') +
            '</div>' +
            '<div class="text-xs text-gray-500 mt-0.5">' +
            (member.phone || '') + (member.phone && member.email ? ' | ' : '') + (member.email || '') +
            '</div>' +
            '</div>' +
            '<div class="flex gap-2 flex-shrink-0">' +
            '<button onclick="event.stopPropagation(); openEditModal(&#39;' + member.id + '&#39;)" class="text-blue-500 hover:text-blue-600 p-2"><svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg></button>' +
            '<button onclick="event.stopPropagation(); openDeleteModal(&#39;' + member.id + '&#39;)" class="text-red-500 hover:text-red-600 p-2"><svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg></button>' +
            '</div>' +
            '</div>' +
            (member.details ? '<div class="mt-3 pt-3 border-t border-gray-100"><p class="text-sm text-gray-600 line-clamp-2">' + member.details + '</p></div>' : '') +
            '</div>';
        });

        document.getElementById('memberList').innerHTML = html;
      } catch (error) {
        console.error('Failed to load members:', error);
      }
    }

    function viewMemberDetail(memberId) {
      window.parent.postMessage({ type: 'navigate', uri: 'ui://family/member-detail' }, '*');
      setTimeout(function() {
        mcpCallTool('getMemberDetailUI', { memberId: memberId });
      }, 100);
    }

    function openCreateModal() {
      document.getElementById('memberName').value = '';
      document.querySelector('input[name="gender"][value="male"]').checked = true;
      document.getElementById('birthDate').value = '';
      document.getElementById('deathDate').value = '';
      document.getElementById('phone').value = '';
      document.getElementById('email').value = '';
      document.getElementById('details').value = '';
      document.getElementById('createModal').style.display = 'flex';
    }

    async function createMember() {
      var name = document.getElementById('memberName').value;
      var gender = document.querySelector('input[name="gender"]:checked').value;
      var birthDate = document.getElementById('birthDate').value;
      var deathDate = document.getElementById('deathDate').value;
      var phone = document.getElementById('phone').value;
      var email = document.getElementById('email').value;
      var details = document.getElementById('details').value;

      if (!name) {
        alert('请输入姓名');
        return;
      }

      try {
        var result = await mcpCallTool('createMember', {
          familyId: currentFamilyId,
          name: name,
          gender: gender,
          birthDate: birthDate || undefined,
          deathDate: deathDate || undefined,
          phone: phone || undefined,
          email: email || undefined,
          details: details || undefined,
        });

        if (result.success) {
          closeModal('createModal');
          await loadMembers();
        } else {
          alert(result.message);
        }
      } catch (error) {
        alert('创建失败，请重试');
      }
    }

    async function openEditModal(memberId) {
      try {
        var result = await mcpCallTool('getMemberById', { memberId: memberId });
        var member = result.member;

        if (member) {
          document.getElementById('editMemberId').value = member.id;
          document.getElementById('editMemberName').value = member.name;
          document.querySelector('input[name="editGender"][value="' + member.gender + '"]').checked = true;
          document.getElementById('editBirthDate').value = member.birthDate || '';
          document.getElementById('editDeathDate').value = member.deathDate || '';
          document.getElementById('editPhone').value = member.phone || '';
          document.getElementById('editEmail').value = member.email || '';
          document.getElementById('editDetails').value = member.details || '';
          document.getElementById('editModal').style.display = 'flex';
        }
      } catch (error) {
        console.error('Failed to load member:', error);
      }
    }

    async function updateMember() {
      var memberId = document.getElementById('editMemberId').value;
      var name = document.getElementById('editMemberName').value;
      var gender = document.querySelector('input[name="editGender"]:checked').value;
      var birthDate = document.getElementById('editBirthDate').value;
      var deathDate = document.getElementById('editDeathDate').value;
      var phone = document.getElementById('editPhone').value;
      var email = document.getElementById('editEmail').value;
      var details = document.getElementById('editDetails').value;

      if (!name) {
        alert('请输入姓名');
        return;
      }

      try {
        var result = await mcpCallTool('updateMember', {
          memberId: memberId,
          name: name,
          gender: gender,
          birthDate: birthDate || undefined,
          deathDate: deathDate || undefined,
          phone: phone || undefined,
          email: email || undefined,
          details: details || undefined,
        });

        if (result.success) {
          closeModal('editModal');
          await loadMembers();
        } else {
          alert(result.message);
        }
      } catch (error) {
        alert('更新失败，请重试');
      }
    }

    function openDeleteModal(memberId) {
      document.getElementById('deleteMemberId').value = memberId;
      document.getElementById('confirmModal').style.display = 'flex';
    }

    async function deleteMember() {
      var memberId = document.getElementById('deleteMemberId').value;

      try {
        var result = await mcpCallTool('deleteMember', { memberId: memberId });

        if (result.success) {
          closeModal('confirmModal');
          await loadMembers();
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