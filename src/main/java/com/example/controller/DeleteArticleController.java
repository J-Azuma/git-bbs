package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.repository.ArticleRepository;

@Controller
@RequestMapping("/delete-article")
public class DeleteArticleController {
	@Autowired
	private ArticleRepository articleRepository;
	
	@RequestMapping("")
	public String deleteArticle(String articleId) {
		Integer deleteArticleId = Integer.parseInt(articleId);
		articleRepository.deleteById(deleteArticleId);
		return "redirect:/show-bbs";
	}
}
