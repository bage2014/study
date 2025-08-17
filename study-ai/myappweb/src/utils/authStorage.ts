import { STORAGE_KEYS } from './storageKeys'

/**
 * 认证存储管理工具类
 * 提供统一的认证信息存取和清空功能
 */
export class AuthStorage {
  /**
   * 设置token
   */
  static setToken(token: string): void {
    localStorage.setItem(STORAGE_KEYS.TOKEN, token)
  }

  /**
   * 获取token
   */
  static getToken(): string | null {
    return localStorage.getItem(STORAGE_KEYS.TOKEN)
  }

  /**
   * 设置refreshToken
   */
  static setRefreshToken(refreshToken: string): void {
    localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, refreshToken)
  }

  /**
   * 获取refreshToken
   */
  static getRefreshToken(): string | null {
    return localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN)
  }

  /**
   * 设置token过期时间
   */
  static setTokenExpireTime(expireTime: string | number): void {
    localStorage.setItem(STORAGE_KEYS.TOKEN_EXPIRE_TIME, String(expireTime))
  }

  /**
   * 设置refreshToken过期时间
   */
  static setRefreshTokenExpireTime(expireTime: string | number): void {
    localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN_EXPIRE_TIME, String(expireTime))
  }

  /**
   * 设置用户信息
   */
  static setUserInfo(userInfo: any): void {
    localStorage.setItem(STORAGE_KEYS.USER_INFO, JSON.stringify(userInfo))
  }

  /**
   * 获取用户信息
   */
  static getUserInfo<T = any>(): T | null {
    const userInfoStr = localStorage.getItem(STORAGE_KEYS.USER_INFO)
    if (userInfoStr) {
      try {
        return JSON.parse(userInfoStr) as T
      } catch (error) {
        console.error('解析用户信息失败:', error)
        return null
      }
    }
    return null
  }

  /**
   * 保存完整的登录响应数据
   */
  static saveLoginResponse(responseData: { userToken: any, user?: any }): void {
    if (responseData && responseData.userToken) {
      this.setToken(responseData.userToken.token)
      this.setRefreshToken(responseData.userToken.refreshToken)
      
      // 保存过期时间
      if (responseData.userToken.tokenExpireTime) {
        this.setTokenExpireTime(responseData.userToken.tokenExpireTime)
      }
      if (responseData.userToken.refreshTokenExpireTime) {
        this.setRefreshTokenExpireTime(responseData.userToken.refreshTokenExpireTime)
      }
      
      // 保存用户信息
      if (responseData.user) {
        this.setUserInfo(responseData.user)
      }
    }
  }

  /**
   * 检查是否已登录
   */
  static isLoggedIn(): boolean {
    return !!this.getToken()
  }

  /**
   * 清空所有认证相关的存储
   */
  static clearAll(): void {
    localStorage.removeItem(STORAGE_KEYS.TOKEN)
    localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN)
    localStorage.removeItem(STORAGE_KEYS.TOKEN_EXPIRE_TIME)
    localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN_EXPIRE_TIME)
    localStorage.removeItem(STORAGE_KEYS.USER_INFO)
  }
}