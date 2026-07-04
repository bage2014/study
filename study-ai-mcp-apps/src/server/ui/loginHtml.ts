export function generateLoginHtml(): string {
  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>家庭族谱 - 登录</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    }
    .card-shadow {
      box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
    }
  </style>
</head>
<body class="bg-gradient-to-br from-green-50 to-teal-100 min-h-screen flex items-center justify-center p-4">
  <div class="w-full max-w-md">
    <div class="bg-white rounded-2xl p-8 card-shadow">
      <div class="text-center mb-8">
        <div class="w-16 h-16 bg-green-500 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
          </svg>
        </div>
        <h1 class="text-2xl font-bold text-gray-800">家庭族谱</h1>
        <p class="text-gray-500 mt-2">记录家族历史，传承家族文化</p>
      </div>

      <div id="authForm">
        <div class="space-y-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">邮箱</label>
            <input type="email" id="email" class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-all" placeholder="请输入邮箱">
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">密码</label>
            <input type="password" id="password" class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-all" placeholder="请输入密码">
          </div>
          <div id="registerFields" style="display: none;">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">昵称</label>
              <input type="text" id="nickname" class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-all" placeholder="请输入昵称">
            </div>
          </div>
          <button id="submitBtn" onclick="handleAuth()" class="w-full bg-green-500 hover:bg-green-600 text-white font-semibold py-3 px-4 rounded-lg transition-all duration-200 shadow-lg hover:shadow-xl">
            登录
          </button>
        </div>

        <div class="mt-6 text-center">
          <span id="toggleText" onclick="toggleAuth()" class="text-green-600 hover:text-green-700 font-medium cursor-pointer">
            还没有账号？点击注册
          </span>
        </div>

        <div id="errorMessage" class="mt-4 text-center text-red-500 text-sm" style="display: none;"></div>
      </div>
    </div>

    <div class="mt-6 text-center text-gray-500 text-sm">
      <p>默认账号：admin@family.com / 123456</p>
    </div>
  </div>

  <script>
    var isRegister = false;

    function mcpCallTool(toolName, params) {
      return new Promise((resolve, reject) => {
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

    function toggleAuth() {
      isRegister = !isRegister;
      var registerFields = document.getElementById('registerFields');
      var submitBtn = document.getElementById('submitBtn');
      var toggleText = document.getElementById('toggleText');
      var errorMessage = document.getElementById('errorMessage');

      errorMessage.style.display = 'none';

      if (isRegister) {
        registerFields.style.display = 'block';
        submitBtn.textContent = '注册';
        toggleText.textContent = '已有账号？点击登录';
      } else {
        registerFields.style.display = 'none';
        submitBtn.textContent = '登录';
        toggleText.textContent = '还没有账号？点击注册';
      }
    }

    async function handleAuth() {
      var email = document.getElementById('email').value;
      var password = document.getElementById('password').value;
      var errorMessage = document.getElementById('errorMessage');

      if (!email || !password) {
        errorMessage.textContent = '请填写邮箱和密码';
        errorMessage.style.display = 'block';
        return;
      }

      if (isRegister) {
        var nickname = document.getElementById('nickname').value;
        if (!nickname) {
          errorMessage.textContent = '请填写昵称';
          errorMessage.style.display = 'block';
          return;
        }

        try {
          var result = await mcpCallTool('register', { email, password, nickname });
          if (result.success) {
            errorMessage.textContent = result.message;
            errorMessage.className = 'mt-4 text-center text-green-500 text-sm';
            errorMessage.style.display = 'block';
            setTimeout(function() {
              toggleAuth();
              document.getElementById('email').value = '';
              document.getElementById('password').value = '';
              document.getElementById('nickname').value = '';
              errorMessage.className = 'mt-4 text-center text-red-500 text-sm';
              errorMessage.style.display = 'none';
            }, 2000);
          } else {
            errorMessage.textContent = result.message;
            errorMessage.style.display = 'block';
          }
        } catch (error) {
          errorMessage.textContent = '注册失败，请重试';
          errorMessage.style.display = 'block';
        }
      } else {
        try {
          var result = await mcpCallTool('login', { email, password });
          if (result.success) {
            window.parent.postMessage({ type: 'navigate', page: 'home' }, '*');
          } else {
            errorMessage.textContent = result.message;
            errorMessage.style.display = 'block';
          }
        } catch (error) {
          errorMessage.textContent = '登录失败，请重试';
          errorMessage.style.display = 'block';
        }
      }
    }

    document.getElementById('email').addEventListener('keypress', function(e) {
      if (e.key === 'Enter') {
        handleAuth();
      }
    });

    document.getElementById('password').addEventListener('keypress', function(e) {
      if (e.key === 'Enter') {
        handleAuth();
      }
    });
  </script>
</body>
</html>`;
}