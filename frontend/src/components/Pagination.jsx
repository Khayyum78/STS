export default function Pagination({page,size,totalPages,onPage,onSize}) {
  return (
    <div className="pagination">
      <button className="page-btn" onClick={()=>onPage(0)} disabled={page===0}>⏮</button>
      <button className="page-btn" onClick={()=>onPage(page-1)} disabled={page===0}>‹</button>
      <span className="badge">Page {page+1} / {Math.max(totalPages,1)}</span>
      <button className="page-btn" onClick={()=>onPage(page+1)} disabled={page+1>=totalPages}>›</button>
      <button className="page-btn" onClick={()=>onPage(totalPages-1)} disabled={page+1>=totalPages}>⏭</button>
      <select className="select" value={size} onChange={e=>onSize(Number(e.target.value))}>
        {[5,10,20,50].map(s=><option key={s} value={s}>{s}/page</option>)}
      </select>
    </div>
  )
}
