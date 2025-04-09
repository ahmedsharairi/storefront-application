import { useState } from "react";
import { Link } from "react-router-dom";

function ViewOne() {
  const [id, setId] = useState("");
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [product, setProduct] = useState<any>(null);

  const handleSubmit = async (e: { preventDefault: () => void }) => {
    e.preventDefault();
    setError("");
    setMessage("");
    setProduct(null);

    if (!id.trim()) {
      return setError("Must input an ID!");
    }

    try {
      const response = await fetch(`http://localhost:8080/api/products/${id}`, {
        method: "GET",
      });

      const data = await response.json();

      if (!data) {
        throw new Error("Unable to find product");
      }

      setProduct(data);

      setMessage("Product found!");
    } catch {
      setError("Product not found!");
    }
  };

  return (
    <div className="d-flex flex-column align-items-center justify-content-center vh-100">
      {error && <div className="alert alert-danger">{error}</div>}
      {message && <div className="alert alert-success">{message}</div>}
      <Link className="btn btn-primary mb-2" to="/">
        Home
      </Link>
      <form onSubmit={handleSubmit} className="mb-4">
        <div className="mb-3">
          <label htmlFor="id" className="form-label">
            Enter Product ID:
          </label>
          <input
            type="text"
            className="form-control"
            id="id"
            value={id}
            onChange={(e) => setId(e.target.value)}
          />
        </div>
        <button className="btn btn-primary" type="submit">
          Submit
        </button>
      </form>

      {product && (
        <div className="card p-4 shadow-lg rounded" style={{ width: "400px" }}>
          <h5 className="text-center mb-3">Product Details</h5>
          <div
            className="border p-3 rounded bg-light"
            style={{ minHeight: "100px" }}
          >
            <p>
              <strong>Category:</strong> {product.category}
            </p>
            <p>
              <strong>Name:</strong> {product.name}
            </p>
            <p>
              <strong>Price:</strong> ${product.price}
            </p>
            <p>
              <strong>Quantity:</strong> {product.qty}
            </p>
          </div>
        </div>
      )}
    </div>
  );
}

export default ViewOne;
