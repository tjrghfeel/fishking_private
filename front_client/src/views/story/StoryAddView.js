import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/common/layout/Navigation";
import SelectFishModal from "../../components/modals/SelectFishModal";
import SelectDateModal from "../../components/modals/SelectDateModal";
import SelectTideModal from "../../components/modals/SelectTideModal";
import SelectTechnicModal from "../../components/modals/SelectTechnicModal";
import SelectLureModal from "../../components/modals/SelectLureModal";
import SelectPlaceModal from "../../components/modals/SelectPlaceModal";
import SelectCompanyAndLocationModal from "../../components/modals/SelectCompanyAndLocationModal";

export default inject("DataStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.category = React.createRef(null);
        this.title = React.createRef(null);
        this.file = React.createRef(null);
        this.state = {
          category_options: [],
          title: "",
          selectedFish: [], // 어종
          selectedDate: null, // 날짜
          selectedTide: null, // 물때
          selectedTechnic: [], // 낚시기법
          selectedLure: [], // 미끼
          selectedPlace: null, // 낚시장소
          selectedLocation: null, // 업체 또는 위치
          uploadImages: [], // 업로드 이미지
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          DataStore: { getEnums },
        } = this.props;
        const options = await getEnums("postTitle");
        this.setState({ category_options: options });
      }

      uploadImage = async () => {
        // TODO : 글쓰기 : 이미지 업로드 API 필요
      };
      addStoryPost = async () => {
        // TODO : 글쓰기 : 글 등록 API 필요
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        // TODO : 글쓰기 : 업체 검색 API 필요
        // TODO : 글쓰기 : 선상 미입력 상태의 퍼블 필요.
        // TODO : 글쓰기 : 선상에 내 위치 선택시 표시 방법 퍼블 필요.
        return (
          <>
            {/** Navigation */}
            <Navigation title={"글쓰기"} showBack={true} />

            {/** 입력 > 카테고리, 제목 */}
            <div className="container nopadding">
              <div className="card">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <select ref={this.category} className="form-control">
                      <option value="">카테고리를 선택해 주세요.</option>
                      {this.state.category_options.length > 0 &&
                        this.state.category_options.map((data, index) => (
                          <option key={index} value={data.key}>
                            {data.value}
                          </option>
                        ))}
                    </select>
                  </div>
                  <div className="form-group">
                    <input
                      ref={this.title}
                      type="text"
                      className="form-control"
                      placeholder="제목을 입력해 주세요. (30자 이하) "
                      value={this.state.title}
                      onChange={(e) =>
                        this.setState({ title: e.target.value.substr(0, 30) })
                      }
                    />
                  </div>
                </form>
              </div>
            </div>

            {/** 입력 > 어종, 날짜, 물때, 낚시기법, 미끼, 낚시장소 */}
            <div className="container nopadding">
              <div className="card-box-grey">
                <dl className="dl-horizontal-round dl-line">
                  <dt>
                    어종 <span className="red">*</span>
                  </dt>
                  <a
                    href="#none"
                    data-toggle="modal"
                    data-target="#selFishModal"
                  >
                    <dd>
                      {this.state.selectedFish.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.codeName}{" "}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>
                    날짜 <span className="red">*</span>
                  </dt>
                  <a
                    href="#none"
                    data-toggle="modal"
                    data-target="#selDateModal"
                  >
                    <dd>
                      {this.state.selectedDate !== null && (
                        <React.Fragment>
                          {this.state.selectedDate.getFullYear() + "년 "}
                          {this.state.selectedDate.getMonth() + 1 + "월 "}
                          {this.state.selectedDate.getDate() + "일 "}
                          {this.props.DataStore.getWeek(
                            this.state.selectedDate
                          ) + "요일"}
                        </React.Fragment>
                      )}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>물때</dt>
                  <a
                    href="#none"
                    data-toggle="modal"
                    data-target="#selTideModal"
                  >
                    <dd>
                      {this.state.selectedTide !== null && (
                        <React.Fragment>
                          {this.state.selectedTide.value}
                        </React.Fragment>
                      )}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>낚시 기법</dt>
                  <a
                    href="#none"
                    data-toggle="modal"
                    data-target="#selTechnicModal"
                  >
                    <dd>
                      {this.state.selectedTechnic.length > 0 &&
                        this.state.selectedTechnic.map((data, index) => (
                          <React.Fragment key={index}>
                            {data.value}{" "}
                          </React.Fragment>
                        ))}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>미끼</dt>
                  <a
                    href="#none"
                    data-toggle="modal"
                    data-target="#selLureModal"
                  >
                    <dd>
                      {this.state.selectedLure.length > 0 &&
                        this.state.selectedLure.map((data, index) => (
                          <React.Fragment key={index}>
                            {data.codeName}{" "}
                          </React.Fragment>
                        ))}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>낚시 장소</dt>
                  <a
                    href="#none"
                    data-toggle="modal"
                    data-target="#selPlaceModal"
                  >
                    <dd>
                      {this.state.selectedPlace !== null && (
                        <React.Fragment>
                          {this.state.selectedPlace.value}
                        </React.Fragment>
                      )}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                </dl>
              </div>
            </div>

            {/** 입력 > 선상 */}
            <div className="container nopadding mt-3">
              <a
                href="#none"
                data-toggle="modal"
                data-target="#selCompanyAndLocationModal"
              >
                <div className="card-round-box pt-0 pb-0 pl-0">
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="cardimgWrap">
                      <img
                        src="/assets/img/sample/boat2.jpg"
                        className="img-fluid"
                        alt=""
                      />
                    </div>
                    <div className="cardInfoWrap">
                      <div className="card-body">
                        <img
                          src="/assets/img/svg/arrow-right.svg"
                          alt=""
                          className="float-right-arrow"
                        />
                        <h6>어복황제3호</h6>
                        <p>인천 중구 축항대로 142</p>
                      </div>
                    </div>
                  </div>
                </div>
              </a>
            </div>

            {/** 입력 > 이미지 */}
            <input
              ref={this.file}
              type="file"
              accept="image/*"
              style={{ display: "none" }}
              onChange={this.uploadImage}
            />
            <div className="container nopadding mt-3">
              <div className="row no-gutters d-flex align-items-center">
                <div className="col-3">
                  <a onClick={() => this.file.current?.click()}>
                    <div className="box-round-grey">
                      <img
                        src="/assets/img/svg/icon-plus-blue.svg"
                        alt=""
                        className="icon-sm icon-plus"
                      />
                    </div>
                  </a>
                </div>
                {this.state.uploadImages.length > 0 &&
                  this.state.uploadImages.map((data, index) => (
                    <div className="col-3">
                      <div className="box-round-grey">
                        <img
                          src="/assets/img/sample/photo3.jpg"
                          className="d-block w-100 photo-img"
                          alt=""
                        />
                      </div>
                    </div>
                  ))}
              </div>
            </div>

            {/** 안내 */}
            <div className="container nopadding mt-5">
              <div className="card card-box">
                <h6 className="card-header-white text-center">
                  <img
                    src="/assets/img/svg/icon-alert.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;알려드립니다.
                </h6>
                <div className="card-body">
                  <ul className="list mb-2">
                    <li>
                      대표 어종, 조황 날짜, 업체 또는 지역을 등록하지 않으면
                      글이 등록되지 않습니다.
                    </li>
                    <li>
                      사진(필수항목)은 최대 50장 까지, 동영상(선택항목)은 1개만
                      등록 가능합니다.
                    </li>
                    <li>
                      계좌번호, 전화번호, 홈페이지, 중복 게시글이 포함된 경우
                      삭제됩니다.
                    </li>
                  </ul>
                </div>
              </div>
            </div>

            <p className="clearfix">
              <br />
              <br />
            </p>

            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.addStoryPost}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    올리기
                  </a>
                </div>
              </div>
            </div>

            {/** 모달 팝업 */}
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) =>
                this.setState({ selectedFish: selected })
              }
            />
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) =>
                this.setState({ selectedDate: selected })
              }
            />
            <SelectTideModal
              id={"selTideModal"}
              onSelected={(selected) =>
                this.setState({ selectedTide: selected })
              }
            />
            <SelectTechnicModal
              id={"selTechnicModal"}
              onSelected={(selected) =>
                this.setState({ selectedTechnic: selected })
              }
            />
            <SelectLureModal
              id={"selLureModal"}
              onSelected={(selected) =>
                this.setState({ selectedLure: selected })
              }
            />
            <SelectPlaceModal
              id={"selPlaceModal"}
              onSelected={(selected) =>
                this.setState({ selectedPlace: selected })
              }
            />
            <SelectCompanyAndLocationModal id={"selCompanyAndLocationModal"} />
          </>
        );
      }
    }
  )
);
