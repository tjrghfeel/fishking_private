import React from "react";
import { BrowserRouter, Route } from "react-router-dom";
import Routers from "./routers";

const App = () => {
  return (
    <BrowserRouter>
      <Route path={`/*`} component={Routers} />
    </BrowserRouter>
  );
};

export default App;
