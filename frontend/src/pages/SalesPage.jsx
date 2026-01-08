import { useEffect, useMemo, useState } from "react";
import { createSale, getSaleById } from "../api/sales";

function tlToKurus(tlStr) {
  // "10.50" => 1050
  if (tlStr === "" || tlStr === null || tlStr === undefined) return 0;
  const n = Number(String(tlStr).replace(",", "."));
  if (!Number.isFinite(n)) return 0;
  return Math.round(n * 100);
}

function kurusToTl(kurus) {
  const n = Number(kurus ?? 0);
  return (n / 100).toFixed(2);
}

export default function SalesPage({ selectedProductId }) {
  const [customerName, setCustomerName] = useState("");
  const [currency, setCurrency] = useState("TRY");

  // Satır formu
  const [line, setLine] = useState({
    productId: "",
    variantId: "",
    quantity: "1",
    unitPriceTL: "0.00",
  });

  const [items, setItems] = useState([]);
  const [err, setErr] = useState("");
  const [info, setInfo] = useState("");
  const [saving, setSaving] = useState(false);

  // Created order preview
  const [createdOrder, setCreatedOrder] = useState(null);

  // İstersen App.jsx selectedProductId’den otomatik doldursun
  useEffect(() => {
    if (selectedProductId !== undefined && selectedProductId !== null) {
      setLine((p) => ({ ...p, productId: String(selectedProductId) }));
    }
  }, [selectedProductId]);

  const subtotalKurus = useMemo(() => {
    return items.reduce((sum, it) => sum + (it.lineTotalKurus ?? 0), 0);
  }, [items]);

  function addItem() {
    setErr("");
    setInfo("");

    const productIdNum = Number(line.productId);
    const variantIdNum = line.variantId ? Number(line.variantId) : null;
    const qty = Number(line.quantity);
    const unitPriceKurus = tlToKurus(line.unitPriceTL);

    if (!Number.isFinite(productIdNum) || productIdNum <= 0) {
      setErr("Geçerli productId gir.");
      return;
    }
    if (!Number.isFinite(qty) || qty <= 0) {
      setErr("Quantity pozitif olmalı.");
      return;
    }
    if (!Number.isFinite(unitPriceKurus) || unitPriceKurus < 0) {
      setErr("Unit price geçersiz.");
      return;
    }

    const lineTotalKurus = unitPriceKurus * qty;

    setItems((prev) => [
      ...prev,
      {
        productId: productIdNum,
        variantId: Number.isFinite(variantIdNum) ? variantIdNum : null,
        quantity: qty,
        unitPriceKurus,
        lineTotalKurus,
      },
    ]);

    // input reset (productId kalsın)
    setLine((p) => ({
      ...p,
      variantId: "",
      quantity: "1",
      unitPriceTL: "0.00",
    }));
  }

  function removeItem(index) {
    setItems((prev) => prev.filter((_, i) => i !== index));
  }

  async function submitSale() {
    setErr("");
    setInfo("");
    setCreatedOrder(null);

    if (items.length === 0) {
      setErr("En az 1 kalem eklemelisin.");
      return;
    }

    setSaving(true);
    try {
      const payload = {
        customerName: customerName?.trim() || null,
        currency: currency || "TRY",
        items: items.map((it) => ({
          productId: it.productId,
          variantId: it.variantId,
          quantity: it.quantity,
          unitPrice: it.unitPriceKurus, // backend kuruş bekliyor
        })),
      };

      const res = await createSale(payload);
      setCreatedOrder(res.data);
      setInfo(`Satış oluşturuldu ✅ OrderNo: ${res.data?.orderNo}`);

      // temizle
      setItems([]);
      setCustomerName("");
    } catch (e) {
      setErr(e?.response?.data?.message ?? e?.message ?? "Satış oluşturma hatası");
    } finally {
      setSaving(false);
    }
  }

  // Opsiyonel: ID ile satış getir (debug/admin)
  const [lookupId, setLookupId] = useState("");
  const [lookup, setLookup] = useState(null);

  async function onLookup() {
    setErr("");
    setInfo("");
    setLookup(null);

    const idNum = Number(lookupId);
    if (!Number.isFinite(idNum) || idNum <= 0) {
      setErr("Geçerli satış ID gir.");
      return;
    }

    try {
      const res = await getSaleById(idNum);
      setLookup(res.data);
      setInfo("Satış getirildi ✅");
    } catch (e) {
      setErr(e?.response?.data?.message ?? e?.message ?? "Satış getirme hatası");
    }
  }

  return (
    <div style={{ padding: 16, maxWidth: 1100 }}>
      <h2>Sales</h2>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10, maxWidth: 700 }}>
        <input
          placeholder="Customer name (opsiyonel)"
          value={customerName}
          onChange={(e) => setCustomerName(e.target.value)}
        />
        <select value={currency} onChange={(e) => setCurrency(e.target.value)}>
          <option value="TRY">TRY</option>
          <option value="USD">USD</option>
          <option value="EUR">EUR</option>
        </select>
      </div>

      <hr />

      <h3>Sale Item (Kalem Ekle)</h3>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr 1fr", gap: 8 }}>
        <input
          placeholder="Product ID"
          value={line.productId}
          onChange={(e) => setLine({ ...line, productId: e.target.value })}
        />
        <input
          placeholder="Variant ID (opsiyonel)"
          value={line.variantId}
          onChange={(e) => setLine({ ...line, variantId: e.target.value })}
        />
        <input
          placeholder="Qty"
          type="number"
          value={line.quantity}
          onChange={(e) => setLine({ ...line, quantity: e.target.value })}
        />
        <input
          placeholder="Unit price (TL)"
          value={line.unitPriceTL}
          onChange={(e) => setLine({ ...line, unitPriceTL: e.target.value })}
        />
      </div>

      <button onClick={addItem} style={{ marginTop: 10 }}>
        + Kalem Ekle
      </button>

      <hr />

      <h3>Items</h3>
      {items.length === 0 ? (
        <div>Henüz kalem eklenmedi.</div>
      ) : (
        <table border="1" cellPadding="8" style={{ borderCollapse: "collapse", width: "100%" }}>
          <thead>
            <tr>
              <th>#</th>
              <th>Product</th>
              <th>Variant</th>
              <th>Qty</th>
              <th>Unit (TL)</th>
              <th>Line Total (TL)</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {items.map((it, idx) => (
              <tr key={idx}>
                <td>{idx + 1}</td>
                <td>{it.productId}</td>
                <td>{it.variantId ?? "-"}</td>
                <td>{it.quantity}</td>
                <td>{kurusToTl(it.unitPriceKurus)}</td>
                <td>{kurusToTl(it.lineTotalKurus)}</td>
                <td>
                  <button onClick={() => removeItem(idx)}>Sil</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <div style={{ marginTop: 10, fontWeight: 700 }}>
        Subtotal: {kurusToTl(subtotalKurus)} {currency}
      </div>

      <button
        onClick={submitSale}
        disabled={saving || items.length === 0}
        style={{ marginTop: 10 }}
      >
        {saving ? "Kaydediliyor..." : "Satışı Tamamla"}
      </button>

      {err && <div style={{ color: "red", marginTop: 10 }}>{err}</div>}
      {info && <div style={{ color: "green", marginTop: 10 }}>{info}</div>}

      {createdOrder && (
        <div style={{ marginTop: 14, padding: 12, border: "1px solid #ddd", borderRadius: 10 }}>
          <div><b>Created:</b> {createdOrder.orderNo} (id: {createdOrder.id})</div>
          <div>Total: {kurusToTl(createdOrder.grandTotal)} {createdOrder.currency}</div>
        </div>
      )}

      <hr />

      <h3>Lookup Sale (opsiyonel)</h3>
      <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
        <input
          placeholder="Sale ID"
          value={lookupId}
          onChange={(e) => setLookupId(e.target.value)}
          style={{ width: 160 }}
        />
        <button onClick={onLookup}>Getir</button>
      </div>

      {lookup && (
        <pre style={{ marginTop: 10, background: "#f7f7f7", padding: 10, borderRadius: 8 }}>
{JSON.stringify(lookup, null, 2)}
        </pre>
      )}
    </div>
  );
}
