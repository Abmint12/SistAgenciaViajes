export default function StatCard({ label, value }) {
  return (
    <div className="flex-1 rounded-2xl border border-slate-200 bg-white px-6 py-5">
      <p className="text-sm text-slate-500">{label}</p>
      <p className="mt-2 text-3xl font-bold text-navy-950">{value}</p>
    </div>
  )
}
