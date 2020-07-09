package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;
import com.example.domain.Comment;

@Repository
public class ArticleRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	// ラムダ式によりextractメソッドを実装
	public ResultSetExtractor<List<Article>> exractData = (rs) -> {
		List<Article> articleList = new ArrayList<>();
		List<Comment> commentList = null;
		int tmp = 0;
		while (rs.next()) {
			if (rs.getInt("id") != tmp) {
				tmp = rs.getInt("id");
				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setName(rs.getString("name"));
				article.setContent(rs.getString("content"));

				commentList = new ArrayList<>();
				article.setCommentList(commentList);
				articleList.add(article);

			}
			if (rs.getInt("com_id") != 0) {
				Comment comment = new Comment();
				comment.setId(rs.getInt("com_id"));
				comment.setName(rs.getString("com_name"));
				comment.setContent(rs.getString("com_content"));
				comment.setArticleId(rs.getInt("article_id"));
				commentList.add(comment);
			}
		}
		return articleList;
	};

	/**
	 * 記事の全件検索を行う。テーブル結合してコメントも同時に取得する.
	 * 
	 * @return 全ての記事のリスト
	 */
	public List<Article> findAll() {
		String sql = "select a.id, a.name, a.content, c.id as com_id, c.name as com_name, c.content as com_content, article_id"
				+ " from articles as a left outer join comments as c on a.id = c.article_id"
				+ " order by a.id desc, com_id desc;";

		List<Article> articleList = template.query(sql, exractData);
		return articleList;
	}

	/**
	 * 記事の作成を行う.
	 * 
	 * @param article 作成する記事
	 */
	public void insert(Article article) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);
		String sql = "insert into articles(name, content) values(:name, :content);";
		template.update(sql, param);
	}

	/**
	 * 記事の削除を行う記事と紐づけられたコメントも一括削除する.
	 * 
	 * @param id 削除対象の記事id
	 */
	public void deleteById(int id) {
		String sql = "WITH deleted AS (DELETE FROM articles WHERE id = :id RETURNING id)"
				+ "DELETE FROM comments WHERE article_id IN (SELECT id FROM deleted)";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
}
