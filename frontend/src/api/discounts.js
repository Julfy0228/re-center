import api from "./axios";

export const getDiscounts = () => api.get("/discounts");
export const getActiveDiscounts = () => api.get("/discounts/active");
export const createDiscount = (data) => api.post("/discounts", data);
export const updateDiscount = (id, data) => api.put(`/discounts/${id}`, data);
export const deleteDiscount = (id) => api.delete(`/discounts/${id}`);
