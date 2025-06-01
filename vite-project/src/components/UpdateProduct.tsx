import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function UpdateProduct() {
  const [id, setId] = useState("");
  const [category, setCategory] = useState("");
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [qty, setQty] = useState("");
  const [error, setError] = useState("");
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

  const handleFindSubmit = async (e: { preventDefault: () => void }) => {
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

    Object.assign(product, {
      ...(category && { category }),
      ...(name && { name }),
      ...(price && { price: parseFloat(price) }),
      ...(qty && { qty: parseInt(qty) }),
    });

    try {
      const updateResponse = await fetch(
        `http://localhost:8080/api/products/${id}`,
        {
          method: "PUT",
          headers: {
            Authorization: "Bearer " + localStorage.getItem("Token"),
            "Content-Type": "application/json",
          },
          body: JSON.stringify(product),
        }
      );

      if (!updateResponse.ok) {
        throw new Error("Error updating product!");
      }

      const response = await fetch(`http://localhost:8080/api/products/${id}`, {
        method: "GET",
      });

      const data = await response.json();

      if (!data) {
        throw new Error("Unable to find product!");
      }

      setProduct(data);
      setCategory("");
      setName("");
      setPrice("");
      setQty("");
    } catch {
      setError("Unable to update product!");
    }
  };

  return (
    <div>
      {auth && (
        <div className="d-flex flex-column align-items-center justify-content-center vh-100">
          {error && <div className="alert alert-danger">{error}</div>}
          <Link className="btn btn-primary mb-2" to="/home">
            Home
          </Link>
          <form onSubmit={handleFindSubmit} className="mb-4">
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
              <form className="mb-4" onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="cat" className="form-label">
                    Category
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="cat"
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                  ></input>
                </div>
                <div className="mb-3">
                  <label htmlFor="name" className="form-label">
                    Name
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  ></input>
                </div>
                <div className="mb-3">
                  <label htmlFor="price" className="form-label">
                    Price
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="price"
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                  ></input>
                </div>
                <div className="mb-3">
                  <label htmlFor="qty" className="form-label">
                    Quantity
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="qty"
                    value={qty}
                    onChange={(e) => setQty(e.target.value)}
                  ></input>
                </div>
                <button className="btn btn-primary" type="submit">
                  Update
                </button>
              </form>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default UpdateProduct;
