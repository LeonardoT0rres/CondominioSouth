import { AuthValues } from '../types';
import UseApiMock from './UseApiMock';

export function loginPost(data: AuthValues) {
  return UseApiMock.postMockLogin(
    '/accounts:signInWithPassword?key=AIzaSyAyL3rowoU_cSIwADdQgRlLXLhKFBfbOxY',
    data
  );
}

export function registerPost(data: AuthValues) {
  return UseApiMock.postMockSignUp(
    '/accounts:signUp?key=AIzaSyAyL3rowoU_cSIwADdQgRlLXLhKFBfbOxY',
    data
  );
}

