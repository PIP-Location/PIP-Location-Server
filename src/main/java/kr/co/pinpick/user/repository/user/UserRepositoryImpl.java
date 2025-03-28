package kr.co.pinpick.user.repository.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.user.dto.request.UserRetrieveRequest;
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
    public List<User> retrieve(User author, UserRetrieveRequest request) {
        return queryFactory
                .selectFrom(user)
                .where(containingQ(request.getQ()), exceptMe(author), exceptBlock(author))
                .offset(request.getPageNo() * 20L)
                .limit(20)
                .fetch();
    }

    private BooleanExpression containingQ(String q) {
        return hasText(q) ? user.nickName.contains(q) : null;
    }

    private BooleanExpression exceptMe(User author) {
        return user.ne(author);
    }

    private BooleanExpression exceptBlock(User author) {
        return JPAExpressions
                .selectFrom(block1)
                .where(block1.author.eq(author))
                .where(block1.block.eq(user))
                .notExists();
    }
}
