package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.controller.community.dto.request.PostRequestDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.domain.community.category.Category;
import eum.backed.server.domain.community.category.CategoryRepository;
import eum.backed.server.domain.community.post.Post;
import eum.backed.server.domain.community.post.PostRepository;
import eum.backed.server.domain.community.post.Status;
import eum.backed.server.domain.community.region.GU.Gu;
import eum.backed.server.domain.community.region.GU.GuRepository;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.scrap.ScrapRepository;
import eum.backed.server.domain.community.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final GuRepository guRepository;
    private final ScrapRepository scrapRepository;
    private final PostResponseDTO postResponseDTO;

    public DataResponse<Response> create(PostRequestDTO.Create create, Users user) throws Exception {
        Gu getGu = guRepository.findById(create.getGuId()).orElseThrow(() -> new IllegalArgumentException("없는 구입니다"));
        Category getCategory = categoryRepository.findById(create.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("없는 카테고리 입니다"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.KOREAN);
        Post post = Post.builder()
                .title(create.getTitle())
                .contents(create.getContent())
                .startDate(simpleDateFormat.parse(create.getStartTime()))
                .endDate(simpleDateFormat.parse(create.getEndTime()))
                .pay(create.getPay())
                .location(create.getLocation())
                .volunteerTime(create.getVolunteerTime())
                .needHelper(create.isNeedHelper())
                .maxNumOfPeople(create.getMaxNumOfPeople())
                .status(Status.RECRUITING)
                .user(user)
                .category(getCategory)
                .gu(getGu).build();
        postRepository.save(post);
        return new DataResponse<>(Response.class).success("게시글 작성 성공");
    }


    public DataResponse<Response> delete(Long postId,Users user) {
        Post getPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        postRepository.delete(getPost);
        return new DataResponse<>(Response.class).success("게시글 삭제 성공");
    }

    public DataResponse update(PostRequestDTO.Update update,Users user) {
        Post getPost = postRepository.findById(update.getPostId()).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getPost.updateTitle(update.getTitle());
        getPost.updateContents(update.getContent());
        postRepository.save(getPost);
        return new DataResponse<>(Response.class).success("게시글 수정 성공");

    }

    public DataResponse updateState(Long postId,Status status, Users user) {
        Post getPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getPost.updateStatus(status);
        postRepository.save(getPost);
        return new DataResponse<>(Response.class).success("게시글 상태 수정 성공");
    }
    public DataResponse<PostResponseDTO.PostResponse> findById(Long postId) {
        Post getPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(getPost);
        return new DataResponse<>(singlePostResponse).success(singlePostResponse, "postId로 찾기");

    }

    public DataResponse<List<PostResponseDTO.PostResponse>> findByCategory(Long categoryId) {
        Category getCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Invalid categoryId"));
        List<Post> posts = postRepository.findByCategoryOrderByCreateDateDesc(getCategory).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> findByCategories = getAllPostResponse(posts);
        return new DataResponse<>(findByCategories).success(findByCategories,"카테고리 별 데이터 조회 성공");
    }

    public DataResponse<List<PostResponseDTO.PostResponse>> findByStatus(Status status) {
        List<Post> posts = postRepository.findByStatusOrderByCreateDateDesc(status).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> findByCategories = getAllPostResponse(posts);
        return new DataResponse<>(findByCategories).success(findByCategories,"상태별 데이터 조회 성공");

    }
    public DataResponse<List<PostResponseDTO.PostResponse>> findByNeedHelper(Boolean needHelper) {
        List<Post> posts = postRepository.findByNeedHelperOrderByCreateDateDesc(needHelper).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> findByCategories = getAllPostResponse(posts);
        return new DataResponse<>(findByCategories).success(findByCategories,"도움주기, 받기 데이터 조회 성공");

    }

    public DataResponse<List<PostResponseDTO.PostResponse>> findByScrap(Users user) {
        List<Scrap> scraps = scrapRepository.findByUserOrderByCreateDateDesc(user).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> postResponseArrayList = new ArrayList<>();
        for (Scrap scrap : scraps) {
            Post post = scrap.getPost();
            PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(post);
            postResponseArrayList.add(singlePostResponse);
        }
        return new DataResponse<>(postResponseArrayList).success(postResponseArrayList, "내가 스크랩한 게시글 조회)");
    }
    private List<PostResponseDTO.PostResponse> getAllPostResponse(List<Post> posts){
        List<PostResponseDTO.PostResponse> postResponseArrayList = new ArrayList<>();
        for (Post post : posts) {
            PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(post);
            postResponseArrayList.add(singlePostResponse);
        }
        return postResponseArrayList;
    }


}
