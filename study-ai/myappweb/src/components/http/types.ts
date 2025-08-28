// HTTP 请求方法类型
export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH' | 'HEAD' | 'OPTIONS';

// HTTP 请求配置接口 - 移除未使用的泛型参数T
export interface HttpRequestConfig {
  url: string;
  method?: HttpMethod;
  data?: any;
  params?: Record<string, string | number | boolean | null | undefined>;
  headers?: Record<string, string>;
  timeout?: number;
  responseType?: 'arraybuffer' | 'blob' | 'document' | 'json' | 'text' | 'stream';
  withCredentials?: boolean;
  [key: string]: any;
}

// HTTP 响应接口
export interface HttpResponse<T = any> {
  data: T;
  status: number;
  statusText: string;
  headers: Record<string, string>;
  config: HttpRequestConfig;
  request: any;
}

// API 响应数据接口
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

// HTTP 错误接口
export interface HttpError extends Error {
  config: HttpRequestConfig;
  response?: HttpResponse;
  request?: any;
  code?: string;
}

// HttpService 类接口定义
export interface HttpServiceType {
  // HTTP 方法请求
  get<T = any>(url: string, config?: Omit<HttpRequestConfig, 'url' | 'method'>): Promise<T>;
  post<T = any>(url: string, data?: any, config?: Omit<HttpRequestConfig, 'url' | 'method' | 'data'>): Promise<T>;
  put<T = any>(url: string, data?: any, config?: Omit<HttpRequestConfig, 'url' | 'method' | 'data'>): Promise<T>;
  delete<T = any>(url: string, config?: Omit<HttpRequestConfig, 'url' | 'method'>): Promise<T>;
  patch<T = any>(url: string, data?: any, config?: Omit<HttpRequestConfig, 'url' | 'method' | 'data'>): Promise<T>;
  
  // 文件上传方法
  upload<T = any>(url: string, file: File, data?: Record<string, any>, config?: Omit<HttpRequestConfig, 'url' | 'method' | 'data'>): Promise<T>;
  
  // 验证码图片获取方法
  getCaptchaImageURL(requestId?: string): string;
  
  // 环境切换方法
  setEnvironment(env: 'development' | 'production' | 'test' | 'mock'): void;
}

// 消息相关接口
export interface Message {
  id: number;
  senderId: number;
  receiverId: number;
  content: string;
  type: number;
  isRead: boolean;
  createTime: string;
  readTime: string | null;
  senderName: string;
  senderAvatar: string;
  receiverName: string;
  receiverAvatar: string;
}

export interface MessageResponse {
  messages: Message[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}