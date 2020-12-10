import { createStore as createRouteStore } from "./common/RouteStore.ts";
import { createStore as createPageStore } from "./common/PageStore";
import { createStore as createMemberStore } from "./common/MemberStore";
import { createStore as createCodeStore } from "./common/CodeStore";

const RouteStore = createRouteStore();
const PageStore = createPageStore();
const MemberStore = createMemberStore();
const CodeStore = createCodeStore();

export const Stores = { RouteStore, PageStore, MemberStore, CodeStore };
