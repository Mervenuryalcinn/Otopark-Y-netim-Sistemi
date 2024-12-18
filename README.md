# OTOPARK-YÖNETİM-SİSTEMİ

# Projeye Ekip Arkadşım
Melek Oral
Github linki:
https://github.com/1220505054
# Genel Bakış

Otopark Yönetim Sistemi, IntelliJ IDEA kullanılarak Java dilinde geliştirilmiş ve MySQL ile veritabanı yönetimi yapan bir uygulamadır. Sistem, yöneticiler ve kullanıcılar için otopark işleyişini yönetmekte, yedi farklı tasarım deseni uygulayarak güçlü bir yapı sunmaktadır. Uygulama, kullanıcı kaydı, giriş, araç yönetimi, park süre takibi ve ödeme işlemleri gibi özellikler sağlar.

# Özellikler

# Genel Özellikler

*Kullanıcı kaydı ve giriş.

*Yöneticiler ve normal kullanıcılar için ayrı arayüzler.

# Yönetici Özellikleri

*Tüm kayıtlı kullanıcılar ve araç bilgilerini görüntüleme.

*Araç kayıtları ekleme, güncelleme ve silme.

*Araç park sürelerini takip etme ve ödeme tutarlarını hesaplama.

*Nakit veya kredi kartı ile ödeme işlemleri.

# Kullanıcı Özellikleri

*Kendi araç bilgilerini görüntüleme.

*Boş park yerlerini kontrol etme.

*Yeni araç ekleme.

*Park sürelerini takip etme ve yöneticiye ödenmesi gereken tutarı görüntüleme.

# Kullanılan Tasarım Desenleri

Singleton: Veritabanı bağlantısı yönetimi için tek bir nesne oluşturur.

State: Park yerlerinin durumunu (dolu veya boş) yönetir.

Observer: Kullanıcılara güncellemelerle ilgili bildirim veya mesaj gönderir.

Strategy: Araçların parkta kaldıkları süreyi dinamik olarak hesaplar.

Factory: Araç ekleme işlemleri.

# Kullanılan Teknolojiler

Programlama Dili: Java

IDE: IntelliJ IDEA

Veritabanı: MySQL

UI Framework: 

