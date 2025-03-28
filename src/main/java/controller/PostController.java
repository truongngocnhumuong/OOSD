package controller;

import dao.PostDAO;
import model.Post;
import utils.DatabaseConnection;

import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Named("postController")
@SessionScoped
public class PostController implements Serializable {
    private Post post = new Post();

    public String savePost() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO posts (title, body, status, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)",
                    Statement.RETURN_GENERATED_KEYS
            );

            // Đảm bảo status không null
            if (post.getStatus() == null || post.getStatus().trim().isEmpty()) {
                post.setStatus("PUBLISHED"); // Giá trị mặc định
            }

            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getBody());
            stmt.setString(3, post.getStatus()); // Chỉ còn 3 tham số

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return "dashboard.xhtml?faces-redirect=true"; // Quay lại dashboard
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM posts ORDER BY created_at DESC")) {
            while (rs.next()) {
                Post p = new Post();
                p.setId(rs.getInt("id"));
                p.setTitle(rs.getString("title"));
                p.setBody(rs.getString("body"));
                p.setUserId(rs.getInt("user_id"));
                p.setStatus(rs.getString("status"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                posts.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

