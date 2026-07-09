export function getSearchHtml(): string {
  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>全局搜索 - 家族档案</title>
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 min-h-screen">
  <header class="bg-white shadow-sm sticky top-0 z-50">
    <div class="max-w-6xl mx-auto px-4 py-4 flex items-center justify-between">
      <div class="flex items-center gap-3">
        <button onclick="navigateTo('home')" class="p-2 hover:bg-gray-100 rounded-lg">
          <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
          </svg>
        </button>
        <div class="relative flex-1 max-w-xl">
          <input type="text" id="searchInput" placeholder="搜索家族、成员、相册..." 
            class="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            onkeyup="handleSearch(event)">
          <svg class="w-5 h-5 text-gray-400 absolute left-3 top-1/2 transform -translate-y-1/2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
          </svg>
        </div>
      </div>
      <div class="flex gap-2">
        <div class="px-3 py-1 bg-blue-100 text-blue-700 text-xs rounded-full" id="resultCount">0 个结果</div>
      </div>
    </div>
    
    <div class="border-t border-gray-100 px-4 py-2">
      <div class="flex gap-2 overflow-x-auto">
        <button onclick="filterType('all')" class="type-btn active px-3 py-1 text-sm rounded-full bg-blue-500 text-white" data-type="all">全部</button>
        <button onclick="filterType('member')" class="type-btn px-3 py-1 text-sm rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200" data-type="member">成员</button>
        <button onclick="filterType('family')" class="type-btn px-3 py-1 text-sm rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200" data-type="family">家族</button>
        <button onclick="filterType('album')" class="type-btn px-3 py-1 text-sm rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200" data-type="album">相册</button>
        <button onclick="filterType('feed')" class="type-btn px-3 py-1 text-sm rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200" data-type="feed">动态</button>
        <button onclick="filterType('memorial')" class="type-btn px-3 py-1 text-sm rounded-full bg-gray-100 text-gray-600 hover:bg-gray-200" data-type="memorial">纪念堂</button>
      </div>
    </div>
  </header>

  <main class="max-w-6xl mx-auto px-4 py-6">
    <div id="searchResults" class="space-y-6">
      <div class="text-center py-12">
        <svg class="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
        </svg>
        <p class="text-gray-500">输入关键词开始搜索</p>
      </div>
    </div>
  </main>

  <script>
    function navigateTo(page) {
      window.parent.postMessage({ type: 'navigate', page: page }, '*');
    }

    var currentType = 'all';
    var searchTimeout;

    function handleSearch(event) {
      if (event.key === 'Enter') {
        performSearch();
      } else {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(performSearch, 300);
      }
    }

    function filterType(type) {
      currentType = type;
      document.querySelectorAll('.type-btn').forEach(function(btn) {
        btn.classList.remove('active', 'bg-blue-500', 'text-white');
        btn.classList.add('bg-gray-100', 'text-gray-600');
      });
      var activeBtn = document.querySelector('[data-type="' + type + '"]');
      activeBtn.classList.remove('bg-gray-100', 'text-gray-600');
      activeBtn.classList.add('active', 'bg-blue-500', 'text-white');
      performSearch();
    }

    async function performSearch() {
      var query = document.getElementById('searchInput').value.trim();
      if (!query) {
        document.getElementById('searchResults').innerHTML = 
          '<div class="text-center py-12">' +
            '<svg class="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
              '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>' +
            '</svg>' +
            '<p class="text-gray-500">输入关键词开始搜索</p>' +
          '</div>';
        document.getElementById('resultCount').textContent = '0 个结果';
        return;
      }

      document.getElementById('searchResults').innerHTML = 
        '<div class="text-center py-12"><div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mx-auto"></div></div>';

      try {
        var result = await mcpCallTool('globalSearch', { 
          query: query, 
          type: currentType 
        });
        
        document.getElementById('resultCount').textContent = result.totalResults + ' 个结果';
        renderResults(result);
      } catch (error) {
        console.error('Search failed:', error);
        document.getElementById('searchResults').innerHTML = 
          '<div class="text-center py-12 text-red-500">搜索失败，请重试</div>';
      }
    }

    function renderResults(result) {
      var html = '';
      
      if (result.members.length > 0 && (currentType === 'all' || currentType === 'member')) {
        html += '<div class="bg-white rounded-xl p-5 shadow-sm">' +
          '<h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">' +
            '<svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
              '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>' +
            '</svg>' +
            '成员 (' + result.members.length + ')' +
          '</h3>' +
          '<div class="space-y-3">';
        
        result.members.forEach(function(m) {
          html += '<div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors cursor-pointer" onclick="viewMember(\'' + m.id + '\')">' +
            '<div class="flex items-center gap-3">' +
              '<div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-medium">' +
                (m.avatar ? '<img src="' + m.avatar + '" class="w-full h-full rounded-full object-cover" />' : m.name.charAt(0)) +
              '</div>' +
              '<div>' +
                '<div class="font-medium text-gray-800">' + m.name + '</div>' +
                '<div class="text-xs text-gray-500">' + m.role + ' · ' + m.familyName + '</div>' +
              '</div>' +
            '</div>' +
            '<svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
              '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>' +
            '</svg>' +
          '</div>';
        });
        
        html += '</div></div>';
      }
      
      if (result.families.length > 0 && (currentType === 'all' || currentType === 'family')) {
        html += '<div class="bg-white rounded-xl p-5 shadow-sm">' +
          '<h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">' +
            '<svg class="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
              '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>' +
            '</svg>' +
            '家族 (' + result.families.length + ')' +
          '</h3>' +
          '<div class="space-y-3">';
        
        result.families.forEach(function(f) {
          html += '<div class="p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors cursor-pointer" onclick="viewFamily(\'' + f.id + '\')">' +
            '<div class="font-medium text-gray-800">' + f.name + '</div>' +
            '<div class="text-xs text-gray-500 mt-1">' + (f.description || '暂无描述') + '</div>' +
            '<div class="text-xs text-blue-500 mt-2">' + f.memberCount + ' 位成员</div>' +
          '</div>';
        });
        
        html += '</div></div>';
      }
      
      if (result.albums.length > 0 && (currentType === 'all' || currentType === 'album')) {
        html += '<div class="bg-white rounded-xl p-5 shadow-sm">' +
          '<h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">' +
            '<svg class="w-5 h-5 text-purple-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
              '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>' +
            '</svg>' +
            '相册 (' + result.albums.length + ')' +
          '</h3>' +
          '<div class="space-y-3">';
        
        result.albums.forEach(function(a) {
          html += '<div class="p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors cursor-pointer">' +
            '<div class="font-medium text-gray-800">' + a.name + '</div>' +
            '<div class="text-xs text-gray-500 mt-1">' + (a.description || '暂无描述') + '</div>' +
            '<div class="text-xs text-purple-500 mt-2">' + a.familyName + ' · ' + a.photoCount + ' 张照片</div>' +
          '</div>';
        });
        
        html += '</div></div>';
      }
      
      if (result.feeds.length > 0 && (currentType === 'all' || currentType === 'feed')) {
        html += '<div class="bg-white rounded-xl p-5 shadow-sm">' +
          '<h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">' +
            '<svg class="w-5 h-5 text-orange-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
              '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z"></path>' +
            '</svg>' +
            '动态 (' + result.feeds.length + ')' +
          '</h3>' +
          '<div class="space-y-3">';
        
        result.feeds.forEach(function(f) {
          html += '<div class="p-3 bg-gray-50 rounded-lg">' +
            '<div class="text-sm text-gray-700 line-clamp-2">' + f.content + '</div>' +
            '<div class="text-xs text-gray-500 mt-2">' + f.authorName + ' · ' + f.familyName + ' · ' + formatDate(f.createdAt) + '</div>' +
          '</div>';
        });
        
        html += '</div></div>';
      }
      
      if (result.memorials.length > 0 && (currentType === 'all' || currentType === 'memorial')) {
        html += '<div class="bg-white rounded-xl p-5 shadow-sm">' +
          '<h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">' +
            '<svg class="w-5 h-5 text-indigo-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
              '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>' +
            '</svg>' +
            '纪念堂 (' + result.memorials.length + ')' +
          '</h3>' +
          '<div class="space-y-3">';
        
        result.memorials.forEach(function(m) {
          html += '<div class="p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors cursor-pointer">' +
            '<div class="font-medium text-gray-800">' + m.name + '</div>' +
            '<div class="text-xs text-gray-500 mt-1">' + (m.title || '') + '</div>' +
            '<div class="text-xs text-indigo-500 mt-2">' + m.familyName + '</div>' +
          '</div>';
        });
        
        html += '</div></div>';
      }
      
      if (result.totalResults === 0) {
        html = '<div class="text-center py-12">' +
          '<svg class="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
            '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>' +
          '</svg>' +
          '<p class="text-gray-500">未找到相关结果</p>' +
        '</div>';
      }
      
      document.getElementById('searchResults').innerHTML = html;
    }

    function viewMember(id) {
      window.parent.postMessage({ type: 'navigate', page: 'member-detail', id: id }, '*');
    }

    function viewFamily(id) {
      window.parent.postMessage({ type: 'navigate', page: 'family-tree', id: id }, '*');
    }

    function formatDate(dateStr) {
      var date = new Date(dateStr);
      return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' });
    }

    document.addEventListener('DOMContentLoaded', function() {
      document.getElementById('searchInput').focus();
    });
  </script>
</body>
</html>`;
}
