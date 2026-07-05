import { User } from '../familyStore';

export function generateStatsHtml(user: User | null, familyId?: string): string {
  return `
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>统计分析</title>
      <script src="https://cdn.tailwindcss.com"></script>
      <style>
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }
        .stat-card { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        .stat-value { font-size: 32px; font-weight: bold; }
        .stat-label { color: #666; font-size: 14px; margin-top: 8px; }
        .progress-bar { height: 24px; background: #e0e0e0; border-radius: 12px; overflow: hidden; }
        .progress-fill { height: 100%; border-radius: 12px; transition: width 0.3s ease; }
        .chart-bar { background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%); border-radius: 8px; color: white; text-align: center; padding: 8px; font-size: 14px; }
      </style>
    </head>
    <body class="bg-gray-50 min-h-screen">
      <header class="bg-white shadow-sm sticky top-0 z-10">
        <div class="max-w-6xl mx-auto px-4 py-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-4">
              <button onclick="navigateTo('home')" class="text-gray-500 hover:text-gray-700">← 返回</button>
              <h1 class="text-xl font-bold text-gray-800">统计分析</h1>
            </div>
          </div>
        </div>
      </header>

      <main class="max-w-6xl mx-auto px-4 py-8">
        <div class="bg-white rounded-xl shadow-sm p-4 mb-6">
          <div class="flex items-center gap-4">
            <label class="font-medium text-gray-700">选择家族：</label>
            <select id="familySelector" class="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none">
              <option value="">全部家族汇总</option>
            </select>
          </div>
        </div>

        <div id="statsContent" class="space-y-6">
          <div class="text-center py-12 text-gray-500">加载中...</div>
        </div>
      </main>

      <script>
        const currentFamilyId = '${familyId || ''}';

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
              await loadStatistics(currentFamilyId);
            } else {
              await loadStatistics();
            }
          } catch (error) {
            console.error('Failed to load families:', error);
          }
        }

        document.getElementById('familySelector').addEventListener('change', async function(e) {
          var selectedFamilyId = e.target.value;
          await loadStatistics(selectedFamilyId || undefined);
        });

        async function loadStatistics(familyId) {
          try {
            var result = await mcpCallTool('getStatistics', { familyId: familyId || '' });
            var stats = result.statistics;
            renderStats(stats);
          } catch (error) {
            console.error('Failed to load statistics:', error);
            document.getElementById('statsContent').innerHTML = '<div class="text-center py-12 text-gray-500">加载失败</div>';
          }
        }

        function renderStats(stats) {
          var total = stats.totalMembers;
          var malePercent = total > 0 ? Math.round((stats.maleCount / total) * 100) : 0;
          var femalePercent = total > 0 ? Math.round((stats.femaleCount / total) * 100) : 0;

          var ageDistribution = stats.ageDistribution;
          var ageKeys = ['0-18', '19-30', '31-50', '51-70', '71+'];
          var maxAgeCount = Math.max(...Object.values(ageDistribution));

          document.getElementById('statsContent').innerHTML = \`
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div class="stat-card">
                <div class="stat-value text-green-600">\${stats.totalMembers}</div>
                <div class="stat-label">家族成员总数</div>
              </div>
              <div class="stat-card">
                <div class="stat-value text-blue-600">\${stats.maleCount}</div>
                <div class="stat-label">男性成员</div>
              </div>
              <div class="stat-card">
                <div class="stat-value text-pink-600">\${stats.femaleCount}</div>
                <div class="stat-label">女性成员</div>
              </div>
              <div class="stat-card">
                <div class="stat-value text-purple-600">\${stats.averageAge}</div>
                <div class="stat-label">平均年龄</div>
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="stat-card">
                <h3 class="text-lg font-semibold mb-4">性别比例</h3>
                <div class="space-y-4">
                  <div>
                    <div class="flex justify-between text-sm mb-2">
                      <span>男性</span>
                      <span>\${malePercent}%</span>
                    </div>
                    <div class="progress-bar">
                      <div class="progress-fill" style="width: \${malePercent}%; background: #3B82F6;"></div>
                    </div>
                  </div>
                  <div>
                    <div class="flex justify-between text-sm mb-2">
                      <span>女性</span>
                      <span>\${femalePercent}%</span>
                    </div>
                    <div class="progress-bar">
                      <div class="progress-fill" style="width: \${femalePercent}%; background: #EC4899;"></div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="stat-card">
                <h3 class="text-lg font-semibold mb-4">在世状态</h3>
                <div class="space-y-4">
                  <div>
                    <div class="flex justify-between text-sm mb-2">
                      <span>在世</span>
                      <span>\${stats.aliveCount} 人</span>
                    </div>
                    <div class="progress-bar">
                      <div class="progress-fill" style="width: \${total > 0 ? (stats.aliveCount/total)*100 : 0}%; background: #4CAF50;"></div>
                    </div>
                  </div>
                  <div>
                    <div class="flex justify-between text-sm mb-2">
                      <span>已故</span>
                      <span>\${stats.deceasedCount} 人</span>
                    </div>
                    <div class="progress-bar">
                      <div class="progress-fill" style="width: \${total > 0 ? (stats.deceasedCount/total)*100 : 0}%; background: #9E9E9E;"></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="stat-card">
              <h3 class="text-lg font-semibold mb-4">年龄分布</h3>
              <div class="grid grid-cols-5 gap-4">
                \${ageKeys.map(key => {
                  var count = ageDistribution[key] || 0;
                  var height = maxAgeCount > 0 ? (count / maxAgeCount) * 100 : 0;
                  return \`
                    <div class="text-center">
                      <div style="height: 120px; display: flex; flex-direction: column-reverse; margin-bottom: 8px;">
                        <div class="chart-bar" style="height: \${height}%;">
                          \${count}
                        </div>
                      </div>
                      <div class="text-sm text-gray-600">\${key}</div>
                    </div>
                  \`;
                }).join('')}
              </div>
            </div>

            <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
              <div class="stat-card">
                <div class="stat-value text-orange-600">\${stats.generationCount}</div>
                <div class="stat-label">家族代数</div>
              </div>
              <div class="stat-card">
                <div class="stat-value text-green-600">\${stats.aliveCount}</div>
                <div class="stat-label">在世成员</div>
              </div>
              <div class="stat-card">
                <div class="stat-value text-gray-600">\${stats.deceasedCount}</div>
                <div class="stat-label">已故成员</div>
              </div>
            </div>
          \`;
        }

        document.addEventListener('DOMContentLoaded', function() {
          loadFamilies();
        });
      </script>
    </body>
    </html>
  `;
}