export interface TodoItem {
  id: string;
  text: string;
  completed: boolean;
  createdAt: Date;
}

export interface TodoListResponse {
  todos: TodoItem[];
}

export interface AddTodoRequest {
  text: string;
}

export interface ToggleTodoRequest {
  id: number;
}

export interface DeleteTodoRequest {
  id: number;
}