import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
})


// =========================
// Request Interceptor
// 每次 API 自動加入 Access Token
// =========================

api.interceptors.request.use(
  config => {

    const accessToken =
      localStorage.getItem(
        'chiikawa_access_token'
      )

    if (accessToken) {

      config.headers.Authorization =
        `Bearer ${accessToken}`
    }

    return config
  },

  error => {
    return Promise.reject(error)
  }
)


// =========================
// 是否正在更新 Token
// =========================

let isRefreshing = false


// =========================
// 等待 Refresh 完成的 API
// =========================

let refreshSubscribers = []


// =========================
// Refresh 成功
// 通知所有等待中的 API
// =========================

const notifyRefreshSuccess = (
  newAccessToken
) => {

  refreshSubscribers.forEach(
    callback =>
      callback(newAccessToken)
  )

  refreshSubscribers = []
}


// =========================
// 加入等待 Refresh 的 API
// =========================

const subscribeTokenRefresh = (
  callback
) => {

  refreshSubscribers.push(
    callback
  )
}


// =========================
// 清除登入資料
// =========================

const clearLoginData = () => {

  localStorage.removeItem(
    'chiikawa_access_token'
  )

  localStorage.removeItem(
    'chiikawa_refresh_token'
  )

  localStorage.removeItem(
    'chiikawa_token'
  )


  // 通知 App.jsx：
  // 登入狀態已失效
  window.dispatchEvent(
    new Event(
      'chiikawa-auth-expired'
    )
  )
}


// =========================
// Response Interceptor
// =========================

api.interceptors.response.use(

  // API 正常
  response => response,


  // API 發生錯誤
  async error => {

    const originalRequest =
      error.config


    // =========================
    // 不是 401
    // 直接交給原本程式處理
    // =========================

    if (
      error.response?.status !== 401
    ) {

      return Promise.reject(error)
    }


    // =========================
    // Login 本身失敗
    // 不做 Refresh
    // =========================

    if (
      originalRequest?.url
        ?.includes('/auth/login')
    ) {

      return Promise.reject(error)
    }


    // =========================
    // Refresh 本身失敗
    // 代表 Refresh Token
    // 也失效了
    // =========================

    if (
      originalRequest?.url
        ?.includes('/auth/refresh')
    ) {

      clearLoginData()

      return Promise.reject(error)
    }


    // =========================
    // 已經 Retry 過
    // 不可以無限循環
    // =========================

    if (originalRequest._retry) {

      clearLoginData()

      return Promise.reject(error)
    }


    originalRequest._retry = true


    // =========================
    // 取得 Refresh Token
    // =========================

    const refreshToken =
      localStorage.getItem(
        'chiikawa_refresh_token'
      )


    // 沒有 Refresh Token
    if (!refreshToken) {

      clearLoginData()

      return Promise.reject(error)
    }


    // =========================
    // 已經有另一個 API
    // 正在 Refresh
    // =========================

    if (isRefreshing) {

      return new Promise(
        (resolve, reject) => {

          subscribeTokenRefresh(
            newAccessToken => {

              originalRequest
                .headers
                .Authorization =
                  `Bearer ${newAccessToken}`

              resolve(
                api(originalRequest)
              )
            }
          )

        }
      )
    }


    // =========================
    // 開始 Refresh
    // =========================

    isRefreshing = true


    try {

      // 注意：
      // 使用 axios
      // 不使用 api
      //
      // 避免 refresh 自己再次
      // 進入 interceptor

      const res =
        await axios.post(
          'http://localhost:8080/api/auth/refresh',
          {
            refreshToken:
              refreshToken
          }
        )


      // =========================
      // 儲存新的 Access Token
      // =========================

      const newAccessToken =
        res.data.accessToken


      localStorage.setItem(
        'chiikawa_access_token',
        newAccessToken
      )


      // =========================
      // 如果後端也回傳
      // 新 Refresh Token
      // 就一起更新
      // =========================

      if (res.data.refreshToken) {

        localStorage.setItem(
          'chiikawa_refresh_token',
          res.data.refreshToken
        )
      }


      // =========================
      // 通知其他等待中的 API
      // =========================

      notifyRefreshSuccess(
        newAccessToken
      )


      // =========================
      // 重新執行原本失敗的 API
      // =========================

      originalRequest
        .headers
        .Authorization =
          `Bearer ${newAccessToken}`


      return api(
        originalRequest
      )


    } catch (refreshError) {

      // Refresh Token
      // 也過期或無效

      clearLoginData()

      return Promise.reject(
        refreshError
      )


    } finally {

      isRefreshing = false
    }
  }
)


export default api