import axios from "axios";

export const api = axios.create({
  baseURL: "http://localhost:8081",
  headers: {
    'Content-Type': 'application/json'
  },
});

export const apiMockLogin = axios.create({
  baseURL: "https://identitytoolkit.googleapis.com/v1/",
  headers: {
    'Content-Type' : 'application/json'
  }
});

export const apiMockSignUp = axios.create({
  baseURL: "https://identitytoolkit.googleapis.com/v1/",
  headers: {
    'Content-Type' : 'application/json'
  }
});
