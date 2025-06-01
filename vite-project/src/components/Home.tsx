import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function Home() {
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

  return (
    <div>
      {auth && (
        <div className="d-flex flex-column align-items-center justify-content-center vh-100">
          <h1 className="text-center mb-3">Inventory Management</h1>
          <nav className="navbar navbar-expand-lg bg-body-tertiary">
            <div className="container-fluid">
              <button
                className="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarNav"
                aria-controls="navbarNav"
                aria-expanded="false"
                aria-label="Toggle navigation"
              >
                <span className="navbar-toggler-icon"></span>
              </button>
              <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav">
                  <li className="nav-item">
                    <Link className="nav-link" to="/create">
                      Create Product
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/view-all">
                      View All
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/view-one">
                      View One
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/update">
                      Update
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/delete">
                      Delete
                    </Link>
                  </li>
                </ul>
              </div>
            </div>
          </nav>
        </div>
      )}
    </div>
  );
}

export default Home;
