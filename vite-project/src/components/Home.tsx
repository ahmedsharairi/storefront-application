import { Link } from "react-router-dom";

function Home() {
  return (
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
  );
}

export default Home;
