import api from "./axios";

export const getAllReviews = () => api.get("/reviews");
export const getMyReviews = () => api.get("/reviews/my");
export const getPublishedReviews = () => api.get("/reviews/published");
export const getPendingReviews = () => api.get("/reviews/pending");
export const createReview = (data) => api.post("/reviews", data);
export const approveReview = (id) => api.put(`/reviews/${id}/approve`);
export const rejectReview = (id) => api.put(`/reviews/${id}/reject`);
export const deleteReview = (id) => api.delete(`/reviews/${id}`);
