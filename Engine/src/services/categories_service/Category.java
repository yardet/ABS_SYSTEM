package services.categories_service;

public class Category
{
    final private String name;
    public Category(String name){
        this.name=name;
    }

    public Category(Category category) {
        this.name = category.name;
    }

    public String getName(){return this.name;}
}

