export function formatCurrency(value) {
  const n = Number(value)
  if (Number.isNaN(n)) return '—'
  return n.toLocaleString('es-EC', { style: 'currency', currency: 'USD' })
}

export function formatDate(value) {
  if (!value) return '—'
  const [year, month, day] = value.split('-')
  if (!year || !month || !day) return value
  return `${day}/${month}/${year}`
}

export const ESTADOS = ['PENDIENTE', 'CONFIRMADA', 'CANCELADA']

export const ESTADO_STYLES = {
  PENDIENTE: 'bg-amber-50 text-amber-700 ring-1 ring-amber-200',
  CONFIRMADA: 'bg-brand-50 text-brand-700 ring-1 ring-brand-100',
  CANCELADA: 'bg-rose-50 text-rose-700 ring-1 ring-rose-200',
}

export const ESTADO_LABELS = {
  PENDIENTE: 'Pendiente',
  CONFIRMADA: 'Confirmada',
  CANCELADA: 'Cancelada',
}
