import { useEffect, useState } from "react";
import api from "../services/api";
import Navbar from "../components/Navbar";

const VendorListPage = () => {
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const fetchVendors = async () => {
    try {
      setLoading(true);
      setError(""); // ✅ reset error

      const response = await api.get(
        `/vendors/all?page=${page}&size=5`
      );

      setVendors(response.data.content);
      setTotalPages(response.data.totalPages);

    } catch (err) {
      console.error(err);
      setError("Failed to fetch vendors");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVendors();
  }, [page]);

  return (
    <div>
      <Navbar />

      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Vendor List</h1>

        {loading && (
          <div className="text-blue-600 font-medium">
            Loading vendors...
          </div>
        )}

        {!loading && error && (
          <div className="text-red-600 font-medium">
            {error}
          </div>
        )}

        {!loading && !error && vendors.length === 0 && (
          <div className="text-gray-600">
            No vendors found.
          </div>
        )}

        {!loading && !error && vendors.length > 0 && (
          <div className="overflow-x-auto">

            <button
              onClick={() => (window.location.href = "/form")}
              className="bg-green-600 text-white px-4 py-2 rounded mb-4"
            >
              + Create Vendor
            </button>

            <table className="min-w-full border border-gray-300 shadow-sm">
              <thead className="bg-gray-100">
                <tr>
                  <th className="border px-4 py-2">ID</th>
                  <th className="border px-4 py-2">Vendor Name</th>
                  <th className="border px-4 py-2">Email</th>
                  <th className="border px-4 py-2">Phone</th>
                  <th className="border px-4 py-2">Company</th>
                  <th className="border px-4 py-2">Status</th>
                </tr>
              </thead>

              <tbody>
                {vendors.map((vendor) => (
                  <tr
  key={vendor.id}
  onClick={() => (window.location.href = `/vendors/${vendor.id}`)}
  className="cursor-pointer hover:bg-gray-100"
>
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

            {/* ✅ Pagination */}
            <div className="mt-4 flex gap-3 items-center">
              <button
                disabled={page === 0}
                onClick={() => setPage(page - 1)}
                className="bg-gray-500 text-white px-3 py-1 rounded disabled:opacity-50"
              >
                Prev
              </button>

              <span className="font-medium">
                Page {page + 1} of {totalPages}
              </span>

              <button
                disabled={page + 1 >= totalPages}
                onClick={() => setPage(page + 1)}
                className="bg-gray-500 text-white px-3 py-1 rounded disabled:opacity-50"
              >
                Next
              </button>
            </div>

          </div>
        )}
      </div>
    </div>
  );
};

export default VendorListPage;