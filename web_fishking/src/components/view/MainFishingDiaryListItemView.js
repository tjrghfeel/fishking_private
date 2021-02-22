import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(
    ({
      data: { id, imageUrl, title, species, sido = "", sigungu = "" },
      PageStore,
    }) => {
      return (
        <li className="item">
          <a onClick={() => PageStore.push(`/story/diary/detail/${id}`)}>
            <div className="imgWrap">
              <img src={imageUrl} className="img-fluid" alt="" />
            </div>
            <div className="InfoWrap">
              <h6>{title}</h6>
              <p>
                <strong className="text-primary">{species}</strong>
                {sido.concat(" ").concat(sigungu)}
              </p>
            </div>
          </a>
        </li>
      );
    }
  )
);
