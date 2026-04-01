import api from "./axios";

export const getMyNotifications = () => api.get("/notifications/my");
export const getUnreadNotifications = () => api.get("/notifications/my/unread");
export const getUnreadCount = () => api.get("/notifications/my/unread/count");
export const markNotificationAsRead = (id) => api.put(`/notifications/${id}/mark-read`);
export const deleteNotification = (id) => api.delete(`/notifications/${id}`);
