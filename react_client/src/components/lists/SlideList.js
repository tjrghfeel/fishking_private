import React from "react";
import ListItemMore from "./ListItemMore";

const SlideList = ({
  title,
  list,
  renderItem: RenderItem,
  hasMore,
  navigateMore,
  cls,
}) => {
  return (
    <>
      {title && <h5>{title}</h5>}
      <div className={"slideList" + (cls ? " ".concat(cls) : "")}>
        <ul className={"listWrap"}>
          {list &&
            list.map((data, index) => <RenderItem key={index} {...data} />)}
          {hasMore && <ListItemMore navigateTo={navigateMore} />}
        </ul>
      </div>
    </>
  );
};

export default SlideList;
