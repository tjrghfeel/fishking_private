/** [API:Props] ~v1/api/myMenuPage */
export interface MyMenuPageDataProps {
  nickName: string;
  profileImage?: string;
  bookingCount: number;
  couponCount: number | 0;
  alarmCount: number | 0;
}
