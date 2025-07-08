import API_BASE_URL from './config';

// 统一响应格式接口
interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

/**
 * 通用请求函数
 * @param endpoint API端点路径
 * @param options fetch选项
 * @returns 响应数据中的data字段
 */
export const request = async <T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> => {
  const url = `${API_BASE_URL}${endpoint}`;
  const response = await fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
  });

  // 解析JSON响应
  const result: ApiResponse<T> = await response.json();

  // 统一错误处理
  if (result.code !== 200) {
    throw new Error(result.message || '请求异常');
  }

  return result.data;
};