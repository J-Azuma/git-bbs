package com.example.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.form.ArticleForm;
import com.example.form.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;

@Controller
@RequestMapping("/insertArticle")
public class InsertArticleController {
	@Autowired
	private ArticleRepository articleRepository;

	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}
	
	/**
	 * 記事を作成する.
	 * 
	 * @param form 入力値を受け取るためのフォームオブジェクト(フォームクラス名を明示したほうが良いかも)
	 * @return indexメソッドにリダイレクト
	 */
	@RequestMapping("")
	public String insertArticle(@Validated ArticleForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return new ShowBbsController().index(model);
		}
		Article article = new Article();
		BeanUtils.copyProperties(form, article);
		articleRepository.insert(article);
		return "redirect:/show-bbs";
	}

	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}

}
