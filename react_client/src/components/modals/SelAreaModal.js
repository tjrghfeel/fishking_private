/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useCallback, useState } from "react";
import { inject, observer } from "mobx-react";

const SelAreaModal = inject("ModalStore")(
  observer(
    ({
      ModalStore: {
        data: { onSelect },
      },
    }) => {
      const [selected, setSelected] = useState([]);
      /** 지역 선택 */
      const onChange = useCallback(
        (text) => {
          if (selected.includes(text)) {
            const index = selected.indexOf(text);
            setSelected([
              ...selected.slice(0, index),
              ...selected.slice(index + 1, selected.length),
            ]);
          } else {
            setSelected([...selected, text]);
          }
        },
        [selected, setSelected]
      );
      /** 적용하기 */
      const onApply = useCallback(() => {}, [selected]);
      /** 지역별 선택 데이터 목록 */
      const arr_area = [
        ["수도권", "충청남도"],
        ["전라북도", "전라남도"],
        ["경상북도", "충청남도"],
      ];
      /** 시군구 선택 데이터 목록 */
      const arr_city = [
        ["인천 중구", "인천 옹진군"],
        ["인천 중구", "인천 옹진군"],
        ["인천 중구", "인천 옹진군"],
        ["인천 중구", "인천 옹진군"],
        ["인천 중구", "인천 옹진군"],
        ["인천 중구", "인천 옹진군"],
        ["인천 중구", "인천 옹진군"],
        ["인천 중구", "인천 옹진군"],
      ];
      return (
        <div
          className="modal fade modal-full"
          id="selAreaModal"
          tabIndex="-1"
          aria-labelledby="selAreaModalLabel"
          aria-hidden="true"
        >
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header bg-primary d-flex justify-content-center">
                <a href="#none" data-dismiss="modal" className="nav-left">
                  <img src="/assets/img/svg/navbar-back.svg" alt="뒤로가기" />
                </a>
                <h5 className="modal-title" id="selAreaModalLabel">
                  지역선택
                </h5>
                <a href="#none" className="nav-right">
                  <img src="/assets/img/svg/navbar-refresh.svg" alt="Refresh" />
                  <span>초기화</span>
                </a>
              </div>
              <div className="modal-body">
                <div className="padding">
                  <form className="form-search">
                    <img
                      src="/assets/img/svg/form-search.svg"
                      alt=""
                      className="icon-search"
                    />
                    <input
                      className="form-control mr-sm-2"
                      type="search"
                      placeholder="지역명을 입력하세요"
                      aria-label="Search"
                    />
                    <a href="search.html">
                      <img
                        src="/assets/img/svg/navbar-search.svg"
                        alt="Search"
                      />
                    </a>
                  </form>

                  <h6 className="modal-title-sub">지역별 선택</h6>

                  {arr_area.map((list, index) => (
                    <div key={index} className="row">
                      {list.map((text, index2) => (
                        <div key={index2} className="col">
                          <label className="control checkbox">
                            <input
                              type="checkbox"
                              className="add-contrast"
                              data-role="collar"
                              onChange={() => onChange(text)}
                            />
                            <span className="control-indicator"></span>
                            <span className="control-text">{text}</span>
                          </label>
                        </div>
                      ))}
                    </div>
                  ))}

                  <h6 className="modal-title-sub">시/군/구 선택</h6>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">인천 중구</span>
                      </label>
                    </div>
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">인천 옹진군</span>
                      </label>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">경기도 시흥시</span>
                      </label>
                    </div>
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">경기도 평택시</span>
                      </label>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">경기도 화성시</span>
                      </label>
                    </div>
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">충남 보령시</span>
                      </label>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">충남 당진시</span>
                      </label>
                    </div>
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">충남 서천군</span>
                      </label>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">충남 태안군</span>
                      </label>
                    </div>
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">전북 군산시</span>
                      </label>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">전북 부안군</span>
                      </label>
                    </div>
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">전남 목포시</span>
                      </label>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">경기도 화성시</span>
                      </label>
                    </div>
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">충남 보령시</span>
                      </label>
                    </div>
                  </div>

                  <div className="row">
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">충남 당진시</span>
                      </label>
                    </div>
                    <div className="col">
                      <label className="control checkbox">
                        <input
                          type="checkbox"
                          className="add-contrast"
                          data-role="collar"
                        />
                        <span className="control-indicator"></span>
                        <span className="control-text">충남 서천군</span>
                      </label>
                    </div>
                  </div>
                </div>
              </div>
              <a
                onClick={onApply}
                className="btn btn-primary btn-lg btn-block btn-btm"
                data-dismiss="modal"
              >
                적용하기
              </a>
            </div>
          </div>
        </div>
      );
    }
  )
);

export default SelAreaModal;
