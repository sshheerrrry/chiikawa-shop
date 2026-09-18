import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function OrderHistoryPage({ user }) {

  // =========================
  // State
  // =========================

  const [orders, setOrders] = useState([])

  // 每個商品的評價資格
  // purchased / reviewed / canReview
  const [reviewStatus, setReviewStatus] = useState({})

  // 哪個商品目前打開撰寫評價表單
  const [showReviewForm, setShowReviewForm] = useState({})

  // 每個商品目前輸入的評價
  const [reviewForm, setReviewForm] = useState({})

  // 哪個商品目前顯示自己的評價
  const [showMyReview, setShowMyReview] = useState({})

  // 儲存自己送出的評價
  const [myReviews, setMyReviews] = useState({})

  const navigate = useNavigate()

  // =========================
  // 付款方式中文名稱
  // =========================
  const getPaymentMethodName = (
    paymentMethod
  ) => {

    switch (paymentMethod) {

      case 'CREDIT_CARD':
        return '信用卡'

      case 'CASH_ON_DELIVERY':
        return '貨到付款'

      case 'ATM_TRANSFER':
        return 'ATM 轉帳'

      default:
        return '未記錄'
    }
  }
  // =========================
  // 查詢商品評價資格
  // =========================

  const loadReviewEligibility = async (
    productId
  ) => {

    try {

      const res = await api.get(
        `/products/${productId}/reviews/eligibility`
      )

      setReviewStatus(prev => ({
        ...prev,
        [productId]: res.data
      }))

    } catch (e) {

      console.error(
        `商品 ${productId} 評價資格讀取失敗`,
        e
      )
    }
  }


  // =========================
  // 打開評價表單
  // =========================

  const openReviewForm = (
    productId
  ) => {

    setShowReviewForm(prev => ({
      ...prev,
      [productId]: true
    }))

    setReviewForm(prev => ({
      ...prev,

      [productId]: {
        rating:
          prev[productId]?.rating || 0,

        comment:
          prev[productId]?.comment || ''
      }
    }))
  }


  // =========================
  // 取消撰寫評價
  // =========================

  const cancelReview = (
    productId
  ) => {

    // 關閉評價表單
    setShowReviewForm(prev => ({
      ...prev,
      [productId]: false
    }))

    // 清空尚未送出的內容
    setReviewForm(prev => ({
      ...prev,

      [productId]: {
        rating: 0,
        comment: ''
      }
    }))
  }


  // =========================
  // 選擇星等
  // =========================

  const setRating = (
    productId,
    rating
  ) => {

    setReviewForm(prev => ({
      ...prev,

      [productId]: {
        ...prev[productId],
        rating
      }
    }))
  }


  // =========================
  // 輸入評價文字
  // =========================

  const setComment = (
    productId,
    comment
  ) => {

    setReviewForm(prev => ({
      ...prev,

      [productId]: {
        ...prev[productId],
        comment
      }
    }))
  }


  // =========================
  // 送出評價
  // =========================

  const submitReview = async (
    productId
  ) => {

    const form =
      reviewForm[productId] || {}


    // 沒有選擇星星
    if (!form.rating) {

      alert('請選擇 1～5 顆星')

      return
    }


    // 評價只能送出一次
    const confirmed =
      window.confirm(
        '評價送出後無法修改，確定要送出嗎？'
      )


    if (!confirmed) {

      return
    }


    try {

      await api.post(
        `/products/${productId}/reviews`,
        {
          rating: form.rating,
          comment: form.comment || ''
        }
      )


      alert('評價成功')


      // 關閉評價表單
      setShowReviewForm(prev => ({
        ...prev,
        [productId]: false
      }))


      // 清空輸入內容
      setReviewForm(prev => ({
        ...prev,

        [productId]: {
          rating: 0,
          comment: ''
        }
      }))


      // 重新查詢評價資格
      // reviewed 會變成 true
      // canReview 會變成 false
      await loadReviewEligibility(
        productId
      )


      // 如果之前曾經讀過自己的評價
      // 先清除快取，避免看到舊資料
      setMyReviews(prev => {

        const next = {
          ...prev
        }

        delete next[productId]

        return next
      })


    } catch (e) {

      alert(
        e.response?.data?.message ||
        '評價失敗'
      )
    }
  }


  // =========================
  // 查看自己的評價
  // =========================

  const toggleMyReview = async (
    productId
  ) => {

    // 如果目前已經打開
    // 再按一次就收起
    if (showMyReview[productId]) {

      setShowMyReview(prev => ({
        ...prev,
        [productId]: false
      }))

      return
    }


    try {

      // 如果之前已經讀取過
      // 不需要再次呼叫 API
      if (!myReviews[productId]) {

        const res = await api.get(
          `/products/${productId}/reviews`
        )


        // 從這個商品所有評價中
        // 找出目前登入會員自己的評價
        const myReview =
          res.data.find(
            review =>
              review.userId === user.id
          )


        setMyReviews(prev => ({
          ...prev,
          [productId]:
            myReview || null
        }))
      }


      // 打開自己的評價
      setShowMyReview(prev => ({
        ...prev,
        [productId]: true
      }))


    } catch (e) {

      console.error(
        '讀取自己的評價失敗',
        e
      )

      alert('讀取評價失敗')
    }
  }


  // =========================
  // 讀取歷史訂單
  // =========================

  useEffect(() => {

    if (!user) return


    const loadOrders = async () => {

      try {

        // 取得歷史訂單
        const res =
          await api.get(
            '/orders/history'
          )


        // 訂單由舊到新排序
        const sortedOrders =
          [...res.data].sort(
            (a, b) =>
              new Date(a.createdAt) -
              new Date(b.createdAt)
          )


        setOrders(sortedOrders)


        // =====================
        // 取得所有不重複的 productId
        // =====================

        const productIds = [
          ...new Set(

            sortedOrders.flatMap(
              order =>

                order.items.map(
                  item =>
                    item.productId
                )

            )

          )
        ]


        // =====================
        // 查詢每個商品評價資格
        // =====================

        await Promise.all(

          productIds.map(
            productId =>

              loadReviewEligibility(
                productId
              )

          )

        )


      } catch (e) {

        console.error(
          '歷史訂單讀取失敗',
          e
        )

        navigate('/login')
      }
    }


    loadOrders()


  }, [user])


  // =========================
  // 未登入
  // =========================

  if (!user) {

    return <p>請先登入。</p>
  }


  // =========================
  // 畫面
  // =========================

  return (

    <section>

      <h2>歷史訂單</h2>


      {orders.length === 0 ? (

        <div className="empty">
          目前沒有訂單。
        </div>

      ) : (

        orders.map(
          (order, index) => (

            <div
              className="order-card"
              key={order.id}
            >

              {/* =====================
                  訂單標題
              ===================== */}

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

              {/* =====================
                  收件與付款資訊
              ===================== */}

              <div className="order-shipping-info">

                <h4>
                  收件資訊
                </h4>


                {order.recipientName ? (

                  <>

                    <p>
                      <strong>
                        收件人：
                      </strong>

                      {order.recipientName}
                    </p>


                    <p>
                      <strong>
                        電話：
                      </strong>

                      {order.phone}
                    </p>


                    <p>
                      <strong>
                        收件地址：
                      </strong>

                      {order.shippingAddress}
                    </p>


                    <p>
                      <strong>
                        付款方式：
                      </strong>

                      {getPaymentMethodName(
                        order.paymentMethod
                      )}
                    </p>

                  </>

                ) : (

                  <p>
                    舊訂單未記錄收件資訊
                  </p>

                )}

              </div>

              {/* =====================
                  訂單商品
              ===================== */}

              {order.items.map(
                item => (

                  <div
                    className="order-item"
                    key={item.id}
                  >

                    {/* =====================
                        商品資料
                    ===================== */}

                    <div>

                      <span>

                        {item.productName}

                        {' × '}

                        {item.quantity}

                      </span>


                      <br />


                      <span>

                        NT${' '}

                        {(
                          Number(
                            item.price
                          ) *
                          item.quantity
                        ).toLocaleString()}

                      </span>

                    </div>


                    {/* =====================
                        尚未評價
                        而且表單尚未打開
                    ===================== */}

                    {reviewStatus[
                      item.productId
                    ]?.canReview &&
                      !showReviewForm[
                      item.productId
                      ] && (

                        <button
                          type="button"
                          onClick={() =>
                            openReviewForm(
                              item.productId
                            )
                          }
                        >
                          撰寫評價
                        </button>

                      )}


                    {/* =====================
                        已經評價
                    ===================== */}

                    {reviewStatus[
                      item.productId
                    ]?.reviewed && (

                        <div>

                          <span
                            className="reviewed-text"
                          >
                            ✓ 已評價
                          </span>


                          {' '}


                          <button
                            type="button"
                            onClick={() =>
                              toggleMyReview(
                                item.productId
                              )
                            }
                          >

                            {showMyReview[
                              item.productId
                            ]
                              ? '收起評價'
                              : '查看我的評價'}

                          </button>

                        </div>

                      )}


                    {/* =====================
                        查看自己的評價
                    ===================== */}

                    {showMyReview[
                      item.productId
                    ] && (

                        <div
                          className="my-review"
                        >

                          {myReviews[
                            item.productId
                          ] ? (

                            <>

                              <h4>
                                我的評價
                              </h4>


                              {/* 星等 */}
                              <div
                                className="review-stars"
                              >

                                {'★'.repeat(
                                  myReviews[
                                    item.productId
                                  ].rating
                                )}

                                {'☆'.repeat(
                                  5 -
                                  myReviews[
                                    item.productId
                                  ].rating
                                )}

                              </div>


                              {/* 評價文字 */}
                              {myReviews[
                                item.productId
                              ].comment ? (

                                <p>
                                  {
                                    myReviews[
                                      item.productId
                                    ].comment
                                  }
                                </p>

                              ) : (

                                <p>
                                  未填寫評價內容
                                </p>

                              )}


                              {/* 評價時間 */}
                              {myReviews[
                                item.productId
                              ].createdAt && (

                                  <small>

                                    評價時間：{' '}

                                    {new Date(
                                      myReviews[
                                        item.productId
                                      ].createdAt
                                    ).toLocaleString(
                                      'zh-TW'
                                    )}

                                  </small>

                                )}

                            </>

                          ) : (

                            <p>
                              找不到自己的評價資料
                            </p>

                          )}

                        </div>

                      )}


                    {/* =====================
                        撰寫評價表單
                    ===================== */}

                    {showReviewForm[
                      item.productId
                    ] &&
                      reviewStatus[
                        item.productId
                      ]?.canReview && (

                        <div
                          className="review-form"
                        >

                          <h4>
                            撰寫評價
                          </h4>


                          {/* =====================
                            星星
                        ===================== */}

                          <div
                            className="rating-input"
                          >

                            {[1, 2, 3, 4, 5]
                              .map(
                                star => (

                                  <button
                                    type="button"
                                    key={star}
                                    onClick={() =>
                                      setRating(
                                        item.productId,
                                        star
                                      )
                                    }
                                  >

                                    {star <=
                                      (
                                        reviewForm[
                                          item.productId
                                        ]?.rating ||
                                        0
                                      )
                                      ? '★'
                                      : '☆'}

                                  </button>

                                )
                              )}

                          </div>


                          {/* =====================
                            評價文字
                        ===================== */}

                          <textarea
                            maxLength="500"
                            placeholder=
                            "請輸入商品評價..."
                            value={
                              reviewForm[
                                item.productId
                              ]?.comment ||
                              ''
                            }
                            onChange={e =>
                              setComment(
                                item.productId,
                                e.target.value
                              )
                            }
                          />


                          {/* =====================
                            提醒
                        ===================== */}

                          <p
                            className=
                            "review-warning"
                          >
                            ⚠ 評價送出後無法修改
                          </p>


                          {/* =====================
                            取消
                        ===================== */}

                          <button
                            type="button"
                            onClick={() =>
                              cancelReview(
                                item.productId
                              )
                            }
                          >
                            取消
                          </button>


                          {' '}


                          {/* =====================
                            送出
                        ===================== */}

                          <button
                            type="button"
                            onClick={() =>
                              submitReview(
                                item.productId
                              )
                            }
                          >
                            送出評價
                          </button>

                        </div>

                      )}

                  </div>

                )
              )}


              {/* =====================
                  訂單總金額
              ===================== */}

              <div
                className="order-total"
              >

                總金額：NT${' '}

                {Number(
                  order.totalAmount
                ).toLocaleString()}

              </div>

            </div>

          )
        )

      )}

    </section>

  )
}