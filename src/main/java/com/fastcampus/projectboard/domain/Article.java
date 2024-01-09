package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")

})
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // setter를 전체 클래스 랩에 걸지 않는 이유는
    //일부러 사용자가 특정 필드에 접근한 세팅을 하지 못하게끔 막고 싶기 때문
    @Setter @Column(nullable = false) private String title;               //제목
    @Setter @Column(nullable = false, length = 10000) private String content;             //본문
    @Setter private String hashtag;             //해시태그

    @CreatedDate @Column(nullable = false)private LocalDateTime createdAt;    //생성일시
    @CreatedBy @Column(nullable = false, length = 100)private String createdBy;           //생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt;   //수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy;          //수정자

    protected Article(){}

    public Article(String title, String content, String hashtag) {

    }

    public static Article of(String title, String content, String hashtag) {
       return new Article(title,content,hashtag);
    }

    //리스트에 넣거나, 리스트에 있는 내용에서 중복 요소를 제거하거나
    //컬렉션에서 중복 요소를 제거, 혹은 정렬을 해야 할 때
    //  비교 를 할 수 있어야 한다.
    //동일성, 동등성 검사를 할 수 있는 equals, hashtag equals랑 hashcode를 구현해야 함
    // -> lombok을 이용하면 간단 ( 그러나 이번 강의에선
    // 독특한 방법으로 equals hashcode를 만들어보자 )


    //각 객체가 같은 객체인지 서로 비교하기 위해서
    //id 컬럼만 비교하면 된다. 왜냐? 다른 내용이 다르면 애초에 다른 객체이고
    //id 마저 같으면 그제서야 비로소 같은 객체이다.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Article article = (Article) o;
        if (!(o instanceof Article article)) return false; //patern variable

        return id != null &&id.equals(article.id); //아직 insert 하지 않은 값은 null 일 수 있다.
        //엔티티를 데이터베이스에 영속화 시키고, 연결짓고 사용하는 환경에서
        //서로 다른 두 로우, 두 엔티티가 같은 조건이 무엇인가에 대한 질문에
        // 지금 이 equals가 답을 한다.
        //id가 부여되지 않았따, 즉 영속화되지 않았다고 하면?
        // 동등성 검사 자체가 의미가 없는 것으로 보고
        // 다 다른 것으로 간주하거나 혹은 처리하지 않는다.
        //그래서 false 처리해버리고, 혹시 id가 있다면?
        // id가 같은 지만 보고, id가 같다면 당연히 두 객체는 같은 객체
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

//id, createdAt, createdBy등은 내가 값을 직접 수정하고나 입력하는게 아니라
// 영속성 컨텍스트 등에서 자동으로 만들어주게 된다. 클라이언트가 값을 수정하는 부분만 setter
// createdAt, createdBy, modifiedAt 등이 자동으로 설정되는건 오디팅 기능