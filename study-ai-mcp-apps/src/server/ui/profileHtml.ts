import { User } from '../familyStore';

export function generateProfileHtml(user: User | null): string {
  if (!user) {
    return `
      <!DOCTYPE html>
      <html lang="zh-CN">
      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>个人中心</title>
        <style>
          * { margin: 0; padding: 0; box-sizing: border-box; }
          body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; }
          .container { max-width: 800px; margin: 0 auto; padding: 20px; }
          .card { background: white; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 24px; margin-bottom: 20px; }
          h1 { color: #333; margin-bottom: 24px; font-size: 24px; }
          .field { margin-bottom: 16px; }
          .field label { display: block; margin-bottom: 8px; color: #666; font-size: 14px; }
          .field input, .field textarea { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 16px; }
          .field input:focus, .field textarea:focus { outline: none; border-color: #4CAF50; }
          .btn { background: #4CAF50; color: white; border: none; padding: 12px 24px; border-radius: 8px; font-size: 16px; cursor: pointer; }
          .btn:hover { background: #45a049; }
          .btn-secondary { background: #f0f0f0; color: #333; }
          .btn-secondary:hover { background: #e0e0e0; }
          .error { color: red; margin-top: 8px; }
          .success { color: green; margin-top: 8px; }
          .section-title { font-size: 18px; font-weight: bold; margin-bottom: 16px; color: #333; border-bottom: 2px solid #eee; padding-bottom: 8px; }
          .hidden { display: none; }
        </style>
      </head>
      <body>
        <div class="container">
          <h1>个人中心</h1>
          <div class="card">
            <div class="section-title">基本信息</div>
            <div class="field">
              <label>昵称</label>
              <input type="text" id="nickname" value="${user?.nickname || ''}">
            </div>
            <div class="field">
              <label>邮箱</label>
              <input type="email" id="email" value="${user?.email || ''}" disabled>
            </div>
            <div class="field">
              <label>手机号</label>
              <input type="tel" id="phone" value="${user?.phone || ''}">
            </div>
            <button class="btn" onclick="updateProfile()">保存修改</button>
            <div id="profile-message" class="success"></div>
          </div>
          <div class="card">
            <div class="section-title">修改密码</div>
            <div class="field">
              <label>旧密码</label>
              <input type="password" id="oldPassword">
            </div>
            <div class="field">
              <label>新密码</label>
              <input type="password" id="newPassword">
            </div>
            <div class="field">
              <label>确认新密码</label>
              <input type="password" id="confirmPassword">
            </div>
            <button class="btn" onclick="changePassword()">修改密码</button>
            <div id="password-message" class="error"></div>
          </div>
          <div class="card">
            <button class="btn btn-secondary" onclick="goHome()">返回首页</button>
          </div>
        </div>
        <script>
          async function callTool(name, params) {
            const response = await fetch('/api/call', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({ toolName: name, params })
            });
            return response.json();
          }
          async function updateProfile() {
            const nickname = document.getElementById('nickname').value;
            const phone = document.getElementById('phone').value;
            const result = await callTool('updateProfile', { nickname, phone });
            const msg = document.getElementById('profile-message');
            if (result.success) {
              msg.className = 'success';
              msg.textContent = result.message;
            } else {
              msg.className = 'error';
              msg.textContent = result.message;
            }
          }
          async function changePassword() {
            const oldPassword = document.getElementById('oldPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            if (newPassword !== confirmPassword) {
              document.getElementById('password-message').textContent = '两次输入的密码不一致';
              return;
            }
            const result = await callTool('changePassword', { oldPassword, newPassword });
            const msg = document.getElementById('password-message');
            if (result.success) {
              msg.className = 'success';
              msg.textContent = result.message;
              document.getElementById('oldPassword').value = '';
              document.getElementById('newPassword').value = '';
              document.getElementById('confirmPassword').value = '';
            } else {
              msg.className = 'error';
              msg.textContent = result.message;
            }
          }
          function goHome() {
            window.parent.postMessage({ type: 'navigate', uri: 'ui://family/home' }, '*');
          }
        </script>
      </body>
      </html>
    `;
  }

  return `
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>个人中心</title>
      <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        .card { background: white; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 24px; margin-bottom: 20px; }
        h1 { color: #333; margin-bottom: 24px; font-size: 24px; }
        .field { margin-bottom: 16px; }
        .field label { display: block; margin-bottom: 8px; color: #666; font-size: 14px; }
        .field input, .field textarea { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 16px; }
        .field input:focus, .field textarea:focus { outline: none; border-color: #4CAF50; }
        .btn { background: #4CAF50; color: white; border: none; padding: 12px 24px; border-radius: 8px; font-size: 16px; cursor: pointer; }
        .btn:hover { background: #45a049; }
        .btn-secondary { background: #f0f0f0; color: #333; }
        .btn-secondary:hover { background: #e0e0e0; }
        .error { color: red; margin-top: 8px; }
        .success { color: green; margin-top: 8px; }
        .section-title { font-size: 18px; font-weight: bold; margin-bottom: 16px; color: #333; border-bottom: 2px solid #eee; padding-bottom: 8px; }
        .hidden { display: none; }
      </style>
    </head>
    <body>
      <div class="container">
        <h1>个人中心</h1>
        <div class="card">
          <div class="section-title">基本信息</div>
          <div class="field">
            <label>昵称</label>
            <input type="text" id="nickname" value="${user.nickname}">
          </div>
          <div class="field">
            <label>邮箱</label>
            <input type="email" id="email" value="${user.email}" disabled>
          </div>
          <div class="field">
            <label>手机号</label>
            <input type="tel" id="phone" value="${user.phone || ''}">
          </div>
          <button class="btn" onclick="updateProfile()">保存修改</button>
          <div id="profile-message"></div>
        </div>
        <div class="card">
          <div class="section-title">修改密码</div>
          <div class="field">
            <label>旧密码</label>
            <input type="password" id="oldPassword">
          </div>
          <div class="field">
            <label>新密码</label>
            <input type="password" id="newPassword">
          </div>
          <div class="field">
            <label>确认新密码</label>
            <input type="password" id="confirmPassword">
          </div>
          <button class="btn" onclick="changePassword()">修改密码</button>
          <div id="password-message"></div>
        </div>
        <div class="card">
          <button class="btn btn-secondary" onclick="goHome()">返回首页</button>
        </div>
      </div>
      <script>
        async function callTool(name, params) {
          const response = await fetch('/api/call', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ toolName: name, params })
          });
          return response.json();
        }
        async function updateProfile() {
          const nickname = document.getElementById('nickname').value;
          const phone = document.getElementById('phone').value;
          const result = await callTool('updateProfile', { nickname, phone });
          const msg = document.getElementById('profile-message');
          if (result.success) {
            msg.className = 'success';
            msg.textContent = result.message;
          } else {
            msg.className = 'error';
            msg.textContent = result.message;
          }
        }
        async function changePassword() {
          const oldPassword = document.getElementById('oldPassword').value;
          const newPassword = document.getElementById('newPassword').value;
          const confirmPassword = document.getElementById('confirmPassword').value;
          if (newPassword !== confirmPassword) {
            document.getElementById('password-message').className = 'error';
            document.getElementById('password-message').textContent = '两次输入的密码不一致';
            return;
          }
          const result = await callTool('changePassword', { oldPassword, newPassword });
          const msg = document.getElementById('password-message');
          if (result.success) {
            msg.className = 'success';
            msg.textContent = result.message;
            document.getElementById('oldPassword').value = '';
            document.getElementById('newPassword').value = '';
            document.getElementById('confirmPassword').value = '';
          } else {
            msg.className = 'error';
            msg.textContent = result.message;
          }
        }
        function goHome() {
          window.parent.postMessage({ type: 'navigate', uri: 'ui://family/home' }, '*');
        }
      </script>
    </body>
    </html>
  `;
}
