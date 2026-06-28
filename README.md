# 🛒 Toko Berkah Jaya

Sistem Informasi Penjualan (Point of Sale/POS) berbasis **Java Desktop** yang dikembangkan menggunakan **Java Swing**, **NetBeans IDE 8.2**, dan **MySQL** sebagai basis data. Aplikasi ini dirancang untuk membantu proses pengelolaan data barang, customer, kategori, transaksi penjualan, serta penyajian laporan secara terintegrasi.

---

## 📌 Fitur Utama

- 🔐 Login User
- 📊 Dashboard
- 📦 Manajemen Data Barang
- 👥 Manajemen Data Customer
- 🗂️ Manajemen Data Kategori
- 🛍️ Transaksi Penjualan
- 📑 Laporan Penjualan
- 💾 Koneksi Database MySQL
- 🎨 Modern User Interface menggunakan FlatLaf

---

## 🛠️ Teknologi yang Digunakan

- Java SE (JDK 8)
- Java Swing
- NetBeans IDE 8.2
- MySQL Database
- JDBC (MySQL Connector)
- XAMPP
- FlatLaf

---

## 📂 Struktur Project

```
TokoBerkahJaya
│
├── src
│   ├── config
│   ├── dao
│   ├── model
│   ├── view
│   └── main
│
├── database
│   └── db_toko_berkah.sql
│
├── screenshot
│   ├── login.png
│   ├── dashboard.png
│   ├── barang.png
│   ├── customer.png
│   ├── kategori.png
│   ├── transaksi.png
│   └── laporan.png
│
├── README.md
└── .gitignore
```

---

## 🗄️ Database

Nama Database

```sql
db_toko_berkah
```

Import file SQL melalui phpMyAdmin sebelum menjalankan aplikasi.

---

## 🚀 Cara Menjalankan Project

### 1. Clone Repository

```bash
git clone https://github.com/USERNAME/toko-berkah-jaya.git
```

### 2. Import Database

Buka **phpMyAdmin** kemudian buat database

```sql
db_toko_berkah
```

Import file

```
database/db_toko_berkah.sql
```

### 3. Buka Project

- Jalankan NetBeans IDE 8.2
- Open Project
- Pilih folder **TokoBerkahJaya**

### 4. Tambahkan Library

Pastikan project telah menambahkan:

- mysql-connector-java
- FlatLaf

### 5. Jalankan Project

Run Project (**F6**)

---

## 📸 Tampilan Aplikasi

### Login

> Tambahkan screenshot login pada folder **screenshot/login.png**

### Dashboard

> Tambahkan screenshot dashboard pada folder **screenshot/dashboard.png**

### Data Barang

> Tambahkan screenshot barang pada folder **screenshot/barang.png**

### Data Customer

> Tambahkan screenshot customer pada folder **screenshot/customer.png**

### Data Kategori

> Tambahkan screenshot kategori pada folder **screenshot/kategori.png**

### Transaksi

> Tambahkan screenshot transaksi pada folder **screenshot/transaksi.png**

### Laporan

> Tambahkan screenshot laporan pada folder **screenshot/laporan.png**

---

## 📋 Modul Sistem

- Login
- Dashboard
- Barang
- Customer
- Kategori
- Transaksi
- Laporan

---

## 👨‍💻 Pengembang

**Muhammad Afrizal Yusni**

Program Studi Teknik Informatika

Universitas Pamulang

---

## 📄 Lisensi

Project ini dibuat untuk keperluan pembelajaran, tugas akademik, dan pengembangan aplikasi desktop menggunakan Java.

© 2026 Muhammad Afrizal Yusni
