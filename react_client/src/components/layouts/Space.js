import React from "react";

const Space = ({ cls }) => {
  return <p className={"space" + (cls ? " ".concat(cls) : "")}></p>;
};

export default Space;
