import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function AdminUserPage({ user }) {

  const [users, setUsers] = useState([])
  const [openUserId, setOpenUserId] = useState(null)

  const navigate = useNavigate()


  // =========================
  // 讀取會員資料
  // =========================
  const loadUsers = async () => {

    try {

      const res =
        await api.get('/admin/users')

      setUsers(res.data)

    } catch (e) {

      alert(
        e.response?.data?.message ||
        '會員資料讀取失敗'
      )
    }
  }


  useEffect(() => {

    if (!user) return

    if (user.role !== 'ADMIN') {

      navigate('/')

      return
    }

    loadUsers()

  }, [user])


  if (!user) {

    return (
      <p>
        請先登入管理員。
      </p>
    )
  }


  if (user.role !== 'ADMIN') {

    return null
  }


  return (

    <section>

      <h2>會員管理</h2>


      <div className="admin-table-wrap">

        <table>

          <thead>

            <tr>

              <th>姓名</th>

              <th>帳號</th>

              <th>Email</th>

              <th>歷史訂單</th>

            </tr>

          </thead>


          <tbody>

            {users.map(member => (

              <tr key={member.id}>

                <td>
                  {member.name}
                </td>

                <td>
                  {member.username}
                </td>

                <td>
                  {member.email || '-'}
                </td>

                <td>

                  <button
                    onClick={() =>
                      setOpenUserId(
                        openUserId === member.id
                          ? null
                          : member.id
                      )
                    }
                  >

                    {openUserId === member.id
                      ? '收起'
                      : `查看 (${member.orders.length})`
                    }

                  </button>


                  {openUserId === member.id && (

                    <div className="member-orders">

                      {member.orders.length === 0 ? (

                        <p>
                          尚無歷史訂單
                        </p>

                      ) : (

                        // =========================
                        // 每位會員的訂單：
                        // 1. 依日期「舊 → 新」排序
                        // 2. 每位會員重新從 #1 編號
                        // =========================
                        [...member.orders]
                          .sort(
                            (a, b) =>
                              new Date(a.createdAt) -
                              new Date(b.createdAt)
                          )
                          .map((order, index) => (

                            <div
                              className="order-card"
                              key={order.id}
                            >

                              <div className="order-title">

                                <strong>
                                  訂單 #{index + 1}
                                </strong>

                                <span>
                                  {new Date(
                                    order.createdAt
                                  ).toLocaleString()}
                                </span>

                              </div>


                              <p>
                                姓名：
                                {order.name}
                              </p>

                              <p>
                                帳號：
                                {order.username}
                              </p>

                              <p>
                                Email：
                                {order.email || '-'}
                              </p>


                              {order.items.map(item => (

                                <div
                                  className="order-item"
                                  key={item.id}
                                >

                                  <span>
                                    {item.productName}
                                    × {item.quantity}
                                  </span>

                                  <span>

                                    NT$ {
                                      (
                                        Number(item.price) *
                                        item.quantity
                                      ).toLocaleString()
                                    }

                                  </span>

                                </div>

                              ))}


                              <div className="order-total">

                                總金額：NT$ {
                                  Number(
                                    order.totalAmount
                                  ).toLocaleString()
                                }

                              </div>

                            </div>

                          ))

                      )}

                    </div>

                  )}

                </td>

              </tr>

            ))}

          </tbody>

        </table>

      </div>

    </section>
  )
}