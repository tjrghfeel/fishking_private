import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch></Switch>
      </BrowserRouter>
    );
  })
);
