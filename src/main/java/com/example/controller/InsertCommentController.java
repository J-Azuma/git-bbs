package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Comment;
import com.example.form.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;

@Controller
@RequestMapping("")
public class InsertCommentController {
	
	@Autowired
	private CommentRepository commentRepository;

	/**
	 * コメント投稿を行う.
	 * 
	 * @param form 投稿したいコメントの内容
	 * @return リダイレクト：掲示板画面
	 */
	@RequestMapping("insertComment")
	public String insertComment(@Validated CommentForm form, BindingResult result,Model model) {
		if (result.hasErrors()) {
			return new ShowBbsController().index(model);
		}
		Comment comment = new Comment();
		BeanUtils.copyProperties(form, comment);
		comment.setArticleId(Integer.parseInt(form.getArticleId()));
		commentRepository.insert(comment);
		return "redirect:/show-bbs";
	}
	
}
