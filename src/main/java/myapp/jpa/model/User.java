package myapp.jpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "user-with-posts",
                attributeNodes = {@NamedAttributeNode("posts")}
        ),
        @NamedEntityGraph(
                name = "user-with-comments",
                attributeNodes = {@NamedAttributeNode("comments")}
        ),
        @NamedEntityGraph(
                name = "user-with-posts-and-comments",
                attributeNodes = {
                        @NamedAttributeNode("posts"),
                        @NamedAttributeNode("comments")
                }
        )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}