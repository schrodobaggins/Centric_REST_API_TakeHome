package REST_API_TakeHome.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue()
    private UUID id;
    private String name;
    private String description;
    private String brand;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "id"))
    private List<String> tags;
    private String category;

    @Column(name = "created_at")
    private String createdAt;

    public Product() {
    }

    public Product(UUID id,
                   String name,
                   String description,
                   String brand,
                   List<String> tags,
                   String category,
                   String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.tags = tags;
        this.category = category;
        this.createdAt = createdAt;
    }
}