<!DOCTYPE html>
<html lang="ar" dir="rtl">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>لوحة تحكم صيدلية الأمين الحديثة</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.rtl.min.css">
<link rel="stylesheet" href="css/admin.css">
</head>
<body>

<nav class="navbar navbar-dark sticky-top flex-md-nowrap p-2 shadow" style="background:#1FBFB0">
  <a class="navbar-brand col-md-3 col-lg-2 me-0 px-3 fs-5" href="#">💊 صيدلية الأمين الحديثة</a>
  <div class="navbar-nav ms-auto">
    <div class="nav-item text-nowrap">
      <span id="user-info" class="text-white me-3"></span>
      <button class="btn btn-sm btn-outline-light" id="logout-btn" style="display:none">خروج</button>
    </div>
  </div>
</nav>

<div class="container-fluid">
  <div class="row">
    <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
      <div class="position-sticky pt-3">
        <ul class="nav flex-column" id="sidebar-nav">
          <li class="nav-item"><a class="nav-link active" data-page="dashboard">📊 الرئيسية</a></li>
          <li class="nav-item"><a class="nav-link" data-page="subcategories">📂 الأصناف الفرعية</a></li>
          <li class="nav-item"><a class="nav-link" data-page="products">💊 المنتجات</a></li>
          <li class="nav-item"><a class="nav-link" data-page="banners">🎯 البانرات</a></li>
          <li class="nav-item"><a class="nav-link" data-page="articles">📰 المقالات</a></li>
          <li class="nav-item"><a class="nav-link" data-page="orders">📦 الطلبات</a></li>
          <li class="nav-item"><a class="nav-link" data-page="users">👥 المستخدمون</a></li>
          <li class="nav-item"><a class="nav-link" data-page="contact">📞 معلومات التواصل</a></li>
        </ul>
      </div>
    </nav>

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
      <div id="login-screen" class="text-center py-5">
        <h2 class="mb-4">🔐 تسجيل الدخول</h2>
        <div class="card mx-auto" style="max-width:400px">
          <div class="card-body">
            <input type="email" id="email" class="form-control mb-3" placeholder="البريد الإلكتروني">
            <input type="password" id="password" class="form-control mb-3" placeholder="كلمة المرور">
            <button id="login-btn" class="btn btn-primary w-100">دخول</button>
            <div id="login-error" class="text-danger mt-2"></div>
            <hr>
            <small class="text-muted">أنشئ حساب من Firebase Console → Authentication</small>
          </div>
        </div>
      </div>

      <div id="dashboard" class="page" style="display:none">
        <h2 class="mt-3">📊 لوحة المعلومات</h2>
        <div class="row mt-4">
          <div class="col-md-3"><div class="card text-white bg-primary mb-3"><div class="card-body"><h5>الأصناف الفرعية</h5><h2 id="stat-subs">0</h2></div></div></div>
          <div class="col-md-3"><div class="card text-white bg-success mb-3"><div class="card-body"><h5>المنتجات</h5><h2 id="stat-products">0</h2></div></div></div>
          <div class="col-md-3"><div class="card text-white bg-warning mb-3"><div class="card-body"><h5>المقالات</h5><h2 id="stat-articles">0</h2></div></div></div>
          <div class="col-md-3"><div class="card text-white bg-info mb-3"><div class="card-body"><h5>الطلبات</h5><h2 id="stat-orders">0</h2></div></div></div>
        </div>
      </div>

      <!-- SubCategories Page -->
      <div id="page-subcategories" class="page" style="display:none">
        <div class="d-flex justify-content-between align-items-center mt-3">
          <h2>📂 الأصناف الفرعية</h2>
          <button class="btn btn-primary" onclick="SubCategory.openForm()">+ إضافة صنف</button>
        </div>
        <table class="table mt-3" id="subs-table">
          <thead><tr><th>الترتيب</th><th>الاسم</th><th>التصنيف الرئيسي</th><th>الحالة</th><th>إجراءات</th></tr></thead>
          <tbody></tbody>
        </table>
      </div>

      <!-- Products Page -->
      <div id="page-products" class="page" style="display:none">
        <div class="d-flex justify-content-between align-items-center mt-3">
          <h2>💊 المنتجات</h2>
          <button class="btn btn-primary" onclick="Product.openForm()">+ إضافة منتج</button>
        </div>
        <table class="table mt-3" id="products-table">
          <thead><tr><th>الصورة</th><th>الاسم</th><th>السعر</th><th>التصنيف</th><th>الأكثر مبيعاً</th><th>إجراءات</th></tr></thead>
          <tbody></tbody>
        </table>
      </div>

      <!-- Banners Page -->
      <div id="page-banners" class="page" style="display:none">
        <div class="d-flex justify-content-between align-items-center mt-3">
          <h2>🎯 البانرات الإعلانية</h2>
          <button class="btn btn-primary" onclick="Banner.openForm()">+ إضافة بانر</button>
        </div>
        <table class="table mt-3" id="banners-table">
          <thead><tr><th>الترتيب</th><th>العنوان</th><th>الوصف</th><th>الحالة</th><th>إجراءات</th></tr></thead>
          <tbody></tbody>
        </table>
      </div>

      <!-- Articles Page -->
      <div id="page-articles" class="page" style="display:none">
        <div class="d-flex justify-content-between align-items-center mt-3">
          <h2>📰 المقالات الصحية</h2>
          <button class="btn btn-primary" onclick="Article.openForm()">+ إضافة مقال</button>
        </div>
        <table class="table mt-3" id="articles-table">
          <thead><tr><th>الصورة</th><th>العنوان</th><th>التصنيف</th><th>التاريخ</th><th>الحالة</th><th>إجراءات</th></tr></thead>
          <tbody></tbody>
        </table>
      </div>

      <!-- Orders Page -->
      <div id="page-orders" class="page" style="display:none">
        <h2 class="mt-3">📦 الطلبات</h2>
        <table class="table mt-3" id="orders-table">
          <thead><tr><th>رقم الطلب</th><th>العميل</th><th>الهاتف</th><th>عدد المنتجات</th><th>الإجمالي</th><th>الحالة</th><th>إجراءات</th></tr></thead>
          <tbody></tbody>
        </table>
      </div>

      <!-- Users Page -->
      <div id="page-users" class="page" style="display:none">
        <h2 class="mt-3">👥 المستخدمون</h2>
        <table class="table mt-3" id="users-table">
          <thead><tr><th>الاسم</th><th>البريد</th><th>الهاتف</th><th>الرصيد</th><th>النقاط</th><th>إجراءات</th></tr></thead>
          <tbody></tbody>
        </table>
      </div>

      <!-- Contact Page -->
      <div id="page-contact" class="page" style="display:none">
        <h2 class="mt-3">📞 معلومات التواصل</h2>
        <form id="contact-form" class="mt-3" style="max-width:700px">
          <div class="mb-3"><label class="form-label">واتساب (عام)</label><input type="text" class="form-control" id="c-whatsapp"></div>
          <div class="mb-3"><label class="form-label">واتساب (الوصفات)</label><input type="text" class="form-control" id="c-whatsappRx"></div>
          <div class="mb-3"><label class="form-label">الهاتف</label><input type="text" class="form-control" id="c-phone"></div>
          <div class="mb-3"><label class="form-label">البريد الإلكتروني</label><input type="email" class="form-control" id="c-email"></div>
          <div class="mb-3"><label class="form-label">العنوان</label><input type="text" class="form-control" id="c-address"></div>
          <div class="mb-3"><label class="form-label">رابط الفيسبوك</label><input type="url" class="form-control" id="c-facebook"></div>
          <div class="mb-3"><label class="form-label">ساعات العمل</label><input type="text" class="form-control" id="c-workingHours"></div>
          <button type="submit" class="btn btn-primary">💾 حفظ</button>
        </form>
      </div>
    </main>
  </div>
</div>

<div class="modal fade" id="formModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="formModalTitle"></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body" id="formModalBody"></div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script type="module" src="js/admin.js"></script>
</body>
</html>
