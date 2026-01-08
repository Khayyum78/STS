import api from "./axios";

// Ürüne göre liste
export const getVariantsByProduct = (productId) =>
  api.get(`/variants/by-product/${productId}`);

// Tekli oluşturma
export const createVariant = (payload) =>
  api.post(`/variants`, payload);

// Toplu oluşturma
export const bulkCreateVariants = (payload) =>
  api.post(`/variants/bulk`, payload);

// Güncelle
export const updateVariant = (id, payload) =>
  api.put(`/variants/${id}`, payload);

// Sil
export const deleteVariant = (id) =>
  api.delete(`/variants/${id}`);
