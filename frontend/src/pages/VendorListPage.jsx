import { useEffect, useState } from "react";
import api from "../services/api";
import Navbar from "../components/Navbar";

const VendorListPage = () => {
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [search, setSearch] = useState("");
  const [debouncedSearch, setDebouncedSearch] = useState("");
  const [status, setStatus] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  // Debounce search
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(search);
      setPage(0);
    }, 500);

    return () => clearTimeout(timer);
  }, [search]);

  const fetchVendors = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get(
        `/vendors/all?page=${page}&size=5&search=${debouncedSearch}&status=${status}&startDate=${startDate}&endDate=${endDate}`
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
  }, [page, debouncedSearch, status, startDate, endDate]);

  return (
    <div>
      <Navbar />

      <div className="p-4 md:p-6">
        <h1 className="text-xl md:text-2xl font-bold mb-4">Vendor List</h1>

        {/* Create button */}
        <button
          onClick={() => (window.location.href = "/form")}
          className="bg-green-600 text-white px-4 py-2 rounded mb-4 w-full sm:w-auto"
        >
          + Create Vendor
        </button>

        {/* Filters */}
        <div className="flex flex-col sm:flex-row flex-wrap gap-3 mb-4">
          <input
            type="text"
            placeholder="Search vendor..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="border px-3 py-2 rounded w-full sm:w-auto"
          />

          <select
            value={status}
            onChange={(e) => {
              setStatus(e.target.value);
              setPage(0);
            }}
            className="border px-3 py-2 rounded w-full sm:w-auto"
          >
            <option value="">All</option>
            <option value="ACTIVE">Active</option>
            <option value="INACTIVE">Inactive</option>
            <option value="PENDING">Pending</option>
          </select>

          <input
            type="date"
            value={startDate}
            onChange={(e) => {
              setStartDate(e.target.value);
              setPage(0);
            }}
            className="border px-3 py-2 rounded w-full sm:w-auto"
          />

          <input
            type="date"
            value={endDate}
            onChange={(e) => {
              setEndDate(e.target.value);
              setPage(0);
            }}
            className="border px-3 py-2 rounded w-full sm:w-auto"
          />
        </div>

        {/* States */}
        {loading && (
          <div className="text-blue-600 font-medium">Loading vendors...</div>
        )}

        {!loading && error && (
          <div className="text-red-600 font-medium">{error}</div>
        )}

        {!loading && !error && vendors.length === 0 && (
          <div className="text-gray-600">No vendors found.</div>
        )}

        {/* Table */}
        {!loading && !error && vendors.length > 0 && (
          <div className="overflow-x-auto">
            <table className="min-w-full border border-gray-300 shadow-sm text-sm md:text-base">
              <thead className="bg-gray-100">
                <tr>
                  <th className="border px-3 py-2">ID</th>
                  <th className="border px-3 py-2">Name</th>
                  <th className="border px-3 py-2">Email</th>
                  <th className="border px-3 py-2">Phone</th>
                  <th className="border px-3 py-2">Company</th>
                  <th className="border px-3 py-2">Status</th>
                </tr>
              </thead>

              <tbody>
                {vendors.map((vendor) => (
                  <tr
                    key={vendor.id}
                    onClick={() =>
                      (window.location.href = `/vendors/${vendor.id}`)
                    }
                    className="cursor-pointer hover:bg-gray-100"
                  >
                    <td className="border px-3 py-2">{vendor.id}</td>
                    <td className="border px-3 py-2">{vendor.vendorName}</td>
                    <td className="border px-3 py-2">{vendor.vendorEmail}</td>
                    <td className="border px-3 py-2">{vendor.vendorPhone}</td>
                    <td className="border px-3 py-2">{vendor.companyName}</td>
                    <td className="border px-3 py-2">{vendor.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            {/* Pagination */}
            <div className="mt-4 flex flex-col sm:flex-row gap-3 items-center justify-between">
              <button
                disabled={page === 0}
                onClick={() => setPage(page - 1)}
                className="bg-gray-500 text-white px-3 py-1 rounded disabled:opacity-50 w-full sm:w-auto"
              >
                Prev
              </button>

              <span className="font-medium text-center">
                Page {page + 1} of {totalPages}
              </span>

              <button
                disabled={page + 1 >= totalPages}
                onClick={() => setPage(page + 1)}
                className="bg-gray-500 text-white px-3 py-1 rounded disabled:opacity-50 w-full sm:w-auto"
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