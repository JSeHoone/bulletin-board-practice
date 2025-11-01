## 게시판 만들기 연습 (JSON)
원티드 포텐업 백엔드 1기 주말과제로 5개의 기능을 만들어 보기 위해서 만들었다.

해당 프로젝트는 MVC가 아닌 JSON으로 반환하는 RestController를 사용하여 구현한 프로젝트이다.
- HttpMessageConverter는 `@ResponseBody` 과 `@RequestBody`를 사용할 때 사용되는 인터페이스이다.
- `@RestController`내부에는 `@ResponseBody`Annotation이 존재하기 때문에 자동으로 HttpMessageConverter가 동작하게된다.
### Sequence Diagram
#### 1. 게시판 전체 조회
```mermaid
sequenceDiagram
    actor User
    participant Filter
    participant DispatcherServlet
    participant HandlerMapping
    participant HandlerAdapter

    box 직접 구현해야 하는 영역
        participant PostController
        participant PostService
        participant PostRepository
    end

    participant Database
    participant HttpMessageConverter

%% 1. 요청 시작
    User->>Filter: HTTP GET /posts
    Filter->>DispatcherServlet: doFilter(request, response)

%% 2. DispatcherServlet 처리 시작
    DispatcherServlet->>HandlerMapping: getHandler(request)
    HandlerMapping-->>DispatcherServlet: PostController.getPosts()

    DispatcherServlet->>HandlerAdapter: getHandlerAdapter(handler)
    HandlerAdapter-->>DispatcherServlet: HandlerAdapter

%% 3. Controller 실행
    DispatcherServlet->>HandlerAdapter: handle(request, response, handler)
    HandlerAdapter->>PostController: getPosts()

%% 4. Service 호출
    PostController->>PostService: showPosts()

%% 5. Repository → DB 조회
    PostService->>PostRepository: findAll()
    PostRepository->>Database: SELECT * FROM posts
    Database-->>PostRepository: List<PostEntity>
    PostRepository-->>PostService: List<PostEntity>

%% 6. DTO 변환
    PostService-->>PostController: List<PostResponseDTO>

%% 7. Controller → HandlerAdapter 반환
    PostController-->>HandlerAdapter: ResponseEntity<List<PostResponseDTO>>

%% 8. JSON 변환 (ViewResolver 없이)
    HandlerAdapter->>HttpMessageConverter: convert DTO → JSON
    HttpMessageConverter-->>DispatcherServlet: JSON String

%% 9. 최종 응답
    DispatcherServlet-->>User: HTTP 200 OK (JSON)
```


#### 2. 게시판 상세 조회
```mermaid
sequenceDiagram
    actor User
    participant Filter
    participant DispatcherServlet
    participant HandlerMapping
    participant HandlerAdapter

    box 직접 구현해야 하는 영역
        participant PostController
        participant PostService
        participant PostRepository
    end

    participant Database
    participant HttpMessageConverter

%% 1. 요청 시작
    User->>Filter: HTTP GET /posts/{postId}
    Filter->>DispatcherServlet: doFilter(request, response)

%% 2. DispatcherServlet 처리 시작
    DispatcherServlet->>HandlerMapping: getHandler(request)
    HandlerMapping-->>DispatcherServlet: PostController.getPost()

    DispatcherServlet->>HandlerAdapter: getHandlerAdapter(handler)
    HandlerAdapter-->>DispatcherServlet: HandlerAdapter

%% 3. Controller 실행
    DispatcherServlet->>HandlerAdapter: handle(request, response, handler)
    HandlerAdapter->>PostController: getPost(Long postId)

%% 4. Service 호출
    PostController->>PostService: showPostDetail(Long postId)

%% 5. Repository → DB 조회
    PostService->>PostRepository: findById(Long postId)
    PostRepository->>Database: SELECT * FROM posts WHERE id = ?
    Database-->>PostRepository: Optional<PostEntity>
    PostRepository-->>PostService: Optional<PostEntity>

%% 6. DTO 변환
    PostService-->>PostController: PostResponseDTO

%% 7. Controller → HandlerAdapter 반환
    PostController-->>HandlerAdapter: ResponseEntity<PostResponseDTO>

%% 8. JSON 변환 (ViewResolver 없이)
    HandlerAdapter->>HttpMessageConverter: convert DTO → JSON
    HttpMessageConverter-->>DispatcherServlet: JSON String

%% 9. 최종 응답
    DispatcherServlet-->>User: HTTP 200 OK (JSON)
```
- 게시판 상세 조회는 `PathVariable({postId})`를 받아서 조회한다. 

#### 3. 게시판 생성
```mermaid
sequenceDiagram
    actor User
    participant Filter
    participant DispatcherServlet
    participant HandlerMapping
    participant HandlerAdapter

    box 직접 구현하는 영역
        participant PostController
        participant PostService
        participant PostRepository
    end

    participant Database
    participant HttpMessageConverter

%% 1. 요청 시작
    User->>Filter: HTTP POST /posts (JSON Body)
    Filter->>DispatcherServlet: doFilter()

%% 2. Handler 탐색
    DispatcherServlet->>HandlerMapping: getHandler(request)
    HandlerMapping-->>DispatcherServlet: PostController.createPost()

    DispatcherServlet->>HandlerAdapter: getHandlerAdapter(handler)
    HandlerAdapter-->>DispatcherServlet: HandlerAdapter

%% 3. Controller 실행
    DispatcherServlet->>HandlerAdapter: handle()
    HandlerAdapter->>PostController: createPost(PostCreateRequest request)

%% 4. Service 호출
    PostController->>PostService: registerPost(PostCreateRequest request)

%% 5. Entity 생성 & 저장
    PostService->>PostService: new PostEntity(request.title, request.content)
    PostService->>PostRepository: save(PostEntity)
    PostRepository->>Database: INSERT INTO posts (title, content, create_at)
    Database-->>PostRepository: PostEntity
    PostRepository-->>PostService: PostEntity

%% 6. DTO 변환
    PostService-->>PostController: PostResponseDTO

%% 7. Controller 응답 반환
    PostController-->>HandlerAdapter: ResponseEntity<PostResponseDTO>

%% 8. JSON 변환
    HandlerAdapter->>HttpMessageConverter: convert DTO -> JSON
    HttpMessageConverter-->>DispatcherServlet: JSON Body

%% 9. 최종 응답
    DispatcherServlet-->>User: HTTP 201 Created (JSON)
```
- `POST` 메서드를 사용하여 HTTP Body에서 post를 생성하는데 필요한 데이터(title, content)를 받는다.
- 받아온 데이터를 **Service 계층에서 post 인스턴스를 생성**한 후, DB에 저장한다.

#### 4. 게시판 업데이트
```mermaid
sequenceDiagram
    actor User
    participant Filter
    participant DispatcherServlet
    participant HandlerMapping
    participant HandlerAdapter

    box 직접 구현해야 하는 영역
        participant PostController
        participant PostService
        participant PostRepository
    end

    participant Database
    participant HttpMessageConverter

%% 1. 요청 시작
    User->>Filter: HTTP PUT /posts/{postId}
    Filter->>DispatcherServlet: doFilter()

%% 2. Handler 찾기
    DispatcherServlet->>HandlerMapping: getHandler(request)
    HandlerMapping-->>DispatcherServlet: PostController.updatePost()

    DispatcherServlet->>HandlerAdapter: getHandlerAdapter(handler)
    HandlerAdapter-->>DispatcherServlet: HandlerAdapter

%% 3. Controller 실행
    DispatcherServlet->>HandlerAdapter: handle()
    HandlerAdapter->>PostController: updatePost(Long postId, PostUpdateRequest request)

%% 4. Service 호출
    PostController->>PostService: updatePost(Long postId, PostUpdateRequest request)

%% 5. 기존 데이터 조회
    PostService->>PostRepository: findById(postId)
    PostRepository->>Database: SELECT * FROM posts WHERE post_id = ?
    Database-->>PostRepository: PostEntity
    PostRepository-->>PostService: PostEntity

%% 6. 엔티티 수정 (Dirty Checking)
    PostService->>PostService: post.update(title, content)
    Note right of PostService: 트랜잭션 종료 시<br/>자동으로 UPDATE SQL 수행됨

%% 7. DTO 응답 반환
    PostService-->>PostController: PostResponseDTO

%% 8. Controller에서 반환
    PostController-->>HandlerAdapter: ResponseEntity<PostResponseDTO>

%% 9. JSON 컨버팅
    HandlerAdapter->>HttpMessageConverter: convert DTO -> JSON
    HttpMessageConverter-->>DispatcherServlet: JSON String

%% 10. 최종 응답
    DispatcherServlet-->>User: HTTP 200 OK (JSON)

```
- `PUT` 메서드는 **전체를 변경**하는 것이고 `PATCH`는 **부분을 업데이트** 하는 것이다.
- 여기서는 전체를 수정하는 것을 가정하고 **`PUT`메서드를 사용**해서 업데이트를 한다.
- Dirty Checking으로 post를 따로 save를 해주지 않아도 된다. 
- Body없이 성공만 알릴 때는 `204 No Content`를 사용하는 것이 더 좋다고 한다.

#### 5. 게시판 삭제
```mermaid
sequenceDiagram
    actor User
    participant Filter
    participant DispatcherServlet
    participant HandlerMapping
    participant HandlerAdapter

    box 직접 구현해야 하는 영역
        participant PostController
        participant PostService
        participant PostRepository
    end

    participant Database
    participant HttpMessageConverter

%% 1. 요청 시작
    User->>Filter: HTTP DELETE /posts/{postId}
    Filter->>DispatcherServlet: doFilter()

%% 2. Handler 조회
    DispatcherServlet->>HandlerMapping: getHandler(request)
    HandlerMapping-->>DispatcherServlet: PostController.deletePost()

    DispatcherServlet->>HandlerAdapter: getHandlerAdapter(handler)
    HandlerAdapter-->>DispatcherServlet: HandlerAdapter

%% 3. Controller 실행
    DispatcherServlet->>HandlerAdapter: handle()
    HandlerAdapter->>PostController: deletePost(Long postId)

%% 4. Service 호출 (조회 후 삭제)
    PostController->>PostService: removePost(Long postId)
    PostService->>PostRepository: findById(postId)
    PostRepository->>Database: SELECT * FROM posts WHERE id = ?
    Database-->>PostRepository: PostEntity or null
    PostRepository-->>PostService: PostEntity

%% 5. 존재 여부 판단
    PostService->>PostService: if(post == null) throw NotFoundException

%% 6. 삭제 실행
    PostService->>PostRepository: delete(postEntity)
    PostRepository->>Database: DELETE FROM posts WHERE id = ?
    Database-->>PostRepository: void
    PostRepository-->>PostService: void

%% 7. Controller 응답
    PostService-->>PostController: void
    PostController-->>HandlerAdapter: ResponseEntity.noContent()  // HTTP 204

%% 8. 최종 응답 (Body 없음)
    DispatcherServlet-->>User: HTTP 204 No Content
```
- `DELETE` 메서드를 사용하고, 어떤 post를 삭제할지 `PathVariable(postId)`를 통해서 post id를 받아온다.
- 삭제하기 전에 해당 post가 있는지 확인하고 **없으면 Exception**을 발생시킨다.
- repository에서 delete메서드는 응답값이 없으므로 **status code를 204로 반환하여 삭제가 완료**되었음을 알린다.  
