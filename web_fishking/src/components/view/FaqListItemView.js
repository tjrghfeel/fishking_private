import React from "react";
import { inject, observer } from "mobx-react";
import ContentView from "./ContentView";

export default inject()(
  observer(
    ({ data: { id, title, questionType, contents }, expend = false }) => {
      return (
        <div className="card">
          <div className="card-header" id={"heading" + id}>
            <h2 className="mb-0">
              <a
                className="btn btn-block text-left"
                data-toggle="collapse"
                data-target={"#collapse" + id}
                aria-expanded={expend ? "true" : "false"}
                aria-controls={"collapse" + id}
              >
                [{questionType}] {title}
              </a>
            </h2>
          </div>

          <div
            id={"collapse" + id}
            className={"collapse" + (expend ? " show" : "")}
            aria-labelledby={"heading" + id}
            data-parent="#accordionFaq"
          >
            <div className="card-body"><ContentView content={contents}/></div>
          </div>
        </div>
      );
    }
  )
);
