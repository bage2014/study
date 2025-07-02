import API_BASE_URL from './config';

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
  const response = await fetch(`${API_BASE_URL}login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password, captcha }),
  });

  if (!response.ok) {
    throw new Error('Login failed');
  }

  return response.json();
};

export const register = async (email: string, password: string, captcha: string): Promise<RegisterResponse> => {
  const response = await fetch(`${API_BASE_URL}register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password, captcha }),
  });

  if (!response.ok) {
    throw new Error('Registration failed');
  }

  return response.json();
};