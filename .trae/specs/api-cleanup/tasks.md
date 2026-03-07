# API 接口清理 - 实现计划

## [ ] 任务 1: 分析前端 API 接口调用
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 分析前端项目中所有 API 模块文件
  - 提取前端使用的 API 接口路径和方法
  - 生成前端所需的 API 接口清单
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**: 
  - `programmatic` TR-1.1: 成功提取前端所有 API 接口调用
  - `programmatic` TR-1.2: 生成完整的前端 API 接口清单
- **Notes**: 重点关注 `vue/src/api` 目录下的所有文件

## [ ] 任务 2: 分析后端 API 接口实现
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 分析后端项目中所有控制器文件
  - 提取后端实现的 API 接口路径和方法
  - 生成后端 API 接口清单
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**: 
  - `programmatic` TR-2.1: 成功提取后端所有 API 接口实现
  - `programmatic` TR-2.2: 生成完整的后端 API 接口清单
- **Notes**: 重点关注 `src/main/java/com/test/takeout/controller` 目录下的所有控制器

## [ ] 任务 3: 识别后端多余的 API 接口
- **Priority**: P0
- **Depends On**: 任务 1, 任务 2
- **Description**: 
  - 对比前端和后端 API 接口清单
  - 识别后端实现但前端未使用的 API 接口
  - 特别注意 order 开头的 API 接口
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**: 
  - `programmatic` TR-3.1: 成功识别后端多余的 API 接口
  - `programmatic` TR-3.2: 验证 order 开头的 API 接口处理正确
- **Notes**: 确保不遗漏任何前端使用的 API 接口

## [ ] 任务 4: 清理后端多余的 API 接口
- **Priority**: P0
- **Depends On**: 任务 3
- **Description**: 
  - 根据识别结果，清理后端多余的 API 接口
  - 确保不删除前端使用的 API 接口
  - 保持 order 开头的 API 接口完整性
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**: 
  - `programmatic` TR-4.1: 成功清理后端多余的 API 接口
  - `programmatic` TR-4.2: 验证前端使用的 API 接口仍然存在
- **Notes**: 清理前备份相关文件，以防万一

## [ ] 任务 5: 验证系统功能
- **Priority**: P1
- **Depends On**: 任务 4
- **Description**: 
  - 运行前端和后端项目
  - 验证所有功能正常运行
  - 特别测试与 API 相关的功能
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**: 
  - `human-judgment` TR-5.1: 验证系统所有功能正常运行
  - `human-judgment` TR-5.2: 验证 API 调用正常
- **Notes**: 重点测试订单相关功能