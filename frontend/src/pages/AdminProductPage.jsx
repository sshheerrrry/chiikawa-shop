import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'


// =========================
// 角色下拉式選單
// =========================
const characterOptions = [
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


// =========================
// 空白商品表單
// =========================
const emptyForm = {
  series: '',
  characterName: '',
  description: '',
  price: '',
  stock: 10,
  imageUrl: ''
}


export default function AdminProductPage({ user }) {

  // =========================
  // 商品資料
  // =========================
  const [products, setProducts] = useState([])

  const [form, setForm] = useState(emptyForm)

  const [editingId, setEditingId] = useState(null)


  // =========================
  // 圖片
  // =========================
  const [imageFile, setImageFile] = useState(null)

  const [preview, setPreview] = useState('')

  const fileInputRef = useRef(null)


  // =========================
  // 搜尋條件
  // =========================
  const [searchSeries, setSearchSeries] = useState('')

  const [searchCharacter, setSearchCharacter] = useState('')

  const [seriesOptions, setSeriesOptions] = useState([])

  const navigate = useNavigate()


  // =========================
  // 查詢所有商品
  // =========================
  const load = async () => {

    try {

      const res = await api.get('/admin/products')

      setProducts(res.data)

    } catch (e) {

      console.error('商品讀取失敗', e)
    }
  }

  const loadSeries = async () => {

    try {

      const res = await api.get('/products/series')

      setSeriesOptions(res.data)

    } catch (e) {

      console.error('系列讀取失敗', e)
    }
  }


  // =========================
  // 管理員權限檢查
  // =========================
  useEffect(() => {

    if (user && user.role !== 'ADMIN') {
      navigate('/')
    } else if (user?.role === 'ADMIN') {
      load()
      loadSeries()
    }

  }, [user])

  // =========================
  // 系列 / 角色改變時
  // 自動更新商品說明
  //
  // 新增、修改商品都會執行
  // =========================
  useEffect(() => {

    if (form.series && form.characterName) {

      setForm(prev => ({
        ...prev,
        description:
          `${prev.series}系列－${prev.characterName}`
      }))

    } else {

      setForm(prev => ({
        ...prev,
        description: ''
      }))
    }

  }, [
    form.series,
    form.characterName
  ])


  if (!user) {

    return <p>請先登入管理員帳號。</p>
  }


  if (user.role !== 'ADMIN') {

    return null
  }


  // =========================
  // 修改表單資料
  // =========================
  const change = (key, value) => {

    setForm(prev => ({
      ...prev,
      [key]: value
    }))
  }


  // =========================
  // 選擇圖片
  // =========================
  const handleImageChange = (e) => {

    const file = e.target.files[0]

    if (!file) {
      return
    }


    // 清掉舊的 blob 預覽網址
    if (preview.startsWith('blob:')) {

      URL.revokeObjectURL(preview)
    }


    setImageFile(file)


    const previewUrl =
      URL.createObjectURL(file)


    setPreview(previewUrl)
  }


  // =========================
  // 上傳圖片
  // =========================
  const uploadImage = async () => {

    // 修改商品時如果沒選新圖片
    // 就使用原本圖片
    if (!imageFile) {

      return form.imageUrl
    }


    const formData =
      new FormData()


    formData.append(
      'file',
      imageFile
    )


    const res = await api.post(
      '/admin/images/upload',
      formData
    )


    return res.data.imageUrl
  }


  // =========================
  // 新增 / 修改商品
  // =========================
  const submit = async (e) => {

    e.preventDefault()


    // =====================
    // 新增商品時
    // 圖片為必填
    // =====================
    if (!editingId && !imageFile) {

      alert('新增商品時必須選擇商品圖片')

      return
    }


    try {

      // 先上傳圖片
      const imageUrl =
        await uploadImage()


      const payload = {

        ...form,

        price:
          Number(form.price),

        stock:
          Number(form.stock),

        imageUrl:
          imageUrl
      }


      // 記住現在是新增還是修改
      const isEditing =
        editingId !== null


      // =====================
      // 修改商品
      // =====================
      if (isEditing) {

        await api.put(
          `/admin/products/${editingId}`,
          payload
        )

      } else {

        // =====================
        // 新增商品
        // =====================
        await api.post(
          '/admin/products',
          payload
        )
      }


      // =====================
      // 成功訊息
      // =====================
      if (isEditing) {

        alert('修改成功')

      } else {

        alert('新增成功')
      }


      // 清空表單
      resetForm()

      // 重新讀取商品
      await load()
      await loadSeries()

    } catch (e) {

      console.error(e)

      alert(
        e.response?.data?.message ||
        '儲存失敗'
      )
    }
  }


  // =========================
  // 修改商品
  // =========================
  const edit = (p) => {

    setEditingId(p.id)


    setForm({

      series:
        p.series,

      characterName:
        p.characterName,

      description:
        p.description || '',

      price:
        p.price,

      stock:
        p.stock,

      imageUrl:
        p.imageUrl || ''
    })


    // 修改時還沒有選新圖片
    setImageFile(null)


    // =====================
    // 顯示目前舊圖片
    // =====================
    if (
      p.imageUrl &&
      p.imageUrl !== '/images/placeholder.svg'
    ) {

      setPreview(
        `http://localhost:8080${p.imageUrl}`
      )

    } else if (
      p.imageUrl === '/images/placeholder.svg'
    ) {

      setPreview(
        '/images/placeholder.svg'
      )

    } else {

      setPreview('')
    }


    // 清除 file input
    if (fileInputRef.current) {

      fileInputRef.current.value = ''
    }


    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    })
  }

  // =========================
  // 下架商品
  // =========================
  const deactivate = async (id) => {

    if (!confirm('確定要下架這個商品嗎？')) {
      return
    }

    try {

      await api.patch(
        `/admin/products/${id}/deactivate`
      )

      alert('商品已下架')

      // 重新取得商品
      await load()

      // 系列可能因商品全部下架而改變
      await loadSeries()

    } catch (e) {

      console.error(e)

      alert(
        e.response?.data?.message ||
        '商品下架失敗'
      )
    }
  }

  // =========================
  // 重新上架商品
  // =========================
  const activate = async (id) => {

    if (!confirm('確定要重新上架這個商品嗎？')) {
      return
    }

    try {

      await api.patch(
        `/admin/products/${id}/activate`
      )

      alert('商品已重新上架')

      // 重新取得商品
      await load()

      // 重新取得系列
      await loadSeries()

    } catch (e) {

      console.error(e)

      alert(
        e.response?.data?.message ||
        '商品重新上架失敗'
      )
    }
  }



  // =========================
  // 清空表單
  // =========================
  const resetForm = () => {

    if (preview.startsWith('blob:')) {

      URL.revokeObjectURL(preview)
    }


    setForm(emptyForm)

    setEditingId(null)

    setImageFile(null)

    setPreview('')


    if (fileInputRef.current) {

      fileInputRef.current.value = ''
    }
  }


  // =========================
  // 商品搜尋
  // 前端篩選
  // =========================
  const filteredProducts =
    products.filter(product => {

      const seriesMatch =

        !searchSeries ||

        product.series
          ?.toLowerCase()
          .includes(
            searchSeries.toLowerCase()
          )


      const characterMatch =

        !searchCharacter ||

        product.characterName ===
        searchCharacter


      return (
        seriesMatch &&
        characterMatch
      )
    })

  //匯出
  const exportPdf = async () => {
    try {
      const res = await api.get(
        '/admin/products/export/pdf',
        {
          responseType: 'blob'
        }
      )

      const url = window.URL.createObjectURL(
        new Blob(
          [res.data],
          { type: 'application/pdf' }
        )
      )

      const link =
        document.createElement('a')

      link.href = url

      link.setAttribute(
        'download',
        'chiikawa-products.pdf'
      )

      document.body.appendChild(link)

      link.click()

      link.remove()

      window.URL.revokeObjectURL(url)

    } catch (e) {
      alert('PDF 匯出失敗')
    }
  }

  // =========================
  // React 畫面
  // =========================
  return (

    <section>

      <h2>
        管理員－商品管理
      </h2>

      <button
        type="button"
        onClick={exportPdf}
      >
        匯出商品清單PDF
      </button>

      {/* =====================================
          新增 / 修改商品
          ===================================== */}

      <form
        className="admin-form"
        onSubmit={submit}
      >

        <h3>

          {editingId
            ? '修改商品'
            : '新增商品'}

        </h3>


        {/* =====================================
            系列
            可自由輸入，也會提示既有系列
            ===================================== */}

        <input
          type="text"
          list="series-suggestions"
          placeholder="系列，例如：天使"
          value={form.series}
          onChange={
            e =>
              change(
                'series',
                e.target.value
              )
          }
          autoComplete="off"
          required
        />

        <datalist id="series-suggestions">

          {seriesOptions.map(series => (

            <option
              key={series}
              value={series}
            />

          ))}

        </datalist>


        {/* =====================================
            角色下拉式選單
            ===================================== */}

        <select
          value={form.characterName}
          onChange={
            e =>
              change(
                'characterName',
                e.target.value
              )
          }
          required
        >

          <option value="">
            請選擇角色
          </option>


          {characterOptions.map(
            character => (

              <option
                key={character}
                value={character}
              >

                {character}

              </option>
            )
          )}

        </select>


        {/* =====================================
            商品說明
            新增時會自動預填
            ===================================== */}

        <textarea
          placeholder="商品說明"
          value={form.description}
          onChange={
            e =>
              change(
                'description',
                e.target.value
              )
          }
        />


        {/* 價格 */}
        <input
          type="number"
          placeholder="價格"
          min="0"
          value={form.price}
          onChange={
            e =>
              change(
                'price',
                e.target.value
              )
          }
          required
        />


        {/* 庫存 */}
        <input
          type="number"
          placeholder="庫存"
          min="0"
          value={form.stock}
          onChange={
            e =>
              change(
                'stock',
                e.target.value
              )
          }
          required
        />


        {/* =====================================
            圖片
            新增商品時必填
            修改商品時可以不重新選
            ===================================== */}

        <div>

          <label>
            商品圖片
            {!editingId && '（必填）'}
          </label>

          <input
            ref={fileInputRef}
            type="file"
            accept=".jpg,.jpeg,.png,.webp,image/*"
            onChange={handleImageChange}
            required={!editingId}
          />

        </div>


        {/* =====================================
            圖片預覽
            ===================================== */}

        {preview && (

          <div>

            <p>
              圖片預覽：
            </p>

            <img
              src={preview}
              alt="商品圖片預覽"
              style={{
                width: '200px',
                height: '200px',
                objectFit: 'cover'
              }}
            />

          </div>
        )}


        {/* =====================================
            新增 / 修改按鈕
            ===================================== */}

        <div className="button-row">

          <button type="submit">

            {editingId
              ? '儲存修改'
              : '新增商品'}

          </button>


          {editingId && (

            <button
              type="button"
              className="secondary"
              onClick={resetForm}
            >

              取消

            </button>
          )}

        </div>

      </form>


      {/* =====================================
          商品搜尋
          ===================================== */}

      <div className="search-bar">

        <select
          value={searchSeries}
          onChange={
            e =>
              setSearchSeries(
                e.target.value
              )
          }
        >

          <option value="">全部系列</option>

          {seriesOptions.map(series => (
            <option
              key={series}
              value={series}
            >
              {series}
            </option>

          ))}

        </select>


        <select
          value={searchCharacter}
          onChange={
            e =>
              setSearchCharacter(
                e.target.value
              )
          }
        >

          <option value="">全部角色</option>

          {characterOptions.map(
            character => (

              <option
                key={character}
                value={character}
              >

                {character}

              </option>
            )
          )}

        </select>


        <button
          type="button"
          onClick={() => {
            setSearchSeries('')
            setSearchCharacter('')
          }}
        >清除搜尋</button>
      </div>


      {/* =====================================
          商品列表
          ===================================== */}

      <div className="admin-table-wrap">

        <table>

          <thead>

            <tr>

              <th>序號</th>

              <th>系列</th>

              <th>角色</th>

              <th>價格</th>

              <th>庫存</th>

              <th>狀態</th>

              <th>操作</th>

            </tr>

          </thead>


          <tbody>

            {filteredProducts.map(
              (p, index) => (

                <tr key={p.id}>

                  <td>
                    {index + 1}
                  </td>

                  <td>
                    {p.series}
                  </td>

                  <td>
                    {p.characterName}
                  </td>

                  <td>

                    NT${' '}

                    {Number(
                      p.price
                    ).toLocaleString()}

                  </td>

                  <td>
                    {p.stock}
                  </td>

                  <td>
                    {p.active
                      ? '上架中'
                      : '已下架'}
                  </td>

                  <td>
                    <button
                      type="button"
                      onClick={
                        () => edit(p)
                      }
                    >

                      修改

                    </button>

                    {p.active ? (
                      // active=true
                      // 顯示下架
                      <button
                        type="button"
                        className="danger"
                        onClick={
                          () => deactivate(p.id)
                        }
                      >

                        下架

                      </button>

                    ) : (
                      // active=false
                      // 顯示重新上架
                      <button
                        type="button"
                        onClick={
                          () => activate(p.id)
                        }
                      >

                        重新上架

                      </button>

                    )}

                  </td>

                </tr>
              )
            )}

          </tbody>

        </table>


        {filteredProducts.length === 0 && (

          <p>
            找不到符合條件的商品。
          </p>

        )}

      </div>

    </section>
  )
}