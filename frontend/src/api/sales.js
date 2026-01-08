import axios from "axios";

// POST /api/sales
export const createSale = (payload) => axios.post("/api/sales", payload);

// GET /api/sales/{id}
export const getSaleById = (id) => axios.get(`/api/sales/${id}`);
