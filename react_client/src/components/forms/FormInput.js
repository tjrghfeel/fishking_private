import React from "react";

const FormInput = ({ type, id, cls, placeholder, value, onChange }) => {
  type = type || "text";
  value = value || "";
  return (
    <input
      type={type}
      id={id}
      className={"form-control" + (cls ? " ".concat(cls) : "")}
      placeholder={placeholder}
      value={value}
      onChange={(e) => {
        onChange(e.target.value);
      }}
    />
  );
};

export default FormInput;
