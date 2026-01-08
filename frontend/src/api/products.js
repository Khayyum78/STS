import api from "./axios";

export const getProducts = () => api.get("/products");
