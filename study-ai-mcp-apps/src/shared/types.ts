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

export interface Card {
  id: string;
  title: string;
  description: string;
  status: string;
  priority: 'low' | 'medium' | 'high' | 'urgent';
  assignee?: string;
  tags: string[];
  createdAt: Date;
  updatedAt: Date;
  dueDate?: Date;
}

export interface Column {
  id: string;
  title: string;
  status: string;
  color: string;
  cardIds: string[];
}

export interface Board {
  id: string;
  title: string;
  columns: Column[];
  cards: Card[];
}