import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function LoginPage({ onLogin }) {
  const [form, setForm] = useState({
    username: '',
    password: ''
  })

  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()

    try {
      const res = await api.post('/auth/login', form)

      // 儲存短效 Access Token
      localStorage.setItem(
        'chiikawa_access_token',
        res.data.accessToken
      )

      // 儲存長效 Refresh Token
      localStorage.setItem(
        'chiikawa_refresh_token',
        res.data.refreshToken
      )

      // 重新取得登入會員
      await onLogin()

      // 管理員進後台，一般會員回首頁
      navigate(
        res.data.user.role === 'ADMIN'
          ? '/admin/products'
          : '/'
      )

    } catch (e) {
      alert(
        e.response?.data?.message ||
        '登入失敗'
      )
    }
  }

  return (
    <form
      className="form-card"
      onSubmit={submit}
    >
      <h2>會員登入</h2>

      <input
        placeholder="帳號"
        value={form.username}
        onChange={e =>
          setForm({
            ...form,
            username: e.target.value
          })
        }
      />

      <input
        type="password"
        placeholder="密碼"
        value={form.password}
        onChange={e =>
          setForm({
            ...form,
            password: e.target.value
          })
        }
      />

      <button>登入</button>

      <p className="hint">
        管理員測試帳號：admin / 000
      </p>
    </form>
  )
}