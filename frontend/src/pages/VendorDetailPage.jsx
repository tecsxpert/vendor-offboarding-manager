import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import Navbar from "../components/Navbar";

const VendorDetailPage = () => {
  const { id } = useParams();
  const [vendor, setVendor] = useState(null);

  useEffect(() => {
    const fetchVendor = async () => {
      try {
        const res = await api.get(`/vendors/${id}`);
        setVendor(res.data);
      } catch (err) {
        console.error(err);
      }
    };

    fetchVendor();
  }, [id]);

  const handleDelete = async () => {
    await api.delete(`/vendors/${id}`);
    window.location.href = "/";
  };

  const getScoreBadge = (score) => {
    if (score >= 70) return "High Risk";
    if (score >= 40) return "Medium Risk";
    return "Low Risk";
  };

  if (!vendor) return <p className="p-6">Loading vendor...</p>;

  return (
    <div>
      <Navbar />

      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">{vendor.vendorName}</h1>

        <p>Email: {vendor.vendorEmail}</p>
        <p>Phone: {vendor.vendorPhone}</p>
        <p>Company: {vendor.companyName}</p>
        <p>Status: {vendor.status}</p>

        <p className="mt-4">
          Score Badge:{" "}
          <span className="border px-3 py-1 rounded">
            {getScoreBadge(vendor.score || 0)}
          </span>
        </p>

        <div className="mt-4 flex gap-3">
          <button
            onClick={() => {
              localStorage.setItem("editVendor", JSON.stringify(vendor));
              window.location.href = "/form";
            }}
            className="bg-yellow-500 text-white px-4 py-2 rounded"
          >
            Edit
          </button>

          <button
            onClick={handleDelete}
            className="bg-red-600 text-white px-4 py-2 rounded"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
};

export default VendorDetailPage;