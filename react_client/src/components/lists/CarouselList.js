import React from "react";

const CarouselList = ({ id, list, renderItem: RenderItem }) => {
  return (
    <div id={id} className="carousel slide" data-ride="carousel">
      <ol className="carousel-indicators">
        {list &&
          list.map((data, index) => (
            <li
              key={index}
              data-target={"#".concat(id)}
              data-slide-to={index}
              className={index === 0 ? "active" : ""}
            ></li>
          ))}
      </ol>
      <div className="carousel-inner">
        {list &&
          list.map((data, index) => (
            <div
              key={index}
              className={"carousel-item" + (index === 0 ? " active" : "")}
            >
              <RenderItem {...data} />
            </div>
          ))}
      </div>
    </div>
  );
};

export default CarouselList;
