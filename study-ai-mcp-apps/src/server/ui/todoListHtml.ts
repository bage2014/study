export const todoListHtml = `
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Todo List</title>
  <style>
    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background: #f5f5f5; }
    .todo-container { background: white; border-radius: 12px; padding: 24px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
    h1 { color: #1a1a1a; margin: 0 0 20px 0; font-size: 24px; }
    .input-row { display: flex; gap: 10px; margin-bottom: 20px; }
    input { flex: 1; padding: 12px; border: 1px solid #e0e0e0; border-radius: 8px; font-size: 16px; outline: none; transition: border-color 0.2s; }
    input:focus { border-color: #6366f1; }
    button { padding: 12px 20px; background: #6366f1; color: white; border: none; border-radius: 8px; cursor: pointer; font-size: 16px; transition: background 0.2s; }
    button:hover { background: #4f46e5; }
    button.delete { background: #ef4444; }
    button.delete:hover { background: #dc2626; }
    .todo-list { list-style: none; padding: 0; margin: 0; }
    .todo-item { display: flex; align-items: center; padding: 12px; border-bottom: 1px solid #f0f0f0; transition: background 0.2s; }
    .todo-item:last-child { border-bottom: none; }
    .todo-item:hover { background: #fafafa; }
    .todo-item.completed .text { text-decoration: line-through; color: #9ca3af; }
    input[type="checkbox"] { margin-right: 12px; width: 18px; height: 18px; cursor: pointer; }
    .text { flex: 1; font-size: 16px; color: #374151; }
    .empty { text-align: center; color: #9ca3af; padding: 40px 20px; }
  </style>
</head>
<body>
  <div class="todo-container">
    <h1>Todo List</h1>
    <div class="input-row">
      <input type="text" id="newTodo" placeholder="Add a new todo..." />
      <button onclick="addTodo()">Add</button>
    </div>
    <ul class="todo-list" id="todoList">
    </ul>
  </div>

  <script>
    function renderTodos(todos) {
      const list = document.getElementById('todoList');
      if (todos.length === 0) {
        list.innerHTML = '<li class="empty">No todos yet. Add one above!</li>';
        return;
      }
      list.innerHTML = todos.map(function(todo) {
        return '<li class="todo-item ' + (todo.completed ? 'completed' : '') + '">' +
          '<input type="checkbox" ' + (todo.completed ? 'checked' : '') + ' onchange="toggleTodo(\\'' + todo.id + '\\')" />' +
          '<span class="text">' + todo.text + '</span>' +
          '<button class="delete" onclick="deleteTodo(\\'' + todo.id + '\\')">Delete</button>' +
          '</li>';
      }).join('');
    }

    async function mcpCallTool(toolName, params) {
      var response = await fetch('http://localhost:3000/mcp', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json, text/event-stream',
        },
        body: JSON.stringify({
          jsonrpc: '2.0',
          id: Date.now(),
          method: 'tools/call',
          params: {
            name: toolName,
            arguments: params,
          },
        }),
      });
      
      var text = await response.text();
      var dataMatch = text.match(/data:\s*(.+)/);
      if (!dataMatch) {
        throw new Error('Invalid response format');
      }
      
      var data = JSON.parse(dataMatch[1]);
      
      if (data.error) {
        throw new Error(data.error.message);
      }
      
      return data.result;
    }

    async function addTodo() {
      const input = document.getElementById('newTodo');
      const text = input.value.trim();
      if (!text) return;
      
      try {
        await mcpCallTool('addTodo', { text });
        input.value = '';
        await loadTodos();
      } catch (error) {
        console.error('Failed to add todo:', error);
      }
    }

    async function toggleTodo(id) {
      try {
        await mcpCallTool('toggleTodo', { id });
        await loadTodos();
      } catch (error) {
        console.error('Failed to toggle todo:', error);
      }
    }

    async function deleteTodo(id) {
      try {
        await mcpCallTool('deleteTodo', { id });
        await loadTodos();
      } catch (error) {
        console.error('Failed to delete todo:', error);
      }
    }

    var isLoading = false;
    
    async function loadTodos() {
      if (isLoading) return;
      isLoading = true;
      try {
        const result = await mcpCallTool('listTodos', {});
        let todos = [];
        if (result && result.content) {
          const textContent = result.content.find(function(c) { return c.type === 'text'; });
          if (textContent && textContent.text) {
            todos = JSON.parse(textContent.text);
          }
        }
        renderTodos(todos);
      } catch (error) {
        console.error('Failed to load todos:', error);
      } finally {
        isLoading = false;
      }
    }

    document.getElementById('newTodo').addEventListener('keypress', function(e) {
      if (e.key === 'Enter') addTodo();
    });

    loadTodos();
  </script>
</body>
</html>
`;