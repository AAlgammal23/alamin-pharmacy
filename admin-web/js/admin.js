// ============ FIREBASE CONFIG ============
// قيم مشروعك من Firebase Console
const firebaseConfig = {
  apiKey: "AIzaSyAhl8TvXaE2sm8VkH2TTljVdsqmdcC2Afs",
  authDomain: "alamin-pharma.firebaseapp.com",
  projectId: "alamin-pharma",
  storageBucket: "alamin-pharma.firebasestorage.app",
  messagingSenderId: "773527249793",
  appId: "1:773527249793:web:87e12e986e7cf72cb73e80"
};

import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
import {
  getAuth, onAuthStateChanged, signInWithEmailAndPassword, signOut
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-auth.js";
import {
  getFirestore, collection, doc, getDocs, getDoc, setDoc, addDoc, deleteDoc,
  onSnapshot, query, orderBy, updateDoc
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-firestore.js";

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);

// ============ AUTH ============
onAuthStateChanged(auth, (user) => {
  if (user) {
    document.getElementById("login-screen").style.display = "none";
    document.getElementById("user-info").textContent = user.email;
    document.getElementById("logout-btn").style.display = "inline-block";
    document.querySelectorAll(".page").forEach(p => p.style.display = "none");
    document.getElementById("dashboard").style.display = "block";
    showPage("dashboard");
    loadAllStats();
  } else {
    document.getElementById("login-screen").style.display = "block";
    document.querySelectorAll(".page").forEach(p => p.style.display = "none");
  }
});

document.getElementById("login-btn").onclick = async () => {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  try { await signInWithEmailAndPassword(auth, email, password); }
  catch (e) { document.getElementById("login-error").textContent = "خطأ: " + e.message; }
};

document.getElementById("logout-btn").onclick = () => signOut(auth);

document.querySelectorAll("[data-page]").forEach(el => {
  el.onclick = (e) => {
    e.preventDefault();
    document.querySelectorAll(".sidebar .nav-link").forEach(l => l.classList.remove("active"));
    el.classList.add("active");
    showPage(el.dataset.page);
  };
});

function showPage(page) {
  document.querySelectorAll(".page").forEach(p => p.style.display = "none");
  const el = document.getElementById("page-" + page);
  if (el) el.style.display = "block";
  else if (page === "dashboard") document.getElementById("dashboard").style.display = "block";
  if (page === "subcategories") SubCategory.load();
  if (page === "products") Product.load();
  if (page === "banners") Banner.load();
  if (page === "articles") Article.load();
  if (page === "orders") Order.load();
  if (page === "users") UserAdmin.load();
  if (page === "contact") Contact.load();
}

async function loadAllStats() {
  try {
    const [subs, prods, articles, orders] = await Promise.all([
      getDocs(collection(db, "subcategories")),
      getDocs(collection(db, "products")),
      getDocs(collection(db, "articles")),
      getDocs(collection(db, "orders"))
    ]);
    document.getElementById("stat-subs").textContent = subs.size;
    document.getElementById("stat-products").textContent = prods.size;
    document.getElementById("stat-articles").textContent = articles.size;
    document.getElementById("stat-orders").textContent = orders.size;
  } catch (e) { console.log(e); }
}

// ============ SUBCATEGORIES ============
const SubCategory = {
  async load() {
    const tb = document.querySelector("#subs-table tbody");
    tb.innerHTML = "<tr><td colspan='5'>جاري التحميل...</td></tr>";
    onSnapshot(query(collection(db, "subcategories"), orderBy("order", "asc")), (snap) => {
      tb.innerHTML = "";
      snap.forEach(d => {
        const c = d.data();
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${c.order || 0}</td>
          <td>${c.nameAr || ""}</td>
          <td>${c.mainCategoryId || ""}</td>
          <td>${c.active ? "✅" : "❌"}</td>
          <td>
            <button class="btn btn-sm btn-warning" onclick='SubCategory.edit("${d.id}", ${JSON.stringify(c).replace(/'/g, "\\'")})'>تعديل</button>
            <button class="btn btn-sm btn-danger" onclick="SubCategory.delete('${d.id}')">حذف</button>
          </td>
        `;
        tb.appendChild(tr);
      });
    });
  },
  openForm() {
    document.getElementById("formModalTitle").textContent = "إضافة صنف فرعي";
    document.getElementById("formModalBody").innerHTML = `
      <form id="sub-form">
        <label class="form-label">الاسم</label>
        <input class="form-control" name="nameAr" required>
        <label class="form-label">التصنيف الرئيسي</label>
        <select class="form-control" name="mainCategoryId">
          <option value="otc">الأدوية الذاتية</option>
          <option value="rx">الأدوية الوصفية</option>
          <option value="vitamins">الفيتامينات والمكملات</option>
        </select>
        <label class="form-label">الأيقونة</label>
        <select class="form-control" name="iconName">
          <option value="MedicalServices">طبي</option>
          <option value="Face">تجميل</option>
          <option value="ChildCare">أطفال</option>
          <option value="Soap">عناية</option>
          <option value="LocalDining">غذاء</option>
        </select>
        <div class="row">
          <div class="col-md-6"><label class="form-label">الترتيب</label><input class="form-control" name="order" type="number" value="0"></div>
          <div class="col-md-6"><label class="form-label">الحالة</label><select class="form-control" name="active"><option value="true">نشط</option><option value="false">معطل</option></select></div>
        </div>
        <button type="submit" class="btn btn-primary mt-3">حفظ</button>
      </form>
    `;
    new bootstrap.Modal(document.getElementById("formModal")).show();
    document.getElementById("sub-form").onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.target));
      data.active = data.active === "true";
      data.order = parseInt(data.order) || 0;
      await addDoc(collection(db, "subcategories"), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  },
  edit(id, c) {
    this.openForm();
    document.getElementById("formModalTitle").textContent = "تعديل صنف";
    const f = document.getElementById("sub-form");
    f.nameAr.value = c.nameAr || "";
    f.mainCategoryId.value = c.mainCategoryId || "otc";
    f.iconName.value = c.iconName || "MedicalServices";
    f.order.value = c.order || 0;
    f.active.value = c.active ? "true" : "false";
    f.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.target));
      data.active = data.active === "true";
      data.order = parseInt(data.order) || 0;
      await setDoc(doc(db, "subcategories", id), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  },
  delete: (id) => confirm("حذف؟") && deleteDoc(doc(db, "subcategories", id))
};

// ============ PRODUCTS ============
const Product = {
  subsCache: [],
  async load() {
    this.subsCache = (await getDocs(collection(db, "subcategories"))).docs.map(d => ({id: d.id, ...d.data()}));
    const tb = document.querySelector("#products-table tbody");
    tb.innerHTML = "<tr><td colspan='6'>جاري التحميل...</td></tr>";
    onSnapshot(query(collection(db, "products"), orderBy("createdAt", "desc")), (snap) => {
      tb.innerHTML = "";
      snap.forEach(d => {
        const p = d.data();
        const sub = this.subsCache.find(s => s.id === p.subCategoryId);
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td><img src="${p.imageUrl || ''}" onerror="this.src='https://via.placeholder.com/50'"></td>
          <td>${p.name || ""}</td>
          <td>${p.priceYer || 0} ر.ي</td>
          <td>${sub ? sub.nameAr : "—"}</td>
          <td>${p.isBestseller ? "🔥" : ""}</td>
          <td>
            <button class="btn btn-sm btn-warning" onclick='Product.edit("${d.id}", ${JSON.stringify(p).replace(/'/g, "\\'")})'>تعديل</button>
            <button class="btn btn-sm btn-danger" onclick="Product.delete('${d.id}')">حذف</button>
          </td>
        `;
        tb.appendChild(tr);
      });
    });
  },
  openForm() {
    const opts = this.subsCache.map(s => `<option value="${s.id}" data-main="${s.mainCategoryId}">${s.nameAr} (${s.mainCategoryId})</option>`).join("");
    document.getElementById("formModalTitle").textContent = "إضافة منتج";
    document.getElementById("formModalBody").innerHTML = `
      <form id="product-form">
        <div class="row">
          <div class="col-md-8"><label class="form-label">الاسم</label><input class="form-control" name="name" required></div>
          <div class="col-md-4"><label class="form-label">المخزون</label><input class="form-control" name="stock" type="number" value="10"></div>
        </div>
        <label class="form-label">الوصف</label>
        <textarea class="form-control" name="description" rows="2"></textarea>
        <label class="form-label">الصنف الفرعي</label>
        <select class="form-control" name="subCategoryId" required>${opts}</select>
        <div class="row mt-2">
          <div class="col-md-4"><label class="form-label">السعر</label><input class="form-control" name="priceYer" type="number" required></div>
          <div class="col-md-4"><label class="form-label">السعر القديم</label><input class="form-control" name="oldPriceYer" type="number" value="0"></div>
          <div class="col-md-4"><label class="form-label">التقييم</label><input class="form-control" name="rating" type="number" step="0.1" value="4.5"></div>
        </div>
        <label class="form-label">رابط الصورة</label>
        <input class="form-control" name="imageUrl" placeholder="https://...">
        <div class="row mt-2">
          <div class="col-md-3"><label class="form-label">مميز</label><select class="form-control" name="isFeatured"><option value="false">لا</option><option value="true">نعم</option></select></div>
          <div class="col-md-3"><label class="form-label">جديد</label><select class="form-control" name="isNew"><option value="false">لا</option><option value="true">نعم</option></select></div>
          <div class="col-md-3"><label class="form-label">الأكثر مبيعاً</label><select class="form-control" name="isBestseller"><option value="false">لا</option><option value="true">نعم</option></select></div>
          <div class="col-md-3"><label class="form-label">مباع</label><input class="form-control" name="soldCount" type="number" value="0"></div>
        </div>
        <label class="form-label">الحالة</label>
        <select class="form-control" name="active"><option value="true">نشط</option><option value="false">معطل</option></select>
        <button type="submit" class="btn btn-primary mt-3">حفظ</button>
      </form>
    `;
    new bootstrap.Modal(document.getElementById("formModal")).show();
    document.getElementById("product-form").onsubmit = async (e) => {
      e.preventDefault();
      const data = parseProductForm(e.target);
      const sub = this.subsCache.find(s => s.id === data.subCategoryId);
      if (sub) data.mainCategoryId = sub.mainCategoryId;
      data.createdAt = Date.now();
      await addDoc(collection(db, "products"), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  },
  edit(id, p) {
    this.openForm();
    document.getElementById("formModalTitle").textContent = "تعديل منتج";
    const f = document.getElementById("product-form");
    f.name.value = p.name || "";
    f.description.value = p.description || "";
    f.subCategoryId.value = p.subCategoryId || "";
    f.priceYer.value = p.priceYer || 0;
    f.oldPriceYer.value = p.oldPriceYer || 0;
    f.stock.value = p.stock || 0;
    f.imageUrl.value = p.imageUrl || "";
    f.isFeatured.value = p.isFeatured ? "true" : "false";
    f.isNew.value = p.isNew ? "true" : "false";
    f.isBestseller.value = p.isBestseller ? "true" : "false";
    f.rating.value = p.rating || 0;
    f.soldCount.value = p.soldCount || 0;
    f.active.value = p.active ? "true" : "false";
    f.onsubmit = async (e) => {
      e.preventDefault();
      const data = parseProductForm(e.target);
      const sub = this.subsCache.find(s => s.id === data.subCategoryId);
      if (sub) data.mainCategoryId = sub.mainCategoryId;
      data.createdAt = p.createdAt || Date.now();
      await setDoc(doc(db, "products", id), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  },
  delete: (id) => confirm("حذف؟") && deleteDoc(doc(db, "products", id))
};

function parseProductForm(form) {
  const data = Object.fromEntries(new FormData(form));
  data.priceYer = parseInt(data.priceYer) || 0;
  data.oldPriceYer = parseInt(data.oldPriceYer) || 0;
  data.stock = parseInt(data.stock) || 0;
  data.rating = parseFloat(data.rating) || 0;
  data.soldCount = parseInt(data.soldCount) || 0;
  data.isFeatured = data.isFeatured === "true";
  data.isNew = data.isNew === "true";
  data.isBestseller = data.isBestseller === "true";
  data.active = data.active === "true";
  return data;
}

// ============ BANNERS ============
const Banner = {
  load() {
    const tb = document.querySelector("#banners-table tbody");
    tb.innerHTML = "<tr><td colspan='5'>جاري التحميل...</td></tr>";
    onSnapshot(query(collection(db, "banners"), orderBy("order", "asc")), (snap) => {
      tb.innerHTML = "";
      snap.forEach(d => {
        const b = d.data();
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${b.order || 0}</td><td>${b.titleAr || ""}</td><td>${b.subtitleAr || ""}</td>
          <td>${b.active ? "✅" : "❌"}</td>
          <td>
            <button class="btn btn-sm btn-warning" onclick='Banner.edit("${d.id}", ${JSON.stringify(b).replace(/'/g, "\\'")})'>تعديل</button>
            <button class="btn btn-sm btn-danger" onclick="Banner.delete('${d.id}')">حذف</button>
          </td>
        `;
        tb.appendChild(tr);
      });
    });
  },
  openForm() {
    document.getElementById("formModalTitle").textContent = "إضافة بانر";
    document.getElementById("formModalBody").innerHTML = `
      <form id="banner-form">
        <label class="form-label">العنوان</label><input class="form-control" name="titleAr" required>
        <label class="form-label">الوصف</label><textarea class="form-control" name="subtitleAr" rows="2"></textarea>
        <label class="form-label">رابط الصورة</label><input class="form-control" name="imageUrl">
        <label class="form-label">لون الخلفية</label><input class="form-control" name="bgColor" value="#1FBFB0">
        <div class="row"><div class="col-md-6"><label class="form-label">الترتيب</label><input class="form-control" name="order" type="number" value="0"></div>
        <div class="col-md-6"><label class="form-label">الحالة</label><select class="form-control" name="active"><option value="true">نشط</option><option value="false">معطل</option></select></div></div>
        <button type="submit" class="btn btn-primary mt-3">حفظ</button>
      </form>
    `;
    new bootstrap.Modal(document.getElementById("formModal")).show();
    document.getElementById("banner-form").onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.target));
      data.active = data.active === "true";
      data.order = parseInt(data.order) || 0;
      await addDoc(collection(db, "banners"), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  },
  edit(id, b) {
    this.openForm();
    document.getElementById("formModalTitle").textContent = "تعديل بانر";
    const f = document.getElementById("banner-form");
    f.titleAr.value = b.titleAr || ""; f.subtitleAr.value = b.subtitleAr || "";
    f.imageUrl.value = b.imageUrl || ""; f.bgColor.value = b.bgColor || "#1FBFB0";
    f.order.value = b.order || 0; f.active.value = b.active ? "true" : "false";
    f.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.target));
      data.active = data.active === "true";
      data.order = parseInt(data.order) || 0;
      await setDoc(doc(db, "banners", id), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  },
  delete: (id) => confirm("حذف؟") && deleteDoc(doc(db, "banners", id))
};

// ============ ARTICLES ============
const Article = {
  load() {
    const tb = document.querySelector("#articles-table tbody");
    tb.innerHTML = "<tr><td colspan='6'>جاري التحميل...</td></tr>";
    onSnapshot(query(collection(db, "articles"), orderBy("publishedAt", "desc")), (snap) => {
      tb.innerHTML = "";
      snap.forEach(d => {
        const a = d.data();
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td><img src="${a.imageUrl || ''}" onerror="this.src='https://via.placeholder.com/50'" style="width:50px;height:50px;object-fit:cover;border-radius:8px;"></td>
          <td>${a.title || ""}</td>
          <td>${a.category || ""}</td>
          <td>${new Date(a.publishedAt || 0).toLocaleDateString("ar")}</td>
          <td>${a.active ? "✅" : "❌"}</td>
          <td>
            <button class="btn btn-sm btn-warning" onclick='Article.edit("${d.id}", ${JSON.stringify(a).replace(/'/g, "\\'")})'>تعديل</button>
            <button class="btn btn-sm btn-danger" onclick="Article.delete('${d.id}')">حذف</button>
          </td>
        `;
        tb.appendChild(tr);
      });
    });
  },
  openForm() {
    document.getElementById("formModalTitle").textContent = "إضافة مقال";
    document.getElementById("formModalBody").innerHTML = `
      <form id="article-form">
        <label class="form-label">العنوان</label><input class="form-control" name="title" required>
        <label class="form-label">الملخص</label><textarea class="form-control" name="summary" rows="2"></textarea>
        <label class="form-label">المحتوى الكامل</label><textarea class="form-control" name="content" rows="6"></textarea>
        <label class="form-label">رابط الصورة</label><input class="form-control" name="imageUrl" placeholder="https://...">
        <label class="form-label">التصنيف</label>
        <select class="form-control" name="category">
          <option value="nutrition">تغذية</option>
          <option value="kids">صحة الطفل</option>
          <option value="diseases">أمراض شائعة</option>
          <option value="general">صحة عامة</option>
        </select>
        <label class="form-label">الكاتب</label><input class="form-control" name="author" value="صيدلية الأمين الحديثة">
        <label class="form-label">الحالة</label><select class="form-control" name="active"><option value="true">نشط</option><option value="false">معطل</option></select>
        <button type="submit" class="btn btn-primary mt-3">حفظ</button>
      </form>
    `;
    new bootstrap.Modal(document.getElementById("formModal")).show();
    document.getElementById("article-form").onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.target));
      data.active = data.active === "true";
      data.publishedAt = Date.now();
      await addDoc(collection(db, "articles"), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  },
  edit(id, a) {
    this.openForm();
    document.getElementById("formModalTitle").textContent = "تعديل مقال";
    const f = document.getElementById("article-form");
    f.title.value = a.title || ""; f.summary.value = a.summary || "";
    f.content.value = a.content || ""; f.imageUrl.value = a.imageUrl || "";
    f.category.value = a.category || "general";
    f.author.value = a.author || "صيدلية الأمين الحديثة";
    f.active.value = a.active ? "true" : "false";
    f.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.target));
      data.active = data.active === "true";
      data.publishedAt = a.publishedAt || Date.now();
      await setDoc(doc(db, "articles", id), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  },
  delete: (id) => confirm("حذف؟") && deleteDoc(doc(db, "articles", id))
};

// ============ ORDERS ============
const Order = {
  load() {
    const tb = document.querySelector("#orders-table tbody");
    tb.innerHTML = "<tr><td colspan='7'>جاري التحميل...</td></tr>";
    onSnapshot(query(collection(db, "orders"), orderBy("createdAt", "desc")), (snap) => {
      tb.innerHTML = "";
      snap.forEach(d => {
        const o = d.data();
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${(o.id || d.id).toString().slice(0,8)}</td>
          <td>${o.userName || ""}</td>
          <td>${o.userPhone || ""}</td>
          <td>${(o.itemsCount || 0)}</td>
          <td>${o.total || 0} ر.ي</td>
          <td><select class="form-select form-select-sm" onchange="Order.changeStatus('${d.id}', this.value)">
            <option value="PENDING" ${o.status==="PENDING"?"selected":""}>قيد المراجعة</option>
            <option value="CONFIRMED" ${o.status==="CONFIRMED"?"selected":""}>مؤكد</option>
            <option value="PREPARING" ${o.status==="PREPARING"?"selected":""}>قيد التجهيز</option>
            <option value="DELIVERED" ${o.status==="DELIVERED"?"selected":""}>تم التوصيل</option>
            <option value="CANCELLED" ${o.status==="CANCELLED"?"selected":""}>ملغي</option>
          </select></td>
          <td><button class="btn btn-sm btn-danger" onclick="Order.delete('${d.id}')">حذف</button></td>
        `;
        tb.appendChild(tr);
      });
    });
  },
  changeStatus: (id, status) => updateDoc(doc(db, "orders", id), { status }),
  delete: (id) => confirm("حذف الطلب؟") && deleteDoc(doc(db, "orders", id))
};

// ============ USERS ============
const UserAdmin = {
  load() {
    const tb = document.querySelector("#users-table tbody");
    tb.innerHTML = "<tr><td colspan='6'>جاري التحميل...</td></tr>";
    onSnapshot(collection(db, "users"), (snap) => {
      tb.innerHTML = "";
      snap.forEach(d => {
        const u = d.data();
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${u.name || ""}</td>
          <td>${u.email || ""}</td>
          <td>${u.phone || ""}</td>
          <td>${u.balance || 0}</td>
          <td>${u.points || 0}</td>
          <td><button class="btn btn-sm btn-warning" onclick='UserAdmin.edit("${d.id}", ${JSON.stringify(u).replace(/'/g, "\\'")})'>تعديل</button></td>
        `;
        tb.appendChild(tr);
      });
    });
  },
  edit(id, u) {
    document.getElementById("formModalTitle").textContent = "تعديل مستخدم";
    document.getElementById("formModalBody").innerHTML = `
      <form id="user-form">
        <label class="form-label">الاسم</label><input class="form-control" name="name" value="${u.name || ''}">
        <label class="form-label">الهاتف</label><input class="form-control" name="phone" value="${u.phone || ''}">
        <div class="row">
          <div class="col-md-6"><label class="form-label">الرصيد</label><input class="form-control" name="balance" type="number" value="${u.balance || 0}"></div>
          <div class="col-md-6"><label class="form-label">النقاط</label><input class="form-control" name="points" type="number" value="${u.points || 0}"></div>
        </div>
        <label class="form-label">أدمن؟</label>
        <select class="form-control" name="isAdmin">
          <option value="false" ${!u.isAdmin?"selected":""}>لا</option>
          <option value="true" ${u.isAdmin?"selected":""}>نعم</option>
        </select>
        <button type="submit" class="btn btn-primary mt-3">حفظ</button>
      </form>
    `;
    new bootstrap.Modal(document.getElementById("formModal")).show();
    document.getElementById("user-form").onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(e.target));
      data.balance = parseInt(data.balance) || 0;
      data.points = parseInt(data.points) || 0;
      data.isAdmin = data.isAdmin === "true";
      await updateDoc(doc(db, "users", id), data);
      bootstrap.Modal.getInstance(document.getElementById("formModal")).hide();
    };
  }
};

// ============ CONTACT ============
const Contact = {
  async load() {
    const snap = await getDoc(doc(db, "settings", "contact"));
    if (snap.exists()) {
      const c = snap.data();
      document.getElementById("c-whatsapp").value = c.whatsapp || "";
      document.getElementById("c-whatsappRx").value = c.whatsappRx || "";
      document.getElementById("c-phone").value = c.phone || "";
      document.getElementById("c-email").value = c.email || "";
      document.getElementById("c-address").value = c.address || "";
      document.getElementById("c-facebook").value = c.facebook || "";
      document.getElementById("c-workingHours").value = c.workingHours || "";
    }
  }
};

document.getElementById("contact-form").onsubmit = async (e) => {
  e.preventDefault();
  const data = {
    whatsapp: document.getElementById("c-whatsapp").value,
    whatsappRx: document.getElementById("c-whatsappRx").value,
    phone: document.getElementById("c-phone").value,
    email: document.getElementById("c-email").value,
    address: document.getElementById("c-address").value,
    facebook: document.getElementById("c-facebook").value,
    workingHours: document.getElementById("c-workingHours").value
  };
  await setDoc(doc(db, "settings", "contact"), data);
  alert("✅ تم الحفظ");
};
