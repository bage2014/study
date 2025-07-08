import API_BASE_URL from './config';
import { request } from './request';

interface LoginResponse {
  token: string;
  user: {
    id: number;
    email: string;
  };
}

interface RegisterResponse {
  message: string;
}

export const login = async (email: string, password: string, captcha: string): Promise<LoginResponse> => {
  return request<LoginResponse>('/login', {
    method: 'POST',
    body: JSON.stringify({ email, password, captcha }),
  });
};

export const register = async (email: string, password: string, captcha: string): Promise<RegisterResponse> => {
  return request<RegisterResponse>('/register', {
    method: 'POST',
    body: JSON.stringify({ email, password, captcha }),
  });
};

export const sendVerificationCode = async (email: string, captcha: string): Promise<{ message: string }> => {
  return request<{ message: string }>('/send-verification-code', {
    method: 'POST',
    body: JSON.stringify({ email, captcha }),
  });
};
