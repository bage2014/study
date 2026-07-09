import { User } from '../familyStore';

export function generateMemberDetailHtml(user: User | null, memberId: string): string {
  return `
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>成员详情</title>
      <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; padding: 12px; }
        .back-btn { display: flex; align-items: center; gap: 6px; color: #4CAF50; cursor: pointer; margin-bottom: 12px; font-size: 13px; }
        .back-btn:hover { text-decoration: underline; }
        .header-card { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 16px; padding: 24px; color: white; margin-bottom: 12px; }
        .avatar { width: 80px; height: 80px; border-radius: 50%; background: rgba(255,255,255,0.2); display: flex; align-items: center; justify-content: center; font-size: 32px; font-weight: bold; margin-bottom: 12px; }
        .member-name { font-size: 24px; font-weight: bold; margin-bottom: 4px; }
        .member-gender { font-size: 14px; opacity: 0.9; }
        .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-top: 16px; }
        .info-item { background: rgba(255,255,255,0.15); padding: 10px; border-radius: 8px; }
        .info-label { font-size: 12px; opacity: 0.8; margin-bottom: 4px; }
        .info-value { font-size: 14px; font-weight: 500; }
        .section-card { background: white; border-radius: 12px; padding: 16px; margin-bottom: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
        .section-title { font-size: 16px; font-weight: bold; color: #333; margin-bottom: 12px; border-bottom: 2px solid #eee; padding-bottom: 8px; }
        .relationship-list { display: grid; grid-template-columns: 1fr; gap: 8px; }
        .relationship-item { display: flex; align-items: center; gap: 12px; padding: 10px; background: #f9f9f9; border-radius: 8px; }
        .rel-avatar { width: 40px; height: 40px; border-radius: 50%; background: #ddd; display: flex; align-items: center; justify-content: center; font-size: 16px; font-weight: bold; }
        .rel-info { flex: 1; }
        .rel-name { font-size: 14px; font-weight: 500; }
        .rel-type { font-size: 12px; color: #666; }
        .action-btns { display: flex; gap: 8px; margin-top: 16px; }
        .btn { padding: 8px 16px; border: none; border-radius: 8px; font-size: 13px; cursor: pointer; }
        .btn-primary { background: #4CAF50; color: white; }
        .btn-primary:hover { background: #45a049; }
        .btn-secondary { background: #f0f0f0; color: #333; }
        .btn-secondary:hover { background: #e0e0e0; }
        .btn-danger { background: #dc3545; color: white; }
        .btn-danger:hover { background: #c82333; }
        .photo-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(100px, 1fr)); gap: 8px; }
        .photo-item { aspect-ratio: 1; border-radius: 8px; overflow: hidden; background: #eee; }
        .photo-item img { width: 100%; height: 100%; object-fit: cover; }
        .timeline-list { position: relative; padding-left: 24px; }
        .timeline-list::before { content: ''; position: absolute; left: 8px; top: 0; bottom: 0; width: 2px; background: #ddd; }
        .timeline-item { position: relative; padding: 12px; background: #f9f9f9; border-radius: 8px; margin-bottom: 8px; }
        .timeline-item::before { content: ''; position: absolute; left: -20px; top: 16px; width: 10px; height: 10px; border-radius: 50%; background: #4CAF50; border: 2px solid white; }
        .timeline-date { font-size: 12px; color: #666; margin-bottom: 4px; }
        .timeline-title { font-size: 14px; font-weight: 500; color: #333; }
        .timeline-desc { font-size: 13px; color: #555; margin-top: 4px; }
        .edit-modal { display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); justify-content: center; align-items: center; z-index: 1000; padding: 16px; }
        .edit-modal.show { display: flex; }
        .edit-content { background: white; border-radius: 12px; padding: 20px; width: 100%; max-width: 400px; max-height: 80vh; overflow-y: auto; }
        .edit-content h3 { margin-bottom: 16px; font-size: 18px; }
        .field { margin-bottom: 12px; }
        .field label { display: block; margin-bottom: 6px; font-size: 13px; color: #666; }
        .field input, .field textarea, .field select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; }
        .modal-actions { display: flex; gap: 8px; margin-top: 16px; }
        .empty-state { text-align: center; padding: 30px; color: #999; }
        .empty-state p { font-size: 14px; }
        .details-section { background: #f9f9f9; padding: 12px; border-radius: 8px; margin-top: 12px; }
        .details-label { font-size: 12px; color: #666; margin-bottom: 4px; }
        .details-content { font-size: 14px; color: #333; line-height: 1.5; }
        @media (min-width: 640px) {
          .container { padding: 20px; }
          .header-card { padding: 32px; }
          .member-name { font-size: 28px; }
          .photo-grid { grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); }
        }
      </style>
    </head>
    <body>
      <div id="appData" style="display:none" data-member-id="${memberId}" data-user-name="${user?.nickname || ''}"></div>
      
      <div class="container">
        <div class="back-btn" onclick="goBack()">
          <span>←</span> 返回
        </div>

        <div id="memberHeader" class="header-card">
          <div class="avatar" id="memberAvatar">?</div>
          <div class="member-name" id="memberName">加载中...</div>
          <div class="member-gender" id="memberGender"></div>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">出生日期</div>
              <div class="info-value" id="memberBirth"></div>
            </div>
            <div class="info-item">
              <div class="info-label">年龄</div>
              <div class="info-value" id="memberAge"></div>
            </div>
            <div class="info-item">
              <div class="info-label">手机号</div>
              <div class="info-value" id="memberPhone"></div>
            </div>
            <div class="info-item">
              <div class="info-label">邮箱</div>
              <div class="info-value" id="memberEmail"></div>
            </div>
          </div>
        </div>

        <div class="section-card">
          <div class="section-title">详细信息</div>
          <div id="memberDetails" class="details-section">
            <div class="details-label">个人简介</div>
            <div class="details-content" id="memberDetailsContent">暂无详细信息</div>
          </div>
        </div>

        <div class="section-card">
          <div class="section-title">关系网络</div>
          <div id="relationshipList" class="relationship-list">
            <div class="empty-state"><p>加载中...</p></div>
          </div>
          <div class="action-btns">
            <button class="btn btn-primary" onclick="showAddRelationForm()">添加关系</button>
          </div>
        </div>

        <div class="section-card">
          <div class="section-title">相关照片</div>
          <div id="photoList" class="photo-grid">
            <div class="empty-state"><p>加载中...</p></div>
          </div>
        </div>

        <div class="section-card">
          <div class="section-title">生平事件</div>
          <div id="eventList" class="timeline-list">
            <div class="empty-state"><p>加载中...</p></div>
          </div>
        </div>

        <div class="section-card">
          <div class="action-btns">
            <button class="btn btn-primary" onclick="showEditForm()">编辑信息</button>
            <button class="btn btn-danger" onclick="deleteMember()">删除成员</button>
          </div>
        </div>
      </div>

      <div id="editModal" class="edit-modal">
        <div class="edit-content">
          <h3>编辑成员信息</h3>
          <div class="field">
            <label>姓名</label>
            <input type="text" id="editName">
          </div>
          <div class="field">
            <label>性别</label>
            <select id="editGender">
              <option value="male">男</option>
              <option value="female">女</option>
            </select>
          </div>
          <div class="field">
            <label>出生日期</label>
            <input type="date" id="editBirth">
          </div>
          <div class="field">
            <label>去世日期</label>
            <input type="date" id="editDeath">
          </div>
          <div class="field">
            <label>手机号</label>
            <input type="tel" id="editPhone">
          </div>
          <div class="field">
            <label>邮箱</label>
            <input type="email" id="editEmail">
          </div>
          <div class="field">
            <label>详细信息</label>
            <textarea id="editDetails" rows="3"></textarea>
          </div>
          <div class="modal-actions">
            <button class="btn btn-primary" onclick="saveEdit()">保存</button>
            <button class="btn btn-secondary" onclick="hideEditForm()">取消</button>
          </div>
          <div id="editMessage" style="margin-top: 12px; font-size: 13px;"></div>
        </div>
      </div>

      <div id="addRelationModal" class="edit-modal">
        <div class="edit-content">
          <h3>添加关系</h3>
          <div class="field">
            <label>选择成员</label>
            <select id="relationMemberSelect"></select>
          </div>
          <div class="field">
            <label>关系类型</label>
            <select id="relationTypeSelect">
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
            </select>
          </div>
          <div class="modal-actions">
            <button class="btn btn-primary" onclick="saveRelation()">添加</button>
            <button class="btn btn-secondary" onclick="hideAddRelationForm()">取消</button>
          </div>
          <div id="relationMessage" style="margin-top: 12px; font-size: 13px;"></div>
        </div>
      </div>

      <script>
        var appData = document.getElementById('appData');
        var currentMemberId = appData ? appData.getAttribute('data-member-id') || '' : '';

        async function callTool(name, params) {
          const response = await fetch('/api/call', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ toolName: name, params })
          });
          return response.json();
        }

        async function loadMember() {
          const result = await callTool('getMemberById', { memberId: currentMemberId });
          const member = result.member;
          if (!member) {
            document.getElementById('memberName').textContent = '成员不存在';
            return;
          }

          document.getElementById('memberAvatar').textContent = member.name.charAt(0);
          document.getElementById('memberName').textContent = member.name;
          document.getElementById('memberGender').textContent = member.gender === 'male' ? '男' : '女';
          document.getElementById('memberBirth').textContent = member.birthDate || '未知';
          document.getElementById('memberPhone').textContent = member.phone || '未填写';
          document.getElementById('memberEmail').textContent = member.email || '未填写';
          document.getElementById('memberDetailsContent').textContent = member.details || '暂无详细信息';

          if (member.birthDate) {
            const birth = new Date(member.birthDate);
            const death = member.deathDate ? new Date(member.deathDate) : new Date();
            const age = Math.floor((death - birth) / (365.25 * 24 * 60 * 60 * 1000));
            document.getElementById('memberAge').textContent = member.deathDate ? age + '岁 (已故)' : age + '岁';
          } else {
            document.getElementById('memberAge').textContent = '未知';
          }

          document.getElementById('editName').value = member.name;
          document.getElementById('editGender').value = member.gender;
          document.getElementById('editBirth').value = member.birthDate || '';
          document.getElementById('editDeath').value = member.deathDate || '';
          document.getElementById('editPhone').value = member.phone || '';
          document.getElementById('editEmail').value = member.email || '';
          document.getElementById('editDetails').value = member.details || '';

          await loadRelationships();
          await loadPhotos();
          await loadEvents();
        }

        async function loadRelationships() {
          const memberResult = await callTool('getMemberById', { memberId: currentMemberId });
          const member = memberResult.member;
          if (!member) return;

          const result = await callTool('listRelationships', { familyId: member.familyId });
          const relationships = result.relationships || [];
          
          const memberRelations = relationships.filter(r => r.memberId1 === currentMemberId || r.memberId2 === currentMemberId);
          
          const container = document.getElementById('relationshipList');
          if (memberRelations.length === 0) {
            container.innerHTML = '<div class="empty-state"><p>暂无关系记录</p></div>';
            return;
          }

          const relationLabels = {
            'father': '父亲', 'mother': '母亲', 'husband': '丈夫', 'wife': '妻子',
            'son': '儿子', 'daughter': '女儿', 'brother': '兄弟', 'sister': '姐妹',
            'grandfather': '祖父', 'grandmother': '祖母', 'grandson': '孙子', 'granddaughter': '孙女',
            'uncle': '叔叔', 'aunt': '姑姑', 'nephew': '侄子', 'niece': '侄女', 'cousin': '堂/表兄弟姐妹'
          };

          const html = await Promise.all(memberRelations.map(async (r) => {
            const otherId = r.memberId1 === currentMemberId ? r.memberId2 : r.memberId1;
            const otherMemberResult = await callTool('getMemberById', { memberId: otherId });
            const otherMember = otherMemberResult.member;
            if (!otherMember) return '';
            
            const label = relationLabels[r.relationshipType] || r.relationshipType;
            const isReverse = r.memberId2 === currentMemberId;
            
            return `
              <div class="relationship-item">
                <div class="rel-avatar">${otherMember.name.charAt(0)}</div>
                <div class="rel-info">
                  <div class="rel-name">${otherMember.name}</div>
                  <div class="rel-type">${isReverse ? getReverseLabel(r.relationshipType) : label}</div>
                </div>
              </div>
            `;
          }));

          container.innerHTML = html.filter(h => h).join('');
        }

        function getReverseLabel(type) {
          const reverseMap = {
            'father': '儿子', 'mother': '女儿', 'husband': '妻子', 'wife': '丈夫',
            'son': '父亲', 'daughter': '母亲', 'brother': '兄弟', 'sister': '姐妹',
            'grandfather': '孙子', 'grandmother': '孙女', 'grandson': '祖父', 'granddaughter': '祖母',
            'uncle': '侄子', 'aunt': '侄女', 'nephew': '叔叔', 'niece': '姑姑', 'cousin': '堂/表兄弟姐妹'
          };
          return reverseMap[type] || type;
        }

        async function loadPhotos() {
          const memberResult = await callTool('getMemberById', { memberId: currentMemberId });
          const member = memberResult.member;
          if (!member) return;

          const families = await callTool('listFamilies', {});
          const family = families.families.find(f => f.id === member.familyId);
          if (!family) return;

          const albums = await callTool('listAlbums', { familyId: family.id });
          
          let allPhotos = [];
          for (const album of albums.albums) {
            const photos = await callTool('listPhotos', { albumId: album.id });
            allPhotos = allPhotos.concat(photos.photos);
          }

          const relatedPhotos = allPhotos.filter(p => p.relatedMemberName === member.name);
          const container = document.getElementById('photoList');

          if (relatedPhotos.length === 0) {
            container.innerHTML = '<div class="empty-state"><p>暂无相关照片</p></div>';
            return;
          }

          container.innerHTML = relatedPhotos.map(p => `
            <div class="photo-item">
              <img src="${p.url}" alt="${p.title}" />
            </div>
          `).join('');
        }

        async function loadEvents() {
          const memberResult = await callTool('getMemberById', { memberId: currentMemberId });
          const member = memberResult.member;
          if (!member) return;

          const result = await callTool('listEvents', { familyId: member.familyId });
          const events = result.events || [];
          const relatedEvents = events.filter(e => e.relatedMemberName === member.name);
          
          const container = document.getElementById('eventList');
          if (relatedEvents.length === 0) {
            container.innerHTML = '<div class="empty-state"><p>暂无相关事件</p></div>';
            return;
          }

          container.innerHTML = relatedEvents.map(e => `
            <div class="timeline-item">
              <div class="timeline-date">${e.date}</div>
              <div class="timeline-title">${e.title}</div>
              <div class="timeline-desc">${e.description}</div>
            </div>
          `).join('');
        }

        function showEditForm() {
          document.getElementById('editModal').classList.add('show');
        }

        function hideEditForm() {
          document.getElementById('editModal').classList.remove('show');
        }

        async function saveEdit() {
          const result = await callTool('updateMember', {
            memberId: currentMemberId,
            name: document.getElementById('editName').value,
            gender: document.getElementById('editGender').value,
            birthDate: document.getElementById('editBirth').value,
            deathDate: document.getElementById('editDeath').value,
            phone: document.getElementById('editPhone').value,
            email: document.getElementById('editEmail').value,
            details: document.getElementById('editDetails').value,
          });

          const msg = document.getElementById('editMessage');
          if (result.success) {
            msg.textContent = result.message;
            msg.style.color = 'green';
            hideEditForm();
            loadMember();
          } else {
            msg.textContent = result.message;
            msg.style.color = 'red';
          }
        }

        async function deleteMember() {
          if (!confirm('确定要删除该成员吗？')) return;
          const result = await callTool('deleteMember', { memberId: currentMemberId });
          if (result.success) {
            goBack();
          } else {
            alert(result.message);
          }
        }

        function showAddRelationForm() {
          document.getElementById('addRelationModal').classList.add('show');
          loadMemberOptions();
        }

        function hideAddRelationForm() {
          document.getElementById('addRelationModal').classList.remove('show');
        }

        async function loadMemberOptions() {
          const memberResult = await callTool('getMemberById', { memberId: currentMemberId });
          const member = memberResult.member;
          if (!member) return;

          const result = await callTool('listMembers', { familyId: member.familyId });
          const members = result.members || [];
          const select = document.getElementById('relationMemberSelect');
          
          select.innerHTML = members.filter(m => m.id !== currentMemberId).map(m => 
            `<option value="${m.id}">${m.name}</option>`
          ).join('');
        }

        async function saveRelation() {
          const memberId2 = document.getElementById('relationMemberSelect').value;
          const relationshipType = document.getElementById('relationTypeSelect').value;
          
          const result = await callTool('createRelationship', {
            memberId1: currentMemberId,
            memberId2: memberId2,
            relationshipType: relationshipType,
          });

          const msg = document.getElementById('relationMessage');
          if (result.success) {
            msg.textContent = result.message;
            msg.style.color = 'green';
            hideAddRelationForm();
            loadRelationships();
          } else {
            msg.textContent = result.message;
            msg.style.color = 'red';
          }
        }

        function goBack() {
          window.parent.postMessage({ type: 'navigate', uri: 'ui://family/members' }, '*');
        }

        loadMember();
      </script>
    </body>
    </html>
  `;
}
