/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";

const Button = ({ children, text, onClick, cls, size, sns }) => {
  size = size || "lg";
  return (
    <a
      onClick={() => (onClick ? onClick() : null)}
      className={
        "btn btn-block " +
        "btn-".concat(size) +
        (cls ? " ".concat(cls) : "") +
        (sns ? " btn-sns-".concat(sns) : "")
      }
    >
      {text}
      {children}
    </a>
  );
};

export default Button;
