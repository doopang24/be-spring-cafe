# be-spring-cafe

2025 마스터즈 백엔드 스프링 카페

[url](http://15.165.203.0:8080/)

```
http://15.165.203.0:8080/
```


### MvcMock

- Spring에서 웹 어플리케이션의 HTTP 요청/응답 흐름을 실제 브라우저 없이 테스트할 수 있도록 도와주는 도구
- 쉽게 말하자면 Spring 이 제공하는 가짜 웹 브라우저

### 외래키 Foreign key

외래키는 관계형 데이터베이스에서 두 테이블을 서로 연결해주는 열(column)이다.
어떤 테이블이 다른 테이블을 참조할 때 사용하는 키이다.
ARTICLES 테이블의 writer는 USER 테이블의 name을 참조하는 외래키

외래키가 중요한 이유
1. 테이블 간의 연관관계를 명확하게 하려고
2. 존재하지 않는 유저의 이름을 참조하지 못하게 하려고(참조 무결성)
3. JPA(ORM)가 객체 사이의 관계를 테이블로 바꿀 수 있도록 도와주려고

### @ManyToOne

여러 개의 엔티티가 하나의 엔티티를 참조한다.
- 여러 개의 article이 하나의 user에 의해 작성
- 여러 개의 주문이 하나의 고객에 속함

### @OneToMany

하나의 엔티티가 여러 개의 엔티티를 소유한다.
- 하나의 user가 여러 개의 article 을 작성한다.

### HiddenHttpMethodFilter

Spring MVC 는 기본적으로 GET, POST 만 제공하기 때문에, DELETE, PUT 등을 사용하고 싶으면 HiddenHttpMethodFilter 를 설정 파일에 등록해야 한다.


<br>

## Trouble Shooting

### boolean 컬럼은 NOT NULL 제약 조건이 걸려있다.

상황
: soft delete를 구현하기 위해 Article 클래스에 boolean 타입의 deleted 필드를 추가했다. 따라서 DB 는 기존에 있던 데이터에도 deleted 칼럼을 추가하려고 한다.
그런데 기존 데이터에는 deleted 칼럼이 없기 때문에 null 이 들어가게 된다. 그러나 boolean 컬럼이 NOT NULL 제약 조건이 걸려 있어서 오류가 발생한다.

해결
: 애플리케이션을 중지하고, H2 콘솔로 가서 수동으로 칼럼을 추가한다.
sql : ALTER TABLE articles ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;
