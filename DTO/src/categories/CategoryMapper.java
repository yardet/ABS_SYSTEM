package categories;

import services.categories_service.Category;

public class CategoryMapper {
    Category category;
    CategoryDTO categoryDTO;


    public CategoryMapper(CategoryDTO categoryDTO) {
        this.category = null;
        this.categoryDTO = categoryDTO;
    }

    public CategoryMapper(Category category) {
        this.category = category;
        this.categoryDTO=null;
    }

    public CategoryDTO mapToDTO() {
        this.categoryDTO=new CategoryDTO(this.category.getName());
        return this.categoryDTO;
    }
}
