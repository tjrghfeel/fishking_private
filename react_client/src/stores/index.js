import { createStore as createRouteStore } from "./common/RouteStore.ts";
import { createStore as createPageStore } from "./common/PageStore";
import { createStore as createMemberStore } from "./common/MemberStore";
import { createStore as createCodeStore } from "./common/CodeStore";
import { createStore as createExternalStore } from "./common/ExternalStore";

const RouteStore = createRouteStore();
const PageStore = createPageStore();
const MemberStore = createMemberStore();
const CodeStore = createCodeStore();
const ExternalStore = createExternalStore();

export const Stores = {
  RouteStore,
  PageStore,
  MemberStore,
  CodeStore,
  ExternalStore,
};
