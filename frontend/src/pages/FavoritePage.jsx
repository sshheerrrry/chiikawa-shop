import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function FavoritePage({ user }) {

  const [favorites, setFavorites] = useState([])

  const navigate = useNavigate()


  // =========================
  // 讀取收藏
  // =========================
  const loadFavorites = async () => {

    try {

      const res =
        await api.get('/favorites')

      setFavorites(res.data)

    } catch (e) {

      console.error(
        '收藏讀取失敗',
        e
      )
    }
  }


  useEffect(() => {

    if (!user) {

      navigate('/login')

      return
    }

    loadFavorites()

  }, [user])


  // =========================
  // 取消收藏
  // =========================
  const removeFavorite = async (
    productId
  ) => {

    try {

      await api.delete(
        `/favorites/${productId}`
      )

      setFavorites(prev =>
        prev.filter(
          item =>
            item.productId !== productId
        )
      )

    } catch (e) {

      alert(
        e.response?.data?.message ||
        '取消收藏失敗'
      )
    }
  }


  // =========================
  // 加入購物車
  // =========================
  const addToCart = async (
    productId
  ) => {

    try {

      await api.post(
        '/cart/items',
        {
          productId,
          quantity: 1
        }
      )

      alert('已加入購物車')

    } catch (e) {

      alert(
        e.response?.data?.message ||
        '加入購物車失敗'
      )
    }
  }


  if (!user) {
    return null
  }


  return (

    <section>

      <h2>我的收藏</h2>


      {favorites.length === 0 ? (

        <p>
          尚無收藏商品
        </p>

      ) : (

        <div className="product-grid">

          {favorites.map(item => (

            <article
              className="product-card"
              key={item.favoriteId}
            >

              <img
                src={
                  item.imageUrl &&
                  item.imageUrl !==
                    '/images/placeholder.svg'
                    ? `http://localhost:8080${item.imageUrl}`
                    : '/images/placeholder.svg'
                }
                alt={
                  `${item.series} ${item.characterName}`
                }
              />


              <div className="product-body">

                <span className="category">

                  {item.series}

                </span>


                <h3>

                  {item.series}
                  －
                  {item.characterName}

                </h3>


                <p>

                  {item.description}

                </p>


                <div className="product-bottom">

                  <strong>

                    NT${' '}

                    {Number(
                      item.price
                    ).toLocaleString()}

                  </strong>

                </div>


                <button
                  type="button"
                  onClick={() =>
                    addToCart(
                      item.productId
                    )
                  }
                >
                  加入購物車
                </button>


                <button
                  type="button"
                  className="favorite-btn active"
                  onClick={() =>
                    removeFavorite(
                      item.productId
                    )
                  }
                >
                  ♥ 取消收藏
                </button>

              </div>

            </article>

          ))}

        </div>

      )}

    </section>
  )
}