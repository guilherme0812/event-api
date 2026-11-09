package api.controller;

import api.domain.coupon.Coupon;
import api.domain.coupon.CouponRequestDTO;
import api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

        @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO data) {
        Coupon coupons = this.couponService.addCouponToEvent(eventId, data);
        return ResponseEntity.ok(coupons);
    }
}
