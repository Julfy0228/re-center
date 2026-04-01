import api from "./axios";

export const getPublishedNews = () => api.get("/news/published");
export const createNews = (data) => api.post("/news", data);
