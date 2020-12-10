import React from "react";
import { BrowserRouter, Route } from "react-router-dom";
import RootRoute from "./routes";

const App = () => {
  return (
    <BrowserRouter>
      <Route path={`/*`} component={RootRoute} />
    </BrowserRouter>
  );
};

export default App;
