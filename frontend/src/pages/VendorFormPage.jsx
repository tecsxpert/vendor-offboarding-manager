import { useState } from "react";
import api from "../services/api";
import Navbar from "../components/Navbar";

const VendorFormPage = ({ editingVendor = null }) => {
  const [formData, setFormData] = useState({
    vendorName: editingVendor?.vendorName || "",
    vendorEmail: editingVendor?.vendorEmail || "",
    vendorPhone: editingVendor?.vendorPhone || "",
    companyName: editingVendor?.companyName || "",
    status: editingVendor?.status || "ACTIVE",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const validateForm = () => {
    if (!formData.vendorName.trim()) return "Vendor name is required";
    if (!formData.vendorEmail.trim()) return "Vendor email is required";
    if (!formData.vendorEmail.includes("@")) return "Enter a valid email";
    if (!formData.companyName.trim()) return "Company name is required";
    return "";
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    const validationError = validateForm();
    if (validationError) {
      setError(validationError);
      return;
    }

    try {
      if (editingVendor?.id) {
        await api.put(`/vendors/${editingVendor.id}`, formData);
        setSuccess("Vendor updated successfully");
      } else {
        await api.post("/vendors", formData);
        setSuccess("Vendor created successfully");
        setFormData({
          vendorName: "",
          vendorEmail: "",
          vendorPhone: "",
          companyName: "",
          status: "ACTIVE",
        });
      }
    } catch (err) {
      console.error(err);
      setError("Something went wrong");
    }
  };

  return (
    <div>
      <Navbar />
      <div className="p-6 max-w-xl">
        <h1 className="text-2xl font-bold mb-4">
          {editingVendor ? "Edit Vendor" : "Create Vendor"}
        </h1>

        {error && <p className="text-red-600 mb-3">{error}</p>}
        {success && <p className="text-green-600 mb-3">{success}</p>}

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            name="vendorName"
            placeholder="Vendor Name"
            value={formData.vendorName}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          />

          <input
            name="vendorEmail"
            placeholder="Vendor Email"
            value={formData.vendorEmail}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          />

          <input
            name="vendorPhone"
            placeholder="Vendor Phone"
            value={formData.vendorPhone}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          />

          <input
            name="companyName"
            placeholder="Company Name"
            value={formData.companyName}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          />

          <select
            name="status"
            value={formData.status}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          >
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>

          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded"
          >
            {editingVendor ? "Update Vendor" : "Create Vendor"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default VendorFormPage;