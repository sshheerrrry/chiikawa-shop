import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

const characterOptions = [
  '',
  '吉伊',
  '小八',
  '烏薩奇',
  '小桃',
  '師傅',
  '栗子饅頭',
  '獅薩',
  '古本',
  '其他'
]

export default function ProductPage({ user }) {
  const [products, setProducts] = useState([])
  const [favoriteIds, setFavoriteIds] = useState([])
  const [currentPage, setCurrentPage] = useState(1)
  const [totalPages, setTotalPages] = useState(0)
  const searchBarRef = useRef(null)
  const pageSize = 6

  const [seriesOptions, setSeriesOptions] = useState([])
  const [series, setSeries] = useState('')
  const [characterName, setCharacterName] = useState('')
  //每個商品的評價資料
  const [reviews, setReviews] = useState({})
  //哪個商品目前展開評價區
  const [showReviews, setShowReviews] = useState({})
  // 每個商品的平均星等 + 評價數
  const [reviewSummaries, setReviewSummaries] = useState({})

  const navigate = useNavigate()

  // 讀取所有系列
  const loadSeries = async () => {
    try {
      const res = await api.get('/products/series')
      setSeriesOptions(res.data)
    } catch (e) {
      console.error('系列讀取失敗', e)
    }
  }

  // =======================
  // 讀取商品平均星等 + 評價數
  // =======================
  const loadReviewSummaries = async (
    productList
  ) => {

    try {

      const requests =
        productList.map(product =>
          api.get(
            `/products/${product.id}/reviews/summary`
          )
        )


      const responses =
        await Promise.all(requests)


      const summaryMap = {}


      productList.forEach(
        (product, index) => {

          summaryMap[product.id] =
            responses[index].data
        }
      )


      setReviewSummaries(
        summaryMap
      )

    } catch (e) {

      console.error(
        '商品評價統計讀取失敗',
        e
      )
    }
  }

  /// =======================
  // 載入商品
  // =======================
  const loadProducts = async (
    page = 1
  ) => {

    try {

      let res


      // =======================
      // 有搜尋條件
      // =======================
      if (
        series ||
        characterName
      ) {

        res = await api.get(
          '/products/search',
          {
            params: {
              series,
              characterName,
              page,
              size: pageSize
            }
          }
        )

      }

      // =======================
      // 沒有搜尋條件
      // =======================
      else {

        res = await api.get(
          '/products',
          {
            params: {
              page,
              size: pageSize
            }
          }
        )

      }


      // =======================
      // 取得目前這一頁的商品
      // =======================
      const productList =
        res.data.content


      // 商品資料
      setProducts(
        productList
      )


      // 目前頁數
      setCurrentPage(
        res.data.currentPage
      )


      // 總頁數
      setTotalPages(
        res.data.totalPages
      )


      // =======================
      // 取得這一頁商品的
      // 平均星等 + 評價數
      // =======================
      await loadReviewSummaries(
        productList
      )


    } catch (e) {

      console.error(
        '商品讀取失敗',
        e
      )

    }
  }

  // =======================
  // 商品分頁切換
  // =======================
  const changePage = async (page) => {

    // 載入指定頁商品
    await loadProducts(page)

    // 回到搜尋列上方
    searchBarRef.current?.scrollIntoView({
      behavior: 'smooth',
      block: 'start'
    })
  }

  // =======================
  // 清除搜尋條件
  // =======================
  const clearSearch = async () => {

    // 清除搜尋條件
    setSeries('')
    setCharacterName('')

    try {

      // 直接取得全部商品第 1 頁
      const res = await api.get(
        '/products',
        {
          params: {
            page: 1,
            size: pageSize
          }
        }
      )


      const productList =
        res.data.content


      // 更新商品
      setProducts(
        productList
      )


      // 回到第 1 頁
      setCurrentPage(
        res.data.currentPage
      )


      // 更新總頁數
      setTotalPages(
        res.data.totalPages
      )


      // 重新取得評價統計
      await loadReviewSummaries(
        productList
      )


    } catch (e) {

      console.error(
        '清除搜尋失敗',
        e
      )
    }
  }

  // =======================
  // 讀取目前會員收藏
  // =======================
  const loadFavorites = async () => {

    if (!user) {

      setFavoriteIds([])
      return
    }

    try {

      const res =
        await api.get('/favorites')

      const ids =
        res.data.map(
          favorite =>
            favorite.productId
        )

      setFavoriteIds(ids)

    } catch (e) {

      console.error(
        '收藏資料讀取失敗',
        e
      )
    }
  }

  //讀取商品評價
  const loadReviews = async (productId) => {

    try {

      const res = await api.get(
        `/products/${productId}/reviews`
      )

      setReviews(prev => ({
        ...prev,
        [productId]: res.data
      }))

    } catch (e) {

      console.error(
        '讀取商品評價失敗',
        e
      )
    }
  }

  //查看評價
  const toggleReviews = async (
    productId
  ) => {
    //不需要商品頁一載入就查全部商品評價，使用者點查看評價才去查該商品
    const willOpen =
      !showReviews[productId]

    setShowReviews(prev => ({
      ...prev,
      [productId]: willOpen
    }))


    if (willOpen) {
      await loadReviews(productId)
    }
  }

  // 第一次進入頁面
  useEffect(() => {
    loadProducts(1)
    loadSeries()
  }, [])

  useEffect(() => {

    if (user) {

      loadFavorites()

    } else {

      setFavoriteIds([])
    }

  }, [user])

  // 加入購物車
  const addToCart = async (productId) => {
    if (!user) {
      alert('加入購物車前請先登入會員')
      navigate('/login')
      return
    }

    try {
      await api.post('/cart/items', {
        productId,
        quantity: 1
      })

      alert('已加入購物車')

    } catch (e) {
      alert(
        e.response?.data?.message ||
        '加入購物車失敗'
      )
    }
  }

  // =======================
  // 收藏 / 取消收藏
  // =======================
  const toggleFavorite = async (
    productId
  ) => {

    // 未登入
    if (!user) {

      alert('收藏商品前請先登入會員')

      navigate('/login')

      return
    }


    const isFavorite =
      favoriteIds.includes(
        productId
      )


    try {

      // =====================
      // 已收藏 → 取消收藏
      // =====================
      if (isFavorite) {

        await api.delete(
          `/favorites/${productId}`
        )


        setFavoriteIds(prev =>
          prev.filter(
            id => id !== productId
          )
        )

      } else {

        // =====================
        // 尚未收藏 → 新增收藏
        // =====================
        await api.post(
          `/favorites/${productId}`
        )


        setFavoriteIds(prev => [
          ...prev,
          productId
        ])
      }

    } catch (e) {

      alert(
        e.response?.data?.message ||
        '收藏操作失敗'
      )
    }
  }

  return (
    <section>

      {/* =======================
          Hero
      ======================= */}
      <div className="hero">
        <div>
          <h1>吉伊卡哇商店</h1>
          <p>選擇喜歡的系列與角色。</p>
        </div>

        <div className="hero-face">
          ૮₍ ˶ᵔ ᵕ ᵔ˶ ₎ა
        </div>
      </div>


      {/* =======================
          搜尋區
      ======================= */}
      <div className="search-bar" ref={searchBarRef}>

        {/* 系列 */}
        <select
          value={series}
          onChange={e =>
            setSeries(e.target.value)
          }
        >
          <option value="">
            全部系列
          </option>

          {seriesOptions.map(value => (
            <option
              key={value}
              value={value}
            >
              {value}
            </option>
          ))}
        </select>


        {/* 角色 */}
        <select
          value={characterName}
          onChange={e =>
            setCharacterName(
              e.target.value
            )
          }
        >
          {characterOptions.map(value => (
            <option
              key={value}
              value={value}
            >
              {value || '全部角色'}
            </option>
          ))}
        </select>


        {/* 搜尋 */}
        <button
          type="button"
          onClick={() =>
            loadProducts(1)
          }
        >
          搜尋
        </button>


        {/* 清除搜尋 */}
        <button
          type="button"
          className="clear-search-btn"
          onClick={clearSearch}
        >
          清除
        </button>

      </div>


      {/* =======================
          商品列表
      ======================= */}
      <div className="product-grid">

        {products.map(product => (

          <article
            className="product-card"
            key={product.id}
          >

            <img
              src={
                product.imageUrl &&
                  product.imageUrl !==
                  '/images/placeholder.svg'
                  ? `http://localhost:8080${product.imageUrl}`
                  : '/images/placeholder.svg'
              }
              alt={
                `${product.series} ${product.characterName}`
              }
            />


            <div className="product-body">

              <span className="category">
                {product.series}
              </span>


              <h3>
                {product.series}
                －
                {product.characterName}
              </h3>


              <p>
                {product.description}
              </p>


              <div className="product-bottom">

                {/* 商品價格 */}
                <strong>
                  NT${' '}
                  {Number(
                    product.price
                  ).toLocaleString()}
                </strong>


                {/* 平均星等 + 評價數 */}
                <span className="product-rating">

                  {reviewSummaries[
                    product.id
                  ]?.reviewCount > 0 ? (

                    <>
                      ★{' '}

                      {Number(
                        reviewSummaries[
                          product.id
                        ].averageRating
                      ).toFixed(1)}

                      {' '}

                      （
                      {reviewSummaries[
                        product.id
                      ].reviewCount}
                      則評價）
                    </>

                  ) : (

                    <>尚無評價</>

                  )}

                </span>

              </div>

              {/* 收藏 */}
              <button
                type="button"
                className={
                  favoriteIds.includes(product.id)
                    ? 'favorite-btn active'
                    : 'favorite-btn'
                }
                onClick={() =>
                  toggleFavorite(product.id)
                }
              >
                {favoriteIds.includes(product.id)
                  ? '♥ 已收藏'
                  : '♡ 收藏'}
              </button>

              {/* 加入購物車 */}
              <button
                type="button"
                onClick={() =>
                  addToCart(product.id)
                }
              >
                加入購物車
              </button>

              <button
                type="button"
                onClick={() =>
                  toggleReviews(product.id)
                }
              >
                {showReviews[product.id]
                  ? '收起評價'
                  : '查看評價'}
              </button>

              {showReviews[product.id] && (

                <div className="review-section">

                  <h4>商品評價</h4>


                  {/* ===================== */}
                  {/* 評價列表 */}
                  {/* ===================== */}

                  {(reviews[product.id] || [])
                    .length === 0 ? (

                    <p>目前尚無評價</p>

                  ) : (

                    (reviews[product.id] || [])
                      .map(review => (

                        <div
                          className="review-item"
                          key={review.id}
                        >

                          {/* 評價會員 */}
                          <strong>
                            {review.username}
                          </strong>


                          {/* 星等 */}
                          <div className="review-stars">

                            {'★'.repeat(
                              review.rating
                            )}

                            {'☆'.repeat(
                              5 - review.rating
                            )}

                          </div>


                          {/* 評價內容 */}
                          {review.comment && (

                            <p>
                              {review.comment}
                            </p>

                          )}


                          {/* 評價建立時間 */}
                          {review.createdAt && (

                            <small
                              className="review-date"
                            >

                              {/*評價時間：{' '}*/}

                              {new Date(
                                review.createdAt
                              ).toLocaleString(
                                'zh-TW'
                              )}

                            </small>

                          )}

                        </div>

                      ))
                  )}

                </div>

              )}
            </div>

          </article>

        ))}

      </div>


      {/* =======================
          分頁
          放在商品列表最下面
      ======================= */}
      {totalPages > 1 && (

        <div className="pagination">

          {/* 上一頁 */}
          <button
            type="button"
            disabled={
              currentPage === 1
            }
            onClick={() =>
              changePage(
                currentPage - 1
              )
            }
          >
            上一頁
          </button>


          {/* 頁碼 */}
          {Array.from(
            { length: totalPages },
            (_, i) => i + 1
          ).map(page => (

            <button
              type="button"
              key={page}
              className={
                currentPage === page
                  ? 'active-page'
                  : ''
              }
              onClick={() =>
                changePage(page)
              }
            >
              {page}
            </button>

          ))}


          {/* 下一頁 */}
          <button
            type="button"
            disabled={
              currentPage ===
              totalPages
            }
            onClick={() =>
              changePage(
                currentPage + 1
              )
            }
          >
            下一頁
          </button>

        </div>

      )}

    </section>
  )
}