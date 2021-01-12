import React from "react";
import { BrowserRouter, Route } from "react-router-dom";
import Routers from "./routes";

String.prototype.maskEmail = function () {
  const index = this.indexOf("@");
  return this.substr(0, 3)
    .concat("****")
    .concat(this.substr(index, this.length));
};

const App = () => {
  return (
    <BrowserRouter>
      <Route path={`/*`} component={Routers} />
    </BrowserRouter>
  );
};

export default App;
