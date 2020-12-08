import { makeAutoObservable } from "mobx";

export class MemberStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** MemberId */
  memberId: number | null = null;
}

export const createStore = () => {
  return new MemberStore();
};

export type TMemberStore = ReturnType<typeof createStore>;
