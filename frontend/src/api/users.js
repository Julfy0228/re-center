import api from "./axios";

export const getMyProfile = () => api.get("/users/my/profile");
export const updateUser = (id, data) => api.put(`/users/${id}`, data);
export const getUsers = () => api.get("/users");
