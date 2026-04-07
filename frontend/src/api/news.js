import api from "./axios";

export const getPublishedNews = () => api.get("/news/published");
export const createNews = (data) => api.post("/news", data);
export const getAllNews = () => api.get("/news");
export const updateNews = (id, data) => api.put(`/news/${id}`, data);
export const deleteNews = (id) => api.delete(`/news/${id}`);
