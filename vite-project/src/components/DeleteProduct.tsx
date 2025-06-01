import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function DeleteProduct() {
  const [id, setId] = useState("");
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [product, setProduct] = useState<any>(null);
  const [auth, setAuth] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const verify = async () => {
      try {
        const token = localStorage.getItem("Token");
        if (!token) {
          navigate("/login");
          return;
        }

        const response = await fetch("http://localhost:8080/api/auth/verify", {
          method: "POST",
          headers: {
            "Content-Type": "text/plain",
          },
          body: token,
        });

        if (!response.ok) {
          throw new Error("Unable to verify token!");
        }

        const result = await response.json();
        if (result) {
          setAuth(true);
        } else {
          navigate("/login");
        }
      } catch (error) {
        console.error("Error", error);
        navigate("/login");
      }
    };
    verify();
  }, []);

  const handleFindSubmit = async (e: { preventDefault: any }) => {
    e.preventDefault();
    setError("");
    setProduct(null);

    if (!id.trim()) {
      return setError("Must input an ID!");
    }

    try {
      const response = await fetch(`http://localhost:8080/api/products/${id}`, {
        method: "GET",
        headers: {
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
      });

      const data = await response.json();

      if (!data) {
        throw new Error("Unable to find product!");
      }

      setProduct(data);
    } catch {
      setError("Product not found!");
    }
  };

  const handleSubmit = async (e: { preventDefault: () => void }) => {
    e.preventDefault();
    setError("");
    setMessage("");

    try {
      const response = await fetch(`http://localhost:8080/api/products/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
      });

      if (!response.ok) {
        throw new Error("Unable to delete product");
      }

      setProduct(null);
      setMessage("Successfully deleted product!");
      setId("");
    } catch {
      setError("Unable to delete Product");
    }
  };

  return (
    <div>
      {auth && (
        <div className="d-flex flex-column align-items-center justify-content-center vh-100">
          {error && <div className="alert alert-danger">{error}</div>}
          {message && <div className="alert alert-success">{message}</div>}
          <Link className="btn btn-primary mb-2" to="/home">
            Home
          </Link>
          <form className="mb-4" onSubmit={handleFindSubmit}>
            <div className="mb-3">
              <label htmlFor="id" className="form-label">
                Enter Product ID:
              </label>
              <input
                type="text"
                className="form-control"
                id="id"
                value={id}
                onChange={(e) => {
                  setId(e.target.value);
                }}
              />
            </div>
            <button className="btn btn-primary" type="submit">
              Submit
            </button>
          </form>
          {product && (
            <div
              className="card p-4 shadow-lg rounded"
              style={{ width: "400px" }}
            >
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
              <form className="mb-4 pt-4" onSubmit={handleSubmit}>
                <button className="btn btn-primary" type="submit">
                  Delete
                </button>
              </form>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default DeleteProduct;
