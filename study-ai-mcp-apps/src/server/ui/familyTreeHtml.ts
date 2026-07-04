import { User } from '../familyStore';

export function generateFamilyTreeHtml(user: User | null, selectedFamilyId: string | undefined): string {
  const userName = user?.nickname || user?.email || '用户';

  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>家庭族谱 - 家族树</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
    .tree-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      overflow-x: auto;
    }
    .tree-row {
      display: flex;
      justify-content: center;
      gap: 20px;
      margin-bottom: 40px;
    }
    .tree-node {
      width: 120px;
      text-align: center;
      position: relative;
    }
    .node-card {
      background: white;
      border: 2px solid #10B981;
      border-radius: 12px;
      padding: 12px 8px;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      cursor: pointer;
      transition: all 0.3s ease;
    }
    .node-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 12px rgba(0,0,0,0.15);
      border-color: #059669;
    }
    .node-card.male {
      border-color: #3B82F6;
    }
    .node-card.female {
      border-color: #EC4899;
    }
    .node-card.deceased {
      opacity: 0.6;
      background: #f3f4f6;
    }
    .node-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin: 0 auto 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      font-size: 16px;
    }
    .node-avatar.male {
      background: #DBEAFE;
      color: #1D4ED8;
    }
    .node-avatar.female {
      background: #FCE7F3;
      color: #BE185D;
    }
    .node-name {
      font-weight: 600;
      color: #1F2937;
      font-size: 13px;
    }
    .node-info {
      font-size: 10px;
      color: #6B7280;
      margin-top: 4px;
    }
    .spouse-row {
      display: flex;
      gap: 8px;
      align-items: center;
    }
    .spouse-line {
      width: 20px;
      height: 2px;
      background: #9CA3AF;
    }
    .generation-label {
      text-align: center;
      font-weight: 600;
      color: #374151;
      margin-bottom: 16px;
      font-size: 14px;
    }
    .relation-line {
      position: absolute;
      top: -20px;
      left: 50%;
      width: 2px;
      height: 20px;
      background: #9CA3AF;
      transform: translateX(-50%);
    }
    .relation-label {
      position: absolute;
      top: -35px;
      left: 50%;
      transform: translateX(-50%);
      font-size: 10px;
      background: #E5E7EB;
      padding: 2px 6px;
      border-radius: 4px;
      white-space: nowrap;
      color: #374151;
    }
  </style>
</head>
<body class="bg-gray-50 min-h-screen">
  <header class="bg-white shadow-sm sticky top-0 z-10">
    <div class="max-w-6xl mx-auto px-4 py-4">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-4">
          <button onclick="navigateTo('home')" class="text-gray-500 hover:text-gray-700">← 返回</button>
          <h1 class="text-xl font-bold text-gray-800">家族树</h1>
        </div>
        <div class="flex items-center gap-4">
          <span class="text-gray-600">${userName}</span>
        </div>
      </div>
    </div>
  </header>

  <main class="max-w-6xl mx-auto px-4 py-8">
    <div class="bg-white rounded-xl shadow-sm p-4 mb-6">
      <div class="flex items-center gap-4">
        <label class="font-medium text-gray-700">选择家族：</label>
        <select id="familySelector" class="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
          <option value="">请选择家族</option>
        </select>
      </div>
    </div>

    <div id="treeContent" class="bg-white rounded-xl shadow-sm p-8">
      <div class="text-center py-12 text-gray-500">
        <svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
        </svg>
        <p>请选择一个家族查看家族树</p>
      </div>
    </div>
  </main>

  <script>
    var currentFamilyId = '${selectedFamilyId || ''}';

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
          await renderFamilyTree(currentFamilyId);
        }
      } catch (error) {
        console.error('Failed to load families:', error);
      }
    }

    document.getElementById('familySelector').addEventListener('change', async function(e) {
      currentFamilyId = e.target.value;
      if (currentFamilyId) {
        await renderFamilyTree(currentFamilyId);
      } else {
        document.getElementById('treeContent').innerHTML = '<div class="text-center py-12 text-gray-500"><svg class="w-16 h-16 mx-auto mb-4 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path></svg><p>请选择一个家族查看家族树</p></div>';
      }
    });

    async function renderFamilyTree(familyId) {
      try {
        var membersResult = await mcpCallTool('listMembers', { familyId });
        var members = membersResult.members;
        
        var relationshipsResult = await mcpCallTool('listRelationships', { familyId });
        var relationships = relationshipsResult.relationships;

        if (members.length === 0) {
          document.getElementById('treeContent').innerHTML = '<div class="text-center py-12 text-gray-500">该家族暂无成员</div>';
          return;
        }

        var generationMap = buildGenerationMap(members, relationships);
        var treeHtml = '<div class="tree-container">';
        
        generationMap.forEach(function(memberGroups, generation) {
          treeHtml += '<div class="generation-label">第' + generation + '代</div>';
          treeHtml += '<div class="tree-row">';
          
          memberGroups.forEach(function(group) {
            if (group.length === 2) {
              treeHtml += '<div class="spouse-row">';
              treeHtml += createNodeHtml(group[0], relationships);
              treeHtml += '<div class="spouse-line"></div>';
              treeHtml += createNodeHtml(group[1], relationships);
              treeHtml += '</div>';
            } else {
              treeHtml += createNodeHtml(group[0], relationships);
            }
          });
          
          treeHtml += '</div>';
        });

        treeHtml += '</div>';
        document.getElementById('treeContent').innerHTML = treeHtml;
      } catch (error) {
        console.error('Failed to render family tree:', error);
      }
    }

    function buildGenerationMap(members, relationships) {
      var generationMap = new Map();
      var memberMap = new Map();
      
      members.forEach(function(m) {
        memberMap.set(m.id, m);
      });

      var rootMembers = members.filter(function(m) {
        return !relationships.some(function(r) {
          return r.memberId2 === m.id && (r.relationshipType === 'son' || r.relationshipType === 'daughter');
        });
      });

      function assignGeneration(memberId, generation) {
        var existingGen = generationMap.get(generation) || [];
        
        var existingGroup = existingGen.find(function(g) {
          return g.some(function(m) { return m.id === memberId; });
        });

        if (!existingGroup) {
          var spouseId = findSpouse(memberId, relationships);
          if (spouseId && memberMap.has(spouseId)) {
            existingGen.push([memberMap.get(memberId), memberMap.get(spouseId)]);
          } else {
            existingGen.push([memberMap.get(memberId)]);
          }
          generationMap.set(generation, existingGen);
        }

        var children = findChildren(memberId, relationships, memberMap);
        children.forEach(function(child) {
          assignGeneration(child.id, generation + 1);
        });
      }

      rootMembers.forEach(function(member) {
        assignGeneration(member.id, 1);
      });

      var sortedMap = new Map([...generationMap.entries()].sort(function(a, b) {
        return a[0] - b[0];
      }));

      return sortedMap;
    }

    function findSpouse(memberId, relationships) {
      var spouseRel = relationships.find(function(r) {
        return (r.memberId1 === memberId || r.memberId2 === memberId) &&
               (r.relationshipType === 'husband' || r.relationshipType === 'wife');
      });
      
      if (spouseRel) {
        return spouseRel.memberId1 === memberId ? spouseRel.memberId2 : spouseRel.memberId1;
      }
      return null;
    }

    function findChildren(memberId, relationships, memberMap) {
      var children = [];
      relationships.forEach(function(r) {
        if (r.memberId1 === memberId && (r.relationshipType === 'son' || r.relationshipType === 'daughter')) {
          if (memberMap.has(r.memberId2)) {
            children.push(memberMap.get(r.memberId2));
          }
        }
      });
      return children;
    }

    function createNodeHtml(member, relationships) {
      if (!member) return '';
      
      var genderClass = member.gender === 'male' ? 'male' : 'female';
      var deceasedClass = member.deathDate ? 'deceased' : '';
      var avatarColor = member.gender === 'male' ? 'bg-blue-100 text-blue-600' : 'bg-pink-100 text-pink-600';
      
      var birthYear = member.birthDate ? member.birthDate.split('-')[0] : '';
      var deathYear = member.deathDate ? member.deathDate.split('-')[0] : '';
      var yearStr = birthYear && deathYear ? birthYear + '-' + deathYear : birthYear || '';

      return '<div class="tree-node">' +
        '<div class="node-card ' + genderClass + ' ' + deceasedClass + '" onclick="showMemberDetail(\'' + member.id + '\')">' +
        '<div class="node-avatar ' + genderClass + '">' + member.name.charAt(0) + '</div>' +
        '<div class="node-name">' + member.name + '</div>' +
        '<div class="node-info">' + yearStr + '</div>' +
        '</div>' +
        '</div>';
    }

    function showMemberDetail(memberId) {
      mcpCallTool('getMemberById', { memberId }).then(function(result) {
        var member = result.member;
        if (member) {
          alert('成员详情:\n姓名: ' + member.name + '\n性别: ' + (member.gender === 'male' ? '男' : '女') + '\n出生日期: ' + (member.birthDate || '未知') + '\n去世日期: ' + (member.deathDate || '健在') + '\n手机号: ' + (member.phone || '未填写') + '\n邮箱: ' + (member.email || '未填写') + '\n备注: ' + (member.details || '未填写'));
        }
      });
    }

    document.addEventListener('DOMContentLoaded', function() {
      loadFamilies();
    });
  </script>
</body>
</html>`;
}