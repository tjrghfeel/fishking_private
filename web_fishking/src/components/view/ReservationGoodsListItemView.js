import React from "react";
import { inject, observer } from "mobx-react";
import ModalStore from "../../stores/ModalStore";

export default inject()(
  observer(
    ({
      data: {
        name = "",
        fishSpecies = [],
        minPersonnel = 0,
        maxPersonnel = 0,
        reservationPersonal = 0,
        startTime = "",
        endTime = "",
        price = 0,
        fishingEndDate = "",
      },
      data,
      onChange,
      props,
      selectedDate,
    }) => {
      return (
          <li className="clearfix"
              onClick={()=>{
                const { PageStore, ModalStore } = props;

                const d = new Date()
                const n = new Date(d.getTime() + 30*60000)
                const t = n.getHours() + "" + (n.getMinutes())
                if (
                  selectedDate == d.getDate() &&
                  startTime <= t
                ) {
                  ModalStore.openModal("Alert", {
                    body: "예약 가능 시간이 지난 상품입니다.",
                  });
                }

                PageStore.push(
                      `/reservation/goods/payment/${
                          data.id
                      }/${selectedDate.format()}`
                  );
              }}
          >
            <div className="list_main">
              <h2>{name}</h2>
              <h2 className="cal_price">
                {Intl.NumberFormat().format(price)} 원
                <p>&#40;{ maxPersonnel - reservationPersonal > 0
                    ? '남은 수 ' + Intl.NumberFormat().format(maxPersonnel - reservationPersonal) +'명'
                    : "대기자예약"}&#41;</p>
              </h2>
            </div>
            <ol className="list_sub">
              <li>{/*최소인원 5명 &#47; 최대인원 21명*/}
                최소인원 {Intl.NumberFormat().format(minPersonnel)}명 /
                최대인원 {Intl.NumberFormat().format(maxPersonnel)}명</li>
              <li>{/*오전 00시 00분 &#126; 오후 1시 00분 &#40;13시간&#41;*/}
                {startTime.formatTime01()} ~ {fishingEndDate} {endTime.formatTime01()} (
                {fishingEndDate !== "" && (
                    +endTime.substr(0, 2) + 24 - startTime.substr(0, 2)
                )}
                {fishingEndDate === "" && (
                    endTime.substr(0, 2) - startTime.substr(0, 2)
                )}
                시간)</li>
              <li>
                <div>
                  {/*어종 &#58; 참돔, 옥돔, 강성돔, 뱅어돔, 부사리, 장어, 참치,
                  연어, 고등어, 삼치*/}
                  어종:{" "}
                  {fishSpecies.map((data, index) => {
                    if (index === 0) {
                      return (
                          <React.Fragment key={index}>{data}</React.Fragment>
                      );
                    } else {
                      return (
                          <React.Fragment key={index}>
                            {", ".concat(data)}
                          </React.Fragment>
                      );
                    }
                  })}
                </div>
              </li>
            </ol>
            {/*<a onClick={()=>{*/}
            {/*    const { PageStore } = props;*/}
            {/*    PageStore.push(*/}
            {/*        `/reservation/goods/payment/${*/}
            {/*            data.id*/}
            {/*        }/${selectedDate.format()}`*/}
            {/*    );*/}
            {/*}} className="cal_submit_btn">상품 바로 결제하기</a>*/}
          </li>

        // <div className="container nopadding mt-2">
        //   <div className="row no-gutters">
        //     <div className="col-2 text-center">
        //       <label className="control checkbox">
        //         <input
        //           type="radio"
        //           name="reservation-goods-radio"
        //           className="add-contrast"
        //           data-role="collar"
        //           onChange={(e) =>
        //             onChange ? onChange(e.target.checked, data) : null
        //           }
        //         />
        //         <span className="control-indicator"></span> <small></small>
        //       </label>
        //     </div>
        //     <div className="col-7">
        //       <h6>{name}</h6>
        //       <span className="tag">
        //         { maxPersonnel - reservationPersonal > 0
        //           ? '남은 수 ' + Intl.NumberFormat().format(maxPersonnel - reservationPersonal) +'명'
        //           : "대기자예약"}
        //       </span>
        //       <ul className="list">
        //         <li>
        //           최소인원 {Intl.NumberFormat().format(minPersonnel)}명 /
        //           최대인원 {Intl.NumberFormat().format(maxPersonnel)}명
        //         </li>
        //         <li>
        //           {startTime.formatTime01()} ~ {fishingEndDate} {endTime.formatTime01()} (
        //           {fishingEndDate !== "" && (
        //             +endTime.substr(0, 2) + 24 - startTime.substr(0, 2)
        //           )}
        //           {fishingEndDate === "" && (
        //             endTime.substr(0, 2) - startTime.substr(0, 2)
        //           )}
        //           시간)
        //         </li>
        //         <li>
        //           어종:{" "}
        //           {fishSpecies.map((data, index) => {
        //             if (index === 0) {
        //               return (
        //                 <React.Fragment key={index}>{data}</React.Fragment>
        //               );
        //             } else {
        //               return (
        //                 <React.Fragment key={index}>
        //                   {", ".concat(data)}
        //                 </React.Fragment>
        //               );
        //             }
        //           })}
        //         </li>
        //       </ul>
        //     </div>
        //     <div className="col-3 text-right">
        //       <strong className="red large">
        //         {Intl.NumberFormat().format(price)}
        //         <span>원</span>
        //       </strong>
        //     </div>
        //   </div>
        //   <p className="space mt-3 mb-1"></p>
        // </div>
      );
    }
  )
);
