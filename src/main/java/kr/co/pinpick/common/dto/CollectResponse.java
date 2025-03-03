package kr.co.pinpick.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class CollectResponse<T> {
    List<T> collect;
    PaginateResponse meta;
}
