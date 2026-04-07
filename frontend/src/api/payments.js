import api from "./axios";

export const createPayment = (data) => api.post("/payments", data);
export const getMyPayments = () => api.get("/payments/my");
export const getAllPayments = () => api.get("/payments");
export const getPendingPayments = () => api.get("/payments/pending");
export const getCompletedPayments = () => api.get("/payments/completed");
export const completePayment = (id) => api.put(`/payments/${id}/complete`);
export const deletePayment = (id) => api.delete(`/payments/${id}`);
