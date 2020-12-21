import { createStore as createPageStore } from "./common/PageStore";
import { createStore as createMemberStore } from "./MemberStore";
import { createStore as createCodeStore } from "./common/CodeStore";
import { createStore as createExternalStore } from "./common/ExternalStore";
import { createStore as createSNSStore } from "./common/SNSStore";
import { createStore as createValidStore } from "./common/ValidStore";
import { createStore as createMyMenuStore } from "./MyMenuStore";
import { createStore as createCouponStore } from "./CouponStore";
import { createStore as createTakeStore } from "./TakeStore";

const PageStore = createPageStore();
const MemberStore = createMemberStore();
const CodeStore = createCodeStore();
const ExternalStore = createExternalStore();
const SNSStore = createSNSStore();
const ValidStore = createValidStore();
const MyMenuStore = createMyMenuStore();
const CouponStore = createCouponStore();
const TakeStore = createTakeStore();

export const Stores = {
  PageStore,
  MemberStore,
  CodeStore,
  ExternalStore,
  SNSStore,
  ValidStore,
  MyMenuStore,
  CouponStore,
  TakeStore,
};
