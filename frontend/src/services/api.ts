import axios from 'axios';

export const api = axios.create({
  baseURL: 'http://localhost:8081',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const apiMockLogin = axios.create({
  baseURL: 'http://localhost:8081',
  headers: {
    'Content-Type': 'application/json',
  },
});
// Firebase: https://identitytoolkit.googleapis.com/v1/

// Endpoint back: /oauth/token

api.interceptors.request.use(async (request: any) => {
  const Authorization = 'Bearer ' + localStorage.getItem('token');
  request.headers = {
    ...request.headers,
    Authorization
  };
  return request;
});
