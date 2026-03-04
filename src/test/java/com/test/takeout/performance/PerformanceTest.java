package com.test.takeout.performance;

import com.test.takeout.controller.StoreController;
import com.test.takeout.entity.Store;
import com.test.takeout.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;

public class PerformanceTest {

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreController storeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStoreListPerformance() {
        // 模拟数据
        List<Store> stores = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Store store = new Store();
            store.setId((long) i);
            store.setName("测试店铺" + i);
            store.setStatus(1);
            stores.add(store);
        }

        // 模拟service方法
        when(storeService.list()).thenReturn(stores);

        // 测试响应时间
        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            storeController.list(1, 12, "", null, null);
        }
        long endTime = System.nanoTime();

        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("Store list performance: " + duration + " ms for 100 requests");
        System.out.println("Average response time: " + (duration / 100.0) + " ms per request");

        // 性能要求：平均响应时间应小于50ms
        assert (duration / 100.0) < 50 : "Performance test failed: average response time too high";
    }

    @Test
    void testStoreGetByIdPerformance() {
        // 模拟数据
        Store store = new Store();
        store.setId(1L);
        store.setName("测试店铺");
        store.setStatus(1);

        // 模拟service方法
        when(storeService.getById(1L)).thenReturn(store);

        // 测试响应时间
        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            storeController.detail(1L);
        }
        long endTime = System.nanoTime();

        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("Store getById performance: " + duration + " ms for 100 requests");
        System.out.println("Average response time: " + (duration / 100.0) + " ms per request");

        // 性能要求：平均响应时间应小于20ms
        assert (duration / 100.0) < 20 : "Performance test failed: average response time too high";
    }
}
