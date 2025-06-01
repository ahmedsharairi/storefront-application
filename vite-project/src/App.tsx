import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Home from "./components/Home";
import CreateProduct from "./components/CreateProduct";
import ViewAll from "./components/ViewAll";
import ViewOne from "./components/ViewOne";
import UpdateProduct from "./components/UpdateProduct";
import DeleteProduct from "./components/DeleteProduct";
import LoginScreen from "./components/LoginScreen";
import RegisterScreen from "./components/RegisterScreen";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginScreen />} />
        <Route path="/register" element={<RegisterScreen />} />
        <Route path="/home" element={<Home />} />
        <Route path="/create" element={<CreateProduct />} />
        <Route path="/view-one" element={<ViewOne />} />
        <Route path="/update" element={<UpdateProduct />} />
        <Route path="/delete" element={<DeleteProduct />} />
        <Route path="/view-all" element={<ViewAll />} />
      </Routes>
    </Router>
  );
}

export default App;
