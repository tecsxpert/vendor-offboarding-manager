import { useState } from "react";
import axios from "axios";

function AIPanel({ vendorId }) {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState(null);
  const [error, setError] = useState("");

  const handleAI = async () => {
    try {
      setLoading(true);
      setError("");
      setData(null);

      const res = await axios.post(`http://localhost:5000/recommend`, {
        vendor_id: vendorId
      });

      setData(res.data);
    }      catch (err) {
      setData({
        action_type: "Review Vendor",
        description:
          "AI service is currently unavailable. Please review vendor status, contract end date, and risk score manually.",
        priority: "Medium"
      });
      setError("");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white p-4 rounded shadow mt-4">
      <h3 className="text-lg font-semibold mb-2">AI Recommendation</h3>

      <button
        onClick={handleAI}
        className="bg-blue-600 text-white px-4 py-2 rounded"
      >
        Generate AI
      </button>

      {/* Loading */}
      {loading && (
        <p className="mt-3 text-gray-500">Generating AI response...</p>
      )}

      {/* Error */}
      {error && (
        <p className="mt-3 text-red-500">{error}</p>
      )}

      {/* Response Card */}
      {data && (
        <div className="mt-4 p-3 border rounded bg-gray-50">
          <p><b>Action:</b> {data.action_type}</p>
          <p><b>Description:</b> {data.description}</p>
          <p><b>Priority:</b> {data.priority}</p>
        </div>
      )}
    </div>
  );
}

export default AIPanel;