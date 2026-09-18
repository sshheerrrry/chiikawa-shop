import { useEffect, useState } from 'react'
import {
  Link,
  Route,
  Routes,
  useNavigate
} from 'react-router-dom'

import api from './api'

import ProductPage from './pages/ProductPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import CartPage from './pages/CartPage'
import OrderHistoryPage from './pages/OrderHistoryPage'
import AddressPage from './pages/AddressPage'
import AdminProductPage from './pages/AdminProductPage'
import AdminUserPage from './pages/AdminUserPage'
import FavoritePage from './pages/FavoritePage'

export default function App() {

  const [user, setUser] = useState(null)

  const navigate = useNavigate()


  // =========================
  // 取得目前登入會員
  // =========================
  const loadUser = async () => {

    const accessToken =
      localStorage.getItem(
        'chiikawa_access_token'
      )

    if (!accessToken) {

      setUser(null)

      return
    }


    try {

      const res =
        await api.get('/auth/me')

      setUser(res.data)

    } catch {

      setUser(null)
    }
  }


  // =========================
  // 網站啟動時確認登入狀態
  // =========================
  useEffect(() => {

    // =========================
    // 網站啟動時
    // 確認登入狀態
    // =========================

    loadUser()


    // =========================
    // JWT 完全失效
    // =========================

    const handleAuthExpired = () => {

      // React 登入狀態清除
      setUser(null)

      // 回首頁
      navigate('/')
    }


    // 監聽 api.js 發出的事件
    window.addEventListener(
      'chiikawa-auth-expired',
      handleAuthExpired
    )


    // Component 卸載時
    // 移除 Listener
    return () => {

      window.removeEventListener(
        'chiikawa-auth-expired',
        handleAuthExpired
      )
    }

  }, [])


  // =========================
  // 登出
  // =========================
  const logout = async () => {

    try {

      await api.post('/auth/logout')

    } finally {

      // Access Token
      localStorage.removeItem(
        'chiikawa_access_token'
      )

      // Refresh Token
      localStorage.removeItem(
        'chiikawa_refresh_token'
      )

      // 清除舊版 Token
      localStorage.removeItem(
        'chiikawa_token'
      )

      setUser(null)

      navigate('/')
    }
  }


  return (
    <>
      <header className="header">

        <Link
          className="brand"
          to="/"
        >
          ちいかわ Chiikawa Shop
        </Link>

        <nav>

          <Link to="/">
            商品
          </Link>

          {user && (
            <Link to="/favorites">
              我的收藏
            </Link>
          )}

          {user && (
            <Link to="/cart">
              購物車
            </Link>
          )}

          {user && (
            <Link to="/addresses">
              收件地址
            </Link>
          )}

          {user && (
            <Link to="/orders">
              歷史訂單
            </Link>
          )}


          {user?.role === 'ADMIN' && (
            <>
              <Link to="/admin/products">
                商品管理
              </Link>

              <Link to="/admin/users">
                會員管理
              </Link>
            </>
          )}


          {!user ? (
            <>
              <Link to="/login">
                登入
              </Link>

              <Link to="/register">
                註冊
              </Link>
            </>
          ) : (
            <>
              <span className="user-name">
                Hi, {user.name}
              </span>

              <button
                className="link-button"
                onClick={logout}
              >
                登出
              </button>
            </>
          )}

        </nav>
      </header>


      <main className="container">

        <Routes>

          <Route
            path="/"
            element={
              <ProductPage user={user} />
            }
          />

          <Route
            path="/login"
            element={
              <LoginPage
                onLogin={loadUser}
              />
            }
          />

          <Route
            path="/register"
            element={
              <RegisterPage
                onLogin={loadUser}
              />
            }
          />

          <Route
            path="/cart"
            element={
              <CartPage user={user} />
            }
          />

          <Route
            path="/addresses"
            element={
              <AddressPage user={user} />
            }
          />

          <Route
            path="/favorites"
            element={
              <FavoritePage user={user} />
            }
          />

          <Route
            path="/orders"
            element={
              <OrderHistoryPage
                user={user}
              />
            }
          />

          <Route
            path="/admin/products"
            element={
              <AdminProductPage
                user={user}
              />
            }
          />

          <Route
            path="/admin/users"
            element={
              <AdminUserPage
                user={user}
              />
            }
          />

        </Routes>

      </main>
    </>
  )
}