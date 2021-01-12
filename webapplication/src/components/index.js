import CouponMyItem from "./common/item/CouponMyItem";
import AlarmItem from "./common/item/AlarmItem";
import QnaItem from "./common/item/QnaItem";
import FaqItem from "./common/item/FaqItem";
import NoticeItem from "./common/item/NoticeItem";

import Loading from "./common/layout/Loading";
import Navigation from "./common/layout/Navigation";

import AlertModal from "./common/modal/AlertModal";
import ConfirmModal from "./common/modal/ConfirmModal";
import SelectModal from "./common/modal/SelectModal";
import CouponModal from "./common/modal/CouponModal";

import CommonCsFaqListView from "./common/view/CommonCsFaqListView";
import CommonCsApplyView from "./common/view/CommonCsApplyView";
import CommonCsQnaRegistView from "./common/view/CommonCsQnaRegistView";
import CommonCsQnaListView from "./common/view/CommonCsQnaListView";
import CommonCsQnaDetailView from "./common/view/CommonCsQnaDetailView";
import CommonNoticeListView from "./common/view/CommonNoticeListView";
import CommonNoticeDetailView from "./common/view/CommonNoticeDetailView";

import BottomTab from "./fishking/layout/BottomTab";
import CsTopTab from "./fishking/layout/CsTopTab";
import CsQnaTopTab from "./fishking/layout/CsQnaTopTab";

export default {
  Common: {
    Item: { CouponMyItem, AlarmItem, QnaItem, FaqItem, NoticeItem },
    Layout: { Loading, Navigation },
    Modal: { AlertModal, ConfirmModal, SelectModal, CouponModal },
    View: {
      CommonCsFaqListView,
      CommonCsApplyView,
      CommonCsQnaRegistView,
      CommonCsQnaListView,
      CommonCsQnaDetailView,
      CommonNoticeListView,
      CommonNoticeDetailView,
    },
  },
  Fishking: {
    Layout: { BottomTab, CsTopTab, CsQnaTopTab },
  },
};
