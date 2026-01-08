import { useEffect, useMemo, useState } from "react";
import {
  createVariant,
  getVariantsByProduct,
  updateVariant,
  deleteVariant,
} from "../api/variants";

// Eğer App.jsx'ten selectedProductId gönderiyorsan:
// <VariantsPage selectedProductId={selectedProductId} />
export default function VariantsPage({ selectedProductId }) {
  const [productId, setProductId] = useState("");
  const [variants, setVariants] = useState([]);
  const [err, setErr] = useState("");
  const [info, setInfo] = useState("");
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  const [form, setForm] = useState({
    sku: "",
    name: "",
    price: "",
    stock: "",
  });

  // edit state
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState({
    sku: "",
    name: "",
    price: "",
    stock: "",
  });

  // App.jsx’ten selectedProductId geldiyse otomatik bağla
  useEffect(() => {
    if (selectedProductId !== undefined && selectedProductId !== null) {
      setProductId(String(selectedProductId));
    }
  }, [selectedProductId]);

  function toNumberOrNull(v) {
    if (v === "" || v === null || v === undefined) return 0;
    const n = Number(v);
    return Number.isFinite(n) ? n : 0;
  }

  function normalizeListData(data) {
    if (Array.isArray(data)) return data;
    if (Array.isArray(data?.items)) return data.items;
    if (Array.isArray(data?.content)) return data.content; // Page<> gibi
    return [];
  }

  const pidNum = useMemo(() => {
    const n = Number(productId);
    return Number.isFinite(n) && n > 0 ? n : null;
  }, [productId]);

  const canCreate = useMemo(() => {
    if (!pidNum) return false;
    const hasSkuOrName = (form.sku?.trim() || form.name?.trim())?.length > 0;
    return hasSkuOrName; // min şart: sku veya name
  }, [pidNum, form.sku, form.name]);

  const canSaveEdit = useMemo(() => {
    if (!pidNum || !editingId) return false;
    const hasSkuOrName = (editForm.sku?.trim() || editForm.name?.trim())?.length > 0;
    return hasSkuOrName;
  }, [pidNum, editingId, editForm.sku, editForm.name]);

  const load = async () => {
    setErr("");
    setInfo("");
    if (!pidNum) {
      setVariants([]);
      return;
    }
    setLoading(true);
    try {
      const res = await getVariantsByProduct(pidNum);
      setVariants(normalizeListData(res.data));
    } catch (e) {
      setErr(e?.response?.data?.message ?? e?.message ?? "Listeleme hatası");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pidNum]);

  const onCreate = async () => {
    setErr("");
    setInfo("");
    if (!canCreate) {
      setErr("Ürün seç (Product ID) ve en az SKU veya Name gir.");
      return;
    }

    setSaving(true);
    try {
      await createVariant({
        productId: pidNum,
        sku: form.sku?.trim() || null,
        name: form.name?.trim() || null,
        price: toNumberOrNull(form.price),
        stock: toNumberOrNull(form.stock),
      });
      setForm({ sku: "", name: "", price: "", stock: "" });
      setInfo("Variant oluşturuldu ✅");
      await load();
    } catch (e) {
      setErr(e?.response?.data?.message ?? e?.message ?? "Oluşturma hatası");
    } finally {
      setSaving(false);
    }
  };

  const startEdit = (v) => {
    setErr("");
    setInfo("");
    setEditingId(v.id);
    setEditForm({
      sku: v.sku ?? "",
      name: v.name ?? "",
      price: v.price ?? "",
      stock: v.stock ?? "",
    });
  };

  const cancelEdit = () => {
    if (saving) return;
    setEditingId(null);
    setEditForm({ sku: "", name: "", price: "", stock: "" });
  };

  const onUpdate = async () => {
    setErr("");
    setInfo("");
    if (!canSaveEdit) {
      setErr("En az SKU veya Name gir.");
      return;
    }

    setSaving(true);
    try {
      await updateVariant(editingId, {
        productId: pidNum,
        sku: editForm.sku?.trim() || null,
        name: editForm.name?.trim() || null,
        price: toNumberOrNull(editForm.price),
        stock: toNumberOrNull(editForm.stock),
      });
      setInfo("Variant güncellendi ✅");
      cancelEdit();
      await load();
    } catch (e) {
      setErr(e?.response?.data?.message ?? e?.message ?? "Güncelleme hatası");
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async (id) => {
    const ok = window.confirm("Bu variant silinsin mi?");
    if (!ok) return;

    setErr("");
    setInfo("");
    setSaving(true);
    try {
      await deleteVariant(id);
      if (editingId === id) cancelEdit();
      setInfo("Variant silindi ✅");
      await load();
    } catch (e) {
      setErr(e?.response?.data?.message ?? e?.message ?? "Silme hatası");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div style={{ padding: 16, maxWidth: 900 }}>
      <h2>Variants</h2>

      <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
        <label>Product ID:</label>
        <input
          value={productId}
          onChange={(e) => setProductId(e.target.value)}
          placeholder="örn: 1"
          style={{ width: 140 }}
        />
        <button onClick={load} disabled={loading}>
          {loading ? "Yükleniyor..." : "Yenile"}
        </button>
        {selectedProductId !== undefined && selectedProductId !== null && (
          <span style={{ opacity: 0.7, fontSize: 12 }}>
            (App.jsx selectedProductId: {selectedProductId})
          </span>
        )}
      </div>

      <hr />

      <h3>Yeni Variant</h3>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 8 }}>
        <input
          placeholder="SKU"
          value={form.sku}
          onChange={(e) => setForm({ ...form, sku: e.target.value })}
        />
        <input
          placeholder="Name"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
        />
        <input
          placeholder="Price"
          type="number"
          value={form.price}
          onChange={(e) => setForm({ ...form, price: e.target.value })}
        />
        <input
          placeholder="Stock"
          type="number"
          value={form.stock}
          onChange={(e) => setForm({ ...form, stock: e.target.value })}
        />
      </div>

      <button
        onClick={onCreate}
        style={{ marginTop: 10 }}
        disabled={!canCreate || saving}
        title={!pidNum ? "Önce Product ID gir" : !canCreate ? "SKU veya Name gir" : ""}
      >
        {saving ? "İşleniyor..." : "Oluştur"}
      </button>

      {err && <div style={{ color: "red", marginTop: 10 }}>{err}</div>}
      {info && <div style={{ color: "green", marginTop: 10 }}>{info}</div>}

      <hr />

      <h3>Variant Listesi</h3>
      {!pidNum ? (
        <div>Önce Product ID gir.</div>
      ) : variants.length === 0 ? (
        <div>Bu ürüne ait variant yok.</div>
      ) : (
        <table
          border="1"
          cellPadding="8"
          style={{ borderCollapse: "collapse", width: "100%" }}
        >
          <thead>
            <tr>
              <th style={{ width: 80 }}>ID</th>
              <th>SKU</th>
              <th>Name</th>
              <th style={{ width: 120 }}>Price</th>
              <th style={{ width: 120 }}>Stock</th>
              <th style={{ width: 220 }}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {variants.map((v) => {
              const isEditing = editingId === v.id;

              return (
                <tr key={v.id ?? v.sku}>
                  <td>{v.id}</td>

                  <td>
                    {isEditing ? (
                      <input
                        value={editForm.sku}
                        onChange={(e) =>
                          setEditForm({ ...editForm, sku: e.target.value })
                        }
                      />
                    ) : (
                      v.sku
                    )}
                  </td>

                  <td>
                    {isEditing ? (
                      <input
                        value={editForm.name}
                        onChange={(e) =>
                          setEditForm({ ...editForm, name: e.target.value })
                        }
                      />
                    ) : (
                      v.name
                    )}
                  </td>

                  <td>
                    {isEditing ? (
                      <input
                        type="number"
                        value={editForm.price}
                        onChange={(e) =>
                          setEditForm({ ...editForm, price: e.target.value })
                        }
                      />
                    ) : (
                      v.price
                    )}
                  </td>

                  <td>
                    {isEditing ? (
                      <input
                        type="number"
                        value={editForm.stock}
                        onChange={(e) =>
                          setEditForm({ ...editForm, stock: e.target.value })
                        }
                      />
                    ) : (
                      v.stock
                    )}
                  </td>

                  <td>
                    {isEditing ? (
                      <>
                        <button
                          onClick={onUpdate}
                          style={{ marginRight: 8 }}
                          disabled={!canSaveEdit || saving}
                        >
                          {saving ? "Kaydediliyor..." : "Kaydet"}
                        </button>
                        <button onClick={cancelEdit} disabled={saving}>
                          İptal
                        </button>
                      </>
                    ) : (
                      <>
                        <button
                          onClick={() => startEdit(v)}
                          style={{ marginRight: 8 }}
                          disabled={saving}
                        >
                          Düzenle
                        </button>
                        <button
                          onClick={() => onDelete(v.id)}
                          disabled={saving}
                        >
                          Sil
                        </button>
                      </>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}

      <div style={{ marginTop: 10, opacity: 0.7, fontSize: 12 }}>
        API fonksiyonları: <b>getVariantsByProduct</b>, <b>createVariant</b>,{" "}
        <b>updateVariant</b>, <b>deleteVariant</b>
      </div>
    </div>
  );
}
