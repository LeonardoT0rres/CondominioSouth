import { api, apiMockLogin, apiMockSignUp } from './api';

function get(url: string) {
  return api.get(url);
}

function post<T>(url: string, data: T) {
  return api.post(url, data);
}

function postMockLogin<T>(url: string, data: T) {
  return apiMockLogin.post(url, data);
}

function postMockSignUp<T>(url: string, data: T) {
  return apiMockSignUp.post(url, data);
}

function put<T>(url: string, data: T) {
  return api.put(url, data);
}
function patch<T>(url: string, data: T) {
  return api.patch(url, data);
}

function deleteData(url: string) {
  return api.delete(url);
}

export default {
  patch,
  get,
  post,
  put,
  deleteData,
  postMockLogin,
  postMockSignUp,
};
