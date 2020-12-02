import React from "react";
import { BrowserRouter, Route } from "react-router-dom";
import Routes from "./routes";

const App: React.Component = () => {
  return (
    <BrowserRouter>
      <Route path={`/*`} component={Routes} />
    </BrowserRouter>
  );
};

export default App;
