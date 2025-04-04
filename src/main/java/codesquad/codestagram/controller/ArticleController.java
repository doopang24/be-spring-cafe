package codesquad.codestagram.controller;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.exception.ArticleNotFoundException;
import codesquad.codestagram.exception.UserNotFoundException;
import codesquad.codestagram.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final String LOGIN_USER = "loginUser";

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<Article> articles = articleService.findAllArticle();
        model.addAttribute("articles", articles);
        return "qna/list";
    }

    @GetMapping("/qna/form")
    public String verifyMember(HttpSession session) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        return "qna/form";
    }

    @PostMapping("/questions")
    public String ask(
            @RequestParam("title") String title,
            @RequestParam("contents") String contents,
            HttpSession session) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        Article article = new Article(loginUser, title, contents);
        articleService.save(article);
        return "redirect:/";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if(loginUser == null) {
            return "redirect:/user/login";
        }
        Article article = articleService.findArticleWithReplies(id)
                .orElseThrow(() -> new ArticleNotFoundException());
//        if(article.isD)
        model.addAttribute("article", article);
        return "qna/show";
    }

    @GetMapping("/questions/{id}/form")
    public String showUpdateForm(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Article article = articleService.findOneArticle(id).get();
        User writer = article.getWriter();
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if(!writer.equals(loginUser)) {
            redirectAttributes.addFlashAttribute("error", "게시글의 작성자만 수정할 수 있습니다.");
            return "redirect:/articles/" + id;
        }
        model.addAttribute("article", article);
        return "qna/form";
    }

    @PutMapping("/questions/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String contents,
                         HttpSession session) {
        Article article = articleService.findOneArticle(id).get();
        User writer = article.getWriter();
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if(!writer.equals(loginUser)) {
            throw new UserNotFoundException(writer.getUserId());
        }
        articleService.update(id, title, contents);
        articleService.save(article);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/questions/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Article article = articleService.findOneArticle(id).get();
        User writer = article.getWriter();
        User loginUser = (User) session.getAttribute(LOGIN_USER);
        if(!writer.equals(loginUser)) {
            redirectAttributes.addFlashAttribute("error", "게시글의 작성자만 삭제할 수 있습니다.");
            return "redirect:/articles/" + id;
            // 게시글의 상세 페이지로 이동하기 위해 id를 붙여서 보낸다
        }
        articleService.deleteArticle(id);
        return "redirect:/";
    }
}
