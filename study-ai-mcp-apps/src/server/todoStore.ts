import { TodoItem } from '../shared/types';

class TodoStore {
  private todos: TodoItem[] = [];

  getAll(): TodoItem[] {
    return this.todos;
  }

  add(text: string): TodoItem {
    const todo: TodoItem = {
      id: crypto.randomUUID(),
      text,
      completed: false,
      createdAt: new Date(),
    };
    this.todos.push(todo);
    return todo;
  }

  toggle(id: string): TodoItem {
    const todo = this.todos.find(t => t.id === id);
    if (todo) {
      todo.completed = !todo.completed;
    }
    return todo!;
  }

  delete(id: string): string {
    const index = this.todos.findIndex(t => t.id === id);
    if (index !== -1) {
      const todo = this.todos[index];
      this.todos.splice(index, 1);
      return todo.text;
    }
    return '';
  }

  clearCompleted(): void {
    this.todos = this.todos.filter(t => !t.completed);
  }
}

export const todoStore = new TodoStore();
