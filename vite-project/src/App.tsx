import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./components/Home";
import CreateProduct from "./components/CreateProduct";
import ViewAll from "./components/ViewAll";
import ViewOne from "./components/ViewOne";
import UpdateProduct from "./components/UpdateProduct";
import DeleteProduct from "./components/DeleteProduct";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
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
