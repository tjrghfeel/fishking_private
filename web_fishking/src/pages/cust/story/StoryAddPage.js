/* global daum, kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  MODAL: {
    SelectFishModal,
    SelectDateModal,
    SelectTideModal,
    SelectTechnicModal,
    SelectLureModal,
    SelectPlaceModal,
    SelectLocationModal,
  },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.file = React.createRef(null);
        this.title = React.createRef(null);
        this.cardMapContainer = React.createRef(null);
        this.cardMap = null;
        this.cardMapMarkers = [];
        this.state = {
          isPageUpdate: false,
          showCardMap: false,
          uploaded: [],
          category: "fishingBlog",
          title: "",
          fishingSpecies: [],
          fishingSpeciesName: [],
          fishingDate: null,
          tide: null,
          tideName: "",
          fishingTechnicList: [],
          fishingTechnicListName: [],
          fishingLureList: [],
          fishingLureListName: [],
          fishingType: null,
          fishingTypeName: "",
          shipId: null,
          shipData: {},
          content: "",
          videoId: null,
          address: null,
          latitude: null,
          longitude: null,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { PageStore, APIStore } = this.props;
        const { put: fishingDiaryId, iscompany } = PageStore.getQueryParams();
        if (iscompany == "Y") await this.setState({ category: "fishingDiary" });
        if (fishingDiaryId) {
          const resolve = await APIStore._get("/v2/api/fishingDiary/detail", {
            fishingDiaryId,
          });
          // 첨부이미지
          const uploaded = [];
          if (resolve.imageUrlList) {
            for (let i = 0; i < resolve.imageUrlList.length; i++) {
              const downloadUrl = resolve.imageUrlList[i];
              const fileId = resolve.imageIdList[i];
              uploaded.push({ fileId, downloadUrl });
            }
          }
          let category = "";
          if (resolve["category"]) category = resolve.category;
          else if (resolve["fishingDiaryType"] == "조행기")
            category = "fishingBlog";
          else category = "fishingDiary";
          category = await this.setState({
            fishingDiaryId: resolve.fishingDiaryId,
            category,
            isPageUpdate: true,
            title: resolve.title,
            // 어종
            fishingSpeciesName: resolve.fishingSpecies
              ?.replace(/[ ]/g, "")
              .split(","),
            fishingSpecies: resolve.fishingSpeciesCodeList,
            // 날짜
            fishingDate: resolve.fishingDate,
            // 물때
            tide: resolve.tideCode,
            tideName: resolve.tide,
            // 낚시기법
            fishingTechnicList: resolve.fishingTechnicCodeList,
            fishingTechnicListName: resolve.fishingTechnic
              ?.replace(/[ ]/g, "")
              .split(","),
            // 미끼
            fishingLureList: resolve.fishingLureCodeList,
            fishingLureListName: resolve.fishingLure
              ?.replace(/[ ]/g, "")
              .split(","),
            // 낚시장소
            fishingTypeName: resolve.fishingType,
            // 선상/위치
            // 내용
            content: resolve.content,
            // 첨부이미지
            uploaded,
          });
          if (resolve.shipId === null) {
            this.setState({
              shipData: { address: resolve.address },
              address: resolve.address,
              latitude: resolve.latitude,
              longitude: resolve.longitude,
              showCardMap: true,
            });
            this.moveCardMap();
          } else {
            this.setState({
              shipId: resolve.shipId,
              shipData: {
                itemType: "Company",
                name: "",
                address: resolve.shipAddress,
                distance: null,
                thumbnailUrl: resolve.shipImageUrl,
                fishingType: null,
                shipId: resolve.shipId,
              },
              address: null,
              latitude: null,
              longitude: null,
              showCardMap: false,
            });
          }
        }
      }

      moveCardMap = () => {
        for (let m of this.cardMapMarkers) {
          m.setMap(null);
        }
        const { latitude, longitude } = this.state;

        if (this.cardMap === null) {
          const options = {
            center: new daum.maps.LatLng(latitude, longitude),
            level: 7,
          };
          this.cardMap = new daum.maps.Map(
            this.cardMapContainer.current,
            options
          );
        }

        const markerPosition = new kakao.maps.LatLng(latitude, longitude);
        const marker = new kakao.maps.Marker({
          position: markerPosition,
        });
        marker.setMap(this.cardMap);

        this.cardMapMarkers.push(marker);

        const moveLatLon = new kakao.maps.LatLng(latitude, longitude);
        this.cardMap.setCenter(moveLatLon);

        setTimeout(() => {
          this.cardMap.relayout();
        }, 100);
      };

      onSubmit = async () => {
        const { ModalStore, APIStore, PageStore } = this.props;
        const {
          category,
          title,
          fishingSpecies = [],
          fishingDate,
          tide,
          fishingTechnicList = [],
          fishingLureList = [],
          fishingType,
          shipId,
          content,
          fileList = [],
          uploaded,
          videoId,
          address,
          latitude,
          longitude,
        } = this.state;
        for (let file of uploaded) {
          fileList.push(file.fileId);
        }

        if (title === "") {
          ModalStore.openModal("Alert", { body: "제목을 입력해주세요." });
          return;
        }
        if (fishingSpecies.length === 0) {
          ModalStore.openModal("Alert", { body: "어종을 선택해주세요." });
          return;
        }
        if (fishingDate === null) {
          ModalStore.openModal("Alert", { body: "날짜를 선택해주세요." });
          return;
        }
        if (shipId === null && address === null) {
          ModalStore.openModal("Alert", {
            body: "업체 또는 위치를 선택해주세요.",
          });
          return;
        }
        if (content === "") {
          ModalStore.openModal("Alert", { body: "내용을 입력해주세요." });
          return;
        }
        if (fileList.length === 0) {
          ModalStore.openModal("Alert", { body: "이미지를 업로드해주세요." });
          return;
        }

        const params = {
          category,
          title,
          fishingSpecies,
          fishingDate,
          tide,
          fishingTechnicList,
          fishingLureList,
          fishingType,
          shipId,
          content,
          fileList,
          videoId,
          address,
          latitude,
          longitude,
        };
        let resolve = null;
        if (this.state.fishingDiaryId) {
          resolve = await APIStore._put("/v2/api/fishingDiary", {
            fishingDiaryId: this.state.fishingDiaryId,
            ...params,
          });
        } else {
          resolve = await APIStore._post("/v2/api/fishingDiary", params);
        }

        if (resolve) {
          ModalStore.openModal("Alert", {
            body: "저장되었습니다.",
            onOk: () => {
              PageStore.goBack();
            },
          });
        }
      };
      uploadFile = async () => {
        const { ModalStore } = this.props;

        if (this.file.current?.files.length > 0) {
          const file = this.file.current?.files[0];

          if (!file.type?.includes("video")) {
            let imageCount = 0;
            for (let item of this.state.uploaded) {
              if (item.downloadUrl.endsWith(".mp4")) continue;
              else imageCount = imageCount + 1;
            }

            if (imageCount >= 20) {
              ModalStore.openModal("Alert", {
                body: "최대 20장까지 가능합니다.",
              });
              this.file.current.value = null;
              return;
            }
          } else if (
            file.type?.includes("video") &&
            this.state.videoId !== null
          ) {
            ModalStore.openModal("Alert", {
              body: "비디오는 1개만 업로드 가능합니다.",
            });
            this.file.current.value = null;
            return;
          }

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", this.state.category);

          const { APIStore } = this.props;
          let upload = await APIStore._post_upload(
            "/v2/api/filePreUpload",
            form
          );

          if (upload) {
            this.setState({ uploaded: this.state.uploaded.concat(upload) });
            if (file.type?.includes("video")) {
              this.setState({ videoId: upload["fileId"] });
            }
          }
          this.file.current.value = null;
        }
      };
      removeUploadFile = (fileId, isVideo = false) => {
        const { DataStore } = this.props;
        const uploaded = DataStore.removeItemOfArrayByKey(
          this.state.uploaded,
          "fileId",
          fileId
        );
        this.setState({ uploaded });
        if (isVideo) this.setState({ videoId: null });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => {
                const fishingSpecies = [];
                const fishingSpeciesName = [];
                if (selected.length > 0) {
                  for (let item of selected) {
                    fishingSpecies.push(item.code);
                    fishingSpeciesName.push(item.codeName);
                  }
                }
                this.setState({ fishingSpecies, fishingSpeciesName });
              }}
            />
            <SelectDateModal
              id={"selDateModal"}
              until={new Date()}
              onSelected={(selected) => {
                if (selected) {
                  const year = selected.getFullYear();
                  const month =
                    selected.getMonth() + 1 < 10
                      ? "0".concat(selected.getMonth() + 1)
                      : selected.getMonth() + 1;
                  const date =
                    selected.getDate() < 10
                      ? "0".concat(selected.getDate())
                      : selected.getDate();
                  this.setState({ fishingDate: `${year}-${month}-${date}` });
                }
              }}
            />
            <SelectTideModal
              id={"selTideModal"}
              onSelected={(selected) =>
                this.setState({
                  tide: selected === null ? null : selected.key,
                  tideName: selected === null ? "" : selected.value,
                })
              }
            />
            <SelectTechnicModal
              id={"selTechnicModal"}
              onSelected={(selected) => {
                let fishingTechnicList = null;
                const fishingTechnicListName = [];
                if (selected.length > 0) {
                  fishingTechnicList = [];
                  for (let item of selected) {
                    fishingTechnicList.push(item.key);
                    fishingTechnicListName.push(item.value);
                  }
                }
                this.setState({ fishingTechnicList, fishingTechnicListName });
              }}
            />
            <SelectLureModal
              id={"selLureModal"}
              onSelected={(selected) => {
                let fishingLureList = null;
                const fishingLureListName = [];
                if (selected.length > 0) {
                  fishingLureList = [];
                  for (let item of selected) {
                    fishingLureList.push(item.code);
                    fishingLureListName.push(item.codeName);
                  }
                }
                this.setState({ fishingLureList, fishingLureListName });
              }}
            />
            <SelectPlaceModal
              id={"selPlaceModal"}
              onSelected={(selected) => {
                this.setState({
                  fishingType: selected === null ? null : selected.key,
                  fishingTypeName: selected === null ? "" : selected.value,
                });
              }}
            />
            <SelectLocationModal
              id={"selLocationModal"}
              onSelected={async (selected) => {
                if (selected.itemType === "Company") {
                  await this.setState({
                    shipId: selected.shipId,
                    shipData: selected,
                    address: null,
                    latitude: null,
                    longitude: null,
                    showCardMap: false,
                  });
                } else if (selected.itemType === "Location") {
                  await this.setState({
                    shipId: null,
                    shipData: { address: selected.address },
                    address: selected.address,
                    latitude: selected.lat,
                    longitude: selected.lng,
                    showCardMap: true,
                  });
                  this.moveCardMap();
                }
              }}
            />

            <NavigationLayout title={"글쓰기"} showBackIcon={true} />

            {/** 카테고리 / 제목 */}
            <div className="container nopadding">
              <div className="card">
                <form className="form-line mt-3">
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
            {/** 어종 / 날짜 / 물때 / 낚시기법 / 미끼 / 낚시장소 */}
            <div className="container nopadding">
              <div className="card-box-grey">
                <dl className="dl-horizontal-round dl-line">
                  <a data-toggle="modal" data-target="#selFishModal">
                    <dt>
                      어종 <span className="red">*</span>
                    </dt>
                  </a>
                  <a data-toggle="modal" data-target="#selFishModal">
                    <dd>
                      {this.state.fishingSpeciesName?.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.concat(" ")}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/cust/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>

                  <a data-toggle="modal" data-target="#selDateModal">
                    <dt>
                      날짜 <span className="red">*</span>
                    </dt>
                  </a>
                  <a data-toggle="modal" data-target="#selDateModal">
                    <dd>
                      {this.state.fishingDate && (
                        <React.Fragment>
                          {this.state.fishingDate.substr(0, 4).concat("년 ")}
                          {this.state.fishingDate.substr(5, 2).concat("월 ")}
                          {this.state.fishingDate.substr(8, 2).concat("일 ")}
                          {this.state.fishingDate
                            .replace(/[-]/g, "")
                            .getWeek()
                            .concat("요일")}
                        </React.Fragment>
                      )}
                      <img
                        src="/assets/cust/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>

                  <a data-toggle="modal" data-target="#selTideModal">
                    <dt>물때</dt>
                  </a>
                  <a data-toggle="modal" data-target="#selTideModal">
                    <dd>
                      {this.state.tideName}
                      <img
                        src="/assets/cust/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>

                  <a data-toggle="modal" data-target="#selPlaceModal">
                    <dt>낚시 장소</dt>
                  </a>
                  <a data-toggle="modal" data-target="#selPlaceModal">
                    <dd>
                      {!this.state.fishingTypeName && (
                        <span style={{color: 'rgba(116,124,132,0.9)', fontWeight: 'normal'}}>선상, 갯바위, 방파제 선택</span>
                      )}
                      {this.state.fishingTypeName}
                      <img
                        src="/assets/cust/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <a data-toggle="modal" data-target="#selTechnicModal">
                    <dt>낚시 기법</dt>
                  </a>
                  <a data-toggle="modal" data-target="#selTechnicModal">
                    <dd>
                      {this.state.fishingTechnicListName?.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.concat(" ")}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/cust/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>

                  <a data-toggle="modal" data-target="#selLureModal">
                    <dt>미끼</dt>
                  </a>
                  <a data-toggle="modal" data-target="#selLureModal">
                    <dd>
                      {this.state.fishingLureListName?.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.concat(" ")}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/cust/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                </dl>
              </div>
            </div>
            {/** 선상 */}
            <div className="container nopadding mt-3">
              <a data-toggle="modal" data-target="#selLocationModal">
                <div className="card-round-box pt-0 pb-0 pl-0">
                  <div className="row no-gutters d-flex align-items-center">
                    <div
                      className="cardimgWrap"
                      style={{
                        display: this.state.showCardMap ? "none" : "block",
                      }}
                    >
                      <img
                        src={
                          this.state.shipData.itemType === "Company"
                            ? this.state.shipData.thumbnailUrl
                            : "/assets/cust/img/sample/boat2.jpg"
                        }
                        className="img-fluid"
                        alt=""
                      />
                    </div>
                    <div
                      ref={this.cardMapContainer}
                      id="card-map"
                      className="cardimgWrap"
                      style={{
                        display: this.state.showCardMap ? "inline" : "none",
                        borderTopLeftRadius: "4px",
                        borderBottomLeftRadius: "4px",
                      }}
                    ></div>
                    <div className="cardInfoWrap">
                      <div className="card-body">
                        <img
                          src="/assets/cust/img/svg/arrow-right.svg"
                          alt=""
                          className="float-right-arrow"
                        />
                        {this.state.shipData.itemType === "Company" && (
                          <h6>
                            {this.state.shipData.name || "선박 또는 위치선택"}
                          </h6>
                        )}
                        <p>
                          {this.state.shipData.address || "선박 또는 위치선택"}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </a>
            </div>
            {/** 문구 */}
            <div className="container nopadding mt-3">
              <div className="row no-gutters">
                <div className="form-group col-12">
                  <div className="input-group">
                    <textarea
                      className="form-control"
                      rows="15"
                      style={{ height: "170px !important" }}
                      placeholder="대표 어종, 조황 날짜, 업체 또는 지역을 등록하지 않으면 글이 등록되지 않습니다.
사진(필수항목)은 최대 50장 까지, 동영상(선택항목)은 1개만 등록 가능합니다.
계좌번호, 전화번호, 홈페이지, 중복 게시글이 포함된 경우 삭제됩니다."
                      onChange={(e) =>
                        this.setState({
                          content: e.target.value.substr(0, 1000),
                        })
                      }
                      value={this.state.content}
                    ></textarea>
                  </div>
                </div>
              </div>
            </div>
            {/** 첨부 이미지 */}
            <input
              ref={this.file}
              type="file"
              accept="video/*,image/*"
              // capture="camera"
              style={{ display: "none" }}
              onChange={this.uploadFile}
            />
            <div className="container nopadding mt-3">
              <div className="row no-gutters d-flex align-items-center">
                <div className="col-3">
                  <a onClick={() => this.file.current?.click()}>
                    <div className="box-round-grey">
                      <img
                        src="/assets/cust/img/svg/icon-plus-blue.svg"
                        alt=""
                        className="icon-sm icon-plus"
                      />
                    </div>
                  </a>
                </div>
                {this.state.uploaded?.map((data, index) => {
                  if (data["downloadUrl"].includes("mp4")) {
                    return (
                      <div className="col-3" key={index}>
                        <div className="box-round-grey">
                          <a
                            onClick={() =>
                              this.removeUploadFile(data.fileId, true)
                            }
                            className="del"
                          >
                            <img src="/assets/cust/img/svg/icon_close_white.svg" />
                          </a>
                          비디오
                        </div>
                      </div>
                    );
                  } else {
                    return (
                      <div className="col-3" key={index}>
                        <div className="box-round-grey">
                          <a
                            onClick={() => this.removeUploadFile(data.fileId)}
                            className="del"
                          >
                            <img src="/assets/cust/img/svg/icon_close_white.svg" />
                          </a>
                          <img
                            src={data.downloadUrl}
                            className="d-block w-100 photo-img"
                            alt=""
                          />
                        </div>
                      </div>
                    );
                  }
                })}
              </div>
            </div>
            {/** 안내 */}
            <div className="container nopadding mt-5">
              <div className="card card-box">
                <h6 className="card-header-white text-center">
                  <img
                    src="/assets/cust/img/svg/icon-alert.svg"
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
            {/** 하단버튼 */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.onSubmit}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    올리기
                  </a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
