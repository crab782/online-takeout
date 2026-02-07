package com.test.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.takeout.common.R;
import com.test.takeout.entity.AddressBook;
import com.test.takeout.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿控制器，处理地址簿相关的请求
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    /**
     * 获取所有地址列表
     * @return 地址列表
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list() {
        log.info("获取所有地址列表");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 获取最后更新的地址
     * @return 最新更新的地址
     */
    @GetMapping("/lastUpdate")
    public R<AddressBook> lastUpdate() {
        log.info("获取最后更新的地址");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        queryWrapper.last("LIMIT 1");

        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook == null) {
            return R.error("地址不存在");
        }

        return R.success(addressBook);
    }

    /**
     * 新增地址
     * @param addressBook 地址信息
     * @return 新增结果
     */
    @PostMapping
    public R<String> add(@RequestBody AddressBook addressBook) {
        log.info("新增地址：{}", addressBook);

        addressBookService.save(addressBook);

        return R.success("新增地址成功");
    }

    /**
     * 修改地址
     * @param addressBook 地址信息
     * @return 修改结果
     */
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        log.info("修改地址：{}", addressBook);

        addressBookService.updateById(addressBook);

        return R.success("修改地址成功");
    }

    /**
     * 删除地址
     * @param id 地址ID
     * @return 删除结果
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long id) {
        log.info("删除地址：{}", id);

        addressBookService.removeById(id);

        return R.success("删除地址成功");
    }

    /**
     * 查询单个地址详情
     * @param id 地址ID
     * @return 地址详情
     */
    @GetMapping("/{id}")
    public R<AddressBook> findOne(@PathVariable Long id) {
        log.info("查询单个地址详情：{}", id);

        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            return R.error("地址不存在");
        }

        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook 地址信息（包含id）
     * @return 设置结果
     */
    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址：{}", addressBook.getId());

        Long id = addressBook.getId();
        if (id == null) {
            return R.error("地址ID不能为空");
        }

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook defaultAddress = addressBookService.getOne(queryWrapper);

        if (defaultAddress != null) {
            defaultAddress.setIsDefault(0);
            addressBookService.updateById(defaultAddress);
        }

        AddressBook newDefaultAddress = addressBookService.getById(id);
        if (newDefaultAddress == null) {
            return R.error("地址不存在");
        }
        newDefaultAddress.setIsDefault(1);
        addressBookService.updateById(newDefaultAddress);

        return R.success("设置默认地址成功");
    }

    /**
     * 获取默认地址
     * @return 默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        log.info("获取默认地址");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook == null) {
            return R.error("默认地址不存在");
        }

        return R.success(addressBook);
    }

}
