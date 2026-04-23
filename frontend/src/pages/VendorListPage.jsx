import { useEffect, useState } from "react";
import api from "../services/api";
import Navbar from "../components/Navbar";

const VendorListPage = () => {
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchVendors = async () => {
      try {
        const response = await api.get("/vendors");
        setVendors(response.data);
      } catch (err) {
        setError("Failed to fetch vendors");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchVendors();
  }, []);

  return (
    <div>
      <Navbar />
      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Vendor List</h1>

        {loading && (
          <div className="text-blue-600 font-medium">Loading vendors...</div>
        )}

        {!loading && error && (
          <div className="text-red-600 font-medium">{error}</div>
        )}

        {!loading && !error && vendors.length === 0 && (
          <div className="text-gray-600">No vendors found.</div>
        )}

        {!loading && !error && vendors.length > 0 && (
          <div className="overflow-x-auto">
            <table className="min-w-full border border-gray-300 shadow-sm">
              <thead className="bg-gray-100">
                <tr>
                  <th className="border px-4 py-2 text-left">ID</th>
                  <th className="border px-4 py-2 text-left">Vendor Name</th>
                  <th className="border px-4 py-2 text-left">Email</th>
                  <th className="border px-4 py-2 text-left">Phone</th>
                  <th className="border px-4 py-2 text-left">Company</th>
                  <th className="border px-4 py-2 text-left">Status</th>
                </tr>
              </thead>
              <tbody>
                {vendors.map((vendor) => (
                  <tr key={vendor.id} className="hover:bg-gray-50">
                    <td className="border px-4 py-2">{vendor.id}</td>
                    <td className="border px-4 py-2">{vendor.vendorName}</td>
                    <td className="border px-4 py-2">{vendor.vendorEmail}</td>
                    <td className="border px-4 py-2">{vendor.vendorPhone}</td>
                    <td className="border px-4 py-2">{vendor.companyName}</td>
                    <td className="border px-4 py-2">{vendor.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default VendorListPage;