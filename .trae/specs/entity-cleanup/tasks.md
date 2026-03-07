# 后端实体类清理 - 实现计划

## [ ] 任务 1: 分析后端实体类
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 分析后端项目中的实体类
  - 列出所有实体类及其用途
  - 生成后端实体类清单
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**: 
  - `programmatic` TR-1.1: 成功提取所有后端实体类
  - `programmatic` TR-1.2: 生成完整的后端实体类清单
- **Notes**: 重点关注 `src/main/java/com/test/takeout/entity` 目录下的所有实体类

## [ ] 任务 2: 识别不需要的实体类
- **Priority**: P0
- **Depends On**: 任务 1
- **Description**: 
  - 分析实体类的使用情况
  - 识别前端未使用的实体类
  - 识别后端未使用的实体类
  - 生成不需要的实体类清单
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**: 
  - `human-judgment` TR-2.1: 正确识别不需要的实体类
  - `human-judgment` TR-2.2: 确保需要的实体类不被误删
- **Notes**: 可以通过搜索实体类在代码中的引用情况来判断是否需要

## [ ] 任务 3: 移除不需要的实体类
- **Priority**: P0
- **Depends On**: 任务 2
- **Description**: 
  - 根据识别结果，移除不需要的实体类
  - 确保不删除需要的实体类
  - 清理相关的服务和控制器代码
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**: 
  - `programmatic` TR-3.1: 成功移除不需要的实体类
  - `programmatic` TR-3.2: 验证需要的实体类仍然存在
- **Notes**: 移除实体类前备份相关文件，以防万一

## [ ] 任务 4: 生成 SQL 语句
- **Priority**: P0
- **Depends On**: 任务 2
- **Description**: 
  - 根据不需要的实体类清单，生成 SQL 语句
  - 生成移除不需要的数据库表和字段的 SQL 语句
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**: 
  - `programmatic` TR-4.1: 成功生成 SQL 语句
  - `programmatic` TR-4.2: 验证 SQL 语句正确性
- **Notes**: 确保 SQL 语句格式正确，可直接执行

## [ ] 任务 5: 验证系统功能
- **Priority**: P1
- **Depends On**: 任务 3, 任务 4
- **Description**: 
  - 运行后端项目
  - 验证所有功能正常运行
  - 特别测试与实体类相关的功能
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**: 
  - `human-judgment` TR-5.1: 验证系统所有功能正常运行
  - `human-judgment` TR-5.2: 验证数据库操作正常
- **Notes**: 重点测试核心功能，确保清理后系统稳定运行