import { useContext } from "react";
import { AuthContext } from "./../context/AuthContext";
import "./../styles/adminPage.css"; // nuevo archivo de estilos

export const AdminPage = () => {
  const { user } = useContext(AuthContext);

  return (
    <div className="container mt-5 text-center">
      <h1 className="admin-welcome text-warning">
        Bienvenido al panel de administrador de{" "}
        <span className="brand">ClandeStock</span>, {user?.username}
      </h1>
    </div>
  );
};
