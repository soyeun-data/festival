package project.festival.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FestivalFilter {
    private String locationPrefix; //위치에 따른 필터
    private String date; // 날짜 정렬
    private String progress; // 종료된/진행 중/ 진행 예정 축제 필터
}
