package services.categories_service;

import categories.CategoryDTO;
import categories.CategoryMapper;

import java.util.HashSet;
import java.util.Set;

public class CategoriesService
{
    private Set<Category> categoriesSet;
    public CategoriesService() {
        categoriesSet=new HashSet<Category>();
    }

    public CategoriesService(CategoriesService categoriesService) {
        categoriesSet=new HashSet<Category>();
        categoriesService.categoriesSet.forEach(category-> this.categoriesSet.add(new Category(category)));
    }

    public void addCategories(String name) throws Exception {
        if(categoriesSet.contains(name))
            throw new Exception("Category allready exist!");
        this.categoriesSet.add(new Category(name));}

    public Set<CategoryDTO> getAllCategoriesInfo() {
        Set<CategoryDTO> CategoriesDtoSet=new HashSet<>();
        categoriesSet.forEach(category -> CategoriesDtoSet.add(new CategoryMapper(category).mapToDTO()));
        return CategoriesDtoSet;
    }
}
