import BlankPage from "./BlankPage";

import FishkingMainMyPage from "./fishking/main/FishkingMainMyPage";

import FishkingCommonNoticePage from "./fishking/common/FishkingCommonNoticePage";
import FishkingCommonNoticeDetailPage from "./fishking/common/FishkingCommonNoticeDetailPage";

import FishkingCsFaqPage from "./fishking/cs/FishkingCsFaqPage";
import FishkingCsApplyPage from "./fishking/cs/FishkingCsApplyPage";
import FishkingCsQnaPage from "./fishking/cs/FishkingCsQnaPage";
import FishkingCsQnaListPage from "./fishking/cs/FishkingCsQnaListPage";
import FishkingCsQnaDetailPage from "./fishking/cs/FishkingCsQnaDetailPage";

import FishkingMemberLoginPage from "./fishking/member/FishkingMemberLoginPage";
import FishkingMemberFindPwPage from "./fishking/member/FishkingMemberFindPwPage";
import FishkingMemberSignupPage from "./fishking/member/FishkingMemberSignupPage";
import FishkingMemberSignoutPage from "./fishking/member/FishkingMemberSignoutPage";

import FishkingReservationMyPage from "./fishking/reservation/FishkingReservationMyPage";

import CommonAlarmMyPage from "./common/alarm/CommonAlarmMyPage";

import CommonCouponMyPage from "./common/coupon/CommonCouponMyPage";

import CommonPolicyMainPage from "./common/policy/CommonPolicyMainPage";
import CommonPolicyTermsPage from "./common/policy/CommonPolicyTermsPage";
import CommonPolicyPrivacyPage from "./common/policy/CommonPolicyPrivacyPage";
import CommonPolicyCancelPage from "./common/policy/CommonPolicyCancelPage";
import CommonPolicyLbsPage from "./common/policy/CommonPolicyLbsPage";
import CommonPolicyAgreePage from "./common/policy/CommonPolicyAgreePage";

import CommonSetMainPage from "./common/set/CommonSetMainPage";
import CommonSetProfilePage from "./common/set/CommonSetProfilePage";
import CommonSetProfileNicknamePage from "./common/set/CommonSetProfileNicknamePage";
import CommonSetProfileStatusPage from "./common/set/CommonSetProfileStatusPage";
import CommonSetProfileEmailPage from "./common/set/CommonSetProfileEmailPage";
import CommonSetProfilePasswordPage from "./common/set/CommonSetProfilePasswordPage";
import CommonSetAlarmPage from "./common/set/CommonSetAlarmPage";
import CommonSetVodPage from "./common/set/CommonSetVodPage";

export default {
  BlankPage,
  Fishking: {
    Main: {
      MyPage: FishkingMainMyPage,
    },
    Common: {
      NoticePage: FishkingCommonNoticePage,
      NoticeDetailPage: FishkingCommonNoticeDetailPage,
    },
    Cs: {
      FaqPage: FishkingCsFaqPage,
      ApplyPage: FishkingCsApplyPage,
      QnaPage: FishkingCsQnaPage,
      QnaListPage: FishkingCsQnaListPage,
      QnaDetailPage: FishkingCsQnaDetailPage,
    },
    Member: {
      LoginPage: FishkingMemberLoginPage,
      FindPwPage: FishkingMemberFindPwPage,
      SignupPage: FishkingMemberSignupPage,
      SignoutPage: FishkingMemberSignoutPage,
    },
    Reservation: {
      MyPage: FishkingReservationMyPage,
    },
  },
  Common: {
    Alarm: {
      MyPage: CommonAlarmMyPage,
    },
    Coupon: {
      MyPage: CommonCouponMyPage,
    },
    Policy: {
      MainPage: CommonPolicyMainPage,
      TermsPage: CommonPolicyTermsPage,
      PrivacyPage: CommonPolicyPrivacyPage,
      CancelPage: CommonPolicyCancelPage,
      LbsPage: CommonPolicyLbsPage,
      AgreePage: CommonPolicyAgreePage,
    },
    Set: {
      MainPage: CommonSetMainPage,
      ProfilePage: CommonSetProfilePage,
      ProfileNicknamePage: CommonSetProfileNicknamePage,
      ProfileStatusPage: CommonSetProfileStatusPage,
      ProfileEmailPage: CommonSetProfileEmailPage,
      ProfilePasswordPage: CommonSetProfilePasswordPage,
      AlarmPage: CommonSetAlarmPage,
      VodPage: CommonSetVodPage,
    },
  },
};
