/********** ********** ********** ********** **********/
/** MODAL */
/********** ********** ********** ********** **********/
import AlertModal from "./modal/AlertModal";
import ConfirmModal from "./modal/ConfirmModal";
import CouponModal from "./modal/CouponModal";
import SelectModal from "./modal/SelectModal";
import SelectFishModal from "./modal/SelectFishModal";
import SelectDateModal from "./modal/SelectDateModal";
import SelectTideModal from "./modal/SelectTideModal";
import SelectTechnicModal from "./modal/SelectTechnicModal";
import SelectLureModal from "./modal/SelectLureModal";
import SelectPlaceModal from "./modal/SelectPlaceModal";
import SelectLocationModal from "./modal/SelectLocationModal";
import AddCouponModal from "./modal/AddCouponModal";
import SNSModal from "./modal/SNSModal";

/********** ********** ********** ********** **********/
/** LAYOUT */
/********** ********** ********** ********** **********/
import LoadingLayout from "./layout/LoadingLayout";
import NavigationLayout from "./layout/NavigationLayout";
import MainTab from "./layout/MainTab";
import CsTab from "./layout/CsTab";
import QnaTab from "./layout/QnaTab";
import StoryMyTab from "./layout/StoryMyTab";
import ZzimTab from "./layout/ZzimTab";
/********** ********** ********** ********** **********/
/** ITEM */
/********** ********** ********** ********** **********/

/********** ********** ********** ********** **********/
/** VIEW */
/********** ********** ********** ********** **********/
import FaqListView from "./view/FaqListView";
import ApplyAddView from "./view/ApplyAddView";
import ApplyEndView from "./view/ApplyEndView";
import QnaAddView from "./view/QnaAddView";
import QnaListView from "./view/QnaListView";
import QnaDetailView from "./view/QnaDetailView";
import NoticeListView from "./view/NoticeListView";
import NoticeDetailView from "./view/NoticeDetailView";
import StoryMyPostListView from "./view/StoryMyPostListView";
import StoryMyCommentListView from "./view/StoryMyCommentListView";
import StoryMyScrapListView from "./view/StoryMyScrapListView";
import StoryMyReviewListView from "./view/StoryMyReviewListView";
import AlarmListItemView from "./view/AlarmListItemView";
import ZzimListView from "./view/ZzimListView";
import ZzimListItemView from "./view/ZzimListItemView";
import CouponMyListItemView from "./view/CouponMyListItemView";
import CouponAvailableListItemView from "./view/CouponAvailableListItemView";
import ReservationMyListItemView from "./view/ReservationMyListItemView";

export default {
  /********** MODAL **********/
  MODAL: {
    AlertModal,
    ConfirmModal,
    CouponModal,
    SelectModal,
    SelectFishModal,
    SelectDateModal,
    SelectTideModal,
    SelectTechnicModal,
    SelectLureModal,
    SelectPlaceModal,
    SelectLocationModal,
    AddCouponModal,
    SNSModal,
  },
  /********** LAYOUT **********/
  LAYOUT: {
    LoadingLayout,
    NavigationLayout,
    MainTab,
    CsTab,
    QnaTab,
    StoryMyTab,
    ZzimTab,
  },
  /********** ITEM **********/
  ITEM: {},
  /********** VIEW **********/
  VIEW: {
    FaqListView,
    ApplyAddView,
    ApplyEndView,
    QnaAddView,
    QnaListView,
    QnaDetailView,
    NoticeListView,
    NoticeDetailView,
    StoryMyPostListView,
    StoryMyCommentListView,
    StoryMyScrapListView,
    StoryMyReviewListView,
    AlarmListItemView,
    ZzimListView,
    ZzimListItemView,
    CouponMyListItemView,
    CouponAvailableListItemView,
    ReservationMyListItemView,
  },
};
