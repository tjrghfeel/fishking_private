import React from "react";
import FormInput from "./FormInput";

const RowForm = ({ rows }) => {
  return (
    <form className="form-line mt-	1">
      {rows.map((row, index) => {
        const { formType, id, label, type, placeholder, value, onChange } = row;
        return (
          <div key={index} className="form-group">
            {label && (
              <label htmlFor={id} className="sr-only">
                {label}
              </label>
            )}
            {formType === "input" && (
              <FormInput
                type={type}
                id={id}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
              />
            )}
          </div>
        );
      })}
    </form>
  );
};

export default RowForm;
