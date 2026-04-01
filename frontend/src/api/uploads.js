import api from "./axios";

export const uploadImage = (file) => {
  const formData = new FormData();
  formData.append("file", file);

  return api.post("/uploads/images", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};
