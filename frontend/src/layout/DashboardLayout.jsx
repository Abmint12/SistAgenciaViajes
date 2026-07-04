import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";

export default function DashboardLayout() {
  return (
    <div className="flex h-screen bg-page">

      <Sidebar backendOnline={true} />

      <main className="flex-1 overflow-y-auto bg-page">
        <Outlet />
      </main>

    </div>
  );
}