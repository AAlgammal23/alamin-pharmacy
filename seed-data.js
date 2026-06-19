// Firebase seed data — الصق هذا في Firebase Console → Firestore → Rules playground
// أو شغّله من خلال Admin SDK
//
// ملاحظة: هذه البيانات الأولية. لوحة التحكم تسمح بإضافة وتعديل وحذف.

const subcategories = [
  // الأدوية الذاتية
  { id: "otc-eye-ear", mainCategoryId: "otc", nameAr: "أدوية العين والأذن", iconName: "MedicalServices", order: 1, active: true },
  { id: "otc-cold", mainCategoryId: "otc", nameAr: "أدوية الزكام والسعال", iconName: "MedicalServices", order: 2, active: true },
  { id: "otc-skin", mainCategoryId: "otc", nameAr: "أدوية الجلدية", iconName: "Face", order: 3, active: true },
  { id: "otc-stomach", mainCategoryId: "otc", nameAr: "أدوية المعدة", iconName: "MedicalServices", order: 4, active: true },
  { id: "otc-sexual", mainCategoryId: "otc", nameAr: "أدوية الصحة الجنسية", iconName: "MedicalServices", order: 5, active: true },
  { id: "otc-baby", mainCategoryId: "otc", nameAr: "أدوية الأطفال والمواليد", iconName: "ChildCare", order: 6, active: true },
  { id: "otc-painkiller", mainCategoryId: "otc", nameAr: "مسكنات الألم", iconName: "MedicalServices", order: 7, active: true },

  // الأدوية الوصفية
  { id: "rx-hormones", mainCategoryId: "rx", nameAr: "أدوية الهرمونات", iconName: "MedicalServices", order: 1, active: true },
  { id: "rx-bp", mainCategoryId: "rx", nameAr: "ضغط الدم", iconName: "MedicalServices", order: 2, active: true },
  { id: "rx-antibiotics", mainCategoryId: "rx", nameAr: "المضادات الحيوية", iconName: "MedicalServices", order: 3, active: true },
  { id: "rx-inflammatory", mainCategoryId: "rx", nameAr: "الالتهابات", iconName: "MedicalServices", order: 4, active: true },
  { id: "rx-diabetes", mainCategoryId: "rx", nameAr: "السكري", iconName: "MedicalServices", order: 5, active: true },
  { id: "rx-cholesterol", mainCategoryId: "rx", nameAr: "الكوليسترول", iconName: "MedicalServices", order: 6, active: true },
  { id: "rx-depression", mainCategoryId: "rx", nameAr: "الاكتئاب", iconName: "MedicalServices", order: 7, active: true },

  // الفيتامينات
  { id: "vit-general", mainCategoryId: "vitamins", nameAr: "صحة عامة", iconName: "LocalDining", order: 1, active: true },
  { id: "vit-sleep", mainCategoryId: "vitamins", nameAr: "نوم", iconName: "LocalDining", order: 2, active: true },
  { id: "vit-minerals", mainCategoryId: "vitamins", nameAr: "معادن", iconName: "LocalDining", order: 3, active: true },
  { id: "vit-slimming", mainCategoryId: "vitamins", nameAr: "تنحيف", iconName: "LocalDining", order: 4, active: true },
  { id: "vit-skin", mainCategoryId: "vitamins", nameAr: "بشرة", iconName: "Face", order: 5, active: true },
  { id: "vit-immunity", mainCategoryId: "vitamins", nameAr: "مناعة", iconName: "LocalDining", order: 6, active: true },
  { id: "vit-sports", mainCategoryId: "vitamins", nameAr: "رياضيين", iconName: "LocalDining", order: 7, active: true },
  { id: "vit-kids", mainCategoryId: "vitamins", nameAr: "صحة الطفل", iconName: "ChildCare", order: 8, active: true },
  { id: "vit-women", mainCategoryId: "vitamins", nameAr: "صحة المرأة", iconName: "Face", order: 9, active: true }
];

// ملاحظة: عند اللصق في Firebase Console أزل علامة = من السطر الأخير
// "صحة المرأة" — شوف Lint: تم إصلاحها أدناه
