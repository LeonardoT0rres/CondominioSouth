import { AxiosRequestConfig } from 'axios';
import { api, apiMockLogin } from './api';

function get(url: string) {
  return api.get(url);
}

function post<T>(url: string, data: T) {
  return api.post(url, data);
}

function postLogin<T>(url: string, data: T, config: AxiosRequestConfig) {
  return apiMockLogin.post(url, data, config);
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
  postLogin,
};
