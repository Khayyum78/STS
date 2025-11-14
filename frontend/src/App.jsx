import { useEffect, useMemo, useState } from 'react'
import { listPaged, createProduct, updateProduct, deleteProduct, attachErrorInterceptor } from './api'
import Pagination from './components/Pagination'
import SortIcon from './components/SortIcon'
import ProductForm from './components/ProductForm'
import './styles.css'

export default function App(){
  const [items,setItems] = useState([])
  const [page,setPage] = useState(0)
  const [size,setSize] = useState(10)
  const [totalPages,setTotalPages] = useState(1)
  const [q,setQ] = useState('')
  const [sort,setSort] = useState('id')
  const [dir,setDir] = useState('asc')
  const [editing,setEditing] = useState(null)
  const [toast,setToast] = useState(null)
  const [loading,setLoading] = useState(false)

  useEffect(()=>{
    attachErrorInterceptor((msg)=>{
      setToast(msg || 'Request failed')
      setTimeout(()=>setToast(null), 1800)
      setLoading(false)
    })
  },[])

  const params = useMemo(()=>({page,size,sort,dir,q:q||undefined}),[page,size,sort,dir,q])

  const load = async ()=>{
    setLoading(true)
    const {data} = await listPaged(params)
    setItems(data.items)
    setTotalPages(data.totalPages || 1)
    setLoading(false)
  }
  useEffect(()=>{ load() },[params])

  const toggleSort = (key)=>{
    if (sort===key) setDir(dir==='asc'?'desc':'asc')
    else { setSort(key); setDir('asc') }
  }

  const onCreateOrUpdate = async (form)=>{
    setLoading(true)
    if (form.id) {
      await updateProduct(form.id, form)
      setToast('Product updated')
    } else {
      await createProduct(form)
      setToast('Product added')
    }
    setEditing(null)
    await load()
    setTimeout(()=>setToast(null), 1500)
  }

  const onDelete = async (id)=>{
    if (!confirm('Silmek istediğine emin misin?')) return
    setLoading(true)
    await deleteProduct(id)
    setToast('Product deleted')
    await load()
    setTimeout(()=>setToast(null), 1500)
  }

  return (
    <div className="container">
      <div className="card">
        <div className="header">
          <div>
            <div className="title">Inventory</div>
            <div className="badge">Clean Architecture • React UI</div>
          </div>
          <div className="toolbar">
            <input className="input" placeholder="Search name/sku…" value={q} onChange={e=>{setPage(0);setQ(e.target.value)}} />
            <button className="btn btn-ghost" onClick={()=>{setQ('');setPage(0)}}>Clear</button>
            <button className="btn btn-primary" onClick={()=>setEditing({})}>+ New</button>
          </div>
        </div>

        {editing && (
          <ProductForm
            initial={editing.id ? editing : null}
            onSubmit={onCreateOrUpdate}
            onCancel={()=>setEditing(null)}
          />
        )}

        <div style={{overflowX:'auto', padding:'0 16px 8px'}}>
          <table className="table">
            <thead>
              <tr>
                <th className="sort" onClick={()=>toggleSort('id')}>ID <SortIcon active={sort==='id'} dir={dir} /></th>
                <th className="sort" onClick={()=>toggleSort('name')}>Name <SortIcon active={sort==='name'} dir={dir} /></th>
                <th className="sort" onClick={()=>toggleSort('sku')}>SKU <SortIcon active={sort==='sku'} dir={dir} /></th>
                <th className="sort" onClick={()=>toggleSort('quantity')} style={{textAlign:'right'}}>Qty <SortIcon active={sort==='quantity'} dir={dir} /></th>
                <th className="sort" onClick={()=>toggleSort('price')} style={{textAlign:'right'}}>Price <SortIcon active={sort==='price'} dir={dir} /></th>
                <th style={{width:160}}></th>
              </tr>
            </thead>
            <tbody>
              {items.map(p=>(
                <tr className="row" key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.name}</td>
                  <td className="badge">{p.sku}</td>
                  <td style={{textAlign:'right'}}>{p.quantity}</td>
                  <td style={{textAlign:'right'}}>{Number(p.price||0).toFixed(2)}</td>
                  <td>
                    <div className="actions">
                      <button className="btn btn-ghost" onClick={()=>setEditing(p)}>Edit</button>
                      <button className="btn btn-danger" onClick={()=>onDelete(p.id)}>Delete</button>
                    </div>
                  </td>
                </tr>
              ))}
              {!items.length && !loading && (
                <tr><td colSpan="6" style={{padding:18,color:'var(--text-dim)'}}>No data found.</td></tr>
              )}
            </tbody>
          </table>
        </div>

        <Pagination
          page={page} size={size} totalPages={totalPages}
          onPage={(p)=>setPage(Math.max(0,p))}
          onSize={(s)=>{setSize(s);setPage(0)}}
        />
      </div>

      {toast && <div className="toast">{toast}</div>}
      {loading && <div className="overlay"><div className="spinner"/></div>}
    </div>
  )
}
