import { useState, useEffect } from "react";

function ViewAll() {
  const [products, setProducts] = useState<any[]>([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getProducts = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/products", {
          method: "GET",
        });

        const data = await response.json();

        if (!data) {
          throw new Error("Unable to retreive products");
        }

        setProducts(data);
        setLoading(false);
      } catch {
        setError("Unable to retreive products");
        setLoading(false);
      }
    };
    getProducts();
  }, []);

  return (
    <div className="d-flex flex-column align-items-center justify-content-center vh-100">
      {error && <div className="alert alert-danger">{error}</div>}
      {loading && <div>Loading...</div>}

      {!loading && !error && products.length === 0 && (
        <div>No products found</div>
      )}
      {!loading && !error && products.length > 0 && (
        <div className="card p-4 shadow-lg rounded" style={{ width: "800px" }}>
          <h5 className="text-center mb-3">All Products</h5>
          <table className="table">
            <thead>
              <tr>
                <th scope="col">Category</th>
                <th scope="col">Name</th>
                <th scope="col">Price</th>
                <th scope="col">Quantity</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product) => (
                <tr key={product._id}>
                  <td>{product.category}</td>
                  <td>{product.name}</td>
                  <td>${product.price}</td>
                  <td>{product.qty}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default ViewAll;
