export default function SortIcon({active,dir}) {
  return <span className="dir" aria-hidden>{active ? (dir==='asc' ? '▲' : '▼') : '↕'}</span>
}
