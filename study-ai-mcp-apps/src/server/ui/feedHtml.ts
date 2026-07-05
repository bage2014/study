import { User } from '../familyStore';

export function generateFeedHtml(user: User | null, familyId?: string): string {
  return `
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>家族动态</title>
      <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        h1 { color: #333; margin-bottom: 20px; font-size: 24px; }
        .btn { background: #4CAF50; color: white; border: none; padding: 10px 20px; border-radius: 8px; font-size: 14px; cursor: pointer; }
        .btn:hover { background: #45a049; }
        .btn-secondary { background: #f0f0f0; color: #333; }
        .btn-secondary:hover { background: #e0e0e0; }
        .feed-card { background: white; border-radius: 12px; padding: 20px; margin-bottom: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        .feed-header { display: flex; align-items: center; margin-bottom: 16px; }
        .feed-avatar { width: 48px; height: 48px; border-radius: 50%; background: #4CAF50; color: white; display: flex; align-items: center; justify-content: center; font-size: 20px; margin-right: 12px; }
        .feed-user-info { flex: 1; }
        .feed-user-name { font-weight: bold; color: #333; }
        .feed-time { color: #999; font-size: 12px; }
        .feed-type { display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 12px; margin-left: 8px; }
        .type-announcement { background: #ffebee; color: #c62828; }
        .type-event { background: #e3f2fd; color: #1565c0; }
        .type-photo { background: #e8f5e9; color: #2e7d32; }
        .type-text { background: #f5f5f5; color: #666; }
        .feed-content { color: #333; line-height: 1.6; margin-bottom: 16px; }
        .feed-photo { max-width: 100%; border-radius: 8px; margin-bottom: 16px; }
        .feed-actions { display: flex; gap: 20px; padding-top: 12px; border-top: 1px solid #eee; }
        .action-btn { display: flex; align-items: center; gap: 6px; cursor: pointer; color: #666; font-size: 14px; }
        .action-btn:hover { color: #4CAF50; }
        .action-btn.liked { color: #f44336; }
        .action-btn.liked svg { fill: #f44336; }
        .comments-section { margin-top: 16px; padding-top: 12px; border-top: 1px dashed #eee; }
        .comment { display: flex; margin-bottom: 12px; }
        .comment-avatar { width: 36px; height: 36px; border-radius: 50%; background: #ccc; color: white; display: flex; align-items: center; justify-content: center; font-size: 16px; margin-right: 8px; }
        .comment-content { flex: 1; background: #f9f9f9; padding: 8px 12px; border-radius: 8px; }
        .comment-user { font-weight: bold; font-size: 13px; margin-bottom: 4px; }
        .comment-text { font-size: 14px; color: #333; }
        .comment-time { font-size: 11px; color: #999; margin-top: 4px; }
        .comment-input { display: flex; gap: 8px; margin-top: 12px; }
        .comment-input input { flex: 1; padding: 10px; border: 1px solid #ddd; border-radius: 20px; font-size: 14px; }
        .comment-input button { padding: 10px 20px; border-radius: 20px; }
        .create-form { background: white; padding: 24px; border-radius: 12px; margin-bottom: 20px; }
        .field { margin-bottom: 16px; }
        .field label { display: block; margin-bottom: 8px; color: #666; }
        .field textarea { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; resize: vertical; }
        .field select, .field input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; }
        .toolbar { display: flex; gap: 10px; margin-bottom: 20px; align-items: center; flex-wrap: wrap; }
        .filter-select { padding: 10px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; }
        .message { margin-top: 16px; padding: 12px; border-radius: 8px; }
        .message.success { background: #d4edda; color: #155724; }
        .message.error { background: #f8d7da; color: #721c24; }
        .empty-state { text-align: center; padding: 60px 20px; color: #999; }
        .empty-state svg { width: 64px; height: 64px; margin-bottom: 16px; opacity: 0.5; }
      </style>
    </head>
    <body>
      <div class="container">
        <h1>家族动态</h1>
        
        <div class="toolbar">
          <button class="btn" onclick="showCreateForm()">发布动态</button>
          <select class="filter-select" id="familySelect" onchange="loadFeeds()">
            <option value="">全部家族</option>
          </select>
          <button class="btn btn-secondary" onclick="goHome()">返回首页</button>
        </div>

        <div id="createFeedForm" class="create-form hidden">
          <h3>发布动态</h3>
          <div class="field">
            <label>动态类型</label>
            <select id="feedType">
              <option value="text">文字</option>
              <option value="photo">图片</option>
              <option value="event">事件</option>
              <option value="announcement">公告</option>
            </select>
          </div>
          <div class="field">
            <label>选择家族</label>
            <select id="feedFamilySelect"></select>
          </div>
          <div class="field">
            <label>内容</label>
            <textarea id="feedContent" rows="4" placeholder="分享你的动态..."></textarea>
          </div>
          <div class="field" id="photoUrlField" style="display:none;">
            <label>图片URL</label>
            <input type="text" id="feedPhotoUrl" placeholder="https://...">
          </div>
          <button class="btn" onclick="submitCreateFeed()">发布</button>
          <button class="btn btn-secondary" onclick="hideCreateForm()">取消</button>
          <div id="createFeedMessage" class="message"></div>
        </div>

        <div id="feedList"></div>
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

          const feedSelect = document.getElementById('feedFamilySelect');
          families.forEach(f => {
            const option = document.createElement('option');
            option.value = f.id;
            option.textContent = f.name;
            feedSelect.appendChild(option);
          });
        }

        async function loadFeeds() {
          const familyId = document.getElementById('familySelect')?.value || currentFamilyId;
          const result = await callTool('listFeeds', { familyId });
          const feeds = result.feeds || [];
          
          const container = document.getElementById('feedList');
          if (feeds.length === 0) {
            container.innerHTML = '<div class="empty-state"><p>暂无动态</p></div>';
            return;
          }

          container.innerHTML = feeds.map(feed => \`
            <div class="feed-card" id="feed-\${feed.id}">
              <div class="feed-header">
                <div class="feed-avatar">\${feed.userName.charAt(0)}</div>
                <div class="feed-user-info">
                  <div class="feed-user-name">\${feed.userName} <span class="feed-type type-\${feed.type}">\${getTypeLabel(feed.type)}</span></div>
                  <div class="feed-time">\${feed.createdAt}</div>
                </div>
              </div>
              <div class="feed-content">\${feed.content}</div>
              \${feed.photoUrl ? \`<img class="feed-photo" src="\${feed.photoUrl}" alt="">\` : ''}
              <div class="feed-actions">
                <div class="action-btn" onclick="toggleLike('\${feed.id}')">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path></svg>
                  <span id="likes-\${feed.id}">\${feed.likes}</span>
                </div>
                <div class="action-btn" onclick="toggleComments('\${feed.id}')">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path></svg>
                  <span>\${feed.comments.length}</span>
                </div>
              </div>
              <div class="comments-section" id="comments-\${feed.id}" style="display:none;">
                \${feed.comments.map(c => \`
                  <div class="comment">
                    <div class="comment-avatar">\${c.userName.charAt(0)}</div>
                    <div class="comment-content">
                      <div class="comment-user">\${c.userName}</div>
                      <div class="comment-text">\${c.content}</div>
                      <div class="comment-time">\${c.createdAt}</div>
                    </div>
                  </div>
                \`).join('')}
                <div class="comment-input">
                  <input type="text" id="comment-input-\${feed.id}" placeholder="发表评论...">
                  <button class="btn" onclick="submitComment('\${feed.id}')">发送</button>
                </div>
              </div>
            </div>
          \`).join('');
        }

        function getTypeLabel(type) {
          const labels = { text: '动态', photo: '图片', event: '事件', announcement: '公告' };
          return labels[type] || type;
        }

        function showCreateForm() {
          document.getElementById('createFeedForm').classList.remove('hidden');
        }

        function hideCreateForm() {
          document.getElementById('createFeedForm').classList.add('hidden');
        }

        document.getElementById('feedType').addEventListener('change', function() {
          document.getElementById('photoUrlField').style.display = this.value === 'photo' ? 'block' : 'none';
        });

        async function submitCreateFeed() {
          const type = document.getElementById('feedType').value;
          const familyId = document.getElementById('feedFamilySelect').value;
          const content = document.getElementById('feedContent').value;
          const photoUrl = document.getElementById('feedPhotoUrl').value;
          
          if (!content) {
            document.getElementById('createFeedMessage').className = 'message error';
            document.getElementById('createFeedMessage').textContent = '请输入内容';
            return;
          }

          const result = await callTool('createFeed', {
            familyId,
            type,
            content,
            photoUrl: type === 'photo' ? photoUrl : undefined,
          });
          const msg = document.getElementById('createFeedMessage');
          
          if (result.success) {
            msg.className = 'message success';
            msg.textContent = result.message;
            document.getElementById('feedContent').value = '';
            document.getElementById('feedPhotoUrl').value = '';
            hideCreateForm();
            loadFeeds();
          } else {
            msg.className = 'message error';
            msg.textContent = result.message;
          }
        }

        async function toggleLike(feedId) {
          const result = await callTool('toggleLike', { feedId });
          if (result.success) {
            document.getElementById('likes-' + feedId).textContent = result.likes;
          }
        }

        function toggleComments(feedId) {
          const section = document.getElementById('comments-' + feedId);
          section.style.display = section.style.display === 'none' ? 'block' : 'none';
        }

        async function submitComment(feedId) {
          const input = document.getElementById('comment-input-' + feedId);
          const content = input.value.trim();
          if (!content) return;
          
          const result = await callTool('addComment', { feedId, content });
          if (result.success) {
            input.value = '';
            loadFeeds();
          }
        }

        function goHome() {
          window.parent.postMessage({ type: 'navigate', uri: 'ui://family/home' }, '*');
        }

        loadFamilies();
        loadFeeds();
      </script>
    </body>
    </html>
  `;
}
