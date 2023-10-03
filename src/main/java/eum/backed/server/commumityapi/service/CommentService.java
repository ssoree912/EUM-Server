package eum.backed.server.commumityapi.service;

import eum.backed.server.common.DataResponse;
import eum.backed.server.commumityapi.controller.dto.Response;
import eum.backed.server.commumityapi.controller.dto.request.CommentRequestDTO;
import eum.backed.server.commumityapi.domain.comment.Comment;
import eum.backed.server.commumityapi.domain.comment.CommentRepository;
import eum.backed.server.commumityapi.domain.post.Post;
import eum.backed.server.commumityapi.domain.post.PostRepository;
import eum.backed.server.commumityapi.domain.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public DataResponse create(CommentRequestDTO.Create create, Users user) {
        Post getPost = postRepository.findById(create.getPostId()).orElseThrow(() -> new IllegalArgumentException("Invalid postID"));
        Comment comment = Comment.builder()
                .content(create.getContent())
                .post(getPost)
                .user(user).build();
        commentRepository.save(comment);
        return new DataResponse<>(Response.class).success("댓글 작성 성공");
    }

    public DataResponse update(CommentRequestDTO.Update update, Users user) {
        Comment getComment = commentRepository.findById(update.getCommentId()).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
        if(user.getUserId() != getComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getComment.updateContent(update.getContent());
        commentRepository.save(getComment);
        return new DataResponse<>(Response.class).success("댓글 수정 성공");
    }

    public DataResponse delete(Long categoryId, Users user) {
        Comment getComment = commentRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
        if(user.getUserId() != getComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자)");
        commentRepository.delete(getComment);
        return new DataResponse<>(Response.class).success("댓글 삭제 성공");
    }
}