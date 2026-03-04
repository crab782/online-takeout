package com.test.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.takeout.common.R;
import com.test.takeout.entity.Store;
import com.test.takeout.entity.StoreFavorite;
import com.test.takeout.service.StoreService;
import com.test.takeout.service.StoreFavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class StoreControllerTest {

    @Mock
    private StoreService storeService;

    @Mock
    private StoreFavoriteService storeFavoriteService;

    @InjectMocks
    private StoreController storeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testList() {
        // 模拟数据
        Page<Store> pageInfo = new Page<>(1, 12);
        Store store = new Store();
        store.setId(1L);
        store.setName("测试店铺");
        store.setStatus(1);
        pageInfo.setRecords(java.util.Collections.singletonList(store));

        // 模拟service方法
        when(storeService.page(any(Page.class), any())).thenReturn(pageInfo);

        // 调用controller方法
        R<Page<Store>> response = storeController.list(1, 12, "", null, null);

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(storeService, times(1)).page(any(Page.class), any());
    }

    @Test
    void testDetail() {
        // 模拟数据
        Long id = 1L;
        Store store = new Store();
        store.setId(id);
        store.setName("测试店铺");
        store.setStatus(1);

        // 模拟service方法
        when(storeService.getById(id)).thenReturn(store);

        // 调用controller方法
        R<Store> response = storeController.detail(id);

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(storeService, times(1)).getById(id);
    }

    @Test
    void testDetail_NotFound() {
        // 模拟数据
        Long id = 999L;

        // 模拟service方法
        when(storeService.getById(id)).thenReturn(null);

        // 调用controller方法
        R<Store> response = storeController.detail(id);

        // 验证结果
        assertEquals(0, response.getCode());
        assertEquals("店铺不存在", response.getMsg());
        verify(storeService, times(1)).getById(id);
    }

    @Test
    void testRecommend() {
        // 模拟数据
        Page<Store> pageInfo = new Page<>(1, 10);
        Store store = new Store();
        store.setId(1L);
        store.setName("推荐店铺");
        store.setStatus(1);
        pageInfo.setRecords(java.util.Collections.singletonList(store));

        // 模拟service方法
        when(storeService.page(any(Page.class), any())).thenReturn(pageInfo);

        // 调用controller方法
        R<Page<Store>> response = storeController.recommend(10);

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(storeService, times(1)).page(any(Page.class), any());
    }

    @Test
    void testHot() {
        // 模拟数据
        Page<Store> pageInfo = new Page<>(1, 10);
        Store store = new Store();
        store.setId(1L);
        store.setName("热门店铺");
        store.setStatus(1);
        pageInfo.setRecords(java.util.Collections.singletonList(store));

        // 模拟service方法
        when(storeService.page(any(Page.class), any())).thenReturn(pageInfo);

        // 调用controller方法
        R<Page<Store>> response = storeController.hot(10);

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(storeService, times(1)).page(any(Page.class), any());
    }

    @Test
    void testSearch() {
        // 模拟数据
        Page<Store> pageInfo = new Page<>(1, 12);
        Store store = new Store();
        store.setId(1L);
        store.setName("搜索店铺");
        store.setStatus(1);
        pageInfo.setRecords(java.util.Collections.singletonList(store));

        // 模拟service方法
        when(storeService.page(any(Page.class), any())).thenReturn(pageInfo);

        // 调用controller方法
        R<Page<Store>> response = storeController.search("测试", 1, 12);

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(storeService, times(1)).page(any(Page.class), any());
    }

    @Test
    void testFavorite() {
        // 模拟数据
        StoreFavorite storeFavorite = new StoreFavorite();
        storeFavorite.setStoreId(1L);

        Store store = new Store();
        store.setId(1L);

        // 模拟service方法
        when(storeService.getById(1L)).thenReturn(store);
        when(storeFavoriteService.getOne(any())).thenReturn(null);
        when(storeFavoriteService.save(any())).thenReturn(true);

        // 调用controller方法
        R<String> response = storeController.favorite(storeFavorite);

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("收藏成功", response.getMsg());
        verify(storeService, times(1)).getById(1L);
        verify(storeFavoriteService, times(1)).save(any());
    }

    @Test
    void testFavorite_AlreadyExists() {
        // 模拟数据
        StoreFavorite storeFavorite = new StoreFavorite();
        storeFavorite.setStoreId(1L);

        Store store = new Store();
        store.setId(1L);

        StoreFavorite existingFavorite = new StoreFavorite();
        existingFavorite.setId(1L);

        // 模拟service方法
        when(storeService.getById(1L)).thenReturn(store);
        when(storeFavoriteService.getOne(any())).thenReturn(existingFavorite);

        // 调用controller方法
        R<String> response = storeController.favorite(storeFavorite);

        // 验证结果
        assertEquals(0, response.getCode());
        assertEquals("已经收藏过该店铺", response.getMsg());
        verify(storeService, times(1)).getById(1L);
    }

    @Test
    void testUnfavorite() {
        // 模拟数据
        StoreFavorite storeFavorite = new StoreFavorite();
        storeFavorite.setStoreId(1L);

        StoreFavorite existingFavorite = new StoreFavorite();
        existingFavorite.setId(1L);

        // 模拟service方法
        when(storeFavoriteService.getOne(any())).thenReturn(existingFavorite);
        when(storeFavoriteService.removeById(1L)).thenReturn(true);

        // 调用controller方法
        R<String> response = storeController.unfavorite(storeFavorite);

        // 验证结果
        assertEquals(1, response.getCode());
        assertEquals("取消收藏成功", response.getMsg());
        verify(storeFavoriteService, times(1)).removeById(1L);
    }

    @Test
    void testUnfavorite_NotExists() {
        // 模拟数据
        StoreFavorite storeFavorite = new StoreFavorite();
        storeFavorite.setStoreId(1L);

        // 模拟service方法
        when(storeFavoriteService.getOne(any())).thenReturn(null);

        // 调用controller方法
        R<String> response = storeController.unfavorite(storeFavorite);

        // 验证结果
        assertEquals(0, response.getCode());
        assertEquals("未收藏该店铺", response.getMsg());
    }

    @Test
    void testFavoriteList() {
        // 模拟数据
        Page<Store> pageInfo = new Page<>(1, 12);
        Store store = new Store();
        store.setId(1L);
        store.setName("收藏店铺");
        store.setStatus(1);
        pageInfo.setRecords(java.util.Collections.singletonList(store));

        // 模拟service方法
        when(storeService.page(any(Page.class), any())).thenReturn(pageInfo);

        // 调用controller方法
        R<Page<Store>> response = storeController.favoriteList(1, 12);

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
    }

    @Test
    void testGetShopInfo() {
        // 模拟数据
        Store store = new Store();
        store.setId(1L);
        store.setName("测试店铺");

        // 模拟service方法
        when(storeService.getById(1L)).thenReturn(store);

        // 调用controller方法
        R<Store> response = storeController.getShopInfo();

        // 验证结果
        assertEquals(1, response.getCode());
        assertNotNull(response.getData());
        verify(storeService, times(1)).getById(1L);
    }

    @Test
    void testGetShopInfo_NotFound() {
        // 模拟service方法
        when(storeService.getById(1L)).thenReturn(null);

        // 调用controller方法
        R<Store> response = storeController.getShopInfo();

        // 验证结果
        assertEquals(0, response.getCode());
        assertEquals("店铺信息不存在", response.getMsg());
        verify(storeService, times(1)).getById(1L);
    }
}
