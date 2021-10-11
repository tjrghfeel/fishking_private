import React, {
    useState,
    useCallback,
    useEffect,
    forwardRef,
    useImperativeHandle,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
    observer(
        forwardRef(({ id, seaCode, DataStore: { getEnums } }, ref) => {
            return (
                <div
                    className="modal fade modal-full"
                    id={id}
                    tabIndex="-1"
                    aria-labelledby={id.concat("Label")}
                    aria-hidden="true"
                >
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header bg-primary d-flex justify-content-center">
                                <a data-dismiss="modal" className="nav-left">
                                    <img
                                        src="/assets/cust/img/svg/navbar-back.svg"
                                        alt="뒤로가기"
                                    />
                                </a>
                                <h5 className="modal-title" id={id.concat("Label")}>
                                    해상 예보
                                </h5>
                            </div>
                            <div className="modal-body" style={{"overflow-x":"auto"}}>
                                <iframe
                                    className="companyDetail-weather-iframe"
                                    src={"https://web.kma.go.kr/mini/marine/inner_marine_forecast.jsp?topArea="+seaCode+"&midArea=12B20100&btmArea=12B20100&unit=M"}
                                    scrolling="yes"
                                    style={{
                                        "border":"none",
                                        "width":"700px",
                                        "height":"170vh",
                                    }}
                                ></iframe>
                            </div>
                        </div>
                    </div>
                </div>
            );
        })
    )
);
