    목표

-----

    개발 대상

----

    개발 조건

----

    개발 프로세스

-----

    디렉토리 구조

````
src
├── main
│   ├── java
│   │   └── org
│   │       └── example
│   │           └── bookstoreapp
│   │               ├── BookstoreAppApplication.java
│   │               ├── common
│   │               │   ├── SoftDelete.java
│   │               │   ├── config
│   │               │   │   ├── JpaAuditingConfig.java
│   │               │   │   └── QuerydslConfig.java
│   │               │   ├── entity
│   │               │   │   └── BaseEntity.java
│   │               │   ├── exception
│   │               │   │   ├── BusinessException.java
│   │               │   │   ├── ErrorCode.java
│   │               │   │   └── GlobalExceptionHandler.java
│   │               │   ├── response
│   │               │   │   ├── ApiResponse.java
│   │               │   │   └── PageResponse.java
│   │               │   └── security
│   │               │       ├── CustomAccessDeniedHandler.java
│   │               │       ├── JwtAuthenticationFilter.java
│   │               │       ├── JwtAuthenticationToken.java
│   │               │       ├── JwtUtil.java
│   │               │       └── SecurityConfig.java
│   │               └── domain
│   │                   ├── auth
│   │                   │   ├── config
│   │                   │   │   └── DataInitializer.java
│   │                   │   ├── controller
│   │                   │   │   └── AuthController.java
│   │                   │   ├── dto
│   │                   │   │   ├── AuthUser.java
│   │                   │   │   └── request
│   │                   │   │       ├── SigninRequest.java
│   │                   │   │       └── SignupRequest.java
│   │                   │   ├── exception
│   │                   │   │   └── AuthErrorCode.java
│   │                   │   └── service
│   │                   │       └── AuthService.java
│   │                   ├── book
│   │                   │   ├── controller
│   │                   │   │   └── BookController.java
│   │                   │   ├── dto
│   │                   │   │   ├── BookCreateRequest.java
│   │                   │   │   ├── BookResponse.java
│   │                   │   │   ├── BookSingleResponse.java
│   │                   │   │   └── BookUpdateRequest.java
│   │                   │   ├── entity
│   │                   │   │   ├── Book.java
│   │                   │   │   └── BookCategory.java
│   │                   │   ├── exception
│   │                   │   │   ├── BookErrorCode.java
│   │                   │   │   └── InvalidBookException.java
│   │                   │   ├── repository
│   │                   │   │   ├── BookRepository.java
│   │                   │   │   ├── BookRepositoryImpl.java
│   │                   │   │   ├── BookSpecs.java
│   │                   │   │   └── CustomBookRepository.java
│   │                   │   └── service
│   │                   │       └── BookService.java
│   │                   ├── bookcontributor
│   │                   │   ├── BookContributorRepository.java
│   │                   │   ├── dto
│   │                   │   │   ├── BookContributorRequest.java
│   │                   │   │   └── BookContributorResponse.java
│   │                   │   └── entity
│   │                   │       └── BookContributor.java
│   │                   ├── contributor
│   │                   │   ├── controller
│   │                   │   │   └── ContributorController.java
│   │                   │   ├── dto
│   │                   │   │   ├── ContributorCreateRequest.java
│   │                   │   │   ├── ContributorResponse.java
│   │                   │   │   └── SearchContributorResponse.java
│   │                   │   ├── entity
│   │                   │   │   ├── Contributor.java
│   │                   │   │   └── ContributorRole.java
│   │                   │   ├── repository
│   │                   │   │   └── ContributorRepository.java
│   │                   │   └── service
│   │                   │       └── ContributorService.java
│   │                   ├── library
│   │                   │   ├── controller
│   │                   │   │   └── LibraryController.java
│   │                   │   ├── dto
│   │                   │   │   ├── request
│   │                   │   │   │   └── AddBookRequest.java
│   │                   │   │   └── response
│   │                   │   │       ├── LibraryBookResponse.java
│   │                   │   │       └── LibraryBookSimpleResponse.java
│   │                   │   ├── entity
│   │                   │   │   ├── Library.java
│   │                   │   │   └── LibraryBook.java
│   │                   │   ├── exception
│   │                   │   │   └── LibraryErrorCode.java
│   │                   │   ├── repository
│   │                   │   │   ├── LibraryBookRepository.java
│   │                   │   │   └── LibraryRepository.java
│   │                   │   └── service
│   │                   │       └── LibraryService.java
│   │                   ├── review
│   │                   │   ├── controller
│   │                   │   │   └── ReviewController.java
│   │                   │   ├── dto
│   │                   │   │   ├── request
│   │                   │   │   │   └── ReviewRequest.java
│   │                   │   │   └── response
│   │                   │   │       └── ReviewResponse.java
│   │                   │   ├── entity
│   │                   │   │   └── Review.java
│   │                   │   ├── exception
│   │                   │   │   └── ReviewErrorCode.java
│   │                   │   ├── repository
│   │                   │   │   ├── CustomReviewRepository.java
│   │                   │   │   ├── ReviewRepository.java
│   │                   │   │   └── ReviewRepositoryImpl.java
│   │                   │   └── service
│   │                   │       └── ReviewService.java
│   │                   ├── searchHistory
│   │                   │   ├── controller
│   │                   │   │   ├── SearchHistoryController1.java
│   │                   │   │   └── SearchHistoryController2.java
│   │                   │   ├── dto
│   │                   │   │   └── response
│   │                   │   │       ├── MySearchHistoryResponse.java
│   │                   │   │       └── SearchResponse.java
│   │                   │   ├── entity
│   │                   │   │   └── SearchHistory.java
│   │                   │   ├── exception
│   │                   │   │   └── enums
│   │                   │   │       └── SearchErrorCode.java
│   │                   │   ├── repository
│   │                   │   │   └── SearchHistoryRepository.java
│   │                   │   └── service
│   │                   │       └── SearchHistoryService.java
│   │                   └── user
│   │                       ├── controller
│   │                       │   ├── UserAdminController.java
│   │                       │   └── UserController.java
│   │                       ├── dto
│   │                       │   ├── request
│   │                       │   │   ├── UserChangeNameAndPasswordRequest.java
│   │                       │   │   └── UserRoleChangeRequest.java
│   │                       │   └── response
│   │                       │       └── UserResponse.java
│   │                       ├── entity
│   │                       │   └── User.java
│   │                       ├── enums
│   │                       │   └── UserRole.java
│   │                       ├── repository
│   │                       │   └── UserRepository.java
│   │                       └── service
│   │                           ├── UserAdminService.java
│   │                           └── UserService.java
│   └── resources
│       └── application.yml
└── test
    ├── java
    │   └── org
    │       └── example
    │           └── bookstoreapp
    │               ├── BookstoreAppApplicationTests.java
    │               ├── config
    │               │   ├── TestSecurityConfig.java
    │               │   ├── WithMockAuthUser.java
    │               │   └── WithMockUserSecurityContextFactory.java
    │               ├── domain
    │               │   ├── auth
    │               │   │   └── AuthServiceTest.java
    │               │   ├── review
    │               │   │   └── ReviewServiceTest.java
    │               │   └── user
    │               │       ├── UserAdminServiceTest.java
    │               │       └── UserServiceTest.java
    │               └── security
    │                   ├── AuthIntegrationTest.java
    │                   └── JwtUtilTest.java
    └── resources
        └── application-test.yml
````        

    주요기능

---

    개발 환경

----

    API 명세서

### TOKEN

| Name           | Type     | Description      | Required |
|----------------|----------|------------------|----------|
| Authorization	 | String	  | JWT 토큰           | 필수       |
| Content-Type   | 	String	 | application/json | 필수       |

### AUTH

| Method | Endpoint         | Description | Parameters | Request Body                                                                                     | Response                                                                                           | Status Code   | Error Codes                                                        |
|--------|------------------|-------------|------------|--------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|---------------|--------------------------------------------------------------------|
| POST   | /api/auth/signup | 회원가입        | 없음         | {<br/>”email: String,<br/>”password” : String,<br/>”name” : String,<br/>”nickname”: String<br/>} | {<br/>”success” : boolean,<br/>”message” : String,<br/>”data” : T,<br/>”timestamp” : Instant<br/>} | 201 Created   | 400 BADREQUEST,<br/> 409 CONFLICT, <br/>500 INTERNALSERVERERROR    |
| POST   | /api/auth/login  | 로그인         | 없음         | {<br/>”email: String,<br/>”password”: String<br/>}                                               | {<br/>”success” : boolean,<br/>”message” : String,<br/>”data” : T,<br/>”timestamp” : Instant<br/>} | 200 OK        | 401 UNAUTHORIZED, <br/>500 INTERNALSERVERERROR                     |
| DELETE | /api/auth/me     | 회원탈퇴        | 없음         | 없음                                                                                               | {<br/>”success” : boolean,<br/>”message” : String,<br/>”data” : T,<br/>”timestamp” : Instant<br/>} | 204 NOCONTENT | 401 UNAUTHORIZED, <br/>403 FORBIDDEN, <br/>500 INTERNALSERVERERROR |

### USER

| Method | Endpoint                           | Description | Parameters                            | Request Body                                                                              | Response                                                                                                                                                                                                 | Status Code | Error Codes                                                         |
|--------|------------------------------------|-------------|---------------------------------------|-------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|---------------------------------------------------------------------|
| GET    | /api/users/me                      | 내 정보 조회     | 없음                                    | 없음                                                                                        | {<br/>”success” : boolean,<br/>”message” : String, <br/>”data” : { <br/>“name” : String, <br/>“nickname” : String, <br/>“userRole” : UserRole , <br/>“email” : String }<br/>”timestamp” : Instant <br/>} | 200 OK      | 401 UNAUTHORIZED,<br/> 403 FORBIDDEN, <br/>500 INTERNALSERVERERROR  |
| PATCH  | /api/users/me                      | 내 정보 수정     | 없음                                    | {<br/>”nickname” : String,<br/>”oldPassword” : String, <br/>”newPassword” : String <br/>} | {<br/>”success” : boolean,<br/>”message” : String, <br/>”data” : { <br/>“name” : String, <br/>“nickname” : String, <br/>“userRole” : UserRole , <br/>“email” : String <br/>”timestamp” : Instant <br/>}  | 200 OK      | 400 BADREQUEST,<br/> 401 UNAUTHORIZED, <br/>500 INTERNALSERVERERROR |
| PATCH  | /api/admin/users/{userId}/userRole | 유저 권한 수정    | userId (PathVariable, Long, required) | {<br/>”userRole” : UserRole<br/> }                                                        | {<br/>”success” : boolean,<br/>”message” : String, <br/>”data” : { <br/>“name” : String, <br/>“nickname” : String, <br/>“userRole” : UserRole , <br/>“email” : String <br/>”timestamp” : Instant <br/>}  | 200 OK      | 400 BADREQUEST,<br/> 403 FORBIDDEN , <br/>500 INTERNALSERVERERROR   |

### BOOK

| Method | Endpoint                                                                                                      | Description    | Parameters                                                                                                                                                                                                                                                                                                                                | Request Body                                                                                                                                                         | Response                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Status Code   | Error Codes                                                                                                                    |
|--------|---------------------------------------------------------------------------------------------------------------|----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|--------------------------------------------------------------------------------------------------------------------------------|
| POST   | /api/admin/books                                                                                              | 도서 정보 등록       | 없음                                                                                                                                                                                                                                                                                                                                        | {<br/>”category “ : String, <br/>”isbn” : String, <br/>”title” : String, <br/>”authorId” : Long, <br/>”publisher” : String, <br/>”publisherDate” :  LocalDate<br/> } | {<br/>”success” : boolean,<br/>”message” : String, <br/>”data” :{<br/>"id": Long,<br/>"publisher": String,<br/>"isbn": String,<br/>"category": Category,<br/>"title": String,<br/>"publicationDate": Date,<br/>"createdBy": Long,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime"<br/>}, <br/>”timestamp” : Instant <br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | 201 CREATED   | 401 UNAUTHORIZED, <br/>403 FORBIDDEN, <br/>409 CONFLICT, <br/>500 INTERNALSERVERERROR                                          |
| GET    | /api/admin/books?title=String&isbn=String&category=String&publisher=String&page=0&size=0&sort=created_at,desc | 도서 목록 조회       | title (QueryParam, String, optional)<br/> isbn (QueryParam, String, optional)<br/> category (QueryParam, String, optional)<br/> publisher (QueryParam, String, optional)<br/> page (QueryParam, int, optional, default=0)<br/> size (QueryParam, int, optional, default=20)<br/> sort (QueryParam, String, optional, e.g. createdAt,desc) | 없음                                                                                                                                                                   | {<br/>"success": boolean,<br/>"message": String,<br/>"data": {<br/>"content": [<br/>{<br/>"id": Long,<br/>"publisher": String,<br/>"isbn": String,<br/>"category": String,<br/>"title": String,<br/>"publicationDate": Date,<br/>"createdBy": Long,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime<br/>},<br/>],<br/>"pageable": {<br/>"pageNumber": Long,<br/>"pageSize": Long,<br/>"sort": {<br/>"empty": boolean,<br/>"unsorted": boolean,<br/>"sorted": boolean<br/>},<br/>"offset": Long,<br/>"unpaged": boolean,<br/>"paged": boolean<br/>},<br/>"last": boolean,<br/>"totalPages": Long,<br/>"totalElements": Long,<br/>"size": Long,<br/>"number": Long,<br/>"sort": {<br/>"empty": boolean,<br/>"unsorted": boolean,<br/>"sorted": boolean<br/>},<br/>"first": boolean,<br/>"numberOfElements": Long,<br/>"empty": boolean<br/>},<br/>"timestamp": Instant<br/>} | 200 OK        | 400 BADREQUEST, <br/>401 UNAUTHORIZED, <br/>403 FORBIDDEN, <br/> 500 INTERNALSERVERERROR                                       |
| GET    | /api/admin/books/{bookId}                                                                                     | 도서 단건 조회       | bookId (PathVariable, Long, required)                                                                                                                                                                                                                                                                                                     | 없음                                                                                                                                                                   | {”success” : boolean,<br/>”message” : String,<br/>”data” :<br/>{<br/>"id": Long,<br/>"publisher": String,<br/>"isbn": String,<br/>"category": Category,<br/>"title": String,<br/>"publicationDate": Date,<br/>"createdBy": Long,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime"<br/>},<br/>”timestamp” : Instant<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | 200 OK        | 400 BADREQUEST, <br/>401 UNAUTHORIZED, <br/>403 FORBIDDEN,  <br/>404 NOTFOUND , <br/>500 INTERNALSERVERERROR                   |
| PUT    | /api/admin/books/{bookId}                                                                                     | 도서 정보 수정       | bookId (PathVariable, Long, required)                                                                                                                                                                                                                                                                                                     | {<br/>”category “ : String, <br/>”isbn” : String, <br/>”title” : String, <br/>”authorId” : Long,<br/> ”publisher” : String, <br/>”publisherDate” :  LocalDate<br/> } | {<br/>”success” : boolean,<br/>”message” : String, <br/>”data” :{<br/>"id": Long,<br/>"publisher": String,<br/>"isbn": String,<br/>"category": Category,<br/>"title": String,<br/>"publicationDate": Date,<br/>"createdBy": Long,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime"<br/>}, <br/>”timestamp” : Instant <br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | 200 OK        | 400 BADREQUEST, <br/>401 UNAUTHORIZED, <br/>403 FORBIDDEN, <br/>404 NOTFOUND,  <br/>409 CONFLICT, <br/>500 INTERNALSERVERERROR |
| DELETE | /api/admin/books/{bookId}                                                                                     | 도서 정보 삭제       | bookId (PathVariable, Long, required)                                                                                                                                                                                                                                                                                                     | 없음                                                                                                                                                                   | {<br/>"success": boolean,<br/>"message": String,<br/>"data": null,<br/>"timestamp": Instant<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | 204 NOCONTENT | 400 BADREQUEST,<br/> 401 UNAUTHORIZED, <br/>403 FORBIDDEN, <br/>404 NOTFOUND , <br/>500 INTERNALSERVERERROR                    |
| POST   | /api/admin/books/{bookId}/contributors/{contributorId}                                                        | 도서 기여자 연관관계 맺기 | bookId (PathVariable, Long, required), <br/> contributorId (PathVariable, Long, required)                                                                                                                                                                                                                                                 | {<br/>"role": "AUTHOR"<br/>}                                                                                                                                         | {<br/>"success": boolean,<br/>"message": String,<br/>"data": {<br/>"bookId": Long,<br/>"contributorId": Long,<br/>"role": Role<br/>},<br/>"timestamp": Instant<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | 200 OK        | 401 UNAUTHORIZED, <br/>403 FORBIDDEN,<br/> 404 NOTFOUND , <br/>500 INTERNALSERVERERROR                                         |

### CONTRIBUTOR

| Method | Endpoint                | Description | Parameters | Request Body                | Response                                                                                                                                     | Status Code | Error Codes                                    |
|--------|-------------------------|-------------|------------|-----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|-------------|------------------------------------------------|
| POST   | /api/admin/contributors | 기여자 등록      | 없음         | {<br/>”name” : String<br/>} | {<br/>”success” : boolean,<br/>”message” : String,<br/>”data” :{<br/>“id” : Long,<br/>“name” : String<br/>},<br/>”timestamp” : Instant<br/>} | 201 CREATED | 400 BADREQUEST, 401 UNAUTHORIZED, 409 CONFLICT |

### REVIEW

| Method | Endpoint                                                                | Description            | Parameters                                                                                                                                                         | Request Body                   | Response                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Status Code | Error Codes                                                                          |
|--------|-------------------------------------------------------------------------|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|--------------------------------------------------------------------------------------|
| POST   | /api/v1/books/{bookId}/reviews                                          | 리뷰 작성                  | bookId (PathVariable, Long, required)                                                                                                                              | {<br/>”content” : String<br/>} | {<br/>"success": boolean,<br/>"message": String,<br/>"data": {<br/>"id": Long,<br/>"content": String,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime<br/>},<br/>"timestamp": Instant<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | 201 CREATED | 401 UNAUTHORIZED,<br/> 404 NOTFOUND    , <br/>500 INTERNALSERVERERROR                |
| GET    | /api/v1/reviews?lastReviewId=12&lastModifiedAt=2025-10-01T09:10&size=10 | 리뷰 조회 (유저가 작성한 리뷰를 조회) | lastReviewId (QueryParam, Long, optional)<br/> lastModifiedAt (QueryParam, LocalDateTime, optional, ISO 8601 형식)<br/> size (QueryParam, int, optional, default=10) | 없음                             | {<br/>"success": boolean,<br/>"message": String,<br/>"data": {<br/>"content": [<br/>{<br/>"id": Long,<br/>"content": String,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime<br/>},<br/>{<br/>"id": Long,<br/>"content": String,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime<br/>},<br/>{<br/>"id": Long,<br/>"content": String,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime<br/>}<br/>],<br/>"pageable": {<br/>"pageNumber": 0,<br/>"pageSize": 10,<br/>"sort": {<br/>"empty": true,<br/>"unsorted": true,<br/>"sorted": false<br/>},<br/>"offset": 0,<br/>"unpaged": false,<br/>"paged": true<br/>},<br/>"first": true,<br/>"last": true,<br/>"size": 10,<br/>"number": 0,<br/>"sort": {<br/>"empty": true,<br/>"unsorted": true,<br/>"sorted": false<br/>},<br/>"numberOfElements": 3,<br/>"empty": false<br/>},<br/>"timestamp": Instant<br/>} | 200 OK      | 401 UNAUTHORIZED,<br/> 404 NOTFOUND  , <br/>500 INTERNALSERVERERROR                  |
| PUT    | /api/v1/reviews/{reviewId}                                              | 리뷰 수정                  | reviewId (PathVariable, Long, required)                                                                                                                            | {<br/>"content": String<br/>}  | {<br/>"success": boolean,<br/>"message": String,<br/>"data": {<br/>"id": Long,<br/>"content": String,<br/>"createdAt": LocalDateTime,<br/>"modifiedAt": LocalDateTime<br/>},<br/>"timestamp": Instant<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | 200 OK      | 401 UNAUTHORIZED,<br/> 404 NOTFOUND       , <br/>500 INTERNALSERVERERROR             |
| DELETE | /api/v1/reviews/{reviewId}                                              | 리뷰 삭제                  | reviewId (PathVariable, Long, required)                                                                                                                            | 없음                             | {<br/>"success": boolean,<br/>"message": String,<br/>"data": null,<br/>"timestamp": Instant<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | 200 OK      | 401 UNAUTHORIZED,<br/>403 FORBIDDEN,<br/> 404 NOTFOUND, <br/>500 INTERNALSERVERERROR |

### LIBRARY

| Method | Endpoint                         | Description | Parameters                                                                                                                                               | Request Body               | Response                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | Status Code | Error Codes                                                                          |
|--------|----------------------------------|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|--------------------------------------------------------------------------------------|
| POST   | /api/v1/libraries                | 내 서재에 책 추가  | 없음                                                                                                                                                       | {<br/>"bookId": Long<br/>} | {<br/>"success": true,<br/>"message": String,<br/>"data": {<br/>"bookId": Long,<br/>"title": String,<br/>"addedAt": LocalDateTime<br/>},<br/>"timestamp": LocalDateTime<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | 201 CREATED | 401 UNAUTHORIZED,<br/> 404 NOTFOUND, <br/>409 CONFLICT, <br/>500 INTERNALSERVERERROR |
| GET    | /api/v1/libraries?page=0         | 내 서재 조회     | page (QueryParam, int, optional, default=0)<br/> size (QueryParam, int, optional, default=8)<br/> sort (QueryParam, String, optional, e.g. addedAt,desc) | 없음                         | {<br/>"success": true,<br/>"message": String,<br/>"data": {<br/>"content": [<br/>{<br/>"bookId": Long,<br/>"title": String,<br/>"authors": [<br/>String<br/>],<br/>"addedAt": LocalDateTime<br/>}<br/>],<br/>"pageable": {<br/>"pageNumber": 0,<br/>"pageSize": 8,<br/>"sort": {<br/>"empty": false,<br/>"sorted": true,<br/>"unsorted": false<br/>},<br/>"offset": 0,<br/>"paged": true,<br/>"unpaged": false<br/>},<br/>"size": 8,<br/>"number": 0,<br/>"sort": {<br/>"empty": false,<br/>"sorted": true,<br/>"unsorted": false<br/>},<br/>"first": true,<br/>"last": true,<br/>"numberOfElements": 1,<br/>"empty": false<br/>},<br/>"timestamp": LocalDateTime<br/>} | 200 OK      | 401 UNAUTHORIZED   , <br/>500 INTERNALSERVERERROR                                    |
| DELETE | /api/v1/libraries/books/{bookId} | 내 서재에 책 삭제  | bookId (PathVariable, Long, required)                                                                                                                    | 없음                         | {<br/>"success": true,<br/>"message": String,<br/>"data": null,<br/>"timestamp": LocalDateTime<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | 200 OK      | 401 UNAUTHORIZED, <br/>404 NOTFOUND    , <br/>500 INTERNALSERVERERROR                |

### SEARCH

| Method | Endpoint                                  | Description             | Parameters                                                                                                 | Request Body | Response                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | Status Code | Error Codes                                                     |
|--------|-------------------------------------------|-------------------------|------------------------------------------------------------------------------------------------------------|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|-----------------------------------------------------------------|
| GET    | /api/v1/search?title=&name=&category=     | 키워드 검색                  | title (Query, String, optional)<br/> name (Query, String, optional) <br/> category (Query, Enum, optional) | 없음           | {<br/>"success": Boolean,<br/>"message": String,<br/>"data": {<br/>"content": [<br/>{<br/>"id": Long,<br/>"title": String,<br/>"contributors": [<br/>{<br/>"id": Long,<br/>"name": String,<br/>"role": Enum<br/>}<br/>],<br/>"category": Enum,<br/>"createdAt": LocalDateTime<br/>}<br/>],<br/>"pageable": {<br/>"pageNumber": Integer,<br/>"pageSize": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"offset": Long,<br/>"paged": Boolean,<br/>"unpaged": Boolean<br/>},<br/>"last": Boolean,<br/>"totalPages": Integer,<br/>"totalElements": Long,<br/>"first": Boolean,<br/>"size": Integer,<br/>"number": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"numberOfElements": Integer,<br/>"empty": Boolean<br/>},<br/>"timestamp": LocalDateTime<br/>} | 200 OK      | 400 BADREQUEST,<br/> 404 NOTFOUND, <br/>500 INTERNALSERVERERROR |
| GET    | /api/v1/search/histories                  | 내 검색 기록                 | 없음                                                                                                         | 없음           | {<br/>"success": Boolean,<br/>"message": String,<br/>"data": {<br/>"content": [<br/>{<br/>"id": Long,<br/>"title": String<br/>"name": String<br/>"category": Enum<br/>"createdAt": LocalDateTime<br/>}<br/>],<br/>"pageable": {<br/>"pageNumber": Integer,<br/>"pageSize": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"offset": Long,<br/>"paged": Boolean,<br/>"unpaged": Boolean<br/>},<br/>"last": Boolean,<br/>"totalPages": Integer,<br/>"totalElements": Long,<br/>"first": Boolean,<br/>"size": Integer,<br/>"number": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"numberOfElements": Integer,<br/>"empty": Boolean<br/>},<br/>"timestamp": LocalDateTime<br/>}                                                                              | 200 OK      | 401 UNAUTHORIZED , <br/>500 INTERNALSERVERERROR                 |
| GET    | /api/v1/search/popularKeywords/titles     | 인기 검색어 title별 조회        | 없음                                                                                                         | 없음           | {<br/>"success": Boolean,<br/>"message": String,<br/>"data": {<br/>"content": [<br/>{<br/>"keyword": String,<br/>"cnt": Long<br/>}<br/>],<br/>"pageable": {<br/>"pageNumber": Integer,<br/>"pageSize": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"offset": Long,<br/>"paged": Boolean,<br/>"unpaged": Boolean<br/>},<br/>"last": Boolean,<br/>"totalPages": Integer,<br/>"totalElements": Long,<br/>"first": Boolean,<br/>"size": Integer,<br/>"number": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"numberOfElements": Integer,<br/>"empty": Boolean<br/>},<br/>"timestamp": LocalDateTime<br/>}                                                                                                                                                  | 200 OK      | 없음                                                              |
| GET    | /api/v1/search/popularKeywords/names      | 인기 검색어 name별 조회         | 없음                                                                                                         | 없음           | {<br/>"success": Boolean,<br/>"message": String,<br/>"data": {<br/>"content": [<br/>{<br/>"keyword": String,<br/>"cnt": Long<br/>}<br/>],<br/>"pageable": {<br/>"pageNumber": Integer,<br/>"pageSize": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"offset": Long,<br/>"paged": Boolean,<br/>"unpaged": Boolean<br/>},<br/>"last": Boolean,<br/>"totalPages": Integer,<br/>"totalElements": Long,<br/>"first": Boolean,<br/>"size": Integer,<br/>"number": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"numberOfElements": Integer,<br/>"empty": Boolean<br/>},<br/>"timestamp": LocalDateTime<br/>}                                                                                                                                                  | 200 OK      | 없음                                                              |
| GET    | /api/v1/search/popularKeywords/categories | 인기 검색어 category별 조회     | 없음                                                                                                         | 없음           | {<br/>"success": Boolean,<br/>"message": String,<br/>"data": {<br/>"content": [<br/>{<br/>"keyword": String,<br/>"cnt": Long<br/>}<br/>],<br/>"pageable": {<br/>"pageNumber": Integer,<br/>"pageSize": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"offset": Long,<br/>"paged": Boolean,<br/>"unpaged": Boolean<br/>},<br/>"last": Boolean,<br/>"totalPages": Integer,<br/>"totalElements": Long,<br/>"first": Boolean,<br/>"size": Integer,<br/>"number": Integer,<br/>"sort": {<br/>"empty": Boolean,<br/>"sorted": Boolean,<br/>"unsorted": Boolean<br/>},<br/>"numberOfElements": Integer,<br/>"empty": Boolean<br/>},<br/>"timestamp": LocalDateTime<br/>}                                                                                                                                                  | 200 OK      | 없음                                                              |
| GET    | /api/v1/search/histories/popular/top10    | 내 검색기록 기반 도서 Top10      | 없음                                                                                                         | 없음           | {<br/>"success": Boolean,<br/>"message": String,<br/>"data": [<br/>{<br/>"id": Long,<br/>"title": String,<br/>"contributors": [<br/>{<br/>"id": Long,<br/>"name": String,<br/>"role": Enum<br/>}<br/>],<br/>"category": Enum,<br/>"createdAt": LocalDateTime<br/>}<br/>],<br/>"timestamp": LocalDateTime<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | 200 OK      | 401 UNAUTHORIZED , <br/>500 INTERNALSERVERERROR                 |
| GET    | /api/v2/search/histories/popular/top10    | 내 검색기록 기반 도서 Top10 (캐싱) | 없음                                                                                                         | 없음           | {<br/>"success": Boolean,<br/>"message": String,<br/>"data": [<br/>{<br/>"id": Long,<br/>"title": String,<br/>"contributors": [<br/>{<br/>"id": Long,<br/>"name": String,<br/>"role": Enum<br/>}<br/>],<br/>"category": Enum,<br/>"createdAt": LocalDateTime<br/>}<br/>],<br/>"timestamp": LocalDateTime<br/>}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | 200 OK      | 401 UNAUTHORIZED , <br/>500 INTERNALSERVERERROR                 |

    ERD
![img.png](img.png)
