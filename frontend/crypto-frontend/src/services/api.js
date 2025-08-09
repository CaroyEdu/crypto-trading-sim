import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const getAccount = (publicId) =>
  axios.get(`${API_URL}/accounts/${publicId}`);

export const getTransactions = (publicId) =>
  axios.get(`${API_URL}/transactions/account/${publicId}`);

export const createTransaction = (payload) =>
  axios.post(`${API_URL}/transactions/create`, null, { params: payload });

export const resetAccount = (publicId) =>
  axios.post(`${API_URL}/accounts/${publicId}/reset`);

export const getPortfolio = (accountPublicId) =>
  axios.get(`${API_URL}/portfolio/account/${accountPublicId}`);
