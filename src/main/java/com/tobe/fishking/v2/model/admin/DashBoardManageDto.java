package com.tobe.fishking.v2.model.admin;

import com.tobe.fishking.v2.entity.board.Post;
import io.swagger.models.auth.In;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashBoardManageDto {
    private MemberData memberData;
    @Setter
    public class MemberData{
        public long totalCount;
        public long memberCount;
        public long shipownerCount;
        public long policeCount;
        public MemberData(long total, long member, long shipowner, long police){
            this.totalCount = total; this.memberCount=member; this.shipownerCount=shipowner; this.policeCount=police;
        }
    }

    private GoodsData goodsData;
    @Setter
    public class GoodsData{
        public long shipCount;
        public long seaRockCount;
        public GoodsData(long ship, long searock){
            this.shipCount = ship; this.seaRockCount = searock;
        }
    }

    private OrderData orderData;
    @Setter
    public class OrderData{
        public long book;
        public long bookRunning;
        public long waitBook;
        public long bookFix;
        public long bookCancel;
        public long fishingComplete;
        public long bookConfirm;
        public OrderData(long book, long bookRunning, long waitBook, long bookFix, long bookCancel, long fishingComplete, long bookConfirm){
            this.book=book;
            this.bookRunning = bookRunning;
            this.waitBook = waitBook;
            this.bookFix = bookFix;
            this.bookCancel =bookCancel;
            this.fishingComplete = fishingComplete;
            this.bookConfirm = bookConfirm;
        }
    }

    private RideData rideData;
    @AllArgsConstructor
    @Setter
    public class RideData{
        public Integer waitRideCount;
        public Integer confirmRide;
        public Integer failConfirm;
        public Integer cancelRide;
    }

    private List<NoticeData> noticeData;
    @AllArgsConstructor
    @Setter
    public class NoticeData{
        public String targetRole;
        public String channelType;
        public String title;
        public String createdDate;

        public NoticeData(Post post){
            this.targetRole = (post.getTargetRole())? "member":"shipowner";
            this.channelType = post.getChannelType().getValue();
            this.title = post.getTitle();
            this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

    }

    private List<OneToOneData> oneToOneData;
    @AllArgsConstructor
    @Setter
    public class OneToOneData{
        public String targetRole;
        public Boolean isReplied;
        public String content;
        public String createdDate;

        public OneToOneData(Post post){
            this.targetRole = (post.getTargetRole())? "member":"shipowner";
            this.isReplied = post.getIsReplied();
            this.content = post.getContents();
            this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }


}
