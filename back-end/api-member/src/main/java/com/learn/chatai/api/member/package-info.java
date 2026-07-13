/**
 * Placeholder for the user-facing API module. No controllers yet — initial PRD scope is
 * admin-only (see .claude/docs/prd/09-future-auth-sso.md and PRD README "초기 범위에서
 * 로그인/회원가입은 제외한다"). This module is wired to core/domain/infra so the first
 * member-facing controller can land without further module-boundary setup; it does not yet
 * apply the Spring Boot plugin since there is no main class to package into a bootJar.
 */
package com.learn.chatai.api.member;
