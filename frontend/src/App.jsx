import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import LoginPage from "./pages/LoginPage";
import VendorListPage from "./pages/VendorListPage";
import VendorFormPage from "./pages/VendorFormPage";
import DashboardPage from "./pages/DashboardPage";
import VendorDetailPage from "./pages/VendorDetailPage";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />

          <Route
            path="/"
            element={
              <ProtectedRoute>
                <VendorListPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <DashboardPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/vendors/:id"
            element={
              <ProtectedRoute>
                <VendorDetailPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/form"
            element={
              <ProtectedRoute>
                <VendorFormPage />
              </ProtectedRoute>
            }
          />
          <Route path="/dashboard" element={<DashboardPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;