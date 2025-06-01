import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function RegisterScreen() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: { preventDefault: () => void }) => {
    e.preventDefault();
    setError("");

    if (!username.trim()) {
      return setError("Username required!");
    }

    if (!password.trim()) {
      return setError("Password required!");
    }

    const userData = {
      username,
      password,
    };

    try {
      const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        throw new Error();
      }

      setUsername("");
      setPassword("");
      navigate("/login");
    } catch {
      setError("Unable to register!");
    }
  };

  return (
    <form
      className="d-flex flex-column align-items-center justify-content-center vh-100"
      onSubmit={handleSubmit}
    >
      <Link className="btn btn-primary" to="/login">
        Login
      </Link>
      <div className="mb-3">
        <label htmlFor="user" className="form-label">
          Username
        </label>
        <input
          type="text"
          className="form-control"
          id="user"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        ></input>
      </div>
      <div className="mb-3">
        <label htmlFor="pass" className="form-label">
          Password
        </label>
        <input
          type="password"
          className="form-control"
          id="pass"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        ></input>
      </div>
      <button className="btn btn-primary" type="submit">
        Register
      </button>
    </form>
  );
}

export default RegisterScreen;
