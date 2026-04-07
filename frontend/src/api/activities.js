import api from "./axios";

export const getMyActivities = () => api.get("/activities/my");
export const createActivity = (data) => api.post("/activities", data);
