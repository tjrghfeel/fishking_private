import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      list,
      onClick
    }) => {
      return (
        <React.Fragment>
          <ol id="content-list">
            <div id="scroll-list" style={{overflowY: "scroll"}}>
              {list.map((data, index) => (
                <li>
                  <div className="img_wrap">
                    <img src={"https://www.fishkingapp.com"+data.shipImageFileUrl} alt=""/>
                  </div>
                  <div className="text_part">
                    <h3>{data.shipName}</h3>
                    {data.fishSpeciesCount > 0 && (
                      <p>
                        {data.fishSpecies.map((s, index) => {
                          if (index < 1) {
                            return (<span>{s.codeName}, </span>)
                          }
                          if (index == 1) {
                            return (<span>{s.codeName} 외 {data.fishSpeciesCount - 2}종 </span>)
                          }
                        })}
                      </p>
                    )}
                    <p>{data.address}</p>
                    <a onClick={() => onClick(data)} className="more_btn">상 세 보 기</a>
                  </div>
                </li>
              ))}
            </div>
          </ol>
        </React.Fragment>
      )
    }
  )
)