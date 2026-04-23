import Navbar from "../components/Navbar";

const HomePage = () => {
  return (
    <div>
      <Navbar />
      <div className="p-6">
        <h1 className="text-2xl font-bold">Frontend Setup Completed</h1>
        <p className="mt-2 text-gray-700">
          React + Vite + Axios + Tailwind are configured.
        </p>
      </div>
    </div>
  );
};

export default HomePage;