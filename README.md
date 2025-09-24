## Giới thiệu

Ứng dụng web Java dựa trên Spring MVC (JSP/JSTL) đóng gói dưới dạng WAR. Dự án hỗ trợ build bằng Ant/PowerShell, chạy cục bộ trên Tomcat/GlassFish, và triển khai qua Docker hoặc Docker Compose. Repo này đã kèm đầy đủ `Dockerfile`, kịch bản build, và cấu hình mẫu JDBC.

## Công nghệ chính

- **Ngôn ngữ/Runtime**: Java 8 (JRE/JDK 1.8)
- **Framework**: Spring 4.3.x (Core, MVC, JDBC, Security)
- **View**: JSP + JSTL
- **App server**: Tomcat 9 / GlassFish (tùy chọn)
- **Build**: Apache Ant (`build.xml`) + PowerShell scripts
- **Đóng gói**: WAR (`dist/nestmartappFinal.war`)
- **CSDL**: SQL Server (driver `mssql-jdbc-12.2.0.jre8.jar`)
- **Container**: Docker, Docker Compose

## Yêu cầu hệ thống

- JDK 8
- Apache Ant 1.10+
- Git
- (Tùy chọn) Docker 20+ và Docker Compose
- (Tùy chọn) Tomcat 9 hoặc GlassFish để chạy cục bộ ngoài Docker

## Cấu trúc thư mục

```text
c:\Projects\nestmart
├─ src/                 # Mã nguồn Java, cấu hình MANIFEST
├─ web/                 # Tài nguyên web tĩnh và JSP
├─ lib/                 # Thư viện .jar (Spring, JDBC, JSTL, ...)
├─ build/               # Thư mục build trung gian (Ant)
├─ dist/                # WAR đầu ra (ví dụ: nestmartappFinal.war)
├─ nbproject/           # Cấu hình NetBeans/Ant
├─ Dockerfile*          # Nhiều biến thể Dockerfile
├─ docker-compose.yml   # Chạy ứng dụng qua Compose
├─ build.xml            # Kịch bản Ant
├─ *.ps1                # Script PowerShell build/deploy
└─ web/WEB-INF/         # `web.xml`, `dispatcher-servlet.xml`, cấu hình Spring
```

## Cấu hình

- `web/WEB-INF/jdbc.properties` hoặc `web/WEB-INF/application.properties`
  - Chỉnh thông số kết nối CSDL (URL, user, password, pool...).
- `web/WEB-INF/applicationContext.xml`, `dispatcher-servlet.xml`
  - Khai báo bean, datasource, view resolver, component scan, v.v.
- Không commit thông tin nhạy cảm. Dùng biến môi trường/secret trong CI/CD nếu cần.
## Build (Ant)

Trên Windows PowerShell (đã cài Ant và JDK 8):

```powershell
# Từ thư mục dự án
ant clean war | cat

# WAR sẽ được tạo trong dist/ (ví dụ: dist/nestmartappFinal.war)
```

Script hỗ trợ:

```powershell
# Build nhanh
./manual-build.ps1

# Build và (tùy) deploy theo cấu hình script
./build-and-deploy.ps1
```

## Chạy cục bộ với Tomcat

1. Cài Tomcat 9.
2. Sao chép WAR từ `dist/*.war` vào `TOMCAT_HOME/webapps/`.
3. Khởi động Tomcat và truy cập: `http://localhost:8080/<context-path>`.

Lưu ý: context-path thường là tên WAR (không đuôi `.war`). Kiểm tra log Tomcat nếu cần.

## Chạy trong NetBeans với GlassFish

1. Mở dự án trong NetBeans (8.x/12.x).
2. Thêm GlassFish vào NetBeans:
   - Tools → Servers → Add Server → GlassFish Server → Next
   - Chọn thư mục cài `GLASSFISH_HOME`, domain `domain1`
   - Java Platform: chọn JDK 8
3. Đặt GlassFish làm server chạy cho project:
   - Chuột phải project → Properties → Run
   - Server: chọn GlassFish Server
   - Context Path: đặt `<context-path>` mong muốn (ví dụ `/app`)
4. Cấu hình JDBC (nếu dùng SQL Server):
   - Sao chép `lib/mssql-jdbc-12.2.0.jre8.jar` vào `GLASSFISH_HOME\glassfish\domains\domain1\lib`
   - Khởi động GlassFish, mở Admin Console `http://localhost:4848`
   - Tạo JDBC Connection Pool và JDBC Resource (nếu app dùng JNDI)
   - Hoặc đảm bảo `web/WEB-INF/jdbc.properties` có thông tin kết nối đúng
5. Chạy ứng dụng:
   - Chuột phải project → Run (hoặc F6)
   - NetBeans sẽ build WAR và deploy lên GlassFish
   - Truy cập: `http://localhost:8080/<context-path>`

## Chạy bằng Docker

Có sẵn nhiều `Dockerfile` (Tomcat, GlassFish, optimized...). Ví dụ với Tomcat:

```powershell
# Build image (ví dụ dùng Dockerfile.tomcat)
docker build -t nestmart:tomcat -f Dockerfile.tomcat .

# Chạy container
docker run --rm -p 8080:8080 --name nestmart-web nestmart:tomcat
```

Hoặc dùng Docker Compose:

```powershell
docker compose up -d
```

Kiểm tra `docker-compose.yml` để tùy chỉnh cổng, biến môi trường, volume.

<!-- Mục Triển khai được lược bỏ vì không còn dùng các nền tảng cụ thể. -->

## Tác vụ thường dùng

- Làm sạch và build WAR: `ant clean war`
- Build Docker image: `docker build -t app -f Dockerfile.tomcat .`
- Chạy bằng Compose: `docker compose up -d`
- Dọn dẹp build: `./cleanup.ps1`

## Gỡ lỗi

- Lỗi kết nối DB: kiểm tra `jdbc.properties` và driver JDBC trong `lib/`.
- 404/500 khi truy cập: xem log server (Tomcat/GlassFish) và cấu hình `dispatcher-servlet.xml`.
- Lỗi build/Ant: đảm bảo JDK 8 và Ant đã có trong PATH.
- Lỗi Docker: kiểm tra biến môi trường truyền vào image/container, cổng trùng lặp, quyền truy cập file.

## Bản quyền

Mã nguồn thuộc về tác giả dự án. Vui lòng xem giấy phép (nếu có) hoặc liên hệ chủ sở hữu trước khi sử dụng thương mại.


