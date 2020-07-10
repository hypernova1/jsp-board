package sam.board.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sam.board.domain.Post;
import sam.board.service.PostService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    PostService postService;

    @Test
    void test() {
        Post post = mock(Post.class);
        when(postService.selectAll()).thenReturn(Arrays.asList(new Post(), new Post()));
        when(post.getTitle()).thenReturn("effective java");
        when(post.getContent()).thenReturn("test");

        List<Post> posts = postService.selectAll();
        assertNotNull(post.getTitle());
        assertEquals(post.getContent(), "test");
        assertEquals(posts.size(), 2);
    }

}