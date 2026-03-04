# 移除分类功能实施计划

## 项目背景
当前系统中存在分类功能，包括：
- 用户端店铺详情页的分类导航栏
- 商家端分类管理页面
- 商家端菜品/套餐修改页的分类设置

根据需求，需要完全移除分类功能，确保前端不再显示分类，后端不再处理分类逻辑。

## 任务分解

### [x] 任务1：移除用户端店铺详情页的分类导航栏
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 移除StoreDetail.vue中的分类导航栏HTML结构
  - 删除分类相关的数据和方法
  - 确保商品列表直接显示所有商品，不再按分类筛选
- **Success Criteria**:
  - 店铺详情页不再显示分类导航栏
  - 所有商品直接显示在一个列表中
- **Test Requirements**:
  - `programmatic` TR-1.1: 店铺详情页加载后不显示分类导航栏
  - `human-judgement` TR-1.2: 商品列表显示完整，无分类筛选功能

### [x] 任务2：移除商家端分类管理页面
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 从路由配置中移除分类管理页面的路由
  - 从后端菜单中移除分类管理选项
- **Success Criteria**:
  - 商家端不再有分类管理页面的入口
  - 分类管理页面无法访问
- **Test Requirements**:
  - `programmatic` TR-2.1: 分类管理页面路由返回404
  - `human-judgement` TR-2.2: 商家端菜单中无分类管理选项

### [x] 任务3：移除商家端菜品/套餐修改页的分类设置
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 移除Add.vue（菜品）和Add.vue（套餐）中的分类选择字段
  - 修改保存逻辑，不再处理分类ID
- **Success Criteria**:
  - 菜品/套餐修改页不再显示分类选择
  - 保存菜品/套餐时不再需要选择分类
- **Test Requirements**:
  - `programmatic` TR-3.1: 菜品/套餐修改页不显示分类选择字段
  - `human-judgement` TR-3.2: 保存菜品/套餐时无分类相关错误

### [x] 任务4：修改前端API调用，移除分类相关请求
- **Priority**: P1
- **Depends On**: 任务1
- **Description**:
  - 修改StoreDetail.vue中的loadFoodData方法，不再调用categoryListApi
  - 直接获取所有菜品和套餐数据，不按分类分组
- **Success Criteria**:
  - 店铺详情页不再请求分类数据
  - 直接获取所有商品数据
- **Test Requirements**:
  - `programmatic` TR-4.1: 店铺详情页加载时不发送分类相关API请求
  - `human-judgement` TR-4.2: 商品数据加载正常，无分类相关错误

### [/] 任务5：修改后端API，忽略分类字段
- **Priority**: P1
- **Depends On**: 任务4
- **Description**:
  - 修改菜品和套餐的查询API，忽略category_id字段
  - 确保返回所有商品数据，不按分类筛选
  - 修改保存API，允许category_id为null或忽略该字段
- **Success Criteria**:
  - 后端API返回所有商品数据，不按分类筛选
  - 保存商品时不需要分类ID
- **Test Requirements**:
  - `programmatic` TR-5.1: 商品查询API返回所有商品，无分类筛选
  - `programmatic` TR-5.2: 商品保存API接受无分类ID的请求

### [ ] 任务6：测试验证
- **Priority**: P2
- **Depends On**: 任务1-5
- **Description**:
  - 测试用户端店铺详情页的商品显示
  - 测试商家端菜品/套餐的添加和修改
  - 测试所有相关页面的功能正常
- **Success Criteria**:
  - 所有页面功能正常，无分类相关错误
  - 商品数据正确显示
- **Test Requirements**:
  - `programmatic` TR-6.1: 所有相关页面加载无错误
  - `human-judgement` TR-6.2: 商品显示完整，操作流畅

## 技术实现要点

### 前端修改
1. **StoreDetail.vue**:
   - 移除分类导航栏HTML
   - 删除categories相关数据
   - 修改loadFoodData方法，直接获取所有商品
   - 调整商品列表显示逻辑

2. **路由配置**:
   - 移除分类管理页面路由

3. **商家端页面**:
   - 移除菜品/套餐修改页的分类选择字段
   - 修改保存逻辑

### 后端修改
1. **API接口**:
   - 修改商品查询接口，忽略category_id
   - 修改商品保存接口，允许无分类ID

2. **数据库操作**:
   - 保持数据库结构不变，仅修改业务逻辑
   - 允许category_id为null

## 风险评估
- **风险1**: 前端代码中可能有其他地方依赖分类数据
  - **缓解措施**: 全面搜索前端代码，确保所有分类相关逻辑都被移除

- **风险2**: 后端API可能有其他地方依赖分类字段
  - **缓解措施**: 检查所有后端API，确保分类字段不是必填项

- **风险3**: 商品数据显示顺序可能受到影响
  - **缓解措施**: 实现默认排序逻辑，确保商品显示顺序合理

## 预期效果
- 用户端店铺详情页不再显示分类导航，直接显示所有商品
- 商家端不再有分类管理功能
- 商家添加/修改菜品时不再需要选择分类
- 所有功能正常运行，无分类相关错误