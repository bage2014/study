import { User } from '../familyStore';

export function generateAlbumHtml(user: User | null, familyId?: string, albumId?: string): string {
  return `
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>家族相册</title>
      <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; padding: 16px; }
        h1 { color: #333; margin-bottom: 16px; font-size: 20px; }
        .btn { background: #4CAF50; color: white; border: none; padding: 10px 16px; border-radius: 8px; font-size: 13px; cursor: pointer; }
        .btn:hover { background: #45a049; }
        .btn-secondary { background: #f0f0f0; color: #333; }
        .btn-secondary:hover { background: #e0e0e0; }
        .btn-danger { background: #f44336; }
        .btn-danger:hover { background: #d32f2f; }
        .album-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 12px; margin-top: 16px; }
        .album-card { background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1); cursor: pointer; }
        .album-card:hover { transform: translateY(-4px); transition: transform 0.2s; }
        .album-cover { height: 120px; background: #eee; display: flex; align-items: center; justify-content: center; overflow: hidden; }
        .album-cover img { width: 100%; height: 100%; object-fit: cover; }
        .album-info { padding: 12px; }
        .album-name { font-size: 14px; font-weight: bold; color: #333; }
        .album-desc { color: #666; font-size: 12px; margin-top: 6px; }
        .album-count { color: #999; font-size: 11px; margin-top: 6px; }
        .photo-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(100px, 1fr)); gap: 8px; margin-top: 16px; }
        .photo-item { aspect-ratio: 1; background: #eee; border-radius: 8px; overflow: hidden; cursor: pointer; }
        .photo-item img { width: 100%; height: 100%; object-fit: cover; }
        .photo-item:hover { opacity: 0.9; }
        .modal { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.8); z-index: 1000; justify-content: center; align-items: center; }
        .modal.show { display: flex; }
        .modal-content { max-width: 90%; max-height: 90%; }
        .modal-content img { max-width: 100%; max-height: 80vh; border-radius: 8px; }
        .modal-close { position: absolute; top: 16px; right: 16px; color: white; font-size: 28px; cursor: pointer; }
        .modal-info { color: white; text-align: center; margin-top: 12px; font-size: 14px; }
        .back-link { display: inline-flex; align-items: center; color: #4CAF50; cursor: pointer; margin-bottom: 12px; font-size: 14px; }
        .back-link:hover { text-decoration: underline; }
        .create-form { background: white; padding: 16px; border-radius: 12px; margin-bottom: 16px; }
        .field { margin-bottom: 12px; }
        .field label { display: block; margin-bottom: 6px; color: #666; font-size: 13px; }
        .field input, .field textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px; font-size: 13px; }
        .toolbar { display: flex; gap: 8px; margin-bottom: 16px; align-items: center; flex-wrap: wrap; }
        .filter-select { padding: 8px; border: 1px solid #ddd; border-radius: 8px; font-size: 13px; }
        .message { margin-top: 12px; padding: 10px; border-radius: 8px; font-size: 13px; }
        .message.success { background: #d4edda; color: #155724; }
        .message.error { background: #f8d7da; color: #721c24; }
        @media (min-width: 640px) {
          .container { padding: 20px; }
          h1 { font-size: 24px; margin-bottom: 20px; }
          .album-grid { grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 16px; }
          .album-cover { height: 150px; }
          .photo-grid { grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 10px; }
        }
        @media (min-width: 1024px) {
          .album-grid { grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 20px; }
          .album-cover { height: 200px; }
          .photo-grid { grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 12px; }
        }
      </style>
    </head>
    <body>
      <div class="container">
        <h1>家族相册</h1>
        
        <div class="toolbar">
          ${!albumId ? `
            <button class="btn" onclick="createAlbum()">创建相册</button>
            <select class="filter-select" id="familySelect" onchange="loadAlbums()">
              <option value="">全部家族</option>
            </select>
          ` : `
            <div class="back-link" onclick="goBack()">← 返回相册列表</div>
            <button class="btn" onclick="uploadPhoto()">上传照片</button>
            <button class="btn btn-danger" onclick="deleteAlbum()">删除相册</button>
          `}
          <button class="btn btn-secondary" onclick="goHome()">返回首页</button>
        </div>

        ${!albumId ? `
          <div id="albumList" class="album-grid">加载中...</div>
          <div id="createAlbumForm" class="create-form hidden">
            <h3>创建新相册</h3>
            <div class="field">
              <label>相册名称</label>
              <input type="text" id="albumName">
            </div>
            <div class="field">
              <label>相册描述</label>
              <textarea id="albumDesc" rows="3"></textarea>
            </div>
            <div class="field">
              <label>选择家族</label>
              <select id="createFamilySelect"></select>
            </div>
            <button class="btn" onclick="submitCreateAlbum()">创建</button>
            <button class="btn btn-secondary" onclick="cancelCreateAlbum()">取消</button>
            <div id="createAlbumMessage" class="message"></div>
          </div>
        ` : `
          <div id="photoList" class="photo-grid">加载中...</div>
          <div id="uploadPhotoForm" class="create-form hidden">
            <h3>上传照片</h3>
            <div class="field">
              <label>照片标题</label>
              <input type="text" id="photoTitle">
            </div>
            <div class="field">
              <label>照片描述</label>
              <textarea id="photoDesc" rows="3"></textarea>
            </div>
            <div class="field">
              <label>照片URL</label>
              <input type="text" id="photoUrl" placeholder="https://...">
            </div>
            <button class="btn" onclick="submitUploadPhoto()">上传</button>
            <button class="btn btn-secondary" onclick="cancelUploadPhoto()">取消</button>
            <div id="uploadPhotoMessage" class="message"></div>
          </div>
        `}
      </div>

      <div id="photoModal" class="modal">
        <span class="modal-close" onclick="closeModal()">×</span>
        <div class="modal-content">
          <img id="modalImage" src="" alt="">
          <div class="modal-info">
            <h3 id="modalTitle"></h3>
            <p id="modalDesc"></p>
          </div>
        </div>
      </div>

      <script>
        const currentFamilyId = '${familyId || ''}';
        const currentAlbumId = '${albumId || ''}';

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
          if (select) {
            families.forEach(f => {
              const option = document.createElement('option');
              option.value = f.id;
              option.textContent = f.name;
              option.selected = f.id === currentFamilyId;
              select.appendChild(option);
            });
          }

          const createSelect = document.getElementById('createFamilySelect');
          if (createSelect) {
            families.forEach(f => {
              const option = document.createElement('option');
              option.value = f.id;
              option.textContent = f.name;
              createSelect.appendChild(option);
            });
          }
        }

        async function loadAlbums() {
          const familyId = document.getElementById('familySelect')?.value || currentFamilyId;
          const result = await callTool('listAlbums', { familyId });
          const albums = result.albums || [];
          
          const container = document.getElementById('albumList');
          if (albums.length === 0) {
            container.innerHTML = '<p style="text-align:center;color:#999;padding:40px;">暂无相册</p>';
            return;
          }

          container.innerHTML = albums.map(album => \`
            <div class="album-card" onclick="openAlbum('\${album.id}', '\${album.familyId}')">
              <div class="album-cover">
                \${album.coverPhotoUrl ? \`<img src="\${album.coverPhotoUrl}" alt="\${album.name}">\` : '<div style="color:#999;">暂无封面</div>'}
              </div>
              <div class="album-info">
                <div class="album-name">\${album.name}</div>
                <div class="album-desc">\${album.description}</div>
                <div class="album-count">\${album.photoCount} 张照片 · \${album.createdAt}</div>
              </div>
            </div>
          \`).join('');
        }

        async function loadPhotos() {
          const result = await callTool('listPhotos', { albumId: currentAlbumId });
          const photos = result.photos || [];
          
          const container = document.getElementById('photoList');
          if (photos.length === 0) {
            container.innerHTML = '<p style="text-align:center;color:#999;padding:40px;">暂无照片</p>';
            return;
          }

          container.innerHTML = photos.map(photo => \`
            <div class="photo-item" onclick="openPhoto('\${photo.url}', '\${photo.title}', '\${photo.description}')">
              <img src="\${photo.thumbnailUrl}" alt="\${photo.title}">
            </div>
          \`).join('');
        }

        function createAlbum() {
          document.getElementById('createAlbumForm').classList.remove('hidden');
        }

        function cancelCreateAlbum() {
          document.getElementById('createAlbumForm').classList.add('hidden');
        }

        async function submitCreateAlbum() {
          const name = document.getElementById('albumName').value;
          const description = document.getElementById('albumDesc').value;
          const familyId = document.getElementById('createFamilySelect').value;
          
          const result = await callTool('createAlbum', { familyId, name, description });
          const msg = document.getElementById('createAlbumMessage');
          
          if (result.success) {
            msg.className = 'message success';
            msg.textContent = result.message;
            document.getElementById('albumName').value = '';
            document.getElementById('albumDesc').value = '';
            cancelCreateAlbum();
            loadAlbums();
          } else {
            msg.className = 'message error';
            msg.textContent = result.message;
          }
        }

        function uploadPhoto() {
          document.getElementById('uploadPhotoForm').classList.remove('hidden');
        }

        function cancelUploadPhoto() {
          document.getElementById('uploadPhotoForm').classList.add('hidden');
        }

        async function submitUploadPhoto() {
          const title = document.getElementById('photoTitle').value;
          const description = document.getElementById('photoDesc').value;
          const url = document.getElementById('photoUrl').value;
          
          if (!url) {
            document.getElementById('uploadPhotoMessage').className = 'message error';
            document.getElementById('uploadPhotoMessage').textContent = '请输入照片URL';
            return;
          }

          const result = await callTool('createPhoto', {
            familyId: currentFamilyId,
            albumId: currentAlbumId,
            title,
            description,
            url,
            thumbnailUrl: url,
          });
          const msg = document.getElementById('uploadPhotoMessage');
          
          if (result.success) {
            msg.className = 'message success';
            msg.textContent = result.message;
            document.getElementById('photoTitle').value = '';
            document.getElementById('photoDesc').value = '';
            document.getElementById('photoUrl').value = '';
            cancelUploadPhoto();
            loadPhotos();
          } else {
            msg.className = 'message error';
            msg.textContent = result.message;
          }
        }

        async function deleteAlbum() {
          if (!confirm('确定要删除这个相册吗？')) return;
          const result = await callTool('deleteAlbum', { albumId: currentAlbumId });
          if (result.success) {
            goBack();
          } else {
            alert(result.message);
          }
        }

        function openAlbum(albumId, familyId) {
          window.parent.postMessage({ type: 'navigate', uri: 'ui://family/album?familyId=' + familyId + '&albumId=' + albumId }, '*');
        }

        function goBack() {
          window.parent.postMessage({ type: 'navigate', uri: 'ui://family/album' + (currentFamilyId ? '?familyId=' + currentFamilyId : '') }, '*');
        }

        function openPhoto(url, title, desc) {
          document.getElementById('modalImage').src = url;
          document.getElementById('modalTitle').textContent = title;
          document.getElementById('modalDesc').textContent = desc;
          document.getElementById('photoModal').classList.add('show');
        }

        function closeModal() {
          document.getElementById('photoModal').classList.remove('show');
        }

        function goHome() {
          window.parent.postMessage({ type: 'navigate', uri: 'ui://family/home' }, '*');
        }

        document.getElementById('photoModal').addEventListener('click', (e) => {
          if (e.target === document.getElementById('photoModal')) {
            closeModal();
          }
        });

        loadFamilies();
        ${!albumId ? 'loadAlbums();' : 'loadPhotos();'}
      </script>
    </body>
    </html>
  `;
}
