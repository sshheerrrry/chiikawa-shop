import {
  useEffect,
  useMemo,
  useState
} from 'react'

import { useNavigate } from 'react-router-dom'
import api from '../api'


export default function CartPage({ user }) {

  const [items, setItems] =
    useState([])

  // 收件地址
  const [addresses, setAddresses] =
    useState([])

  const [selectedAddressId, setSelectedAddressId] =
    useState('')

  // 付款方式
  const [paymentMethod, setPaymentMethod] =
    useState('')

  const navigate = useNavigate()


  // Spring Boot 後端網址
  const backendUrl =
    'http://localhost:8080'


  // =========================
  // 處理商品圖片網址
  // =========================
  const getImageUrl = (imageUrl) => {

    if (!imageUrl) {
      return '/images/placeholder.svg'
    }

    if (
      imageUrl.startsWith('http://') ||
      imageUrl.startsWith('https://')
    ) {
      return imageUrl
    }

    return `${backendUrl}${imageUrl}`
  }


  // =========================
  // 載入購物車
  // =========================
  const load = async () => {

    if (!user) return

    try {

      const res =
        await api.get('/cart')

      setItems(res.data)

    } catch {

      navigate('/login')
    }
  }


  // =========================
  // 載入收件地址
  // =========================
  const loadAddresses = async () => {

    if (!user) return

    try {

      const res =
        await api.get('/addresses')

      const data = res.data

      setAddresses(data)


      // =========================
      // 自動選擇預設地址
      // =========================
      const defaultAddress =
        data.find(
          address =>
            address.isDefault
        )


      if (defaultAddress) {

        setSelectedAddressId(
          String(defaultAddress.id)
        )

      } else if (data.length > 0) {

        // 如果沒有預設地址
        // 就選第一筆
        setSelectedAddressId(
          String(data[0].id)
        )
      }

    } catch (e) {

      console.error(
        '載入收件地址失敗',
        e
      )
    }
  }


  // =========================
  // 初始載入
  // =========================
  useEffect(() => {

    if (user) {
      load()
      loadAddresses()
    }

  }, [user])


  // =========================
  // 計算總金額
  // =========================
  const total = useMemo(
    () =>
      items.reduce(
        (sum, item) =>
          sum +
          Number(item.price) *
          item.quantity,
        0
      ),
    [items]
  )


  // =========================
  // 修改商品數量
  // =========================
  const updateQty =
    async (id, quantity) => {

      if (quantity <= 0) return

      try {

        await api.put(
          `/cart/items/${id}`,
          { quantity }
        )

        await load()

      } catch (e) {

        console.error(
          '修改購物車數量失敗',
          e
        )

        alert(
          e.response?.data?.message ||
          '修改商品數量失敗'
        )
      }
    }


  // =========================
  // 刪除購物車商品
  // =========================
  const remove = async id => {

    try {

      await api.delete(
        `/cart/items/${id}`
      )

      await load()

    } catch (e) {

      console.error(
        '刪除商品失敗',
        e
      )

      alert(
        e.response?.data?.message ||
        '刪除商品失敗'
      )
    }
  }


  // =========================
  // 結帳
  // =========================
  const checkout = async () => {

    // 沒有地址
    if (!selectedAddressId) {

      alert('請選擇收件地址')

      return
    }


    // 沒有付款方式
    if (!paymentMethod) {

      alert('請選擇付款方式')

      return
    }


    try {

      await api.post(
        '/orders/checkout',
        {
          addressId:
            Number(selectedAddressId),

          paymentMethod:
            paymentMethod
        }
      )


      alert('結帳成功！')


      navigate('/orders')

    } catch (e) {

      console.error(
        '結帳失敗',
        e
      )

      alert(
        e.response?.data?.message ||
        '結帳失敗'
      )
    }
  }


  if (!user) {

    return (
      <p>
        請先登入。
      </p>
    )
  }


  return (
    <section>

      <h2>
        我的購物車
      </h2>


      {items.length === 0 ? (

        <div className="empty">
          購物車目前沒有商品。
        </div>

      ) : (

        <>

          {/* =====================
              購物車商品
          ===================== */}

          <div className="cart-list">

            {items.map(item => (

              <div
                className="cart-row"
                key={item.id}
              >

                <img
                  src={
                    getImageUrl(
                      item.imageUrl
                    )
                  }
                  alt={
                    `${item.series}－${item.characterName}`
                  }
                />


                <div className="grow">

                  <h3>
                    {item.series}
                    －
                    {item.characterName}
                  </h3>

                  <p>
                    NT${' '}
                    {Number(
                      item.price
                    ).toLocaleString()}
                  </p>

                </div>


                <div className="qty">

                  <button
                    onClick={() =>
                      updateQty(
                        item.id,
                        item.quantity - 1
                      )
                    }
                  >
                    -
                  </button>


                  <span>
                    {item.quantity}
                  </span>


                  <button
                    onClick={() =>
                      updateQty(
                        item.id,
                        item.quantity + 1
                      )
                    }
                  >
                    +
                  </button>

                </div>


                <button
                  className="danger"
                  onClick={() =>
                    remove(item.id)
                  }
                >
                  刪除
                </button>

              </div>

            ))}

          </div>


          {/* =====================
              收件地址
          ===================== */}

          <div className="checkout-section">

            <h3>
              選擇收件地址
            </h3>


            {addresses.length === 0 ? (

              <div>

                <p>
                  尚未建立收件地址。
                </p>

                <button
                  type="button"
                  onClick={() =>
                    navigate('/addresses')
                  }
                >
                  ＋ 新增收件地址
                </button>

              </div>

            ) : (

              <>

                {addresses.map(
                  address => (

                    <label
                      key={address.id}
                      className="checkout-option"
                    >

                      <input
                        type="radio"
                        name="address"
                        value={address.id}
                        checked={
                          selectedAddressId ===
                          String(address.id)
                        }
                        onChange={e =>
                          setSelectedAddressId(
                            e.target.value
                          )
                        }
                      />


                      <span>

                        <strong>
                          {address.recipientName}
                        </strong>

                        {address.isDefault &&
                          '（預設地址）'}

                        <br />

                        {address.phone}

                        <br />

                        {address.postalCode &&
                          `${address.postalCode} `}

                        {address.city}
                        {address.district}
                        {address.address}

                      </span>

                    </label>

                  )
                )}


                <button
                  type="button"
                  onClick={() =>
                    navigate('/addresses')
                  }
                >
                  管理收件地址
                </button>

              </>

            )}

          </div>


          {/* =====================
              付款方式
          ===================== */}

          <div className="checkout-section">

            <h3>
              選擇付款方式
            </h3>


            <label className="checkout-option">

              <input
                type="radio"
                name="paymentMethod"
                value="CREDIT_CARD"
                checked={
                  paymentMethod ===
                  'CREDIT_CARD'
                }
                onChange={e =>
                  setPaymentMethod(
                    e.target.value
                  )
                }
              />

              <span>
                信用卡
              </span>

            </label>


            <label className="checkout-option">

              <input
                type="radio"
                name="paymentMethod"
                value="CASH_ON_DELIVERY"
                checked={
                  paymentMethod ===
                  'CASH_ON_DELIVERY'
                }
                onChange={e =>
                  setPaymentMethod(
                    e.target.value
                  )
                }
              />

              <span>
                貨到付款
              </span>

            </label>


            <label className="checkout-option">

              <input
                type="radio"
                name="paymentMethod"
                value="ATM_TRANSFER"
                checked={
                  paymentMethod ===
                  'ATM_TRANSFER'
                }
                onChange={e =>
                  setPaymentMethod(
                    e.target.value
                  )
                }
              />

              <span>
                ATM 轉帳
              </span>

            </label>

          </div>


          {/* =====================
              結帳
          ===================== */}

          <div className="checkout-box">

            <strong>
              總金額：NT${' '}
              {total.toLocaleString()}
            </strong>


            <button
              onClick={checkout}
              disabled={
                addresses.length === 0
              }
            >
              確認結帳
            </button>

          </div>

        </>

      )}

    </section>
  )
}