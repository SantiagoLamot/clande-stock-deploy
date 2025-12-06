import logoC from "../../assets/lc-logo2.png";

export default function LoginNavbar() {
  return (
    <nav className="navbar navbar-light bg-light px-3 shadow-sm">
      <a className="navbar-brand" href="/">
        <img src={logoC} alt="Logo" height="40" />
      </a>
    </nav>
  );
}
