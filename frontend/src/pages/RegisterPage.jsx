import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function RegisterPage({ onLogin }) {
  const [form, setForm] = useState({
    username: '',
    password: '',
    name: '',
    email: ''
  })

  const navigate = useNavigate()

  const change = (key, value) =>
    setForm({ ...form, [key]: value })

  const submit = async (e) => {
    e.preventDefault()

    try {
      const res = await api.post('/auth/register', form)

      // 註冊完成後，後端也會回傳 JWT。
      localStorage.setItem('chiikawa_token', res.data.token)

      await onLogin()
      navigate('/')
    } catch (e) {
      alert(e.response?.data?.message || '註冊失敗')
    }
  }

  return (
    <form className="form-card" onSubmit={submit}>
      <h2>會員註冊</h2>

      <input
        placeholder="帳號"
        value={form.username}
        onChange={e => change('username', e.target.value)}
      />

      <input
        type="password"
        placeholder="密碼"
        value={form.password}
        onChange={e => change('password', e.target.value)}
      />

      <input
        placeholder="姓名"
        value={form.name}
        onChange={e => change('name', e.target.value)}
      />

      <input
        type="email"
        placeholder="Email"
        value={form.email}
        onChange={e => change('email', e.target.value)}
      />

      <button>註冊</button>
    </form>
  )
}
