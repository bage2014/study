export function generateTestHtml(): string {
  return `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>Test</title>
</head>
<body>
  <div id="appData" style="display:none" data-value="test"></div>
  <h1>Test Page</h1>
  <script>
    var appData = document.getElementById('appData');
    var value = appData ? appData.getAttribute('data-value') : '';
    console.log('Value:', value);
    document.body.innerHTML += '<div>Loaded with value: ' + value + '</div>';
  </script>
</body>
</html>`;
}
