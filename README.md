# Vuemov Backend API

Backend API cho ứng dụng xem phim Vuemov, được xây dựng bằng Spring Boot và MongoDB.

## Tính năng

- **Authentication**: Đăng ký, đăng nhập với JWT
- **User Profile**: Quản lý thông tin người dùng
- **Favorites**: Lưu danh sách phim yêu thích
- **Watch History**: Lưu lịch sử xem phim
- **Comments**: Bình luận và đánh giá phim

## Yêu cầu

- Java 17+
- Maven 3.6+
- MongoDB 5.0+

## Cài đặt

### 1. Cài đặt MongoDB

```bash
# macOS (Homebrew)
brew install mongodb-community
brew services start mongodb-community

# Ubuntu
sudo apt install mongodb
sudo systemctl start mongodb

# Windows - Tải từ https://www.mongodb.com/try/download/community
```

### 2. Chạy project

```bash
# Build
mvn clean install

# Chạy
mvn spring-boot:run

# Hoặc chạy JAR
java -jar target/vuemov-backend-1.0.0.jar
```

API sẽ chạy tại: http://localhost:8080

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Đăng ký tài khoản |
| POST | `/api/auth/login` | Đăng nhập |

### User Profile

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/user/profile` | Lấy thông tin profile |
| GET | `/api/user/favorites` | Lấy danh sách yêu thích |
| POST | `/api/user/favorites/{slug}` | Thêm phim vào yêu thích |
| DELETE | `/api/user/favorites/{slug}` | Xóa khỏi yêu thích |
| GET | `/api/user/favorites/{slug}/check` | Kiểm tra phim có trong yêu thích |
| GET | `/api/user/history` | Lấy lịch sử xem |
| POST | `/api/user/history/{slug}` | Thêm vào lịch sử xem |
| DELETE | `/api/user/history/{slug}` | Xóa khỏi lịch sử |
| DELETE | `/api/user/history` | Xóa toàn bộ lịch sử |

### Comments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/comments/{slug}` | Lấy bình luận theo phim |
| POST | `/api/comments` | Thêm bình luận |
| DELETE | `/api/comments/{id}` | Xóa bình luận |

## Ví dụ API Calls

### Đăng ký

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "username": "testuser",
    "password": "123456"
  }'
```

### Đăng nhập

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "123456"
  }'
```

### Thêm phim yêu thích

```bash
curl -X POST http://localhost:8080/api/user/favorites/tien-nghich \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Cấu hình

Chỉnh sửa file `src/main/resources/application.properties`:

```properties
server.port=8080
spring.data.mongodb.uri=mongodb://localhost:27017/vuemov
jwt.secret=YourSecretKeyHere
jwt.expiration=86400000
```

## Deploy lên Free Hosting

### Render.com (Free)
1. Tạo tài khoản Render.com
2. Kết nối GitHub repository
3. Tạo Web Service
4. Thêm environment variables
5. Render sẽ tự build và deploy

### Railway.app (Free $5/tháng)
1. Tạo tài khoản Railway.app
2. Tạo project mới → New Project → Empty Project
3. Thêm MongoDB plugin
4. Deploy từ GitHub

### Vercel (Serverless)
- Cần dùng thêm plugin `vercel-spring-boot`

## Cấu trúc Project

```
src/main/java/com/vuemov/
├── VuemovBackendApplication.java
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── CommentController.java
│   ├── HealthController.java
│   └── UserController.java
├── dto/
│   ├── ApiResponse.java
│   ├── AuthResponse.java
│   ├── CommentRequest.java
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   └── UserResponse.java
├── model/
│   ├── Comment.java
│   ├── User.java
│   └── WatchHistoryItem.java
├── repository/
│   ├── CommentRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthenticationFilter.java
│   └── JwtTokenProvider.java
└── service/
    ├── AuthService.java
    ├── CommentService.java
    └── UserService.java
```

## License

MIT
