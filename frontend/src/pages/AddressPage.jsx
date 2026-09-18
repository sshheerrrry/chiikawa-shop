import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'


export default function AddressPage({ user }) {
    const navigate = useNavigate()
    const [addresses, setAddresses] = useState([])
    const [showForm, setShowForm] = useState(false)
    const [editingId, setEditingId] = useState(null)

    const [form, setForm] =
        useState({
            recipientName: '',
            phone: '',
            postalCode: '',
            city: '',
            district: '',
            address: '',
            isDefault: false
        })


    // =========================
    // 查詢我的所有地址
    // =========================
    const loadAddresses = async () => {

        if (!user) return

        try {

            const res =
                await api.get('/addresses')

            setAddresses(res.data)

        } catch (e) {

            console.error(
                '讀取收件地址失敗',
                e
            )

            alert(
                e.response?.data?.message ||
                '讀取收件地址失敗'
            )
        }
    }


    // =========================
    // 登入會員改變時
    // 重新讀取地址
    // =========================
    useEffect(() => {

        if (user) {

            loadAddresses()
        }

    }, [user])


    // =========================
    // 表單欄位修改
    // =========================
    const handleChange = e => {

        const {
            name,
            value,
            type,
            checked
        } = e.target


        setForm(prev => ({
            ...prev,

            [name]:
                type === 'checkbox'
                    ? checked
                    : value
        }))
    }


    // =========================
    // 新增地址
    // =========================
    const createAddress = async e => {

        e.preventDefault()

        try {

            await api.post(
                '/addresses',
                form
            )


            alert('新增收件地址成功')


            // 清空表單
            setForm({
                recipientName: '',
                phone: '',
                postalCode: '',
                city: '',
                district: '',
                address: '',
                isDefault: false
            })


            // 關閉表單
            setShowForm(false)


            // 重新讀取地址
            await loadAddresses()

        } catch (e) {

            console.error(
                '新增收件地址失敗',
                e
            )

            alert(
                e.response?.data?.message ||
                '新增收件地址失敗'
            )
        }
    }

    // =========================
    // 設為預設地址
    // =========================
    const setDefaultAddress = async id => {

        try {

            await api.patch(
                `/addresses/${id}/default`
            )

            // 重新取得地址
            await loadAddresses()

        } catch (e) {

            console.error(
                '設定預設地址失敗',
                e
            )

            alert(
                e.response?.data?.message ||
                '設定預設地址失敗'
            )
        }
    }

    // =========================
    // 開始修改地址
    // =========================
    const startEdit = address => {

        setEditingId(address.id)

        setForm({
            recipientName:
                address.recipientName,

            phone:
                address.phone,

            postalCode:
                address.postalCode || '',

            city:
                address.city,

            district:
                address.district,

            address:
                address.address,

            isDefault:
                address.isDefault
        })

        setShowForm(true)

        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        })
    }

    // =========================
    // 取消新增 / 修改
    // =========================
    const cancelForm = () => {

        setEditingId(null)

        setShowForm(false)

        setForm({
            recipientName: '',
            phone: '',
            postalCode: '',
            city: '',
            district: '',
            address: '',
            isDefault: false
        })
    }

    // =========================
    // 修改地址
    // =========================
    const updateAddress = async e => {

        e.preventDefault()

        try {

            await api.put(
                `/addresses/${editingId}`,
                form
            )

            alert('修改收件地址成功')

            cancelForm()

            await loadAddresses()

        } catch (e) {

            console.error(
                '修改收件地址失敗',
                e
            )

            alert(
                e.response?.data?.message ||
                '修改收件地址失敗'
            )
        }
    }

    // =========================
    // 刪除地址
    // =========================
    const deleteAddress = async address => {

        const confirmed =
            window.confirm(
                `確定要刪除「${address.recipientName}」的收件地址嗎？`
            )

        // 使用者按取消
        if (!confirmed) {
            return
        }


        try {

            await api.delete(
                `/addresses/${address.id}`
            )

            alert('刪除收件地址成功')

            await loadAddresses()

        } catch (e) {

            console.error(
                '刪除收件地址失敗',
                e
            )

            alert(
                e.response?.data?.message ||
                '刪除收件地址失敗'
            )
        }
    }

    // =========================
    // 尚未登入
    // =========================
    if (!user) {

        return (
            <section>

                <h2>我的收件地址</h2>

                <p>
                    請先登入。
                </p>

                <button
                    onClick={() =>
                        navigate('/login')
                    }
                >
                    前往登入
                </button>

            </section>
        )
    }


    return (
        <section>

            <h2>我的收件地址</h2>


            {/* =========================
          新增地址按鈕
         ========================= */}

            {!showForm && (

                <button
                    onClick={() => {

                        setEditingId(null)

                        setForm({
                            recipientName: '',
                            phone: '',
                            postalCode: '',
                            city: '',
                            district: '',
                            address: '',
                            isDefault: false
                        })

                        setShowForm(true)
                    }}
                >
                    ＋ 新增收件地址
                </button>

            )}


            {/* =========================
          新增地址表單
         ========================= */}

            {showForm && (

                <form
                    onSubmit={
                        editingId
                            ? updateAddress
                            : createAddress
                    }
                    className="address-form"
                >

                    <h3>
                        {editingId
                            ? '修改收件地址'
                            : '新增收件地址'}
                    </h3>


                    <label>
                        收件人姓名
                    </label>

                    <input
                        type="text"
                        name="recipientName"
                        value={form.recipientName}
                        onChange={handleChange}
                        required
                    />


                    <label>
                        手機號碼
                    </label>

                    <input
                        type="tel"
                        name="phone"
                        value={form.phone}
                        onChange={handleChange}
                        required
                    />


                    <label>
                        郵遞區號
                    </label>

                    <input
                        type="text"
                        name="postalCode"
                        value={form.postalCode}
                        onChange={handleChange}
                    />


                    <label>
                        縣市
                    </label>

                    <input
                        type="text"
                        name="city"
                        value={form.city}
                        onChange={handleChange}
                        required
                    />


                    <label>
                        鄉鎮市區
                    </label>

                    <input
                        type="text"
                        name="district"
                        value={form.district}
                        onChange={handleChange}
                        required
                    />


                    <label>
                        詳細地址
                    </label>

                    <input
                        type="text"
                        name="address"
                        value={form.address}
                        onChange={handleChange}
                        required
                    />


                    <label className="default-checkbox">

                        <input
                            type="checkbox"
                            name="isDefault"
                            checked={form.isDefault}
                            onChange={handleChange}
                        />

                        <span>
                            設為預設地址
                        </span>

                    </label>


                    <div>

                        <button type="submit">
                            {editingId
                                ? '儲存修改'
                                : '儲存地址'}
                        </button>


                        <button
                            type="button"
                            onClick={cancelForm}
                        >
                            取消
                        </button>

                    </div>

                </form>

            )}


            {/* =========================
            地址清單
         ========================= */}

            {addresses.length === 0 ? (

                <div className="empty">

                    尚未建立收件地址。

                </div>

            ) : (

                <div className="address-list">

                    {addresses.map(address => (

                        <div
                            className="address-card"
                            key={address.id}
                        >

                            <div>

                                <h3>

                                    {address.recipientName}

                                    {address.isDefault && (
                                        <span>
                                            {' '}（預設地址）
                                        </span>
                                    )}

                                </h3>


                                <p>
                                    {address.phone}
                                </p>


                                <p>
                                    {address.postalCode}
                                    {' '}
                                    {address.city}
                                    {address.district}
                                    {address.address}
                                </p>


                                {!address.isDefault && (

                                    <button
                                        type="button"
                                        onClick={() =>
                                            setDefaultAddress(
                                                address.id
                                            )
                                        }
                                    >
                                        設為預設地址
                                    </button>

                                )}

                                <button
                                    type="button"
                                    onClick={() =>
                                        startEdit(address)
                                    }
                                >
                                    修改地址
                                </button>


                                <button
                                    type="button"
                                    className="danger"
                                    onClick={() =>
                                        deleteAddress(address)
                                    }
                                >
                                    刪除地址
                                </button>

                            </div>

                        </div>

                    ))}

                </div>

            )}

        </section>
    )
}