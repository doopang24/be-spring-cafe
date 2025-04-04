package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.exception.ArticleNotFoundException;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReplyController {

    private final ReplyService replyService;
    private final ArticleService articleService;
    private final String LOGIN_USER = "loginUser";

    public ReplyController(ReplyService replyService, ArticleService articleService) {
        this.replyService = replyService;
        this.articleService = articleService;
    }

    @PostMapping("/questions/{id}/answers")
    public String createReply(@PathVariable Long id,
                              @RequestParam String contents,
                              HttpSession session) {
        Article article = articleService.findOneArticle(id)
                .orElseThrow(() -> new ArticleNotFoundException());
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        Reply reply = new Reply(loginUser, article, contents);

        replyService.save(reply);
        article.addReply(reply);
        return "redirect:/articles/" + id;
    }

    @PutMapping("/questions/{articleId}/answers/{replyId}")
    public String updateReply(@PathVariable Long articleId,
                              @PathVariable Long replyId,
                              @RequestParam String contents,
                              HttpSession session) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if(loginUser == null) {
            return "redirect:/user/login";
        }
        replyService.updateReply(replyId, contents, loginUser);
        return "redirect:/articles/" + articleId;
    }

    // 로그인한 사용자와 댓글 작성자가 같으면 삭제할 수 있다
    @DeleteMapping("/questions/{id}/answers/{answerId}")
    public String deleteReply(@PathVariable("id") Long articleId,
                              @PathVariable("answerId") Long replyId,
                              HttpSession session) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if(loginUser == null) {
            return "redirect:/user/login";
        }
        Reply reply = replyService.findOneReply(replyId).get();
        replyService.deleteReply(replyId, loginUser.getId());
        return "redirect:/articles/" + articleId;
    }
}
