import api from "./axios";

export const getMyBookings = () => api.get("/bookings/my");
export const createBooking = (data) => api.post("/bookings", data);
