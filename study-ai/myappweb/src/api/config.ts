const isDev = process.env.NODE_ENV === 'development';

const BASE_URL = {
  dev: 'http://localhost:8080/',
  pro: 'http://localhost:8080/'
};

const API_BASE_URL = isDev ? BASE_URL.dev : BASE_URL.pro;

export default API_BASE_URL;