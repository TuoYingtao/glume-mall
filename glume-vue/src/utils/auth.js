const defaultSettings = require('@/settings.js')
import Cookies from 'js-cookie'

const TokenKey = defaultSettings.login_token


export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
   return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function setItem(key, value) {
  value = JSON.stringify(value);
  window.localStorage.setItem(key, value);
}

export function getItem(key, defaultValue) {
  let value = window.localStorage.getItem(key)
  try {
    value = JSON.parse(value)
    return value || defaultValue
  } catch (e) {
    console.log("JSON 解析失败：" + e);
  }
  return -1;
}

export function removeItem(key) {
  window.localStorage.removeItem(key)
}

export function clear() {
  window.localStorage.clear()
}
