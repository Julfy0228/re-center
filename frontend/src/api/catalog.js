import api from "./axios";

export const getCategories = () => api.get("/categories");
export const getServices = () => api.get("/services");
export const getServicesByCategory = (categoryId) =>
  api.get(`/services/category/${categoryId}`);
export const createService = (data) => api.post("/services", data);
export const updateService = (id, data) => api.put(`/services/${id}`, data);
export const deleteService = (id) => api.delete(`/services/${id}`);
