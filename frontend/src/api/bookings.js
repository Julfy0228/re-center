import api from "./axios";

export const getMyBookings = (params = {}) => api.get("/bookings/my", { params });
export const getAllBookings = (params = {}) => api.get("/bookings", { params });
export const createBooking = (data) => api.post("/bookings", data);
export const updateBooking = (id, data) => api.put(`/bookings/${id}`, data);
export const deleteBooking = (id) => api.delete(`/bookings/${id}`);
