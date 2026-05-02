import { useEffect, useState } from "react";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  ResponsiveContainer
} from "recharts";
import api from "../services/api";
import Navbar from "../components/Navbar";

const AnalyticsPage = () => {
  const [stats, setStats] = useState(null);
  const [period, setPeriod] = useState("monthly");

  useEffect(() => {
    const fetchStats = async () => {
      const res = await api.get("/vendors/stats");
      setStats(res.data);
    };

    fetchStats();
  }, [period]);

  if (!stats) return <p className="p-6">Loading analytics...</p>;

  const chartData = [
    { name: "Active", value: stats.active },
    { name: "Inactive", value: stats.inactive },
    { name: "Pending", value: stats.pending }
  ];

  return (
    <div>
      <Navbar />

      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Analytics Dashboard</h1>

        <select
          value={period}
          onChange={(e) => setPeriod(e.target.value)}
          className="border p-2 rounded mb-6"
        >
          <option value="weekly">Weekly</option>
          <option value="monthly">Monthly</option>
          <option value="yearly">Yearly</option>
        </select>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
          <div className="bg-white p-4 rounded shadow">Total: {stats.total}</div>
          <div className="bg-white p-4 rounded shadow">Active: {stats.active}</div>
          <div className="bg-white p-4 rounded shadow">Inactive: {stats.inactive}</div>
          <div className="bg-white p-4 rounded shadow">Pending: {stats.pending}</div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-white p-4 rounded shadow h-80">
            <h2 className="font-semibold mb-3">Vendor Status Pie Chart</h2>
            <ResponsiveContainer width="100%" height="90%">
              <PieChart>
                <Pie data={chartData} dataKey="value" nameKey="name" outerRadius={100} label>
                  {chartData.map((entry, index) => (
                    <Cell key={index} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className="bg-white p-4 rounded shadow h-80">
            <h2 className="font-semibold mb-3">Vendor Status Bar Chart</h2>
            <ResponsiveContainer width="100%" height="90%">
              <BarChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis allowDecimals={false} />
                <Tooltip />
                <Bar dataKey="value" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AnalyticsPage;