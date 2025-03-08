package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.CommentCollectResponse;
import kr.co.pinpick.archive.dto.CommentDetailResponse;
import kr.co.pinpick.archive.dto.CommentResponse;
import kr.co.pinpick.archive.dto.CreateCommentRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.archive.repository.ArchiveCommentRepository;
import kr.co.pinpick.common.dto.PaginateResponse;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArchiveCommentRepository archiveCommentRepository;

    @Override
    @Transactional
    public CommentResponse create(User author, Archive archive, CreateCommentRequest request, ArchiveComment parent) {
        var comment = ArchiveComment.builder()
                .author(author)
                .archive(archive)
                .parent(parent)
                .content(request.getContent())
                .build();

        return CommentResponse.fromEntity(archiveCommentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentCollectResponse get(Archive archive) {
        var comments = archiveCommentRepository.findByArchiveAndParentIsNull(archive);
        return CommentCollectResponse.builder()
                .collect(comments.stream().map(CommentResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(comments.size()).build())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDetailResponse find(ArchiveComment comment) {
        return CommentDetailResponse.fromEntity(comment);
    }
}
