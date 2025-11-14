import { useEffect, useState } from 'react'
const empty = { id:null, name:'', sku:'', quantity:0, price:0 }

export default function ProductForm({initial, onSubmit, onCancel}) {
  const [form,setForm] = useState(empty)
  useEffect(()=>{ setForm(initial ?? empty) },[initial])

  const change = e=>{
    const {name,value} = e.target
    setForm(f=>({...f,[name]: name==='quantity'||name==='price' ? Number(value) : value }))
  }

  const submit = e=>{
    e.preventDefault()
    if(!form.name || !form.sku) return alert('Name ve SKU zorunlu')
    onSubmit(form)
  }

  return (
    <form className="form" onSubmit={submit}>
      <input className="input" placeholder="Name" name="name" value={form.name} onChange={change} />
      <input className="input" placeholder="SKU" name="sku" value={form.sku} onChange={change} />
      <input className="input" type="number" placeholder="Quantity" name="quantity" value={form.quantity} onChange={change} />
      <input className="input" type="number" step="0.01" placeholder="Price" name="price" value={form.price} onChange={change} />
      <div className="full" style={{display:'flex', gap:8, justifyContent:'flex-end'}}>
        <button className="btn btn-ghost" type="button" onClick={onCancel}>Cancel</button>
        <button className="btn btn-primary" type="submit">{form.id ? 'Update' : 'Add'}</button>
      </div>
    </form>
  )
}
