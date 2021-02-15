import React, {
  useState,
  useEffect,
  useCallback,
  useImperativeHandle,
  forwardRef,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
  observer(
    forwardRef(({ id, onSelected, DataStore }, ref) => {
      const [arrService, setArrService] = useState([]); // # 서비스
      const [arrFac, setArrFac] = useState([]); // # 편의시설
      const [selectedService, setSelectedService] = useState([]);
      const [selectedFac, setSelectedFac] = useState([]);

      useEffect(() => {
        (async () => {
          let resolve = await DataStore.getCodes("85", 3);
          setArrService(resolve);

          resolve = await DataStore.getCodes("87", 3);
          setArrFac(resolve);
        })();
      }, [setArrFac, setArrService, DataStore]);

      const onChange = useCallback(
        (type = "service" | "facility", checked, item) => {
          if (type === "service") {
            if (checked) {
              selectedService.push(item);
              setSelectedService(selectedService);
            } else {
              const selected = DataStore.removeItemOfArrayByKey(
                selectedService,
                "id",
                item.id
              );
              setSelectedService(selected);
            }
          } else if (type === "facility") {
            if (checked) {
              selectedFac.push(item);
              setSelectedFac(selectedFac);
            } else {
              const selected = DataStore.removeItemOfArrayByKey(
                selectedFac,
                "id",
                item.id
              );
              setSelectedFac(selected);
            }
          }
        },
        [
          selectedService,
          setSelectedService,
          selectedFac,
          setSelectedFac,
          DataStore,
        ]
      );
      const onInit = useCallback(() => {
        setSelectedFac([]);
        setSelectedService([]);
        const elements = document.querySelectorAll(`#${id} input`);
        for (let element of elements) {
          element.checked = false;
        }
      }, [setSelectedFac, setSelectedService]);
      useImperativeHandle(ref, () => ({ onInit }));
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
                  옵션선택
                </h5>
                <a className="nav-right" onClick={onInit}>
                  <img
                    src="/assets/cust/img/svg/navbar-refresh.svg"
                    alt="Refresh"
                  />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="padding">
                  <h6 className="modal-title-sub">서비스 제공</h6>
                  {arrService.map((data, index) => (
                    <div className="row" key={index}>
                      {data.map((item, index2) => (
                        <div className="col" key={index2}>
                          {item.id !== null && (
                            <label className="control checkbox">
                              <input
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                onChange={(e) => {
                                  onChange("service", e.target.checked, item);
                                }}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item.codeName}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}

                  <h6 className="modal-title-sub">편의시설</h6>
                  {arrFac.map((data, index) => (
                    <div className="row" key={index}>
                      {data.map((item, index2) => (
                        <div className="col" key={index2}>
                          {item.id !== null && (
                            <label className="control checkbox">
                              <input
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                onChange={(e) => {
                                  onChange("facility", e.target.checked, item);
                                }}
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item.codeName}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
              </div>
              <a
                onClick={() =>
                  onSelected ? onSelected(selectedService, selectedFac) : null
                }
                className="btn btn-primary btn-lg btn-block btn-btm"
                data-dismiss="modal"
              >
                적용하기
              </a>
            </div>
          </div>
        </div>
      );
    })
  )
);
