package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.entity.fishing.Sailor;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import com.tobe.fishking.v2.repository.fishking.RideShipRepository;
import com.tobe.fishking.v2.service.NaksihaeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NaksihaeTest {

    @Autowired
    private NaksihaeService naksihaeService;

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private RideShipRepository rideShipRepository;

    private String token = "373cebd541b84ab8996ce4b96ae3e498";

    @Test
    public void getToken() {
        Map<String, Object> result = new HashMap<>();
        try {
            result = naksihaeService.getToken();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(result.get("token"));
        System.out.println(result.get("expireDate"));

        assertThat(result, is(result != null));
    }

    @Test
    public void getHarborData() {
        String code = null;
        try {
            code = naksihaeService.getHarborCode("", "", token);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        assertThat(code, is(code != null));
    }

    @Test
    @Transactional
    public void registration() {
        Goods goods = goodsRepository.getOne(15L);
        List<RideShip> riders = new ArrayList<>();
        List<Sailor> sailors = new ArrayList<>();
        riders.add(rideShipRepository.getOne(11158L));
        String result = "";
        try {
            result = naksihaeService.reportRegistration(goods, riders, sailors, token);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        assertThat(result, is(""));
    }
}
