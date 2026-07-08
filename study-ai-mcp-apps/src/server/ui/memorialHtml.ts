import { User } from '../familyStore';

export function generateMemorialHtml(user: User | null, familyId?: string): string {
  return `
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>纪念堂</title>
      <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; }
        .container { max-width: 1000px; margin: 0 auto; padding: 12px; }
        h1 { color: #333; margin-bottom: 12px; font-size: 18px; }
        .btn { background: #4CAF50; color: white; border: none; padding: 8px 14px; border-radius: 8px; font-size: 13px; cursor: pointer; }
        .btn:hover { background: #45a049; }
        .btn-secondary { background: #f0f0f0; color: #333; }
        .btn-secondary:hover { background: #e0e0e0; }
        .memorial-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 12px; margin-top: 16px; }
        .memorial-card { background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        .memorial-portrait { height: 120px; background: #333; display: flex; align-items: center; justify-content: center; overflow: hidden; position: relative; }
        .memorial-portrait img { width: 100%; height: 100%; object-fit: cover; }
        .memorial-portrait::after { content: ''; position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.3); }
        .memorial-info { padding: 14px; }
        .memorial-name { font-size: 16px; font-weight: bold; color: #333; text-align: center; margin-bottom: 6px; }
        .memorial-dates { color: #666; font-size: 12px; text-align: center; margin-bottom: 8px; }
        .memorial-epitaph { font-style: italic; color: #888; font-size: 12px; text-align: center; padding: 10px; background: #f9f9f9; border-radius: 8px; }
        .memorial-details { margin-top: 12px; }
        .memorial-obituary { color: #555; font-size: 13px; line-height: 1.5; }
        .create-form { background: white; padding: 14px; border-radius: 12px; margin-bottom: 12px; }
        .field { margin-bottom: 12px; }
        .field label { display: block; margin-bottom: 6px; color: #666; font-size: 13px; }
        .field input, .field textarea, .field select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px; font-size: 13px; }
        .toolbar { display: flex; gap: 8px; margin-bottom: 12px; align-items: center; flex-wrap: wrap; }
        .filter-select { padding: 8px; border: 1px solid #ddd; border-radius: 8px; font-size: 13px; }
        .message { margin-top: 12px; padding: 10px; border-radius: 8px; font-size: 13px; }
        .message.success { background: #d4edda; color: #155724; }
        .message.error { background: #f8d7da; color: #721c24; }
        .empty-state { text-align: center; padding: 40px 16px; color: #999; }
        .empty-state p { font-size: 14px; }
        .ribbon { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 3px 10px; border-radius: 4px; font-size: 11px; margin-bottom: 12px; display: inline-block; }
        @media (min-width: 640px) {
          .container { padding: 20px; }
          h1 { font-size: 24px; margin-bottom: 20px; }
          .memorial-grid { grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 16px; }
          .memorial-portrait { height: 150px; }
          .memorial-info { padding: 16px; }
          .memorial-name { font-size: 18px; }
        }
        @media (min-width: 1024px) {
          .memorial-grid { grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 20px; }
          .memorial-portrait { height: 200px; }
          .memorial-info { padding: 20px; }
          .memorial-name { font-size: 20px; }
        }
      </style>
    </head>
    <body>
      <div class="container">
        <h1>纪念堂</h1>
        <span class="ribbon">缅怀先人 · 传承记忆</span>
        
        <div class="toolbar">
          <button class="btn" onclick="showCreateForm()">创建纪念</button>
          <select class="filter-select" id="familySelect" onchange="loadMemorials()">
            <option value="">全部家族</option>
          </select>
          <button class="btn btn-secondary" onclick="goHome()">返回首页</button>
        </div>

        <div id="createMemorialForm" class="create-form hidden">
          <h3>创建纪念</h3>
          <div class="field">
            <label>选择家族</label>
            <select id="memorialFamilySelect"></select>
          </div>
          <div class="field">
            <label>逝者姓名</label>
            <input type="text" id="memorialName">
          </div>
          <div class="field">
            <label>出生日期</label>
            <input type="date" id="memorialBirth">
          </div>
          <div class="field">
            <label>去世日期</label>
            <input type="date" id="memorialDeath">
          </div>
          <div class="field">
            <label>墓志铭</label>
            <textarea id="memorialEpitaph" rows="3" placeholder="逝者生平写照..."></textarea>
          </div>
          <div class="field">
            <label>肖像URL</label>
            <input type="text" id="memorialPortrait" placeholder="https://...">
          </div>
          <div class="field">
            <label>生平简介</label>
            <textarea id="memorialObituary" rows="4" placeholder="简述逝者生平事迹..."></textarea>
          </div>
          <button class="btn" onclick="submitCreateMemorial()">创建</button>
          <button class="btn btn-secondary" onclick="hideCreateForm()">取消</button>
          <div id="createMemorialMessage" class="message"></div>
        </div>

        <div id="memorialList" class="memorial-grid"></div>
      </div>

      <script>
        const currentFamilyId = '${familyId || ''}';

        async function callTool(name, params) {
          const response = await fetch('/api/call', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ toolName: name, params })
          });
          return response.json();
        }

        async function loadFamilies() {
          const result = await callTool('listFamilies', {});
          const families = result.families || [];
          
          const select = document.getElementById('familySelect');
          families.forEach(f => {
            const option = document.createElement('option');
            option.value = f.id;
            option.textContent = f.name;
            option.selected = f.id === currentFamilyId;
            select.appendChild(option);
          });

          const memSelect = document.getElementById('memorialFamilySelect');
          families.forEach(f => {
            const option = document.createElement('option');
            option.value = f.id;
            option.textContent = f.name;
            memSelect.appendChild(option);
          });
        }

        async function loadMemorials() {
          const familyId = document.getElementById('familySelect')?.value || currentFamilyId;
          const result = await callTool('listMemorials', { familyId });
          const memorials = result.memorials || [];
          
          const container = document.getElementById('memorialList');
          if (memorials.length === 0) {
            container.innerHTML = '<div class="empty-state"><p>暂无纪念记录</p></div>';
            return;
          }

          container.innerHTML = memorials.map(m => \`
            <div class="memorial-card">
              <div class="memorial-portrait">
                \${m.portraitUrl ? \`<img src="\${m.portraitUrl}" alt="\${m.memberName}">\` : '<div style="color:white;font-size:48px;">\${m.memberName.charAt(0)}</div>'}
              </div>
              <div class="memorial-info">
                <div class="memorial-name">\${m.memberName}</div>
                <div class="memorial-dates">\${m.birthDate} — \${m.deathDate}</div>
                \${m.epitaph ? \`<div class="memorial-epitaph">"\${m.epitaph}"</div>\` : ''}
                \${m.obituary ? \`
                  <div class="memorial-details">
                    <div class="memorial-obituary">\${m.obituary}</div>
                  </div>
                \` : ''}
              </div>
            </div>
          \`).join('');
        }

        function showCreateForm() {
          document.getElementById('createMemorialForm').classList.remove('hidden');
        }

        function hideCreateForm() {
          document.getElementById('createMemorialForm').classList.add('hidden');
        }

        async function submitCreateMemorial() {
          const familyId = document.getElementById('memorialFamilySelect').value;
          const memberName = document.getElementById('memorialName').value;
          const birthDate = document.getElementById('memorialBirth').value;
          const deathDate = document.getElementById('memorialDeath').value;
          const epitaph = document.getElementById('memorialEpitaph').value;
          const portraitUrl = document.getElementById('memorialPortrait').value;
          const obituary = document.getElementById('memorialObituary').value;
          
          if (!memberName || !birthDate || !deathDate) {
            document.getElementById('createMemorialMessage').className = 'message error';
            document.getElementById('createMemorialMessage').textContent = '请填写必要信息';
            return;
          }

          const result = await callTool('createMemorial', {
            familyId,
            memberId: 'member-' + Date.now(),
            memberName,
            birthDate,
            deathDate,
            epitaph: epitaph || undefined,
            portraitUrl: portraitUrl || undefined,
            obituary: obituary || undefined,
          });
          const msg = document.getElementById('createMemorialMessage');
          
          if (result.success) {
            msg.className = 'message success';
            msg.textContent = result.message;
            document.getElementById('memorialName').value = '';
            document.getElementById('memorialBirth').value = '';
            document.getElementById('memorialDeath').value = '';
            document.getElementById('memorialEpitaph').value = '';
            document.getElementById('memorialPortrait').value = '';
            document.getElementById('memorialObituary').value = '';
            hideCreateForm();
            loadMemorials();
          } else {
            msg.className = 'message error';
            msg.textContent = result.message;
          }
        }

        function goHome() {
          window.parent.postMessage({ type: 'navigate', uri: 'ui://family/home' }, '*');
        }

        loadFamilies();
        loadMemorials();
      </script>
    </body>
    </html>
  `;
}
