package pl.adamsiedlecki.otm.orchout;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class FacebookPublisherService {

    private final OrchOutApiClient orchOutApiClient;

    public String post(final File file, final String caption) {
        return orchOutApiClient.sendFacebookPost(caption, file).getPostId();
    }

    public String postComment(final String postId, final String comment) {
        return orchOutApiClient.sendFacebookPostComment(postId, comment).getCommentId();
    }

}
