import { STORAGE_KEYS } from '../../utils/storageKeys';
import router from '../../router'; // 导入router实例
import type {
  HttpRequestConfig,
  HttpResponse,
  HttpError,
  HttpServiceType
} from './types';

class HttpService implements HttpServiceType {
  private baseURL: string;
  private timeout: number;
  private defaultHeaders: Record<string, string>;

  constructor() {
    // 从环境变量获取 baseURL，支持多环境配置
    this.baseURL = import.meta.env.VITE_API_BASE_URL || '/api';
    this.timeout = 30000;
    this.defaultHeaders = {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    };
  }

  // 添加缺失的getCaptchaImageURL方法
  public getCaptchaImageURL(requestId?: string): string {
    const baseUrl = this.baseURL.replace(/\/+$/, '');
    const query = requestId ? `?requestId=${requestId}` : '';
    return `${baseUrl}/captcha/image${query}`;
  }

  // 动态切换环境的方法
  public setEnvironment(env: 'development' | 'production' | 'test' | 'mock'): void {
    const envConfigs = {
      development: '/',
      production: 'https://api.yourdomain.com',
      test: 'https://api.test.yourdomain.com',
      mock: 'https://api.mock.yourdomain.com'
    };
    
    this.baseURL = envConfigs[env];
    console.log(`已切换到${env}环境，API Base URL: ${this.baseURL}`);
  }

  // 请求拦截器
  private requestInterceptor(config: HttpRequestConfig): HttpRequestConfig {
    // 添加认证 token
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
    if (token) {
      config.headers = {
        ...config.headers,
        'Authorization': `${token}`
      };
    }
    return config;
  }

  // 响应拦截器
  private responseInterceptor<T>(response: HttpResponse<T>): T {
    return response.data;
  }

  // 错误处理
  // 修复HttpError类型定义
  private handleError(error: any) {
    let errorMessage = '网络请求失败，请稍后重试';
    let errorCode = 'NETWORK_ERROR';

    if (error.response) {
      errorCode = error.response.status.toString();
      errorMessage = error.response.data?.message || error.response.statusText || errorMessage;
      
      if (error.response.status === 401) {
        localStorage.removeItem(STORAGE_KEYS.TOKEN);
        router.push({
          name: 'login',
          query: { redirect: window.location.pathname }
        });
      }
    } else if (error.request) {
      errorMessage = '服务器无响应，请检查网络连接';
    }

    // 修复HttpError类型
    const customError = new Error(errorMessage) as HttpError;
    customError.config = error.config || {};
    customError.response = error.response;
    customError.request = error.request;
    customError.code = errorCode;

    throw customError;
  }

  // 构建 URL
  private buildURL(url: string, params?: Record<string, any>): string {
    if (url?.startsWith('/')) {
      url = url.slice(1);
    }
    
    if (!params) return `${this.baseURL}${url}`;
    
    const searchParams = new URLSearchParams();
    for (const [key, value] of Object.entries(params)) {
      if (value !== undefined && value !== null) {
        searchParams.append(key, String(value));
      }
    }

    const queryString = searchParams.toString();
    return queryString ? `${this.baseURL}${url}?${queryString}` : `${this.baseURL}${url}`;
  }

  // 发送请求
  private async request<T = any>(config: HttpRequestConfig): Promise<HttpResponse<T>> {
    // 应用请求拦截器
    const processedConfig = this.requestInterceptor({
      ...config,
      headers: {
        ...this.defaultHeaders,
        ...config.headers
      },
      timeout: config.timeout || this.timeout
    });

    // 构建 URL
    const url = this.buildURL(processedConfig.url, processedConfig.params);

    // 创建 AbortController 处理超时
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), processedConfig.timeout);

    try {
      const response = await fetch(url, {
        method: processedConfig.method || 'GET',
        headers: processedConfig.headers,
        body: processedConfig.data !== undefined && processedConfig.method !== 'GET' && processedConfig.method !== 'HEAD'
          ? JSON.stringify(processedConfig.data)
          : undefined,
        signal: controller.signal,
        credentials: processedConfig.withCredentials ? 'include' : 'same-origin'
      });

      clearTimeout(timeoutId);

      if (!response.ok) {
        throw {
          config: processedConfig,
          response,
          request: null
        };
      }

      // 根据 responseType 解析响应
      let data: T;
      if (processedConfig.responseType === 'json' || !processedConfig.responseType) {
        data = await response.json();
      } else if (processedConfig.responseType === 'text') {
        data = (await response.text()) as unknown as T;
      } else if (processedConfig.responseType === 'blob') {
        data = (await response.blob()) as unknown as T;
      } else if (processedConfig.responseType === 'arraybuffer') {
        data = (await response.arrayBuffer()) as unknown as T;
      } else {
        data = await response.text() as unknown as T;
      }

      const responseHeaders: Record<string, string> = {};
      response.headers.forEach((value, key) => {
        responseHeaders[key] = value;
      });

      return {
        data,
        status: response.status,
        statusText: response.statusText,
        headers: responseHeaders,
        config: processedConfig,
        request: null
      };
    } catch (error) {
      clearTimeout(timeoutId);
      throw this.handleError(error);
    }
  }

  // GET 请求
  public async get<T = any>(url: string, config?: Omit<HttpRequestConfig, 'url' | 'method'>): Promise<T> {
    const response = await this.request<T>({
      url,
      method: 'GET',
      ...config
    });
    return response.data;
  }

  public async post<T = any>(url: string, data?: any, config?: Omit<HttpRequestConfig, 'url' | 'method' | 'data'>): Promise<T> {
    const response = await this.request<T>({
      url,
      method: 'POST',
      data,
      ...config
    });
    return response.data;
  }

  public async put<T = any>(url: string, data?: any, config?: Omit<HttpRequestConfig, 'url' | 'method' | 'data'>): Promise<T> {
    const response = await this.request<T>({
      url,
      method: 'PUT',
      data,
      ...config
    });
    return response.data;
  }

  public async delete<T = any>(url: string, config?: Omit<HttpRequestConfig, 'url' | 'method'>): Promise<T> {
    const response = await this.request<T>({
      url,
      method: 'DELETE',
      ...config
    });
    return response.data;
  }

  public async patch<T = any>(url: string, data?: any, config?: Omit<HttpRequestConfig, 'url' | 'method' | 'data'>): Promise<T> {
    const response = await this.request<T>({
      url,
      method: 'PATCH',
      data,
      ...config
    });
    return response.data;
  }

  public async upload<T = any>(url: string, file: File, data?: Record<string, any>, config?: Omit<HttpRequestConfig, 'url' | 'method' | 'data'>): Promise<T> {
    const formData = new FormData();
    formData.append('file', file);
    
    if (data) {
      Object.entries(data).forEach(([key, value]) => {
        formData.append(key, String(value));
      });
    }

    const response = await this.request<T>({
      url,
      method: 'POST',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      ...config
    });
    return response.data;
  }

  // 添加rebuildURL方法
  public rebuildURL(url: string): string {
    if (url?.startsWith('/')) {
      url = url.slice(1);
    }
    return `${this.baseURL}${url}`;
  }
}

export default new HttpService();