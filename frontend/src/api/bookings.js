import api from "./axios";

export const getMyBookings = () => api.get("/bookings/my");
export const getAllBookings = () => api.get("/bookings");
export const createBooking = (data) => api.post("/bookings", data);
export const updateBooking = (id, data) => api.put(`/bookings/${id}`, data);
export const deleteBooking = (id) => api.delete(`/bookings/${id}`);
