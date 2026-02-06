package com.test.takeout.controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/front/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @GetMapping("/list")
    public ResponseVO<List<AddressVO>> getAddressList(@RequestHeader("Authorization") String token) {
        return ResponseVO.success(addressService.getAddressList(token));
    }

    @PostMapping("/add")
    public ResponseVO<Void> addAddress(@RequestHeader("Authorization") String token,
                                       @RequestBody AddressAddDTO addDTO) {
        addressService.addAddress(token, addDTO);
        return ResponseVO.success(null);
    }

    @PutMapping("/update")
    public ResponseVO<Void> updateAddress(@RequestHeader("Authorization") String token,
                                          @RequestBody AddressVO addressVO) {
        addressService.updateAddress(token, addressVO);
        return ResponseVO.success(null);
    }

    @DeleteMapping("/delete")
    public ResponseVO<Void> deleteAddress(@RequestHeader("Authorization") String token,
                                          @RequestParam Long addressId) {
        addressService.deleteAddress(token, addressId);
        return ResponseVO.success(null);
    }

    @PutMapping("/default")
    public ResponseVO<Void> setDefaultAddress(@RequestHeader("Authorization") String token,
                                              @RequestParam Long addressId) {
        addressService.setDefaultAddress(token, addressId);
        return ResponseVO.success(null);
    }
}
