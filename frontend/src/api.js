import axios from 'axios'

export const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' }
})

export function attachErrorInterceptor(onError){
  api.interceptors.response.use(
    r => r,
    err => {
      const msg = err?.response?.data?.error || err?.message || 'Request failed'
      onError?.(msg)
      return Promise.reject(err)
    }
  )
}

export const listPaged = (params) => api.get('/products/paged', { params })
export const createProduct = (p) => api.post('/products', p)
export const updateProduct = (id, p) => api.put(`/products/${id}`, p)
export const deleteProduct = (id) => api.delete(`/products/${id}`)
