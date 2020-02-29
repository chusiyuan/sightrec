package com.wlc.sightrec.controller;

import com.alibaba.fastjson.JSONObject;
import com.wlc.sightrec.entity.Comment;
import com.wlc.sightrec.service.CommentService;
import com.wlc.sightrec.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/comments"}, method = {RequestMethod.GET})
    public JSONObject getComments(@RequestParam("query") String query,
                                  @RequestParam("pageNum") int pageNum,
                                  @RequestParam("pageSize") int pageSize,
                                  HttpServletRequest request, HttpServletResponse response) {
        // token 验证没写
        // query 为搜索参数
        String auth = request.getHeader("Authorization");

        List<Comment> commentList;
        List<Comment> commentListByPage;
        JSONObject getComments = new JSONObject();
        int statusCode = response.getStatus();
        try {
            query = "%" + query + "%";
            // 模糊搜索，返回所有符合条件的评论
            commentList = commentService.getCommentsByContent(query);
            int commentNum = commentList.size();
            // 根据 页码 和 每页评论数 分页
            int totalPage = (int) Math.ceil(commentNum * 1.0 / pageSize);
            int offset = pageSize * (pageNum - 1);
            commentListByPage = commentList.subList(offset, Math.min(commentNum, offset + pageSize));
            getComments.put("data", JsonUtil.getCommentData(totalPage, pageNum, commentNum, commentListByPage));
            getComments.put("meta", JsonUtil.getMeta("获取评论列表成功", statusCode));
            return getComments;
        } catch (Exception e) {
            getComments.put("data", null);
            getComments.put("meta", JsonUtil.getMeta("获取评论列表失败", statusCode));
            return getComments;
        }
    }

    @RequestMapping(path = {"/comments"}, method = {RequestMethod.POST})
    public JSONObject addComment(@RequestBody Comment comment,
                                 HttpServletRequest request, HttpServletResponse response) {
        // token 验证没写
        String auth = request.getHeader("Authorization");

        JSONObject addComment = new JSONObject();
        int statusCode = response.getStatus();
        try {
            commentService.addComment(comment);
            addComment.put("data", comment);
            addComment.put("meta", JsonUtil.getMeta("添加评论成功", statusCode));
            return addComment;
        } catch (Exception e) {
            addComment.put("data", null);
            addComment.put("meta", JsonUtil.getMeta("添加评论成功", statusCode));
            return addComment;
        }
    }

    @RequestMapping(path = {"/comments/{id}"}, method = {RequestMethod.GET})
    public JSONObject getComment(@PathVariable("id") int id,
                                 HttpServletRequest request, HttpServletResponse response) {
        // token 验证没写
        String auth = request.getHeader("Authorization");

        JSONObject getComment = new JSONObject();
        int statusCode = response.getStatus();
        try {
            Comment commentById = commentService.getCommentById(id);
            getComment.put("data", commentById);
            getComment.put("meta", JsonUtil.getMeta("获取评论成功", statusCode));
            return getComment;
        } catch (Exception e) {
            getComment.put("data", null);
            getComment.put("meta", JsonUtil.getMeta("获取评论失败", statusCode));
            return getComment;
        }
    }

    @RequestMapping(path = {"/comments/{id}"}, method = {RequestMethod.PUT})
    public JSONObject modifyComment(@PathVariable("id") int id, @RequestBody JSONObject comment,
                                    HttpServletRequest request, HttpServletResponse response) {
        // token 验证没写
        String auth = request.getHeader("Authorization");

        JSONObject modifyComment = new JSONObject();
        int statusCode = response.getStatus();
        try {
            commentService.modifyComment(id, comment.getString("content"));
            modifyComment.put("data", null);
            modifyComment.put("meta", JsonUtil.getMeta("修改评论成功", statusCode));
            return modifyComment;
        } catch (Exception e) {
            modifyComment.put("data", null);
            modifyComment.put("meta", JsonUtil.getMeta("修改评论失败：状态码异常", statusCode));
            return modifyComment;
        }
    }

    @RequestMapping(path = {"/comments/{id}"}, method = {RequestMethod.DELETE})
    public JSONObject deleteComment(@PathVariable("id") int id,
                                    HttpServletRequest request, HttpServletResponse response) {
        // token 验证没写
        String auth = request.getHeader("Authorization");

        JSONObject deleteComment = new JSONObject();
        int statusCode = response.getStatus();
        try {
            commentService.deleteComment(id);
            deleteComment.put("data", null);
            deleteComment.put("meta", JsonUtil.getMeta("删除评论成功", statusCode));
            return deleteComment;
        } catch (Exception e) {
            deleteComment.put("data", null);
            deleteComment.put("meta", JsonUtil.getMeta("删除评论失败：状态码异常", statusCode));
            return deleteComment;
        }
    }
}