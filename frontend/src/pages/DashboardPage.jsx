import { useEffect, useState } from "react";
import api from "../services/api";
import Navbar from "../components/Navbar";

import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const DashboardPage = () => {
  const [stats, setStats] = useState({
    total: 0,
    active: 0,
    inactive: 0,
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const res = await api.get("/vendors/stats");
        setStats(res.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

 const chartData = [
  { name: "Total", value: stats.totalVendors || 0 },
  { name: "Active", value: stats.activeVendors || 0 },
  { name: "Inactive", value: stats.inactiveVendors || 0 },
  { name: "Pending", value: stats.pendingVendors || 0 },
];
  return (
    <div>
      <Navbar />

      <div className="p-6">
        <h1 className="text-2xl font-bold mb-6">Dashboard</h1>

        {loading ? (
          <p>Loading...</p>
        ) : (
          <>
            {/* KPI Cards */}
            <div className="grid grid-cols-4 gap-4 mb-8">
              <div className="bg-blue-500 text-white p-4 rounded shadow">
                <h2>Total Vendors</h2>
                <p className="text-xl">{stats.totalVendors}</p>
              </div>

              <div className="bg-green-500 text-white p-4 rounded shadow">
                <h2>Active</h2>
                <p className="text-xl">{stats.activeVendors}</p>
              </div>

              <div className="bg-red-500 text-white p-4 rounded shadow">
                <h2>Inactive</h2>
                <p className="text-xl">{stats.inactiveVendors}</p>
              </div>
              <div className="bg-yellow-500 text-white p-4 rounded shadow">
  <h2>Pending</h2>
  <p className="text-xl">{stats.pendingVendors}</p>
</div>
            </div>

            {/* Chart */}
            <div className="bg-white p-4 rounded shadow">
              <h2 className="mb-4 font-semibold">Vendor Stats</h2>

              <ResponsiveContainer width="100%" height={300}>
  <BarChart data={chartData}>
    <XAxis dataKey="name" />
    <YAxis />
    <Tooltip />
    <Bar dataKey="value" />
  </BarChart>
</ResponsiveContainer>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default DashboardPage;