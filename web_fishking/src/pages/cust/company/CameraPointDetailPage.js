/* global daum, kakao, $, Hls, videojs , Cloudcam, Player */
import React from "react";
import {inject, observer} from "mobx-react";
import {withRouter} from "react-router-dom";
import Components from "../../../components";
// import ReactHighcharts from 'react-highcharts';
import {ResponsiveLine} from '@nivo/line'
import {Defs, linearGradientDef} from "@nivo/core";
import ApexCharts from 'apexcharts';
import Chart from "apexcharts";

const {
    VIEW: {CompanyGoodListItemView, GoodsBlogListItemView},
    MODAL: {CompanyGoodsDetailModal, CompanyDetailWeatherModal},
    LAYOUT: {NavigationLayout, MainTab}
} = Components;

export default inject(
    "PageStore",
    "APIStore",
    "NativeStore",
    "ModalStore"
)(
    observer(
        withRouter(
            class extends React.Component {
                constructor(props) {
                    super(props);
                    this.container = React.createRef(null);
                    this.video = React.createRef(null);
                    // this.weatherIframe = React.createRef(null);
                    this.map = null;
                    this.mediaError = false;
                    this.state = {
                        connectionType: '',
                        loaded: false,
                        tidalLevelData: [],
                        liveVideo: "",
                        profileImage: "",
                    };


                }

                /********** ********** ********** ********** **********/

                /** function */
                /********** ********** ********** ********** **********/
                componentDidMount() {
                    this.loadPageData();
                }

                loadPageData = async () => {
                    window.scrollTo(0, 0);
                    const {
                        match: {
                            params: {id},
                        },
                        APIStore,
                        PageStore,
                        NativeStore,
                    } = this.props;

                    // NativeStore.postMessage('Connections', {});
                    document.addEventListener("message", event => {
                        this.setState({connectionType: event.data});
                    });
                    window.addEventListener("message", event => {
                        this.setState({connectionType: event.data});
                    });


                    //일별 날씨 정보를 보여주는 iframe 주소 필요한 해상 코드 받아오기. 선박에 대한 해상코드를 받아온다.
                    // let seaCode = await APIStore._get(`/v2/api/ship/${id}/seaCode`);
                    // await this.setState({"seaCode": seaCode})

                    //카메라 포인트 정보 조회.
                    let cameraPoint = await APIStore._get(`/v2/api/cameraPointPage/${id}`)
                    await this.setState({
                        //카메라 포인트 정보
                        "cameraPointName": cameraPoint["cameraPointDetail"]["name"],
                        profileImage: cameraPoint["cameraPointDetail"]["imgUrl"],
                        liveVideo: cameraPoint["cameraPointDetail"]["liveVideo"],
                        //시간대별 날씨
                        weather: cameraPoint["timelyWeather"],
                        //주간 날씨
                        dailyWeather: cameraPoint["dailyWeather"],
                    })

                    //선박 위치의 기상 정보 조회 api.
                    // let weather = await APIStore._get(`/v2/api/cameraPoint/${id}/weather`);
                    // await this.setState({"weather": weather});

                    //조위 데이터
                    // let tidalLevelData = await APIStore._get(`/v2/api/allTideList/cameraPoint/${id}`)
                    // let data = []
                    // let dataX = []
                    // let tidalLevelMax = 0
                    // for(var i=0; i<tidalLevelData.length; i++){
                    //     if(tidalLevelData[i].level > tidalLevelMax){tidalLevelMax = tidalLevelData[i].level}
                    //
                    //     if(tidalLevelData[i].dateTime.substring(14,16)=="00"){
                    //         data.push({"x": tidalLevelData[i].dateTime.substring(11, 13), "y": tidalLevelData[i].level})
                    //     }
                    //     else{
                    //         data.push({"x": tidalLevelData[i].dateTime.substring(11, 16), "y": tidalLevelData[i].level})
                    //     }
                    //
                    //     //x축에 표시할 데이터 추출.
                    //     if(
                    //         (tidalLevelData[i].dateTime.substring(14,16)=="00")
                    //         && (tidalLevelData[i].dateTime.substring(11, 13)%3==0)
                    //     ){
                    //         dataX.push(tidalLevelData[i].dateTime.substring(11, 13))
                    //     }
                    //     else if(
                    //         (tidalLevelData[i].dateTime.substring(11, 13) == new Date().getHours())
                    //         && (tidalLevelData[i].dateTime.substring(14, 16) == new Date().getMinutes())
                    //     ){
                    //         // dataX.push(tidalLevelData[i].dateTime.substring(11, 16))
                    //     }
                    // }
                    // tidalLevelData = [
                    //     {
                    //         "id": "japan",
                    //         "color": "hsl(200, 100%, 58%)",
                    //         "data": data
                    //     },
                    // ]
                    // await this.setState({
                    //     "tidalLevelData": tidalLevelData,
                    //     "tidalLevelDataX": dataX,
                    //     "tidalLevelMax": tidalLevelMax,
                    // })

                    //항구 주간 날씨 데이터
                    // let dailyWeather = await APIStore._get(`/v2/api/cameraPoint/${id}/dailyWeather`);
                    // await this.setState({"dailyWeather": dailyWeather});

                    // resolve.liveVideo =
                    //   "rtsp://vc-net2-ss.sktelecom.com:8558/playback?authtoken=DujPjs1larZJUObH%2FB7hbGGeGmnM7DWtBTgUPTIidC2kSQ6OUFJCPjU%2FhSkMr1KI3QKkWbD1KwEmcEWUkZ0WtGaNMhS07aCfSgmW0G1ng98VQ2TLOWUzJh1Kcn27AChFBKjs3Zz1NCiPTEbHeAXsWT9X%2B%2F6Aevf4CXVXGm2Mbf0hn9pXlWgR3W9gaL%2BSwmysMmxfkPzmnoHNM4MPp4y3ppO7PJAgWnHElymjo1gX7RFasyNGzcErx8fs2NZKG692&rtspURI=rtsp://222.237.231.101:8554/243757/Playback?sessionID=HdxPbOAfsj7Q7I2B8y8cfuufQkYr&dateTime=20210327T094125Z&scale=1";
                    // # 비디오 표시
                    // let resolve = await APIStore._get(`/v2/api/ship/19`);
                    // await this.setState({liveVideo: resolve.liveVideo})
                    // let liveVideo = resolve.liveVideo
                    if (this.state.liveVideo && this.state.liveVideo !== "") {
                        const video = document.querySelector("#video");
                        let url =
                            this.state.liveVideo ||
                            "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";

                        if (url.startsWith("rtsp://")) {
                            const player = new Player({streamUrl: url});
                            player.init();
                            // video.src = url;
                            // const player = Cloudcam.player("video", {
                            //   socket:
                            //     "ws://116.125.120.90:9000/streams/52fd554cc3ab32e99ed6e29f812cc6e2",
                            // });

                            // if (this.state.connectionType === 'wifi') {
                            //   player.start();
                            // }
                        } else if (Hls.isSupported()) {
                            if (url.includes('fishkingapp')) {
                                const liveMark = document.querySelector("#live-mark");
                                liveMark.style.display = 'none';
                                video.src = url;
                            } else {
                                const hls = new Hls({
                                    capLevelToPlayerSize: true,
                                    capLevelOnFPSDrop: true,
                                });
                                hls.attachMedia(video);
                                hls.on(Hls.Events.MEDIA_ATTACHED, () => {
                                    hls.loadSource(url);
                                    hls.on(Hls.Events.MANIFEST_PARSED, (e, data) => {
                                        this.mediaError = false;
                                        // setTimeout(() => {
                                        //   video.play();
                                        // }, 800);
                                    });
                                    hls.on(Hls.Events.ERROR, (e, data) => {
                                        const {type, details, fatal} = data;

                                        if (type === Hls.ErrorTypes.NETWORK_ERROR) {
                                            hls.startLoad();
                                        } else if (type === Hls.ErrorTypes.MEDIA_ERROR) {
                                            hls.detachMedia();
                                            setTimeout(() => {
                                                video.src = url;
                                            }, 800);
                                            this.mediaError = true;
                                        } else {
                                            console.error("MEDIA DESTROY");
                                            hls.destroy();
                                        }
                                    });
                                });
                            }
                            if (this.state.connectionType === 'wifi') {
                                video.play();
                            }
                        } else {
                            video.src = url;
                            video.addEventListener("loadedmetadata", () => {
                                // alert("video meta loaded");
                                // video.play();
                                if (this.state.connectionType === 'wifi') {
                                    video.play();
                                }
                            });
                        }
                    }

                    // # 별점 스크립트 로드
                    // PageStore.injectScript("/assets/cust/js/jquery.rateit.min.js", {
                    //     global: true,
                    // });
                    //
                    // // # 지도표시
                    // const options = {
                    //     center: new daum.maps.LatLng(resolve.latitude, resolve.longitude),
                    //     level: 7,
                    // };
                    // this.map = new daum.maps.Map(this.container.current, options);
                    // const marker = new kakao.maps.Marker({
                    //     position: new kakao.maps.LatLng(
                    //         resolve.latitude,
                    //         resolve.longitude
                    //     ),
                    // });
                    // marker.setMap(this.map);
                    // if ((resolve.rockData || []).length > 0) {
                    //     for (let rock of resolve.rockData) {
                    //         const m = new kakao.maps.Marker({
                    //             position: new kakao.maps.LatLng(rock.latitude, rock.longitude),
                    //         });
                    //         m.setMap(this.map);
                    //     }
                    // }
                    // this.setState({loaded: true});
                };
                requestLike = async () => {
                    const {APIStore, ModalStore} = this.props;
                    if (this.state.liked) {
                        await APIStore._delete("/v2/api/take", {
                            // takeType: "ship",
                            linkId: this.state.id,
                        });
                        this.setState({liked: false});
                        ModalStore.openModal("Alert", {body: "찜 목록에서 해제되었습니다."});
                    } else {
                        await APIStore._post("/v2/api/take", {
                            // takeType: "ship",
                            linkId: this.state.id,
                        });
                        this.setState({liked: true});
                        ModalStore.openModal("Alert", {
                            body: (
                                <React.Fragment>
                                    <p>
                                        찜되었습니다.
                                        <br/>
                                        찜 목록은 마이메뉴의 찜한 업체에서
                                        <br/>
                                        확인하실 수 있습니다.
                                    </p>
                                </React.Fragment>
                            ),
                        });
                    }
                };
                modalSNS = (type) => {
                    // >>>>> 업체 공유하기
                    const {ModalStore} = this.props;
                    let msg = null;
                    if (type === 'map') {
                        msg = `https://m.map.kakao.com/actions/searchView?q=${encodeURI(this.state.address)}#!/QOQSMTP,NVWSTR/map/car`
                    }
                    console.log(msg);
                    ModalStore.openModal("SNS", {
                        onSelect: (selected) => {
                            console.log(selected);
                        },
                        address: msg
                    });
                };
                copyAddress = () => {
                    const {NativeStore} = this.props;
                    NativeStore.clipboardCopy(this.state.address);
                };
                makeCall = () => {
                    const {ModalStore, NativeStore} = this.props;
                    ModalStore.openModal("Confirm", {
                        title: "전화걸기",
                        body: (
                            <p>
                                [해당 출조점으로 전화 연결합니다.]
                                <br/>
                                전화 연결시 통화 내용이
                                <br/>
                                녹음될 수 있습니다.
                            </p>
                        ),
                        onOk: () => {
                            NativeStore.linking(`tel://${this.state.tel}`);
                        },
                    });
                };
                findWay = () => {
                    const {NativeStore} = this.props;
                    NativeStore.openMap({
                        lat: this.state.latitude,
                        lng: this.state.longitude,
                    });
                };
                advice = () => {
                    const {PageStore} = this.props;
                    PageStore.push(
                        `/cs/qna/add?q=${encodeURI(
                            JSON.stringify({
                                contents: `선박id:${this.state.id}\n선박명:${this.state.name}\n\n신고/건의 내용:\n`,
                            })
                        )}`
                    );
                };
                goToStoryDiary = (item) => {
                    const {PageStore} = this.props;
                    PageStore.push(`/story/diary/detail/${item.id}`);
                };
                goToStoryUser = (item) => {
                    const {PageStore} = this.props;
                    PageStore.push(`/story/story/detail/${item.id}`);
                };
                /********** ********** ********** ********** **********/

                /** render */
                /********** ********** ********** ********** **********/
                render() {
                    const {
                        PageStore,
                        match: {
                            params: {id},
                        },
                    } = this.props;
                    return (
                        <React.Fragment>
                            <CompanyGoodsDetailModal
                                id={"goodsModal"}
                                data={this.state.goodsDetail || {}}
                            />

                            <NavigationLayout
                                title={this.state.cameraPointName}
                                showBackIcon={true}
                            />
                            {/** 상품이미지 */}
                            <div
                                id="carousel-visual-detail"
                                className="carousel slide"
                                data-ride="carousel"
                                style={{
                                    marginTop:"0px",
                                }}
                            >
                                {/*<div className="float-top-left">*/}
                                {/*  <a onClick={() => PageStore.goBack()}>*/}
                                {/*    <img*/}
                                {/*      src="/assets/cust/img/svg/navbar-back.svg"*/}
                                {/*      alt="뒤로가기"*/}
                                {/*    />*/}
                                {/*  </a>*/}
                                {/*</div>*/}
                                <div className="carousel-inner">
                                    <div className="carousel-item active">
                                        {this.state.liveVideo === "" &&
                                        this.state.profileImage === "" && (
                                            <React.Fragment>
                                                <img
                                                    src="/assets/cust/img/sample/boat1.jpg"
                                                    className="d-block w-100"
                                                    alt=""
                                                />
                                                <span className="play">
                                                    <img
                                                        src="/assets/cust/img/svg/live-play-big.svg"
                                                        alt=""
                                                    />
                                                </span>
                                                <div className="play-progress">
                                                    <div className="play-progress-time">01:21</div>
                                                    <div className="play-bar">
                                                        <div
                                                            className="play-on"
                                                            style={{width: "15%"}}
                                                        ></div>
                                                    </div>
                                                    <span
                                                        className="play-control"
                                                        style={{left: "20%"}}
                                                    ></span>
                                                    <div className="play-progress-time-all">02.57</div>
                                                </div>
                                                <div className="float-btm-right">
                                                    <a>
                                                        <img
                                                            src="/assets/cust/img/svg/play-sound-on.svg"
                                                            alt="사운드켜기"
                                                        />
                                                    </a>
                                                    <a>
                                                        <img
                                                            src="/assets/cust/img/svg/play-expand.svg"
                                                            alt="전체보기"
                                                        />
                                                    </a>
                                                </div>
                                            </React.Fragment>
                                        )}
                                        {this.state.liveVideo === "" &&
                                        this.state.profileImage !== "" && (
                                            <React.Fragment>
                                                <img
                                                    src={this.state.profileImage}
                                                    className="d-block w-100"
                                                    alt=""
                                                />
                                            </React.Fragment>
                                        )}
                                        {this.state.liveVideo !== "" && (
                                            <React.Fragment>
                                                <canvas
                                                    id="videoCanvas"
                                                    style={{
                                                        width: "100%",
                                                        display: this.state.liveVideo?.startsWith("rtsp://")
                                                            ? "block"
                                                            : "none",
                                                    }}
                                                />
                                                <video
                                                    id="video"
                                                    muted
                                                    playsInline
                                                    controls
                                                    // autoPlay
                                                    style={{
                                                        width: "100%",
                                                        display: !this.state.liveVideo?.startsWith(
                                                            "rtsp://"
                                                        )
                                                            ? "block"
                                                            : "none",
                                                    }}
                                                ></video>
                                                {/*<iframe width="560" height="315" src='https://vgai.capslive.co.kr/video/adt/MTkwMzg3'*/}
                                                {/*        frameborder="0"*/}
                                                {/*        allow="acceleroeter; autoplay; encrypted-media; gyroscope; picture-in-picture"*/}
                                                {/*        sandbox="allow-forms allow-modals allow-same-origin allow-script"*/}
                                                {/*        allowfullscreen*/}
                                                {/*        title='video'*/}
                                                {/*>*/}
                                                {/*</iframe>*/}
                                                <span
                                                    id="live-mark"
                                                    className="play-live"
                                                    style={{marginBottom: "8px", marginRight: "8px"}}
                                                >
                          LIVE
                        </span>
                                            </React.Fragment>
                                        )}
                                    </div>
                                </div>
                            </div>

                            <div className="container nopadding">
                                <h5>
                                    {/*<a*/}
                                    {/*    className="btn btn-round-grey btn-xs float-right-more"*/}
                                    {/*    data-toggle="modal" data-target="#selPlaceModal"*/}
                                    {/*    style={{*/}
                                    {/*        // "border":"solid 1.5px #0069d9",*/}
                                    {/*        "border-radius": "5px",*/}
                                    {/*        "padding": "6px 11px",*/}
                                    {/*        "font-size": "15px",*/}
                                    {/*        "color": "#333",*/}
                                    {/*    }}*/}
                                    {/*>*/}
                                    {/*    해상 예보*/}
                                    {/*</a>*/}
                                    날씨 정보
                                </h5>
                                <ul>
                                    <div className="shipDetail-weather">
                                        <table>
                                            <tr>
                                                <td><img src={this.state.weather && this.state.weather.weatherImg}/></td>
                                                <td>{this.state.weather && this.state.weather.tmp} ℃</td>
                                            </tr>
                                            <tr>
                                                <td>{this.state.weather && this.state.weather.weather}</td>
                                                <td>강수 {this.state.weather && this.state.weather.rainProbability} %</td>
                                            </tr>
                                            <tr>
                                                <td>습도 {this.state.weather && this.state.weather.humidity} %</td>
                                                <td>{this.state.weather &&
                                                (this.state.weather.windDirection + " " + this.state.weather.windSpeed)} m/s
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </ul>
                                {/*시간대별 날씨 */}
                                <h5>시간대별 날씨</h5>
                                <ul>
                                    <div className="harborDetail-timelyWeather">
                                        <table>
                                            <tr>
                                                {this.state.weather && this.state.weather.weatherByTime.map((data, index)=>(
                                                    <td>{data.time.substring(0,2)+":"+data.time.substring(2,4)}</td>
                                                ))}
                                            </tr>
                                            <tr>
                                                {this.state.weather && this.state.weather.weatherByTime.map((data, index)=>(
                                                    <td><img src={data.weatherImg}/></td>
                                                ))}
                                            </tr>
                                            <tr>
                                                {this.state.weather && this.state.weather.weatherByTime.map((data, index)=>(
                                                    <td>{data.tmp} ℃</td>
                                                ))}
                                            </tr>

                                        </table>
                                    </div>
                                </ul>
                                {/*시간대별 조위 */}
                                { (this.state.tidalLevelData.length != 0) && (
                                    <React.Fragment>
                                        <h5>조위</h5>
                                        <ul style={{}}>
                                            {/*<ReactHighcharts config={this.config}/>*/}
                                            <div className="harborDetail-tidal" >
                                                <ResponsiveLine
                                                    data={this.state.tidalLevelData}
                                                    margin={{ top: 0, right: 40, bottom: 50, left: 40 }}
                                                    xScale={{ type: 'point' }}
                                                    yScale={{
                                                        type: 'linear', min: 0, max: this.state.tidalLevelMax+100, stacked: true, reverse: false,
                                                    }}
                                                    yFormat=" >-.2f"
                                                    axisTop={null}
                                                    axisRight={null}
                                                    axisBottom={{
                                                        orient: 'bottom',
                                                        tickSize: 5,
                                                        tickPadding: 5,
                                                        tickRotation: 0,
                                                        tickValues: this.state.tidalLevelDataX,
                                                        // legend: '시간',
                                                        // legendOffset: 32,
                                                        // legendPosition: 'middle'
                                                    }}
                                                    axisLeft={{
                                                        orient: 'left',
                                                        tickSize: 5,
                                                        tickPadding: 5,
                                                        tickRotation: 0,
                                                        // legend: '조위',
                                                        // legendOffset: -30,
                                                        // legendPosition: 'top',

                                                    }}
                                                    axisRight={{
                                                        orient: 'right',
                                                        tickSize: 5,
                                                        tickPadding: 5,
                                                        tickRotation: 0,
                                                    }}
                                                    colors="#7fb6f0"
                                                    enableGridX={false}
                                                    enableGridY={true}
                                                    enablePoints={false}
                                                    pointSize={2}
                                                    pointColor={{ theme: 'background' }}
                                                    pointBorderWidth={2}
                                                    // pointBorderColor={{ from: 'serieColor' }}
                                                    pointBorderColor="#7fb6f0"
                                                    pointLabelYOffset={-12}
                                                    useMesh={true}
                                                    legends={[]}
                                                    enableArea={true}
                                                    defs={[
                                                        linearGradientDef('gradientA', [
                                                            { offset: 100, color: '#7fb6f0' },
                                                        ]),
                                                    ]}
                                                    fill={[{ match: '*', id: 'gradientA' }]}
                                                    markers={[
                                                        // {
                                                        //     axis: 'y',
                                                        //     value: 100,
                                                        //     lineStyle: { stroke: '#b0413e', strokeWidth: 2 },
                                                        //     legend: 'y marker',
                                                        //     legendOrientation: 'vertical',
                                                        // },
                                                        {
                                                            axis: 'x',
                                                            // value: data[0].data[5].x,
                                                            value: (parseInt(new Date().getMinutes()/10)===0)?
                                                                ((new Date().getHours())+"")
                                                                : ((new Date().getHours())+":"+parseInt(new Date().getMinutes()/10)+"0"),
                                                            lineStyle: { stroke: '#7fb6f0', strokeWidth: 0.5 },
                                                            legend: (new Date().getHours())+":"+parseInt(new Date().getMinutes()/10)+"0 현재",
                                                            height: 100,
                                                            legendPosition: "top",
                                                        },
                                                    ]}
                                                />
                                            </div>
                                        </ul>
                                    </React.Fragment>
                                )}

                                {/*날짜별 날씨 */}
                                <h5>주간 날씨</h5>
                                <ul>
                                    <div className="harborDetail-timelyWeather">
                                        <table>
                                            <tr>
                                                {this.state.dailyWeather && this.state.dailyWeather.map((data, index)=>(
                                                    <td>{data.date}</td>
                                                ))}
                                            </tr>
                                            <tr>
                                                {this.state.dailyWeather && this.state.dailyWeather.map((data, index)=>(
                                                    <td><img src={data.weatherPmImg || data.weatherImg}/></td>
                                                ))}
                                            </tr>
                                            <tr>
                                                {this.state.dailyWeather && this.state.dailyWeather.map((data, index)=>(
                                                    <td>{data.tmpMin + "~" + data.tmpMax + " ℃"}</td>
                                                ))}
                                            </tr>

                                        </table>
                                    </div>
                                </ul>


                                {/*<div className="space"></div>*/}
                            </div>

                            {/** 오류신고 */}
                            <div className="container nopadding">
                                <div className="warningWrap mt-3 text-center">
                                    <h5 className="align-items-center">
                                        <a onClick={this.advice}>
                                            <img
                                                src="/assets/cust/img/svg/icon-info.svg"
                                                alt=""
                                                className="vam"
                                            />{" "}
                                            <span className="grey">잘못된 정보 알리기</span>
                                        </a>
                                    </h5>
                                    <p>
                                        <small className="grey">
                                            ㈜투비는 통신판매중계자로서 통신판매의 당사자가 아니며,
                                            <br/>
                                            상품의 예약, 이용 및 환불 등과 관련한 의무와 책임은 각
                                            판매자에게 있습니다.
                                        </small>
                                    </p>
                                </div>
                            </div>

                            {/** 하단버튼 */}
                            {/*<div className="fixed-bottom">*/}
                            {/*    <div className="row no-gutters">*/}
                            {/*        <div className="col-3">*/}
                            {/*            <a*/}
                            {/*                onClick={this.makeCall}*/}
                            {/*                className="btn btn-secondary btn-lg btn-block"*/}
                            {/*            >*/}
                            {/*                <img*/}
                            {/*                    src="/assets/cust/img/svg/icon-call-w.svg"*/}
                            {/*                    alt=""*/}
                            {/*                    className="vam"*/}
                            {/*                />*/}
                            {/*            </a>*/}
                            {/*        </div>*/}
                            {/*        <div className="col-9">*/}
                            {/*            <a*/}
                            {/*                onClick={() => PageStore.push(`/reservation/goods/${id}`)}*/}
                            {/*                className="btn btn-primary btn-lg btn-block"*/}
                            {/*            >*/}
                            {/*                예약하기*/}
                            {/*            </a>*/}
                            {/*        </div>*/}
                            {/*    </div>*/}
                            {/*</div>*/}

                            {/*<CompanyDetailWeatherModal*/}
                            {/*    id={"selPlaceModal"}*/}
                            {/*    seaCode={this.state.seaCode}*/}
                            {/*/>*/}
                            <MainTab activeIndex={5} />
                        </React.Fragment>
                    );
                }
            }
        )
    )
);
