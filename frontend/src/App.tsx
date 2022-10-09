import {Component} from "react";
import {Link, Route, Routes} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import logo from "./assets/img/country-globe.png"

import ValidatePhoneNumber from "./components/validate-phone-number.component";

class App extends Component {
  render() {
    return (
        <div>
          <nav className="navbar navbar-expand navbar-dark bg-dark px-3">
            <Link to={"/"} className="navbar-brand">
              <img src={logo} alt="Logo" width="30px"/>
            </Link>
            <div className="navbar-nav mr-auto">
              <li className="nav-item">
                <Link to={"/"} className="nav-link">
                  Phone Number Info
                </Link>
              </li>
            </div>
          </nav>

          <div className="container mt-3">
            <Routes>
              <Route path="/" element={<ValidatePhoneNumber/>}/>
            </Routes>
          </div>
        </div>
    );
  }
}

export default App;
