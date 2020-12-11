import { createStore as createPageStore } from "./common/PageStore";
import { createStore as createMemberStore } from "./common/MemberStore";
import { createStore as createCodeStore } from "./common/CodeStore";
import { createStore as createExternalStore } from "./common/ExternalStore";
import { createStore as createSNSStore } from "./common/SNSStore";

const PageStore = createPageStore();
const MemberStore = createMemberStore();
const CodeStore = createCodeStore();
const ExternalStore = createExternalStore();
const SNSStore = createSNSStore();

export const Stores = {
  PageStore,
  MemberStore,
  CodeStore,
  ExternalStore,
  SNSStore,
};
