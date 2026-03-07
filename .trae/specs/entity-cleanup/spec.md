# 后端实体类清理 - 产品需求文档

## Overview
- **Summary**: 检查后端项目中的实体类，识别并移除不需要的实体类，生成 SQL 语句移除不需要的数据库表和字段。
- **Purpose**: 优化项目结构，减少冗余代码，提高系统维护性，清理数据库中不需要的表和字段。
- **Target Users**: 开发人员、系统维护人员。

## Goals
- 全面分析后端项目中的实体类
- 识别不需要的实体类
- 移除不需要的实体类
- 生成 SQL 语句移除不需要的数据库表和字段
- 验证清理后系统功能正常

## Non-Goals (Out of Scope)
- 不修改现有实体类的结构
- 不修改现有数据库表的结构（除非是删除不需要的表和字段）
- 不处理实体类之间的关联关系

## Background & Context
- 项目包含 Vue 2.x 前端和 Java 后端
- 后端使用 Spring Boot + MyBatis Plus 框架
- 可能存在后端实现了但前端未使用的实体类
- 可能存在数据库中未使用的表和字段

## Functional Requirements
- **FR-1**: 分析后端项目中的实体类
- **FR-2**: 识别不需要的实体类
- **FR-3**: 移除不需要的实体类
- **FR-4**: 生成 SQL 语句移除不需要的数据库表和字段
- **FR-5**: 验证清理后系统功能正常

## Non-Functional Requirements
- **NFR-1**: 确保清理后系统功能不受影响
- **NFR-2**: 确保清理后代码结构清晰
- **NFR-3**: 确保数据库结构优化

## Constraints
- **Technical**: Spring Boot + MyBatis Plus 框架
- **Business**: 确保系统功能正常运行
- **Dependencies**: 后端实体类，数据库表结构

## Assumptions
- 后端实体类位于 `src/main/java/com/test/takeout/entity` 目录下
- 实体类与数据库表一一对应
- 清理不需要的实体类不会影响系统功能

## Acceptance Criteria

### AC-1: 后端实体类分析完成
- **Given**: 后端项目代码
- **When**: 分析后端实体类
- **Then**: 生成后端实体类清单
- **Verification**: `programmatic`

### AC-2: 不需要的实体类识别完成
- **Given**: 后端实体类清单
- **When**: 分析实体类使用情况
- **Then**: 识别出不需要的实体类
- **Verification**: `human-judgment`

### AC-3: 不需要的实体类移除完成
- **Given**: 不需要的实体类清单
- **When**: 移除不需要的实体类
- **Then**: 后端代码中不再包含不需要的实体类
- **Verification**: `programmatic`

### AC-4: SQL 语句生成完成
- **Given**: 不需要的实体类清单
- **When**: 生成 SQL 语句
- **Then**: 生成移除不需要的数据库表和字段的 SQL 语句
- **Verification**: `programmatic`

### AC-5: 系统功能验证
- **Given**: 清理后的代码
- **When**: 运行系统
- **Then**: 所有功能正常运行
- **Verification**: `human-judgment`

## Open Questions
- [ ] 如何判断实体类是否需要？
- [ ] 移除实体类后是否需要更新相关的服务和控制器？