package kr.co.pinpick.user.repository.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.common.dto.request.SearchRequest;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.co.pinpick.user.entity.QBlock.block1;
import static kr.co.pinpick.user.entity.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> search(User principal, SearchRequest request) {
        return queryFactory
                .selectFrom(user)
                .where(containingQ(request.getQ()), exceptMe(principal), exceptBlock(principal))
                .orderBy(user.nickname.asc())
                .limit(request.getLimit())
                .fetch();
    }

    private BooleanExpression containingQ(String q) {
        return hasText(q) ? user.nickname.contains(q) : null;
    }

    private BooleanExpression exceptMe(User principal) {
        return user.id.ne(principal.getId());
    }

    private BooleanExpression exceptBlock(User principal) {
        return JPAExpressions
                .selectFrom(block1)
                .where(block1.user.id.eq(principal.getId()))
                .where(block1.block.id.eq(user.id))
                .notExists();
    }
}
