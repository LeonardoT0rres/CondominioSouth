import { AuthValues } from '../types';
import UseApiMock from './UseApiMock';

export function loginPost(data: AuthValues) {
  return UseApiMock.postLogin(
    "oauth/token",
    new URLSearchParams({
      grant_type: "password",
      "client-id": "ds-easy-vote-api",
      username: data.email,
      password: data.password,
      "client-secret": "ds-easy-vote-api-xpto",
    }),
    {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Authorization: `Basic ${btoa(
          `ds-easy-vote-api:ds-easy-vote-api-xpto`
        )}`,
      },
    }
  );
}

/**
 * headers: {
      authorization: 'Basic ' + btoa(`ds-easy-vote-api:ds-easy-vote-api-xpto`),
      grant_type: 'password',
      username: data.email,
      password: data.password,
    },
 */

// client-id: ds-easy-vote-api
// client-secret: ds-easy-vote-api-xpto
//'Basic ' + atob(${email}:${senha})

// export function registerPost(data: AuthValues) {
//   return UseApiMock.postMockSignUp(
//     '/accounts:signUp?key=AIzaSyAyL3rowoU_cSIwADdQgRlLXLhKFBfbOxY',
//     data
//   );
// }
