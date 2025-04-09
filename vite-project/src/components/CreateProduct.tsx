import { useState } from "react";
import { Link } from "react-router-dom";

function CreateProduct() {
  const [category, setCategory] = useState("");
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [qty, setQty] = useState("");
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e: { preventDefault: () => void }) => {
    e.preventDefault();
    setError("");
    setMessage("");

    if (!category.trim()) {
      return setError("Category is required!");
    }
    if (!name.trim()) {
      return setError("Name is required!");
    }
    if (isNaN(parseFloat(price)) || parseFloat(price) <= 0) {
      return setError("Price is invalid!");
    }
    if (!Number.isInteger(Number(qty)) || Number(qty) < 0) {
      return setError("Quantity is invalid!");
    }

    const productData = {
      category,
      name,
      price: parseFloat(price),
      qty: parseInt(qty),
    };

    try {
      const response = await fetch("http://localhost:8080/api/products", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(productData),
      });

      if (!response.ok) {
        throw new Error("Failed to create Product!");
      }

      setMessage("Product created successfully");
      setCategory("");
      setName("");
      setPrice("");
      setQty("");
    } catch {
      setError("Error creating product!");
    }
  };

  return (
    <form
      className="d-flex flex-column align-items-center justify-content-center vh-100"
      onSubmit={handleSubmit}
    >
      {error && <div className="alert alert-danger">{error}</div>}
      {message && <div className="alert alert-success">{message}</div>}
      <Link className="btn btn-primary" to="/">
        Home
      </Link>
      <div className="mb-3">
        <label htmlFor="cat" className="form-label">
          Category
        </label>
        <input
          type="text"
          className="form-control"
          id="cat"
          value={category}
          onChange={(e) => {
            setCategory(e.target.value);
          }}
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
          onChange={(e) => {
            setName(e.target.value);
          }}
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
          onChange={(e) => {
            setPrice(e.target.value);
          }}
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
          onChange={(e) => {
            setQty(e.target.value);
          }}
        ></input>
      </div>
      <button className="btn btn-primary" type="submit">
        Create
      </button>
    </form>
  );
}

export default CreateProduct;
