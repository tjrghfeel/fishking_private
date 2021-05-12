package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Ad;
import com.tobe.fishking.v2.entity.common.Banner;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.AdDto;
import com.tobe.fishking.v2.model.admin.ModifyMainBannerDto;
import com.tobe.fishking.v2.repository.common.AdRepository;
import com.tobe.fishking.v2.repository.common.BannerRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BannerManageService {
    private final MemberService memberService;
    private final AdRepository adRepository;
    private final ShipRepository shipRepository;
    private final BannerRepository bannerRepository;
    private final FileRepository fileRepository;

    @Transactional
    public Map<String, Object> getAllBanner(String token) throws ServiceLogicException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다");}

        Map<String, Object> result = new HashMap<>();
        List<AdDto> adList = adRepository.findAllOrderByAdType();

        Map<String, Object> homeBanner = new HashMap<>();
        List<AdDto> mainLive = new ArrayList<>(10);
        List<AdDto> mainShip = new ArrayList<>(10);
        List<AdDto> mainAd = new ArrayList<>(10);

        Map<String, Object> shipRockBanner = new HashMap<>();
        List<AdDto> shipPremium = new ArrayList<>(1);
        List<AdDto> shipAd = new ArrayList<>(1);
        List<AdDto> rockPremium = new ArrayList<>(1);
        List<AdDto> rockAd = new ArrayList<>(1);

        for(AdDto item: adList){
            if(item.getAdType().equals(AdType.MAIN_LIVE.getValue())){mainLive.add(item);}
            else if(item.getAdType().equals(AdType.MAIN_SHIP.getValue())){mainShip.add(item);}
            else if(item.getAdType().equals(AdType.MAIN_AD.getValue())){mainAd.add(item);}
            else if(item.getAdType().equals(AdType.SHIP_PREMIUM_AD.getValue())){shipPremium.add(item);}
            else if(item.getAdType().equals(AdType.SHIP_AD.getValue())){shipAd.add(item);}
            else if(item.getAdType().equals(AdType.ROCK_PREMIUM_AD.getValue())){rockPremium.add(item);}
            else if(item.getAdType().equals(AdType.ROCK_AD.getValue())){rockAd.add(item);}
        }

        homeBanner.put("mainLive", mainLive);
        homeBanner.put("mainShip", mainShip);
        homeBanner.put("mainAd", mainAd);
        shipRockBanner.put("shipPremium", shipPremium);
        shipRockBanner.put("shipAd", shipAd);
        shipRockBanner.put("rockPremium", rockPremium);
        shipRockBanner.put("rockAd", rockAd);

        result.put("homeBanner", homeBanner);
        result.put("shipRockBanner", shipRockBanner);

        return result;
    }

    @Transactional
    public Boolean setBanner(String bannerCode, Long shipId, String addRemove, String token) throws ServiceLogicException, ResourceNotFoundException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다");}

        AdType adType = AdType.valueOf(bannerCode);
        Ship ship = shipRepository.findById(shipId)
            .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+shipId));
        Ad ad = adRepository.findByShipAndAdType(ship, adType);

        if(addRemove.equals("add")){
            if(ad != null){throw new ServiceLogicException("해당 선박은 이미 배너 목록에 존재합니다");}
            else{
                Ad newAd = Ad.builder()
                        .adType(adType)
                        .ship(ship)
                        .createdBy(manager)
                        .build();
                adRepository.save(newAd);
                return true;
            }
        }
        else if(addRemove.equals("remove")){
            if(ad == null){throw new ServiceLogicException("해당 선박이 배너 목록에 존재하지 않습니다");}
            else{
                adRepository.delete(ad);
                return true;
            }
        }
        else{
            return false;
        }
    }

    //메인 배너 설정
    @Transactional
    public Boolean setMainBanner(ModifyMainBannerDto dto, String token) throws ServiceLogicException, ResourceNotFoundException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다");}

        List<Banner> preBannerList = bannerRepository.findAllOrderByOrder();
        bannerRepository.deleteAll(preBannerList);

        Map<String, Object>[] bannerList = dto.getBannerList();
        for(int i=0; i<bannerList.length; i++){
            Banner banner = Banner.builder()
                    .imagePath((String)bannerList[i].get("ImageUrl"))
                    .linkURL((String)bannerList[i].get("linkUrl"))
                    .orders(i+1)
                    .createdBy(manager)
                    .build();
            bannerRepository.save(banner);

            Long fileId = new Long((Integer)bannerList[i].get("fileId"));
            FileEntity imageFile = fileRepository.findById(fileId)
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id ::"+fileId));
            imageFile.saveTemporaryFile(banner.getId());
        }

        return true;
    }
}
